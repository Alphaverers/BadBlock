package net.minecraft.src;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class GuiMainMenu extends GuiScreen {
	private static final Random rand = new Random();
	String[] logoBlockLayers = new String[]{" *   * * *   * *** *** *** *** *** ***", " ** ** * **  * *   *   * * * * *    * ", " * * * * * * * **  *   **  *** **   * ", " *   * * *  ** *   *   * * * * *    * ", " *   * * *   * *** *** * * * * *    * "};
	private LogoEffectRandomizer[][] logoEffects;
	private float updateCounter = 0.0F;
	private String splashString = "missingno";
	private String[] splashes = new String[]{"MINECRAFT DEMO", "(Stay tuned for more)"};
	public static boolean shw = false;

	public GuiMainMenu() {
		try {
			this.splashString = this.splashes[rand.nextInt(this.splashes.length)];
		} catch (Exception exception2) {
		}

	}

	public void updateScreen() {
		++this.updateCounter;
		if(this.logoEffects != null) {
			for(int i1 = 0; i1 < this.logoEffects.length; ++i1) {
				for(int i2 = 0; i2 < this.logoEffects[i1].length; ++i2) {
					this.logoEffects[i1][i2].updateLogoEffects();
				}
			}
		}

	}

	protected void keyTyped(char c1, int i2) {
	}

	public void initGui() {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(new Date());
		if(calendar1.get(2) + 1 == 11 && calendar1.get(5) == 9) {
			this.splashString = "Happy birthday, ez!";
		} else if(calendar1.get(2) + 1 == 6 && calendar1.get(5) == 1) {
			this.splashString = "Happy birthday, Notch!";
		} else if(calendar1.get(2) + 1 == 12 && calendar1.get(5) == 24) {
			this.splashString = "Merry X-mas!";
		} else if(calendar1.get(2) + 1 == 1 && calendar1.get(5) == 1) {
			this.splashString = "Happy new year!";
		}

		this.controlList.clear();
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 48, "Singleplayer"));
		this.controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 72, "Multiplayer"));
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, "Options..."));
		if(this.mc.session == null) {
			((GuiButton)this.controlList.get(1)).enabled = false;
		}

	}

	protected void actionPerformed(GuiButton guiButton1) {
		if(guiButton1.id == 0) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.options));
		}

		if(guiButton1.id == 1) {
			this.mc.displayGuiScreen(new GuiSelectWorld(this));
		}

		if(guiButton1.id == 2) {
			this.mc.displayGuiScreen(new GuiMultiplayer(this));
		}

	}

	public void drawScreen(int i1, int i2, float f3) {
		this.drawDefaultBackground();
		Tessellator tessellator4 = Tessellator.instance;
		this.drawLogo(f3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/logo.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		tessellator4.setColorOpaque_I(0xFFFFFF);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)(this.width / 2 + 90), 70.0F, 0.0F);
		GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
		float f5 = (1.8F - MathHelper.abs(MathHelper.sin((float)(System.currentTimeMillis() % 1000L) / 1000.0F * (float)Math.PI * 2.0F) * 0.1F)) * 100.0F / (float)(this.fontRenderer.getStringWidth(this.splashString) + 32);
		GL11.glScalef(f5, f5, f5);
		this.drawCenteredString(this.fontRenderer, this.splashString, 0, -8, 16776960);
		GL11.glPopMatrix();
		this.drawString(this.fontRenderer, "Copyright Mojang. Distribution strictly prohibited.", this.width - this.fontRenderer.getStringWidth("Copyright Mojang. Distribution strictly prohibited.") - 2, this.height - 10, 0xFFFFFF);
		long j7 = Runtime.getRuntime().maxMemory();
		long j9 = Runtime.getRuntime().totalMemory();
		String string11 = "Free memory: " + (j7 - Runtime.getRuntime().freeMemory()) * 100L / j7 + "% of " + j7 / 1024L / 1024L + "MB";
		this.drawString(this.fontRenderer, string11, this.width - this.fontRenderer.getStringWidth(string11) - 2, 2, 8421504);
		String string12 = "Allocated memory: " + j9 * 100L / j7 + "% (" + j9 / 1024L / 1024L + "MB)";
		this.drawString(this.fontRenderer, string12, this.width - this.fontRenderer.getStringWidth(string12) - 2, 12, 8421504);
		super.drawScreen(i1, i2, f3);
		this.drawString(this.fontRenderer, "QA Build: DEV1", 2, 12, 2105376);
		}
	

	private void drawLogo(float renderPartialTick) {
		int i2;
		int i3;
		if(this.logoEffects == null) {
			this.logoEffects = new LogoEffectRandomizer[this.logoBlockLayers[0].length()][this.logoBlockLayers.length];

			for(i2 = 0; i2 < this.logoEffects.length; ++i2) {
				for(i3 = 0; i3 < this.logoEffects[i2].length; ++i3) {
					this.logoEffects[i2][i3] = new LogoEffectRandomizer(this, i2, i3);
				}
			}
		}

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		i2 = 120 * (new ScaledResolution(this.mc.displayWidth, this.mc.displayHeight)).scaleFactor;
		GLU.gluPerspective(70.0F, (float)this.mc.displayWidth / (float)i2, 0.05F, 100.0F);
		GL11.glViewport(0, this.mc.displayHeight - i2, this.mc.displayWidth, i2);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glDepthMask(true);

		for(i3 = 0; i3 < 3; ++i3) {
			GL11.glPushMatrix();
			GL11.glTranslatef(0.4F, 0.6F, -12.0F);
			if(i3 == 0) {
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
				GL11.glTranslatef(0.0F, -0.4F, 0.0F);
				GL11.glScalef(0.98F, 1.0F, 1.0F);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			}

			if(i3 == 1) {
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			}

			if(i3 == 2) {
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
			}

			GL11.glScalef(1.0F, -1.0F, 1.0F);
			GL11.glRotatef(15.0F, 1.0F, 0.0F, 0.0F);
			GL11.glScalef(0.89F, 1.0F, 0.4F);
			GL11.glTranslatef((float)(-this.logoBlockLayers[0].length()) * 0.5F, (float)(-this.logoBlockLayers.length) * 0.5F, 0.0F);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
			if(i3 == 0) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/title/black.png"));
			}

			RenderBlocks renderBlocks4 = new RenderBlocks();

			for(int i5 = 0; i5 < this.logoBlockLayers.length; ++i5) {
				for(int i6 = 0; i6 < this.logoBlockLayers[i5].length(); ++i6) {
					if(this.logoBlockLayers[i5].charAt(i6) != 32) {
						GL11.glPushMatrix();
						LogoEffectRandomizer logoEffectRandomizer7 = this.logoEffects[i6][i5];
						float f8 = (float)(logoEffectRandomizer7.prevHeight + (logoEffectRandomizer7.height - logoEffectRandomizer7.prevHeight) * (double)renderPartialTick);
						float f9 = 1.0F;
						float f10 = 1.0F;
						if(i3 == 0) {
							f9 = f8 * 0.04F + 1.0F;
							f10 = 1.0F / f9;
							f8 = 0.0F;
						}

						GL11.glTranslatef((float)i6, (float)i5, f8);
						GL11.glScalef(f9, f9, f9);
						GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
						renderBlocks4.renderBlockAsItem(Block.stone, f10);
						GL11.glPopMatrix();
					}
				}
			}

			GL11.glPopMatrix();
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
		GL11.glEnable(GL11.GL_CULL_FACE);
	}

	public static Random getRandom() {
		return rand;
	}
}
