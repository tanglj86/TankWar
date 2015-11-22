package basic;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TankClient extends Frame {

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.lauchFrame("TankWar");
	}

	public void lauchFrame(String name) {
		setTitle(name);
		setLocation(50, 50);
		setSize(800, 600);
		setBackground(Color.GREEN);
		setResizable(false);
		setVisible(true);
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

		});
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

		g.fillOval(50, 50, 30, 30);
		// 设置回原来的颜色
		g.setColor(oldColor);
	}

}
