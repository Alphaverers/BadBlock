package net.minecraft.src;

import java.io.IOException;
import java.net.Socket;

import net.minecraft.server.MinecraftServer;

class NetworkAcceptThread extends Thread {
	final MinecraftServer minecraftServer;
	final NetworkListenThread networkListenThread;

	NetworkAcceptThread(NetworkListenThread thread, String string2, MinecraftServer minecraftServer) {
		super(string2);
		this.networkListenThread = thread;
		this.minecraftServer = minecraftServer;
	}

	public void run() {
		while(this.networkListenThread.isListening) {
			try {
				Socket socket1 = NetworkListenThread.getServerSocket(this.networkListenThread).accept();
				if(socket1 != null) {
					NetLoginHandler netLoginHandler2 = new NetLoginHandler(this.minecraftServer, socket1, "Connection #" + NetworkListenThread.incrementConnections(this.networkListenThread));
					NetworkListenThread.addPendingConnection(this.networkListenThread, netLoginHandler2);
				}
			} catch (IOException iOException3) {
				iOException3.printStackTrace();
			}
		}

	}
}
