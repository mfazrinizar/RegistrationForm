package com.github.mfazrinizar.RegistrationForm;

/*
 * Author       : M. Fazri Nizar
 * Institution  : Sriwijaya University
 * GitHub       : github.com/mfazrinizar
 * File Name    : Main.java
 */
import com.github.mfazrinizar.RegistrationForm.CustomUI.WindowUI;
import com.github.mfazrinizar.RegistrationForm.CustomUI.RegistrationForm;

public class Main {
    public static void main(String[] args) {
        WindowUI.setRedWindowTheme();
        new RegistrationForm();
    }
}