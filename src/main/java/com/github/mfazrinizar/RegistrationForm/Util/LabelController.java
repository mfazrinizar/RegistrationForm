package com.github.mfazrinizar.RegistrationForm.Util;

/*
 * Author       : M. Fazri Nizar
 * Institution  : Sriwijaya University
 * GitHub       : github.com/mfazrinizar
 * File Name    : LabelController.java
 */

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LabelController {
    public static void clearInvalidLabel(JLabel... labels) {
        for (JLabel label : labels) {
            label.setText("");
        }
    }

    public static void clearTextField(JTextField... textFields) {
        for (JTextField textField : textFields) {
            textField.setText("");
        }
    }

    public static void clearPasswordTextField(JPasswordField... passwordFields) {
        for (JPasswordField passwordField : passwordFields) {
            passwordField.setText("");
        }
    }
}

