package org.thread.messagebus;

import java.util.Random;

public class Producer implements Runnable {
    private final MessageBus bus;
    private final String name;
    private final String[] topics;
    private final Random random = new Random();

    public Producer(MessageBus bus, String name, String[] topics) {
        this.bus = bus;
        this.name = name;
        this.topics = topics;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            String topic = topics[random.nextInt(topics.length)];
            String payload = "payload-" + random.nextInt(10_000) + " from " + name;

            try {
                bus.publish(topic, payload);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
