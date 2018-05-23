package com.tfk.game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JFrame;

public class Window extends Canvas{
	private static final long serialVersionUID = 6980614585263434684L; 
	public static int width = 500, height = 800;
	public final static Rectangle bottomBounds = new Rectangle(0, height, width, 1);
	public Window(Game game) {
		JFrame frame = new JFrame("Flappy");
		frame.setSize(new Dimension(width, height + 29)); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.add(game);
	}
}
