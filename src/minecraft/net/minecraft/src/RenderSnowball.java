package net.minecraft.src;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderSnowball extends Render {
	public void doRenderSnowball(EntitySnowball entitySnowball1, double d2, double d4, double d6, float f8, float f9) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d2, (float)d4, (float)d6);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		int i10 = Item.snowball.getIconIndex((ItemStack)null);
		this.loadTexture("/gui/items.png");
		Tessellator tessellator11 = Tessellator.instance;
		float f12 = (float)(i10 % 16 * 16 + 0) / 256.0F;
		float f13 = (float)(i10 % 16 * 16 + 16) / 256.0F;
		float f14 = (float)(i10 / 16 * 16 + 0) / 256.0F;
		float f15 = (float)(i10 / 16 * 16 + 16) / 256.0F;
		float f16 = 1.0F;
		float f17 = 0.5F;
		float f18 = 0.25F;
		GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		tessellator11.startDrawingQuads();
		tessellator11.setNormal(0.0F, 1.0F, 0.0F);
		tessellator11.addVertexWithUV((double)(0.0F - f17), (double)(0.0F - f18), 0.0D, (double)f12, (double)f15);
		tessellator11.addVertexWithUV((double)(f16 - f17), (double)(0.0F - f18), 0.0D, (double)f13, (double)f15);
		tessellator11.addVertexWithUV((double)(f16 - f17), (double)(1.0F - f18), 0.0D, (double)f13, (double)f14);
		tessellator11.addVertexWithUV((double)(0.0F - f17), (double)(1.0F - f18), 0.0D, (double)f12, (double)f14);
		tessellator11.draw();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	public void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		this.doRenderSnowball((EntitySnowball)entity1, d2, d4, d6, f8, f9);
	}
}
