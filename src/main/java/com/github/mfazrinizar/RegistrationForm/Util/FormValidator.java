package com.github.mfazrinizar.RegistrationForm.Util;

/*
 * Author       : M. Fazri Nizar
 * Institution  : Sriwijaya University
 * GitHub       : github.com/mfazrinizar
 * File Name    : FormValidator.java
 */

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class FormValidator {
    public static boolean[] isNameValid(String name) {
        return new boolean[] { !name.isEmpty(), name.length() <= 100 };
    }

    public static boolean[] isEmailValid(String email) {
        return new boolean[] { email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"),
                email.length() <= 100 };
    }

    public static boolean isCountryValid(String country) {
        return !country.isEmpty();
    }

    public static boolean isProvinceValid(String province) {
        return province.length() <= 100;
    }

    public static boolean[] isPasswordValid(String password) {
        return new boolean[] { password.length() >= 8, password.length() <= 100 };
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.isEmpty() || phoneNumber.matches("^\\+[1-9]\\d{9,14}$");
    }

    public static boolean isPasswordConfirmationValid(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public boolean isMandatoryFieldEmpty(final JLabel nameErrorLabel,
                                         final JLabel emailErrorLabel, final JLabel passwordErrorLabel, final JLabel confirmPasswordErrorLabel,
                                         final JTextField nameTextField,
                                         final JTextField emailTextField,
                                         final JPasswordField passwordTextField, final JPasswordField confirmPasswordTextField,
                                         boolean isUpdate) {
        boolean nameInvalid = false;
        boolean emailInvalid = false;
        boolean passwordInvalid = false;
        boolean confirmPasswordInvalid = false;

        if (nameTextField.getText().trim().isEmpty()) {
            nameErrorLabel.setText("Nama tidak boleh kosong.");
            nameInvalid = true;
        }
        if (emailTextField.getText().trim().isEmpty()) {
            emailErrorLabel.setText("Email tidak boleh kosong.");
            emailInvalid = true;
        }
        if (new String(passwordTextField.getPassword()).isEmpty() && !isUpdate) {
            passwordErrorLabel.setText("Password tidak boleh kosong.");
            passwordInvalid = true;
        }
        if (new String(confirmPasswordTextField.getPassword()).isEmpty() && !isUpdate) {
            confirmPasswordErrorLabel.setText("Konfirmasi Password tidak boleh kosong.");
            confirmPasswordInvalid = true;
        }

        return nameInvalid || emailInvalid || passwordInvalid || confirmPasswordInvalid;
    }

    public boolean isFormValid(String currentName, String currentEmail, String currentPassword,
                               String currentConfirmPassword, String currentCountry,
                               String currentProvince, String currentPhoneNumber, final JLabel nameErrorLabel,
                               final JLabel emailErrorLabel, final JLabel passwordErrorLabel, final JLabel confirmPasswordErrorLabel,
                               final JLabel countryErrorLabel, final JLabel provinceErrorLabel,
                               final JLabel phoneNumberErrorLabel, boolean isUpdate,
                               boolean countryComboBoxDictionaryContainsCurrentCountry) {
        currentName = currentName.trim();
        currentEmail = currentEmail.trim();
        currentCountry = currentCountry.trim();
        currentProvince = currentProvince.trim();
        currentPhoneNumber = currentPhoneNumber.trim();
        // currentPassword = currentPassword;
        // currentConfirmPassword = currentConfirmPassword;

        boolean[] nameInvalid = FormValidator.isNameValid(currentName);
        nameInvalid[0] = !nameInvalid[0];
        nameInvalid[1] = !nameInvalid[1];
        boolean[] emailInvalid = FormValidator.isEmailValid(currentEmail);
        emailInvalid[0] = !emailInvalid[0];
        emailInvalid[1] = !emailInvalid[1];
        boolean[] passwordInvalid = isUpdate ? (currentPassword.isEmpty() ? new boolean[] { true, true }
                : FormValidator.isPasswordValid(currentPassword)) : FormValidator.isPasswordValid(currentPassword);
        passwordInvalid[0] = !passwordInvalid[0];
        passwordInvalid[1] = !passwordInvalid[1];
        boolean[] confirmPasswordInvalid = isUpdate
                ? (currentConfirmPassword.isEmpty() ? new boolean[] { true, true }
                : FormValidator
                .isPasswordValid(currentConfirmPassword))
                : FormValidator
                .isPasswordValid(currentConfirmPassword);
        confirmPasswordInvalid[0] = !confirmPasswordInvalid[0];
        confirmPasswordInvalid[1] = !confirmPasswordInvalid[1];
        boolean phoneNumberInvalid = !FormValidator.isPhoneNumberValid(currentPhoneNumber);
        boolean provinceInvalid = !FormValidator.isProvinceValid(currentProvince);
        boolean passwordConfirmationInvalid = isUpdate ? (!currentConfirmPassword.isEmpty() && !FormValidator.isPasswordConfirmationValid(
                currentPassword,
                currentConfirmPassword))
                : !FormValidator.isPasswordConfirmationValid(
                currentPassword,
                currentConfirmPassword);
        boolean countryComboBoxSelectedInvalid = !currentCountry.isEmpty() && !countryComboBoxDictionaryContainsCurrentCountry;

        if (nameInvalid[0])
            nameErrorLabel.setText("Nama tidak boleh kosong.");
        if (nameInvalid[1])
            nameErrorLabel.setText("Nama tidak boleh lebih dari 100 karakter.");
        if (emailInvalid[0])
            emailErrorLabel.setText("Email tidak valid. Masukkan kembali.");
        if (emailInvalid[1])
            emailErrorLabel.setText("Email tidak boleh lebih dari 100 karakter.");
        if (passwordInvalid[0])
            passwordErrorLabel.setText("Password minimal 8 karakter.");
        if (passwordInvalid[1])
            passwordErrorLabel.setText("Password tidak boleh lebih dari 100 karakter.");
        if (confirmPasswordInvalid[0])
            confirmPasswordErrorLabel.setText("Konfirmasi Password minimal 8 karakter.");
        if (confirmPasswordInvalid[1])
            confirmPasswordErrorLabel.setText("Konfirmasi Password tidak boleh lebih dari 100 karakter.");
        if (phoneNumberInvalid) {
            phoneNumberErrorLabel.setText("Nomor Telepon "
                    + (currentPhoneNumber.length() > 15 - 1 ? "maksimal 15" : "minimal 10")
                    + " digit dengan kode negara (+62821xx)");
        }
        if (provinceInvalid)
            provinceErrorLabel.setText("Provinsi tidak boleh lebih dari 100 karakter");
        if (passwordConfirmationInvalid) {
            passwordErrorLabel.setText("Password tidak sama dengan Konfirmasi Password.");
            confirmPasswordErrorLabel.setText("Konfirmasi Password tidak sama dengan Password.");
        }
        if (countryComboBoxSelectedInvalid) {
            countryErrorLabel.setText("Negara tidak valid. Tulis atau pilih dari dropdown list.");
        }

        // System.out.println("emailInvalid[0] = " + emailInvalid[0] + ",
        // emailInvalid[1] = " + emailInvalid[1] +
        // ", passwordInvalid[0] = " + passwordInvalid[0] + ", passwordInvalid[1] = " +
        // passwordInvalid[1]
        // + ", confirmPasswordInvalid[0] = " + confirmPasswordInvalid[0]
        // + ", confirmPasswordInvalid[1] = " + confirmPasswordInvalid[1] + ",
        // phoneNumberInvalid = "
        // + phoneNumberInvalid + ", provinceInvalid = " + provinceInvalid
        // + ", passwordConfirmationInvalid = " + passwordConfirmationInvalid
        // + ", countryComboBoxSelectedInvalid = " + countryComboBoxSelectedInvalid);
        return !emailInvalid[0] && !emailInvalid[1] && !passwordInvalid[0] && !passwordInvalid[1]
                && !confirmPasswordInvalid[0] && !confirmPasswordInvalid[1] && !phoneNumberInvalid
                && !provinceInvalid && !passwordConfirmationInvalid && !countryComboBoxSelectedInvalid;
    }
}
