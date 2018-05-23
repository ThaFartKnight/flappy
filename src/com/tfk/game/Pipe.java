package com.tfk.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Pipe extends GameObject {
	private Game game;
	private final int width = 40, height;
	private boolean active = true; //Active to grant points
	public Pipe(int x, int y, int height, Game game) {
		super(x, y);
		this.game = game;
		this.height = height;
	}

	public void tick() {
		// TODO move pipe
		super.x -= Game.velX;
		Rectangle bounds = new Rectangle(super.x, super.y, width, height);
		if(game.getBird().getBounds().intersects(bounds)){
			game.getBird().killBird();
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.green);
		g.fillRect(super.x, super.y, width, height);
		
	}
	public Rectangle getBounds() {
		return new Rectangle(super.x, super.y, width, height);
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
