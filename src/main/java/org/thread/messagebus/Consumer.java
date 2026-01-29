package org.thread.messagebus;

public class Consumer implements Runnable {
    private final MessageBus bus;
    private final String name;
    private final String topic;

    public Consumer(MessageBus bus, String name, String topic) {
        this.bus = bus;
        this.name = name;
        this.topic = topic;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message msg = bus.take(topic);
                System.out.println("[Consumer " + name + "] topic=" + topic + " payload=" + msg.payload());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
