import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransferSimulator {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String TRANSFER_ENDPOINT = "/transfers";

    public static void main(String[] args) {
        // Create an ExecutorService with a fixed thread pool size
        int numThreads = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        // Submit transfer tasks to the executor
        for (int i = 0; i < numThreads; i++) {
            TransferTask task = new TransferTask(BASE_URL + TRANSFER_ENDPOINT);
            while (!executor.submit(task).isDone()) {
                // Wait for the executor to be available
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        executor.shutdown();
    }
}

class TransferTask implements Runnable {

    private String transferEndpoint;

    public TransferTask(String transferEndpoint) {
        this.transferEndpoint = transferEndpoint;
    }

    @Override
    public void run() {
        // Simulate a transfer by calling the REST API endpoint
        try {
            // Create a URL object for the transfer endpoint
            URL url = new URL(transferEndpoint);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method and headers
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            // Set the request body
            float randomNum = (float) Math.random() * 10;
            String requestBody = "{\"amount\":" + randomNum + ", \"fromAccountId\": \"872031184\", \"toAccountId\": \"894925953\"}";
            connection.setDoOutput(true);
            connection.getOutputStream().write(requestBody.getBytes("UTF-8"));

            // Get the response code and message
            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();

            // Close the connection
            connection.disconnect();

            // Print the response code and message
            System.out.println("Response code: " + responseCode);
            System.out.println("Response message: " + responseMessage);
        } catch (IOException e) {
            System.out.println("Error simulating transfer: " + e.getMessage());
        }
    }
}