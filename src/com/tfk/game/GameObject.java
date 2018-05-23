package com.tfk.game;

import java.awt.Graphics;

public abstract class GameObject {
	protected int x, y;
	public abstract void tick();
	public abstract void render(Graphics g);
	
	public GameObject(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	// Getters and setters
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
}
