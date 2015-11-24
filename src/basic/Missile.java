package basic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Missile {
	public static final int WIDTH = 8;
	public static final int HEIGHT = 8;
	private static final int SPEEDX = 8;
	private static final int SPEEDY = 8;
	private int x, y;
	private Direction dir;
	private boolean live = true;
	private TankClient tc;
	private boolean good;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] images = null;
	private static Map<String, Image> missileImages = new HashMap<String, Image>();
	static {
		images = new Image[] {
				tk.getImage(Missile.class.getClassLoader().getResource(
						"images/missileD.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource(
						"images/missileL.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource(
						"images/missileLD.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource(
						"images/missileLU.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource(
						"images/missileR.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource(
						"images/missileRD.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource(
						"images/missileRU.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource(
						"images/missileU.gif")) };

		missileImages.put("D", images[0]);
		missileImages.put("L", images[1]);
		missileImages.put("LD", images[2]);
		missileImages.put("LU", images[3]);
		missileImages.put("R", images[4]);
		missileImages.put("RD", images[5]);
		missileImages.put("RU", images[6]);
		missileImages.put("U", images[7]);
	}

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

	public Missile(int x, int y, boolean good, Direction dir, TankClient tc) {
		this(x, y, dir);
		this.tc = tc;
		this.good = good;
	}

	public void draw(Graphics g) {
		if (!live) {
			tc.missiles.remove(this);
			return;
		}
		Color c = g.getColor();
		if (good)
			g.setColor(Color.RED);
		else
			g.setColor(Color.BLACK);
		switch (dir) {
		case L:
			g.drawImage(missileImages.get("L"), x, y, null);
			break;
		case R:
			g.drawImage(missileImages.get("R"), x, y, null);
			break;
		case U:
			g.drawImage(missileImages.get("U"), x, y, null);
			break;
		case D:
			g.drawImage(missileImages.get("D"), x, y, null);
			break;
		case LU:
			g.drawImage(missileImages.get("LU"), x, y, null);
			break;
		case LD:
			g.drawImage(missileImages.get("LD"), x, y, null);
			break;
		case RU:
			g.drawImage(missileImages.get("RU"), x, y, null);
			break;
		case RD:
			g.drawImage(missileImages.get("RD"), x, y, null);
			break;

		default:
			break;
		}
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
		if (this.good != t.isGood() && this.isLive()) {
			if (this.getRect().intersects(t.getRect()) && t.isLive()) {
				if (t.isGood()) {
					t.setLife(t.getLife() - 10);
					if (t.getLife() <= 0) {
						t.setLive(false);
					}
				} else {
					t.setLive(false);
				}
				this.live = false;
				Explode e = new Explode(x, y, tc);
				tc.explodes.add(e);
				return true;
			}
		}
		return false;
	}

	public boolean hitTanks(List<Tank> tanks) {
		if (this.isLive()) {
			for (int i = 0; i < tanks.size(); i++) {
				if (hitTank(tanks.get(i)))
					return true;
			}
		}
		return false;
	}

	public boolean hitWall(Wall w) {
		if (this.isLive()) {
			if (this.getRect().intersects(w.getRect())) {
				this.live = false;
				return true;
			}
		}
		return false;
	}
}
