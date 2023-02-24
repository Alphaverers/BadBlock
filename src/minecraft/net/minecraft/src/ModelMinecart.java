package net.minecraft.src;

public class ModelMinecart extends ModelBase {
	public ModelRenderer[] sideModels = new ModelRenderer[7];

	public ModelMinecart() {
		this.sideModels[0] = new ModelRenderer(0, 10);
		this.sideModels[1] = new ModelRenderer(0, 0);
		this.sideModels[2] = new ModelRenderer(0, 0);
		this.sideModels[3] = new ModelRenderer(0, 0);
		this.sideModels[4] = new ModelRenderer(0, 0);
		this.sideModels[5] = new ModelRenderer(44, 10);
		byte b1 = 20;
		byte b2 = 8;
		byte b3 = 16;
		byte b4 = 4;
		this.sideModels[0].addBox((float)(-b1 / 2), (float)(-b3 / 2), -1.0F, b1, b3, 2, 0.0F);
		this.sideModels[0].setRotationPoint(0.0F, (float)(0 + b4), 0.0F);
		this.sideModels[5].addBox((float)(-b1 / 2 + 1), (float)(-b3 / 2 + 1), -1.0F, b1 - 2, b3 - 2, 1, 0.0F);
		this.sideModels[5].setRotationPoint(0.0F, (float)(0 + b4), 0.0F);
		this.sideModels[1].addBox((float)(-b1 / 2 + 2), (float)(-b2 - 1), -1.0F, b1 - 4, b2, 2, 0.0F);
		this.sideModels[1].setRotationPoint((float)(-b1 / 2 + 1), (float)(0 + b4), 0.0F);
		this.sideModels[2].addBox((float)(-b1 / 2 + 2), (float)(-b2 - 1), -1.0F, b1 - 4, b2, 2, 0.0F);
		this.sideModels[2].setRotationPoint((float)(b1 / 2 - 1), (float)(0 + b4), 0.0F);
		this.sideModels[3].addBox((float)(-b1 / 2 + 2), (float)(-b2 - 1), -1.0F, b1 - 4, b2, 2, 0.0F);
		this.sideModels[3].setRotationPoint(0.0F, (float)(0 + b4), (float)(-b3 / 2 + 1));
		this.sideModels[4].addBox((float)(-b1 / 2 + 2), (float)(-b2 - 1), -1.0F, b1 - 4, b2, 2, 0.0F);
		this.sideModels[4].setRotationPoint(0.0F, (float)(0 + b4), (float)(b3 / 2 - 1));
		this.sideModels[0].rotateAngleX = (float)Math.PI / 2F;
		this.sideModels[1].rotateAngleY = 4.712389F;
		this.sideModels[2].rotateAngleY = (float)Math.PI / 2F;
		this.sideModels[3].rotateAngleY = (float)Math.PI;
		this.sideModels[5].rotateAngleX = -1.5707964F;
	}

	public void render(float f1, float f2, float f3, float f4, float f5, float f6) {
		this.sideModels[5].rotationPointY = 4.0F - f3;

		for(int i7 = 0; i7 < 6; ++i7) {
			this.sideModels[i7].render(f6);
		}

	}

	public void setRotationAngles(float f1, float f2, float f3, float f4, float f5, float f6) {
	}
}
