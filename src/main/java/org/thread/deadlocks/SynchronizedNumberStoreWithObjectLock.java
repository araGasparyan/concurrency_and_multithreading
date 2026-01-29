package org.thread.deadlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SynchronizedNumberStoreWithObjectLock {
    private final List<Integer> numbers = new ArrayList<>();
    private final Random random = new Random();
    private final Object lock = new Object();

    public void addRandom() {
        synchronized (lock) {
            numbers.add(random.nextInt(1000));
        }
    }

    public long sum() {
        synchronized (lock) {
            long s = 0;
            for (int n : numbers) s += n;
            return s;
        }
    }

    public double sqrtOfSumOfSquares() {
        synchronized (lock) {
            long ss = 0;
            for (int n : numbers) ss += (long) n * n;
            return Math.sqrt(ss);
        }
    }

    public int size() {
        synchronized (lock) {
            return numbers.size();
        }
    }
}