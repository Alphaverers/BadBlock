package net.minecraft.src;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;

public class ImageBufferDownload implements ImageBuffer {
	private int[] imageData;
	private int imageWidth;
	private int imageHeight;

	public BufferedImage parseUserSkin(BufferedImage bufferedImage1) {
		if(bufferedImage1 == null) {
			return null;
		} else {
			this.imageWidth = 64;
			this.imageHeight = 32;
			BufferedImage bufferedImage2 = new BufferedImage(this.imageWidth, this.imageHeight, 2);
			Graphics graphics3 = bufferedImage2.getGraphics();
			graphics3.drawImage(bufferedImage1, 0, 0, (ImageObserver)null);
			graphics3.dispose();
			this.imageData = ((DataBufferInt)bufferedImage2.getRaster().getDataBuffer()).getData();
			this.setAreaOpaque(0, 0, 32, 16);
			this.setAreaTransparent(32, 0, 64, 32);
			this.setAreaOpaque(0, 16, 64, 32);
			boolean z4 = false;

			int i5;
			int i6;
			int i7;
			for(i5 = 32; i5 < 64; ++i5) {
				for(i6 = 0; i6 < 16; ++i6) {
					i7 = this.imageData[i5 + i6 * 64];
					if((i7 >> 24 & 255) < 128) {
						z4 = true;
					}
				}
			}

			if(!z4) {
				for(i5 = 32; i5 < 64; ++i5) {
					for(i6 = 0; i6 < 16; ++i6) {
						i7 = this.imageData[i5 + i6 * 64];
						if((i7 >> 24 & 255) < 128) {
							z4 = true;
						}
					}
				}
			}

			return bufferedImage2;
		}
	}

	private void setAreaTransparent(int i1, int i2, int i3, int i4) {
		if(!this.hasTransparency(i1, i2, i3, i4)) {
			for(int i5 = i1; i5 < i3; ++i5) {
				for(int i6 = i2; i6 < i4; ++i6) {
					this.imageData[i5 + i6 * this.imageWidth] &= 0xFFFFFF;
				}
			}

		}
	}

	private void setAreaOpaque(int i1, int i2, int i3, int i4) {
		for(int i5 = i1; i5 < i3; ++i5) {
			for(int i6 = i2; i6 < i4; ++i6) {
				this.imageData[i5 + i6 * this.imageWidth] |= 0xFF000000;
			}
		}

	}

	private boolean hasTransparency(int i1, int i2, int i3, int i4) {
		for(int i5 = i1; i5 < i3; ++i5) {
			for(int i6 = i2; i6 < i4; ++i6) {
				int i7 = this.imageData[i5 + i6 * this.imageWidth];
				if((i7 >> 24 & 255) < 128) {
					return true;
				}
			}
		}

		return false;
	}
}
