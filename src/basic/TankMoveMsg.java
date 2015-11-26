package basic;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class TankMoveMsg implements Msg {
	int msgType = Msg.TANK_MOVE_MSG;
	int x,y;
	int id;
	Direction dir;
	private TankClient tc;

	public TankMoveMsg(int id, int x, int y,Direction dir) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.dir = dir;
	}

	public TankMoveMsg(TankClient tc) {
		this.tc = tc;
	}

	@Override
	public void send(DatagramSocket ds, String ip, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);
			dos.writeInt(id);
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(dir.ordinal());

		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] buf = baos.toByteArray();
		DatagramPacket dp = new DatagramPacket(buf, buf.length,
				new InetSocketAddress(ip, udpPort));
		try {
			ds.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void parse(DataInputStream dis) {
		try {
			int id = dis.readInt();
			if (id == this.tc.myTank.id) {
				return;
			}
			int x = dis.readInt();
			int y = dis.readInt();
			int dir1 = dis.readInt();
			Direction dir = Direction.values()[dir1];
			System.out.println("id: " + id + ",dir: " + dir.toString());
			boolean exist = false;
			for (int i = 0; i < tc.tanks.size(); i++) {
				Tank tank = tc.tanks.get(i);
				if (tank.id == id) {
					tank.x = x;
					tank.y = y;
					tank.dir = dir;
					exist = true;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
