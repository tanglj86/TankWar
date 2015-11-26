package basic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class TankServer {

	public static final int TCP_PORT = 8888;
	public static final int UDP_PORT = 6666;
	private static int ID = 100;
	List<Client> clients = new ArrayList<Client>();

	public void start() {
		new Thread(new UDPRunnable()).start();
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(TCP_PORT);
			while (true) {
				Socket s = null;
				try {
					s = ss.accept();
					DataInputStream dis = new DataInputStream(
							s.getInputStream());
					String IP = s.getInetAddress().getHostAddress();
					int udpPort = dis.readInt();
					Client c = new Client(IP, udpPort);
					clients.add(c);
					System.out.println("A Client Connect! Addr- "
							+ s.getInetAddress() + ":" + s.getPort()
							+ "--udp port:" + udpPort);
					DataOutputStream dos = new DataOutputStream(
							s.getOutputStream());
					dos.writeInt(ID++);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (s != null) {
						s.close();
						s = null;
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new TankServer().start();
	}

	private class Client {
		String IP;
		int udpPort;

		public Client(String IP, int udpPort) {
			this.IP = IP;
			this.udpPort = udpPort;
		}
	}

	private class UDPRunnable implements Runnable {
		byte[] buf = new byte[1024];

		@Override
		public void run() {
			DatagramSocket ds = null;
			try {
				ds = new DatagramSocket(UDP_PORT);
				System.out.println("UDP thread started at port:" + UDP_PORT);
				while (true) {
					DatagramPacket dp = new DatagramPacket(buf, buf.length);
					try {
						ds.receive(dp);
						for (int i = 0; i < clients.size(); i++) {
							Client client = clients.get(i);
							dp.setSocketAddress(new InetSocketAddress(client.IP, client.udpPort));
							ds.send(dp);
						}
						System.out.println("a packeage received");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (SocketException e) {
				e.printStackTrace();
			}finally{
				if (ds != null) {
					ds.close();
					ds = null;
				}
			}
			
		}

	}
}
