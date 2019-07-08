package com.jsys;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.List;

public class Blood {
	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;
	public int x, y;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image image = null;
	private boolean live = true;
	private Client c;
	static {
		image = tk.getImage(Blood.class.getResource("../../Images/blood.png"));
	}
	public void draw(Graphics g) {
		if (live) {
			g.drawImage(image, x, y, WIDTH, HEIGHT, null);
		}
	}
	public Blood(int x, int y, Client c) {
		this.x = x;
		this.y = y;
		this.c = c;
	}
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	public void colliedWithBlood(Tank tk) {
		if (this.live && this.getRect().intersects(tk.getRect())) {
			this.c.homeTank.setLife(200);
			this.live = false;
		}
	}
}
