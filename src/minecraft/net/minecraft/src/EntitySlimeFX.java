package net.minecraft.src;

public class EntitySlimeFX extends EntityFX {
	public EntitySlimeFX(World world1, double d2, double d4, double d6, Item item8) {
		super(world1, d2, d4, d6, 0.0D, 0.0D, 0.0D);
		this.particleTextureIndex = item8.getIconIndex((ItemStack)null);
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.particleGravity = Block.blockSnow.blockParticleGravity;
		this.particleScale /= 2.0F;
	}

	public int getFXLayer() {
		return 2;
	}

	public void renderParticle(Tessellator tessellator1, float f2, float f3, float f4, float f5, float f6, float f7) {
		float f8 = ((float)(this.particleTextureIndex % 16) + this.particleTextureJitterX / 4.0F) / 16.0F;
		float f9 = f8 + 0.015609375F;
		float f10 = ((float)(this.particleTextureIndex / 16) + this.particleTextureJitterY / 4.0F) / 16.0F;
		float f11 = f10 + 0.015609375F;
		float f12 = 0.1F * this.particleScale;
		float f13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)f2 - interpPosX);
		float f14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)f2 - interpPosY);
		float f15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f2 - interpPosZ);
		float f16 = this.getBrightness(f2);
		tessellator1.setColorOpaque_F(f16 * this.particleRed, f16 * this.particleGreen, f16 * this.particleBlue);
		tessellator1.addVertexWithUV((double)(f13 - f3 * f12 - f6 * f12), (double)(f14 - f4 * f12), (double)(f15 - f5 * f12 - f7 * f12), (double)f8, (double)f11);
		tessellator1.addVertexWithUV((double)(f13 - f3 * f12 + f6 * f12), (double)(f14 + f4 * f12), (double)(f15 - f5 * f12 + f7 * f12), (double)f8, (double)f10);
		tessellator1.addVertexWithUV((double)(f13 + f3 * f12 + f6 * f12), (double)(f14 + f4 * f12), (double)(f15 + f5 * f12 + f7 * f12), (double)f9, (double)f10);
		tessellator1.addVertexWithUV((double)(f13 + f3 * f12 - f6 * f12), (double)(f14 - f4 * f12), (double)(f15 + f5 * f12 - f7 * f12), (double)f9, (double)f11);
	}
}
