package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

@WebServlet(name = "SkiResortServlet", urlPatterns = {"/ski"})
public class SkiResortServlet extends HttpServlet {
    private final Random random = new Random();
    private static final int MAX_RETRIES = 5;
    private static final double ERROR_RATE = 0.05; // Error rates for testing: 5%, 10%, 15%

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int retryCount = 0;
        boolean success = false;

        while (retryCount < MAX_RETRIES) {
            try {
                if (retryCount < MAX_RETRIES - 1 && random.nextDouble() < ERROR_RATE) {
                    throw new RuntimeException("Simulated server error on attempt " + (retryCount + 1));
                }

                // Process request on successful attempt
                processRequest(request, response);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Request processed successfully on attempt " + (retryCount + 1));
                success = true;
                break; // Exit after successful processing
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage() + ". Attempt " + (retryCount + 1) + " failed, retrying...");
                retryCount++; // Increment the retry count
            }
        }

        if (!success) { // If never succeeded after retries
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Failed to process the request after all retries.");
        }
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) {
        // Placeholder for actual request processing logic
        System.out.println("Processing the request...");
    }
}
