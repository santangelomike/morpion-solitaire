package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.ui;

import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive;

import javax.swing.*;

public class MenuPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public JButton button;
    public JCheckBox checkBox;
    public JComboBox comboBox;

    public MenuPanel() {

        this.add(new JLabel("Welcome to the JoinFive game"));

        comboBox = new JComboBox(JoinFive.Rule.values());

        comboBox.setBounds(50, 50, 90, 20);

        this.add(comboBox);

        checkBox = new JCheckBox("Show grid hints");

        checkBox.setBounds(100, 100, 50, 50);

        this.add(checkBox);

        button = new JButton();

        button.setText("Start new game");

        this.add(button);
    }
}
