package org.thread.messagebus;

public class MessageBusMain {
    static void main() throws InterruptedException {
        MessageBus bus = new MessageBus(50);

        String[] topics = {"sports", "music", "news"};

        Thread p1 = new Thread(new Producer(bus, "P1", topics));
        Thread p2 = new Thread(new Producer(bus, "P2", topics));

        Thread c1 = new Thread(new Consumer(bus, "C1", "sports"));
        Thread c2 = new Thread(new Consumer(bus, "C2", "music"));
        Thread c3 = new Thread(new Consumer(bus, "C3", "news"));

        p1.start();
        p2.start();
        c1.start();
        c2.start();
        c3.start();

        p1.join();
        p2.join();
        c1.join();
        c2.join();
        c3.join();
    }
}
