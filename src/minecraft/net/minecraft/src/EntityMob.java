package net.minecraft.src;

public class EntityMob extends EntityCreature implements IMobs {
	protected int attackStrength = 2;

	public EntityMob(World world1) {
		super(world1);
		this.health = 20;
	}

	public void onLivingUpdate() {
		float f1 = this.getBrightness(1.0F);
		if(f1 > 0.5F) {
			this.entityAge += 2;
		}

		super.onLivingUpdate();
	}

	public void onUpdate() {
		super.onUpdate();
		if(this.worldObj.difficultySetting == 0) {
			this.setEntityDead();
		}

	}

	protected Entity findPlayerToAttack() {
		EntityPlayer entityPlayer1 = this.worldObj.getClosestPlayerToEntity(this, 16.0D);
		return entityPlayer1 != null && this.canEntityBeSeen(entityPlayer1) ? entityPlayer1 : null;
	}

	public boolean attackEntityFrom(Entity entity1, int i2) {
		if(super.attackEntityFrom(entity1, i2)) {
			if(this.riddenByEntity != entity1 && this.ridingEntity != entity1) {
				if(entity1 != this) {
					this.entityToAttack = entity1;
				}

				return true;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	protected void attackEntity(Entity entity1, float f2) {
		if((double)f2 < 2.5D && entity1.boundingBox.maxY > this.boundingBox.minY && entity1.boundingBox.minY < this.boundingBox.maxY) {
			this.attackTime = 20;
			entity1.attackEntityFrom(this, this.attackStrength);
		}

	}

	protected float getBlockPathWeight(int i1, int i2, int i3) {
		return 0.5F - this.worldObj.getBrightness(i1, i2, i3);
	}

	public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeEntityToNBT(nBTTagCompound1);
	}

	public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readEntityFromNBT(nBTTagCompound1);
	}

	public boolean getCanSpawnHere() {
		int i1 = MathHelper.floor_double(this.posX);
		int i2 = MathHelper.floor_double(this.boundingBox.minY);
		int i3 = MathHelper.floor_double(this.posZ);
		if(this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, i1, i2, i3) > this.rand.nextInt(32)) {
			return false;
		} else {
			int i4 = this.worldObj.getBlockLightValue(i1, i2, i3);
			return i4 <= this.rand.nextInt(8) && super.getCanSpawnHere();
		}
	}
}
