package net.minecraft.src;

public class EntitySlime extends EntityLiving implements IMobs {
	public float squishFactor;
	public float prevSquishFactor;
	private int slimeJumpDelay = 0;
	public int size = 1;

	public EntitySlime(World world1) {
		super(world1);
		this.texture = "/mob/slime.png";
		this.size = 1 << this.rand.nextInt(3);
		this.yOffset = 0.0F;
		this.slimeJumpDelay = this.rand.nextInt(20) + 10;
		this.setSlimeSize(this.size);
	}

	public void setSlimeSize(int i1) {
		this.size = i1;
		this.setSize(0.6F * (float)i1, 0.6F * (float)i1);
		this.health = i1 * i1;
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeEntityToNBT(nBTTagCompound1);
		nBTTagCompound1.setInteger("Size", this.size - 1);
	}

	public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readEntityFromNBT(nBTTagCompound1);
		this.size = nBTTagCompound1.getInteger("Size") + 1;
	}

	public void onUpdate() {
		this.prevSquishFactor = this.squishFactor;
		boolean z1 = this.onGround;
		super.onUpdate();
		if(this.onGround && !z1) {
			for(int i2 = 0; i2 < this.size * 8; ++i2) {
				float f3 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
				float f4 = this.rand.nextFloat() * 0.5F + 0.5F;
				float f5 = MathHelper.sin(f3) * (float)this.size * 0.5F * f4;
				float f6 = MathHelper.cos(f3) * (float)this.size * 0.5F * f4;
				this.worldObj.spawnParticle("slime", this.posX + (double)f5, this.boundingBox.minY, this.posZ + (double)f6, 0.0D, 0.0D, 0.0D);
			}

			if(this.size > 2) {
				this.worldObj.playSoundAtEntity(this, "mob.slime", this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
			}

			this.squishFactor = -0.5F;
		}

		this.squishFactor *= 0.6F;
	}

	protected void updateEntityActionState() {
		EntityPlayer entityPlayer1 = this.worldObj.getClosestPlayerToEntity(this, 16.0D);
		if(entityPlayer1 != null) {
			this.faceEntity(entityPlayer1, 10.0F);
		}

		if(this.onGround && this.slimeJumpDelay-- <= 0) {
			this.slimeJumpDelay = this.rand.nextInt(20) + 10;
			if(entityPlayer1 != null) {
				this.slimeJumpDelay /= 3;
			}

			this.isJumping = true;
			if(this.size > 1) {
				this.worldObj.playSoundAtEntity(this, "mob.slime", this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
			}

			this.squishFactor = 1.0F;
			this.moveStrafing = 1.0F - this.rand.nextFloat() * 2.0F;
			this.moveForward = (float)(1 * this.size);
		} else {
			this.isJumping = false;
			if(this.onGround) {
				this.moveStrafing = this.moveForward = 0.0F;
			}
		}

	}

	public void setEntityDead() {
		if(this.size > 1 && this.health == 0) {
			for(int i1 = 0; i1 < 4; ++i1) {
				float f2 = ((float)(i1 % 2) - 0.5F) * (float)this.size / 4.0F;
				float f3 = ((float)(i1 / 2) - 0.5F) * (float)this.size / 4.0F;
				EntitySlime entitySlime4 = new EntitySlime(this.worldObj);
				entitySlime4.setSlimeSize(this.size / 2);
				entitySlime4.setLocationAndAngles(this.posX + (double)f2, this.posY + 0.5D, this.posZ + (double)f3, this.rand.nextFloat() * 360.0F, 0.0F);
				this.worldObj.spawnEntityInWorld(entitySlime4);
			}
		}

		super.setEntityDead();
	}

	public void onCollideWithPlayer(EntityPlayer entityPlayer1) {
		if(this.size > 1 && this.canEntityBeSeen(entityPlayer1) && (double)this.getDistanceToEntity(entityPlayer1) < 0.6D * (double)this.size && entityPlayer1.attackEntityFrom(this, this.size)) {
			this.worldObj.playSoundAtEntity(this, "mob.slimeattack", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
		}

	}

	protected String getHurtSound() {
		return "mob.slime";
	}

	protected String getDeathSound() {
		return "mob.slime";
	}

	protected int getDropItemId() {
		return this.size == 1 ? Item.slimeBall.shiftedIndex : 0;
	}

	public boolean getCanSpawnHere() {
		Chunk chunk1 = this.worldObj.getChunkFromBlockCoords(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY));
		return (this.size == 1 || this.worldObj.difficultySetting > 0) && this.rand.nextInt(10) == 0 && chunk1.getRandomWithSeed(987234911L).nextInt(30) == 0 && this.posY < 16.0D;
	}

	protected float getSoundVolume() {
		return 0.6F;
	}
}
