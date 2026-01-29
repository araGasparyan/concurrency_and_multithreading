package org.thread.concurrentmodification;

import java.util.HashMap;
import java.util.Map;


import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SynchronizedThreadSafeMap extends AbstractMap<Integer, Integer> {

    private final Map<Integer, Integer> delegate = new HashMap<>();
    private final Object lock = new Object();

    @Override
    public Integer put(Integer key, Integer value) {
        synchronized (lock) {
            return delegate.put(key, value);
        }
    }

    @Override
    public Integer get(Object key) {
        synchronized (lock) {
            return delegate.get(key);
        }
    }

    @Override
    public int size() {
        synchronized (lock) {
            return delegate.size();
        }
    }

    @Override
    public Set<Entry<Integer, Integer>> entrySet() {
        synchronized (lock) {
            return Set.copyOf(delegate.entrySet()); // snapshot -> safe iteration
        }
    }
}
