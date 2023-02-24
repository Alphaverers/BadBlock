package net.minecraft.src;

public class ModelChicken extends ModelBase {
	public ModelRenderer head;
	public ModelRenderer body;
	public ModelRenderer rightLeg;
	public ModelRenderer leftLeg;
	public ModelRenderer rightWing;
	public ModelRenderer leftWing;
	public ModelRenderer bill;
	public ModelRenderer chin;

	public ModelChicken() {
		byte b1 = 16;
		this.head = new ModelRenderer(0, 0);
		this.head.addBox(-2.0F, -6.0F, -2.0F, 4, 6, 3, 0.0F);
		this.head.setRotationPoint(0.0F, (float)(-1 + b1), -4.0F);
		this.bill = new ModelRenderer(14, 0);
		this.bill.addBox(-2.0F, -4.0F, -4.0F, 4, 2, 2, 0.0F);
		this.bill.setRotationPoint(0.0F, (float)(-1 + b1), -4.0F);
		this.chin = new ModelRenderer(14, 4);
		this.chin.addBox(-1.0F, -2.0F, -3.0F, 2, 2, 2, 0.0F);
		this.chin.setRotationPoint(0.0F, (float)(-1 + b1), -4.0F);
		this.body = new ModelRenderer(0, 9);
		this.body.addBox(-3.0F, -4.0F, -3.0F, 6, 8, 6, 0.0F);
		this.body.setRotationPoint(0.0F, (float)(0 + b1), 0.0F);
		this.rightLeg = new ModelRenderer(26, 0);
		this.rightLeg.addBox(-1.0F, 0.0F, -3.0F, 3, 5, 3);
		this.rightLeg.setRotationPoint(-2.0F, (float)(3 + b1), 1.0F);
		this.leftLeg = new ModelRenderer(26, 0);
		this.leftLeg.addBox(-1.0F, 0.0F, -3.0F, 3, 5, 3);
		this.leftLeg.setRotationPoint(1.0F, (float)(3 + b1), 1.0F);
		this.rightWing = new ModelRenderer(24, 13);
		this.rightWing.addBox(0.0F, 0.0F, -3.0F, 1, 4, 6);
		this.rightWing.setRotationPoint(-4.0F, (float)(-3 + b1), 0.0F);
		this.leftWing = new ModelRenderer(24, 13);
		this.leftWing.addBox(-1.0F, 0.0F, -3.0F, 1, 4, 6);
		this.leftWing.setRotationPoint(4.0F, (float)(-3 + b1), 0.0F);
	}

	public void render(float f1, float f2, float f3, float f4, float f5, float f6) {
		this.setRotationAngles(f1, f2, f3, f4, f5, f6);
		this.head.render(f6);
		this.bill.render(f6);
		this.chin.render(f6);
		this.body.render(f6);
		this.rightLeg.render(f6);
		this.leftLeg.render(f6);
		this.rightWing.render(f6);
		this.leftWing.render(f6);
	}

	public void setRotationAngles(float f1, float f2, float f3, float f4, float f5, float f6) {
		this.head.rotateAngleX = -(f5 / 57.295776F);
		this.head.rotateAngleY = f4 / 57.295776F;
		this.bill.rotateAngleX = this.head.rotateAngleX;
		this.bill.rotateAngleY = this.head.rotateAngleY;
		this.chin.rotateAngleX = this.head.rotateAngleX;
		this.chin.rotateAngleY = this.head.rotateAngleY;
		this.body.rotateAngleX = (float)Math.PI / 2F;
		this.rightLeg.rotateAngleX = MathHelper.cos(f1 * 0.6662F) * 1.4F * f2;
		this.leftLeg.rotateAngleX = MathHelper.cos(f1 * 0.6662F + (float)Math.PI) * 1.4F * f2;
		this.rightWing.rotateAngleZ = f3;
		this.leftWing.rotateAngleZ = -f3;
	}
}
