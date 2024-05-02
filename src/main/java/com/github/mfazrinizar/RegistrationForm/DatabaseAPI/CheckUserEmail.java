package com.github.mfazrinizar.RegistrationForm.DatabaseAPI;

/*
 * Author       : M. Fazri Nizar
 * Institution  : Sriwijaya University
 * GitHub       : github.com/mfazrinizar
 * File Name    : CheckUserEmail.java
 */

import java.awt.Frame;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class CheckUserEmail {
    private class DoesEmailExistTask extends SwingWorker<Boolean, Void> {
        private final String email;
        private final String API_URL;
        private final String BEARER_TOKEN;
        private final JDialog progressDialog;
        private final Consumer<Boolean> callback;

        public DoesEmailExistTask(String email, String API_URL, String BEARER_TOKEN, Consumer<Boolean> callback) {
            this.email = email;
            this.API_URL = API_URL;
            this.BEARER_TOKEN = BEARER_TOKEN;
            this.callback = callback;

            progressDialog = new JDialog((Frame) null, "Mohon tunggu...", true);
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            progressDialog.getContentPane().add(progressBar);
            progressDialog.pack();
            progressDialog.setLocationRelativeTo(null);
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            SwingUtilities.invokeLater(() -> progressDialog.setVisible(true));

            boolean result = doesEmailExist(email, API_URL, BEARER_TOKEN);

            SwingUtilities.invokeLater(() -> progressDialog.setVisible(false));

            return result;
        }

        @Override
        protected void done() {
            try {
                Boolean result = get();
                callback.accept(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void doesEmailExistAsync(String email, String API_URL, String BEARER_TOKEN, Consumer<Boolean> callback) {
        new DoesEmailExistTask(email, API_URL, BEARER_TOKEN, callback).execute();
    }

    private boolean doesEmailExist(String email, final String API_URL, final String BEARER_TOKEN) {
        final String CHECK_EMAIL_QUERY = "SELECT COUNT(*) FROM registration_details WHERE email = ?;";
        HttpClient client = HttpClient.newHttpClient();

        String checkEmailBody = String.format("{\n" +
                        "\"params\": [\n" +
                        "  \"%s\"\n" +
                        "],\n" +
                        "\"sql\": \"%s\"\n" +
                        "}",
                email,
                CHECK_EMAIL_QUERY);

        HttpRequest checkEmailRequest = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + BEARER_TOKEN)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(checkEmailBody))
                .build();

        try {
            HttpResponse<String> checkEmailResponse = client.send(checkEmailRequest, BodyHandlers.ofString());

            // Parse the response to get the count
            String responseBody = checkEmailResponse.body();
            Pattern pattern = Pattern.compile("\"COUNT\\(\\*\\)\"\\s*:\\s*(\\d+)");
            Matcher matcher = pattern.matcher(responseBody);

            if (matcher.find()) {
                int count = Integer.parseInt(matcher.group(1));
                return count > 0;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
