package com.github.mfazrinizar.RegistrationForm.Listener;

/*
 * Author       : M. Fazri Nizar
 * Institution  : Sriwijaya University
 * GitHub       : github.com/mfazrinizar
 * File Name    : RegisterButtonListener.java
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.github.mfazrinizar.RegistrationForm.Util.FormValidator;
import com.github.mfazrinizar.RegistrationForm.Util.LabelFieldController;
import com.github.mfazrinizar.RegistrationForm.Credentials.Credentials;
import com.github.mfazrinizar.RegistrationForm.CustomUI.AutoCompleteComboBox;
import com.github.mfazrinizar.RegistrationForm.DatabaseAPI.CheckUserEmail;
import com.github.mfazrinizar.RegistrationForm.DatabaseAPI.RegisterUserData;

public class RegisterButtonListener implements ActionListener {
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

    public RegisterButtonListener(JTextField nameTextField, JTextField emailTextField, JPasswordField passwordTextField,
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
        CheckUserEmail emailChecker = new CheckUserEmail();
        RegisterUserData registerUser = new RegisterUserData();
        LabelFieldController.clearInvalidLabel(nameErrorLabel, emailErrorLabel, passwordErrorLabel,
                confirmPasswordErrorLabel, countryErrorLabel, provinceErrorLabel, phoneNumberErrorLabel);

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

        if (validator.isMandatoryFieldEmpty(nameErrorLabel, emailErrorLabel, passwordErrorLabel,
                confirmPasswordErrorLabel,
                nameTextField, emailTextField, passwordTextField, confirmPasswordTextField,
                false)) {
            JOptionPane.showMessageDialog(null,
                    "Semua atau beberapa bidang formulir yang wajib diisi masih kosong.");
        } else {
            if (validator.isFormValid(name, email, password, confirmPassword, country, province, phoneNumber,
                    nameErrorLabel,
                    emailErrorLabel, passwordErrorLabel, confirmPasswordErrorLabel, countryErrorLabel,
                    provinceErrorLabel, phoneNumberErrorLabel, false,
                    countryComboBoxSelectedItemContainsCurrentCountry)) {
                emailChecker.doesEmailExistAsync(emailTextField.getText().trim(), API_URL, BEARER_TOKEN, exists -> {
                    if (exists) {
                        emailErrorLabel.setText("Email sudah terdaftar.");
                        JOptionPane.showMessageDialog(null, "Email sudah terdaftar. Gunakan email lain.");
                    } else {
                        registerUser.registerUserAsync(API_URL, BEARER_TOKEN, name, email, country, province,
                                phoneNumber, password, success -> {
                                    if (success) {
                                        System.out.println("Registrasi Sukses.");
                                    } else {
                                        System.out.println("Registrasi Gagal.");
                                    }
                                });
                    }
                });
            }
        }
    }
}

