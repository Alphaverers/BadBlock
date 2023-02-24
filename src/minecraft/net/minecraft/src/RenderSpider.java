package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderSpider extends RenderLiving {
	public RenderSpider() {
		super(new ModelSpider(), 1.0F);
		this.setRenderPassModel(new ModelSpider());
	}

	protected float setSpiderDeathMaxRotation(EntitySpider entitySpider1) {
		return 180.0F;
	}

	protected boolean setSpiderEyeBrightness(EntitySpider entitySpider1, int i2) {
		if(i2 != 0) {
			return false;
		} else if(i2 != 0) {
			return false;
		} else {
			this.loadTexture("/mob/spider_eyes.png");
			float f3 = (1.0F - entitySpider1.getBrightness(1.0F)) * 0.5F;
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, f3);
			return true;
		}
	}

	protected float getDeathMaxRotation(EntityLiving entityLiving1) {
		return this.setSpiderDeathMaxRotation((EntitySpider)entityLiving1);
	}

	protected boolean shouldRenderPass(EntityLiving entityLiving1, int i2) {
		return this.setSpiderEyeBrightness((EntitySpider)entityLiving1, i2);
	}
}
