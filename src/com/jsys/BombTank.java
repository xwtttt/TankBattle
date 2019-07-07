package com.jsys;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class BombTank {
	private int x,y;
	private boolean live = true;
	private Client c;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] images = null;
	private int step = 0;
	static{
		images = new Image[]{
				tk.getImage(BombTank.class.getResource("../../Images/0.gif")),
				tk.getImage(BombTank.class.getResource("../../Images/1.gif")),
				tk.getImage(BombTank.class.getResource("../../Images/2.gif")),
				tk.getImage(BombTank.class.getResource("../../Images/3.gif")),
				tk.getImage(BombTank.class.getResource("../../Images/4.gif")),
				tk.getImage(BombTank.class.getResource("../../Images/5.gif")),
				tk.getImage(BombTank.class.getResource("../../Images/6.gif")),
				tk.getImage(BombTank.class.getResource("../../Images/7.gif")),
				tk.getImage(BombTank.class.getResource("../../Images/8.gif")),
				tk.getImage(BombTank.class.getResource("../../Images/9.gif")),
				tk.getImage(BombTank.class.getResource("../../Images/10.gif"))};
		}
	
	public BombTank(int x, int y, Client c) {
		this.x = x;
		this.y = y;
		this.c = c;
	}
	public void draw(Graphics g){
		if(!live){
			this.c.bombTanks.remove(this);
			return;
		}
		if(step == 10){
			live = false;
			step = -1;
		}
		step++;
		g.drawImage(images[step], x, y, null);
	}
}
