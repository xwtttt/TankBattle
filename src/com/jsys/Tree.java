package com.jsys;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Tree {
	public static final int WIDTH = 40;
	public static final int HEIGTH = 40;
	private int x,y;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image image = null;
	static{
		image = tk.getImage(Tree.class.getResource("../../Images/tree.gif"));
	}
	public void draw(Graphics g){
		g.drawImage(image, x, y, WIDTH, HEIGTH, null);
	}
	public Tree(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
}
