package org.thread.deadlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NumberStoreWithReentrantLock {
    private final List<Integer> numbers = new ArrayList<>();
    private final Random random = new Random();
    private final Lock lock = new ReentrantLock();

    public void addRandom() {
        lock.lock();
        try {
            numbers.add(random.nextInt(1000));
        } finally {
            lock.unlock();
        }
    }

    public long sum() {
        lock.lock();
        try {
            long s = 0;
            for (int n : numbers) s += n;
            return s;
        } finally {
            lock.unlock();
        }
    }

    public double sqrtOfSumOfSquares() {
        lock.lock();
        try {
            long ss = 0;
            for (int n : numbers) ss += (long) n * n;
            return Math.sqrt(ss);
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return numbers.size();
        } finally {
            lock.unlock();
        }
    }
}
