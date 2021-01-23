package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.ui;

import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel extends JPanel implements ActionListener {

    private final MenuPanel menuPanel;

    public MainPanel() {

        super(new BorderLayout());

        menuPanel = new MenuPanel();
        menuPanel.button.addActionListener(this);

        this.add(menuPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JoinFive.Rule rule = null;

        for (JRadioButton radioButton : menuPanel.radioButtons) {
            if (radioButton.isSelected()) rule = JoinFive.Rule.valueOf(radioButton.getText());
        }

        remove(menuPanel);

        GamePanel gamePanel = new GamePanel(rule);
        
        add(gamePanel);

        repaint();
        revalidate();
    }
}
