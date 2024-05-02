package com.github.mfazrinizar.RegistrationForm.Listener;

/*
 * Author       : M. Fazri Nizar
 * Institution  : Sriwijaya University
 * GitHub       : github.com/mfazrinizar
 * File Name    : GetDataButtonListener.java
 */

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.github.mfazrinizar.RegistrationForm.Credentials.Credentials;
import com.github.mfazrinizar.RegistrationForm.CustomUI.AutoCompleteComboBox;
import com.github.mfazrinizar.RegistrationForm.DatabaseAPI.CheckPasswordUser;
import com.github.mfazrinizar.RegistrationForm.DatabaseAPI.GetUserData;
import com.github.mfazrinizar.RegistrationForm.Util.FormValidator;
import com.github.mfazrinizar.RegistrationForm.Util.GetZonedDateTime;
import com.github.mfazrinizar.RegistrationForm.Util.LabelController;

public class GetDataButtonListener implements ActionListener {
    private final JTextField nameTextField;
    private final JTextField emailTextField;
    private final JPasswordField passwordTextField;
    private final JPasswordField confirmPasswordTextField;
    private final AutoCompleteComboBox countryComboBox;
    private final JTextField provinceTextField;
    private final JTextField phoneNumberTextField;
    private final JLabel nameErrorLabel;
    private final JLabel emailErrorLabel;
    private final JLabel passwordErrorLabel;
    private final JLabel confirmPasswordErrorLabel;
    private final JLabel countryErrorLabel;
    private final JLabel provinceErrorLabel;
    private final JLabel phoneNumberErrorLabel;

    public GetDataButtonListener(JTextField nameTextField, JTextField emailTextField, JPasswordField passwordTextField,
                                 JPasswordField confirmPasswordTextField, AutoCompleteComboBox countryComboBox, JTextField provinceTextField,
                                 JTextField phoneNumberTextField, JLabel nameErrorLabel, JLabel emailErrorLabel, JLabel passwordErrorLabel,
                                 JLabel confirmPasswordErrorLabel, JLabel countryErrorLabel, JLabel provinceErrorLabel,
                                 JLabel phoneNumberErrorLabel) {
        this.nameTextField = nameTextField;
        this.emailTextField = emailTextField;
        this.passwordTextField = passwordTextField;
        this.confirmPasswordTextField = confirmPasswordTextField;
        this.countryComboBox = countryComboBox;
        this.provinceTextField = provinceTextField;
        this.phoneNumberTextField = phoneNumberTextField;
        this.nameErrorLabel = nameErrorLabel;
        this.emailErrorLabel = emailErrorLabel;
        this.passwordErrorLabel = passwordErrorLabel;
        this.confirmPasswordErrorLabel = confirmPasswordErrorLabel;
        this.countryErrorLabel = countryErrorLabel;
        this.provinceErrorLabel = provinceErrorLabel;
        this.phoneNumberErrorLabel = phoneNumberErrorLabel;
    }

    private final Credentials env = new Credentials();
    private final String API_URL = env.getAPI_URL();
    private final String BEARER_TOKEN = env.getBEARER_TOKEN();

    @Override
    public void actionPerformed(ActionEvent e) {
        LabelController.clearInvalidLabel(nameErrorLabel, emailErrorLabel, passwordErrorLabel,
                confirmPasswordErrorLabel, countryErrorLabel, provinceErrorLabel, phoneNumberErrorLabel);

        CheckPasswordUser passwordChecker = new CheckPasswordUser();
        GetZonedDateTime zonedDateTime = new GetZonedDateTime();
        GetUserData getUserData = new GetUserData();

        String name = nameTextField.getText().trim();
        String email = emailTextField.getText().trim();
        String password = new String(passwordTextField.getPassword());
        String confirmPassword = new String(confirmPasswordTextField.getPassword());
        String country = countryComboBox.getItemAt(countryComboBox.getSelectedIndex());
        String province = provinceTextField.getText().trim();
        String phoneNumber = phoneNumberTextField.getText().trim();
        boolean countryComboBoxSelectedItemContainsCurrentCountry = countryComboBox.getDictionary()
                .contains(country);
        FormValidator validator = new FormValidator();

        Object[] options = { "TextField", "Database" };
        int n = JOptionPane.showOptionDialog(null,
                "Dari mana Anda ingin mengambil data?",
                "Pilih Sumber Data",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (n == JOptionPane.YES_OPTION) { // TextField dipilih
            if (validator.isMandatoryFieldEmpty(nameErrorLabel, emailErrorLabel, passwordErrorLabel,
                    confirmPasswordErrorLabel, nameTextField, emailTextField, passwordTextField,
                    confirmPasswordTextField,
                    false)) {
                JOptionPane.showMessageDialog(null,
                        "Semua atau beberapa bidang formulir yang wajib diisi masih kosong.");
            } else if (validator.isFormValid(name, email, password, confirmPassword, country, province, phoneNumber,
                    nameErrorLabel, emailErrorLabel, passwordErrorLabel, confirmPasswordErrorLabel,
                    countryErrorLabel, provinceErrorLabel, phoneNumberErrorLabel, false,
                    countryComboBoxSelectedItemContainsCurrentCountry)) {

                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file + ".txt"))) {
                        writer.write(
                                "Waktu: " + zonedDateTime.getCurrentZonedTime()
                                        + " (WIB, Data Diambil dari TextField Langsung)");
                        writer.newLine();
                        writer.write("Nama: " + nameTextField.getText().trim());
                        writer.newLine();
                        writer.write("Email: " + emailTextField.getText().trim());
                        writer.newLine();
                        writer.write(
                                "Negara: " + countryComboBox.getItemAt(countryComboBox.getSelectedIndex()));
                        writer.newLine();
                        writer.write("Provinsi: " + provinceTextField.getText().trim());
                        writer.newLine();
                        writer.write("Nomor Telepon: " + phoneNumberTextField.getText().trim());
                        writer.newLine();
                        JOptionPane.showMessageDialog(null, "Data Terambil dari TextField.");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } else if (n == JOptionPane.NO_OPTION) { // Database dipilih
            JPanel panel = new JPanel(new GridLayout(2, 2));
            JTextField getDataEmailField = new JTextField();
            JPasswordField getDataPasswordField = new JPasswordField();
            panel.add(new JLabel("Email:"));
            panel.add(getDataEmailField);
            panel.add(new JLabel("Password:"));
            panel.add(getDataPasswordField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Masukkan email dan password terdaftar",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String inputEmail = getDataEmailField.getText().trim();
                String inputPassword = new String(getDataPasswordField.getPassword());

                boolean emailInvalid = !FormValidator.isEmailValid(inputEmail)[0];
                boolean passwordInvalid = !FormValidator.isPasswordValid(inputPassword)[0];

                if (emailInvalid || passwordInvalid) {
                    if (emailInvalid)
                        JOptionPane.showMessageDialog(null, "Email tidak valid.");
                    if (passwordInvalid)
                        JOptionPane.showMessageDialog(null, "Password minimal 8 karakter.");
                } else {
                    passwordChecker.checkPasswordAsync(inputEmail, inputPassword, API_URL, BEARER_TOKEN,
                            passwordCheckResult -> {

                                System.out.println("Password check result: " + passwordCheckResult);

                                switch (passwordCheckResult) {
                                    case "CORRECT" -> {
                                        int dialogResult = JOptionPane.showConfirmDialog(null,
                                                "Email dan Password benar, klik OK untuk melanjutkan", "Konfirmasi",
                                                JOptionPane.OK_CANCEL_OPTION);
                                        if (dialogResult == JOptionPane.OK_OPTION) {

                                            getUserData.getUserDataAsync(inputEmail, API_URL, BEARER_TOKEN, userData -> {
                                                if (userData != null) {
                                                    JFileChooser fileChooser = new JFileChooser();
                                                    if (fileChooser
                                                            .showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                                                        File file = fileChooser.getSelectedFile();
                                                        try (BufferedWriter writer = new BufferedWriter(
                                                                new FileWriter(file + ".txt"))) {
                                                            writer.write(
                                                                    "Waktu Saat Diambil: "
                                                                            + zonedDateTime.getCurrentZonedTime()
                                                                            + " (WIB, Data Diambil dari Database)");
                                                            writer.newLine();
                                                            writer.write("Nama: " + userData[0]);
                                                            writer.newLine();
                                                            writer.write("Email: " + userData[1]);
                                                            writer.newLine();
                                                            writer.write("Negara: " + userData[2]);
                                                            writer.newLine();
                                                            writer.write("Provinsi: " + userData[3]);
                                                            writer.newLine();
                                                            writer.write("Nomor Telepon: " + userData[4]);
                                                            writer.newLine();
                                                            writer.write(
                                                                    "Waktu Saat Terdaftar: " + userData[5]
                                                                            + " (WIB)");
                                                            writer.newLine();
                                                            JOptionPane.showMessageDialog(null,
                                                                    "Data Terambil dari Database");
                                                        } catch (IOException ex) {
                                                            JOptionPane.showMessageDialog(null,
                                                                    "Terdapat masalah. Coba lagi atau hubungi developer. ");
                                                            ex.printStackTrace();
                                                        }
                                                    }
                                                } else {
                                                    JOptionPane.showMessageDialog(null,
                                                            "Gagal mendapatkan data. Coba lagi atau hubungi developer.");
                                                }
                                            });
                                        }
                                    }
                                    case "INCORRECT" ->
                                            JOptionPane.showMessageDialog(null, "Email atau Password salah.");
                                    case "EMAIL-DOES-NOT-EXIST" ->
                                            JOptionPane.showMessageDialog(null, "Email atau Password salah."); // Menghindari


                                    // brute
                                    // force
                                    case "BAD-REQUEST" -> JOptionPane.showMessageDialog(null,
                                            "Gagal mendapatkan data. Coba lagi (kode: " + passwordCheckResult
                                                    + ")");
                                    case "BAD-RESPONSE" -> JOptionPane.showMessageDialog(null,
                                            "Gagal mendapatkan data. Cek koneksi internet Anda atau hubungi developer (kode: "
                                                    + passwordCheckResult + ")");
                                    case "BAD-FORMAT" -> JOptionPane.showMessageDialog(null,
                                            "Gagal mendapatkan data. Hubungi developer (kode: "
                                                    + passwordCheckResult
                                                    + ")");
                                    default -> JOptionPane.showMessageDialog(null,
                                            "Gagal mendapatkan data. Hubungi developer.");
                                }
                            });
                }
            }
        }
    }
}

