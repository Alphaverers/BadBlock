package net.minecraft.src;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiIngame extends Gui {
	private static RenderItem itemRenderer = new RenderItem();
	private List chatMessageList = new ArrayList();
	private Random rand = new Random();
	private Minecraft mc;
	public String testMessage = null;
	private int updateCounter = 0;
	private String recordPlaying = "";
	private int recordPlayingUpFor = 0;
	public float damageGuiPartialTime;
	float prevVignetteBrightness = 1.0F;
	public boolean renderQAName = true;
	public static String uqKey = "";
	private boolean initedArea = false;
	private long lastSeed;
	private int lgroupX;
	private int lgroupY;
	public String currentArea = "";
	public long areaTimer = 0L;
	private static final String[] syllab = new String[]{"SIE", "LOH", "KII", "HUR", "MIS", "RUU", "VY", "KA", "TAV", "OLE", "PAH", "MUI", "MAT", "JA", "SAU", "NIN", "UD", "MU", "NGI", "BAR", "LUG", "MAH", "GIR", "AK", "USU", "ESE", "IRU", "UUN", "AMTU", "AGAS", "HI", "TOOI", "YORU", "NEN", "PON", "ONNA", "TSU", "YA", "AO", "ONI", "AN", "KO", "SHI", "YUME", "YARI", "TEST"};

	public void RenderHungerBar() {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/icons.png"));
		this.drawTexturedModalRect(0, 0, 0, 27, 32, 16);
		int i1 = this.mc.thePlayer.hunger < 300 ? 0 : (this.mc.thePlayer.hunger < 600 ? 1 : (this.mc.thePlayer.hunger < 900 ? 2 : 3));
		i1 = 3 - i1;
		this.drawTexturedModalRect(0, 0, i1 * 32, 43, 32, 16);
	}

	public void renderSomethingIdk(int i1, int i2, int i3, int i4, int i5, int i6) {
		Tessellator tessellator10 = Tessellator.instance;
		tessellator10.startDrawingQuads();
		tessellator10.addVertexWithUV((double)(i1 + 0), (double)(i2 + i6), 0.0D, (double)((float)(i3 + 0) * 0.00390625F), (double)((float)(i4 + i6) * 0.00390625F));
		tessellator10.addVertexWithUV((double)(i1 + i5), (double)(i2 + i6), 0.0D, (double)((float)(i3 + i5) * 0.00390625F), (double)((float)(i4 + i6) * 0.00390625F));
		tessellator10.addVertexWithUV((double)(i1 + i5), (double)(i2 + 0), 0.0D, (double)((float)(i3 + i5) * 0.00390625F), (double)((float)(i4 + 0) * 0.00390625F));
		tessellator10.addVertexWithUV((double)(i1 + 0), (double)(i2 + 0), 0.0D, (double)((float)(i3 + 0) * 0.00390625F), (double)((float)(i4 + 0) * 0.00390625F));
		tessellator10.setNormal(0.0F, 1.0F, 0.0F);
		tessellator10.draw();
	}

	public static String Namegen2(long j0, int i2, int i3) {
		long j4 = (long)(i2 + 392214);
		long j6 = (long)(i3 + 392214);
		long j8 = j6 * 784428L + j4;
		long j10000 = j0 + j8;
		int i12 = (int)Math.sqrt((double)(i2 * i2 + i3 * i3));
		int i13 = 0;
		int i14 = 3;

		for(int i15 = i12; i15 / i14 > 0; i14 *= 4) {
			++i13;
		}

		String string17 = "";
		if(i13 > 0) {
			for(int i16 = 0; i16 != i13; ++i16) {
				string17 = string17 + syllab[(int)(((long)(i12 + i13 + i14) + j8 * (long)(2 + i16)) % 45L)];
			}

			string17 = string17 + "-";
		}

		string17 = string17 + syllab[(int)((j8 * 2L + (long)i12) % 45L)];
		string17 = string17 + syllab[(int)((j8 * 3L + (long)i12) % 45L)];
		string17 = string17 + syllab[(int)((j8 * 4L + (long)i12) % 45L)];
		return string17;
	}

	public void ResetNamegen() {
		this.lgroupX = (int)(this.mc.thePlayer.posX / 32.0D);
		this.lgroupY = (int)(this.mc.thePlayer.posZ / 32.0D);
		this.lastSeed = this.mc.theWorld.randomSeed;
		this.initedArea = true;
		this.currentArea = Namegen2(this.lastSeed, this.lgroupX, this.lgroupY);
		System.out.println("Name at " + this.mc.thePlayer.posX + " " + this.mc.thePlayer.posZ + ": " + this.currentArea);
		this.areaTimer = System.currentTimeMillis();
	}

	public boolean NamegenNeedsReset() {
		int i1 = (int)(this.mc.thePlayer.posX / 32.0D);
		int i2 = (int)(this.mc.thePlayer.posZ / 32.0D);
		return !this.initedArea || this.lastSeed != this.mc.theWorld.randomSeed || i1 != this.lgroupX || i2 != this.lgroupY;
	}

	public GuiIngame(Minecraft minecraft) {
		this.mc = minecraft;
	}

	public void renderGameOverlay(float renderPartialTick, boolean hasScreen, int width, int height) {
		ScaledResolution scaledResolution5 = new ScaledResolution(this.mc.displayWidth, this.mc.displayHeight);
		int i6 = scaledResolution5.getScaledWidth();
		int i7 = scaledResolution5.getScaledHeight();
		FontRenderer fontRenderer8 = this.mc.fontRenderer;
		this.mc.entityRenderer.setupOverlayRendering();
		GL11.glEnable(GL11.GL_BLEND);
		if(this.mc.options.fancyGraphics) {
			this.renderVignette(this.mc.thePlayer.getBrightness(renderPartialTick), i6, i7);
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/gui.png"));
		InventoryPlayer inventoryPlayer9 = this.mc.thePlayer.inventory;
		this.zLevel = -90.0F;
		this.drawTexturedModalRect(i6 / 2 - 91, i7 - 22, 0, 0, 182, 22);
		this.drawTexturedModalRect(i6 / 2 - 91 - 1 + inventoryPlayer9.currentItem * 20, i7 - 22 - 1, 0, 22, 24, 22);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/icons.png"));
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
		this.drawTexturedModalRect(i6 / 2 - 7, i7 / 2 - 7, 0, 0, 16, 16);
		GL11.glDisable(GL11.GL_BLEND);
		boolean z10 = this.mc.thePlayer.heartsLife / 3 % 2 == 1;
		if(this.mc.thePlayer.heartsLife < 10) {
			z10 = false;
		}

		int i11 = this.mc.thePlayer.health;
		int i12 = this.mc.thePlayer.prevHealth;
		this.rand.setSeed((long)(this.updateCounter * 312871));
		int i13;
		int i14;
		int i16;
		int i20;
		int i30;
		if(this.mc.playerController.shouldDrawHUD()) {
			i13 = this.mc.thePlayer.getPlayerArmorValue();
			i14 = i6 / 2 + 19;
			int i15 = i7 - (this.mc.options.difficulty == 4 ? 32 : 41);

			int i19;
			for(i16 = 0; i16 != 4; ++i16) {
				ItemStack itemStack17 = this.mc.thePlayer.inventory.armorItemInSlot(i16);
				if(itemStack17 != null && itemStack17.getItem() instanceof ItemArmor) {
					Item item18 = itemStack17.getItem();
					i19 = itemStack17.getMaxDamage();
					i20 = i19 - itemStack17.itemDmg;
					float f21 = (float)i20 / (float)i19;
					this.drawTexturedModalRect(i14 + 18 * i16, i15, 52, 9, 9, 9);
					this.drawTexturedModalRect(i14 + 9 + 18 * i16, i15, 52, 9, 9, 9);
					if(f21 >= 0.75F) {
						this.drawTexturedModalRect(i14 + 18 * i16, i15, 88, 9, 9, 9);
						this.drawTexturedModalRect(i14 + 9 + 18 * i16, i15, 88, 9, 9, 9);
					} else if(f21 >= 0.5F) {
						this.drawTexturedModalRect(i14 + 18 * i16, i15, 97, 9, 9, 9);
						this.drawTexturedModalRect(i14 + 9 + 18 * i16, i15, 88, 9, 9, 9);
					} else if(f21 >= 0.25F) {
						this.drawTexturedModalRect(i14 + 9 + 18 * i16, i15, 88, 9, 9, 9);
					} else {
						this.drawTexturedModalRect(i14 + 9 + 18 * i16, i15, 97, 9, 9, 9);
					}
				}
			}

			if(this.mc.thePlayer.dashTimer != 0) {
				this.drawGradientRect(i6 / 2 - 50 - 1, i7 - 90 - 1, i6 / 2 - 50 + 100 + 1, i7 - 90 + 5 + 1, -14671840, 0xFF000000);
				this.drawGradientRect(i6 / 2 - 50, i7 - 90, i6 / 2 - 50 + (int)(100.0F * (1.0F - (float)this.mc.thePlayer.dashTimer / 30.0F)), i7 - 90 + 5, -3584, -13893888);
			}

			if(this.mc.theWorld.bossfightInProgress) {
				this.drawGradientRect(i6 / 2 - 150 - 1, 59, i6 / 2 - 150 + 301, 66, -14671840, 0xFF000000);
				this.drawGradientRect(i6 / 2 - 150, 60, i6 / 2 - 150 + (int)(300.0F * ((float)this.mc.theWorld.bossRef.health / (float)this.mc.theWorld.bossRef.maxHP)), 65, -3070185, -786683);
				fontRenderer8.drawStringWithShadow(this.mc.theWorld.bossname, i6 / 2 - 150 - 1, 65, 0xFFFFFF);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/icons.png"));
			}

			int i31;
			if(this.mc.options.difficulty == 4) {
				if(this.mc.thePlayer.health != 0) {
					i16 = i6 / 2 - 91;
					i30 = i7 - 32;
					this.drawTexturedModalRect(i16, i30, 0, 16, 9, 9);
				}
			} else {
				for(i16 = 0; i16 < 10; ++i16) {
					i30 = i7 - 32;
					if(i13 > 0) {
						i31 = i6 / 2 + 91 - i16 * 8 - 9;
						if(i16 * 2 + 1 < i13) {
							this.drawTexturedModalRect(i31, i30, 34, 9, 9, 9);
						}

						if(i16 * 2 + 1 == i13) {
							this.drawTexturedModalRect(i31, i30, 25, 9, 9, 9);
						}

						if(i16 * 2 + 1 > i13) {
							this.drawTexturedModalRect(i31, i30, 16, 9, 9, 9);
						}
					}

					byte b32 = 0;
					if(z10) {
						b32 = 1;
					}

					i19 = i6 / 2 - 91 + i16 * 8;
					if(i11 <= 4) {
						i30 += this.rand.nextInt(2);
					}

					this.drawTexturedModalRect(i19, i30, 16 + b32 * 9, 0, 9, 9);
					if(z10) {
						if(i16 * 2 + 1 < i12) {
							this.drawTexturedModalRect(i19, i30, 70, 0, 9, 9);
						}

						if(i16 * 2 + 1 == i12) {
							this.drawTexturedModalRect(i19, i30, 79, 0, 9, 9);
						}
					}

					if(i16 * 2 + 1 < i11) {
						this.drawTexturedModalRect(i19, i30, 52, 0, 9, 9);
					}

					if(i16 * 2 + 1 == i11) {
						this.drawTexturedModalRect(i19, i30, 61, 0, 9, 9);
					}
				}
			}

			if(this.mc.thePlayer.isInsideOfMaterial(Material.water)) {
				i16 = (int)Math.ceil((double)(this.mc.thePlayer.air - 2) * 10.0D / 300.0D);
				i30 = (int)Math.ceil((double)this.mc.thePlayer.air * 10.0D / 300.0D) - i16;

				for(i31 = 0; i31 < i16 + i30; ++i31) {
					if(i31 < i16) {
						this.drawTexturedModalRect(i6 / 2 - 91 + i31 * 8, i7 - 32 - 9, 16, 18, 9, 9);
					} else {
						this.drawTexturedModalRect(i6 / 2 - 91 + i31 * 8, i7 - 32 - 9, 25, 18, 9, 9);
					}
				}
			}
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glPushMatrix();
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		GLStatics.b();
		GL11.glPopMatrix();

		for(i13 = 0; i13 < 9; ++i13) {
			this.renderInventorySlot(i13, i6 / 2 - 90 + i13 * 20 + 2, i7 - 16 - 3, renderPartialTick);
		}

		GLStatics.a();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		if(this.mc.options.d) {
			fontRenderer8.drawStringWithShadow("Minecraft Alpha 1.0.16.05_13 Lilypad", 2, 2, 0xFFFFFF);
			fontRenderer8.drawStringWithShadow(this.mc.debug, 2, 12, 0xFFFFFF);
			fontRenderer8.drawStringWithShadow(this.mc.debugInfoRenders(), 2, 22, 0xFFFFFF);
			fontRenderer8.drawStringWithShadow(this.mc.getEntityDebug(), 2, 32, 0xFFFFFF);
			fontRenderer8.drawStringWithShadow(this.mc.debugInfoEntities(), 2, 42, 0xFFFFFF);
			if(this.mc.theWorld != null) {
				fontRenderer8.drawStringWithShadow(Long.toString(this.mc.theWorld.randomSeed), 2, 52, 0xFFFFFF);
			}

			if(uqKey != "") {
				fontRenderer8.drawStringWithShadow(uqKey, 2, 52, 0xFFFFFF);
			}

			long j25 = Runtime.getRuntime().maxMemory();
			long j27 = Runtime.getRuntime().totalMemory();
			long j33 = j27 - Runtime.getRuntime().freeMemory();
			String string35 = "Used memory: " + j33 * 100L / j25 + "% (" + j33 / 1024L / 1024L + "MB) of " + j25 / 1024L / 1024L + "MB";
			this.drawString(fontRenderer8, string35, i6 - fontRenderer8.getStringWidth(string35) - 2, 2, 14737632);
			String string36 = "Allocated memory: " + j27 * 100L / j25 + "% (" + j27 / 1024L / 1024L + "MB)";
			this.drawString(fontRenderer8, string36, i6 - fontRenderer8.getStringWidth(string36) - 2, 12, 14737632);
		} else {
			fontRenderer8.drawStringWithShadow("Minecraft Alpha 1.0.16.05_13 Lilypad", 2, 2, 0xFFFFFF);
			fontRenderer8.drawStringWithShadow("BADBLOCK alpha-1.1", 5, 15, 0xFFFF00);
			if(uqKey != "") {
				fontRenderer8.drawStringWithShadow(uqKey, 2, 12, 0xFFFFFF);
			}
		}

		GL11.glPushMatrix();
		if(this.NamegenNeedsReset()) {
			this.ResetNamegen();
		}

		float f26 = (float)(1.0D * Math.pow(0.5D, (double)(this.currentArea.length() / 10)));
		GL11.glScalef(1.0F + f26, 1.0F + f26, 1.0F + f26);
		i14 = (int)(255.0F * Math.max(1.0F - (float)Math.min(System.currentTimeMillis() - this.areaTimer, 5000L) / 5000.0F, 0.3F));
		if(i14 != 0) {
			fontRenderer8.drawStringWithShadow(this.currentArea, i6 / 2 - 80, 20, 0xFFFFFF + i14 * 16777216);
		}

		GL11.glPopMatrix();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/icons.png"));
		if(this.recordPlayingUpFor > 0) {
			float f28 = (float)this.recordPlayingUpFor - renderPartialTick;
			i16 = (int)(f28 * 256.0F / 20.0F);
			if(i16 > 255) {
				i16 = 255;
			}

			if(i16 > 0) {
				GL11.glPushMatrix();
				GL11.glTranslatef((float)(i6 / 2), (float)(i7 - 48), 0.0F);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				fontRenderer8.drawString(this.recordPlaying, -fontRenderer8.getStringWidth(this.recordPlaying) / 2, -4, (Color.HSBtoRGB(f28 / 50.0F, 0.7F, 0.6F) & 0xFFFFFF) + (i16 << 24));
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glPopMatrix();
			}
		}

		byte b29 = 10;
		boolean z34 = false;
		if(this.mc.currentScreen instanceof GuiChat) {
			b29 = 20;
			z34 = true;
		}

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, (float)(i7 - 48), 0.0F);
		if(GuiScreen.currentID != "") {
			fontRenderer8.drawStringWithShadow("[Use numpad to enter, - to clear, + to give]", 2, 10, 16449390);
			fontRenderer8.drawStringWithShadow("Give item: " + GuiScreen.currentID, 2, 20, 16449390);

			try {
				i30 = Integer.parseInt(GuiScreen.currentID);
				if((Block.blocksList.length <= i30 || Block.blocksList[i30] == null) && (Item.itemsList.length <= i30 || Item.itemsList[i30] == null)) {
					fontRenderer8.drawStringWithShadow("(INVALID)", 2, 30, 16711680);
				}
			} catch (Exception exception24) {
				fontRenderer8.drawStringWithShadow("(ERROR)", 2, 30, 16711680);
			}
		}

		for(i30 = 0; i30 < this.chatMessageList.size() && i30 < b29; ++i30) {
			if(((ChatLine)this.chatMessageList.get(i30)).updateCounter < 200 || z34) {
				double d37 = (1.0D - (double)((ChatLine)this.chatMessageList.get(i30)).updateCounter / 200.0D) * 10.0D;
				if(d37 < 0.0D) {
					d37 = 0.0D;
				}

				if(d37 > 1.0D) {
					d37 = 1.0D;
				}

				i20 = (int)(255.0D * d37 * d37);
				if(z34) {
					i20 = 255;
				}

				if(i20 > 0) {
					int i22 = -i30 * 9;
					String string23 = ((ChatLine)this.chatMessageList.get(i30)).message;
					this.drawRect(2, i22 - 1, 322, i22 + 8, i20 / 2 << 24);
					GL11.glEnable(GL11.GL_BLEND);
					fontRenderer8.drawStringWithShadow(string23, 2, i22, 0xFFFFFF + (i20 << 24));
				}
			}
		}

		if(this.renderQAName) {
			GL11.glScalef(2.0F, 2.0F, 2.0F);
			fontRenderer8.drawStringWithShadow("QA BUILD: " + ScreenInputPass.name, 5, 0, 553320302);
		}

		GL11.glPopMatrix();
		if(this.mc.options.difficulty != 4) {
			GL11.glPushMatrix();
			GL11.glTranslatef((float)i6 / 2.0F - 16.0F, (float)i7 - 50.0F, -1.0F);
			GL11.glEnable(GL11.GL_BLEND);
			this.RenderHungerBar();
			GL11.glPopMatrix();
		}

		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void renderVignette(float brightness, int width, int height) {
		brightness = 1.0F - brightness;
		if(brightness < 0.0F) {
			brightness = 0.0F;
		}

		if(brightness > 1.0F) {
			brightness = 1.0F;
		}

		this.prevVignetteBrightness += (float)((double)(brightness - this.prevVignetteBrightness) * 0.01D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_ONE_MINUS_SRC_COLOR);
		GL11.glColor4f(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/misc/vignette.png"));
		Tessellator tessellator4 = Tessellator.instance;
		tessellator4.startDrawingQuads();
		tessellator4.addVertexWithUV(0.0D, (double)height, -90.0D, 0.0D, 1.0D);
		tessellator4.addVertexWithUV((double)width, (double)height, -90.0D, 1.0D, 1.0D);
		tessellator4.addVertexWithUV((double)width, 0.0D, -90.0D, 1.0D, 0.0D);
		tessellator4.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		tessellator4.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	private void renderInventorySlot(int i1, int i2, int i3, float renderPartialTick) {
		ItemStack itemStack5 = this.mc.thePlayer.inventory.mainInventory[i1];
		if(itemStack5 != null) {
			float f6 = (float)itemStack5.animationsToGo - renderPartialTick;
			if(f6 > 0.0F) {
				GL11.glPushMatrix();
				float f7 = 1.0F + f6 / 5.0F;
				GL11.glTranslatef((float)(i2 + 8), (float)(i3 + 12), 0.0F);
				GL11.glScalef(1.0F / f7, (f7 + 1.0F) / 2.0F, 1.0F);
				GL11.glTranslatef((float)(-(i2 + 8)), (float)(-(i3 + 12)), 0.0F);
			}

			itemRenderer.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, itemStack5, i2, i3);
			if(f6 > 0.0F) {
				GL11.glPopMatrix();
			}

			itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, itemStack5, i2, i3);
		}
	}

	public void updateTick() {
		if(this.recordPlayingUpFor > 0) {
			--this.recordPlayingUpFor;
		}

		++this.updateCounter;

		for(int i1 = 0; i1 < this.chatMessageList.size(); ++i1) {
			ChatLine chatLine2 = (ChatLine)this.chatMessageList.get(i1);
			++chatLine2.updateCounter;
		}

	}

	public void addChatMessage(String message) {
		while(this.mc.fontRenderer.getStringWidth(message) > 320) {
			int i2;
			for(i2 = 1; i2 < message.length() && this.mc.fontRenderer.getStringWidth(message.substring(0, i2 + 1)) <= 320; ++i2) {
			}

			this.addChatMessage(message.substring(0, i2));
			message = message.substring(i2);
		}

		this.chatMessageList.add(0, new ChatLine(message));

		while(this.chatMessageList.size() > 50) {
			this.chatMessageList.remove(this.chatMessageList.size() - 1);
		}

	}

	public void setRecordPlayingMessage(String record) {
		this.recordPlaying = "Now playing: " + record;
		this.recordPlayingUpFor = 60;
	}
}
