package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;

public class TextureCompassFX extends TextureFX {
	protected float[] currentFireFrame = new float[320];
	protected float[] lastFireFrame = new float[320];
	private Minecraft mc;
	private int[] compassIconImageData = new int[256];
	private double currentAngle;
	private double angleDelta;

	public TextureCompassFX(Minecraft minecraft1) {
		super(Item.compass.getIconIndex((ItemStack)null));
		this.mc = minecraft1;
		this.tileImage = 1;

		try {
			BufferedImage bufferedImage2 = ImageIO.read(Minecraft.class.getResource("/gui/items.png"));
			int i3 = this.iconIndex % 16 * 16;
			int i4 = this.iconIndex / 16 * 16;
			bufferedImage2.getRGB(i3, i4, 16, 16, this.compassIconImageData, 0, 16);
		} catch (IOException iOException5) {
			iOException5.printStackTrace();
		}

	}

	public void onTick() {
		for(int i1 = 0; i1 < 256; ++i1) {
			int i2 = this.compassIconImageData[i1] >> 24 & 255;
			int i3 = this.compassIconImageData[i1] >> 16 & 255;
			int i4 = this.compassIconImageData[i1] >> 8 & 255;
			int i5 = this.compassIconImageData[i1] >> 0 & 255;
			if(this.anaglyphEnabled) {
				int i6 = (i3 * 30 + i4 * 59 + i5 * 11) / 100;
				int i7 = (i3 * 30 + i4 * 70) / 100;
				int i8 = (i3 * 30 + i5 * 70) / 100;
				i3 = i6;
				i4 = i7;
				i5 = i8;
			}

			this.imageData[i1 * 4 + 0] = (byte)i3;
			this.imageData[i1 * 4 + 1] = (byte)i4;
			this.imageData[i1 * 4 + 2] = (byte)i5;
			this.imageData[i1 * 4 + 3] = (byte)i2;
		}

		double d20 = 0.0D;
		double d21;
		double d22;
		if(this.mc.theWorld != null && this.mc.thePlayer != null) {
			d21 = (double)this.mc.theWorld.spawnX - this.mc.thePlayer.posX;
			d22 = (double)this.mc.theWorld.spawnZ - this.mc.thePlayer.posZ;
			d20 = (double)(this.mc.thePlayer.rotationYaw - 90.0F) * Math.PI / 180.0D - Math.atan2(d22, d21);
		}

		for(d21 = d20 - this.currentAngle; d21 < -3.141592653589793D; d21 += Math.PI * 2D) {
		}

		while(d21 >= Math.PI) {
			d21 -= Math.PI * 2D;
		}

		if(d21 < -1.0D) {
			d21 = -1.0D;
		}

		if(d21 > 1.0D) {
			d21 = 1.0D;
		}

		this.angleDelta += d21 * 0.1D;
		this.angleDelta *= 0.8D;
		this.currentAngle += this.angleDelta;
		d22 = Math.sin(this.currentAngle);
		double d23 = Math.cos(this.currentAngle);

		int i9;
		int i10;
		int i11;
		int i12;
		int i13;
		int i14;
		int i15;
		short s16;
		int i17;
		int i18;
		int i19;
		for(i9 = -4; i9 <= 4; ++i9) {
			i10 = (int)(8.5D + d23 * (double)i9 * 0.3D);
			i11 = (int)(7.5D - d22 * (double)i9 * 0.3D * 0.5D);
			i12 = i11 * 16 + i10;
			i13 = 100;
			i14 = 100;
			i15 = 100;
			s16 = 255;
			if(this.anaglyphEnabled) {
				i17 = (i13 * 30 + i14 * 59 + i15 * 11) / 100;
				i18 = (i13 * 30 + i14 * 70) / 100;
				i19 = (i13 * 30 + i15 * 70) / 100;
				i13 = i17;
				i14 = i18;
				i15 = i19;
			}

			this.imageData[i12 * 4 + 0] = (byte)i13;
			this.imageData[i12 * 4 + 1] = (byte)i14;
			this.imageData[i12 * 4 + 2] = (byte)i15;
			this.imageData[i12 * 4 + 3] = (byte)s16;
		}

		for(i9 = -8; i9 <= 16; ++i9) {
			i10 = (int)(8.5D + d22 * (double)i9 * 0.3D);
			i11 = (int)(7.5D + d23 * (double)i9 * 0.3D * 0.5D);
			i12 = i11 * 16 + i10;
			i13 = i9 >= 0 ? 255 : 100;
			i14 = i9 >= 0 ? 20 : 100;
			i15 = i9 >= 0 ? 20 : 100;
			s16 = 255;
			if(this.anaglyphEnabled) {
				i17 = (i13 * 30 + i14 * 59 + i15 * 11) / 100;
				i18 = (i13 * 30 + i14 * 70) / 100;
				i19 = (i13 * 30 + i15 * 70) / 100;
				i13 = i17;
				i14 = i18;
				i15 = i19;
			}

			this.imageData[i12 * 4 + 0] = (byte)i13;
			this.imageData[i12 * 4 + 1] = (byte)i14;
			this.imageData[i12 * 4 + 2] = (byte)i15;
			this.imageData[i12 * 4 + 3] = (byte)s16;
		}

	}
}
