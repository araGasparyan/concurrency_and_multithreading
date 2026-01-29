package org.thread.deadlocks;


public class DeadlocksMain {
    static void main() throws InterruptedException {
        SynchronizedNumberStoreWithObjectLock shared = new SynchronizedNumberStoreWithObjectLock();
        // SynchronizedNumberStoreWithSynchronizedMethods shared = new SynchronizedNumberStoreWithSynchronizedMethods();
        // NumberStoreWithReentrantLock  shared = new NumberStoreWithReentrantLock();
        // COWNumberStore  shared = new COWNumberStore();
        // AtomicSnapshotNumberStore shared = new AtomicSnapshotNumberStore();

        Thread writer = new LoopingWorkerThread(
                "writer",
                shared::addRandom,
                5
        );

        Thread sumPrinter = new LoopingWorkerThread(
                "sum",
                () -> System.out.println("[SUM] size=" + shared.size() + " sum=" + shared.sum()),
                1000
        );

        Thread sqrtSumSquaresPrinter = new LoopingWorkerThread(
                "sqrt-sum-squares",
                () -> System.out.println("[SQRT(SUMSQ)] size=" + shared.size() + " value=" + shared.sqrtOfSumOfSquares()),
                1000
        );

        writer.start();
        sumPrinter.start();
        sqrtSumSquaresPrinter.start();

        writer.join();
        sumPrinter.join();
        sqrtSumSquaresPrinter.join();

        System.out.println("main thread finished");
    }
}
