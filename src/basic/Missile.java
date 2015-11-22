package basic;

import java.awt.Color;
import java.awt.Graphics;

import basic.Tank.Direction;

public class Missile {
	public static final int WIDTH = 5;
	public static final int HEIGHT = 5;
	private static final int SPEEDX = 15;
	private static final int SPEEDY = 15;
	private int x, y;
	private Direction dir;

	public Missile(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}

	public void draw(Graphics g) {
		Color c = g.getColor();
		move();
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(Color.BLACK);
		g.setColor(c);
	}

	private void move() {
		switch (dir) {
		case L:
			x -= SPEEDX;
			break;
		case R:
			x += SPEEDX;
			break;
		case U:
			y -= SPEEDY;
			break;
		case D:
			y += SPEEDY;
			break;
		case LU:
			x -= SPEEDX;
			y -= SPEEDY;
			break;
		case LD:
			x -= SPEEDX;
			y += SPEEDY;
			break;
		case RU:
			x += SPEEDX;
			y -= SPEEDY;
			break;
		case RD:
			x += SPEEDX;
			y += SPEEDY;
			break;

		default:
			break;
		}
	}
}
