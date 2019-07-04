package com.jsys;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class Bullets {
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	private int x, y;
	private Direction direction;
	private int speedX = 20;
	private int speedY = 20;
	private boolean good;
	private boolean live;
	private static Image[] images = null;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private Client c;
	static {
		images = new Image[] { tk.getImage(Bullets.class.getResource("../../Images/bulletL.gif")),
				tk.getImage(Bullets.class.getResource("../../Images/bulletU.gif")),
				tk.getImage(Bullets.class.getResource("../../Images/bulletR.gif")),
				tk.getImage(Bullets.class.getResource("../../Images/bulletD.gif")) };
	}

	public Bullets(int x, int y, Direction direction, boolean good, Client c) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.good = good;
		this.c = c;
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public void draw(Graphics g) {
		switch (direction) {
		case L:
			g.drawImage(images[0], x, y, WIDTH, HEIGHT, null);
			break;
		case U:
			g.drawImage(images[1], x, y, WIDTH, HEIGHT, null);
			break;
		case R:
			g.drawImage(images[2], x, y, WIDTH, HEIGHT, null);
			break;
		case D:
			g.drawImage(images[3], x, y, WIDTH, HEIGHT, null);
			break;
		default:
			break;
		}
		move();
	}

	private void move() {
		switch (direction) {
		case L:
			x -= speedX;
			break;
		case U:
			y -= speedY;
			break;
		case R:
			x += speedX;
			break;
		case D:
			y += speedY;
			break;
		default:
			break;
		}
	}
}
