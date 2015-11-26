package basic;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class NetClient {
	private int udpPort;
	TankClient tc;

	DatagramSocket ds = null;
	private String IP;

	public NetClient(TankClient tc) {
		this.tc = tc;
	}

	public void connect(String ip, int port) {
		this.IP = ip;
		try {
			ds = new DatagramSocket(udpPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		Socket s = null;
		try {
			s = new Socket(ip, port);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
			DataInputStream dis = new DataInputStream(s.getInputStream());
			int id = dis.readInt();
			tc.myTank.id = id;
			if (id % 2 == 0)
				tc.myTank.good = false;
			else
				tc.myTank.good = true;
			System.out.println("Connected to server and be given a ID:" + id);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (s != null) {
				try {
					s.close();
					s = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		TankNewMsg msg = new TankNewMsg(tc.myTank);
		send(msg);
		new Thread(new UDPReceiveThread()).start();
	}

	public void send(Msg msg) {
		msg.send(ds, IP, TankServer.UDP_PORT);
	}

	private class UDPReceiveThread implements Runnable {

		byte[] buf = new byte[1024];

		@Override
		public void run() {
			System.out.println("UDP thread started at port:" + udpPort);
			while (true) {
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				try {
					ds.receive(dp);
					parse(dp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		private void parse(DatagramPacket dp) throws IOException {
			ByteArrayInputStream bais = new ByteArrayInputStream(buf, 0,
					dp.getLength());
			DataInputStream dis = new DataInputStream(bais);
			int msgType = dis.readInt();
			Msg msg = null;
			switch (msgType) {
			case Msg.TANK_NEW_MSG:
				// System.out.println("a TANK_NEW_MSG received from server");
				msg = new TankNewMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
			case Msg.TANK_MOVE_MSG:
				// System.out.println("a TANK_MOVE_MSG received from server");
				msg = new TankMoveMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
			case Msg.MISSILE_NEW_MSG:
				// System.out.println("a TANK_MOVE_MSG received from server");
				msg = new MissileNewMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
			case Msg.TANK_DEAD_MSG:
				// System.out.println("a TANK_MOVE_MSG received from server");
				msg = new TankDeadMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
			case Msg.MISSILE_DEAD_MSG:
				// System.out.println("a TANK_MOVE_MSG received from server");
				msg = new MissileDeadMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
			}
		}

	}

	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}
}
