package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;

public class TerrainTextureManager {
	private float[] texCols = new float[768];
	private int[] pixels = new int[5120];
	private int[] zBuf = new int[5120];
	private int[] waterBuf = new int[5120];
	private int[] waterBr = new int[5120];
	private int[] yBuf = new int[34];
	private int[] textures = new int[768];

	public TerrainTextureManager() {
		try {
			BufferedImage bufferedImage1 = ImageIO.read(TerrainTextureManager.class.getResource("/terrain.png"));
			int[] i2 = new int[65536];
			bufferedImage1.getRGB(0, 0, 256, 256, i2, 0, 256);

			for(int i3 = 0; i3 < 256; ++i3) {
				int i4 = 0;
				int i5 = 0;
				int i6 = 0;
				int i7 = i3 % 16 * 16;
				int i8 = i3 / 16 * 16;
				int i9 = 0;

				for(int i10 = 0; i10 < 16; ++i10) {
					for(int i11 = 0; i11 < 16; ++i11) {
						int i12 = i2[i11 + i7 + (i10 + i8) * 256];
						int i13 = i12 >> 24 & 255;
						if(i13 > 128) {
							i4 += i12 >> 16 & 255;
							i5 += i12 >> 8 & 255;
							i6 += i12 & 255;
							++i9;
						}
					}

					if(i9 == 0) {
						++i9;
					}

					this.texCols[i3 * 3 + 0] = (float)(i4 / i9);
					this.texCols[i3 * 3 + 1] = (float)(i5 / i9);
					this.texCols[i3 * 3 + 2] = (float)(i6 / i9);
				}
			}
		} catch (IOException iOException14) {
			iOException14.printStackTrace();
		}

		for(int i15 = 0; i15 < 256; ++i15) {
			if(Block.blocksList[i15] != null) {
				this.textures[i15 * 3 + 0] = Block.blocksList[i15].getBlockTextureFromSide(1);
				this.textures[i15 * 3 + 1] = Block.blocksList[i15].getBlockTextureFromSide(2);
				this.textures[i15 * 3 + 2] = Block.blocksList[i15].getBlockTextureFromSide(3);
			}
		}

	}

	public void render(IsoImageBuffer isoImageBuffer1) {
		World world2 = isoImageBuffer1.level;
		if(world2 == null) {
			isoImageBuffer1.noContent = true;
			isoImageBuffer1.rendered = true;
		} else {
			int i3 = isoImageBuffer1.x * 16;
			int i4 = isoImageBuffer1.y * 16;
			int i5 = i3 + 16;
			int i6 = i4 + 16;
			Chunk chunk7 = world2.getChunkFromChunkCoords(isoImageBuffer1.x, isoImageBuffer1.y);
			if(chunk7.isChunkRendered) {
				isoImageBuffer1.noContent = true;
				isoImageBuffer1.rendered = true;
			} else {
				isoImageBuffer1.noContent = false;
				Arrays.fill(this.zBuf, 0);
				Arrays.fill(this.waterBuf, 0);
				Arrays.fill(this.yBuf, 160);

				for(int i8 = i6 - 1; i8 >= i4; --i8) {
					for(int i9 = i5 - 1; i9 >= i3; --i9) {
						int i10 = i9 - i3;
						int i11 = i8 - i4;
						int i12 = i10 + i11;
						boolean z13 = true;

						for(int i14 = 0; i14 < 128; ++i14) {
							int i15 = i11 - i10 - i14 + 160 - 16;
							if(i15 < this.yBuf[i12] || i15 < this.yBuf[i12 + 1]) {
								Block block16 = Block.blocksList[world2.getBlockId(i9, i14, i8)];
								if(block16 == null) {
									z13 = false;
								} else if(block16.material == Material.water) {
									int i24 = world2.getBlockId(i9, i14 + 1, i8);
									if(i24 == 0 || Block.blocksList[i24].material != Material.water) {
										float f25 = (float)i14 / 127.0F * 0.6F + 0.4F;
										float f26 = world2.getBrightness(i9, i14 + 1, i8) * f25;
										if(i15 >= 0 && i15 < 160) {
											int i27 = i12 + i15 * 32;
											if(i12 >= 0 && i12 <= 32 && this.waterBuf[i27] <= i14) {
												this.waterBuf[i27] = i14;
												this.waterBr[i27] = (int)(f26 * 127.0F);
											}

											if(i12 >= -1 && i12 <= 31 && this.waterBuf[i27 + 1] <= i14) {
												this.waterBuf[i27 + 1] = i14;
												this.waterBr[i27 + 1] = (int)(f26 * 127.0F);
											}

											z13 = false;
										}
									}
								} else {
									if(z13) {
										if(i15 < this.yBuf[i12]) {
											this.yBuf[i12] = i15;
										}

										if(i15 < this.yBuf[i12 + 1]) {
											this.yBuf[i12 + 1] = i15;
										}
									}

									float f17 = (float)i14 / 127.0F * 0.6F + 0.4F;
									int i18;
									int i19;
									float f20;
									float f22;
									if(i15 >= 0 && i15 < 160) {
										i18 = i12 + i15 * 32;
										i19 = this.textures[block16.blockID * 3 + 0];
										f20 = (world2.getBrightness(i9, i14 + 1, i8) * 0.8F + 0.2F) * f17;
										if(i12 >= 0 && this.zBuf[i18] <= i14) {
											this.zBuf[i18] = i14;
											this.pixels[i18] = 0xFF000000 | (int)(this.texCols[i19 * 3 + 0] * f20) << 16 | (int)(this.texCols[i19 * 3 + 1] * f20) << 8 | (int)(this.texCols[i19 * 3 + 2] * f20);
										}

										if(i12 < 31) {
											f22 = f20 * 0.9F;
											if(this.zBuf[i18 + 1] <= i14) {
												this.zBuf[i18 + 1] = i14;
												this.pixels[i18 + 1] = 0xFF000000 | (int)(this.texCols[i19 * 3 + 0] * f22) << 16 | (int)(this.texCols[i19 * 3 + 1] * f22) << 8 | (int)(this.texCols[i19 * 3 + 2] * f22);
											}
										}
									}

									if(i15 >= -1 && i15 < 159) {
										i18 = i12 + (i15 + 1) * 32;
										i19 = this.textures[block16.blockID * 3 + 1];
										f20 = world2.getBrightness(i9 - 1, i14, i8) * 0.8F + 0.2F;
										int i21 = this.textures[block16.blockID * 3 + 2];
										f22 = world2.getBrightness(i9, i14, i8 + 1) * 0.8F + 0.2F;
										float f23;
										if(i12 >= 0) {
											f23 = f20 * f17 * 0.6F;
											if(this.zBuf[i18] <= i14 - 1) {
												this.zBuf[i18] = i14 - 1;
												this.pixels[i18] = 0xFF000000 | (int)(this.texCols[i19 * 3 + 0] * f23) << 16 | (int)(this.texCols[i19 * 3 + 1] * f23) << 8 | (int)(this.texCols[i19 * 3 + 2] * f23);
											}
										}

										if(i12 < 31) {
											f23 = f22 * 0.9F * f17 * 0.4F;
											if(this.zBuf[i18 + 1] <= i14 - 1) {
												this.zBuf[i18 + 1] = i14 - 1;
												this.pixels[i18 + 1] = 0xFF000000 | (int)(this.texCols[i21 * 3 + 0] * f23) << 16 | (int)(this.texCols[i21 * 3 + 1] * f23) << 8 | (int)(this.texCols[i21 * 3 + 2] * f23);
											}
										}
									}
								}
							}
						}
					}
				}

				this.postProcess();
				if(isoImageBuffer1.image == null) {
					isoImageBuffer1.image = new BufferedImage(32, 160, 2);
				}

				isoImageBuffer1.image.setRGB(0, 0, 32, 160, this.pixels, 0, 32);
				isoImageBuffer1.rendered = true;
			}
		}
	}

	private void postProcess() {
		for(int i1 = 0; i1 < 32; ++i1) {
			for(int i2 = 0; i2 < 160; ++i2) {
				int i3 = i1 + i2 * 32;
				if(this.zBuf[i3] == 0) {
					this.pixels[i3] = 0;
				}

				if(this.waterBuf[i3] > this.zBuf[i3]) {
					int i4 = this.pixels[i3] >> 24 & 255;
					this.pixels[i3] = ((this.pixels[i3] & 16711422) >> 1) + this.waterBr[i3];
					if(i4 < 128) {
						this.pixels[i3] = Integer.MIN_VALUE + this.waterBr[i3] * 2;
					} else {
						this.pixels[i3] |= 0xFF000000;
					}
				}
			}
		}

	}
}
