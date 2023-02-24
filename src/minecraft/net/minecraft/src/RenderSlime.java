package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderSlime extends RenderLiving {
	private ModelBase modelSlime;

	public RenderSlime(ModelBase modelBase1, ModelBase modelBase2, float f3) {
		super(modelBase1, f3);
		this.modelSlime = modelBase2;
	}

	protected boolean renderSlimePassModel(EntitySlime entitySlime1, int i2) {
		if(i2 == 0) {
			this.setRenderPassModel(this.modelSlime);
			GL11.glEnable(GL11.GL_NORMALIZE);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			return true;
		} else {
			if(i2 == 1) {
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}

			return false;
		}
	}

	protected void squishSlime(EntitySlime entitySlime1, float f2) {
		float f3 = (entitySlime1.prevSquishFactor + (entitySlime1.squishFactor - entitySlime1.prevSquishFactor) * f2) / ((float)entitySlime1.size * 0.5F + 1.0F);
		float f4 = 1.0F / (f3 + 1.0F);
		float f5 = (float)entitySlime1.size;
		GL11.glScalef(f4 * f5, 1.0F / f4 * f5, f4 * f5);
	}

	protected void preRenderCallback(EntityLiving entityLiving1, float f2) {
		this.squishSlime((EntitySlime)entityLiving1, f2);
	}

	protected boolean shouldRenderPass(EntityLiving entityLiving1, int i2) {
		return this.renderSlimePassModel((EntitySlime)entityLiving1, i2);
	}
}
