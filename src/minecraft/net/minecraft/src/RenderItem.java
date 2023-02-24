package net.minecraft.src;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderItem extends Render {
	private RenderBlocks itemRenderBlocks = new RenderBlocks();
	private Random random = new Random();

	public RenderItem() {
		this.shadowSize = 0.15F;
		this.shadowOpaque = 0.75F;
	}

	public void doRenderItem(EntityItem entityItem1, double d2, double d4, double d6, float f8, float f9) {
		this.random.setSeed(187L);
		ItemStack itemStack10 = entityItem1.item;
		GL11.glPushMatrix();
		float f11 = MathHelper.sin(((float)entityItem1.age + f9) / 10.0F + entityItem1.hoverStart) * 0.1F + 0.1F;
		float f12 = (((float)entityItem1.age + f9) / 20.0F + entityItem1.hoverStart) * 57.295776F;
		byte b13 = 1;
		if(entityItem1.item.stackSize > 1) {
			b13 = 2;
		}

		if(entityItem1.item.stackSize > 5) {
			b13 = 3;
		}

		if(entityItem1.item.stackSize > 20) {
			b13 = 4;
		}

		GL11.glTranslatef((float)d2, (float)d4 + f11, (float)d6);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		float f16;
		float f17;
		float f18;
		if(itemStack10.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemStack10.itemID].getRenderType())) {
			GL11.glRotatef(f12, 0.0F, 1.0F, 0.0F);
			this.loadTexture("/terrain.png");
			float f27 = 0.25F;
			if(!Block.blocksList[itemStack10.itemID].renderAsNormalBlock() && itemStack10.itemID != Block.stairSingle.blockID) {
				f27 = 0.5F;
			}

			GL11.glScalef(f27, f27, f27);

			for(int i28 = 0; i28 < b13; ++i28) {
				GL11.glPushMatrix();
				if(i28 > 0) {
					f16 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f27;
					f17 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f27;
					f18 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f27;
					GL11.glTranslatef(f16, f17, f18);
				}

				this.itemRenderBlocks.renderBlockOnInventory(Block.blocksList[itemStack10.itemID]);
				GL11.glPopMatrix();
			}
		} else {
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			int i14 = itemStack10.getIconIndex();
			if(itemStack10.itemID < 256) {
				this.loadTexture("/terrain.png");
			} else {
				this.loadTexture("/gui/items.png");
			}

			Tessellator tessellator15 = Tessellator.instance;
			f16 = (float)(i14 % 16 * 16 + 0) / 256.0F;
			f17 = (float)(i14 % 16 * 16 + 16) / 256.0F;
			f18 = (float)(i14 / 16 * 16 + 0) / 256.0F;
			float f19 = (float)(i14 / 16 * 16 + 16) / 256.0F;
			float f20 = 1.0F;
			float f21 = 0.5F;
			float f22 = 0.25F;

			for(int i23 = 0; i23 < b13; ++i23) {
				GL11.glPushMatrix();
				if(i23 > 0) {
					float f24 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					float f25 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					float f26 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					GL11.glTranslatef(f24, f25, f26);
				}

				GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
				tessellator15.startDrawingQuads();
				tessellator15.setNormal(0.0F, 1.0F, 0.0F);
				tessellator15.addVertexWithUV((double)(0.0F - f21), (double)(0.0F - f22), 0.0D, (double)f16, (double)f19);
				tessellator15.addVertexWithUV((double)(f20 - f21), (double)(0.0F - f22), 0.0D, (double)f17, (double)f19);
				tessellator15.addVertexWithUV((double)(f20 - f21), (double)(1.0F - f22), 0.0D, (double)f17, (double)f18);
				tessellator15.addVertexWithUV((double)(0.0F - f21), (double)(1.0F - f22), 0.0D, (double)f16, (double)f18);
				tessellator15.draw();
				GL11.glPopMatrix();
			}
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	public void renderItemIntoGUI(FontRenderer fontRenderer1, RenderEngine renderEngine2, ItemStack itemStack3, int i4, int i5) {
		if(itemStack3 != null) {
			if(itemStack3.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemStack3.itemID].getRenderType())) {
				int i6 = itemStack3.itemID;
				renderEngine2.bindTexture(renderEngine2.getTexture("/terrain.png"));
				Block block7 = Block.blocksList[i6];
				GL11.glPushMatrix();
				GL11.glTranslatef((float)(i4 - 2), (float)(i5 + 3), 0.0F);
				GL11.glScalef(10.0F, 10.0F, 10.0F);
				GL11.glTranslatef(1.0F, 0.5F, 8.0F);
				GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glScalef(1.0F, 1.0F, 1.0F);
				this.itemRenderBlocks.renderBlockOnInventory(block7);
				GL11.glPopMatrix();
			} else if(itemStack3.getIconIndex() >= 0) {
				GL11.glDisable(GL11.GL_LIGHTING);
				if(itemStack3.itemID < 256) {
					renderEngine2.bindTexture(renderEngine2.getTexture("/terrain.png"));
				} else {
					renderEngine2.bindTexture(renderEngine2.getTexture("/gui/items.png"));
				}

				this.renderIcon(i4, i5, itemStack3.getIconIndex() % 16 * 16, itemStack3.getIconIndex() / 16 * 16, 16, 16);
				GL11.glEnable(GL11.GL_LIGHTING);
			}

			GL11.glEnable(GL11.GL_CULL_FACE);
		}
	}

	public void renderItemOverlayIntoGUI(FontRenderer fontRenderer1, RenderEngine renderEngine2, ItemStack itemStack3, int i4, int i5) {
		if(itemStack3 != null) {
			if(itemStack3.stackSize > 1) {
				String string6 = "" + itemStack3.stackSize;
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				fontRenderer1.drawStringWithShadow(string6, i4 + 19 - 2 - fontRenderer1.getStringWidth(string6), i5 + 6 + 3, 0xFFFFFF);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}

			if(itemStack3.itemDmg > 0) {
				int i11 = 13 - itemStack3.itemDmg * 13 / itemStack3.getMaxDamage();
				int i7 = 255 - itemStack3.itemDmg * 255 / itemStack3.getMaxDamage();
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				Tessellator tessellator8 = Tessellator.instance;
				int i9 = 255 - i7 << 16 | i7 << 8;
				int i10 = (255 - i7) / 4 << 16 | 16128;
				this.renderQuad(tessellator8, i4 + 2, i5 + 13, 13, 2, 0);
				this.renderQuad(tessellator8, i4 + 2, i5 + 13, 12, 1, i10);
				this.renderQuad(tessellator8, i4 + 2, i5 + 13, i11, 1, i9);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}

		}
	}

	private void renderQuad(Tessellator tessellator1, int i2, int i3, int i4, int i5, int i6) {
		tessellator1.startDrawingQuads();
		tessellator1.setColorOpaque_I(i6);
		tessellator1.addVertex((double)(i2 + 0), (double)(i3 + 0), 0.0D);
		tessellator1.addVertex((double)(i2 + 0), (double)(i3 + i5), 0.0D);
		tessellator1.addVertex((double)(i2 + i4), (double)(i3 + i5), 0.0D);
		tessellator1.addVertex((double)(i2 + i4), (double)(i3 + 0), 0.0D);
		tessellator1.draw();
	}

	public void renderIcon(int i1, int i2, int i3, int i4, int i5, int i6) {
		float f7 = 0.0F;
		float f8 = 0.00390625F;
		float f9 = 0.00390625F;
		Tessellator tessellator10 = Tessellator.instance;
		tessellator10.startDrawingQuads();
		tessellator10.addVertexWithUV((double)(i1 + 0), (double)(i2 + i6), (double)f7, (double)((float)(i3 + 0) * f8), (double)((float)(i4 + i6) * f9));
		tessellator10.addVertexWithUV((double)(i1 + i5), (double)(i2 + i6), (double)f7, (double)((float)(i3 + i5) * f8), (double)((float)(i4 + i6) * f9));
		tessellator10.addVertexWithUV((double)(i1 + i5), (double)(i2 + 0), (double)f7, (double)((float)(i3 + i5) * f8), (double)((float)(i4 + 0) * f9));
		tessellator10.addVertexWithUV((double)(i1 + 0), (double)(i2 + 0), (double)f7, (double)((float)(i3 + 0) * f8), (double)((float)(i4 + 0) * f9));
		tessellator10.draw();
	}

	public void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		this.doRenderItem((EntityItem)entity1, d2, d4, d6, f8, f9);
	}
}
