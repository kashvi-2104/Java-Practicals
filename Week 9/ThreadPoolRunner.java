
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolRunner {

    public static void main(String[] args) {

        // Create a fixed thread pool with 3 threads
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Submit 10 tasks
        for (int i = 1; i <= 10; i++) {
            int taskId = i;

            executor.submit(() -> {
                try {
                    System.out.println(
                        "Task " + taskId + " is running on "
                        + Thread.currentThread().getName()
                    );

                    // Sleep for 1 second
                    Thread.sleep(1000);

                    System.out.println(
                        "Task " + taskId + " completed by "
                        + Thread.currentThread().getName()
                    );

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // No more tasks will be submitted
        executor.shutdown();

        try {
            // Wait until all tasks are completed
            if (executor.awaitTermination(30, TimeUnit.SECONDS)) {
                System.out.println("All tasks completed.");
            } else {
                System.out.println("Timeout! Some tasks are still running.");
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
