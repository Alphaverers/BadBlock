package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class EntityPickupFX extends EntityFX {
	private Entity entityToPickUp;
	private Entity entityPickingUp;
	private int age = 0;
	private int maxAge = 0;
	private float yOffs;

	public EntityPickupFX(World worldObj, Entity entityToPickUp, Entity entityPickingUp, float yOffset) {
		super(worldObj, entityToPickUp.posX, entityToPickUp.posY, entityToPickUp.posZ, entityToPickUp.motionX, entityToPickUp.motionY, entityToPickUp.motionZ);
		this.entityToPickUp = entityToPickUp;
		this.entityPickingUp = entityPickingUp;
		this.maxAge = 3;
		this.yOffs = yOffset;
	}

	public void renderParticle(Tessellator tessellator1, float f2, float f3, float f4, float f5, float f6, float f7) {
		float f8 = ((float)this.age + f2) / (float)this.maxAge;
		f8 *= f8;
		double d9 = this.entityToPickUp.posX;
		double d11 = this.entityToPickUp.posY;
		double d13 = this.entityToPickUp.posZ;
		double d15 = this.entityPickingUp.lastTickPosX + (this.entityPickingUp.posX - this.entityPickingUp.lastTickPosX) * (double)f2;
		double d17 = this.entityPickingUp.lastTickPosY + (this.entityPickingUp.posY - this.entityPickingUp.lastTickPosY) * (double)f2 + (double)this.yOffs;
		double d19 = this.entityPickingUp.lastTickPosZ + (this.entityPickingUp.posZ - this.entityPickingUp.lastTickPosZ) * (double)f2;
		double d21 = d9 + (d15 - d9) * (double)f8;
		double d23 = d11 + (d17 - d11) * (double)f8;
		double d25 = d13 + (d19 - d13) * (double)f8;
		int i27 = MathHelper.floor_double(d21);
		int i28 = MathHelper.floor_double(d23 + (double)(this.yOffset / 2.0F));
		int i29 = MathHelper.floor_double(d25);
		float f30 = this.worldObj.getBrightness(i27, i28, i29);
		d21 -= interpPosX;
		d23 -= interpPosY;
		d25 -= interpPosZ;
		GL11.glColor4f(f30, f30, f30, 1.0F);
		RenderManager.instance.renderEntityWithPosYaw(this.entityToPickUp, (double)((float)d21), (double)((float)d23), (double)((float)d25), this.entityToPickUp.rotationYaw, f2);
	}

	public void onUpdate() {
		++this.age;
		if(this.age == this.maxAge) {
			this.setEntityDead();
		}

	}

	public int getFXLayer() {
		return 3;
	}
}
