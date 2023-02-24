package net.minecraft.src;

public class ModelZombie extends ModelBiped {
	public void setRotationAngles(float f1, float f2, float f3, float f4, float f5, float f6) {
		super.setRotationAngles(f1, f2, f3, f4, f5, f6);
		float f7 = MathHelper.sin(this.swingProgress * (float)Math.PI);
		float f8 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float)Math.PI);
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleZ = 0.0F;
		this.bipedRightArm.rotateAngleY = -(0.1F - f7 * 0.6F);
		this.bipedLeftArm.rotateAngleY = 0.1F - f7 * 0.6F;
		this.bipedRightArm.rotateAngleX = -1.5707964F;
		this.bipedLeftArm.rotateAngleX = -1.5707964F;
		this.bipedRightArm.rotateAngleX -= f7 * 1.2F - f8 * 0.4F;
		this.bipedLeftArm.rotateAngleX -= f7 * 1.2F - f8 * 0.4F;
		this.bipedRightArm.rotateAngleZ += MathHelper.cos(f3 * 0.09F) * 0.05F + 0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(f3 * 0.09F) * 0.05F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(f3 * 0.067F) * 0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(f3 * 0.067F) * 0.05F;
	}
}
