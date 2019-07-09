package com.jsys;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.List;

public class Bomb{
	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;
	public int x, y;
	private boolean live = true;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image image = null;
	private Client c;
	static {
		image = tk.getImage(Bomb.class.getResource("../../Images/bomb.jpg"));
	}

	public Bomb(int x, int y, Client c) {
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

	// Ì¹¿ËÅöµ½±¬Õ¨
	public void colliedWithBomb(Tank tk, List<Tank> tanks) {
		if (this.live && this.getRect().intersects(tk.getRect())) {
			if(tanks.size() >= 1){
				for (int j = 0; j < 2; j++) {
					int n = tanks.size() - 1;
					tanks.remove(n);
				}
			}else{
				tanks.remove(0);
			}
			this.live = false;

		}
	}

	
}
