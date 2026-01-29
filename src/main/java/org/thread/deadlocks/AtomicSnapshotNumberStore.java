package org.thread.deadlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicSnapshotNumberStore {
    private final AtomicReference<List<Integer>> ref =
            new AtomicReference<>(List.of());

    private final Random random = new Random();

    public void addRandom() {
        while (true) {
            List<Integer> current = ref.get();

            List<Integer> copy = new ArrayList<>(current);
            copy.add(random.nextInt(1000));

            if (ref.compareAndSet(current, List.copyOf(copy))) {
                return;
            }
            // CAS failed → retry
        }
    }

    public long sum() {
        long s = 0;
        for (int n : ref.get()) s += n;
        return s;
    }

    public double sqrtOfSumOfSquares() {
        long ss = 0;
        for (int n : ref.get()) ss += (long) n * n;
        return Math.sqrt(ss);
    }

    public int size() {
        return ref.get().size();
    }
}
