package com.github.mfazrinizar.RegistrationForm.CustomUI;

// Initialized for github.com/mfazrinizar

/*
 * Author       : M. Fazri Nizar
 * Institution  : Sriwijaya University
 * GitHub       : github.com/mfazrinizar
 * File Name    : RegistrationForm.java
 */

import com.github.mfazrinizar.RegistrationForm.Listener.DeleteButtonListener;
import com.github.mfazrinizar.RegistrationForm.Listener.GetDataButtonListener;
import com.github.mfazrinizar.RegistrationForm.Listener.RegisterButtonListener;
import com.github.mfazrinizar.RegistrationForm.Listener.UpdateButtonListener;
import com.github.mfazrinizar.RegistrationForm.Util.PhoneNumberTextFieldFilter;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class RegistrationForm extends JFrame {

    public RegistrationForm() {
        super("Borang Pendaftaran Member");
        // warmUpHttpRequestAsync("https://google.com");

        JLabel titleLabel = new JLabel("Form Registrasi", JLabel.CENTER);
        titleLabel.setForeground(Color.BLUE);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 24));

        JLabel nameLabel = new JLabel("Nama*:");
        JLabel emailLabel = new JLabel("Email*:");
        JLabel passwordLabel = new JLabel("Password*:");
        JLabel confirmPasswordLabel = new JLabel("Konfirmasi Password*:");
        JLabel countryLabel = new JLabel("Negara:");
        JLabel provinceLabel = new JLabel("Provinsi:");
        JLabel phoneNumberLabel = new JLabel("Nomor Telepon:");
        JLabel requiredLabel = new JLabel("* Wajib Diisi");

        JTextField nameTextField = new JTextField(20);
        JTextField emailTextField = new JTextField(20);
        JTextField countryTextField = new JTextField(20);

        List<String> countries = Arrays.asList("United States", "Canada", "Afghanistan", "Albania", "Algeria",
                "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and/or Barbuda", "Argentina",
                "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh",
                "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia",
                "Bosnia and Herzegovina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory",
                "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Cape Verde",
                "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island",
                "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Cook Islands", "Costa Rica",
                "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica",
                "Dominican Republic", "East Timor", "Ecudaor", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea",
                "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France",
                "France, Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon",
                "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe",
                "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands",
                "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)",
                "Iraq", "Ireland", "Israel", "Italy", "Ivory Coast", "Jamaica", "Japan", "Jordan", "Kazakhstan",
                "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kosovo", "Kuwait",
                "Kyrgyzstan", "Lao People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia",
                "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia",
                "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique",
                "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of",
                "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar",
                "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand",
                "Nicaragua", "Niger", "Nigeria", "Niue", "Norfork Island", "Northern Mariana Islands", "Norway", "Oman",
                "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn",
                "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda",
                "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino",
                "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore",
                "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa",
                "South Georgia South Sandwich Islands", "South Sudan", "Spain", "Sri Lanka", "St. Helena",
                "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbarn and Jan Mayen Islands", "Swaziland", "Sweden",
                "Switzerland", "Syrian Arab Republic", "Taiwan", "Tajikistan", "Tanzania, United Republic of",
                "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan",
                "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom",
                "United States minor outlying islands", "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City State",
                "Venezuela", "Vietnam", "Virigan Islands (British)", "Virgin Islands (U.S.)",
                "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zaire", "Zambia", "Zimbabwe");
        AutoCompleteComboBox countryComboBox = new AutoCompleteComboBox(countries);

        JTextField provinceTextField = new JTextField(20);
        JTextField phoneNumberTextField = new JTextField(20);
        ((AbstractDocument) phoneNumberTextField.getDocument()).setDocumentFilter(new PhoneNumberTextFieldFilter());

        JPasswordField passwordTextField = new JPasswordField(20);
        JPasswordField confirmPasswordTextField = new JPasswordField(20);

        JLabel nameErrorLabel = new JLabel("");
        nameErrorLabel.setForeground(Color.RED);
        JLabel emailErrorLabel = new JLabel("");
        emailErrorLabel.setForeground(Color.RED);
        JLabel passwordErrorLabel = new JLabel("");
        passwordErrorLabel.setForeground(Color.RED);
        JLabel confirmPasswordErrorLabel = new JLabel("");
        confirmPasswordErrorLabel.setForeground(Color.RED);
        JLabel provinceErrorLabel = new JLabel("");
        provinceErrorLabel.setForeground(Color.RED);
        JLabel countryErrorLabel = new JLabel("");
        countryErrorLabel.setForeground(Color.RED);
        JLabel phoneNumberErrorLabel = new JLabel("");
        phoneNumberErrorLabel.setForeground(Color.RED);

        JButton registerButton = new JButton("Daftar");
        JButton getDataButton = new JButton("Ambil Data");
        JButton deleteButton = new JButton("Hapus");
        JButton updateButton = new JButton("Ubah/Perbarui");

        registerButton.addActionListener(
                new RegisterButtonListener(nameTextField, emailTextField, passwordTextField,
                        confirmPasswordTextField,
                        countryComboBox, provinceTextField, phoneNumberTextField,
                        nameErrorLabel, emailErrorLabel,
                        passwordErrorLabel, confirmPasswordErrorLabel, countryErrorLabel,
                        provinceErrorLabel,
                        phoneNumberErrorLabel));

        getDataButton.addActionListener(
                new GetDataButtonListener(nameTextField, emailTextField, passwordTextField,
                        confirmPasswordTextField, countryComboBox, provinceTextField,
                        phoneNumberTextField, nameErrorLabel, emailErrorLabel,
                        passwordErrorLabel, confirmPasswordErrorLabel, countryErrorLabel,
                        provinceErrorLabel,
                        phoneNumberErrorLabel));

        deleteButton.addActionListener(
                new DeleteButtonListener(nameTextField, emailTextField, passwordTextField,
                        confirmPasswordTextField, countryTextField, provinceTextField,
                        phoneNumberTextField, emailErrorLabel, countryErrorLabel,
                        provinceErrorLabel,
                        phoneNumberErrorLabel, confirmPasswordErrorLabel, passwordErrorLabel,
                        countryComboBox));

        updateButton.addActionListener(
                new UpdateButtonListener(nameTextField, emailTextField, passwordTextField,
                        confirmPasswordTextField, countryComboBox, provinceTextField,
                        phoneNumberTextField, nameErrorLabel, emailErrorLabel,
                        passwordErrorLabel, confirmPasswordErrorLabel, countryErrorLabel,
                        provinceErrorLabel, phoneNumberErrorLabel, countries));

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 0, 10)); // 8 baris, 2 kolom, 10 pixels gap secara vertical

        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.add(nameTextField, BorderLayout.CENTER);
        namePanel.add(nameErrorLabel, BorderLayout.PAGE_END);
        formPanel.add(nameLabel);
        formPanel.add(namePanel);

        JPanel emailPanel = new JPanel(new BorderLayout());
        emailPanel.add(emailTextField, BorderLayout.CENTER);
        emailPanel.add(emailErrorLabel, BorderLayout.PAGE_END);
        formPanel.add(emailLabel);
        formPanel.add(emailPanel);

        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.add(passwordTextField, BorderLayout.CENTER);
        passwordPanel.add(passwordErrorLabel, BorderLayout.PAGE_END);
        formPanel.add(passwordLabel);
        formPanel.add(passwordPanel);

        JPanel confirmPasswordPanel = new JPanel(new BorderLayout());
        confirmPasswordPanel.add(confirmPasswordTextField, BorderLayout.CENTER);
        confirmPasswordPanel.add(confirmPasswordErrorLabel, BorderLayout.PAGE_END);
        formPanel.add(confirmPasswordLabel);
        formPanel.add(confirmPasswordPanel);

        JPanel countryPanel = new JPanel(new BorderLayout());
        countryPanel.add(countryComboBox, BorderLayout.CENTER);
        countryPanel.add(countryErrorLabel, BorderLayout.PAGE_END);
        formPanel.add(countryLabel);
        formPanel.add(countryPanel);

        JPanel provincePanel = new JPanel(new BorderLayout());
        provincePanel.add(provinceTextField, BorderLayout.CENTER);
        provincePanel.add(provinceErrorLabel, BorderLayout.PAGE_END);
        formPanel.add(provinceLabel);
        formPanel.add(provincePanel);

        JPanel phoneNumberPanel = new JPanel(new BorderLayout());
        phoneNumberPanel.add(phoneNumberTextField, BorderLayout.CENTER);
        phoneNumberPanel.add(phoneNumberErrorLabel, BorderLayout.PAGE_END);
        formPanel.add(phoneNumberLabel);
        formPanel.add(phoneNumberPanel);

        formPanel.add(requiredLabel);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 4, 10, 0)); // 1 baris, 3 kolom, 10 pixels gap secara
        // horizontal
        bottomPanel.add(registerButton);
        bottomPanel.add(getDataButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(updateButton);
        panel.add(bottomPanel, BorderLayout.PAGE_END);

        this.add(panel);

        this.setSize(400, 500);
        this.setLocationRelativeTo(null);

        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
