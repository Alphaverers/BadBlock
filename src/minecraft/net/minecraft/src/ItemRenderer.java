package net.minecraft.src;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class ItemRenderer {
	private Minecraft mc;
	private ItemStack itemToRender = null;
	private float equippedProgress = 0.0F;
	private float prevEquippedProgress = 0.0F;
	private RenderBlocks renderBlocksInstance = new RenderBlocks();

	public ItemRenderer(Minecraft minecraft) {
		this.mc = minecraft;
	}

	public void renderItem(ItemStack itemStack) {
		GL11.glPushMatrix();
		if(itemStack.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemStack.itemID].getRenderType())) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
			this.renderBlocksInstance.renderBlockOnInventory(Block.blocksList[itemStack.itemID]);
		} else {
			if(itemStack.itemID < 256) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
			} else {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/items.png"));
			}

			Tessellator tessellator2 = Tessellator.instance;
			float f3 = (float)(itemStack.getIconIndex() % 16 * 16 + 0) / 256.0F;
			float f4 = (float)(itemStack.getIconIndex() % 16 * 16 + 16) / 256.0F;
			float f5 = (float)(itemStack.getIconIndex() / 16 * 16 + 0) / 256.0F;
			float f6 = (float)(itemStack.getIconIndex() / 16 * 16 + 16) / 256.0F;
			float f7 = 1.0F;
			float f8 = 0.0F;
			float f9 = 0.3F;
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glTranslatef(-f8, -f9, 0.0F);
			float f10 = 1.5F;
			GL11.glScalef(f10, f10, f10);
			GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
			float f11 = 0.0625F;
			tessellator2.startDrawingQuads();
			tessellator2.setNormal(0.0F, 0.0F, 1.0F);
			tessellator2.addVertexWithUV(0.0D, 0.0D, 0.0D, (double)f4, (double)f6);
			tessellator2.addVertexWithUV((double)f7, 0.0D, 0.0D, (double)f3, (double)f6);
			tessellator2.addVertexWithUV((double)f7, 1.0D, 0.0D, (double)f3, (double)f5);
			tessellator2.addVertexWithUV(0.0D, 1.0D, 0.0D, (double)f4, (double)f5);
			tessellator2.draw();
			tessellator2.startDrawingQuads();
			tessellator2.setNormal(0.0F, 0.0F, -1.0F);
			tessellator2.addVertexWithUV(0.0D, 1.0D, (double)(0.0F - f11), (double)f4, (double)f5);
			tessellator2.addVertexWithUV((double)f7, 1.0D, (double)(0.0F - f11), (double)f3, (double)f5);
			tessellator2.addVertexWithUV((double)f7, 0.0D, (double)(0.0F - f11), (double)f3, (double)f6);
			tessellator2.addVertexWithUV(0.0D, 0.0D, (double)(0.0F - f11), (double)f4, (double)f6);
			tessellator2.draw();
			tessellator2.startDrawingQuads();
			tessellator2.setNormal(-1.0F, 0.0F, 0.0F);

			int i12;
			float f13;
			float f14;
			float f15;
			for(i12 = 0; i12 < 16; ++i12) {
				f13 = (float)i12 / 16.0F;
				f14 = f4 + (f3 - f4) * f13 - 0.001953125F;
				f15 = f7 * f13;
				tessellator2.addVertexWithUV((double)f15, 0.0D, (double)(0.0F - f11), (double)f14, (double)f6);
				tessellator2.addVertexWithUV((double)f15, 0.0D, 0.0D, (double)f14, (double)f6);
				tessellator2.addVertexWithUV((double)f15, 1.0D, 0.0D, (double)f14, (double)f5);
				tessellator2.addVertexWithUV((double)f15, 1.0D, (double)(0.0F - f11), (double)f14, (double)f5);
			}

			tessellator2.draw();
			tessellator2.startDrawingQuads();
			tessellator2.setNormal(1.0F, 0.0F, 0.0F);

			for(i12 = 0; i12 < 16; ++i12) {
				f13 = (float)i12 / 16.0F;
				f14 = f4 + (f3 - f4) * f13 - 0.001953125F;
				f15 = f7 * f13 + 0.0625F;
				tessellator2.addVertexWithUV((double)f15, 1.0D, (double)(0.0F - f11), (double)f14, (double)f5);
				tessellator2.addVertexWithUV((double)f15, 1.0D, 0.0D, (double)f14, (double)f5);
				tessellator2.addVertexWithUV((double)f15, 0.0D, 0.0D, (double)f14, (double)f6);
				tessellator2.addVertexWithUV((double)f15, 0.0D, (double)(0.0F - f11), (double)f14, (double)f6);
			}

			tessellator2.draw();
			tessellator2.startDrawingQuads();
			tessellator2.setNormal(0.0F, 1.0F, 0.0F);

			for(i12 = 0; i12 < 16; ++i12) {
				f13 = (float)i12 / 16.0F;
				f14 = f6 + (f5 - f6) * f13 - 0.001953125F;
				f15 = f7 * f13 + 0.0625F;
				tessellator2.addVertexWithUV(0.0D, (double)f15, 0.0D, (double)f4, (double)f14);
				tessellator2.addVertexWithUV((double)f7, (double)f15, 0.0D, (double)f3, (double)f14);
				tessellator2.addVertexWithUV((double)f7, (double)f15, (double)(0.0F - f11), (double)f3, (double)f14);
				tessellator2.addVertexWithUV(0.0D, (double)f15, (double)(0.0F - f11), (double)f4, (double)f14);
			}

			tessellator2.draw();
			tessellator2.startDrawingQuads();
			tessellator2.setNormal(0.0F, -1.0F, 0.0F);

			for(i12 = 0; i12 < 16; ++i12) {
				f13 = (float)i12 / 16.0F;
				f14 = f6 + (f5 - f6) * f13 - 0.001953125F;
				f15 = f7 * f13;
				tessellator2.addVertexWithUV((double)f7, (double)f15, 0.0D, (double)f3, (double)f14);
				tessellator2.addVertexWithUV(0.0D, (double)f15, 0.0D, (double)f4, (double)f14);
				tessellator2.addVertexWithUV(0.0D, (double)f15, (double)(0.0F - f11), (double)f4, (double)f14);
				tessellator2.addVertexWithUV((double)f7, (double)f15, (double)(0.0F - f11), (double)f3, (double)f14);
			}

			tessellator2.draw();
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		}

		GL11.glPopMatrix();
	}

	public void renderItemInFirstPerson(float renderPartialTick) {
		float f2 = this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * renderPartialTick;
		EntityPlayerSP entityPlayerSP3 = this.mc.thePlayer;
		GL11.glPushMatrix();
		GL11.glRotatef(entityPlayerSP3.prevRotationPitch + (entityPlayerSP3.rotationPitch - entityPlayerSP3.prevRotationPitch) * renderPartialTick, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(entityPlayerSP3.prevRotationYaw + (entityPlayerSP3.rotationYaw - entityPlayerSP3.prevRotationYaw) * renderPartialTick, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		float f4 = this.mc.theWorld.getBrightness(MathHelper.floor_double(entityPlayerSP3.posX), MathHelper.floor_double(entityPlayerSP3.posY), MathHelper.floor_double(entityPlayerSP3.posZ));
		GL11.glColor4f(f4, f4, f4, 1.0F);
		float f5;
		float f6;
		float f7;
		float f8;
		if(this.itemToRender != null) {
			GL11.glPushMatrix();
			f5 = 0.8F;
			f6 = entityPlayerSP3.getSwingProgress(renderPartialTick);
			f7 = MathHelper.sin(f6 * (float)Math.PI);
			f8 = MathHelper.sin(MathHelper.sqrt_float(f6) * (float)Math.PI);
			GL11.glTranslatef(-f8 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f6) * (float)Math.PI * 2.0F) * 0.2F, -f7 * 0.2F);
			GL11.glTranslatef(0.7F * f5, -0.65F * f5 - (1.0F - f2) * 0.6F, -0.9F * f5);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			f6 = entityPlayerSP3.getSwingProgress(renderPartialTick);
			f7 = MathHelper.sin(f6 * f6 * (float)Math.PI);
			f8 = MathHelper.sin(MathHelper.sqrt_float(f6) * (float)Math.PI);
			GL11.glRotatef(-f7 * 20.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-f8 * 20.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-f8 * 80.0F, 1.0F, 0.0F, 0.0F);
			f6 = 0.4F;
			GL11.glScalef(f6, f6, f6);
			this.renderItem(this.itemToRender);
			GL11.glPopMatrix();
		} else {
			GL11.glPushMatrix();
			f5 = 0.8F;
			f6 = entityPlayerSP3.getSwingProgress(renderPartialTick);
			f7 = MathHelper.sin(f6 * (float)Math.PI);
			f8 = MathHelper.sin(MathHelper.sqrt_float(f6) * (float)Math.PI);
			GL11.glTranslatef(-f8 * 0.3F, MathHelper.sin(MathHelper.sqrt_float(f6) * (float)Math.PI * 2.0F) * 0.4F, -f7 * 0.4F);
			GL11.glTranslatef(0.8F * f5, -0.75F * f5 - (1.0F - f2) * 0.6F, -0.9F * f5);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			f6 = entityPlayerSP3.getSwingProgress(renderPartialTick);
			f7 = MathHelper.sin(f6 * f6 * (float)Math.PI);
			f8 = MathHelper.sin(MathHelper.sqrt_float(f6) * (float)Math.PI);
			GL11.glRotatef(f8 * 70.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-f7 * 20.0F, 0.0F, 0.0F, 1.0F);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTextureForDownloadableImage(this.mc.thePlayer.skinUrl, this.mc.thePlayer.getTexture()));
			GL11.glTranslatef(-1.0F, 3.6F, 3.5F);
			GL11.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(1.0F, 1.0F, 1.0F);
			GL11.glTranslatef(5.6F, 0.0F, 0.0F);
			Render render9 = RenderManager.instance.getEntityRenderObject(this.mc.thePlayer);
			RenderPlayer renderPlayer10 = (RenderPlayer)render9;
			f8 = 1.0F;
			GL11.glScalef(f8, f8, f8);
			renderPlayer10.drawFirstPersonHand();
			GL11.glPopMatrix();
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
	}

	public void renderOverlays(float renderPartialTick) {
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		int i2;
		if(this.mc.thePlayer.fire > 0) {
			i2 = this.mc.renderEngine.getTexture("/terrain.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, i2);
			this.renderFireInFirstPerson(renderPartialTick);
		}

		if(this.mc.thePlayer.isEntityInsideOpaqueBlock()) {
			i2 = MathHelper.floor_double(this.mc.thePlayer.posX);
			int i3 = MathHelper.floor_double(this.mc.thePlayer.posY);
			int i4 = MathHelper.floor_double(this.mc.thePlayer.posZ);
			int i5 = this.mc.renderEngine.getTexture("/terrain.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, i5);
			int i6 = this.mc.theWorld.getBlockId(i2, i3, i4);
			if(Block.blocksList[i6] != null) {
				this.renderInsideOfBlock(renderPartialTick, Block.blocksList[i6].getBlockTextureFromSide(2));
			}
		}

		if(this.mc.thePlayer.isInsideOfMaterial(Material.water)) {
			i2 = this.mc.renderEngine.getTexture("/water.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, i2);
			this.renderWarpedTextureOverlay(renderPartialTick);
		}

		GL11.glEnable(GL11.GL_ALPHA_TEST);
	}

	private void renderInsideOfBlock(float renderPartialTick, int id) {
		Tessellator tessellator3 = Tessellator.instance;
		this.mc.thePlayer.getBrightness(renderPartialTick);
		float f4 = 0.1F;
		GL11.glColor4f(f4, f4, f4, 0.5F);
		GL11.glPushMatrix();
		float f5 = -1.0F;
		float f6 = 1.0F;
		float f7 = -1.0F;
		float f8 = 1.0F;
		float f9 = -0.5F;
		float f10 = 0.0078125F;
		float f11 = (float)(id % 16) / 256.0F - f10;
		float f12 = ((float)(id % 16) + 15.99F) / 256.0F + f10;
		float f13 = (float)(id / 16) / 256.0F - f10;
		float f14 = ((float)(id / 16) + 15.99F) / 256.0F + f10;
		tessellator3.startDrawingQuads();
		tessellator3.addVertexWithUV((double)f5, (double)f7, (double)f9, (double)f12, (double)f14);
		tessellator3.addVertexWithUV((double)f6, (double)f7, (double)f9, (double)f11, (double)f14);
		tessellator3.addVertexWithUV((double)f6, (double)f8, (double)f9, (double)f11, (double)f13);
		tessellator3.addVertexWithUV((double)f5, (double)f8, (double)f9, (double)f12, (double)f13);
		tessellator3.draw();
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	private void renderWarpedTextureOverlay(float renderPartialTick) {
		Tessellator tessellator2 = Tessellator.instance;
		float f3 = this.mc.thePlayer.getBrightness(renderPartialTick);
		GL11.glColor4f(f3, f3, f3, 0.5F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glPushMatrix();
		float f4 = 4.0F;
		float f5 = -1.0F;
		float f6 = 1.0F;
		float f7 = -1.0F;
		float f8 = 1.0F;
		float f9 = -0.5F;
		float f10 = -this.mc.thePlayer.rotationYaw / 64.0F;
		float f11 = this.mc.thePlayer.rotationPitch / 64.0F;
		tessellator2.startDrawingQuads();
		tessellator2.addVertexWithUV((double)f5, (double)f7, (double)f9, (double)(f4 + f10), (double)(f4 + f11));
		tessellator2.addVertexWithUV((double)f6, (double)f7, (double)f9, (double)(0.0F + f10), (double)(f4 + f11));
		tessellator2.addVertexWithUV((double)f6, (double)f8, (double)f9, (double)(0.0F + f10), (double)(0.0F + f11));
		tessellator2.addVertexWithUV((double)f5, (double)f8, (double)f9, (double)(f4 + f10), (double)(0.0F + f11));
		tessellator2.draw();
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void renderFireInFirstPerson(float renderPartialTick) {
		Tessellator tessellator2 = Tessellator.instance;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.9F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		float f3 = 1.0F;

		for(int i4 = 0; i4 < 2; ++i4) {
			GL11.glPushMatrix();
			int i5 = Block.fire.blockIndexInTexture + i4 * 16;
			int i6 = (i5 & 15) << 4;
			int i7 = i5 & 240;
			float f8 = (float)i6 / 256.0F;
			float f9 = ((float)i6 + 15.99F) / 256.0F;
			float f10 = (float)i7 / 256.0F;
			float f11 = ((float)i7 + 15.99F) / 256.0F;
			float f12 = (0.0F - f3) / 2.0F;
			float f13 = f12 + f3;
			float f14 = 0.0F - f3 / 2.0F;
			float f15 = f14 + f3;
			float f16 = -0.5F;
			GL11.glTranslatef((float)(-(i4 * 2 - 1)) * 0.24F, -0.3F, 0.0F);
			GL11.glRotatef((float)(i4 * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
			tessellator2.startDrawingQuads();
			tessellator2.addVertexWithUV((double)f12, (double)f14, (double)f16, (double)f9, (double)f11);
			tessellator2.addVertexWithUV((double)f13, (double)f14, (double)f16, (double)f8, (double)f11);
			tessellator2.addVertexWithUV((double)f13, (double)f15, (double)f16, (double)f8, (double)f10);
			tessellator2.addVertexWithUV((double)f12, (double)f15, (double)f16, (double)f9, (double)f10);
			tessellator2.draw();
			GL11.glPopMatrix();
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void updateEquippedItem() {
		this.prevEquippedProgress = this.equippedProgress;
		EntityPlayerSP entityPlayerSP1 = this.mc.thePlayer;
		ItemStack itemStack2 = entityPlayerSP1.inventory.getCurrentItem();
		float f4 = 0.4F;
		float f5 = itemStack2 == this.itemToRender ? 1.0F : 0.0F;
		float f6 = f5 - this.equippedProgress;
		if(f6 < -f4) {
			f6 = -f4;
		}

		if(f6 > f4) {
			f6 = f4;
		}

		this.equippedProgress += f6;
		if(this.equippedProgress < 0.1F) {
			this.itemToRender = itemStack2;
		}

	}

	public void resetEquippedProgress() {
		this.equippedProgress = 0.0F;
	}

	public void resetEquippedProgress2() {
		this.equippedProgress = 0.0F;
	}
}
