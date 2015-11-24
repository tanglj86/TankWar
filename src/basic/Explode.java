package basic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Explode {
	int x, y;
	private boolean live = true;
	private static boolean init = false;

	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] images = {
			tk.getImage(Explode.class.getClassLoader().getResource(
					"images/0.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource(
					"images/1.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource(
					"images/2.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource(
					"images/3.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource(
					"images/4.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource(
					"images/5.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource(
					"images/6.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource(
					"images/7.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource(
					"images/8.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource(
					"images/9.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource(
					"images/10.gif")) };
	private TankClient tc;

	// int[] diameter = { 4, 7, 12, 18, 26, 32, 14, 6 };
	int step = 0;

	public Explode(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}

	public void draw(Graphics g) {
		if(!init){
			for (int i = 0; i < images.length; i++) {
				g.drawImage(images[i], -100, -100, null);
			}
		}
		if (!live) {
			tc.explodes.remove(this);
			return;
		}

		if (step == images.length) {
			live = false;
			step = 0;
			return;
		}

		Color c = g.getColor();
		g.setColor(Color.ORANGE);
		// g.fillOval(x, y, diameter[step], diameter[step]);
		g.drawImage(images[step], x, y, null);
		g.setColor(c);

		step++;
	}

}
