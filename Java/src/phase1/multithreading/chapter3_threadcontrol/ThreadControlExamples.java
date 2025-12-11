package chapter3_threadcontrol;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ThreadControlExamples {

    // Helper for clean timestamps
    private static String ts() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("🟢 Starting Thread Control Demo at " + ts());
        System.out.println("=" .repeat(60));

        // 1️⃣ Thread.sleep(millis) — Rate-limiting & cooperative pause
        demoSleep();

        // 2️⃣ thread.join() — Wait for completion (sequencing)
        demoJoin();

        // 3️⃣ thread.interrupt() — Graceful cancellation
        demoInterrupt();

        // 4️⃣ Daemon threads — Background helpers
        demoDaemon();

        // 5️⃣ Bonus: Thread.yield() — Voluntary yield (illustrative)
        demoYield();

        System.out.println("=" .repeat(60));
        System.out.println("✅ All demos completed at " + ts());
    }

    // ────────────────────────────────────────────────────────────────
    // 1️⃣ Thread.sleep(millis) — "Pause me for a while"
    // Purpose: Simulate work, rate-limiting, cooperative scheduling
    // ⚠️ NEVER ignore InterruptedException!
    // ────────────────────────────────────────────────────────────────
    static void demoSleep() throws InterruptedException {
        System.out.println("\n🔹 DEMO 1: Thread.sleep() — Rate-Limited Polling");
        System.out.println("   (Polling every 500ms, 3 times)");

        Runnable poller = () -> {
            for (int i = 1; i <= 3; i++) {
                System.out.println("   [" + ts() + "] Poll #" + i + " — Checking for updates...");
                try {
                    Thread.sleep(500);  // ← Sleep in CURRENT thread
                } catch (InterruptedException e) {
                    System.err.println("   ❗ Poller interrupted — exiting early");
                    Thread.currentThread().interrupt(); // Restore flag
                    return;
                }
            }
            System.out.println("   [" + ts() + "] Polling complete.");
        };

        Thread t = new Thread(poller, "Poller-Thread");
        t.start();
        t.join(); // Wait for it (see demoJoin for details)
    }

    // ────────────────────────────────────────────────────────────────
    // 2️⃣ thread.join() — "Wait for you to finish"
    // Purpose: Enforce ordering (e.g., download → unzip)
    // ────────────────────────────────────────────────────────────────
    static void demoJoin() throws InterruptedException {
        System.out.println("\n🔹 DEMO 2: thread.join() — Sequencing: Download → Process");

        Runnable download = () -> {
            System.out.println("   [" + ts() + "] 📥 Starting download (simulated 1s)...");
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            System.out.println("   [" + ts() + "] ✅ Download finished.");
        };

        Runnable process = () -> {
            System.out.println("   [" + ts() + "] 🛠️  Processing file...");
            try { Thread.sleep(300); } catch (InterruptedException ignored) {}
            System.out.println("   [" + ts() + "] ✅ Processing done.");
        };

        Thread downloader = new Thread(download, "Downloader");
        Thread processor = new Thread(process, "Processor");

        // Start download
        downloader.start();

        // Wait for download BEFORE starting processing
        downloader.join();  // ← Blocks here until downloader finishes
        processor.start();
        processor.join();   // Wait for processor too (for clean output)

        System.out.println("   [" + ts() + "] 🎯 Sequence complete: download → process");
    }

    // ────────────────────────────────────────────────────────────────
    // 3️⃣ thread.interrupt() — "Please stop when convenient"
    // Purpose: Cooperative cancellation (e.g., user clicks 'Cancel')
    // ✅ Best practice: Check isInterrupted() + restore flag in catch
    // ────────────────────────────────────────────────────────────────
    static void demoInterrupt() throws InterruptedException {
        System.out.println("\n🔹 DEMO 3: thread.interrupt() — Graceful Cancellation");

        Runnable longTask = () -> {
            System.out.println("   [" + ts() + "] 🔁 Starting long-running task (10 steps, 200ms each)...");
            for (int i = 1; i <= 10; i++) {
                // 🔑 Check interrupt status BEFORE doing work
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("   [" + ts() + "] 🛑 Interrupt detected at step " + i + " — cleaning up & exiting");
                    return; // Exit cleanly
                }

                System.out.println("   [" + ts() + "] Step " + i + "/10");
                try {
                    Thread.sleep(200); // Simulate work — can be interrupted here
                } catch (InterruptedException e) {
                    // 🔑 Restore interrupt status & exit
                    System.err.println("   ❗ Interrupted during sleep at step " + i);
                    Thread.currentThread().interrupt(); // ← Critical!
                    return;
                }
            }
            System.out.println("   [" + ts() + "] ✅ Task completed naturally.");
        };

        Thread worker = new Thread(longTask, "Worker");
        worker.start();

        // Let it run for ~1s, then cancel
        Thread.sleep(1100);
        System.out.println("   [" + ts() + "] 🚫 Main thread: Requesting cancellation...");
        worker.interrupt(); // ← Send interrupt signal

        worker.join(); // Wait for graceful exit
        System.out.println("   [" + ts() + "] 🎯 Worker thread terminated.");
    }

    // ────────────────────────────────────────────────────────────────
    // 4️⃣ Daemon Threads — "Background helpers"
    // Purpose: Non-critical background work (e.g., logging, monitoring)
    // ⚠️ JVM exits when ONLY daemons remain — they’re killed abruptly!
    // ────────────────────────────────────────────────────────────────
    static void demoDaemon() throws InterruptedException {
        System.out.println("\n🔹 DEMO 4: Daemon Threads — Background Heartbeat");

        Runnable heartbeat = () -> {
            int count = 0;
            try {
                while (true) {
                    System.out.println("   [" + ts() + "] 💓 Heartbeat #" + (++count));
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {
                System.out.println("   [" + ts() + "] 🫀 Heartbeat interrupted — exiting");
            }
        };

        Thread daemon = new Thread(heartbeat, "Heartbeat-Daemon");
        daemon.setDaemon(true);  // ← MUST be before start()
        daemon.start();

        System.out.println("   [" + ts() + "] 🔔 Main thread sleeping 1.2s...");
        Thread.sleep(1200); // Let daemon print ~4 times

        System.out.println("   [" + ts() + "] 🏁 Main thread ending — JVM will EXIT now (daemons die)");
        // No join() — daemon won't block shutdown
    }

    // ────────────────────────────────────────────────────────────────
    // 5️⃣ Bonus: Thread.yield() — "Let others run (same priority)"
    // Purpose: Hint to scheduler — rarely needed; no guarantees
    // ────────────────────────────────────────────────────────────────
    static void demoYield() {
        System.out.println("\n🔹 BONUS: Thread.yield() — Voluntary Yield (Illustrative)");

        Runnable spinner = () -> {
            long count = 0;
            while (count < 5_000_000L) {
                count++;
                // Occasionally suggest yielding
                if (count % 1_000_000 == 0) {
                    System.out.println("   [" + Thread.currentThread().getName() + "] Count: " + count);
                    Thread.yield(); // ← Hint: "Other same-priority threads, go ahead!"
                }
            }
            System.out.println("   [" + Thread.currentThread().getName() + "] ✅ Done.");
        };

        Thread t1 = new Thread(spinner, "Spinner-1");
        Thread t2 = new Thread(spinner, "Spinner-2");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("   🧭 Note: yield() is a hint — output order may vary by JVM/OS.");
    }
}