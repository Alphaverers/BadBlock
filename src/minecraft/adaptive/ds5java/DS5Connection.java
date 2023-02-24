package adaptive.ds5java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Scanner;

public class DS5Connection {
	public DatagramSocket dgramSock;
	public int port = -1;
	public InetAddress addr;
	public boolean connected = false;

	public boolean Connect() {
		File file1 = new File("C:\\Temp\\DualSenseX\\DualSenseX_PortNumber.txt");

		try {
			Scanner scanner2 = new Scanner(file1);
			if(!scanner2.hasNextInt()) {
				scanner2.close();
				return false;
			}

			this.port = scanner2.nextInt();
			scanner2.close();
		} catch (FileNotFoundException fileNotFoundException5) {
			return false;
		}

		try {
			this.addr = InetAddress.getByName("localhost");
			this.dgramSock = new DatagramSocket();
		} catch (UnknownHostException unknownHostException3) {
			return false;
		} catch (SocketException socketException4) {
			return false;
		}

		this.connected = true;
		return true;
	}

	public void Disconnect() {
		this.dgramSock.close();
		this.connected = false;
	}

	public boolean Send(DS5Packet dS5Packet1) {
		if(!this.connected) {
			return false;
		} else {
			byte[] b2 = dS5Packet1.buildJSON().getBytes(Charset.forName("ASCII"));
			DatagramPacket datagramPacket3 = new DatagramPacket(b2, b2.length, this.addr, this.port);

			try {
				this.dgramSock.send(datagramPacket3);
				return true;
			} catch (IOException iOException5) {
				return false;
			}
		}
	}
}
