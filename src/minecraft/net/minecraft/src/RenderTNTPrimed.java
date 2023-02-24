package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderTNTPrimed extends Render {
	private RenderBlocks blockRenderer = new RenderBlocks();

	public RenderTNTPrimed() {
		this.shadowSize = 0.5F;
	}

	public void doRenderTNT(EntityTNTPrimed entityTNTPrimed1, double d2, double d4, double d6, float f8, float f9) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d2, (float)d4, (float)d6);
		float f10;
		if((float)entityTNTPrimed1.fuse - f9 + 1.0F < 10.0F) {
			f10 = 1.0F - ((float)entityTNTPrimed1.fuse - f9 + 1.0F) / 10.0F;
			if(f10 < 0.0F) {
				f10 = 0.0F;
			}

			if(f10 > 1.0F) {
				f10 = 1.0F;
			}

			f10 *= f10;
			f10 *= f10;
			float f11 = 1.0F + f10 * 0.3F;
			GL11.glScalef(f11, f11, f11);
		}

		f10 = (1.0F - ((float)entityTNTPrimed1.fuse - f9 + 1.0F) / 100.0F) * 0.8F;
		this.loadTexture("/terrain.png");
		this.blockRenderer.renderBlockOnInventory(Block.tnt);
		if(entityTNTPrimed1.fuse / 5 % 2 == 0) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, f10);
			this.blockRenderer.renderBlockOnInventory(Block.tnt);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}

		GL11.glPopMatrix();
	}

	public void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		this.doRenderTNT((EntityTNTPrimed)entity1, d2, d4, d6, f8, f9);
	}
}
