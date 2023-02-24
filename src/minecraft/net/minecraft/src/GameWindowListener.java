package net.minecraft.src;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import net.minecraft.client.Minecraft;

public final class GameWindowListener extends WindowAdapter {
	final Minecraft mc;
	final Thread thread;

	public GameWindowListener(Minecraft minecraft1, Thread thread2) {
		this.mc = minecraft1;
		this.thread = thread2;
	}

	public void windowClosing(WindowEvent windowEvent1) {
		this.mc.shutdown();

		try {
			this.thread.join();
		} catch (InterruptedException interruptedException3) {
			interruptedException3.printStackTrace();
		}

		System.exit(0);
	}
}
