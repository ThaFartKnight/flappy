package com.tfk.game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;

import com.tfk.main.NeuralNetwork;
import com.tfk.main.iFunctions;

public class Bird extends GameObject {
	private Image img = null;
	public final static int width = 50, height = 40; //When using a new image adjust these, these are specific constants for the original image
	private int velY = 0;
	private int score = 0;
	private boolean alive = false;
	private NeuralNetwork brain;
	private Game game;
	public Bird(Game game) {
		super(Math.round(Window.width / 4), Math.round(Window.height / 2));
		this.game = game;
		try {
			img = ImageIO.read(getClass().getResource("/flappy.png"));
		}catch (IOException e){
			e.printStackTrace();
			game.stop();
		}
		brain = new NeuralNetwork(4, 8, 1);
		alive = true;
	}
	
	public Bird(Game game, NeuralNetwork brain) {
		super(Math.round(Window.width / 4), Math.round(Window.height / 2));
		this.game = game;
		try {
			img = ImageIO.read(getClass().getResource("/flappy.png"));
		}catch (IOException e){
			e.printStackTrace();
			game.stop();
		}
		this.brain = brain.copy();
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
			if(y < -40) {
				alive = false;
			}
			if(alive) {
				score++;
			}
		}
	}
	
	public void think(LinkedList<Pipe> pipes) {
		float[] inputs = new float[4];
		Pipe closestPipe = new Pipe(0,0,0, game);
		float tmp = Window.width;
		
		for(int i = 0; i < pipes.size(); i++) {
			float diff = pipes.get(i).x - this.x;
			if(diff > 0 && diff < tmp) {
				tmp = diff;
				closestPipe = pipes.get(i);
			}
		}
		
		inputs[0] = this.y / (float) (Window.height);
		inputs[1] = velY;
		inputs[2] = closestPipe.x / (float) (Window.width);
		inputs[3] = (float) closestPipe.getBounds().getHeight();
		
		float[] output = brain.predict(inputs);
		if(output[0] >= 0.5) {
			jump();
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
	public int getScore() {
		return score;
	}
	public boolean isAlive() {
		return alive;
	}
	
	public Bird copy() {
		return new Bird(game, this.brain.copy());
	}
	public void mutate() {
		brain.mutate((iFunctions) ((val, x, y) -> {
			if((float) (Math.random()) < 0.1f) {
				float offset = (float) (new Random().nextGaussian() * 0.5f);
				float newx = val + offset;
				return newx;
			} else {
				return val;
			}
		}));
	}
}
