package basic;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class TankClient extends Frame {
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;

	Tank myTank = new Tank(30, 50, true, this);
	Tank enemyTank = new Tank(50, 70, false, this);
	List<Missile> missiles = new ArrayList<Missile>();
	Image offScreenImage = null;

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.lauchFrame("TankWar");
	}

	public void lauchFrame(String name) {
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
	}

	@Override
	public void paint(Graphics g) {
		g.drawString("missiles count:" + missiles.size(), 20, 40);
		myTank.draw(g);
		enemyTank.draw(g);
		for (int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
			m.draw(g);
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
	 * ÖØ»­
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
	 * ¼üÅÌµÄ¼àÌýÀà
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
			myTank.keyReleased(e);
		}
	}

}
