package net.minecraft.src;

import java.awt.image.BufferedImage;

public class IsoImageBuffer {
	public BufferedImage image;
	public World level;
	public int x;
	public int y;
	public boolean rendered = false;
	public boolean noContent = false;
	public int lastVisible = 0;
	public boolean addedToRenderQueue = false;

	public IsoImageBuffer(World world1, int i2, int i3) {
		this.level = world1;
		this.init(i2, i3);
	}

	public void init(int i1, int i2) {
		this.rendered = false;
		this.x = i1;
		this.y = i2;
		this.lastVisible = 0;
		this.addedToRenderQueue = false;
	}

	public void setLevel(World world1, int i2, int i3) {
		this.level = world1;
		this.init(i2, i3);
	}
}
