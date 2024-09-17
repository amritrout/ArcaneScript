import fi.iki.elonen.NanoHTTPD;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class ArcaneHttpServer extends NanoHTTPD {

    private static final long TIMEOUT_MS = 10000;

    public ArcaneHttpServer(int port) {
        super(port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        System.out.println("Received request: " + session.getMethod() + " " + session.getUri());
        Response response;

        if (Method.POST.equals(session.getMethod())) {
            Map<String, String> files = new HashMap<>();
            try {
                session.parseBody(files);
                String fileName = files.get("postData");

                if (fileName != null) {
                    // Read the uploaded file
                    File file = new File(fileName);
                    String fileContent = new String(Files.readAllBytes(file.toPath()));

                    // Process the file content
                    String result = executeJavaCode(fileContent);

                    response = newFixedLengthResponse(result);
                } else {
                    response = newFixedLengthResponse("No file uploaded.");
                }
            } catch (IOException | NanoHTTPD.ResponseException e) {
                e.printStackTrace();
                response = newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain", "Error processing file.");
            }
        } else {
            response = newFixedLengthResponse("Send a POST request with a file to process.");
        }

        // Set CORS headers
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");

        return response;
    }

    private String executeJavaCode(String fileContent) {
        File tempFile = null;
        File extractedJarFile = null;
        Process process = null;
        try {
            // Determine the directory of the running JAR file (ArcaneHttpServer.jar)
            File jarDir = new File(ArcaneHttpServer.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
            tempFile = new File(jarDir, "ArcaneCode_" + System.currentTimeMillis() + ".txt");

            // Write the file content to the temp file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                writer.write(fileContent);
            }

            // Extract the arcaneInterpreter.jar file into the same directory
            InputStream jarInputStream = getClass().getClassLoader().getResourceAsStream("arcaneInterpreter.jar");
            if (jarInputStream == null) {
                return "Error: JAR resource not found in resources.";
            }

            extractedJarFile = new File(jarDir, "arcaneInterpreter_" + System.currentTimeMillis() + ".jar");
            try (FileOutputStream outputStream = new FileOutputStream(extractedJarFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = jarInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            ProcessBuilder pb = new ProcessBuilder("java", "-jar", extractedJarFile.getAbsolutePath(), tempFile.getAbsolutePath());
            pb.redirectErrorStream(true);
            process = pb.start();

            // Set up a timeout for the execution
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Process finalProcess = process;
            Future<String> resultFuture = executor.submit(() -> {
                StringBuilder result = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(finalProcess.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line).append("\n");
                        System.out.println(line);
                    }
                }
                return result.toString();
            });

            try {
                String result = resultFuture.get(TIMEOUT_MS, TimeUnit.MILLISECONDS);
                return result;
            } catch (TimeoutException e) {
                if (process != null) {
                    process.destroyForcibly(); // Forcibly terminate the process
                }
                return "Error: Execution timed out.";
            } finally {
                executor.shutdown();
                executor.awaitTermination(10, TimeUnit.SECONDS); // Wait for the executor to terminate
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error executing JAR file.";
        } finally {
            // Clean up temporary files
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
            if (extractedJarFile != null && extractedJarFile.exists()) {
                extractedJarFile.delete();
            }
            if (process != null) {
                process.destroy(); // Ensure process is destroyed
            }
        }
    }

    public static void main(String[] args) {
        try {
            ArcaneHttpServer server = new ArcaneHttpServer(8443);
            server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
            System.out.println("Server started on port 8443");
        } catch (IOException e) {
            System.err.println("Failed to start the server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
