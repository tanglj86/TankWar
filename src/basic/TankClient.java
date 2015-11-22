package basic;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TankClient extends Frame {
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	public static final int TANK_WIDTH = 30;
	public static final int TANK_HEIGHT = 30;
	private int xPosition = 50;
	private int yPosition = 100;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Window#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		Color oldColor = g.getColor();
		g.setColor(Color.RED);

		g.fillOval(xPosition, yPosition, TANK_WIDTH, TANK_HEIGHT);
		// 设置回原来的颜色
		g.setColor(oldColor);
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
	 * @author warrior
	 *
	 */
	private class KeyMonitor extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			int code = e.getKeyCode();
			switch (code) {
			case KeyEvent.VK_LEFT:
				xPosition -= 5;
				break;

			case KeyEvent.VK_RIGHT:
				xPosition += 5;
				break;
			case KeyEvent.VK_UP:
				yPosition -= 5;
				break;
			case KeyEvent.VK_DOWN:
				yPosition += 5;
				break;
			default:
				break;
			}
		}
	}

}
