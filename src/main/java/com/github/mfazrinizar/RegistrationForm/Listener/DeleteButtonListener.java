package com.github.mfazrinizar.RegistrationForm.Listener;

/*
 * Author       : M. Fazri Nizar
 * Institution  : Sriwijaya University
 * GitHub       : github.com/mfazrinizar
 * File Name    : DeleteButtonListener.java
 */

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.github.mfazrinizar.RegistrationForm.CustomUI.AutoCompleteComboBox;
import com.github.mfazrinizar.RegistrationForm.DatabaseAPI.CheckPasswordUser;
import com.github.mfazrinizar.RegistrationForm.DatabaseAPI.DeleteUserData;
import com.github.mfazrinizar.RegistrationForm.Util.FormValidator;
import com.github.mfazrinizar.RegistrationForm.Util.LabelController;
import com.github.mfazrinizar.RegistrationForm.Util.PasswordHasher;

public class DeleteButtonListener implements ActionListener {
    // Hard-Coded ID dan API Key, seharusnya terdapat di environment variables atau
    // di-request dari server
    // API Credential (ACCOUNT_ID, DATABASE_ID, dan BEARER_TOKEN) sewaktu-waktu
    // dapat berubah
    // Kontak developer jika sudah tidak valid (dari GitHub di atas)
    private final JTextField nameTextField;
    private final JTextField emailTextField;
    private final JPasswordField passwordTextField;
    private final JPasswordField confirmPasswordTextField;
    private final JTextField countryTextField;
    private final JTextField provinceTextField;
    private final JTextField phoneNumberTextField;
    private final JLabel emailErrorLabel;
    private final JLabel countryErrorLabel;
    private final JLabel provinceErrorLabel;
    private final JLabel phoneNumberErrorLabel;
    private final JLabel confirmPasswordErrorLabel;
    private final JLabel passwordErrorLabel;
    private final AutoCompleteComboBox countryComboBox;

    public DeleteButtonListener(JTextField nameTextField, JTextField emailTextField, JPasswordField passwordTextField,
                                JPasswordField confirmPasswordTextField, JTextField countryTextField, JTextField provinceTextField,
                                JTextField phoneNumberTextField, JLabel emailErrorLabel, JLabel countryErrorLabel,
                                JLabel provinceErrorLabel,
                                JLabel phoneNumberErrorLabel, JLabel confirmPasswordErrorLabel, JLabel passwordErrorLabel,
                                AutoCompleteComboBox countryComboBox) {
        this.nameTextField = nameTextField;
        this.emailTextField = emailTextField;
        this.passwordTextField = passwordTextField;
        this.confirmPasswordTextField = confirmPasswordTextField;
        this.countryTextField = countryTextField;
        this.provinceTextField = provinceTextField;
        this.phoneNumberTextField = phoneNumberTextField;
        this.emailErrorLabel = emailErrorLabel;
        this.countryErrorLabel = countryErrorLabel;
        this.provinceErrorLabel = provinceErrorLabel;
        this.phoneNumberErrorLabel = phoneNumberErrorLabel;
        this.confirmPasswordErrorLabel = confirmPasswordErrorLabel;
        this.passwordErrorLabel = passwordErrorLabel;
        this.countryComboBox = countryComboBox;
    }

    private final PasswordHasher hasher = new PasswordHasher();
    private final String ACCOUNT_ID = "YWVmZmYzZjI5NjlhY2ZlNmQwMzAwN2I5NGY4ZDgyMmM=";
    private final String DATABASE_ID = "N2JkMTk4ZTAtZjk4Ni00ZGI2LWFjYTQtOTAwYmQ2ZGI3MmQ5";
    private final String API_URL = "https://api.cloudflare.com/client/v4/accounts/"
            + hasher.urlBase64Decode(ACCOUNT_ID)
            + "/d1/database/"
            + hasher.urlBase64Decode(DATABASE_ID) + "/query";
    private final String BEARER_TOKEN = hasher
            .urlBase64Decode("MmJobm9ycHBObWhsTDlSUjNLLVRaSy1CMmRXbnFUalFZdThralM0ZQ==");

    @Override
    public void actionPerformed(ActionEvent e) {
        CheckPasswordUser passwordChecker = new CheckPasswordUser();
        DeleteUserData userDataDeleter = new DeleteUserData();
        Object[] options = { "TextField", "Database" };
        int n = JOptionPane.showOptionDialog(null,
                "Dari mana Anda ingin menghapus data?",
                "Pilih Sumber Data",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (n == JOptionPane.YES_OPTION) { // TextField dipilih
            LabelController.clearInvalidLabel(emailErrorLabel, countryErrorLabel, provinceErrorLabel,
                    phoneNumberErrorLabel, confirmPasswordErrorLabel, passwordErrorLabel);
            LabelController.clearTextField(nameTextField, emailTextField, countryTextField, provinceTextField,
                    phoneNumberTextField);
            LabelController.clearPasswordTextField(passwordTextField, confirmPasswordTextField);
            countryComboBox.setSelectedIndex(0);
        } else if (n == JOptionPane.NO_OPTION) { // Database dipilih
            JPanel panel = new JPanel(new GridLayout(2, 2));
            JTextField deleteDataEmailField = new JTextField();
            JPasswordField deleteDataPasswordField = new JPasswordField();
            panel.add(new JLabel("Email:"));
            panel.add(deleteDataEmailField);
            panel.add(new JLabel("Password:"));
            panel.add(deleteDataPasswordField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Masukkan email dan password terdaftar",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String inputEmail = deleteDataEmailField.getText().trim();
                String inputPassword = new String(deleteDataPasswordField.getPassword());

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
                                switch (passwordCheckResult) {
                                    case "CORRECT" -> {
                                        int dialogResult = JOptionPane.showConfirmDialog(null,
                                                "Email dan Password benar, klik OK untuk menghapus data", "Konfirmasi",
                                                JOptionPane.OK_CANCEL_OPTION);
                                        if (dialogResult == JOptionPane.OK_OPTION) {
                                            userDataDeleter.deleteUserDataAsync(inputEmail, API_URL, BEARER_TOKEN,
                                                    deleteIsSuccess -> {
                                                        if (deleteIsSuccess) {
                                                            JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
                                                        } else {
                                                            JOptionPane.showMessageDialog(null,
                                                                    "Gagal menghapus data. Coba lagi atau hubungi developer.");
                                                        }
                                                    });
                                        }
                                    }
                                    case "INCORRECT" ->
                                            JOptionPane.showMessageDialog(null, "Email atau Password salah");
                                    case "EMAIL-DOES-NOT-EXIST" ->
                                            JOptionPane.showMessageDialog(null, "Email atau Password salah."); // Menghindari bruteforce
                                    case "BAD-REQUEST" -> JOptionPane.showMessageDialog(null,
                                            "Gagal menghapus data. Coba lagi (kode: " + passwordCheckResult + ")");
                                    case "BAD-RESPONSE" -> JOptionPane.showMessageDialog(null,
                                            "Gagal menghapus data. Cek koneksi internet Anda atau hubungi developer (kode: "
                                                    + passwordCheckResult + ")");
                                    case "BAD-FORMAT" -> JOptionPane.showMessageDialog(null,
                                            "Gagal menghapus data. Hubungi developer (kode: " + passwordCheckResult
                                                    + ")");
                                    default ->
                                            JOptionPane.showMessageDialog(null, "Gagal menghapus data. Hubungi developer.");
                                }
                            });
                }
            }
        }
    }
}

