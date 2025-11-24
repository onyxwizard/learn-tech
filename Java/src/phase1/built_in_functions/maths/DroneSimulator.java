package phase1.built_in_functions.maths;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Drone Telemetry & Control Simulator — Demonstrating High-Utility {@link Math} Methods
 * 
 * <p><b>Design Philosophy:</b>
 * This class models a lightweight autonomous drone control loop. It intentionally uses
 * {@code java.lang.Math} methods where they offer correctness, performance, or clarity
 * over hand-rolled alternatives — especially for floating-point edge cases.
 * 
 * <p><b>Key Principles Illustrated:</b>
 * <ul>
 *   <li>✅ <i>Defensive numerics</i> (avoid overflow, preserve sign, handle ±0.0)</li>
 *   <li>✅ <i>Domain-appropriate units</i> (radians vs degrees, dB, meters)</li>
 *   <li>✅ <i>Separation of concerns</i>: physics model vs UI rounding vs safety checks</li>
 *   <li>⚠️ <i>Anti-pattern callouts</i>: where {@code Math} is <i>not</i> the right tool</li>
 * </ul>
 */
public class DroneSimulator {

    public static void main(String[] args) {
        // ───────────────────────────────────────────────────────────────────────────────
        // Initial state (SI units unless noted)
        // ───────────────────────────────────────────────────────────────────────────────
        double x = 10.3;        // meters — East offset from origin
        double y = -7.8;        // meters — North offset (negative = South)
        double batteryLevel = 0.85;  // [0.0, 1.0] — normalized state-of-charge
        double altitude = 120.7;     // meters AGL (Above Ground Level)
        double targetAltitude = 100.0; // meters — desired cruise altitude
        double windSpeed = -3.2;     // m/s — positive = tailwind (same dir as travel)
        double sensorNoise = 1e-5;   // unitless — represents ADC quantization noise

        // ───────────────────────────────────────────────────────────────────────────────
        // 1. Euclidean Distance — SAFER than manual sqrt(x² + y²)
        // ───────────────────────────────────────────────────────────────────────────────
        /* 
         * Math.hypot(double x, double y)
         * 
         * Purpose: Compute √(x² + y²) without intermediate overflow/underflow.
         * Params: 
         *   - x: first component (double)
         *   - y: second component (double)
         * Returns: 
         *   - √(x² + y²), correctly rounded; special cases:
         *       hypot(±∞, y) → +∞
         *       hypot(NaN, y) → NaN
         *       hypot(±0.0, ±0.0) → +0.0
         * Why use it? 
         *   Manual: Math.sqrt(x*x + y*y) may overflow if x or y is large (~1e154).
         *   Example: x=1e200, y=1e200 → x*x = ∞, but hypot(x,y)=~1.414e200 (finite).
         * Industry use: GPS distance, vector norms, physics engines.
         */
        double horizontalDistance = Math.hypot(x, y);
        System.out.printf("✅ Distance from base: %.3f m%n", horizontalDistance); // ~13.0 m

        // ───────────────────────────────────────────────────────────────────────────────
        // 2. Value Clamping — Enforce operational bounds
        // ───────────────────────────────────────────────────────────────────────────────
        /* 
         * Math.min(double a, double b) & Math.max(double a, double b)
         * 
         * Purpose: Return lesser/greater of two values. Overloaded for int/long/float/double.
         * Params: a, b — values to compare (same type)
         * Returns: 
         *   - min: smaller value (or a if a==b)
         *   - max: larger value (or a if a==b)
         * Special cases: NaN propagates (min(NaN, 5) → NaN)
         * Why use it?
         *   Idiomatic, branch-free (often optimized to cmov on modern CPUs), null-safe.
         *   Avoids verbose if-else chains: altitude = Math.max(MIN_ALT, Math.min(MAX_ALT, alt));
         * Industry use: config validation, UI slider bounds, sensor saturation.
         */
        final double MIN_SAFE_ALTITUDE = 10.0;  // meters — avoid ground collision
        final double MAX_SAFE_ALTITUDE = 150.0; // meters — regulatory limit
        altitude = Math.max(MIN_SAFE_ALTITUDE, Math.min(MAX_SAFE_ALTITUDE, altitude));
        System.out.printf("✅ Altitude clamped to: %.1f m%n", altitude); // remains 120.7

        // ───────────────────────────────────────────────────────────────────────────────
        // 3. Rounding for Human-Readable Telemetry (UI layer)
        // ───────────────────────────────────────────────────────────────────────────────
        /* 
         * Math.round(double a) → long
         * Math.round(float a) → int
         * 
         * Purpose: Round to nearest integer using "round half up" (ties → +∞).
         * Params: a — value to round
         * Returns:
         *   - (long)Math.floor(a + 0.5d) for doubles
         *   - Special: round(±0.5) → +0, round(±∞) → ±∞, round(NaN) → 0 (!)
         * ⚠️ Warning:
         *   - NOT suitable for financial rounding (use BigDecimal.setScale() + RoundingMode)
         *   - For display, prefer String.format("%.0f") to avoid long→int cast issues.
         * Industry use: logging, dashboards, non-critical UI.
         */
        long altitudeRounded = Math.round(altitude); // 121L
        int batteryPercent = Math.round((float)(batteryLevel * 100.0f)); // 85
        System.out.println("📊 Display telemetry | Alt: " + altitudeRounded + " m | Bat: " + batteryPercent + "%");

        // ───────────────────────────────────────────────────────────────────────────────
        // 4. Heading Calculation — Robust angle via atan2
        // ───────────────────────────────────────────────────────────────────────────────
        /* 
         * Math.atan2(double y, double x)
         * 
         * Purpose: Compute angle θ of point (x, y) in polar coords (θ = arctan(y/x)).
         * Params:
         *   - y: ordinate (e.g., North offset)
         *   - x: abscissa (e.g., East offset)
         * Returns: 
         *   - θ ∈ [-π, +π] radians.
         *   - Special: atan2(±0.0, -0.0) = ±π; atan2(±0.0, +0.0) = ±0.0
         * Why use it over Math.atan(y/x)?
         *   ✅ Handles x=0 (no div-by-zero)
         *   ✅ Correct quadrant (e.g., (-1,-1) → -135°, not +45°)
         *   ✅ IEEE 754 compliant sign handling for ±0.0
         * Industry use: navigation, computer graphics, robotics.
         */
        double headingRadians = Math.atan2(y, x);  // y first! (North, East) → (y, x)
        double headingDegrees = Math.toDegrees(headingRadians);
        System.out.printf("🧭 Heading: %.2f° (from +East axis)%n", headingDegrees); // ~ -37.1°

        /* 
         * Math.toDegrees(double angRad)
         * 
         * Purpose: Convert radians → degrees. Exact inverse of toRadians.
         * Params: angRad — angle in radians
         * Returns: angRad × 180 / π
         * Precision: ~1 ULP error — sufficient for UI/display.
         * Note: Not for iterative trig (accumulate error); keep internal calcs in radians.
         */

        // ───────────────────────────────────────────────────────────────────────────────
        // 5. Exponential Battery Decay Model
        // ───────────────────────────────────────────────────────────────────────────────
        /* 
         * Math.exp(double a)
         * 
         * Purpose: Compute e^a (Euler's number raised to power a).
         * Params: a — exponent
         * Returns: e^a; special: exp(±∞) → ±∞, exp(NaN) → NaN
         * Model: Battery(t) = Battery₀ × e^(-k·t)  (first-order decay)
         * Why not Math.pow(Math.E, a)? 
         *   - exp() is faster (hardware-optimized) and more accurate.
         *   - Avoids Math.E inaccuracy (Math.E is only ~16-digit precise).
         * Industry use: decay models, growth, probability (e.g., softmax).
         */
        double flightTimeHours = 0.5; // 30 minutes
        double decayConstant = 0.2;   // per hour (empirical)
        batteryLevel *= Math.exp(-decayConstant * flightTimeHours);
        System.out.printf("🔋 Battery after %.1f h: %.2f%%%n", flightTimeHours, batteryLevel * 100.0);

        // ───────────────────────────────────────────────────────────────────────────────
        // 6. Signal Strength in Decibels (logarithmic scale)
        // ───────────────────────────────────────────────────────────────────────────────
        /* 
         * Math.log10(double a)
         * 
         * Purpose: Base-10 logarithm. Used in engineering scales (dB, pH, Richter).
         * Params: a — positive value (a > 0)
         * Returns: log₁₀(a); special: log10(0) → -∞, log10(<0) → NaN
         * Formula: dB = 10·log₁₀(P / P₀)
         * Why not Math.log(a)/Math.log(10)? 
         *   - log10() is faster and more accurate (avoids division error).
         * Industry use: RF engineering, acoustics, data normalization.
         */
        double receivedPower = 5e-3; // 5 mW
        double referencePower = 1e-3; // 1 mW (0 dBm reference)
        double signalDb = 10.0 * Math.log10(receivedPower / referencePower); // ~6.99 dBm
        System.out.printf("📶 Signal: %.2f dBm%n", signalDb);

        // ───────────────────────────────────────────────────────────────────────────────
        // 7. Wind Compensation — Preserve sign while adjusting magnitude
        // ───────────────────────────────────────────────────────────────────────────────
        /* 
         * Math.copySign(double magnitude, double sign)
         * 
         * Purpose: Return magnitude with sign of sign argument.
         * Params:
         *   - magnitude: absolute value to return
         *   - sign: value whose sign bit is copied
         * Returns: 
         *   - |magnitude| × signbit(sign) 
         *   - Handles ±0.0, ±∞, NaN sign bit correctly per IEEE 754.
         * Why not (sign < 0 ? -magnitude : magnitude)?
         *   ✅ Handles -0.0 (critical in physics: velocity direction)
         *   ✅ No branches → faster on pipelined CPUs
         *   ✅ NaN sign propagation for debugging
         * Industry use: physics engines, control systems, financial delta adjustments.
         */
        double baseThrust = 4.0; // m/s²
        double windCompensation = 0.5; // m/s² — additive correction
        double thrust = baseThrust + Math.copySign(windCompensation, windSpeed);
        System.out.printf("💨 Thrust (wind-comp): %.2f m/s² | Wind: %.1f m/s%n", thrust, windSpeed);

        // ───────────────────────────────────────────────────────────────────────────────
        // 8. Discretization for Grid-Based Path Planning
        // ───────────────────────────────────────────────────────────────────────────────
        /* 
         * Math.floor(double a) & Math.ceil(double a)
         * 
         * Purpose:
         *   - floor(a): largest double ≤ a that's an integer (toward -∞)
         *   - ceil(a): smallest double ≥ a that's an integer (toward +∞)
         * Params: a — value to bound
         * Returns: mathematical integer as double (e.g., floor(2.9) → 2.0)
         * Edge: floor(-2.1) = -3.0 (not -2.0!)
         * Industry use: spatial indexing, histogram binning, LOD selection.
         */
        double gridCellHeight = 10.0; // meters per altitude layer
        int currentGridLayer = (int) Math.floor(altitude / gridCellHeight); // 12
        double nextLayerCeiling = Math.ceil(altitude / gridCellHeight) * gridCellHeight; // 130.0
        System.out.printf("🗺️ Grid layer: %d | Next layer at: %.0f m%n", currentGridLayer, nextLayerCeiling);

        // ───────────────────────────────────────────────────────────────────────────────
        // 9. Direction Detection — Signum for state machines
        // ───────────────────────────────────────────────────────────────────────────────
        /* 
         * Math.signum(double d)
         * 
         * Purpose: Extract sign of value as -1.0, 0.0, or +1.0.
         * Params: d — value
         * Returns:
         *   - +1.0 if d > 0
         *   - -1.0 if d < 0
         *   - +0.0 if d == +0.0
         *   - -0.0 if d == -0.0
         *   - NaN if d is NaN
         * Why not d > 0 ? 1 : (d < 0 ? -1 : 0)?
         *   ✅ Preserves signed zero (relevant in angular velocity)
         *   ✅ NaN-safe (avoids silent logic errors)
         * Industry use: control logic, finite state machines, PID controllers.
         */
        double altitudeDelta = targetAltitude - altitude; // -20.7 → descending
        double climbDirection = Math.signum(altitudeDelta); // -1.0
        String directionStr = climbDirection > 0 ? "⬆ climb" : (climbDirection < 0 ? "⬇ descend" : "→ hover");
        System.out.println("🎯 Vertical command: " + directionStr);

        // ───────────────────────────────────────────────────────────────────────────────
        // 10. Cube Root — Correct handling of negative inputs
        // ───────────────────────────────────────────────────────────────────────────────
        /* 
         * Math.cbrt(double a)
         * 
         * Purpose: Real cube root (∛a). Defined for all real a.
         * Params: a — radicand
         * Returns: 
         *   - ∛a (e.g., cbrt(-8) = -2.0)
         *   - Preserves sign; monotonic and odd function.
         * Why not Math.pow(a, 1.0/3.0)?
         *   ✅ pow(-8, 1/3.0) → NaN (due to complex result in pow)
         *   ✅ cbrt is faster and more accurate for cube roots.
         * Industry use: volume normalization, stress tensors, color space conversion.
         */
        double pressureSensorRaw = -125.0; // hypothetical negative bias
        double pressureNormalized = Math.cbrt(pressureSensorRaw); // -5.0
        System.out.printf("🌬️ Pressure (cbrt): %.1f units%n", pressureNormalized);

        // ───────────────────────────────────────────────────────────────────────────────
        // 11. Efficient Scaling by Powers of Two — DSP-style
        // ───────────────────────────────────────────────────────────────────────────────
        /* 
         * Math.scalb(double d, int scaleFactor)
         * 
         * Purpose: Scale d by 2^scaleFactor: d × 2^scaleFactor.
         * Params:
         *   - d: value to scale
         *   - scaleFactor: exponent (int, typically -1022 to +1023)
         * Returns: d × 2^scaleFactor, rounded as if by single FP multiply.
         * Why use it?
         *   ✅ Faster & more accurate than Math.pow(d, 2^k) or d * (1 << k) (avoids int overflow)
         *   ✅ Used in FFTs, fixed-point emulation, sensor gain stages.
         * Example: ADC gain = 16 → scalb(raw, 4)
         */
        double adcReading = 1.23456789;
        double amplifiedReading = Math.scalb(adcReading, 4); // ×16 → 19.753...
        System.out.printf("📡 ADC (×16 gain): %.6f%n", amplifiedReading);

        // ───────────────────────────────────────────────────────────────────────────────
        // 12. Floating-Point Tolerance Testing — For verification systems
        // ───────────────────────────────────────────────────────────────────────────────
        /* 
         * Math.ulp(double d)
         * 
         * Purpose: Unit in Last Place — smallest representable increment at magnitude |d|.
         * Params: d — value
         * Returns: 
         *   - Distance between d and next larger double.
         *   - ulp(1.0) = 2⁻⁵² ≈ 2.22e-16
         *   - ulp(100.0) = 2⁻⁴⁶ ≈ 1.42e-14 (scales with magnitude!)
         * Why use it for comparisons?
         *   ✅ Adaptive tolerance: abs(a-b) ≤ 2 * ulp(a) → "nearly equal"
         *   ✅ Better than fixed epsilon (e.g., 1e-9 fails for large numbers).
         * Industry use: numerical library testing, scientific validation.
         */
        double expectedWaypoint = 100.0;
        double computedWaypoint = expectedWaypoint + Math.ulp(expectedWaypoint); // 1 ULP off
        boolean isInTolerance = Math.abs(computedWaypoint - expectedWaypoint) <= 2.0 * Math.ulp(expectedWaypoint);
        System.out.println("✅ Waypoint tolerance test passed: " + isInTolerance);

        // ───────────────────────────────────────────────────────────────────────────────
        // 13. Round to Nearest Integer — as Double (for JSON/APIs)
        // ───────────────────────────────────────────────────────────────────────────────
        /* 
         * Math.rint(double a)
         * 
         * Purpose: Round to nearest integer *value*, returned as double.
         * Params: a — value
         * Returns: 
         *   - Nearest double that’s a mathematical integer.
         *   - Ties → even integer (IEEE 754 roundTiesToEven)
         *   - e.g., rint(2.5) → 2.0, rint(3.5) → 4.0
         * Why not round()? 
         *   - rint preserves double type (avoids long→double cast)
         *   - Uses round-to-even (reduces bias in repeated rounding)
         * Industry use: scientific data export (NetCDF, HDF5), signal processing.
         */
        double gpsCoordinate = 10.49999999999999;
        double snappedCoordinate = Math.rint(gpsCoordinate); // 10.0 (not 11.0!)
        System.out.printf("📍 Snapped GPS coord: %.1f%n", snappedCoordinate);
    }
}