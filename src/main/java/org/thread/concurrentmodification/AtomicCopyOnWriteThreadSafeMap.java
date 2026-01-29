package org.thread.concurrentmodification;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicCopyOnWriteThreadSafeMap extends AbstractMap<Integer, Integer> {

    private final AtomicReference<Map<Integer, Integer>> ref =
            new AtomicReference<>(new HashMap<>());

    @Override
    public Integer put(Integer key, Integer value) {
        while (true) {
            Map<Integer, Integer> current = ref.get();
            Map<Integer, Integer> copy = new HashMap<>(current);
            Integer prev = copy.put(key, value);
            if (ref.compareAndSet(current, copy)) {
                return prev;
            }
        }
    }

    @Override
    public Integer get(Object key) {
        return ref.get().get(key);
    }

    @Override
    public int size() {
        return ref.get().size();
    }

    @Override
    public Set<Entry<Integer, Integer>> entrySet() {
        return ref.get().entrySet(); // safe: snapshot map instance is never modified
    }
}
