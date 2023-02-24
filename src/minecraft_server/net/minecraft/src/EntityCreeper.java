package net.minecraft.src;

public class EntityCreeper extends EntityMobs {
	int timeSinceIgnited;
	int lastActiveTime;
	int fuseDuration = 30;
	int creeperState = -1;

	public EntityCreeper(World world1) {
		super(world1);
		this.texture = "/mob/creeper.png";
	}

	public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeEntityToNBT(nBTTagCompound1);
	}

	public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readEntityFromNBT(nBTTagCompound1);
	}

	protected void updateEntityActionState() {
		this.lastActiveTime = this.timeSinceIgnited;
		if(this.timeSinceIgnited > 0 && this.creeperState < 0) {
			--this.timeSinceIgnited;
		}

		if(this.creeperState >= 0) {
			this.creeperState = 2;
		}

		super.updateEntityActionState();
		if(this.creeperState != 1) {
			this.creeperState = -1;
		}

	}

	protected String getHurtSound() {
		return "mob.creeper";
	}

	protected String getDeathSound() {
		return "mob.creeperdeath";
	}

	public void onDeath(Entity entity1) {
		super.onDeath(entity1);
		if(entity1 instanceof EntitySkeleton) {
			this.dropItem(Item.record13.shiftedIndex + this.rand.nextInt(2), 1);
		}

	}

	protected void attackEntity(Entity entity1, float f2) {
		if(this.creeperState <= 0 && f2 < 3.0F || this.creeperState > 0 && f2 < 7.0F) {
			if(this.timeSinceIgnited == 0) {
				this.worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);
			}

			this.creeperState = 1;
			++this.timeSinceIgnited;
			if(this.timeSinceIgnited == this.fuseDuration) {
				this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 3.0F);
				this.setEntityDead();
			}

			this.hasAttacked = true;
		}

	}

	protected int getDropItemId() {
		return Item.gunpowder.shiftedIndex;
	}
}
