package com.jsys;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class Home {
	public static final int WIDTH = 70;
	public static final int HEIGHT = 70;
	private int x,y;
	private boolean live = true;
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image image = null;
	static{
		image = tk.getImage(Home.class.getResource("../../Images/home.jpg"));
	}
	public Home(int x,int y){
		this.x = x;
		this.y = y;
	}
	public void draw(Graphics g){
		if(!live){
			return;
		}
		g.drawImage(image, x, y, WIDTH, HEIGHT, null);
	}
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	} 
}
