
public class CounterRace {

    static int counter = 0;

    static void incrementWithoutSync() {
        counter++;
    }

    static synchronized void incrementWithSync() {
        counter++;
    }

    public static void main(String[] args) throws InterruptedException {

        int numberOfThreads = 10;
        int incrementsPerThread = 100000;

        Thread[] threads = new Thread[numberOfThreads];

        // Without Synchronization
        counter = 0;

        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    incrementWithoutSync();
                }
            });

            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        int expected = numberOfThreads * incrementsPerThread;

        System.out.println("Without synchronization:");
        System.out.println("Expected count: " + expected);
        System.out.println("Actual count:   " + counter);

        // With Synchronization
        counter = 0;

        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    incrementWithSync();
                }
            });

            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("\nWith synchronization:");
        System.out.println("Expected count: " + expected);
        System.out.println("Actual count:   " + counter);
    }
}
