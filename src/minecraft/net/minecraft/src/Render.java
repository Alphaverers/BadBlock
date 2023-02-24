package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public abstract class Render {
	protected RenderManager renderManager;
	private ModelBase modelBase = new ModelBiped();
	private RenderBlocks renderBlocks = new RenderBlocks();
	protected float shadowSize = 0.0F;
	protected float shadowOpaque = 1.0F;

	public abstract void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9);

	protected void loadTexture(String string1) {
		RenderEngine renderEngine2 = this.renderManager.renderEngine;
		renderEngine2.bindTexture(renderEngine2.getTexture(string1));
	}

	protected void loadDownloadableImageTexture(String string1, String string2) {
		RenderEngine renderEngine3 = this.renderManager.renderEngine;
		renderEngine3.bindTexture(renderEngine3.getTextureForDownloadableImage(string1, string2));
	}

	private void renderEntityOnFire(Entity entity1, double d2, double d4, double d6, float f8) {
		GL11.glDisable(GL11.GL_LIGHTING);
		int i9 = Block.fire.blockIndexInTexture;
		int i10 = (i9 & 15) << 4;
		int i11 = i9 & 240;
		float f12 = (float)i10 / 256.0F;
		float f13 = ((float)i10 + 15.99F) / 256.0F;
		float f14 = (float)i11 / 256.0F;
		float f15 = ((float)i11 + 15.99F) / 256.0F;
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d2, (float)d4, (float)d6);
		float f16 = entity1.width * 1.4F;
		GL11.glScalef(f16, f16, f16);
		this.loadTexture("/terrain.png");
		Tessellator tessellator17 = Tessellator.instance;
		float f18 = 1.0F;
		float f19 = 0.5F;
		float f20 = 0.0F;
		float f21 = entity1.height / entity1.width;
		GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(0.0F, 0.0F, 0.4F + (float)((int)f21) * 0.02F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		tessellator17.startDrawingQuads();

		while(f21 > 0.0F) {
			tessellator17.addVertexWithUV((double)(f18 - f19), (double)(0.0F - f20), 0.0D, (double)f13, (double)f15);
			tessellator17.addVertexWithUV((double)(0.0F - f19), (double)(0.0F - f20), 0.0D, (double)f12, (double)f15);
			tessellator17.addVertexWithUV((double)(0.0F - f19), (double)(1.4F - f20), 0.0D, (double)f12, (double)f14);
			tessellator17.addVertexWithUV((double)(f18 - f19), (double)(1.4F - f20), 0.0D, (double)f13, (double)f14);
			--f21;
			--f20;
			f18 *= 0.9F;
			GL11.glTranslatef(0.0F, 0.0F, -0.04F);
		}

		tessellator17.draw();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_LIGHTING);
	}

	private void renderShadow(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderEngine renderEngine10 = this.renderManager.renderEngine;
		renderEngine10.bindTexture(renderEngine10.getTexture("%%/shadow.png"));
		World world11 = this.getWorldFromRenderManager();
		GL11.glDepthMask(false);
		float f12 = this.shadowSize;
		double d13 = entity1.lastTickPosX + (entity1.posX - entity1.lastTickPosX) * (double)f9;
		double d15 = entity1.lastTickPosY + (entity1.posY - entity1.lastTickPosY) * (double)f9 + (double)entity1.getShadowSize();
		double d17 = entity1.lastTickPosZ + (entity1.posZ - entity1.lastTickPosZ) * (double)f9;
		int i19 = MathHelper.floor_double(d13 - (double)f12);
		int i20 = MathHelper.floor_double(d13 + (double)f12);
		int i21 = MathHelper.floor_double(d15 - (double)f12);
		int i22 = MathHelper.floor_double(d15);
		int i23 = MathHelper.floor_double(d17 - (double)f12);
		int i24 = MathHelper.floor_double(d17 + (double)f12);
		double d25 = d2 - d13;
		double d27 = d4 - d15;
		double d29 = d6 - d17;
		Tessellator tessellator31 = Tessellator.instance;
		tessellator31.startDrawingQuads();

		for(int i32 = i19; i32 <= i20; ++i32) {
			for(int i33 = i21; i33 <= i22; ++i33) {
				for(int i34 = i23; i34 <= i24; ++i34) {
					int i35 = world11.getBlockId(i32, i33 - 1, i34);
					if(i35 > 0 && world11.getBlockLightValue(i32, i33, i34) > 3) {
						this.renderShadowOnBlock(Block.blocksList[i35], d2, d4 + (double)entity1.getShadowSize(), d6, i32, i33, i34, f8, f12, d25, d27 + (double)entity1.getShadowSize(), d29);
					}
				}
			}
		}

		tessellator31.draw();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
	}

	private World getWorldFromRenderManager() {
		return this.renderManager.worldObj;
	}

	private void renderShadowOnBlock(Block block1, double d2, double d4, double d6, int i8, int i9, int i10, float f11, float f12, double d13, double d15, double d17) {
		Tessellator tessellator19 = Tessellator.instance;
		if(block1.renderAsNormalBlock()) {
			double d20 = ((double)f11 - (d4 - ((double)i9 + d15)) / 2.0D) * 0.5D * (double)this.getWorldFromRenderManager().getBrightness(i8, i9, i10);
			if(d20 >= 0.0D) {
				if(d20 > 1.0D) {
					d20 = 1.0D;
				}

				tessellator19.setColorRGBA_F(1.0F, 1.0F, 1.0F, (float)d20);
				double d22 = (double)i8 + block1.minX + d13;
				double d24 = (double)i8 + block1.maxX + d13;
				double d26 = (double)i9 + block1.minY + d15 + 0.015625D;
				double d28 = (double)i10 + block1.minZ + d17;
				double d30 = (double)i10 + block1.maxZ + d17;
				float f32 = (float)((d2 - d22) / 2.0D / (double)f12 + 0.5D);
				float f33 = (float)((d2 - d24) / 2.0D / (double)f12 + 0.5D);
				float f34 = (float)((d6 - d28) / 2.0D / (double)f12 + 0.5D);
				float f35 = (float)((d6 - d30) / 2.0D / (double)f12 + 0.5D);
				tessellator19.addVertexWithUV(d22, d26, d28, (double)f32, (double)f34);
				tessellator19.addVertexWithUV(d22, d26, d30, (double)f32, (double)f35);
				tessellator19.addVertexWithUV(d24, d26, d30, (double)f33, (double)f35);
				tessellator19.addVertexWithUV(d24, d26, d28, (double)f33, (double)f34);
			}
		}
	}

	public static void renderOffsetAABB(AxisAlignedBB axisAlignedBB0, double d1, double d3, double d5) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Tessellator tessellator7 = Tessellator.instance;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		tessellator7.startDrawingQuads();
		tessellator7.setTranslationD(d1, d3, d5);
		tessellator7.setNormal(0.0F, 0.0F, -1.0F);
		tessellator7.addVertex(axisAlignedBB0.minX, axisAlignedBB0.maxY, axisAlignedBB0.minZ);
		tessellator7.addVertex(axisAlignedBB0.maxX, axisAlignedBB0.maxY, axisAlignedBB0.minZ);
		tessellator7.addVertex(axisAlignedBB0.maxX, axisAlignedBB0.minY, axisAlignedBB0.minZ);
		tessellator7.addVertex(axisAlignedBB0.minX, axisAlignedBB0.minY, axisAlignedBB0.minZ);
		tessellator7.setNormal(0.0F, 0.0F, 1.0F);
		tessellator7.addVertex(axisAlignedBB0.minX, axisAlignedBB0.minY, axisAlignedBB0.maxZ);
		tessellator7.addVertex(axisAlignedBB0.maxX, axisAlignedBB0.minY, axisAlignedBB0.maxZ);
		tessellator7.addVertex(axisAlignedBB0.maxX, axisAlignedBB0.maxY, axisAlignedBB0.maxZ);
		tessellator7.addVertex(axisAlignedBB0.minX, axisAlignedBB0.maxY, axisAlignedBB0.maxZ);
		tessellator7.setNormal(0.0F, -1.0F, 0.0F);
		tessellator7.addVertex(axisAlignedBB0.minX, axisAlignedBB0.minY, axisAlignedBB0.minZ);
		tessellator7.addVertex(axisAlignedBB0.maxX, axisAlignedBB0.minY, axisAlignedBB0.minZ);
		tessellator7.addVertex(axisAlignedBB0.maxX, axisAlignedBB0.minY, axisAlignedBB0.maxZ);
		tessellator7.addVertex(axisAlignedBB0.minX, axisAlignedBB0.minY, axisAlignedBB0.maxZ);
		tessellator7.setNormal(0.0F, 1.0F, 0.0F);
		tessellator7.addVertex(axisAlignedBB0.minX, axisAlignedBB0.maxY, axisAlignedBB0.maxZ);
		tessellator7.addVertex(axisAlignedBB0.maxX, axisAlignedBB0.maxY, axisAlignedBB0.maxZ);
		tessellator7.addVertex(axisAlignedBB0.maxX, axisAlignedBB0.maxY, axisAlignedBB0.minZ);
		tessellator7.addVertex(axisAlignedBB0.minX, axisAlignedBB0.maxY, axisAlignedBB0.minZ);
		tessellator7.setNormal(-1.0F, 0.0F, 0.0F);
		tessellator7.addVertex(axisAlignedBB0.minX, axisAlignedBB0.minY, axisAlignedBB0.maxZ);
		tessellator7.addVertex(axisAlignedBB0.minX, axisAlignedBB0.maxY, axisAlignedBB0.maxZ);
		tessellator7.addVertex(axisAlignedBB0.minX, axisAlignedBB0.maxY, axisAlignedBB0.minZ);
		tessellator7.addVertex(axisAlignedBB0.minX, axisAlignedBB0.minY, axisAlignedBB0.minZ);
		tessellator7.setNormal(1.0F, 0.0F, 0.0F);
		tessellator7.addVertex(axisAlignedBB0.maxX, axisAlignedBB0.minY, axisAlignedBB0.minZ);
		tessellator7.addVertex(axisAlignedBB0.maxX, axisAlignedBB0.maxY, axisAlignedBB0.minZ);
		tessellator7.addVertex(axisAlignedBB0.maxX, axisAlignedBB0.maxY, axisAlignedBB0.maxZ);
		tessellator7.addVertex(axisAlignedBB0.maxX, axisAlignedBB0.minY, axisAlignedBB0.maxZ);
		tessellator7.setTranslationD(0.0D, 0.0D, 0.0D);
		tessellator7.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void renderAABB(AxisAlignedBB axisAlignedBB0) {
		Tessellator tessellator1 = Tessellator.instance;
		tessellator1.startDrawingQuads();
		tessellator1.addVertex(axisAlignedBB0.minX, axisAlignedBB0.maxY, axisAlignedBB0.minZ);
		tessellator1.addVertex(axisAlignedBB0.maxX, axisAlignedBB0.maxY, axisAlignedBB0.minZ);
		tessellator1.addVertex(axisAlignedBB0.maxX, axisAlignedBB0.minY, axisAlignedBB0.minZ);
		tessellator1.addVertex(axisAlignedBB0.minX, axisAlignedBB0.minY, axisAlignedBB0.minZ);
		tessellator1.addVertex(axisAlignedBB0.minX, axisAlignedBB0.minY, axisAlignedBB0.maxZ);
		tessellator1.addVertex(axisAlignedBB0.maxX, axisAlignedBB0.minY, axisAlignedBB0.maxZ);
		tessellator1.addVertex(axisAlignedBB0.maxX, axisAlignedBB0.maxY, axisAlignedBB0.maxZ);
		tessellator1.addVertex(axisAlignedBB0.minX, axisAlignedBB0.maxY, axisAlignedBB0.maxZ);
		tessellator1.addVertex(axisAlignedBB0.minX, axisAlignedBB0.minY, axisAlignedBB0.minZ);
		tessellator1.addVertex(axisAlignedBB0.maxX, axisAlignedBB0.minY, axisAlignedBB0.minZ);
		tessellator1.addVertex(axisAlignedBB0.maxX, axisAlignedBB0.minY, axisAlignedBB0.maxZ);
		tessellator1.addVertex(axisAlignedBB0.minX, axisAlignedBB0.minY, axisAlignedBB0.maxZ);
		tessellator1.addVertex(axisAlignedBB0.minX, axisAlignedBB0.maxY, axisAlignedBB0.maxZ);
		tessellator1.addVertex(axisAlignedBB0.maxX, axisAlignedBB0.maxY, axisAlignedBB0.maxZ);
		tessellator1.addVertex(axisAlignedBB0.maxX, axisAlignedBB0.maxY, axisAlignedBB0.minZ);
		tessellator1.addVertex(axisAlignedBB0.minX, axisAlignedBB0.maxY, axisAlignedBB0.minZ);
		tessellator1.addVertex(axisAlignedBB0.minX, axisAlignedBB0.minY, axisAlignedBB0.maxZ);
		tessellator1.addVertex(axisAlignedBB0.minX, axisAlignedBB0.maxY, axisAlignedBB0.maxZ);
		tessellator1.addVertex(axisAlignedBB0.minX, axisAlignedBB0.maxY, axisAlignedBB0.minZ);
		tessellator1.addVertex(axisAlignedBB0.minX, axisAlignedBB0.minY, axisAlignedBB0.minZ);
		tessellator1.addVertex(axisAlignedBB0.maxX, axisAlignedBB0.minY, axisAlignedBB0.minZ);
		tessellator1.addVertex(axisAlignedBB0.maxX, axisAlignedBB0.maxY, axisAlignedBB0.minZ);
		tessellator1.addVertex(axisAlignedBB0.maxX, axisAlignedBB0.maxY, axisAlignedBB0.maxZ);
		tessellator1.addVertex(axisAlignedBB0.maxX, axisAlignedBB0.minY, axisAlignedBB0.maxZ);
		tessellator1.draw();
	}

	public void setRenderManager(RenderManager renderManager1) {
		this.renderManager = renderManager1;
	}

	public void doRenderShadowAndFire(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		if(this.renderManager.options.fancyGraphics && this.shadowSize > 0.0F) {
			double d10 = this.renderManager.getDistanceToCamera(entity1.posX, entity1.posY, entity1.posZ);
			float f12 = (float)((1.0D - d10 / 256.0D) * (double)this.shadowOpaque);
			if(f12 > 0.0F) {
				this.renderShadow(entity1, d2, d4, d6, f12, f9);
			}
		}

		if(entity1.fire > 0) {
			this.renderEntityOnFire(entity1, d2, d4, d6, f9);
		}

	}

	public FontRenderer getFontRendererFromRenderManager() {
		return this.renderManager.getFontRenderer();
	}
}
