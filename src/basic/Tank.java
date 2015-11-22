package basic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Tank {
	public static final int TANK_WIDTH = 30;
	public static final int TANK_HEIGHT = 30;
	// �ƶ��ľ���
	public static final int MOVE_STEP = 5;
	private int x, y;

	public Tank(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * ����һ��̹��
	 * 
	 * @param g
	 */
	public void draw(Graphics g) {
		Color oldColor = g.getColor();
		g.setColor(Color.RED);

		g.fillOval(x, y, TANK_WIDTH, TANK_HEIGHT);
		// ���û�ԭ������ɫ
		g.setColor(oldColor);
	}

	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
		case KeyEvent.VK_LEFT:
			x -= MOVE_STEP;
			break;

		case KeyEvent.VK_RIGHT:
			x += MOVE_STEP;
			break;
		case KeyEvent.VK_UP:
			y -= MOVE_STEP;
			break;
		case KeyEvent.VK_DOWN:
			y += MOVE_STEP;
			break;
		default:
			break;
		}
	}
}
