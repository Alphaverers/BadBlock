package net.minecraft.src;

public class EntitySkeleton extends EntityMob {
	public EntitySkeleton(World world1) {
		super(world1);
		this.texture = "/mob/skeleton.png";
	}

	protected String getLivingSound() {
		return "mob.skeleton";
	}

	protected String getHurtSound() {
		return "mob.skeletonhurt";
	}

	protected String getDeathSound() {
		return "mob.skeletonhurt";
	}

	public void onLivingUpdate() {
		if(this.worldObj.isDaytime()) {
			float f1 = this.getBrightness(1.0F);
			if(f1 > 0.5F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) && this.rand.nextFloat() * 30.0F < (f1 - 0.4F) * 2.0F) {
				this.fire = 300;
			}
		}

		super.onLivingUpdate();
	}

	protected void attackEntity(Entity entity1, float f2) {
		if(f2 < 10.0F) {
			double d3 = entity1.posX - this.posX;
			double d5 = entity1.posZ - this.posZ;
			if(this.attackTime == 0) {
				EntityArrow entityArrow7 = new EntityArrow(this.worldObj, this);
				++entityArrow7.posY;
				double d8 = entity1.posY - (double)0.2F - entityArrow7.posY;
				float f10 = MathHelper.sqrt_double(d3 * d3 + d5 * d5) * 0.2F;
				this.worldObj.playSoundAtEntity(this, "random.bow", 1.0F, 1.0F / (this.rand.nextFloat() * 0.4F + 0.8F));
				this.worldObj.spawnEntityInWorld(entityArrow7);
				entityArrow7.setArrowHeading(d3, d8 + (double)f10, d5, 0.6F, 12.0F);
				this.attackTime = 30;
			}

			this.rotationYaw = (float)(Math.atan2(d5, d3) * 180.0D / (double)(float)Math.PI) - 90.0F;
			this.hasAttacked = true;
		}

	}

	public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeEntityToNBT(nBTTagCompound1);
	}

	public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readEntityFromNBT(nBTTagCompound1);
	}

	protected int getDropItemId() {
		return Item.arrow.shiftedIndex;
	}
}
