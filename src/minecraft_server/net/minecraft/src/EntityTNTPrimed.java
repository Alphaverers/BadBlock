package net.minecraft.src;

public class EntityTNTPrimed extends Entity {
	public int fuse;

	public EntityTNTPrimed(World world1) {
		super(world1);
		this.fuse = 0;
		this.preventEntitySpawning = true;
		this.setSize(0.98F, 0.98F);
		this.yOffset = this.height / 2.0F;
	}

	public EntityTNTPrimed(World world, float x, float y, float z) {
		this(world);
		this.setPosition((double)x, (double)y, (double)z);
		float f5 = (float)(Math.random() * (double)(float)Math.PI * 2.0D);
		this.motionX = (double)(-MathHelper.sin(f5 * (float)Math.PI / 180.0F) * 0.02F);
		this.motionY = (double)0.2F;
		this.motionZ = (double)(-MathHelper.cos(f5 * (float)Math.PI / 180.0F) * 0.02F);
		this.canTriggerWalking = false;
		this.fuse = 80;
		this.prevPosX = (double)x;
		this.prevPosY = (double)y;
		this.prevPosZ = (double)z;
	}

	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= (double)0.04F;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= (double)0.98F;
		this.motionY *= (double)0.98F;
		this.motionZ *= (double)0.98F;
		if(this.onGround) {
			this.motionX *= (double)0.7F;
			this.motionZ *= (double)0.7F;
			this.motionY *= -0.5D;
		}

		if(this.fuse-- <= 0) {
			this.setEntityDead();
			this.createExplosion();
		} else {
			this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
		}

	}

	private void createExplosion() {
		float f1 = 4.0F;
		this.worldObj.createExplosion((Entity)null, this.posX, this.posY, this.posZ, f1);
	}

	protected void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		nBTTagCompound1.setByte("Fuse", (byte)this.fuse);
	}

	protected void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		this.fuse = nBTTagCompound1.getByte("Fuse");
	}
}
