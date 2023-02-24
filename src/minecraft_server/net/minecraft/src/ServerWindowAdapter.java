package net.minecraft.src;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import net.minecraft.server.MinecraftServer;

final class ServerWindowAdapter extends WindowAdapter {
	final MinecraftServer mcServer;

	ServerWindowAdapter(MinecraftServer minecraftServer1) {
		this.mcServer = minecraftServer1;
	}

	public void windowClosing(WindowEvent windowEvent1) {
		this.mcServer.stopRunning();

		while(!this.mcServer.serverStopped) {
			try {
				Thread.sleep(100L);
			} catch (InterruptedException interruptedException3) {
				interruptedException3.printStackTrace();
			}
		}

		System.exit(0);
	}
}
