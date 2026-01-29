package org.thread.messagebus;

import java.util.HashMap;
import java.util.Map;

public class MessageBus {
    private final Map<String, BlockingMessageQueue> topicQueues = new HashMap<>();
    private final int perTopicCapacity;

    public MessageBus(int perTopicCapacity) {
        this.perTopicCapacity = perTopicCapacity;
    }

    public void publish(String topic, String payload) throws InterruptedException {
        getOrCreateQueue(topic).put(new Message(topic, payload));
    }

    public Message take(String topic) throws InterruptedException {
        return getOrCreateQueue(topic).take();
    }

    private synchronized BlockingMessageQueue getOrCreateQueue(String topic) {
            return topicQueues.computeIfAbsent(topic, t -> new BlockingMessageQueue(perTopicCapacity));
    }
}