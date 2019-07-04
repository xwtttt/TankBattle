package com.jsys;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class River {
	public static final int WIDTH = 100;
	public static final int HEIGHT = 250;
	private int x,y;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image image = null;
	static{
		image = tk.getImage(River.class.getResource("../../Images/river.jpg"));
	}
	public void draw(Graphics g){
		g.drawImage(image, x, y, WIDTH, HEIGHT, null);
	}
	
	public Rectangle gerRect(){
		return new Rectangle(x, y, WIDTH, HEIGHT) ;
	}

	public River(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
