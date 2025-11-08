package phase1.basics.primitive.demo;

import phase1.basics.primitive.DataTypesLab;

public class DataTypesDemo {
    static class DefaultsDemo {
        byte b;
        short s;
        int i;
        long l;
        float f;
        double d;
        char c ;
        boolean bool;
        String str; // reference type → defaults to null
    }
    public static void main(String[] args) {
        System.out.println("✅ DEFAULT VALUES (uninitialized FIELDS):");
        DataTypesLab dataType = new DataTypesLab();
        DefaultsDemo demo = new DefaultsDemo();
        System.out.println("  byte: " + demo.b);
        System.out.println("  short: " + demo.s);
        System.out.println("  int: " + demo.i);
        System.out.println("  long: " + demo.l);
        System.out.println("  float: " + demo.f);
        System.out.println("  double: " + demo.d);
        System.out.println("  char: [" + demo.c + "]"); // prints blank (null char)
        System.out.println("  boolean: " + demo.bool);
        System.out.println("  String: " + demo.str);    // null
        System.out.println();


    }
}
