package com.jsys;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class Client extends Frame implements ActionListener {

	/**
	 * 坦克大战主类
	 */
	private static final long serialVersionUID = 1L;
	// 定义框的宽和高
	public static final int FRAME_WIDTH = 1600;
	public static final int FRAME_HEIGHT = 900;
	// 获取屏幕的尺寸
	Toolkit tk = Toolkit.getDefaultToolkit();
	Dimension screenSize = tk.getScreenSize();
	int screenWidth = screenSize.width;
	int screenHeight = screenSize.height;
	// 定义游戏菜单 吧台
	MenuBar jmb = null;
	// 菜单
	Menu jm1, jm2, jm3, jm4 = null;
	// 子菜单
	MenuItem jmi1, jmi2, jmi3, jmi4, jmi5, jmi6, jmi7, jmi8, jmi9 = null;

	public boolean printable = true;
	// 定义画布
	Image screenImage = null;
	// 定义树
	River river = null;
	// 定义tree集合
	List<Tree> trees = new ArrayList<Tree>();
	// 定义MetalWall集合
	List<MetalWall> metalWalls = new ArrayList<MetalWall>();
	// 定义CommonWall集合
	List<CommonWall> otherWall = new ArrayList<CommonWall>();
	// 定义Home类
	Home home = null;
	// 定义家的墙
	List<CommonWall> homeWall = new ArrayList<CommonWall>();
	// 定义我方坦克
	Tank homeTank = null;
	// 定义敌方坦克
	List<Tank> enemyTank = new ArrayList<Tank>();
	// 定义子弹集合
	List<Bullets> bullets = new ArrayList<Bullets>();

	// main函数
	public static void main(String args[]) {
		new Client();
	}

	public void update(Graphics g) {
		// 拿到画布
		screenImage = this.createImage(FRAME_WIDTH, FRAME_HEIGHT);
		// 获得画布的画笔并封装绘制流程
		Graphics gps = screenImage.getGraphics();
		framePaint(gps);
		// 每次将画布覆盖Frame框，达到刷新效果
		g.drawImage(screenImage, 0, 0, null);
	}

	// 绘制地图信息
	private void framePaint(Graphics g) {
		// 绘制河
		river.draw(g);

		// 绘制金属墙
		for (int i = 0; i < metalWalls.size(); i++) {
			metalWalls.get(i).draw(g);
			homeTank.colliedWithMetalWall(metalWalls.get(i));
			for (int j = 0; j < enemyTank.size(); j++) {
				enemyTank.get(j).colliedWithMetalWall(metalWalls.get(i));
			}
		}

		// 绘制其他普通墙
		for (int i = 0; i < otherWall.size(); i++) {
			otherWall.get(i).draw(g);
			homeTank.colliedWithCommonWall(otherWall.get(i));
			for (int j = 0; j < enemyTank.size(); j++) {
				enemyTank.get(j).colliedWithCommonWall(otherWall.get(i));
			}
		}
		// 绘制家
		home.draw(g);

		// 绘制家的墙
		for (int i = 0; i < homeWall.size(); i++) {
			homeWall.get(i).draw(g);
			homeTank.colliedWithCommonWall(homeWall.get(i));
			for (int j = 0; j < enemyTank.size(); j++) {
				enemyTank.get(j).colliedWithCommonWall(homeWall.get(i));
			}
		}

		// 绘制我方坦克
		homeTank.draw(g);
		// 绘制我防坦克和河流的碰撞
		homeTank.colliedWithRiver(river);
		// 绘制我防坦克和家的碰撞
		homeTank.colliedWithHome(home);

		// 绘制树
		for (int i = 0; i < trees.size(); i++) {
			trees.get(i).draw(g);
		}
		// 绘制敌方坦克
		for (int i = 0; i < enemyTank.size(); i++) {
			enemyTank.get(i).draw(g);
		}
		// 绘制敌方坦克和河流的碰撞
		for (int i = 0; i < enemyTank.size(); i++) {
			enemyTank.get(i).colliedWithRiver(river);
		}

		// 绘制敌方坦克和我方坦克的碰撞
		for (int i = 0; i < enemyTank.size(); i++) {
			for (int j = 0; j < enemyTank.size(); j++) {
				if (i != j) {
					enemyTank.get(i).colliedWithTank(enemyTank.get(j));
					enemyTank.get(i).colliedWithTank(homeTank);
					homeTank.colliedWithTank(enemyTank.get(i));
				}
			}
		}
		// 绘制子弹
		for (int i = 0; i < bullets.size(); i++) {
			Bullets b = bullets.get(i);
			b.draw(g);
		}
	}

	// 开启线程，调用repaint()
	private class PaintThread implements Runnable {

		@Override
		public void run() {
			while (printable) {
				repaint();
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 按钮监听
	@Override
	public void actionPerformed(ActionEvent e) {

	}

	public Client() {
		// 设置窗口可见
		this.setVisible(true);
		// 设置窗口大小不可变
		this.setResizable(false);
		// 设置窗口大小不可变
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		// 设置窗口位置居中
		this.setLocation(screenWidth / 2 - FRAME_WIDTH / 2, screenHeight / 2 - FRAME_HEIGHT / 2);
		// 设置背景颜色
		this.setBackground(Color.gray);
		// 设置标题
		this.setTitle("坦克大战");
		// 设置窗口关闭模式
		this.addWindowListener(new WindowAdapter() {
			// 设置方法
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		// 初始化游戏菜单
		jmb = new MenuBar();
		jm1 = new Menu("游戏");
		jmi1 = new MenuItem("开始新游戏");
		jmi2 = new MenuItem("退出");
		// jmi1.setFont(new Font("TimesRoman",Font.BOLD,15));
		// jmi2.setFont(new Font("TimesRoman",Font.BOLD,15));
		jm2 = new Menu("暂停/继续");
		jmi3 = new MenuItem("暂停");
		jmi4 = new MenuItem("继续");

		jm3 = new Menu("游戏级别");
		jmi5 = new MenuItem("游戏级别1");
		jmi6 = new MenuItem("游戏级别2");
		jmi7 = new MenuItem("游戏级别3");
		jmi8 = new MenuItem("游戏级别4");

		jm4 = new Menu("游戏帮助");
		jmi9 = new MenuItem("游戏说明");
		// 绑定监听事件
		jmi1.addActionListener(this);
		jmi1.setActionCommand("Begin");
		jmi2.addActionListener(this);
		jmi2.setActionCommand("Exit");
		jmi3.addActionListener(this);
		jmi3.setActionCommand("Stop");
		jmi4.addActionListener(this);
		jmi4.setActionCommand("Continue");
		jmi5.addActionListener(this);
		jmi5.setActionCommand("Level1");
		jmi6.addActionListener(this);
		jmi6.setActionCommand("Level2");
		jmi7.addActionListener(this);
		jmi7.setActionCommand("Level3");
		jmi8.addActionListener(this);
		jmi8.setActionCommand("Level4");
		jmi9.addActionListener(this);
		jmi9.setActionCommand("Help");
		jmb.add(jm1);
		jm1.add(jmi1);
		jm1.add(jmi2);
		jmb.add(jm2);
		jm2.add(jmi3);
		jm2.add(jmi4);
		jmb.add(jm3);
		jm3.add(jmi5);
		jm3.add(jmi6);
		jm3.add(jmi7);
		jm3.add(jmi8);
		jmb.add(jm4);
		jm4.add(jmi9);
		this.setMenuBar(jmb);
		// 添加键盘监听事件
		this.addKeyListener(new KeyMonitor());
		// 启动线程,调用PaintThread()函数
		new Thread(new PaintThread()).start();
		// 初始化River
		river = new River(200, 100);
		// 初始化Tree
		for (int i = 0; i < 5; i++) {
			trees.add(new Tree(i * 40, 500));
			trees.add(new Tree(FRAME_WIDTH / 2 - (i + 3) * 40, 500));
			trees.add(new Tree(FRAME_WIDTH / 2 + (i + 3) * 40, 500));
			trees.add(new Tree(FRAME_WIDTH - (i + 1) * 40, 500));
		}
		// 初始化金属墙
		for (int i = 0; i < 10; i++) {
			metalWalls.add(new MetalWall(300 + i * (30), 200));
			metalWalls.add(new MetalWall(300 + i * (30), 230));
		}
		for (int i = 0; i < 10; i++) {
			metalWalls.add(new MetalWall(1200, FRAME_HEIGHT - i * (30)));
			metalWalls.add(new MetalWall(1230, FRAME_HEIGHT - i * (30)));
		}

		// 初始化其他普通墙
		for (int i = 0; i < 12; i++) {
			otherWall.add(new CommonWall(480, FRAME_HEIGHT - i * (30)));
			otherWall.add(new CommonWall(510, FRAME_HEIGHT - i * (30)));
			otherWall.add(new CommonWall(1100, FRAME_HEIGHT - i * (30)));
			otherWall.add(new CommonWall(1130, FRAME_HEIGHT - i * (30)));
			otherWall.add(new CommonWall(FRAME_WIDTH - (i + 1) * 30, 250));
			otherWall.add(new CommonWall(FRAME_WIDTH - (i + 1) * 30, 300));
		}
		for (int i = 0; i < 20; i++) {
			otherWall.add(new CommonWall(FRAME_WIDTH / 2 + (i - 9) * 30, 450));
			otherWall.add(new CommonWall(FRAME_WIDTH / 2 + (i - 9) * 30, 420));
		}
		// 初始化家的墙
		for (int i = 0; i < 4; i++) {
			homeWall.add(new CommonWall(755, 795 + i * 30));
			homeWall.add(new CommonWall(865, 795 + i * 30));
			homeWall.add(new CommonWall(770 + i * 30, 795));
		}
		// 初始化家
		home = new Home(790, 830);
		// 初始化我方坦克
		homeTank = new Tank(650, 830, Direction.STOP, true, this);
		// 初始化敌方20个坦克
		for (int i = 0; i < 20; i++) {
			if (i < 9) {
				enemyTank.add(new Tank(500 + i * 50, 50, Direction.D, false, this));
			} else if (i < 14) {
				enemyTank.add(new Tank(0, 50 + (i - 9) * 50, Direction.D, false, this));
			} else {
				enemyTank.add(new Tank(1550, (i - 14) * 50 + 300, Direction.D, false, this));
			}
		}
	}

	// 键盘监听事件
	private class KeyMonitor extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			homeTank.keyPressed(e);
		}

		public void keyReleased(KeyEvent e) {
			homeTank.keyReleased(e);
		}
	}

}
