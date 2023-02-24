package net.minecraft.src;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderArrow extends Render {
	public void renderArrow(EntityArrow entityArrow1, double d2, double d4, double d6, float f8, float f9) {
		this.loadTexture("/item/arrows.png");
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d2, (float)d4, (float)d6);
		GL11.glRotatef(entityArrow1.prevRotationYaw + (entityArrow1.rotationYaw - entityArrow1.prevRotationYaw) * f9 - 90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(entityArrow1.prevRotationPitch + (entityArrow1.rotationPitch - entityArrow1.prevRotationPitch) * f9, 0.0F, 0.0F, 1.0F);
		Tessellator tessellator10 = Tessellator.instance;
		byte b11 = 0;
		float f12 = 0.0F;
		float f13 = 0.5F;
		float f14 = (float)(0 + b11 * 10) / 32.0F;
		float f15 = (float)(5 + b11 * 10) / 32.0F;
		float f16 = 0.0F;
		float f17 = 0.15625F;
		float f18 = (float)(5 + b11 * 10) / 32.0F;
		float f19 = (float)(10 + b11 * 10) / 32.0F;
		float f20 = 0.05625F;
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		float f21 = (float)entityArrow1.arrowShake - f9;
		if(f21 > 0.0F) {
			float f22 = -MathHelper.sin(f21 * 3.0F) * f21;
			GL11.glRotatef(f22, 0.0F, 0.0F, 1.0F);
		}

		GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(f20, f20, f20);
		GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
		GL11.glNormal3f(f20, 0.0F, 0.0F);
		tessellator10.startDrawingQuads();
		tessellator10.addVertexWithUV(-7.0D, -2.0D, -2.0D, (double)f16, (double)f18);
		tessellator10.addVertexWithUV(-7.0D, -2.0D, 2.0D, (double)f17, (double)f18);
		tessellator10.addVertexWithUV(-7.0D, 2.0D, 2.0D, (double)f17, (double)f19);
		tessellator10.addVertexWithUV(-7.0D, 2.0D, -2.0D, (double)f16, (double)f19);
		tessellator10.draw();
		GL11.glNormal3f(-f20, 0.0F, 0.0F);
		tessellator10.startDrawingQuads();
		tessellator10.addVertexWithUV(-7.0D, 2.0D, -2.0D, (double)f16, (double)f18);
		tessellator10.addVertexWithUV(-7.0D, 2.0D, 2.0D, (double)f17, (double)f18);
		tessellator10.addVertexWithUV(-7.0D, -2.0D, 2.0D, (double)f17, (double)f19);
		tessellator10.addVertexWithUV(-7.0D, -2.0D, -2.0D, (double)f16, (double)f19);
		tessellator10.draw();

		for(int i23 = 0; i23 < 4; ++i23) {
			GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			GL11.glNormal3f(0.0F, 0.0F, f20);
			tessellator10.startDrawingQuads();
			tessellator10.addVertexWithUV(-8.0D, -2.0D, 0.0D, (double)f12, (double)f14);
			tessellator10.addVertexWithUV(8.0D, -2.0D, 0.0D, (double)f13, (double)f14);
			tessellator10.addVertexWithUV(8.0D, 2.0D, 0.0D, (double)f13, (double)f15);
			tessellator10.addVertexWithUV(-8.0D, 2.0D, 0.0D, (double)f12, (double)f15);
			tessellator10.draw();
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	public void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		this.renderArrow((EntityArrow)entity1, d2, d4, d6, f8, f9);
	}
}
