package com.github.mfazrinizar.RegistrationForm.DatabaseAPI;

/*
 * Author       : M. Fazri Nizar
 * Institution  : Sriwijaya University
 * GitHub       : github.com/mfazrinizar
 * File Name    : GetUserData.java
 */

import java.awt.Frame;
import java.io.IOException;
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

public class GetUserData {
    class GetUserDataTask extends SwingWorker<String[], Void> {
        private final String inputEmail;
        private final String API_URL;
        private final String BEARER_TOKEN;
        private final JDialog progressDialog;
        private final Consumer<String[]> callback;

        public GetUserDataTask(String inputEmail, String API_URL, String BEARER_TOKEN,
                               Consumer<String[]> callback) {
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
        protected String[] doInBackground() throws Exception {
            SwingUtilities.invokeLater(() -> progressDialog.setVisible(true));

            String[] result = getUserData(inputEmail, API_URL, BEARER_TOKEN);

            SwingUtilities.invokeLater(() -> progressDialog.setVisible(false));

            return result;
        }

        @Override
        protected void done() {
            try {
                String[] result = get();
                callback.accept(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getUserDataAsync(String inputEmail, String API_URL, String BEARER_TOKEN,
                                 Consumer<String[]> callback) {
        new GetUserDataTask(inputEmail, API_URL, BEARER_TOKEN, callback).execute();
    }

    private String[] getUserData(String inputEmail, final String API_URL, final String BEARER_TOKEN) {
        HttpClient client = HttpClient.newHttpClient();

        final String GET_USER_QUERY = "SELECT name, email, country, province, phone_number, registered_time FROM registration_details WHERE email = ?;";

        String body = String.format("{\n" +
                        "\"params\": [\n" +
                        "  \"%s\"\n" +
                        "],\n" +
                        "\"sql\": \"%s\"\n" +
                        "}",
                inputEmail,
                GET_USER_QUERY);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + BEARER_TOKEN)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                Pattern pattern = Pattern.compile(
                        "\"name\"\\s*:\\s*\"(.*)\",\\s*\"email\"\\s*:\\s*\"(.*)\",\\s*\"country\"\\s*:\\s*\"(.*)\",\\s*\"province\"\\s*:\\s*\"(.*)\",\\s*\"phone_number\"\\s*:\\s*\"(.*)\",\\s*\"registered_time\"\\s*:\\s*\"(.*)\"");
                Matcher matcher = pattern.matcher(responseBody);

                if (matcher.find()) {
                    String name = matcher.group(1);
                    String email = matcher.group(2);
                    String country = matcher.group(3);
                    String province = matcher.group(4);
                    String phoneNumber = matcher.group(5);
                    String registeredTime = matcher.group(6);

                    return new String[] { name, email, country, province, phoneNumber, registeredTime };
                } else {
                    return null;
                }
            } else {
                return null;
            }

        } catch (IOException | InterruptedException e1) {
            e1.printStackTrace();
            return null;
        }
    }
}

