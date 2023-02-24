package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class NetworkManager {
	public static final Object threadSyncObject = new Object();
	public static int numReadThreads;
	public static int numWriteThreads;
	private Object sendQueueLock = new Object();
	private Socket networkSocket;
	private DataInputStream socketInputStream;
	private DataOutputStream socketOutputStream;
	private boolean isRunning = true;
	private List readPackets = Collections.synchronizedList(new LinkedList());
	private List dataPackets = Collections.synchronizedList(new LinkedList());
	private List chunkDataPackets = Collections.synchronizedList(new LinkedList());
	private NetHandler netHandler;
	private boolean isServerTerminating = false;
	private Thread writeThread;
	private Thread readThread;
	private boolean isTerminating = false;
	private String terminationReason = "";
	private int timeSinceLastRead = 0;
	private int sendQueueByteLength = 0;
	private int chunkDataSendCounter = 0;

	public NetworkManager(Socket socket, String threadName, NetHandler netHandler) throws IOException {
		this.networkSocket = socket;
		this.netHandler = netHandler;
		socket.setTrafficClass(24);
		this.socketInputStream = new DataInputStream(socket.getInputStream());
		this.socketOutputStream = new DataOutputStream(socket.getOutputStream());
		this.readThread = new NetworkReaderThread(this, threadName + " read thread");
		this.writeThread = new NetworkWriterThread(this, threadName + " write thread");
		this.readThread.start();
		this.writeThread.start();
	}

	public void setNetHandler(NetHandler netHandler) {
		this.netHandler = netHandler;
	}

	public void addToSendQueue(Packet packet) {
		if(!this.isServerTerminating) {
			Object object2 = this.sendQueueLock;
			synchronized(this.sendQueueLock) {
				this.sendQueueByteLength += packet.getPacketSize() + 1;
				if(packet.isChunkDataPacket) {
					this.chunkDataPackets.add(packet);
				} else {
					this.dataPackets.add(packet);
				}

			}
		}
	}

	private void sendPacket() {
		try {
			boolean z1 = true;
			Packet packet2;
			Object object3;
			if(!this.dataPackets.isEmpty()) {
				z1 = false;
				object3 = this.sendQueueLock;
				synchronized(this.sendQueueLock) {
					packet2 = (Packet)this.dataPackets.remove(0);
					this.sendQueueByteLength -= packet2.getPacketSize() + 1;
				}

				Packet.writePacket(packet2, this.socketOutputStream);
			}

			if((z1 || this.chunkDataSendCounter-- <= 0) && !this.chunkDataPackets.isEmpty()) {
				z1 = false;
				object3 = this.sendQueueLock;
				synchronized(this.sendQueueLock) {
					packet2 = (Packet)this.chunkDataPackets.remove(0);
					this.sendQueueByteLength -= packet2.getPacketSize() + 1;
				}

				Packet.writePacket(packet2, this.socketOutputStream);
				this.chunkDataSendCounter = 50;
			}

			if(z1) {
				Thread.sleep(10L);
			}
		} catch (InterruptedException interruptedException8) {
		} catch (Exception exception9) {
			if(!this.isTerminating) {
				this.onNetworkError(exception9);
			}
		}

	}

	private void readPacket() {
		try {
			Packet packet1 = Packet.readPacket(this.socketInputStream);
			if(packet1 != null) {
				this.readPackets.add(packet1);
			} else {
				this.networkShutdown("End of stream");
			}
		} catch (Exception exception2) {
			if(!this.isTerminating) {
				this.onNetworkError(exception2);
			}
		}

	}

	private void onNetworkError(Exception exception) {
		exception.printStackTrace();
		this.networkShutdown("Internal exception: " + exception.toString());
	}

	public void networkShutdown(String terminationReason) {
		if(this.isRunning) {
			this.isTerminating = true;
			this.terminationReason = terminationReason;
			(new NetworkMasterThread(this)).start();
			this.isRunning = false;

			try {
				this.socketInputStream.close();
			} catch (Throwable throwable5) {
			}

			try {
				this.socketOutputStream.close();
			} catch (Throwable throwable4) {
			}

			try {
				this.networkSocket.close();
			} catch (Throwable throwable3) {
			}

		}
	}

	public void processReadPackets() throws IOException {
		if(this.sendQueueByteLength > 1048576) {
			this.networkShutdown("Send buffer overflow");
		}

		if(this.readPackets.isEmpty()) {
			if(this.timeSinceLastRead++ == 1200) {
				this.networkShutdown("Timed out");
			}
		} else {
			this.timeSinceLastRead = 0;
		}

		int i1 = 100;

		while(!this.readPackets.isEmpty() && i1-- >= 0) {
			Packet packet2 = (Packet)this.readPackets.remove(0);
			packet2.processPacket(this.netHandler);
		}

		if(this.isTerminating && this.readPackets.isEmpty()) {
			this.netHandler.handleErrorMessage(this.terminationReason);
		}

	}

	public SocketAddress getRemoteAddress() {
		return this.networkSocket.getRemoteSocketAddress();
	}

	public void serverShutdown() {
		this.isServerTerminating = true;
		this.readThread.interrupt();
		(new ThreadMonitorConnection(this)).start();
	}

	public int getNumChunkDataPackets() {
		return this.chunkDataPackets.size();
	}

	static boolean isRunning(NetworkManager networkManager) {
		return networkManager.isRunning;
	}

	static boolean isServerTerminating(NetworkManager networkManager) {
		return networkManager.isServerTerminating;
	}

	static void readNetworkPacket(NetworkManager networkManager) {
		networkManager.readPacket();
	}

	static void sendNetworkPacket(NetworkManager networkManager) {
		networkManager.sendPacket();
	}

	static Thread getReadThread(NetworkManager networkManager) {
		return networkManager.readThread;
	}

	static Thread getWriteThread(NetworkManager networkManager) {
		return networkManager.writeThread;
	}
}
