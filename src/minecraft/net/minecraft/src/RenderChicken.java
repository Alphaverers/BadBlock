package net.minecraft.src;

public class RenderChicken extends RenderLiving {
	public RenderChicken(ModelBase modelBase1, float f2) {
		super(modelBase1, f2);
	}

	public void renderChicken(EntityChicken entityChicken1, double d2, double d4, double d6, float f8, float f9) {
		super.doRenderLiving(entityChicken1, d2, d4, d6, f8, f9);
	}

	protected float getWingRotation(EntityChicken entityChicken1, float f2) {
		float f3 = entityChicken1.prevWingRotation + (entityChicken1.wingRotation - entityChicken1.prevWingRotation) * f2;
		float f4 = entityChicken1.prevDestPos + (entityChicken1.destPos - entityChicken1.prevDestPos) * f2;
		return (MathHelper.sin(f3) + 1.0F) * f4;
	}

	protected float handleRotationFloat(EntityLiving entityLiving1, float f2) {
		return this.getWingRotation((EntityChicken)entityLiving1, f2);
	}

	public void doRenderLiving(EntityLiving entityLiving1, double d2, double d4, double d6, float f8, float f9) {
		this.renderChicken((EntityChicken)entityLiving1, d2, d4, d6, f8, f9);
	}

	public void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		this.renderChicken((EntityChicken)entity1, d2, d4, d6, f8, f9);
	}
}
