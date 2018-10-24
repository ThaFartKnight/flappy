package com.tfk.game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Input extends KeyAdapter{
	private Game game;
	//Temp class for manual playing
	public Input(Game game) {
		this.game = game;
	}
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			//game.getBird().jump();
		}
	}
}
