package com.tfk.game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bird extends GameObject {
	private Image img = null;
	public final static int width = 50, height = 40; //When using a new image adjust these, these are specific constants for the original image
	private int velY = 0;
	private int score = 0;
	private boolean alive = false;
	public Bird(int x, int y, Game game) {
		super(x, y);
		try {
			img = ImageIO.read(getClass().getResource("/flappy.png"));
		}catch (IOException e){
			e.printStackTrace();
			game.stop();
		}
		alive = true;
	}

	public void tick() {
		if(alive){
			super.y -= velY;
			if(velY > -30) {
				velY -= 3;
			}
			if(getBounds().intersects(Window.bottomBounds)) {
				alive = false;
			}
		}
	}
	
	public void jump() {
		velY = 23;
	}
	
	public void render(Graphics g) {
		if(img != null) {
			g.drawImage(img, super.x, super.y, width, height, null);
		}
	}
	
	public Ellipse2D.Double getBounds(){
		return new Ellipse2D.Double(super.x, super.y, width, height);
	}
	
	//TODO respawn function
	
	public void killBird() {
		alive = false;
	}
	public void increaseScore() {
		score++;
		System.out.println(score);
	}
	public int getScore() {
		return score;
	}
	public boolean isAlive() {
		return alive;
	}
}
