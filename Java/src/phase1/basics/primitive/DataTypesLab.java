package phase1.basics.primitive;

public class DataTypesLab {
    public DataTypesLab(){
        System.out.println("🔬 Java Primitive Types & Literals Lab");
        System.out.println("=====================================\n");

        // 🧮 INTEGERS: byte, short, int, long
        System.out.println("🔢 INTEGERS:");
        byte maxVolume = 100;                          // 8-bit
        short year = 2025;                             // 16-bit
        int population = 800_000_000;                // 32-bit + underscores
        long galaxyStars = 100_000_000_000_000L;       // 64-bit + L suffix

        System.out.println("  byte maxVolume = " + maxVolume);
        System.out.println("  short year = " + year);
        System.out.println("  int population = " + population);
        System.out.println("  long galaxyStars = " + galaxyStars);

        // 🔢 Alternate integer literals: Hexadecimal & Binary
        int hexValue = 0xFF;        // 255 in hex
        int binaryValue = 0b11010;  // 26 in binary
        System.out.println("  Hex 0xFF = " + hexValue);
        System.out.println("  Binary 0b11010 = " + binaryValue);
        System.out.println();

        // 📐 FLOATING-POINT: float, double
        System.out.println("🧮 FLOATING-POINT:");
        float temperature = 98.6f;           // must use 'f' suffix
        double pi = 3.14159;                 // default is double
        double scientific = 1.23e4;          // 12300.0

        System.out.println("  float temperature = " + temperature + "f");
        System.out.println("  double pi = " + pi);
        System.out.println("  Scientific notation: 1.23e4 = " + scientific);
        System.out.println();

        // 🔤 CHAR & STRING
        System.out.println("🔤 CHAR & STRING:");
        char grade = 'A';                    // single quotes
        char omega = '\u03A9';               // Unicode: Ω
        String message = "Hello\n\t\"World\"!"; // escapes: \n, \t, \"

        System.out.println("  char grade = '" + grade + "'");
        System.out.println("  char omega (Unicode \\u03A9) = '" + omega + "'");
        System.out.println("  String message = \"" + message + "\"");
        System.out.println();
    }
}
