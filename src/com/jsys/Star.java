package com.jsys;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class Star {
	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;
	public int x, y;
	private boolean live = true;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image image = null;
	private Client c;
	static {
		image = tk.getImage(Bomb.class.getResource("../../Images/star.jpg"));
	}
	
	public Star(int x, int y, Client c) {
		this.x = x;
		this.y = y;
		this.c = c;
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public void draw(Graphics g) {
		if (live) {
			g.drawImage(image, x, y, WIDTH, HEIGHT, null);
		}
	}
	public void colliedWithStar(Tank tk) {
		if (this.live && this.getRect().intersects(tk.getRect())) {
			this.live = false;
			
		}
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
}
