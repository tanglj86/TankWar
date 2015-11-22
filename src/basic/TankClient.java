package basic;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TankClient extends Frame {
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	private int x = 5;
	private int y = 5;
	
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

		g.fillOval(x, y, 30, 30);
		// 设置回原来的颜色
		g.setColor(oldColor);
		x += 5;
		y += 5;
	}
	
	@Override
	public void update(Graphics g) {
		if(offScreenImage == null){
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

}
