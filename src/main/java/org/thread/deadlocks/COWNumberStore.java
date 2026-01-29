package org.thread.deadlocks;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class COWNumberStore {
    private final List<Integer> numbers = new CopyOnWriteArrayList<>();
    private final Random random = new Random();

    public void addRandom() {
        numbers.add(random.nextInt(1000));
    }

    public long sum() {
        long s = 0;
        for (int n : numbers) s += n;
        return s;
    }

    public double sqrtOfSumOfSquares() {
        long ss = 0;
        for (int n : numbers) ss += (long) n * n;
        return Math.sqrt(ss);
    }

    public int size() {
        return numbers.size();
    }
}
