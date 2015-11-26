package basic;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class TankNewMsg implements Msg {
	int msgType = Msg.TANK_NEW_MSG;
	Tank tank;
	private TankClient tc;

	public TankNewMsg(Tank t) {
		this.tank = t;
	}

	public TankNewMsg(TankClient tc) {
		this.tc = tc;
	}

	public void send(DatagramSocket ds, String ip, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);
			dos.writeInt(tank.id);
			dos.writeInt(tank.x);
			dos.writeInt(tank.y);
			dos.writeInt(tank.dir.ordinal());
			dos.writeBoolean(tank.good);

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
			boolean good = dis.readBoolean();
			// System.out.println("id: " + id + ",x: " + x + ",y: " + y +
			// ",dir: "
			// + dir.toString() + ",is good: " + good);
			boolean exist = false;
			for (int i = 0; i < tc.tanks.size(); i++) {
				Tank tank2 = tc.tanks.get(i);
				if (tank2.id == id) {
					exist = true;
					break;
				}

			}
			if (!exist) {
				TankNewMsg tnMsg = new TankNewMsg(tc.myTank);
				tc.nc.send(tnMsg);
				
				Tank t = new Tank(x, y, dir, good, tc);
				t.id = id;
				tc.tanks.add(t);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
