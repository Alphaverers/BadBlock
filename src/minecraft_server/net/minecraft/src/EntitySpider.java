package net.minecraft.src;

public class EntitySpider extends EntityMobs {
	public EntitySpider(World world1) {
		super(world1);
		this.texture = "/mob/spider.png";
		this.setSize(1.4F, 0.9F);
		this.moveSpeed = 0.8F;
	}

	public double getMountedYOffset() {
		return (double)this.height * 0.75D - 0.5D;
	}

	protected Entity findPlayerToAttack() {
		float f1 = this.getBrightness(1.0F);
		if(f1 < 0.5F) {
			double d2 = 16.0D;
			return this.worldObj.getClosestPlayerToEntity(this, d2);
		} else {
			return null;
		}
	}

	protected String getLivingSound() {
		return "mob.spider";
	}

	protected String getHurtSound() {
		return "mob.spider";
	}

	protected String getDeathSound() {
		return "mob.spiderdeath";
	}

	protected void attackEntity(Entity entity1, float f2) {
		float f3 = this.getBrightness(1.0F);
		if(f3 > 0.5F && this.rand.nextInt(100) == 0) {
			this.entityToAttack = null;
		} else {
			if(f2 > 2.0F && f2 < 6.0F && this.rand.nextInt(10) == 0) {
				if(this.onGround) {
					double d4 = entity1.posX - this.posX;
					double d6 = entity1.posZ - this.posZ;
					float f8 = MathHelper.sqrt_double(d4 * d4 + d6 * d6);
					this.motionX = d4 / (double)f8 * 0.5D * (double)0.8F + this.motionX * (double)0.2F;
					this.motionZ = d6 / (double)f8 * 0.5D * (double)0.8F + this.motionZ * (double)0.2F;
					this.motionY = (double)0.4F;
				}
			} else {
				super.attackEntity(entity1, f2);
			}

		}
	}

	public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeEntityToNBT(nBTTagCompound1);
	}

	public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readEntityFromNBT(nBTTagCompound1);
	}

	protected int getDropItemId() {
		return Item.silk.shiftedIndex;
	}
}
