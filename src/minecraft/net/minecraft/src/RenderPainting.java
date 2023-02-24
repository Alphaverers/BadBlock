package net.minecraft.src;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderPainting extends Render {
	private Random rand = new Random();

	public void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		this.a_lt((EntityPainting)entity1, d2, d4, d6, f8, f9);
	}

	public void a_lt(EntityPainting entityPainting1, double d2, double d4, double d6, float f8, float f9) {
		this.rand.setSeed(187L);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d2, (float)d4, (float)d6);
		GL11.glRotatef(f8, 0.0F, 1.0F, 0.0F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		SimpleDateFormat simpleDateFormat10 = new SimpleDateFormat("HH");
		int i11 = Integer.parseInt(simpleDateFormat10.format(Calendar.getInstance().getTime()));
		this.loadTexture(i11 <= 22 && i11 >= 5 ? "/art/kz.png" : "/art/zz.png");
		EnumArt enumArt12 = entityPainting1.art;
		GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
		this.setSizes(entityPainting1, enumArt12.sizeX, enumArt12.sizeY, enumArt12.offsetX, enumArt12.offsetY);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	private void setSizes(EntityPainting entityPainting, int i2, int i3, int i4, int i5) {
		float f6 = (float)(-i2) / 2.0F;
		float f7 = (float)(-i3) / 2.0F;

		for(int i10 = 0; i10 < i2 / 16; ++i10) {
			for(int i11 = 0; i11 < i3 / 16; ++i11) {
				float f12 = f6 + (float)((i10 + 1) * 16);
				float f13 = f6 + (float)(i10 * 16);
				float f14 = f7 + (float)((i11 + 1) * 16);
				float f15 = f7 + (float)(i11 * 16);
				this.getOffset(entityPainting, (f12 + f13) / 2.0F, (f14 + f15) / 2.0F);
				float f16 = (float)(i4 + i2 - i10 * 16) / 256.0F;
				float f17 = (float)(i4 + i2 - (i10 + 1) * 16) / 256.0F;
				float f18 = (float)(i5 + i3 - i11 * 16) / 256.0F;
				float f19 = (float)(i5 + i3 - (i11 + 1) * 16) / 256.0F;
				Tessellator tessellator32 = Tessellator.instance;
				tessellator32.startDrawingQuads();
				tessellator32.setNormal(0.0F, 0.0F, -1.0F);
				tessellator32.addVertexWithUV((double)f12, (double)f15, -0.5D, (double)f17, (double)f18);
				tessellator32.addVertexWithUV((double)f13, (double)f15, -0.5D, (double)f16, (double)f18);
				tessellator32.addVertexWithUV((double)f13, (double)f14, -0.5D, (double)f16, (double)f19);
				tessellator32.addVertexWithUV((double)f12, (double)f14, -0.5D, (double)f17, (double)f19);
				tessellator32.setNormal(0.0F, 0.0F, 1.0F);
				tessellator32.addVertexWithUV((double)f12, (double)f14, 0.5D, 0.75D, 0.0D);
				tessellator32.addVertexWithUV((double)f13, (double)f14, 0.5D, 0.8125D, 0.0D);
				tessellator32.addVertexWithUV((double)f13, (double)f15, 0.5D, 0.8125D, 0.0625D);
				tessellator32.addVertexWithUV((double)f12, (double)f15, 0.5D, 0.75D, 0.0625D);
				tessellator32.setNormal(0.0F, -1.0F, 0.0F);
				tessellator32.addVertexWithUV((double)f12, (double)f14, -0.5D, 0.75D, 0.001953125D);
				tessellator32.addVertexWithUV((double)f13, (double)f14, -0.5D, 0.8125D, 0.001953125D);
				tessellator32.addVertexWithUV((double)f13, (double)f14, 0.5D, 0.8125D, 0.001953125D);
				tessellator32.addVertexWithUV((double)f12, (double)f14, 0.5D, 0.75D, 0.001953125D);
				tessellator32.setNormal(0.0F, 1.0F, 0.0F);
				tessellator32.addVertexWithUV((double)f12, (double)f15, 0.5D, 0.75D, 0.001953125D);
				tessellator32.addVertexWithUV((double)f13, (double)f15, 0.5D, 0.8125D, 0.001953125D);
				tessellator32.addVertexWithUV((double)f13, (double)f15, -0.5D, 0.8125D, 0.001953125D);
				tessellator32.addVertexWithUV((double)f12, (double)f15, -0.5D, 0.75D, 0.001953125D);
				tessellator32.setNormal(-1.0F, 0.0F, 0.0F);
				tessellator32.addVertexWithUV((double)f12, (double)f14, 0.5D, 0.751953125D, 0.0D);
				tessellator32.addVertexWithUV((double)f12, (double)f15, 0.5D, 0.751953125D, 0.0625D);
				tessellator32.addVertexWithUV((double)f12, (double)f15, -0.5D, 0.751953125D, 0.0625D);
				tessellator32.addVertexWithUV((double)f12, (double)f14, -0.5D, 0.751953125D, 0.0D);
				tessellator32.setNormal(1.0F, 0.0F, 0.0F);
				tessellator32.addVertexWithUV((double)f13, (double)f14, -0.5D, 0.751953125D, 0.0D);
				tessellator32.addVertexWithUV((double)f13, (double)f15, -0.5D, 0.751953125D, 0.0625D);
				tessellator32.addVertexWithUV((double)f13, (double)f15, 0.5D, 0.751953125D, 0.0625D);
				tessellator32.addVertexWithUV((double)f13, (double)f14, 0.5D, 0.751953125D, 0.0D);
				tessellator32.draw();
			}
		}

	}

	private void getOffset(EntityPainting entityPainting, float f2, float f3) {
		int i4 = MathHelper.floor_double(entityPainting.posX);
		int i5 = MathHelper.floor_double(entityPainting.posY + (double)(f3 / 16.0F));
		int i6 = MathHelper.floor_double(entityPainting.posZ);
		if(entityPainting.direction == 0) {
			i4 = MathHelper.floor_double(entityPainting.posX + (double)(f2 / 16.0F));
		}

		if(entityPainting.direction == 1) {
			i6 = MathHelper.floor_double(entityPainting.posZ - (double)(f2 / 16.0F));
		}

		if(entityPainting.direction == 2) {
			i4 = MathHelper.floor_double(entityPainting.posX - (double)(f2 / 16.0F));
		}

		if(entityPainting.direction == 3) {
			i6 = MathHelper.floor_double(entityPainting.posZ + (double)(f2 / 16.0F));
		}

		float f7 = this.renderManager.worldObj.getBrightness(i4, i5, i6);
		GL11.glColor3f(f7, f7, f7);
	}
}
