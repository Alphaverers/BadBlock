package net.minecraft.src;

import java.net.ConnectException;
import java.net.UnknownHostException;

import net.minecraft.client.Minecraft;

class ThreadConnectToServer extends Thread {
	final Minecraft mc;
	final String ip;
	final int port;
	final GuiConnecting connectingGui;

	ThreadConnectToServer(GuiConnecting guiConnecting, Minecraft minecraft, String ip, int port) {
		this.connectingGui = guiConnecting;
		this.mc = minecraft;
		this.ip = ip;
		this.port = port;
	}

	public void run() {
		try {
			GuiConnecting.setNetClientHandler(this.connectingGui, new NetClientHandler(this.mc, this.ip, this.port));
			if(GuiConnecting.isCancelled(this.connectingGui)) {
				return;
			}

			GuiConnecting.getNetClientHandler(this.connectingGui).addToSendQueue(new Packet2Handshake(this.mc.session.username));
		} catch (UnknownHostException unknownHostException2) {
			if(GuiConnecting.isCancelled(this.connectingGui)) {
				return;
			}

			this.mc.displayGuiScreen(new GuiConnectFailed("Failed to connect to the server", "Unknown host \'" + this.ip + "\'"));
		} catch (ConnectException connectException3) {
			if(GuiConnecting.isCancelled(this.connectingGui)) {
				return;
			}

			this.mc.displayGuiScreen(new GuiConnectFailed("Failed to connect to the server", connectException3.getMessage()));
		} catch (Exception exception4) {
			if(GuiConnecting.isCancelled(this.connectingGui)) {
				return;
			}

			exception4.printStackTrace();
			this.mc.displayGuiScreen(new GuiConnectFailed("Failed to connect to the server", exception4.toString()));
		}

	}
}
