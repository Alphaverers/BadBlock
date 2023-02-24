package net.minecraft.src;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

public class ScreenItemCheat extends GuiScreen {
	public Minecraft b;
	private RenderBlocks blockRendererBlurryTroll = new RenderBlocks();
	private boolean createButtons = true;
	private int lastCols = -1;
	private int lastRows = -1;
	private int lastNOfPages = -1;
	private long rotateTimer = -1L;
	private boolean selectingBlocks = true;
	private boolean resetButtons = false;
	private int currentPage = 0;

	public ScreenItemCheat(Minecraft minecraft1) {
		this.b = minecraft1;
	}

	public void DottyIfYouDontKnowWhatsGoingOnHere_NeitherDoI(int i1, int i2, int i3, int i4, int i5, int i6) {
		Tessellator tessellator10 = Tessellator.instance;
		tessellator10.startDrawingQuads();
		tessellator10.addVertexWithUV((double)(i1 + 0), (double)(i2 + i6), 0.0D, (double)((float)(i3 + 0) * 0.00390625F), (double)((float)(i4 + i6) * 0.00390625F));
		tessellator10.addVertexWithUV((double)(i1 + i5), (double)(i2 + i6), 0.0D, (double)((float)(i3 + i5) * 0.00390625F), (double)((float)(i4 + i6) * 0.00390625F));
		tessellator10.addVertexWithUV((double)(i1 + i5), (double)(i2 + 0), 0.0D, (double)((float)(i3 + i5) * 0.00390625F), (double)((float)(i4 + 0) * 0.00390625F));
		tessellator10.addVertexWithUV((double)(i1 + 0), (double)(i2 + 0), 0.0D, (double)((float)(i3 + 0) * 0.00390625F), (double)((float)(i4 + 0) * 0.00390625F));
		tessellator10.setNormal(0.0F, 1.0F, 0.0F);
		tessellator10.draw();
	}

	public void RenderTheFunny(Block block1) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.b.renderEngine.getTexture("/terrain.png"));
		int i2 = block1.blockIndexInTexture;
		this.DottyIfYouDontKnowWhatsGoingOnHere_NeitherDoI(0, 0, i2 % 16 * 16, i2 / 16 * 16, 16, 16);
	}

	public void RenderTheItem(Item item1) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.b.renderEngine.getTexture("/gui/items.png"));
		int i2 = item1.iconIndex;
		this.DottyIfYouDontKnowWhatsGoingOnHere_NeitherDoI(0, 0, i2 % 16 * 16, i2 / 16 * 16, 16, 16);
	}

	public int nthExistingBlock(int i1) {
		int i2 = 0;

		for(int i3 = 1; i3 != 256; ++i3) {
			if(Block.blocksList[i3] != null && i3 != 120) {
				++i2;
			}

			if(i2 == i1 + 1) {
				return i3;
			}
		}

		return -1;
	}

	public int nOfExistingBlocks() {
		int i1 = 0;

		for(int i2 = 1; i2 != 256; ++i2) {
			if(Block.blocksList[i2] != null && i2 != 120) {
				++i1;
			}
		}

		return i1;
	}

	public int nthExistingItem(int i1) {
		int i2 = 0;

		for(int i3 = 256; i3 != Item.itemsList.length; ++i3) {
			if(Item.itemsList[i3] != null) {
				++i2;
			}

			if(i2 == i1 + 1) {
				return i3;
			}
		}

		return -1;
	}

	public int nOfExistingItems() {
		int i1 = 0;

		for(int i2 = 256; i2 != Item.itemsList.length; ++i2) {
			if(Item.itemsList[i2] != null) {
				++i1;
			}
		}

		return i1;
	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	protected void actionPerformed(GuiButton guiButton1) {
		if(guiButton1.enabled) {
			if(guiButton1.id == 2) {
				this.resetButtons = true;
				this.selectingBlocks = true;
			} else if(guiButton1.id == 3) {
				this.resetButtons = true;
				this.selectingBlocks = false;
			} else if(guiButton1.id == 4) {
				if(this.currentPage == 0) {
					this.currentPage = this.lastNOfPages - 1;
				} else {
					--this.currentPage;
				}

				this.resetButtons = true;
			} else if(guiButton1.id == 5) {
				++this.currentPage;
				this.currentPage %= this.lastNOfPages;
				this.resetButtons = true;
			}

			if(guiButton1.id >= 4096) {
				this.b.thePlayer.dropPlayerItemWithRandomChoice(new ItemStack(Item.itemsList[guiButton1.id - 4096], 1), true);
			} else if(guiButton1.id >= 2048) {
				this.b.thePlayer.dropPlayerItemWithRandomChoice(new ItemStack(Block.blocksList[guiButton1.id - 2048], 64), true);
			}

		}
	}

	public void drawScreen(int i1, int i2, float f3) {
		this.drawDefaultBackground();
		if(this.rotateTimer == -1L) {
			this.rotateTimer = System.currentTimeMillis();
		}

		float f4 = Math.min((float)(System.currentTimeMillis() - this.rotateTimer) / 100.0F, 1.0F);
		this.drawGradientRect(0, (int)((float)this.height * (1.0F - f4)), this.width, this.height, 1614823488, 1612718112);
		this.drawCenteredString(this.fontRenderer, "Palette", this.width / 2, 15, 0xFFFFFF);
		this.drawCenteredString(this.fontRenderer, "" + this.currentPage, 208, 35, 0xFFFFFF);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.b.renderEngine.getTexture("/terrain.png"));
		int i5 = (this.width - 18) / 32;
		int i6 = (this.height - 50) / 23;
		if(i5 != 0 && i6 != 0) {
			int i7 = (this.selectingBlocks ? this.nOfExistingBlocks() : this.nOfExistingItems()) / (i5 * i6) + 1;
			if(this.lastCols != i5 || this.lastRows != i6 || this.resetButtons) {
				this.lastNOfPages = i7;
				this.lastCols = i5;

				for(this.lastRows = i6; i7 <= this.currentPage; --this.currentPage) {
				}

				this.controlList.clear();
				this.createButtons = true;
				this.resetButtons = false;
			}

			if(this.createButtons) {
				this.controlList.add(new SelButton(2, 18, 30, "Blocks"));
				this.controlList.add(new SelButton(3, 68, 30, "Items"));
				this.controlList.add(new SelButton(4, 148, 30, "<<"));
				this.controlList.add(new SelButton(5, 218, 30, ">>"));
				((GuiButton)this.controlList.get(this.selectingBlocks ? 0 : 1)).enabled = false;
				((GuiButton)this.controlList.get(2)).enabled = ((GuiButton)this.controlList.get(3)).enabled = i7 != 1;
			}

			int i8 = this.currentPage * i5 * i6;

			for(int i9 = 0; i9 != i5 * i6; ++i9) {
				int i10;
				float f13;
				if(this.selectingBlocks) {
					i10 = this.nthExistingBlock(i8 + i9);
					if(i10 == -1) {
						break;
					}

					Block block11 = Block.blocksList[i10];
					boolean z12 = RenderBlocks.renderItemIn3d(block11.getRenderType()) || block11.getRenderType() == 1 || block11.getRenderType() == 2;
					f13 = 18.0F + 32.0F * (float)(i9 % i5);
					float f14 = 50.0F + 23.0F * (float)(i9 / i5);
					if(this.createButtons) {
						this.controlList.add(new BlockButton(2048 + i10, (int)f13, (int)f14, "", i10, this));
					}

					GL11.glPushMatrix();
					if(z12) {
						GL11.glTranslatef(f13 + 14.0F, f14 + 8.0F, 16.0F);
						GL11.glScalef(16.0F, 16.0F, 16.0F);
						GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
						GL11.glRotatef(30.0F, -1.0F, -1.0F, 0.0F);
						float f15 = (float)(System.currentTimeMillis() - this.rotateTimer) / 13000.0F * 360.0F;
						GL11.glRotatef(f15, 0.0F, -1.0F, 0.0F);
						this.blockRendererBlurryTroll.renderBlockOnInventory(block11);
					} else {
						GL11.glTranslatef(f13 + 5.0F, f14, 13.0F);
						this.RenderTheFunny(block11);
					}

					GL11.glPopMatrix();
				} else {
					i10 = this.nthExistingItem(i8 + i9);
					if(i10 == -1) {
						break;
					}

					Item item16 = Item.itemsList[i10];
					float f17 = 18.0F + 32.0F * (float)(i9 % i5);
					f13 = 50.0F + 23.0F * (float)(i9 / i5);
					if(this.createButtons) {
						this.controlList.add(new BlockButton(4096 + i10, (int)f17, (int)f13, "", i10, this));
					}

					GL11.glPushMatrix();
					GL11.glTranslatef(f17 + 5.0F, f13, 13.0F);
					this.RenderTheItem(item16);
					GL11.glPopMatrix();
				}
			}

			this.createButtons = false;
			super.drawScreen(i1, i2, f3);
		}
	}
}
