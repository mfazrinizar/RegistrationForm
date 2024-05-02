package com.github.mfazrinizar.RegistrationForm.DatabaseAPI;

/*
 * Author       : M. Fazri Nizar
 * Institution  : Sriwijaya University
 * GitHub       : github.com/mfazrinizar
 * File Name    : RegisterUserData.java
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

import com.github.mfazrinizar.RegistrationForm.Util.GetZonedDateTime;
import com.github.mfazrinizar.RegistrationForm.Util.PasswordHasher;

public class RegisterUserData {

    private class RegisterUserTask extends SwingWorker<Boolean, Void> {
        private final String API_URL;
        private final String BEARER_TOKEN;
        private final JDialog progressDialog;
        private final Consumer<Boolean> callback;
        String name;
        String email;
        String country;
        String province;
        String phoneNumber;
        String password;

        public RegisterUserTask(String API_URL, String BEARER_TOKEN, String name, String email, String country,
                                String province, String phoneNumber, String password, Consumer<Boolean> callback) {
            this.API_URL = API_URL;
            this.BEARER_TOKEN = BEARER_TOKEN;
            this.name = name;
            this.email = email;
            this.country = country;
            this.province = province;
            this.phoneNumber = phoneNumber;
            this.password = password;
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

            boolean result = registerUser(API_URL, BEARER_TOKEN, name, email, password, country, province,
                    phoneNumber);

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

    public void registerUserAsync(String API_URL, String BEARER_TOKEN, String name,
                                  String email, String password, String country, String province, String phoneNumber,
                                  Consumer<Boolean> callback) {
        new RegisterUserTask(API_URL, BEARER_TOKEN, name, email, password, country, province, phoneNumber, callback)
                .execute();
    }

    private boolean registerUser(final String API_URL, final String BEARER_TOKEN, String name, String email,
                                 String password, String country, String province, String phoneNumber) {
        final String REGISTER_QUERY = "INSERT INTO registration_details (name, email, country, province, phone_number, password, salt, registered_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        GetZonedDateTime getZonedDateTime = new GetZonedDateTime();
        String registeredTime = getZonedDateTime.getCurrentZonedTime();
        HttpClient client = HttpClient.newHttpClient();

        PasswordHasher hasher = new PasswordHasher();
        byte[] salt = null;
        try {
            salt = hasher.getSalt();
        } catch (NoSuchAlgorithmException e1) {
            JOptionPane.showMessageDialog(null,
                    "Terdapat masalah, coba ganti password atau tekan tombol Daftar lagi.");
            return false;
        }

        String encodedSalt = hasher.encodeToBase64(salt);

        String body = String.format("{\n" +
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
                name.trim(),
                email.trim(),
                country.trim(),
                province.trim(),
                phoneNumber.trim(),
                new String(hasher.getHashedPassword(password, salt)),
                encodedSalt,
                registeredTime,
                REGISTER_QUERY);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + BEARER_TOKEN)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JOptionPane.showMessageDialog(null, "Berhasil didaftarkan.");
                return true;
            } else {
                JOptionPane.showMessageDialog(null,
                        "Gagal didaftarkan. Cek koneksi internet Anda atau hubungi developer.");
                return false;
            }

        } catch (IOException | InterruptedException e1) {
            JOptionPane.showMessageDialog(null, "Gagal didaftarkan. Coba lagi.");
            e1.printStackTrace();
            return false;
        }
    }
}

