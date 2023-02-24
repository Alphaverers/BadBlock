package net.minecraft.src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.Timer;

public class GuiStatsComponent extends JComponent {
	private int[] memoryUse = new int[256];
	private int updateCounter = 0;
	private String[] displayStrings = new String[10];

	public GuiStatsComponent() {
		this.setPreferredSize(new Dimension(256, 196));
		this.setMinimumSize(new Dimension(256, 196));
		this.setMaximumSize(new Dimension(256, 196));
		(new Timer(500, new GuiStatsListener(this))).start();
		this.setBackground(Color.BLACK);
	}

	private void update() {
		long j1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.gc();
		this.displayStrings[0] = "Memory use: " + j1 / 1024L / 1024L + " mb (" + Runtime.getRuntime().freeMemory() * 100L / Runtime.getRuntime().maxMemory() + "% free)";
		this.displayStrings[1] = "Threads: " + NetworkManager.numReadThreads + " + " + NetworkManager.numWriteThreads;
		this.memoryUse[this.updateCounter++ & 255] = (int)(j1 * 100L / Runtime.getRuntime().maxMemory());
		this.repaint();
	}

	public void paint(Graphics graphics) {
		graphics.setColor(new Color(0xFFFFFF));
		graphics.fillRect(0, 0, 256, 192);

		int i2;
		for(i2 = 0; i2 < 256; ++i2) {
			int i3 = this.memoryUse[i2 + this.updateCounter & 255];
			graphics.setColor(new Color(i3 + 28 << 16));
			graphics.fillRect(i2, 100 - i3, 1, i3);
		}

		graphics.setColor(Color.BLACK);

		for(i2 = 0; i2 < this.displayStrings.length; ++i2) {
			String string4 = this.displayStrings[i2];
			if(string4 != null) {
				graphics.drawString(string4, 32, 116 + i2 * 16);
			}
		}

	}

	static void update(GuiStatsComponent component) {
		component.update();
	}
}
