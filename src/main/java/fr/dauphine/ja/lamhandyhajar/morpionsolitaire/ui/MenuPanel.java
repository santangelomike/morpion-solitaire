package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.ui;

import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive;

import javax.swing.*;
import java.util.ArrayList;

public class MenuPanel extends JPanel {

    public JButton button;
    public ArrayList<JRadioButton> radioButtons;

    public MenuPanel() {

        radioButtons = new ArrayList<>();

        for (JoinFive.Rule rule : JoinFive.Rule.values()) {
            radioButtons.add(new JRadioButton(rule.name()));
        }

        for (JRadioButton radioButton : radioButtons) {
            this.add(radioButton);
        }

        button = new JButton();
        button.setText("Nouvelle partie");

        this.add(button);
    }
}
