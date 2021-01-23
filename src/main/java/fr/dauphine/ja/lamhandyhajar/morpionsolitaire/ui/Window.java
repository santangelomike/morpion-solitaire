package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.ui;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
	
	private static final long serialVersionUID = 1L;

	public Window() throws HeadlessException {
        this.add(new MainPanel(), BorderLayout.CENTER);
        this.setSize(1000, 750);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setTitle("Morpion solitaire");
    }
}
