package com.github.mfazrinizar.RegistrationForm.DatabaseAPI;

/*
 * Author       : M. Fazri Nizar
 * Institution  : Sriwijaya University
 * GitHub       : github.com/mfazrinizar
 * File Name    : UpdateUserData.java
 */

import java.awt.Frame;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.NoSuchAlgorithmException;
import java.util.function.Consumer;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.github.mfazrinizar.RegistrationForm.Util.PasswordHasher;

public class UpdateUserData {

    private class UpdateUserTask extends SwingWorker<Boolean, Void> {
        private final String API_URL;
        private final String BEARER_TOKEN;
        private final JDialog progressDialog;
        private final Consumer<Boolean> callback;
        private final String name;
        private final String email;
        private final String password;
        private final String country;
        private final String province;
        private final String phoneNumber;
        private final String originalEmail;

        public UpdateUserTask(String API_URL, String BEARER_TOKEN, String name, String email, String password,
                              String country, String province, String phoneNumber, String originalEmail, Consumer<Boolean> callback) {
            this.API_URL = API_URL;
            this.BEARER_TOKEN = BEARER_TOKEN;
            this.name = name;
            this.email = email;
            this.password = password;
            this.country = country;
            this.province = province;
            this.phoneNumber = phoneNumber;
            this.callback = callback;
            this.originalEmail = originalEmail;

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

            boolean result = updateUserData(API_URL, BEARER_TOKEN, name, email, password, country, province,
                    phoneNumber, originalEmail);

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

    public void updateUserAsync(String API_URL, String BEARER_TOKEN, String name, String email, String password,
                                String country, String province, String phoneNumber, String originalEmail, Consumer<Boolean> callback) {
        new UpdateUserTask(API_URL, BEARER_TOKEN, name, email, password, country, province, phoneNumber, originalEmail,
                callback)
                .execute();
    }

    private boolean updateUserData(final String API_URL, final String BEARER_TOKEN, String name, String email,
                                   String password, String country, String province,
                                   String phoneNumber, String emailOrigin) {
        final String UPDATE_QUERY_WITH_PASSWORD = "UPDATE registration_details SET name = COALESCE(NULLIF(?, ''), name), email = COALESCE(NULLIF(?, ''), email), country = COALESCE(NULLIF(?, ''), country), province = COALESCE(NULLIF(?, ''), province), phone_number = COALESCE(NULLIF(?, ''), phone_number), password = COALESCE(NULLIF(?, ''), password), salt = COALESCE(NULLIF(?, ''), salt) WHERE email = ?;";
        final String UPDATE_QUERY_WITHOUT_PASSWORD = "UPDATE registration_details SET name = COALESCE(NULLIF(?, ''), name), email = COALESCE(NULLIF(?, ''), email), country = COALESCE(NULLIF(?, ''), country), province = COALESCE(NULLIF(?, ''), province), phone_number = COALESCE(NULLIF(?, ''), phone_number) WHERE email = ?;";
        HttpClient client = HttpClient.newHttpClient();

        PasswordHasher hasher = new PasswordHasher();
        byte[] salt;
        try {
            salt = hasher.getSalt();
        } catch (NoSuchAlgorithmException e1) {
            JOptionPane.showMessageDialog(null, "Terdapat masalah, coba ganti password atau tekan tombol Daftar lagi.");
            return false;
        }

        String encodedSalt = hasher.encodeToBase64(salt);

        String[] params = {
                name.trim().isEmpty() ? "" : name,
                email.trim().isEmpty() ? "" : email,
                country.trim().isEmpty() ? "" : country,
                province.trim().isEmpty() ? "" : province,
                phoneNumber.trim().isEmpty() ? "" : phoneNumber,
                password.isEmpty() ? "" : hasher.getHashedPassword(password, salt),
                password.isEmpty() ? "" : encodedSalt,
                emailOrigin
        };

        String sql = password.isEmpty() ? UPDATE_QUERY_WITHOUT_PASSWORD : UPDATE_QUERY_WITH_PASSWORD;

        String bodyWithPassword = String.format("{\n" +
                        "\"params\": [\n" +
                        "  \"%s\",\n" +
                        "  \"%s\",\n" +
                        "  \"%s\",\n" +
                        "  \"%s\",\n" +
                        "  \"%s\",\n" +
                        "  \"%s\",\n" +
                        "  \"%s\",\n" +
                        "  \"%s\"\n" +
                        "],\n" +
                        "\"sql\": \"%s\"\n" +
                        "}",
                params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], sql);
        String bodyWithoutPassword = String.format("{\n" +
                        "\"params\": [\n" +
                        "  \"%s\",\n" +
                        "  \"%s\",\n" +
                        "  \"%s\",\n" +
                        "  \"%s\",\n" +
                        "  \"%s\",\n" +
                        "  \"%s\"\n" +
                        "],\n" +
                        "\"sql\": \"%s\"\n" +
                        "}",
                params[0], params[1], params[2], params[3], params[4], params[7], sql);
        // System.out.println(java.util.Arrays.toString(params));
        // System.out.println(bodyWithoutPassword);
        // System.out.println(bodyWithPassword);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + BEARER_TOKEN)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(password.isEmpty() ? bodyWithoutPassword : bodyWithPassword))
                .build();

        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            // System.out.println(response.body());
            return response.statusCode() == 200;

        } catch (IOException | InterruptedException e1) {
            e1.printStackTrace();
            return false;
        }
    }
}
