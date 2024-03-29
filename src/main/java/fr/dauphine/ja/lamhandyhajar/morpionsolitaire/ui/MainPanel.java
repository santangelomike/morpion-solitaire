package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.ui;

import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final MenuPanel menuPanel;

    public MainPanel() {

        super(new BorderLayout());

        menuPanel = new MenuPanel();
        menuPanel.button.addActionListener(this);

        this.add(menuPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JoinFive.Rule rule = (JoinFive.Rule) menuPanel.comboBox.getSelectedItem();
        boolean hint = menuPanel.checkBox.isSelected();

        remove(menuPanel);

        GamePanel gamePanel = new GamePanel(rule, hint);

        add(gamePanel);

        repaint();
        revalidate();
    }
}
