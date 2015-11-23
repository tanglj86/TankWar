package basic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import basic.Tank.Direction;

public class Missile {
	public static final int WIDTH = 5;
	public static final int HEIGHT = 5;
	private static final int SPEEDX = 15;
	private static final int SPEEDY = 15;
	private int x, y;
	private Direction dir;
	private boolean live = true;
	private TankClient tc;

	/**
	 * @return the live
	 */
	public boolean isLive() {
		return live;
	}

	public Missile(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}

	public Missile(int x, int y, Direction dir, TankClient tc) {
		this(x, y, dir);
		this.tc = tc;
	}

	public void draw(Graphics g) {
		if (!live) {
			tc.missiles.remove(this);
			return;
		}
		Color c = g.getColor();
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(Color.BLACK);
		g.setColor(c);
		move();
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

		if (x < 0 || x > TankClient.GAME_WIDTH || y < 0
				|| y > TankClient.GAME_HEIGHT) {
			this.live = false;
		}
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public boolean hitTank(Tank t) {
		if (this.getRect().intersects(t.getRect()) && t.isLive()) {
			t.setLive(false);
			this.live = false;
			Explode e = new Explode(x, y, tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}

	public boolean hitTanks(List<Tank> tanks) {
		for (int i = 0; i < tanks.size(); i++) {
			if (hitTank(tanks.get(i)))
				return true;
		}
		return false;
	}
}
