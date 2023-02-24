package net.minecraft.src;

public class ModelSpider extends ModelBase {
	public ModelRenderer spiderHead;
	public ModelRenderer spiderNeck;
	public ModelRenderer spiderBody;
	public ModelRenderer spiderLeg1;
	public ModelRenderer spiderLeg2;
	public ModelRenderer spiderLeg3;
	public ModelRenderer spiderLeg4;
	public ModelRenderer spiderLeg5;
	public ModelRenderer spiderLeg6;
	public ModelRenderer spiderLeg7;
	public ModelRenderer spiderLeg8;

	public ModelSpider() {
		float f1 = 0.0F;
		byte b2 = 15;
		this.spiderHead = new ModelRenderer(32, 4);
		this.spiderHead.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, f1);
		this.spiderHead.setRotationPoint(0.0F, (float)(0 + b2), -3.0F);
		this.spiderNeck = new ModelRenderer(0, 0);
		this.spiderNeck.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, f1);
		this.spiderNeck.setRotationPoint(0.0F, (float)b2, 0.0F);
		this.spiderBody = new ModelRenderer(0, 12);
		this.spiderBody.addBox(-5.0F, -4.0F, -6.0F, 10, 8, 12, f1);
		this.spiderBody.setRotationPoint(0.0F, (float)(0 + b2), 9.0F);
		this.spiderLeg1 = new ModelRenderer(18, 0);
		this.spiderLeg1.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f1);
		this.spiderLeg1.setRotationPoint(-4.0F, (float)(0 + b2), 2.0F);
		this.spiderLeg2 = new ModelRenderer(18, 0);
		this.spiderLeg2.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f1);
		this.spiderLeg2.setRotationPoint(4.0F, (float)(0 + b2), 2.0F);
		this.spiderLeg3 = new ModelRenderer(18, 0);
		this.spiderLeg3.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f1);
		this.spiderLeg3.setRotationPoint(-4.0F, (float)(0 + b2), 1.0F);
		this.spiderLeg4 = new ModelRenderer(18, 0);
		this.spiderLeg4.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f1);
		this.spiderLeg4.setRotationPoint(4.0F, (float)(0 + b2), 1.0F);
		this.spiderLeg5 = new ModelRenderer(18, 0);
		this.spiderLeg5.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f1);
		this.spiderLeg5.setRotationPoint(-4.0F, (float)(0 + b2), 0.0F);
		this.spiderLeg6 = new ModelRenderer(18, 0);
		this.spiderLeg6.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f1);
		this.spiderLeg6.setRotationPoint(4.0F, (float)(0 + b2), 0.0F);
		this.spiderLeg7 = new ModelRenderer(18, 0);
		this.spiderLeg7.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f1);
		this.spiderLeg7.setRotationPoint(-4.0F, (float)(0 + b2), -1.0F);
		this.spiderLeg8 = new ModelRenderer(18, 0);
		this.spiderLeg8.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f1);
		this.spiderLeg8.setRotationPoint(4.0F, (float)(0 + b2), -1.0F);
	}

	public void render(float f1, float f2, float f3, float f4, float f5, float f6) {
		this.setRotationAngles(f1, f2, f3, f4, f5, f6);
		this.spiderHead.render(f6);
		this.spiderNeck.render(f6);
		this.spiderBody.render(f6);
		this.spiderLeg1.render(f6);
		this.spiderLeg2.render(f6);
		this.spiderLeg3.render(f6);
		this.spiderLeg4.render(f6);
		this.spiderLeg5.render(f6);
		this.spiderLeg6.render(f6);
		this.spiderLeg7.render(f6);
		this.spiderLeg8.render(f6);
	}

	public void setRotationAngles(float f1, float f2, float f3, float f4, float f5, float f6) {
		this.spiderHead.rotateAngleY = f4 / 57.295776F;
		this.spiderHead.rotateAngleX = f5 / 57.295776F;
		float f7 = 0.7853982F;
		this.spiderLeg1.rotateAngleZ = -f7;
		this.spiderLeg2.rotateAngleZ = f7;
		this.spiderLeg3.rotateAngleZ = -f7 * 0.74F;
		this.spiderLeg4.rotateAngleZ = f7 * 0.74F;
		this.spiderLeg5.rotateAngleZ = -f7 * 0.74F;
		this.spiderLeg6.rotateAngleZ = f7 * 0.74F;
		this.spiderLeg7.rotateAngleZ = -f7;
		this.spiderLeg8.rotateAngleZ = f7;
		float f8 = -0.0F;
		float f9 = 0.3926991F;
		this.spiderLeg1.rotateAngleY = f9 * 2.0F + f8;
		this.spiderLeg2.rotateAngleY = -f9 * 2.0F - f8;
		this.spiderLeg3.rotateAngleY = f9 * 1.0F + f8;
		this.spiderLeg4.rotateAngleY = -f9 * 1.0F - f8;
		this.spiderLeg5.rotateAngleY = -f9 * 1.0F + f8;
		this.spiderLeg6.rotateAngleY = f9 * 1.0F - f8;
		this.spiderLeg7.rotateAngleY = -f9 * 2.0F + f8;
		this.spiderLeg8.rotateAngleY = f9 * 2.0F - f8;
		float f10 = -(MathHelper.cos(f1 * 0.6662F * 2.0F + 0.0F) * 0.4F) * f2;
		float f11 = -(MathHelper.cos(f1 * 0.6662F * 2.0F + (float)Math.PI) * 0.4F) * f2;
		float f12 = -(MathHelper.cos(f1 * 0.6662F * 2.0F + (float)Math.PI / 2F) * 0.4F) * f2;
		float f13 = -(MathHelper.cos(f1 * 0.6662F * 2.0F + 4.712389F) * 0.4F) * f2;
		float f14 = Math.abs(MathHelper.sin(f1 * 0.6662F + 0.0F) * 0.4F) * f2;
		float f15 = Math.abs(MathHelper.sin(f1 * 0.6662F + (float)Math.PI) * 0.4F) * f2;
		float f16 = Math.abs(MathHelper.sin(f1 * 0.6662F + (float)Math.PI / 2F) * 0.4F) * f2;
		float f17 = Math.abs(MathHelper.sin(f1 * 0.6662F + 4.712389F) * 0.4F) * f2;
		this.spiderLeg1.rotateAngleY += f10;
		this.spiderLeg2.rotateAngleY += -f10;
		this.spiderLeg3.rotateAngleY += f11;
		this.spiderLeg4.rotateAngleY += -f11;
		this.spiderLeg5.rotateAngleY += f12;
		this.spiderLeg6.rotateAngleY += -f12;
		this.spiderLeg7.rotateAngleY += f13;
		this.spiderLeg8.rotateAngleY += -f13;
		this.spiderLeg1.rotateAngleZ += f14;
		this.spiderLeg2.rotateAngleZ += -f14;
		this.spiderLeg3.rotateAngleZ += f15;
		this.spiderLeg4.rotateAngleZ += -f15;
		this.spiderLeg5.rotateAngleZ += f16;
		this.spiderLeg6.rotateAngleZ += -f16;
		this.spiderLeg7.rotateAngleZ += f17;
		this.spiderLeg8.rotateAngleZ += -f17;
	}
}
