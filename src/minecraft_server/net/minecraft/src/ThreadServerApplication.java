package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public final class ThreadServerApplication extends Thread {
	final MinecraftServer mcServer;

	public ThreadServerApplication(String threadName, MinecraftServer minecraftServer) {
		super(threadName);
		this.mcServer = minecraftServer;
	}

	public void run() {
		this.mcServer.run();
	}
}
