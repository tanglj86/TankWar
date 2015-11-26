package basic;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TankClient extends Frame {
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	private static Random r = new Random();

	Tank myTank = new Tank(30, 50, Direction.STOP, true, this);
	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	List<Tank> tanks = new ArrayList<Tank>();
	NetClient nc = new NetClient(this);
	Image offScreenImage = null;
	ConnDialog dialog = new ConnDialog();

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.lauchFrame("TankWar");
	}

	/**
	 * @param name
	 */
	public void lauchFrame(String name) {
		/*
		 * Direction[] d = Direction.values(); for (int i = 0; i < 10; i++) {
		 * int rn = r.nextInt(GAME_HEIGHT / 2); int dir = r.nextInt(d.length);
		 * tanks.add(new Tank(50 + 20 * (i + 1) + rn, 50 + 20 * (i + 1) + rn,
		 * d[dir], false, this)); }
		 */
		setTitle(name);
		setLocation(50, 50);
		setSize(GAME_WIDTH, GAME_HEIGHT);
		setBackground(Color.GREEN);
		setResizable(false);
		setVisible(true);
		this.addKeyListener(new KeyMonitor());
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

		});
		new Thread(new PaintThread()).start();
		//nc.connect("127.0.0.1", TankServer.TCP_PORT);
	}

	@Override
	public void paint(Graphics g) {
		g.drawString("missiles count:" + missiles.size(), 20, 40);
		g.drawString("tanks count:" + tanks.size(), 20, 60);
		g.drawString("explode count:" + explodes.size(), 20, 80);
		myTank.draw(g);
		for (int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
			if(m.hitTank(myTank)){
				TankDeadMsg msg = new TankDeadMsg(myTank.id);
				nc.send(msg);
				MissileDeadMsg mdmMsg = new MissileDeadMsg(m.tankId, m.id);
				nc.send(mdmMsg);
			}
			//m.hitTanks(tanks);
			m.draw(g);
		}

		for (int i = 0; i < explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			t.draw(g);
		}
	}

	@Override
	public void update(Graphics g) {
		if (offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics graphics = offScreenImage.getGraphics();
		Color color = graphics.getColor();
		graphics.setColor(Color.GREEN);
		graphics.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		graphics.setColor(color);
		paint(graphics);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	/**
	 * 重画
	 * 
	 * @author warrior
	 *
	 */
	private class PaintThread implements Runnable {

		@Override
		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 键盘的监听类
	 * 
	 * @author warrior
	 *
	 */
	private class KeyMonitor extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_C) {
				dialog.setVisible(true);
			} else
				myTank.keyReleased(e);
		}
	}

	class ConnDialog extends Dialog {
		Button b = new Button("确定");
		TextField tfIP = new TextField("127.0.0.1", 12);
		TextField tfPort = new TextField("" + TankServer.TCP_PORT, 4);
		TextField tfMyUDPPort = new TextField("2223", 4);

		public ConnDialog() {
			super(TankClient.this, true);

			this.setLayout(new FlowLayout());
			this.add(new Label("IP:"));
			this.add(tfIP);
			this.add(new Label("Port:"));
			this.add(tfPort);
			this.add(new Label("My UDP Port:"));
			this.add(tfMyUDPPort);
			this.add(b);
			this.setLocation(300, 300);
			this.pack();
			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					setVisible(false);
				}
			});
			b.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					String IP = tfIP.getText().trim();
					int port = Integer.parseInt(tfPort.getText().trim());
					int myUDPPort = Integer.parseInt(tfMyUDPPort.getText()
							.trim());
					nc.setUdpPort(myUDPPort);
					nc.connect(IP, port);
					setVisible(false);
				}

			});
		}

	}
}
