package basic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

public class Tank {
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	// 移动的距离
	public static final int SPEEDX = 6;
	public static final int SPEEDY = 6;
	private int x, y;
	private int oldX, oldY;
	private boolean bL = false;
	private boolean bR = false;
	private boolean bU = false;
	private boolean bD = false;

	private TankClient tc = null;
	private boolean live = true;

	private static Random r = new Random();
	private BloodBar bb = new BloodBar();

	private int step = r.nextInt(12) + 13;
	private int life = 100;

	/**
	 * @return the life
	 */
	public int getLife() {
		return life;
	}

	/**
	 * @param life
	 *            the life to set
	 */
	public void setLife(int life) {
		this.life = life;
	}

	enum Direction {
		L, LU, LD, R, RU, RD, U, D, STOP
	};

	// 坦克方向
	private Direction dir = Direction.STOP;
	// 炮筒方向
	private Direction ptDir = Direction.D;

	// 正方还是反方
	private boolean good;

	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.good = good;
	}

	public Tank(int x, int y, Direction dir, boolean good, TankClient tc) {
		this(x, y, good);
		this.tc = tc;
		this.dir = dir;
	}

	/**
	 * 画出一辆坦克
	 * 
	 * @param g
	 */
	public void draw(Graphics g) {
		if (!live) {
			if (!good)
				tc.tanks.remove(this);
			return;
		}
		Color oldColor = g.getColor();
		if (good)
			g.setColor(Color.RED);
		else
			g.setColor(Color.BLUE);
		g.fillOval(x, y, WIDTH, HEIGHT);
		// 设置回原来的颜色
		g.setColor(oldColor);
		if (good)
			bb.draw(g);
		move();

		switch (ptDir) {
		case L:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y
					+ Tank.HEIGHT / 2);
			break;
		case R:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH,
					y + Tank.HEIGHT / 2);
			break;
		case U:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH
					/ 2, y);
			break;
		case D:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH
					/ 2, y + Tank.HEIGHT);
			break;
		case LU:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y);
			break;
		case LD:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y
					+ Tank.HEIGHT);
			break;
		case RU:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH,
					y);
			break;
		case RD:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH,
					y + Tank.HEIGHT);
			break;

		default:
			break;
		}
	}

	private void move() {
		this.oldX = x;
		this.oldY = y;
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
		if (this.dir != Direction.STOP)
			this.ptDir = this.dir;
		if (x < 5)
			x = 5;
		if (y < 25)
			y = 25;
		if (x + Tank.WIDTH > TankClient.GAME_WIDTH)
			x = TankClient.GAME_WIDTH - Tank.WIDTH;
		if (y + Tank.HEIGHT > TankClient.GAME_HEIGHT)
			y = TankClient.GAME_HEIGHT - Tank.HEIGHT;

		if (!good) {
			Direction[] d = Direction.values();
			if (step == 0) {
				step = r.nextInt(12) + 3;
				int nextInt = r.nextInt(d.length);
				this.dir = d[nextInt];
				// this.fire();
			}
			step--;
			if (r.nextInt(40) > 38)
				fire();
		}
	}

	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
		case KeyEvent.VK_F2:
			if(!this.live){
				this.live = true;
				this.life = 100;
			}
			break;
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		default:
			break;
		}
		locateDirection();
	}

	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
		case KeyEvent.VK_CONTROL:
			fire();
			break;
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		case KeyEvent.VK_A:
			this.superFire();
			break;
		default:
			break;
		}
		locateDirection();
	}

	private void locateDirection() {
		if (bL && !bU && !bR && !bD)
			dir = Direction.L;
		else if (bL && bU && !bR && !bD)
			dir = Direction.LU;
		else if (!bL && bU && !bR && !bD)
			dir = Direction.U;
		else if (!bL && bU && bR && !bD)
			dir = Direction.RU;
		else if (!bL && !bU && bR && !bD)
			dir = Direction.R;
		else if (!bL && !bU && bR && bD)
			dir = Direction.RD;
		else if (!bL && !bU && !bR && bD)
			dir = Direction.D;
		else if (bL && !bU && !bR && bD)
			dir = Direction.LD;
		else if (!bL && !bU && !bR && !bD)
			dir = Direction.STOP;
	}

	public Missile fire() {
		if (!live)
			return null;
		int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
		int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
		Missile m = new Missile(x, y, good, ptDir, tc);
		tc.missiles.add(m);
		return m;
	}

	private Missile fire(Direction dir) {
		if (!live)
			return null;
		int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
		int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
		Missile m = new Missile(x, y, good, dir, tc);
		tc.missiles.add(m);
		return m;
	}

	public void superFire() {
		if (!live)
			return;
		Direction[] dirs = Direction.values();
		for (int i = 0; i < dirs.length - 1; i++) {
			fire(dirs[i]);
		}
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public boolean isLive() {
		return live;
	}

	public boolean isGood() {
		return good;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	private void fallBack() {
		x = oldX;
		y = oldY;
	}

	public boolean collideWall(Wall w) {
		if (this.isLive()) {
			if (this.getRect().intersects(w.getRect())) {
				this.fallBack();
				return true;
			}
		}
		return false;
	}

	public boolean collideTanks(List<Tank> lists) {
		if (this.isLive()) {
			for (int i = 0; i < lists.size(); i++) {
				Tank tank = lists.get(i);
				if (this != tank) {
					if (this.getRect().intersects(tank.getRect())) {
						this.fallBack();
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean eat(Blood b) {
		if(this.live && b.isLive() && this.getRect().intersects(b.getRect())) {
			this.life = 100;
			b.setLive(false);
			return true;
		}
		return false;
	}

	private class BloodBar {
		public void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y - 10, WIDTH, 5);
			int w = WIDTH * life / 100;
			g.fillRect(x, y - 10, w, 5);
			g.setColor(c);
		}
	}
}
