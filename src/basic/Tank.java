package basic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Tank {
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	// 移动的距离
	public static final int SPEEDX = 10;
	public static final int SPEEDY = 10;
	private int x, y;
	private boolean bL = false;
	private boolean bR = false;
	private boolean bU = false;
	private boolean bD = false;

	private TankClient tc = null;

	enum Direction {
		L, LU, LD, R, RU, RD, U, D, STOP
	};

	// 坦克方向
	private Direction dir = Direction.STOP;
	// 炮筒方向
	private Direction ptDir = Direction.D;

	public Tank(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Tank(int x, int y, TankClient tc) {
		this(x, y);
		this.tc = tc;
	}

	/**
	 * 画出一辆坦克
	 * 
	 * @param g
	 */
	public void draw(Graphics g) {
		Color oldColor = g.getColor();
		g.setColor(Color.RED);
		move();
		g.fillOval(x, y, WIDTH, HEIGHT);
		// 设置回原来的颜色
		g.setColor(oldColor);

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
	}

	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
		case KeyEvent.VK_CONTROL:
			tc.missile = fire();
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
		int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
		int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
		Missile m = new Missile(x, y, ptDir);
		return m;
	}

}
