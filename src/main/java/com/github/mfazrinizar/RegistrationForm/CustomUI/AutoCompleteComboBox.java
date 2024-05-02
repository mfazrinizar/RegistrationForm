package com.github.mfazrinizar.RegistrationForm.CustomUI;

/*
 * Author       : M. Fazri Nizar
 * Institution  : Sriwijaya University
 * GitHub       : github.com/mfazrinizar
 * File Name    : AutoCompleteComboBox.java
 */

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
//import java.util.stream.Collectors;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class AutoCompleteComboBox extends JComboBox<String> {
    private final List<String> dictionary;

    public AutoCompleteComboBox(List<String> dictionary) {
        super(dictionary.toArray(new String[0]));
        this.dictionary = dictionary;
        this.setEditable(true);

        final JTextField textfield = (JTextField) this.getEditor().getEditorComponent();
        textfield.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                SwingUtilities.invokeLater(() -> {
                    if (ke.getKeyCode() == KeyEvent.VK_UP || ke.getKeyCode() == KeyEvent.VK_DOWN
                            || ke.getKeyCode() == KeyEvent.VK_CONTROL || ke.getKeyCode() == KeyEvent.VK_A) {
                        return;
                    } // Agar dropdown list dapat dinavigasi dengan keyboard
                    filter(textfield.getText());
                    if (!textfield.getText().isEmpty()) {
                        showPopup(); // Menunjukkan dropdown list jika textfield tidak kosong
                    }
                });
            }
        });

        this.insertItemAt("", 0); // Tambahkan "" sebagai item pertama (agar value pertama kali user menjalankan
        // program kosong)
        this.setSelectedIndex(0);
    }

    public void filter(String enteredText) {
        List<String> filteredItems = dictionary.stream()
                .filter(item -> item.toLowerCase().startsWith(enteredText.toLowerCase()))
                .toList();

        if (filteredItems.isEmpty()) {
            String[] array = { "" };
            this.setModel(new DefaultComboBoxModel<>(array));
        } else {
            this.setModel(new DefaultComboBoxModel<>(filteredItems.toArray(new String[0])));
        }

        JTextField textfield = (JTextField) this.getEditor().getEditorComponent();
        textfield.setText(enteredText);
    }

    public List<String> getDictionary() {
        return this.dictionary;
    }
}

