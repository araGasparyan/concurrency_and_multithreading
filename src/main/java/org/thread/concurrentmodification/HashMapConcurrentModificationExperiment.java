package org.thread.concurrentmodification;

import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

class HashMapConcurrentModificationExperiment {

     static void main() throws InterruptedException {
        // ---- Settings ----
        int initialSize = 10_000;
        int runSeconds = 10;     // measure window
        int warmupSeconds = 3;   // JIT warm-up (mainly useful for Java 8+)

        // ---- Choose map (change only this line) ----
        // Map<Integer, Integer> map = new HashMap<>();
        Map<Integer, Integer> map = new SynchronizedThreadSafeMap();
        // Map<Integer, Integer> map = new AtomicCopyOnWriteThreadSafeMap();
        // Map<Integer, Integer> map = new ConcurrentHashMap<>();
        // Map<Integer, Integer> map = Collections.synchronizedMap(new HashMap<>());

        // ---- Init ----
        for (int i = 0; i < initialSize; i++) {
            map.put(i, 1);
        }

        AtomicBoolean stop = new AtomicBoolean(false);

        AtomicLong writes = new AtomicLong(0);
        AtomicLong readLoops = new AtomicLong(0);
        AtomicLong lastSum = new AtomicLong(0);

        AtomicLong warmupWrites = new AtomicLong(0);
        AtomicLong warmupReadLoops = new AtomicLong(0);

        Thread writer = new Thread(() -> {
            int key = initialSize;
            while (!stop.get()) {
                map.put(key++, 1);
                writes.incrementAndGet();

                if ((key % 100_000) == 0) {
                    System.out.println("[writer] size=" + map.size());
                }
            }
        }, "writer");

        Thread reader = new Thread(() -> {
            while (!stop.get()) {
                try {
                    long sum = 0;

                    // For Collections.synchronizedMap, uncomment this block
                    // and comment out the plain for-loop below:
                    //
                    // synchronized (map) {
                    //     for (Map.Entry<Integer, Integer> e : map.entrySet()) {
                    //         sum += e.getValue();
                    //     }
                    // }

                    for (Map.Entry<Integer, Integer> e : map.entrySet()) {
                        sum += e.getValue();
                    }

                    lastSum.set(sum);
                    readLoops.incrementAndGet();

                } catch (ConcurrentModificationException ex) {
                    System.out.println("\n❌ ConcurrentModificationException caught in reader!");
                    ex.printStackTrace(System.out);
                    stop.set(true);
                }
            }
        }, "reader");

        long startNs = System.nanoTime();
        writer.start();
        reader.start();

        // ---- Warmup window ----
        Thread.sleep(warmupSeconds * 1000L);
        warmupWrites.set(writes.get());
        warmupReadLoops.set(readLoops.get());

        // ---- Measurement window ----
        Thread.sleep(runSeconds * 1000L);
        stop.set(true);

        writer.join();
        reader.join();

        long endNs = System.nanoTime();
        long totalMs = (endNs - startNs) / 1_000_000;

        long measuredWrites = writes.get() - warmupWrites.get();
        long measuredReadLoops = readLoops.get() - warmupReadLoops.get();

        // ---- Report summary ----
        System.out.println("\n===== SUMMARY =====");
        System.out.println("java.version=" + System.getProperty("java.version"));
        System.out.println("map.impl=" + map.getClass().getName());
        System.out.println("initialSize=" + initialSize);
        System.out.println("warmupSeconds=" + warmupSeconds);
        System.out.println("runSeconds=" + runSeconds);
        System.out.println("totalRuntimeMs=" + totalMs);

        System.out.println("measuredWrites=" + measuredWrites);
        System.out.println("measuredReadLoops=" + measuredReadLoops);
        System.out.println("lastSum=" + lastSum.get());

        double writesPerSec = measuredWrites / (double) runSeconds;
        double readLoopsPerSec = measuredReadLoops / (double) runSeconds;

        System.out.println("writesPerSec=" + writesPerSec);
        System.out.println("readLoopsPerSec=" + readLoopsPerSec);

        System.out.println("===================");
    }
}

