package com.jsys;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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

import javax.swing.JOptionPane;

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
	public boolean bombable = true;
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
	// 定义爆炸坦克的集合
	List<BombTank> bombTanks = new ArrayList<BombTank>();
	// 定义炸弹
	Bomb bomb = null;
	// 定义护盾
	HuDun hudun = null;
	// 定义回血图片
	Blood blood = null;
	// 定义星星
	Star star = null;
	int n = 200;

	int level = 30;

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
		// 判断游戏输赢
		// 1.赢
		if (homeTank.isLive() && home.isLive() && enemyTank.size() == 0) {
			// 赢了,清空页面残留子弹，其他砖块，并提升用户过关
			this.bullets.clear();
			this.otherWall.clear();
			this.homeWall.clear();
			this.metalWalls.clear();
			this.trees.clear();
			g.setColor(Color.red);
			g.setFont(new Font("TimesToman", Font.BOLD, 40));
			g.drawString("恭喜过关", 700, 300);
		}
		// 2.输
		if (!homeTank.isGood() || !home.isLive() || !homeTank.isLive()) {
			// 输了
			this.otherWall.clear();
			this.homeWall.clear();
			g.setColor(Color.red);
			g.setFont(new Font("TimesToman", Font.BOLD, 40));
			g.drawString("你输了", 700, 300);
		}

		// 绘制河
		river.draw(g);

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

		// 绘制爆炸
		bomb.draw(g);

		// 绘制护盾
		hudun.draw(g);

		// 绘制回血
		blood.draw(g);

		// 绘制星星
		star.draw(g);

		hudun.colliedWithHuDun(homeTank, homeWall);

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
		// 我方坦克和bomb碰撞
		bomb.colliedWithBomb(homeTank, enemyTank);
		// 我方坦克和血碰撞
		blood.colliedWithBlood(homeTank);
		// 我方坦克和星星碰撞
		star.colliedWithStar(homeTank);

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
			for (int j = 0; j < homeWall.size(); j++) {
				if(metalWalls.size() == 40){					
					b.colliedWithCommonWall(homeWall.get(j));
				}
			}
			for (int j = 0; j < otherWall.size(); j++) {
				b.colliedWithCommonWall(otherWall.get(j));
			}
			for (int j = 0; j < metalWalls.size(); j++) {
				b.colliedWithMetalWall(metalWalls.get(j), bullets);
			}
			b.colliedWithBullets(bullets);
			b.colliedWithTank(homeTank);
			b.colliedWithHome(home);
			for (int j = 0; j < enemyTank.size(); j++) {
				b.colliedWithTank(enemyTank.get(j));
			}
		}
		// 定义爆炸坦克的集合
		for (int i = 0; i < bombTanks.size(); i++) {
			bombTanks.get(i).draw(g);
		}

		// 绘制树
		for (int i = 0; i < trees.size(); i++) {
			trees.get(i).draw(g);
		}
		// 绘制金属墙
		for (int i = 0; i < metalWalls.size(); i++) {
			metalWalls.get(i).draw(g);
			homeTank.colliedWithMetalWall(metalWalls.get(i));
			for (int j = 0; j < enemyTank.size(); j++) {
				enemyTank.get(j).colliedWithMetalWall(metalWalls.get(i));
			}
		}

		// 绘制剩余敌方坦克的数量以及我方坦克的生命值
		g.setColor(Color.green); 
		g.setFont(new Font("TimesToman", Font.BOLD, 40));
		g.drawString("剩余敌方坦克：" + enemyTank.size(), 300, 150);
		g.drawString("生命值：" + homeTank.getLife(), 1300, 150);

		if (hudun.isLive() == false) {
			n--;
		}

		if (n < 0 && metalWalls.size() > 40) {
				metalWalls.remove(metalWalls.size() - 1);
				for (int i = 0; i < 4; i++) {
					homeWall.add(new CommonWall(755, 795 + i * 30));
					homeWall.add(new CommonWall(865, 795 + i * 30));
					homeWall.add(new CommonWall(770 + i * 30, 795));
				}
		}
	}

	// 开启线程，调用repaint()
	private class PaintThread implements Runnable {

		@Override
		public void run() {
			while (printable) {
				repaint();
				try {
					Thread.sleep(level);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 按钮监听
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Begin")) {
			printable = false;
			Object[] options = { "确定", "取消" };
			int response = JOptionPane.showOptionDialog(this, "确定重新开始？", "判断", JOptionPane.YES_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (response == 0) {
				printable = true;
				this.dispose();
				new Client();
			} else {
				printable = true;
				new Thread(new PaintThread()).start();
			}
		} else if (e.getActionCommand().equals("Exit")) {
			System.out.println("Exit");
			printable = false;
			Object[] options = { "确定", "取消" };
			int response = JOptionPane.showOptionDialog(this, "确定退出？", "判断", JOptionPane.YES_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (response == 0) {
				System.exit(0);
			} else {
				printable = true;
				new Thread(new PaintThread()).start();
			}
		} else if (e.getActionCommand().equals("Stop")) {
			printable = false;
		} else if (e.getActionCommand().equals("Continue")) {
			printable = true;
			new Thread(new PaintThread()).start();
		} else if (e.getActionCommand().equals("Level1")) {
			this.level = 50;
		} else if (e.getActionCommand().equals("Level2")) {
			this.level = 40;
		} else if (e.getActionCommand().equals("Level3")) {
			this.level = 30;
		} else if (e.getActionCommand().equals("Level4")) {
			this.level = 20;
		} else if (e.getActionCommand().equals("Help")) {
			System.out.println("Help");
			printable = false;
			JOptionPane.showMessageDialog(null, "用 A、W、S、D控制方向，用J发射子弹", "提示", JOptionPane.INFORMATION_MESSAGE);
			printable = true;
			new Thread(new PaintThread()).start();
		}
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
		this.setBackground(new Color(142,53,74));
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
		
		// 第一次先执行一遍坦克爆炸
		BombTank bt = new BombTank(-100, -100, this);
		bombTanks.add(bt);

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
		// 初始化bomb炸弹
		bomb = new Bomb((int) (Math.random() * 1550), 50 + (int) (Math.random() * 800), this);
		// 初始化护盾
		hudun = new HuDun((int) (Math.random() * 1550), 50 + (int) (Math.random() * 800), this);
		// 初始化回血
		blood = new Blood((int) (Math.random() * 1550), 50 + (int) (Math.random() * 800), this);
		// 初始化星星
		star = new Star((int) (Math.random() * 1550), 50 + (int) (Math.random() * 800), this);
	}

	// 键盘监听事件
	private class KeyMonitor extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			homeTank.keyPressed(e);
			if(e.getKeyCode() == KeyEvent.VK_R){
				dispose();
				new Client();
			}else if(e.getKeyCode() == KeyEvent.VK_U){
				enemyTank.clear();
			}
		}

		public void keyReleased(KeyEvent e) {
			homeTank.keyReleased(e);
		}
	}

}
