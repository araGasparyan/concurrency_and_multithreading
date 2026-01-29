package org.thread.messagebus;

import java.util.ArrayDeque;
import java.util.Deque;

public class BlockingMessageQueue {
    private final Deque<Message> deque = new ArrayDeque<>();
    private final int capacity;

    public BlockingMessageQueue(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("capacity must be > 0");
        this.capacity = capacity;
    }

    public synchronized void put(Message message) throws InterruptedException {
        while (deque.size() >= capacity) {
            wait();
        }
        deque.addLast(message);
        notifyAll();
    }

    public synchronized Message take() throws InterruptedException {
        while (deque.isEmpty()) {
            wait();
        }
        Message msg = deque.removeFirst();
        notifyAll();
        return msg;
    }
}
