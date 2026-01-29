package org.thread.deadlocks;

public class LoopingWorkerThread extends Thread {
    private final Runnable work;
    private final long sleepMs;

    public LoopingWorkerThread(String name, Runnable work, long sleepMs) {
        super(name);
        this.work = work;
        this.sleepMs = sleepMs;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            work.run();
            sleepQuietly(sleepMs);
        }
    }

    private void sleepQuietly(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            interrupt();
        }
    }
}