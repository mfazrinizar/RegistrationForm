package com.github.mfazrinizar.RegistrationForm.DatabaseAPI;

/*
 * Author       : M. Fazri Nizar
 * Institution  : Sriwijaya University
 * GitHub       : github.com/mfazrinizar
 * File Name    : DeleteUserData.java
 */

import java.awt.Frame;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;

import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class DeleteUserData {
    private class DeleteUserDataTask extends SwingWorker<Boolean, Void> {
        private final String inputEmail;
        private final String API_URL;
        private final String BEARER_TOKEN;
        private final JDialog progressDialog;
        private final Consumer<Boolean> callback;

        public DeleteUserDataTask(String inputEmail, String API_URL, String BEARER_TOKEN, Consumer<Boolean> callback) {
            this.inputEmail = inputEmail;
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

            boolean result = deleteUserData(inputEmail, API_URL, BEARER_TOKEN);

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

    public void deleteUserDataAsync(String inputEmail, String API_URL, String BEARER_TOKEN,
                                    Consumer<Boolean> callback) {
        new DeleteUserDataTask(inputEmail, API_URL, BEARER_TOKEN, callback).execute();
    }

    private boolean deleteUserData(String inputEmail, final String API_URL, final String BEARER_TOKEN) {
        final String DELETE_USER_QUERY = "DELETE FROM registration_details WHERE email = ?;";
        HttpClient client = HttpClient.newHttpClient();

        String body = String.format("{\n" +
                        "\"params\": [\n" +
                        "  \"%s\"\n" +
                        "],\n" +
                        "\"sql\": \"%s\"\n" +
                        "}",
                inputEmail,
                DELETE_USER_QUERY);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + BEARER_TOKEN)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;

        } catch (IOException | InterruptedException e1) {
            e1.printStackTrace();
            return false;
        }
    }
}

