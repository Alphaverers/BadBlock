package net.minecraft.src;

public class ModelCow extends ModelQuadraped {
	ModelRenderer udders;
	ModelRenderer horn1;
	ModelRenderer horn2;

	public ModelCow() {
		super(12, 0.0F);
		this.head = new ModelRenderer(0, 0);
		this.head.addBox(-4.0F, -4.0F, -6.0F, 8, 8, 6, 0.0F);
		this.head.setRotationPoint(0.0F, 4.0F, -8.0F);
		this.horn1 = new ModelRenderer(22, 0);
		this.horn1.addBox(-5.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
		this.horn1.setRotationPoint(0.0F, 3.0F, -7.0F);
		this.horn2 = new ModelRenderer(22, 0);
		this.horn2.addBox(4.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
		this.horn2.setRotationPoint(0.0F, 3.0F, -7.0F);
		this.udders = new ModelRenderer(52, 0);
		this.udders.addBox(-2.0F, -3.0F, 0.0F, 4, 6, 2, 0.0F);
		this.udders.setRotationPoint(0.0F, 14.0F, 6.0F);
		this.udders.rotateAngleX = (float)Math.PI / 2F;
		this.body = new ModelRenderer(18, 4);
		this.body.addBox(-6.0F, -10.0F, -7.0F, 12, 18, 10, 0.0F);
		this.body.setRotationPoint(0.0F, 5.0F, 2.0F);
		--this.leg1.rotationPointX;
		++this.leg2.rotationPointX;
		this.leg1.rotationPointZ += 0.0F;
		this.leg2.rotationPointZ += 0.0F;
		--this.leg3.rotationPointX;
		++this.leg4.rotationPointX;
		--this.leg3.rotationPointZ;
		--this.leg4.rotationPointZ;
	}

	public void render(float f1, float f2, float f3, float f4, float f5, float f6) {
		super.render(f1, f2, f3, f4, f5, f6);
		this.horn1.render(f6);
		this.horn2.render(f6);
		this.udders.render(f6);
	}

	public void setRotationAngles(float f1, float f2, float f3, float f4, float f5, float f6) {
		super.setRotationAngles(f1, f2, f3, f4, f5, f6);
		this.horn1.rotateAngleY = this.head.rotateAngleY;
		this.horn1.rotateAngleX = this.head.rotateAngleX;
		this.horn2.rotateAngleY = this.head.rotateAngleY;
		this.horn2.rotateAngleX = this.head.rotateAngleX;
	}
}
