package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

public class FontRenderer {
	private int[] charWidth = new int[256];
	public int fontTextureName = 0;
	private int fontDisplayLists;
	private IntBuffer buffer = GLAllocation.createDirectIntBuffer(1024);

	public FontRenderer(GameSettings gameSettings1, String string2, RenderEngine renderEngine3) {
		BufferedImage bufferedImage4;
		try {
			bufferedImage4 = ImageIO.read(RenderEngine.class.getResourceAsStream(string2));
		} catch (IOException iOException18) {
			throw new RuntimeException(iOException18);
		}

		int i5 = bufferedImage4.getWidth();
		int i6 = bufferedImage4.getHeight();
		int[] i7 = new int[i5 * i6];
		bufferedImage4.getRGB(0, 0, i5, i6, i7, 0, i5);

		int i9;
		int i10;
		int i11;
		int i12;
		for(int i8 = 0; i8 < 256; ++i8) {
			i9 = i8 % 16;
			i10 = i8 / 16;

			for(i11 = 7; i11 >= 0; --i11) {
				i12 = i9 * 8 + i11;
				boolean z13 = true;

				for(int i14 = 0; i14 < 8 && z13; ++i14) {
					if((i7[i12 + (i10 * 8 + i14) * i5] & 255) > 0) {
						z13 = false;
					}
				}

				if(!z13) {
					break;
				}
			}

			if(i8 == 32) {
				i11 = 2;
			}

			this.charWidth[i8] = i11 + 2;
		}

		this.fontTextureName = renderEngine3.allocateAndSetupTexture(bufferedImage4);
		this.fontDisplayLists = GLAllocation.generateDisplayLists(288);
		Tessellator tessellator19 = Tessellator.instance;

		for(i9 = 0; i9 < 256; ++i9) {
			GL11.glNewList(this.fontDisplayLists + i9, GL11.GL_COMPILE);
			tessellator19.startDrawingQuads();
			i10 = i9 % 16 * 8;
			i11 = i9 / 16 * 8;
			tessellator19.addVertexWithUV(0.0D, 7.989999771118164D, 0.0D, (double)((float)i10 / 128.0F + 0.0F), (double)(((float)i11 + 7.99F) / 128.0F + 0.0F));
			tessellator19.addVertexWithUV(7.989999771118164D, 7.989999771118164D, 0.0D, (double)(((float)i10 + 7.99F) / 128.0F + 0.0F), (double)(((float)i11 + 7.99F) / 128.0F + 0.0F));
			tessellator19.addVertexWithUV(7.989999771118164D, 0.0D, 0.0D, (double)(((float)i10 + 7.99F) / 128.0F + 0.0F), (double)((float)i11 / 128.0F + 0.0F));
			tessellator19.addVertexWithUV(0.0D, 0.0D, 0.0D, (double)((float)i10 / 128.0F + 0.0F), (double)((float)i11 / 128.0F + 0.0F));
			tessellator19.draw();
			GL11.glTranslatef((float)this.charWidth[i9], 0.0F, 0.0F);
			GL11.glEndList();
		}

		for(i9 = 0; i9 < 32; ++i9) {
			i10 = (i9 >> 3 & 1) * 85;
			i11 = (i9 >> 2 & 1) * 170 + i10;
			i12 = (i9 >> 1 & 1) * 170 + i10;
			int i20 = (i9 >> 0 & 1) * 170 + i10;
			if(i9 == 6) {
				i11 += 85;
			}

			boolean z21 = i9 >= 16;
			if(gameSettings1.anaglyph) {
				int i15 = (i11 * 30 + i12 * 59 + i20 * 11) / 100;
				int i16 = (i11 * 30 + i12 * 70) / 100;
				int i17 = (i11 * 30 + i20 * 70) / 100;
				i11 = i15;
				i12 = i16;
				i20 = i17;
			}

			if(z21) {
				i11 /= 4;
				i12 /= 4;
				i20 /= 4;
			}

			GL11.glNewList(this.fontDisplayLists + 256 + i9, GL11.GL_COMPILE);
			GL11.glColor3f((float)i11 / 255.0F, (float)i12 / 255.0F, (float)i20 / 255.0F);
			GL11.glEndList();
		}

	}

	public void drawStringWithShadow(String string1, int i2, int i3, int i4) {
		this.renderString(string1, i2 + 1, i3 + 1, i4, true);
		this.drawString(string1, i2, i3, i4);
	}

	public void drawString(String string1, int i2, int i3, int i4) {
		this.renderString(string1, i2, i3, i4, false);
	}

	public void renderString(String string1, int i2, int i3, int i4, boolean z5) {
		if(string1 != null) {
			if(z5) {
				int i6 = i4 >> 24 & 255;
				int i7 = i4 & 0xFF000000;
				i4 = (i4 & 16579836) >> 2;
				i4 += i7;
				if(i6 != 255 && i6 != 0) {
					i4 |= i6 / 2 << 24;
				} else {
					i4 |= 0xFF000000;
				}
			}

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fontTextureName);
			float f12 = (float)(i4 >> 16 & 255) / 255.0F;
			float f13 = (float)(i4 >> 8 & 255) / 255.0F;
			float f8 = (float)(i4 & 255) / 255.0F;
			float f9 = (float)(i4 >> 24 & 255) / 255.0F;
			if(f9 == 0.0F) {
				f9 = 1.0F;
			}

			GL11.glColor4f(f12, f13, f8, f9);
			this.buffer.clear();
			GL11.glPushMatrix();
			GL11.glTranslatef((float)i2, (float)i3, 0.0F);

			for(int i10 = 0; i10 < string1.length(); ++i10) {
				int i11;
				for(; string1.charAt(i10) == 167 && string1.length() > i10 + 1; i10 += 2) {
					i11 = "0123456789abcdef".indexOf(string1.toLowerCase().charAt(i10 + 1));
					if(i11 < 0 || i11 > 15) {
						i11 = 15;
					}

					this.buffer.put(this.fontDisplayLists + 256 + i11 + (z5 ? 16 : 0));
					if(this.buffer.remaining() == 0) {
						this.buffer.flip();
						GL11.glCallLists(this.buffer);
						this.buffer.clear();
					}
				}

				i11 = " !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_\'abcdefghijklmnopqrstuvwxyz{|}~\u2302\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8?\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1???\u00ae\u00ac???\u00ab\u00bb".indexOf(string1.charAt(i10));
				if(i11 >= 0) {
					this.buffer.put(this.fontDisplayLists + i11 + 32);
				}

				if(this.buffer.remaining() == 0) {
					this.buffer.flip();
					GL11.glCallLists(this.buffer);
					this.buffer.clear();
				}
			}

			this.buffer.flip();
			GL11.glCallLists(this.buffer);
			GL11.glPopMatrix();
			GL11.glDisable(GL11.GL_BLEND);
		}
	}

	public int getStringWidth(String string1) {
		if(string1 == null) {
			return 0;
		} else {
			int i2 = 0;

			for(int i3 = 0; i3 < string1.length(); ++i3) {
				if(string1.charAt(i3) == 167) {
					++i3;
				} else {
					int i4 = " !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_\'abcdefghijklmnopqrstuvwxyz{|}~\u2302\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8?\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1???\u00ae\u00ac???\u00ab\u00bb".indexOf(string1.charAt(i3));
					if(i4 >= 0) {
						i2 += this.charWidth[i4 + 32];
					}
				}
			}

			return i2;
		}
	}
}
