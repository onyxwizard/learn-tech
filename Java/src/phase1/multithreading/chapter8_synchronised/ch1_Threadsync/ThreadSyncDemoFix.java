package chapter8_synchronised.ch1_Threadsync;

import java.util.ArrayList;
import java.util.List;

public class ThreadSyncDemoFix {
    // Shared mutable state
    private static class Counter {
        int count = 0;
    }

    // ✅ SAFE: No synchronization
    // Appraoch 1: Add Sync to method 
    synchronized void increment(Counter c) {
        /**
         * Approach 2: Use sync inside method
         * -------------------------
         *  synchronized (c) {
         *     *Add logic*
         *  }
         * ------------------------
         */
        // Simulate work to force interleaving
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
        }

        // Compound operation: NOT atomic
        int current = c.count; // Read
        current++; // Modify
        c.count = current; // Write
        System.out.println(Thread.currentThread().getName() + " → " + c.count);
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadSyncDemoFix obj = new ThreadSyncDemoFix();
        Counter counter = new Counter(); // Shared object

        // Two threads incrementing 1000x each → expected: 2000
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) obj.increment(counter);
        }, "T1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) obj.increment(counter);
        }, "T2");

        t1.start(); t2.start();
        t1.join(); t2.join();

        System.out.println("\n✅ Expected: 2000 | Actual: " + counter.count);
        if (counter.count != 2000) {
            System.out.println("❌ RACE CONDITION DETECTED!");
        }
    }
}