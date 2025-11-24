package phase1.built_in_functions.wrapper;

import java.util.*;

/**
 * Character Wrapper Deep Dive — Beyond ASCII, Into Unicode Reality
 * 
 * <p>
 * Key truths often missed:
 * <ul>
 * <li>❌ {@code char} is NOT a Unicode character — it's a UTF-16 <i>code
 * unit</i>.</li>
 * <li>✅ Supplementary characters (e.g., emojis 🎉, historic scripts) require
 * <b>two</b> chars (surrogate pairs).</li>
 * <li>⚠️ Case conversion is <b>not</b> 1:1: 'ß' → 'SS', 'İ' (Turkish) ≠ 'I' +
 * dot.</li>
 * <li>✅ {@code Character} methods are Unicode-aware (Unicode 15.1 in Java
 * 21).</li>
 * </ul>
 * 
 * <p>
 * This demo covers:
 * <ul>
 * <li>✅ Core contract ({@code charValue}, {@code equals},
 * {@code compareTo})</li>
 * <li>✅ Unicode-aware predicates ({@code isLetter}, {@code isWhitespace},
 * etc.)</li>
 * <li>✅ Surrogate pairs & code points (handling emojis correctly)</li>
 * <li>✅ Case conversion pitfalls (locale, reversibility)</li>
 * <li>✅ Digit parsing & radix support</li>
 * <li>✅ Real-world patterns (input validation, sanitization, i18n)</li>
 * </ul>
 */
public class CharacterWrapperDeepDive {

  public static void main(String[] args) {
    System.out.println("=".repeat(80));
    System.out.println("CHARACTER: When 'char' Isn't Enough — Unicode in Practice");
    System.out.println("=".repeat(80));

    // ───────────────────────────────────────────────────────────────────────────────
    // 1. INSTANTIATION & CORE CONTRACT
    // ───────────────────────────────────────────────────────────────────────────────
    demoCoreContract();

    // ───────────────────────────────────────────────────────────────────────────────
    // 2. UNICODE CATEGORIES — Not Just "Letter" or "Digit"
    // ───────────────────────────────────────────────────────────────────────────────
    demoUnicodeCategories();

    // ───────────────────────────────────────────────────────────────────────────────
    // 3. SURROGATE PAIRS & CODE POINTS — Handling Emojis & Historic Scripts
    // ───────────────────────────────────────────────────────────────────────────────
    demoSurrogatesAndCodePoints();

    // ───────────────────────────────────────────────────────────────────────────────
    // 4. CASE CONVERSION — The Hidden Complexity
    // ───────────────────────────────────────────────────────────────────────────────
    demoCaseConversion();

    // ───────────────────────────────────────────────────────────────────────────────
    // 5. DIGIT & RADIX OPERATIONS — Parsing Numbers in Any Base
    // ───────────────────────────────────────────────────────────────────────────────
    demoDigitOperations();

    // ───────────────────────────────────────────────────────────────────────────────
    // 6. REAL-WORLD PATTERNS & BEST PRACTICES
    // ───────────────────────────────────────────────────────────────────────────────
    printBestPractices();
  }

  // ───────────────────────────────────────────────────────────────────────────────────
  // 1. CORE CONTRACT: boxing, equals, compareTo, hashCode
  // ───────────────────────────────────────────────────────────────────────────────────
  private static void demoCoreContract() {
    System.out.println("\n🔹 1. Core Contract — Simple, but with nuance");

    // ✅ Autoboxing uses valueOf() — cached for performance (but no universal cache
    // like Boolean)
    Character c1 = 'A'; // → Character.valueOf('A')
    Character c2 = Character.valueOf('A');
    System.out.println("  'A' autoboxed == valueOf('A')? " + (c1 == c2)); // true (JVM may cache small chars)

    // ❌ Avoid 'new Character()' — deprecated, wasteful
    @SuppressWarnings("deprecation")
    Character bad = new Character('A'); // Don't do this!

    // 📌 charValue() — unboxing
    char ch = c1.charValue(); // 'A'
    System.out.println("  charValue(): '" + ch + "'");

    // 📌 equals() — value-based, null-safe
    System.out.println("  'A'.equals('A'): " + c1.equals(c2)); // true
    System.out.println("  'A'.equals('B'): " + c1.equals('B')); // false (autounboxes 'B')
    System.out.println("  'A'.equals(null): " + c1.equals(null)); // false

    // 📌 compareTo() — numeric (by char value)
    System.out.println("  'A'.compareTo('B'): " + c1.compareTo('B')); // -1

    // 📌 hashCode() — based on char value
    System.out.println("  'A'.hashCode(): " + c1.hashCode()); // 65
  }

  // ───────────────────────────────────────────────────────────────────────────────────
  // 2. UNICODE CATEGORIES — It's not just ASCII!
  // ───────────────────────────────────────────────────────────────────────────────────
  private static void demoUnicodeCategories() {
    System.out.println("\n🔹 2. Unicode Categories — Beyond ASCII");

    char[] testChars = {
        'A', // Uppercase Latin
        'é', // Latin small e with acute (Ll)
        '€', // Currency symbol (Sc)
        '5', // Digit (Nd)
        '²', // Superscript two (No — other number)
        ' ', // Space (Zs)
        '\t', // Tab (Cc — control)
        '–', // En dash (Pd)
        '∑', // Sigma (Sm — math symbol)
        'ﷺ', // Arabic blessing (Cf — format)
        ' ', // Em space (Zs)
        'α', // Greek alpha (Ll)
        'Ω' // Greek omega (Lu)
    };

    System.out.printf("%-5s | %-8s | %-8s | %-8s | %-8s | %-8s | Category%n",
        "Char", "isLetter", "isDigit", "isSpace", "isISO", "isJavaID");
    System.out.println("-".repeat(70));

    for (char c : testChars) {
      String repr = c == ' ' ? "' '" : c == '\t' ? "'\\t'" : String.valueOf(c);
      System.out.printf("%-5s | %-8s | %-8s | %-8s | %-8s | %-8s | %s%n",
          repr,
          Character.isLetter(c),
          Character.isDigit(c),
          Character.isWhitespace(c),
          Character.isISOControl(c),
          Character.isJavaIdentifierStart(c),
          getCategoryName(c));
    }
  }

  // Helper: Map getType() to human-readable category
  private static String getCategoryName(char c) {
    int type = Character.getType(c);
    return switch (type) {
      case Character.UPPERCASE_LETTER -> "Lu";
      case Character.LOWERCASE_LETTER -> "Ll";
      case Character.TITLECASE_LETTER -> "Lt";
      case Character.MODIFIER_LETTER -> "Lm";
      case Character.OTHER_LETTER -> "Lo";
      case Character.NON_SPACING_MARK -> "Mn";
      case Character.ENCLOSING_MARK -> "Me";
      case Character.COMBINING_SPACING_MARK -> "Mc";
      case Character.DECIMAL_DIGIT_NUMBER -> "Nd";
      case Character.LETTER_NUMBER -> "Nl";
      case Character.OTHER_NUMBER -> "No";
      case Character.SPACE_SEPARATOR -> "Zs";
      case Character.LINE_SEPARATOR -> "Zl";
      case Character.PARAGRAPH_SEPARATOR -> "Zp";
      case Character.CONNECTOR_PUNCTUATION -> "Pc";
      case Character.DASH_PUNCTUATION -> "Pd";
      case Character.START_PUNCTUATION -> "Ps";
      case Character.END_PUNCTUATION -> "Pe";
      case Character.INITIAL_QUOTE_PUNCTUATION -> "Pi";
      case Character.FINAL_QUOTE_PUNCTUATION -> "Pf";
      case Character.OTHER_PUNCTUATION -> "Po";
      case Character.MATH_SYMBOL -> "Sm";
      case Character.CURRENCY_SYMBOL -> "Sc";
      case Character.MODIFIER_SYMBOL -> "Sk";
      case Character.OTHER_SYMBOL -> "So";
      case Character.CONTROL -> "Cc";
      case Character.FORMAT -> "Cf";
      case Character.SURROGATE -> "Cs";
      case Character.PRIVATE_USE -> "Co";
      case Character.UNASSIGNED -> "Cn";
      default -> "Other(" + type + ")";
    };
  }

  // ───────────────────────────────────────────────────────────────────────────────────
  // 3. SURROGATE PAIRS — When 1 char isn't enough (emojis, CJK extensions)
  // ───────────────────────────────────────────────────────────────────────────────────
  private static void demoSurrogatesAndCodePoints() {
    System.out.println("\n🔹 3. Surrogate Pairs & Code Points — The Emoji Reality");

    // Example: "🎉" (U+1F389) — requires 2 chars in UTF-16
    String party = "🎉";
    System.out.println("  String: \"" + party + "\" | length(): " + party.length()); // 2!
    System.out.println("  codePointCount(): " + party.codePointCount(0, party.length())); // 1

    char high = party.charAt(0); // '\uD83C'
    char low = party.charAt(1); // '\uDF89'

    System.out.printf("  High: '\\u%04X', Low: '\\u%04X'%n", (int) high, (int) low);
    System.out.println("  isHighSurrogate? " + Character.isHighSurrogate(high)); // true
    System.out.println("  isLowSurrogate? " + Character.isLowSurrogate(low)); // true
    System.out.println("  isSurrogatePair? " + Character.isSurrogatePair(high, low)); // true

    // ✅ Convert to code point (int)
    int codePoint = Character.toCodePoint(high, low);
    System.out.printf("  Code point: U+%04X (%d)%n", codePoint, codePoint); // U+1F389

    // ✅ Convert back to char array
    char[] chars = Character.toChars(codePoint);
    System.out.println("  toChars() → \"" + new String(chars) + "\"");

    // ❗ Danger: Using charAt() on code points → breaks emojis!
    String text = "Hello 🎉 World";
    System.out.println("  charAt(6) = '" + text.charAt(6) + "' (not '🎉'!)");
  }

  // ───────────────────────────────────────────────────────────────────────────────────
  // 4. CASE CONVERSION — It's Complicated (and Locale Matters!)
  // ───────────────────────────────────────────────────────────────────────────────────
  private static void demoCaseConversion() {
    System.out.println("\n🔹 4. Case Conversion — Not as Simple as It Seems");

    char[] tricky = {
        'ß', // German sharp s → "SS"
        'İ', // Turkish dotted I
        'ı', // Turkish dotless i
        'ﬁ', // Ligature (U+FB01) — may decompose
        'ς', // Greek final sigma
        'É' // Capital E with acute
    };

    System.out.printf("%-3s | %-5s | %-5s | %-5s | Reversible?%n",
        "Ch", "Lower", "Upper", "Title");
    System.out.println("-".repeat(40));

    for (char c : tricky) {
      char lower = Character.toLowerCase(c);
      char upper = Character.toUpperCase(c);
      char title = Character.toTitleCase(c);

      boolean revLower = Character.toUpperCase(lower) == upper;
      boolean revUpper = Character.toLowerCase(upper) == lower;

      System.out.printf("'%c' | '%c'   | '%c'   | '%c'   | %s%n",
          c, lower, upper, title,
          revLower && revUpper ? "Yes" : "No");
    }

    // 🌍 Locale matters! But Character.* is ROOT locale (Unicode default)
    // For Turkish: use String.toLowerCase(Locale.forLanguageTag("tr"))
    System.out.println("\n  ⚠️ Note: Character.toUpperCase('i') = 'I', but Turkish expects 'İ'!");
    System.out.println("     Use String.toUpperCase(Locale.TURKISH) for locale-sensitive cases.");
  }

  // ───────────────────────────────────────────────────────────────────────────────────
  // 5. DIGIT & RADIX OPERATIONS — Parsing in Any Base
  // ───────────────────────────────────────────────────────────────────────────────────
  private static void demoDigitOperations() {
    System.out.println("\n🔹 5. Digit & Radix — Numbers in Any Base");

    // ✅ digit(char, radix) — get numeric value
    System.out.println("  digit('A', 16) = " + Character.digit('A', 16)); // 10
    System.out.println("  digit('z', 36) = " + Character.digit('z', 36)); // 35
    System.out.println("  digit('5', 2) = " + Character.digit('5', 2)); // -1 (invalid in base 2)

    // ✅ forDigit(digit, radix) — reverse
    System.out.println("  forDigit(15, 16) = '" + Character.forDigit(15, 16) + "'"); // 'f'

    // ✅ getNumericValue() — for Unicode numbers (e.g., Roman, Arabic-Indic)
    System.out.println("  getNumericValue('5') = " + Character.getNumericValue('5')); // 5
    System.out.println("  getNumericValue('Ⅴ') = " + Character.getNumericValue('Ⅴ')); // 5 (Roman V)
    System.out.println("  getNumericValue('۳') = " + Character.getNumericValue('۳')); // 3 (Arabic-Indic)

    // Real-world: parse hex string manually
    String hex = "1A3";
    int value = 0;
    for (char c : hex.toCharArray()) {
      int d = Character.digit(c, 16);
      if (d == -1)
        throw new NumberFormatException("Invalid hex digit: " + c);
      value = value * 16 + d;
    }
    System.out.println("  Manual hex \"1A3\" → " + value + " (0x" + Integer.toHexString(value) + ")");
  }

  // ───────────────────────────────────────────────────────────────────────────────────
  // 6. BEST PRACTICES — What Senior Engineers Do
  // ───────────────────────────────────────────────────────────────────────────────────
  private static void printBestPractices() {
    System.out.println("\n" + "=".repeat(80));
    System.out.println("🎯 CHARACTER BEST PRACTICES — Unicode Done Right");
    System.out.println("=".repeat(80));

    System.out.println("✅ DO:");
    System.out.println(" • Use codePointCount(), codePointAt() for string iteration (emojis!)");
    System.out.println(" • Prefer Character.isXxx() over hand-rolled ranges (e.g., 'a' <= c && c <= 'z')");
    System.out.println(" • Use digit()/forDigit() for base conversion — handles letters correctly");
    System.out.println(" • For locale-sensitive case: use String.toLowerCase(Locale), not Character");

    System.out.println("\n❌ AVOID:");
    System.out.println(" • Assuming char == Unicode character (breaks on emojis)");
    System.out.println(" • Using c >= '0' && c <= '9' — fails for Arabic/Indic digits");
    System.out.println(" • Relying on Character.toUpperCase().toLowerCase() == original (not reversible!)");
    System.out.println(" • Ignoring surrogate pairs in text processing (causes data corruption)");

    System.out.println("\n💡 Pro Patterns:");
    System.out.println("   // Safe code point iteration:");
    System.out.println("   for (int i = 0; i < str.length(); ) {");
    System.out.println("       int cp = str.codePointAt(i);");
    System.out.println("       // process cp");
    System.out.println("       i += Character.charCount(cp);");
    System.out.println("   }");
    System.out.println("");
    System.out.println("   // Input validation for usernames:");
    System.out.println("   boolean isValidChar(char c) {");
    System.out.println("       return Character.isLetterOrDigit(c) || c == '_' || c == '-';");
    System.out.println("   }");
  }
}
