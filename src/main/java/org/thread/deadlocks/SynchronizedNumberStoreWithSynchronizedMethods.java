package org.thread.deadlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SynchronizedNumberStoreWithSynchronizedMethods {
    private final List<Integer> numbers = new ArrayList<>();
    private final Random random = new Random();

    public synchronized void addRandom() {
        numbers.add(random.nextInt(1000));
    }

    public synchronized long sum() {
        long s = 0;
        for (int n : numbers) {
            s += n;
        }
        return s;
    }

    public synchronized double sqrtOfSumOfSquares() {
        long ss = 0;
        for (int n : numbers) {
            ss += (long) n * n;
        }
        return Math.sqrt(ss);
    }

    public synchronized int size() {
        return numbers.size();
    }
}
