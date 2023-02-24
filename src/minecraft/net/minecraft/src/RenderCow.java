package net.minecraft.src;

public class RenderCow extends RenderLiving {
	public RenderCow(ModelBase modelBase1, float f2) {
		super(modelBase1, f2);
	}

	public void renderCow(EntityCow entityCow1, double d2, double d4, double d6, float f8, float f9) {
		super.doRenderLiving(entityCow1, d2, d4, d6, f8, f9);
	}

	public void doRenderLiving(EntityLiving entityLiving1, double d2, double d4, double d6, float f8, float f9) {
		this.renderCow((EntityCow)entityLiving1, d2, d4, d6, f8, f9);
	}

	public void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		this.renderCow((EntityCow)entity1, d2, d4, d6, f8, f9);
	}
}
