package com.github.mfazrinizar.RegistrationForm.DatabaseAPI;

/*
 * Author       : M. Fazri Nizar
 * Institution  : Sriwijaya University
 * GitHub       : github.com/mfazrinizar
 * File Name    : CheckPasswordUser.java
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

import com.github.mfazrinizar.RegistrationForm.Util.PasswordHasher;

public class CheckPasswordUser {
    private class CheckPasswordTask extends SwingWorker<String, Void> {
        private final String inputEmail;
        private final String inputPassword;
        private final String API_URL;
        private final String BEARER_TOKEN;
        private final JDialog progressDialog;
        private final Consumer<String> callback;

        public CheckPasswordTask(String inputEmail, String inputPassword, String API_URL, String BEARER_TOKEN,
                                 Consumer<String> callback) {
            this.inputEmail = inputEmail;
            this.inputPassword = inputPassword;
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
        protected String doInBackground() throws Exception {
            SwingUtilities.invokeLater(() -> progressDialog.setVisible(true));

            String result = checkPassword(inputEmail, inputPassword, API_URL, BEARER_TOKEN);

            SwingUtilities.invokeLater(() -> progressDialog.setVisible(false));

            return result;
        }

        @Override
        protected void done() {
            try {
                String result = get();
                callback.accept(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void checkPasswordAsync(String inputEmail, String inputPassword, String API_URL, String BEARER_TOKEN,
                                   Consumer<String> callback) {
        new CheckPasswordTask(inputEmail, inputPassword, API_URL, BEARER_TOKEN, callback).execute();
    }

    private String checkPassword(String inputEmail, String inputPassword, final String API_URL,
                                 final String BEARER_TOKEN) {
        final String GET_USER_QUERY = "SELECT password, salt FROM registration_details WHERE email = ?;";
        HttpClient client = HttpClient.newHttpClient();
        PasswordHasher hasher = new PasswordHasher();

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
            // System.out.println(response.body() + "walawe");
            // System.out.println(response.toString());
            // System.out.println(response.body().toString() + " apa ini we");

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                Pattern pattern = Pattern.compile("\"password\"\\s*:\\s*\"(.*)\",\\s*\"salt\"\\s*:\\s*\"(.*)\"");
                Pattern patternEmpty = Pattern.compile("\"results\"\\s*:\\s*\\[\\]");
                Matcher matcher = pattern.matcher(responseBody);
                Matcher matcherEmpty = patternEmpty.matcher(responseBody);

                if (matcher.find()) {
                    String password = matcher.group(1);
                    // System.out.println(password);
                    String salt = matcher.group(2);
                    // System.out.println(salt);

                    String hashedInputPassword = hasher.getHashedPassword(inputPassword, hasher.base64Decode(salt));
                    if (hashedInputPassword.equals(password)) {
                        return "CORRECT";
                    } else {
                        return "INCORRECT";
                    }
                } else if (matcherEmpty.find()) {
                    return "EMAIL-DOES-NOT-EXIST";
                } else {
                    return "BAD-FORMAT";
                }
            } else {
                return "BAD-RESPONSE";
            }

        } catch (IOException | InterruptedException e1) {
            e1.printStackTrace();
            return "BAD-REQUEST";
        }
    }
}

