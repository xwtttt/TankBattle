package com.jsys;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.List;

public class HuDun {
	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;
	public int x, y;
	private boolean live = true;
	private boolean changeToMental = false;

	public boolean isChangeToMental() {
		return changeToMental;
	}

	public void setChangeToMental(boolean changeToMental) {
		this.changeToMental = changeToMental;
	}

	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image image = null;
	private Client c;
	static {
		image = tk.getImage(HuDun.class.getResource("../../Images/hudun.jpg"));
	}

	public void draw(Graphics g) {
		if (live) {
			g.drawImage(image, x, y, WIDTH, HEIGHT, null);
		}
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public HuDun(int x, int y, Client c) {
		this.x = x;
		this.y = y;
		this.c = c;
	}

	public void colliedWithHuDun(Tank tk, List<CommonWall> cm) {
		if (this.live && this.getRect().intersects(tk.getRect()) && this.changeToMental == false) {
			System.out.println("111");
			this.live = false;
			this.changeToMental = true;
			for (int i = 0; i < 4; i++) {
				MetalWall mw1 = new MetalWall(755, 795 + i * 30);
				MetalWall mw2 = new MetalWall(865, 795 + i * 30);
				MetalWall mw3 = new MetalWall(770 + i * 30, 795);
				this.c.metalWalls.add(mw1);
				this.c.metalWalls.add(mw2);
				this.c.metalWalls.add(mw3);
			}
		}
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
}
