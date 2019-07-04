package com.jsys;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;


public class CommonWall {
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	private int x, y;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image image = null;
	static {
		image = tk.getImage(CommonWall.class.getResource("../../Images/commonWall.gif"));
	}

	public void draw(Graphics g) {
		g.drawImage(image, x, y, WIDTH, HEIGHT, null);
	}

	public CommonWall(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

}
