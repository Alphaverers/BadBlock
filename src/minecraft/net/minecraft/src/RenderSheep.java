package net.minecraft.src;

public class RenderSheep extends RenderLiving {
	public RenderSheep(ModelBase modelBase1, ModelBase modelBase2, float f3) {
		super(modelBase1, f3);
		this.setRenderPassModel(modelBase2);
	}

	protected boolean renderFur(EntitySheep entitySheep1, int i2) {
		this.loadTexture("/mob/sheep_fur.png");
		return i2 == 0 && !entitySheep1.sheared;
	}

	protected boolean shouldRenderPass(EntityLiving entityLiving1, int i2) {
		return this.renderFur((EntitySheep)entityLiving1, i2);
	}
}
