package com.github.mfazrinizar.RegistrationForm.Listener;

/*
 * Author       : M. Fazri Nizar
 * Institution  : Sriwijaya University
 * GitHub       : github.com/mfazrinizar
 * File Name    : UpdateButtonListener.java
 */

import java.util.List;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.github.mfazrinizar.RegistrationForm.Credentials.Credentials;
import com.github.mfazrinizar.RegistrationForm.CustomUI.AutoCompleteComboBox;
import com.github.mfazrinizar.RegistrationForm.DatabaseAPI.CheckPasswordUser;
import com.github.mfazrinizar.RegistrationForm.DatabaseAPI.GetUserData;
import com.github.mfazrinizar.RegistrationForm.DatabaseAPI.UpdateUserData;
import com.github.mfazrinizar.RegistrationForm.Util.FormValidator;
import com.github.mfazrinizar.RegistrationForm.Util.LabelController;

public class UpdateButtonListener implements ActionListener {
    private JTextField nameTextField, emailTextField, provinceTextField, phoneNumberTextField;
    private JPasswordField passwordTextField, confirmPasswordTextField;
    private JLabel nameErrorLabel, emailErrorLabel, passwordErrorLabel, confirmPasswordErrorLabel,
            countryErrorLabel, provinceErrorLabel,
            phoneNumberErrorLabel;
    private AutoCompleteComboBox countryComboBox;
    private String originalName, originalEmail, originalCountry, originalProvince, originalPhoneNumber;
    private final List<String> countries;

    public UpdateButtonListener(JTextField nameTextField, JTextField emailTextField, JPasswordField passwordTextField,
                                JPasswordField confirmPasswordTextField, AutoCompleteComboBox countryComboBox, JTextField provinceTextField,
                                JTextField phoneNumberTextField, JLabel nameErrorLabel, JLabel emailErrorLabel, JLabel passwordErrorLabel,
                                JLabel confirmPasswordErrorLabel, JLabel countryErrorLabel, JLabel provinceErrorLabel,
                                JLabel phoneNumberErrorLabel, List<String> countries) {
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
        this.countries = countries;
    }

    private final Credentials env = new Credentials();
    private final String API_URL = env.getAPI_URL();
    private final String BEARER_TOKEN = env.getBEARER_TOKEN();

    @Override
    public void actionPerformed(ActionEvent e) {
        CheckPasswordUser passwordChecker = new CheckPasswordUser();
        GetUserData userDataGetter = new GetUserData();

        // Create a new dialog with email and password fields
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField emailInputField = new JTextField();
        JPasswordField passwordInputField = new JPasswordField();
        panel.add(new JLabel("Email:"));
        panel.add(emailInputField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordInputField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Masukkan email dan password terdaftar",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String inputEmail = emailInputField.getText();
            String inputPassword = new String(passwordInputField.getPassword());

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
                                            "Email dan Password benar, tekan OK untuk melanjutkan", "Konfirmasi",
                                            JOptionPane.OK_CANCEL_OPTION);
                                    if (dialogResult == JOptionPane.OK_OPTION) {
                                        userDataGetter.getUserDataAsync(inputEmail, API_URL, BEARER_TOKEN, userData -> {
                                            if (userData != null) {
                                                final JDialog updateDialog = new JDialog();
                                                updateDialog.setModal(true);
                                                JPanel updatePanel = new JPanel(new GridLayout(0, 2, 5, 5));
                                                nameTextField = new JTextField();
                                                emailTextField = new JTextField();
                                                passwordTextField = new JPasswordField();
                                                confirmPasswordTextField = new JPasswordField();
                                                countryComboBox = new AutoCompleteComboBox(countries);
                                                provinceTextField = new JTextField();
                                                phoneNumberTextField = new JTextField();

                                                nameErrorLabel = new JLabel("");
                                                nameErrorLabel.setForeground(Color.RED);

                                                emailErrorLabel = new JLabel("");
                                                emailErrorLabel.setForeground(Color.RED);

                                                passwordErrorLabel = new JLabel("");
                                                passwordErrorLabel.setForeground(Color.RED);

                                                provinceErrorLabel = new JLabel("");
                                                provinceErrorLabel.setForeground(Color.RED);

                                                countryErrorLabel = new JLabel("");
                                                countryErrorLabel.setForeground(Color.RED);

                                                confirmPasswordErrorLabel = new JLabel("");
                                                confirmPasswordErrorLabel.setForeground(Color.RED);

                                                countryErrorLabel = new JLabel("");
                                                countryErrorLabel.setForeground(Color.RED);

                                                phoneNumberErrorLabel = new JLabel("");
                                                phoneNumberErrorLabel.setForeground(Color.RED);

                                                JPanel namePanel = new JPanel(new BorderLayout());
                                                namePanel.add(nameTextField, BorderLayout.CENTER);
                                                namePanel.add(nameErrorLabel, BorderLayout.PAGE_END);

                                                JPanel emailPanel = new JPanel(new BorderLayout());
                                                emailPanel.add(emailTextField, BorderLayout.CENTER);
                                                emailPanel.add(emailErrorLabel, BorderLayout.PAGE_END);

                                                JPanel passwordPanel = new JPanel(new BorderLayout());
                                                passwordPanel.add(passwordTextField, BorderLayout.CENTER);
                                                passwordPanel.add(passwordErrorLabel, BorderLayout.PAGE_END);

                                                JPanel confirmPasswordPanel = new JPanel(new BorderLayout());
                                                confirmPasswordPanel.add(confirmPasswordTextField, BorderLayout.CENTER);
                                                confirmPasswordPanel.add(confirmPasswordErrorLabel,
                                                        BorderLayout.PAGE_END);

                                                JPanel provincePanel = new JPanel(new BorderLayout());
                                                provincePanel.add(provinceTextField, BorderLayout.CENTER);
                                                provincePanel.add(provinceErrorLabel, BorderLayout.PAGE_END);

                                                JPanel countryPanel = new JPanel(new BorderLayout());
                                                countryPanel.add(countryComboBox, BorderLayout.CENTER);
                                                countryPanel.add(countryErrorLabel, BorderLayout.PAGE_END);

                                                JPanel phoneNumberPanel = new JPanel(new BorderLayout());
                                                phoneNumberPanel.add(phoneNumberTextField, BorderLayout.CENTER);
                                                phoneNumberPanel.add(phoneNumberErrorLabel, BorderLayout.PAGE_END);

                                                updatePanel.add(new JLabel("Nama:"));
                                                updatePanel.add(namePanel);
                                                updatePanel.add(new JLabel("Email:"));
                                                updatePanel.add(emailPanel);
                                                updatePanel.add(new JLabel("Password Baru:"));
                                                updatePanel.add(passwordPanel);
                                                updatePanel.add(new JLabel("Konfirmasi Password Baru:"));
                                                updatePanel.add(confirmPasswordPanel);
                                                updatePanel.add(new JLabel("Negara:"));
                                                updatePanel.add(countryPanel);
                                                updatePanel.add(new JLabel("Provinsi:"));
                                                updatePanel.add(provincePanel);
                                                updatePanel.add(new JLabel("Nomor Telepon:"));
                                                updatePanel.add(phoneNumberPanel);

                                                JButton updateInsideButton = new JButton("Ubah/Perbarui");
                                                JButton cancelButton = new JButton("Batal");

                                                updateInsideButton
                                                        .addActionListener(
                                                                new UpdateInsideButtonActionListener(updateDialog));
                                                cancelButton
                                                        .addActionListener(
                                                                new CancelButtonActionListener(updateDialog));

                                                updatePanel.add(updateInsideButton);
                                                updatePanel.add(cancelButton);

                                                JPanel contentPanel = new JPanel(new BorderLayout());
                                                contentPanel.add(
                                                        new JLabel("(Hanya Ubah Data yang Ingin Diubah)",
                                                                JLabel.CENTER),
                                                        BorderLayout.NORTH);
                                                contentPanel.add(updatePanel, BorderLayout.CENTER);

                                                updateDialog.add(contentPanel);
                                                updateDialog.setTitle("Ubah Data Registrasi");
                                                updateDialog.pack();

                                                updateDialog.addWindowListener(new WindowAdapter() {
                                                    @Override
                                                    public void windowClosing(WindowEvent e) {
                                                        updateDialog.dispose();
                                                    }
                                                });

                                                originalName = userData[0];
                                                originalEmail = userData[1];
                                                originalCountry = userData[2];
                                                originalProvince = userData[3];
                                                originalPhoneNumber = userData[4];

                                                nameTextField.setText(originalName);
                                                emailTextField.setText(originalEmail);
                                                countryComboBox.setSelectedItem(originalCountry);
                                                provinceTextField.setText(originalProvince);
                                                phoneNumberTextField.setText(originalPhoneNumber);

                                                updateDialog.setLocationRelativeTo(null);
                                                updateDialog.setVisible(true);
                                            } else {
                                                JOptionPane.showMessageDialog(null,
                                                        "Gagal mendapatkan data. Coba lagi atau hubungi developer.");
                                            }
                                        });
                                    }
                                }
                                case "INCORRECT" -> JOptionPane.showMessageDialog(null, "Email atau Password salah.");
                                case "EMAIL-DOES-NOT-EXIST" ->
                                        JOptionPane.showMessageDialog(null, "Email atau Password salah."); // Menghindari

                                // brute
                                // force
                                case "BAD-REQUEST" -> JOptionPane.showMessageDialog(null,
                                        "Gagal mendapatkan data. Coba lagi (kode: " + passwordCheckResult + ")");
                                case "BAD-RESPONSE" -> JOptionPane.showMessageDialog(null,
                                        "Gagal mendapatkan data. Cek koneksi internet Anda atau hubungi developer (kode: "
                                                + passwordCheckResult + ")");
                                case "BAD-FORMAT" -> JOptionPane.showMessageDialog(null,
                                        "Gagal mendapatkan data. Hubungi developer (kode: " + passwordCheckResult
                                                + ")");
                                default ->
                                        JOptionPane.showMessageDialog(null, "Gagal mendapatkan data. Hubungi developer.");
                            }
                        });
            }
        }
    }

    private boolean allFormsAreSame(String currentName, String currentEmail, String currentPassword,
                                    String currentConfirmPassword, String currentCountry,
                                    String currentProvince, String currentPhoneNumber) {
//        System.out.println("currentName: " + currentName + ", originalName: " + originalName +
//                ", currentEmail: " + currentEmail + ", originalEmail: " + originalEmail + ", currentCountry: "
//                + currentCountry + ", originalCountry: " + originalCountry + ", currentProvince: "
//                + currentProvince + ", originalProvince: " + originalProvince + ", currentPhoneNumber: "
//                + currentPhoneNumber + ", originalPhoneNumber: " + originalPhoneNumber
//                + ", currentPassword: " + currentPassword + ", currentConfirmPassword: "
//                + currentConfirmPassword);
//        System.out.println("currentName.equals(originalName): " + currentName.equals(originalName)
//                + ", currentEmail.equals(originalEmail): " + currentEmail.equals(originalEmail)
//                + ", currentCountry.equals(originalCountry): " + currentCountry.equals(originalCountry)
//                + ", currentProvince.equals(originalProvince): " + currentProvince.equals(originalProvince)
//                + ", currentPhoneNumber.equals(originalPhoneNumber): "
//                + currentPhoneNumber.equals(originalPhoneNumber) + ", currentPassword.isEmpty(): "
//                + currentPassword.isEmpty() + ", currentConfirmPassword.isEmpty(): "
//                + currentConfirmPassword.isEmpty());
        return currentName.equals(originalName) && currentEmail.equals(originalEmail)
                && currentCountry.equals(originalCountry) && currentProvince.equals(originalProvince)
                && currentPhoneNumber.equals(originalPhoneNumber) && currentPassword.isEmpty()
                && currentConfirmPassword.isEmpty();
    }

    class UpdateInsideButtonActionListener implements ActionListener {
        private final JDialog dialogToDispose;

        public UpdateInsideButtonActionListener(JDialog dialogToDispose) {
            this.dialogToDispose = dialogToDispose;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Update only the fields that are different from before
            // For the Password and Konfirmasi Password fields, leave them blank. If they
            // have inputs, validate them, and if valid, update them too.
            UpdateUserData userDataUpdater = new UpdateUserData();
            LabelController.clearInvalidLabel(nameErrorLabel, emailErrorLabel, passwordErrorLabel,
                    confirmPasswordErrorLabel, countryErrorLabel, provinceErrorLabel, phoneNumberErrorLabel);

            String name = nameTextField.getText().trim();
            String email = emailTextField.getText().trim();
            String password = new String(passwordTextField.getPassword());
            String confirmPassword = new String(confirmPasswordTextField.getPassword());
            String country = countryComboBox.getItemAt(countryComboBox.getSelectedIndex()) == null ? ""
                    : countryComboBox.getItemAt(countryComboBox.getSelectedIndex()).trim();
            String province = provinceTextField.getText().trim();
            String phoneNumber = phoneNumberTextField.getText();
            boolean countryComboBoxDictionaryContainsDirectoryCountry = countryComboBox.getDictionary()
                    .contains(country);
            FormValidator validator = new FormValidator();

            // private boolean isFormValid(String currentName, String currentEmail, String
            // currentCountry,
            // String currentProvince, String currentPhoneNumber, String currentPassword,
            // String currentConfirmPassword) {

            if (validator.isMandatoryFieldEmpty(nameErrorLabel, emailErrorLabel, passwordErrorLabel,
                    confirmPasswordErrorLabel,
                    nameTextField, emailTextField, passwordTextField, confirmPasswordTextField,
                    true)) {
                JOptionPane.showMessageDialog(null,
                        "Semua atau beberapa bidang formulir yang wajib diisi tidak boleh kosong.");
            } else {
                System.out.println("Masuk else");
                if (allFormsAreSame(name, email, password, confirmPassword, country, province, phoneNumber)) {
                    JOptionPane.showMessageDialog(null,
                            "Tidak ada data yang diubah. Silakan ubah data yang ingin diubah atau klik Batal untuk keluar.");
                } else if (validator.isFormValid(name, email, password, confirmPassword, country, province,
                        phoneNumber,
                        nameErrorLabel, emailErrorLabel, passwordErrorLabel, confirmPasswordErrorLabel,
                        countryErrorLabel, provinceErrorLabel, phoneNumberErrorLabel, true,
                        countryComboBoxDictionaryContainsDirectoryCountry)) {
                    // System.out.println(same + " " + valid);
                    System.out.println("Masuk else if");

                    name = name.equals(originalName) ? "" : name;
                    email = email.equals(originalEmail) ? "" : email;
                    country = country.equals(originalCountry) ? "" : country;
                    province = province.equals(originalProvince) ? "" : province;
                    phoneNumber = phoneNumber.equals(originalPhoneNumber) ? "" : phoneNumber;

                    userDataUpdater.updateUserAsync(API_URL, BEARER_TOKEN, name, email, password, country, province,
                            phoneNumber,
                            originalEmail,
                            result -> {
                                if (result) {
                                    JOptionPane.showMessageDialog(null, "Data registrasi berhasil diperbarui.");
                                    dialogToDispose.dispose();
                                } else {
                                    JOptionPane.showMessageDialog(null,
                                            "Data gagal diperbarui. Coba lagi atau hubungi developer.");
                                }
                            });
                }
            }
            // System.out.println(valid + " " + same);
        }
    }

    static class CancelButtonActionListener implements ActionListener {
        private final JDialog dialogToDispose;

        public CancelButtonActionListener(JDialog dialogToDispose) {
            this.dialogToDispose = dialogToDispose;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            dialogToDispose.dispose();
        }
    }
}

