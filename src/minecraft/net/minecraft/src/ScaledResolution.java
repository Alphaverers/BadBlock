package net.minecraft.src;

public class ScaledResolution {
	private int scaledWidth;
	private int scaledHeight;
	public int scaleFactor;

	public ScaledResolution(int i1, int i2) {
		this.scaledWidth = i1;
		this.scaledHeight = i2;

		for(this.scaleFactor = 1; this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240; ++this.scaleFactor) {
		}

		this.scaledWidth /= this.scaleFactor;
		this.scaledHeight /= this.scaleFactor;
	}

	public int getScaledWidth() {
		return this.scaledWidth;
	}

	public int getScaledHeight() {
		return this.scaledHeight;
	}
}
