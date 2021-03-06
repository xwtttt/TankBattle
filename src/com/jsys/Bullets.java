package com.jsys;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.List;

public class Bullets {
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	private int x, y;
	private Direction direction;
	private int speedX = 20;
	private int speedY = 20;
	private boolean good;
	// private boolean live;
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
			g.drawImage(images[1], x, y, 2 * WIDTH, HEIGHT, null);
			break;
		case R:
			g.drawImage(images[2], x, y, WIDTH, HEIGHT, null);
			break;
		case D:
			g.drawImage(images[3], x, y, 2 * WIDTH, HEIGHT, null);
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
		if (x <= 0 || x > Client.FRAME_WIDTH) {
			this.c.bullets.remove(this);
		}
		if (y <= 0 || y > Client.FRAME_HEIGHT) {
			this.c.bullets.remove(this);
		}
	}

	// 子弹碰到普通墙
	public void colliedWithCommonWall(CommonWall cw) {
		if (this.getRect().intersects(cw.getRect())) {
			this.c.bullets.remove(this);
			this.c.homeWall.remove(cw);
			this.c.otherWall.remove(cw);
		}
	}

	public boolean isGood() {
		return good;
	}

	public void setGood(boolean good) {
		this.good = good;
	}

	// 子弹碰到铁墙
	public void colliedWithMetalWall(MetalWall mw, List<Bullets> bullets) {
		if (this.c.star.isLive() == false && this.good) {
			return;
		} else if (this.getRect().intersects(mw.getRect())) {
			this.c.bullets.remove(this);
		}
	}

	// 子弹碰到子弹
	public void colliedWithBullets(List<Bullets> bullets) {
		for (int i = 0; i < bullets.size(); i++) {
			Bullets b = bullets.get(i);
			if ((this.good != b.good) && (this != b) && (this.getRect().intersects(b.getRect()))) {
				this.c.bullets.remove(this);
				bullets.remove(b);
			}
		}
	}

	// 子弹碰到敌方
	public void colliedWithTank(Tank tk) {
		if ((tk.isLive()) && (this.good != tk.isGood()) && (this.getRect().intersects(tk.getRect()))) {
			BombTank bt = new BombTank(tk.getX(), tk.getY(), this.c);
			this.c.bombTanks.add(bt);
			if (!tk.isGood()) {
				// 我方坦克击中敌方坦克
				this.c.bullets.remove(this);
				this.c.enemyTank.remove(tk);
			} else {
				this.c.bullets.remove(this);
				tk.setLife(tk.getLife() - 50);
				if (tk.getLife() <= 0) {
					tk.setLive(false);
				}
			}
		}
	}

	public void colliedWithHome(Home home) {
		if ((home.isLive()) && this.getRect().intersects(home.getRect())) {
			// 游戏结束
			BombTank bt = new BombTank(home.getX(), home.getY(), this.c);
			this.c.bombTanks.add(bt);
			home.setLive(false);
			this.c.homeTank.setLive(false);
			this.c.bullets.remove(this);
		}
	}

}
