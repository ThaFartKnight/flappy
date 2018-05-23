package com.tfk.game; 

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;
import java.util.Random;

public class Game extends Canvas implements Runnable{
	private static final long serialVersionUID = 5385576283692280705L;
	private Thread thread;
	private boolean running = false;
	private LinkedList<Bird> birds = new LinkedList<Bird>();
	private LinkedList<Pipe> topPipes = new LinkedList<>(); //max 2 pipes
	private LinkedList<Pipe> bottomPipes = new LinkedList<>();//max 2 pipes
	
	//Game options
	public static final int velX = 5;
	public static final int spacing = 250;
	
	public Game() {
		new Window(this);
		birds.add(new Bird((Math.round(Window.width / 4)), (Window.height - Bird.height) / 2, this));
		newPipes();
		running = true;
		start();
	}
	
	public synchronized void start() {
		thread = new Thread();
		addKeyListener(new Input(this));
		run();
	}
	
	public synchronized void stop() {
		try {
			thread.join(20);
			running = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 30.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				tick();
				delta--;
			}
			if(running) {
				render();
				if(System.currentTimeMillis() - timer > 1000) {
					timer += 1000;
				}
			}
		}
	}
	
	private void newPipes() {
		topPipes.clear();
		bottomPipes.clear();
		Random r = new Random();
		
		//First row
		int top = r.nextInt(Window.height - spacing - 40) + 20;
		int bottom = top + spacing;
		topPipes.add(new Pipe(Window.width, 0, top, this));
		bottomPipes.add(new Pipe(Window.width, bottom, Window.height - bottom, this));
		//Second row
		top = r.nextInt(Window.height - spacing - 40) + 20;
		bottom = top + spacing;
		topPipes.add(new Pipe((int)Math.round(Window.width * 1.75), 0, top, this));
		bottomPipes.add(new Pipe((int)Math.round(Window.width * 1.75), bottom, Window.height - bottom, this));	
	}
	
	private void tick() {
		boolean allDead = true;
		for(Bird b : birds) {
			if(b.isAlive()) {
				allDead = false;
				break;
			}
		}
		if(allDead) {
			newPipes();
			birds.clear();
			birds.add(new Bird((Math.round(Window.width / 4)), (Window.height - Bird.height) / 2, this));
		}
		for(int i = 0; i < topPipes.size(); i++) {
			topPipes.get(i).tick();
			bottomPipes.get(i).tick();
		}
		
		if(topPipes.getFirst().x <= -40) {
			topPipes.set(0, topPipes.get(1));
			bottomPipes.set(0, bottomPipes.get(1));
			
			Random r = new Random();
			int top = r.nextInt(Window.height - spacing - 40) + 20;
			int bottom = top + spacing;
			topPipes.set(1, new Pipe((int)Math.round(Window.width * 0.75) + topPipes.getFirst().x, 0, top, this));
			bottomPipes.set(1, new Pipe((int)Math.round(Window.width * 0.75) + topPipes.getFirst().x, bottom, Window.height - bottom, this));
		}
		for(int i = 0; i < birds.size(); i++) {
			Bird b = birds.get(i);
			if(topPipes.getFirst().isActive()) {
				//first pipe
				Pipe p = topPipes.getFirst();
				if(b.isAlive() && p.x < b.x) {
					b.increaseScore();
					if(i +1 == birds.size()){
						topPipes.getFirst().setActive(false);
					}
				}
			}else {
				//second pipe
				Pipe p = topPipes.getLast();
				if(b.isAlive() && p.x < b.x && p.isActive()) {
					b.increaseScore();
					if(i +1 == birds.size()){
						topPipes.getLast().setActive(false);
					}
				}
			}
			
		}
		birds.forEach( b -> {
			b.tick();
		});
	}
	
	//TODO draw score -- not necessary for ML
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			requestFocus();
			return;
		}
		Graphics g = bs.getDrawGraphics();
		//Draw background and stuff
		//Static drawings
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, Window.width, Window.height);
		
		//Draw objects
		birds.forEach( b -> {
			if(b.isAlive()) {
				b.render(g);	
			}
			
		});
		//In a forloop so that the pipes move row by row at a time
		for(int i = 0; i < topPipes.size(); i++) {
			topPipes.get(i).render(g);
			bottomPipes.get(i).render(g);
		}
		//show and dispose of the render
		g.dispose();
		bs.show();
	}
	
	//Temp func for manual control of bird 
	//Not nessesary when machine learning in play
	public Bird getBird() {
		return birds.getFirst();
	}
	public LinkedList<Bird> getBirds(){
		return birds;
	}
}
