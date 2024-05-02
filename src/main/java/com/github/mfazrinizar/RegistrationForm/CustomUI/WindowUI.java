package com.github.mfazrinizar.RegistrationForm.CustomUI;

/*
 * Author       : M. Fazri Nizar
 * Institution  : Sriwijaya University
 * GitHub       : github.com/mfazrinizar
 * File Name    : WindowUI.java
 */

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class WindowUI {
    public static void setRedWindowTheme() {
        UIManager.put("activeCaption", new javax.swing.plaf.ColorUIResource(Color.RED));
        UIManager.put("activeCaptionText", new javax.swing.plaf.ColorUIResource(Color.WHITE));
        JFrame.setDefaultLookAndFeelDecorated(true);
    }
}
