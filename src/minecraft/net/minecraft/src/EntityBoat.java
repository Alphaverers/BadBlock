package net.minecraft.src;

import java.util.List;

public class EntityBoat extends Entity {
	public int damageTaken;
	public int timeSinceHit;
	public int forwardDirection;

	public EntityBoat(World world1) {
		super(world1);
		this.damageTaken = 0;
		this.timeSinceHit = 0;
		this.forwardDirection = 1;
		this.preventEntitySpawning = true;
		this.setSize(1.5F, 0.6F);
		this.yOffset = this.height / 2.0F;
		this.canTriggerWalking = false;
	}

	public AxisAlignedBB getCollisionBox(Entity entity1) {
		return entity1.boundingBox;
	}

	public AxisAlignedBB getBoundingBox() {
		return this.boundingBox;
	}

	public boolean canBePushed() {
		return true;
	}

	public EntityBoat(World worldObj, double x, double y, double z) {
		this(worldObj);
		this.setPosition(x, y + (double)this.yOffset, z);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
	}

	public double getMountedYOffset() {
		return (double)this.height * 0.0D - (double)0.3F;
	}

	public boolean attackEntityFrom(Entity entity1, int i2) {
		this.forwardDirection = -this.forwardDirection;
		this.timeSinceHit = 10;
		this.damageTaken += i2 * 10;
		if(this.damageTaken > 40) {
			int i3;
			for(i3 = 0; i3 < 3; ++i3) {
				this.entityDropItem(Block.planks.blockID, 1, 0.0F);
			}

			for(i3 = 0; i3 < 2; ++i3) {
				this.entityDropItem(Item.stick.shiftedIndex, 1, 0.0F);
			}

			this.setEntityDead();
		}

		return true;
	}

	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public void onUpdate() {
		super.onUpdate();
		if(this.timeSinceHit > 0) {
			--this.timeSinceHit;
		}

		if(this.damageTaken > 0) {
			--this.damageTaken;
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		byte b1 = 5;
		double d2 = 0.0D;

		for(int i4 = 0; i4 < b1; ++i4) {
			double d5 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(i4 + 0) / (double)b1 - 0.125D;
			double d7 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(i4 + 1) / (double)b1 - 0.125D;
			AxisAlignedBB axisAlignedBB9 = AxisAlignedBB.getBoundingBoxFromPool(this.boundingBox.minX, d5, this.boundingBox.minZ, this.boundingBox.maxX, d7, this.boundingBox.maxZ);
			if(this.worldObj.isAABBInMaterial(axisAlignedBB9, Material.water)) {
				d2 += 1.0D / (double)b1;
			}
		}

		double d23 = d2 * 2.0D - 1.0D;
		this.motionY += (double)0.04F * d23;
		if(this.riddenByEntity != null) {
			this.motionX += this.riddenByEntity.motionX * 0.2D;
			this.motionZ += this.riddenByEntity.motionZ * 0.2D;
		}

		double d6 = 0.4D;
		if(this.motionX < -d6) {
			this.motionX = -d6;
		}

		if(this.motionX > d6) {
			this.motionX = d6;
		}

		if(this.motionZ < -d6) {
			this.motionZ = -d6;
		}

		if(this.motionZ > d6) {
			this.motionZ = d6;
		}

		if(this.onGround) {
			this.motionX *= 0.5D;
			this.motionY *= 0.5D;
			this.motionZ *= 0.5D;
		}

		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		double d8 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		double d10;
		double d12;
		if(d8 > 0.15D) {
			d10 = Math.cos((double)this.rotationYaw * Math.PI / 180.0D);
			d12 = Math.sin((double)this.rotationYaw * Math.PI / 180.0D);

			for(int i14 = 0; (double)i14 < 1.0D + d8 * 60.0D; ++i14) {
				double d15 = (double)(this.rand.nextFloat() * 2.0F - 1.0F);
				double d17 = (double)(this.rand.nextInt(2) * 2 - 1) * 0.7D;
				double d19;
				double d21;
				if(this.rand.nextBoolean()) {
					d19 = this.posX - d10 * d15 * 0.8D + d12 * d17;
					d21 = this.posZ - d12 * d15 * 0.8D - d10 * d17;
					this.worldObj.spawnParticle("splash", d19, this.posY - 0.125D, d21, this.motionX, this.motionY, this.motionZ);
				} else {
					d19 = this.posX + d10 + d12 * d15 * 0.7D;
					d21 = this.posZ + d12 - d10 * d15 * 0.7D;
					this.worldObj.spawnParticle("splash", d19, this.posY - 0.125D, d21, this.motionX, this.motionY, this.motionZ);
				}
			}
		}

		if(this.isCollidedHorizontally && d8 > 0.15D) {
			this.setEntityDead();

			int i24;
			for(i24 = 0; i24 < 3; ++i24) {
				this.entityDropItem(Block.planks.blockID, 1, 0.0F);
			}

			for(i24 = 0; i24 < 2; ++i24) {
				this.entityDropItem(Item.stick.shiftedIndex, 1, 0.0F);
			}
		} else {
			this.motionX *= (double)0.99F;
			this.motionY *= (double)0.95F;
			this.motionZ *= (double)0.99F;
		}

		this.rotationPitch = 0.0F;
		d10 = (double)this.rotationYaw;
		d12 = this.prevPosX - this.posX;
		double d25 = this.prevPosZ - this.posZ;
		if(d12 * d12 + d25 * d25 > 0.001D) {
			d10 = (double)((float)(Math.atan2(d25, d12) * 180.0D / Math.PI));
		}

		double d16;
		for(d16 = d10 - (double)this.rotationYaw; d16 >= 180.0D; d16 -= 360.0D) {
		}

		while(d16 < -180.0D) {
			d16 += 360.0D;
		}

		if(d16 > 20.0D) {
			d16 = 20.0D;
		}

		if(d16 < -20.0D) {
			d16 = -20.0D;
		}

		this.rotationYaw = (float)((double)this.rotationYaw + d16);
		this.setRotation(this.rotationYaw, this.rotationPitch);
		List list18 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand((double)0.2F, 0.0D, (double)0.2F));
		if(list18 != null && list18.size() > 0) {
			for(int i26 = 0; i26 < list18.size(); ++i26) {
				Entity entity20 = (Entity)list18.get(i26);
				if(entity20 != this.riddenByEntity && entity20.canBePushed() && entity20 instanceof EntityBoat) {
					entity20.applyEntityCollision(this);
				}
			}
		}

		if(this.riddenByEntity != null && this.riddenByEntity.isDead) {
			this.riddenByEntity = null;
		}

	}

	protected void updateRiderPosition() {
		double d1 = Math.cos((double)this.rotationYaw * Math.PI / 180.0D) * 0.4D;
		double d3 = Math.sin((double)this.rotationYaw * Math.PI / 180.0D) * 0.4D;
		this.riddenByEntity.setPosition(this.posX + d1, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ + d3);
	}

	protected void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
	}

	protected void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
	}

	public float getShadowSize() {
		return 0.0F;
	}

	public boolean interact(EntityPlayer entityPlayer1) {
		entityPlayer1.mountEntity(this);
		return true;
	}
}
