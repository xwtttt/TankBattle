package com.jsys;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

public class Tank {
	public static final int WIDTH = 50;
	public static final int LENGTH = 50;
	private int x, y;
	private Direction direction;
	private Direction oldDirection = Direction.U;
	private boolean bL, bU, bR, bD = false;
	private boolean good;
	private int speedX = 10;
	private int speedY = 10;
	private int life = 200;
	private int oldX, oldY;
	private int n = (int) (Math.random() * 10) + 5;
	private boolean live = true;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] images = null;
	private Client c;

	static {
		images = new Image[] { tk.getImage(Tank.class.getResource("../../Images/tankL.gif")),
				tk.getImage(Tank.class.getResource("../../Images/tankU.gif")),
				tk.getImage(Tank.class.getResource("../../Images/tankR.gif")),
				tk.getImage(Tank.class.getResource("../../Images/tankD.gif")) };
	}

	public Tank(int x, int y, Direction direction, boolean good, Client c) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.good = good;
		this.c = c;
	}

	public void draw(Graphics g) {
		// 定义敌方坦克方向
		if (!good) {
			if (n == 0) {
				Direction[] directions = Direction.values();
				int rn = (int) (Math.random() * 4);
				direction = directions[rn];
				n = (int) (Math.random() * 10) + 5;
			}
			n--;
		}
		switch (direction) {
		case L:
			g.drawImage(images[0], x, y, WIDTH, LENGTH, null);
			break;
		case U:
			g.drawImage(images[1], x, y, WIDTH, LENGTH, null);
			break;
		case R:
			g.drawImage(images[2], x, y, WIDTH, LENGTH, null);
			break;
		case D:
			g.drawImage(images[3], x, y, WIDTH, LENGTH, null);
			break;
		case STOP:
			if (oldDirection == Direction.L) {
				g.drawImage(images[0], x, y, WIDTH, LENGTH, null);
			} else if (oldDirection == Direction.U) {
				g.drawImage(images[1], x, y, WIDTH, LENGTH, null);
			} else if (oldDirection == Direction.R) {
				g.drawImage(images[2], x, y, WIDTH, LENGTH, null);
			} else if (oldDirection == Direction.D) {
				g.drawImage(images[3], x, y, WIDTH, LENGTH, null);
			}
			break;
		}
		move();
	}

	private void move() {
		oldX = x;
		oldY = y;
		switch (direction) {
		case L:
			x -= speedX;
			if (x < 0) {
				x = 0;
			}
			break;
		case U:
			y -= speedY;
			if (y < 40) {
				y = 40;
			}
			break;
		case R:
			x += speedX;
			if (x >= Client.FRAME_WIDTH - WIDTH) {
				x = Client.FRAME_WIDTH - WIDTH;
			}
			break;
		case D:
			y += speedY;
			if (y >= Client.FRAME_HEIGHT - LENGTH) {
				y = Client.FRAME_HEIGHT - LENGTH;
			}
		case STOP:
			break;
		}
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_A:
			bL = true;
			break;
		case KeyEvent.VK_W:
			bU = true;
			break;
		case KeyEvent.VK_D:
			bR = true;
			break;
		case KeyEvent.VK_S:
			bD = true;
			break;
		case KeyEvent.VK_J:
			fire();
			break;
		}
		decideDirection();
	}

	private void decideDirection() {
		// TODO Auto-generated method stub
		if (bL && !bU && !bR && !bD) {
			direction = Direction.L;
			oldDirection = direction;
		} else if (bU && !bR && !bD && !bL) {
			direction = Direction.U;
			oldDirection = direction;
		} else if (bR && !bD && !bL && !bU) {
			direction = Direction.R;
			oldDirection = direction;
		} else if (bD && !bL && !bU && !bR) {
			direction = Direction.D;
			oldDirection = direction;
		} else if (!bD && !bL && !bU && !bR) {
			direction = Direction.STOP;
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_A:
			bL = false;
			break;
		case KeyEvent.VK_W:
			bU = false;
			break;
		case KeyEvent.VK_D:
			bR = false;
			break;
		case KeyEvent.VK_S:
			bD = false;
			break;
		
		}
		decideDirection();
	}

	private void fire() {
		// TODO Auto-generated method stub
		int x = this.x + WIDTH / 2 - Bullets.WIDTH / 2;
		int y = this.y + LENGTH / 2 - Bullets.HEIGHT / 2;
		Direction dir = this.direction;
		if(this.direction == Direction.STOP){
			dir = oldDirection;
		}
		boolean good = this.good;
		this.c.bullets.add(new Bullets(x, y, dir, good, this.c));
	}

	// 坦克和河流碰撞
	public void colliedWithRiver(River river) {
		if (this.getRect().intersects(river.gerRect())) {
			changeToOldDirection();
		}
	}

	// 坦克和铁墙碰撞
	public void colliedWithMetalWall(MetalWall mw) {
		if (this.getRect().intersects(mw.getRect())) {
			changeToOldDirection();
		}
	}

	// 坦克和普通墙碰撞
	public void colliedWithCommonWall(CommonWall cw) {
		if (this.getRect().intersects(cw.getRect())) {
			changeToOldDirection();
		}
	}

	// 坦克和家碰撞
	public void colliedWithHome(Home ho) {
		if (this.getRect().intersects(ho.getRect())) {
			changeToOldDirection();
		}
	}

	// 坦克之间的碰撞
	public void colliedWithTank(Tank tank) {
		if (this.getRect().intersects(tank.getRect())) {
			changeToOldDirection();
		}
	}

	private void changeToOldDirection() {
		x = oldX;
		y = oldY;
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, LENGTH);
	}
}
