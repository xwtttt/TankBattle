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
	 * ̹�˴�ս����
	 */
	private static final long serialVersionUID = 1L;
	// �����Ŀ�͸�
	public static final int FRAME_WIDTH = 1600;
	public static final int FRAME_HEIGHT = 900;
	// ��ȡ��Ļ�ĳߴ�
	Toolkit tk = Toolkit.getDefaultToolkit();
	Dimension screenSize = tk.getScreenSize();
	int screenWidth = screenSize.width;
	int screenHeight = screenSize.height;
	// ������Ϸ�˵� ��̨
	MenuBar jmb = null;
	// �˵�
	Menu jm1, jm2, jm3, jm4 = null;
	// �Ӳ˵�
	MenuItem jmi1, jmi2, jmi3, jmi4, jmi5, jmi6, jmi7, jmi8, jmi9 = null;

	public boolean printable = true;
	// ���廭��
	Image screenImage = null;
	// ������
	River river = null;
	// ����tree����
	List<Tree> trees = new ArrayList<Tree>();
	// ����MetalWall����
	List<MetalWall> metalWalls = new ArrayList<MetalWall>();
	// ����CommonWall����
	List<CommonWall> otherWall = new ArrayList<CommonWall>();
	// ����Home��
	Home home = null;
	// ����ҵ�ǽ
	List<CommonWall> homeWall = new ArrayList<CommonWall>();
	// �����ҷ�̹��
	Tank homeTank = null;
	// ����з�̹��
	List<Tank> enemyTank = new ArrayList<Tank>();
	// �����ӵ�����
	List<Bullets> bullets = new ArrayList<Bullets>();

	// main����
	public static void main(String args[]) {
		new Client();
	}

	public void update(Graphics g) {
		// �õ�����
		screenImage = this.createImage(FRAME_WIDTH, FRAME_HEIGHT);
		// ��û����Ļ��ʲ���װ��������
		Graphics gps = screenImage.getGraphics();
		framePaint(gps);
		// ÿ�ν���������Frame�򣬴ﵽˢ��Ч��
		g.drawImage(screenImage, 0, 0, null);
	}

	// ���Ƶ�ͼ��Ϣ
	private void framePaint(Graphics g) {
		// ���ƺ�
		river.draw(g);

		// ���ƽ���ǽ
		for (int i = 0; i < metalWalls.size(); i++) {
			metalWalls.get(i).draw(g);
			homeTank.colliedWithMetalWall(metalWalls.get(i));
			for (int j = 0; j < enemyTank.size(); j++) {
				enemyTank.get(j).colliedWithMetalWall(metalWalls.get(i));
			}
		}

		// ����������ͨǽ
		for (int i = 0; i < otherWall.size(); i++) {
			otherWall.get(i).draw(g);
			homeTank.colliedWithCommonWall(otherWall.get(i));
			for (int j = 0; j < enemyTank.size(); j++) {
				enemyTank.get(j).colliedWithCommonWall(otherWall.get(i));
			}
		}
		// ���Ƽ�
		home.draw(g);

		// ���Ƽҵ�ǽ
		for (int i = 0; i < homeWall.size(); i++) {
			homeWall.get(i).draw(g);
			homeTank.colliedWithCommonWall(homeWall.get(i));
			for (int j = 0; j < enemyTank.size(); j++) {
				enemyTank.get(j).colliedWithCommonWall(homeWall.get(i));
			}
		}

		// �����ҷ�̹��
		homeTank.draw(g);
		// �����ҷ�̹�˺ͺ�������ײ
		homeTank.colliedWithRiver(river);
		// �����ҷ�̹�˺ͼҵ���ײ
		homeTank.colliedWithHome(home);

		// ������
		for (int i = 0; i < trees.size(); i++) {
			trees.get(i).draw(g);
		}
		// ���Ƶз�̹��
		for (int i = 0; i < enemyTank.size(); i++) {
			enemyTank.get(i).draw(g);
		}
		// ���Ƶз�̹�˺ͺ�������ײ
		for (int i = 0; i < enemyTank.size(); i++) {
			enemyTank.get(i).colliedWithRiver(river);
		}

		// ���Ƶз�̹�˺��ҷ�̹�˵���ײ
		for (int i = 0; i < enemyTank.size(); i++) {
			for (int j = 0; j < enemyTank.size(); j++) {
				if (i != j) {
					enemyTank.get(i).colliedWithTank(enemyTank.get(j));
					enemyTank.get(i).colliedWithTank(homeTank);
					homeTank.colliedWithTank(enemyTank.get(i));
				}
			}
		}
		// �����ӵ�
		for (int i = 0; i < bullets.size(); i++) {
			Bullets b = bullets.get(i);
			b.draw(g);
		}
	}

	// �����̣߳�����repaint()
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

	// ��ť����
	@Override
	public void actionPerformed(ActionEvent e) {

	}

	public Client() {
		// ���ô��ڿɼ�
		this.setVisible(true);
		// ���ô��ڴ�С���ɱ�
		this.setResizable(false);
		// ���ô��ڴ�С���ɱ�
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		// ���ô���λ�þ���
		this.setLocation(screenWidth / 2 - FRAME_WIDTH / 2, screenHeight / 2 - FRAME_HEIGHT / 2);
		// ���ñ�����ɫ
		this.setBackground(Color.gray);
		// ���ñ���
		this.setTitle("̹�˴�ս");
		// ���ô��ڹر�ģʽ
		this.addWindowListener(new WindowAdapter() {
			// ���÷���
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		// ��ʼ����Ϸ�˵�
		jmb = new MenuBar();
		jm1 = new Menu("��Ϸ");
		jmi1 = new MenuItem("��ʼ����Ϸ");
		jmi2 = new MenuItem("�˳�");
		// jmi1.setFont(new Font("TimesRoman",Font.BOLD,15));
		// jmi2.setFont(new Font("TimesRoman",Font.BOLD,15));
		jm2 = new Menu("��ͣ/����");
		jmi3 = new MenuItem("��ͣ");
		jmi4 = new MenuItem("����");

		jm3 = new Menu("��Ϸ����");
		jmi5 = new MenuItem("��Ϸ����1");
		jmi6 = new MenuItem("��Ϸ����2");
		jmi7 = new MenuItem("��Ϸ����3");
		jmi8 = new MenuItem("��Ϸ����4");

		jm4 = new Menu("��Ϸ����");
		jmi9 = new MenuItem("��Ϸ˵��");
		// �󶨼����¼�
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
		// ��Ӽ��̼����¼�
		this.addKeyListener(new KeyMonitor());
		// �����߳�,����PaintThread()����
		new Thread(new PaintThread()).start();
		// ��ʼ��River
		river = new River(200, 100);
		// ��ʼ��Tree
		for (int i = 0; i < 5; i++) {
			trees.add(new Tree(i * 40, 500));
			trees.add(new Tree(FRAME_WIDTH / 2 - (i + 3) * 40, 500));
			trees.add(new Tree(FRAME_WIDTH / 2 + (i + 3) * 40, 500));
			trees.add(new Tree(FRAME_WIDTH - (i + 1) * 40, 500));
		}
		// ��ʼ������ǽ
		for (int i = 0; i < 10; i++) {
			metalWalls.add(new MetalWall(300 + i * (30), 200));
			metalWalls.add(new MetalWall(300 + i * (30), 230));
		}
		for (int i = 0; i < 10; i++) {
			metalWalls.add(new MetalWall(1200, FRAME_HEIGHT - i * (30)));
			metalWalls.add(new MetalWall(1230, FRAME_HEIGHT - i * (30)));
		}

		// ��ʼ��������ͨǽ
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
		// ��ʼ���ҵ�ǽ
		for (int i = 0; i < 4; i++) {
			homeWall.add(new CommonWall(755, 795 + i * 30));
			homeWall.add(new CommonWall(865, 795 + i * 30));
			homeWall.add(new CommonWall(770 + i * 30, 795));
		}
		// ��ʼ����
		home = new Home(790, 830);
		// ��ʼ���ҷ�̹��
		homeTank = new Tank(650, 830, Direction.STOP, true, this);
		// ��ʼ���з�20��̹��
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

	// ���̼����¼�
	private class KeyMonitor extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			homeTank.keyPressed(e);
		}

		public void keyReleased(KeyEvent e) {
			homeTank.keyReleased(e);
		}
	}

}
