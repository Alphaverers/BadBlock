package net.minecraft.src;

import java.util.List;

public class EntityLiving extends Entity {
	public int heartsHalvesLife = 20;
	public float unusedRotationPitch2;
	public float unusedFloat;
	public float unusedRotationPitch;
	public float renderYawOffset = 0.0F;
	public float prevRenderYawOffset = 0.0F;
	protected float ridingRotUnused;
	protected float prevRidingRotUnused;
	protected float rotationUnused;
	protected float prevRotationUnused;
	protected boolean unusedBool1 = true;
	protected String texture = "/char.png";
	protected boolean unusedBool2 = true;
	protected float unusedRotation = 0.0F;
	protected String entityType = null;
	protected float unusedFloat1 = 1.0F;
	protected int scoreValue = 0;
	protected float unusedFloat2 = 0.0F;
	public boolean isAIEnabled = false;
	public float prevSwingProgress;
	public float swingProgress;
	public int health = 10;
	public int prevHealth;
	private int livingSoundTime;
	public int hurtTime;
	public int maxHurtTime;
	public float attackedAtYaw = 0.0F;
	public int deathTime = 0;
	public int attackTime = 0;
	public float prevCameraPitch;
	public float cameraPitch;
	protected boolean dead = false;
	public int unusedInt = -1;
	public float unusedFloat4 = (float)(Math.random() * (double)0.9F + (double)0.1F);
	public float prevLimbYaw;
	public float limbYaw;
	public float limbSwing;
	private int newPosRotationIncrements;
	private double newPosX;
	private double newPosY;
	private double newPosZ;
	private double newRotationYaw;
	private double newRotationPitch;
	float unusedFloat3 = 0.0F;
	protected int entityAge = 0;
	protected float moveStrafing;
	protected float moveForward;
	protected float randomYawVelocity;
	protected boolean isJumping = false;
	protected float defaultPitch = 0.0F;
	protected float moveSpeed = 0.7F;
	private Entity currentTarget;
	private int numTicksToChaseTarget = 0;

	public EntityLiving(World world1) {
		super(world1);
		this.preventEntitySpawning = true;
		this.unusedRotationPitch = (float)(Math.random() + 1.0D) * 0.01F;
		this.setPosition(this.posX, this.posY, this.posZ);
		this.unusedRotationPitch2 = (float)Math.random() * 12398.0F;
		this.rotationYaw = (float)(Math.random() * (double)(float)Math.PI * 2.0D);
		this.unusedFloat = 1.0F;
		this.stepHeight = 0.5F;
	}

	protected boolean canEntityBeSeen(Entity entity) {
		return this.worldObj.rayTraceBlocks(Vec3D.createVector(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ), Vec3D.createVector(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ)) == null;
	}

	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public boolean canBePushed() {
		return !this.isDead;
	}

	protected float getEyeHeight() {
		return this.height * 0.85F;
	}

	public int getTalkInterval() {
		return 80;
	}

	public void onEntityUpdate() {
		this.prevSwingProgress = this.swingProgress;
		super.onEntityUpdate();
		if(this.rand.nextInt(1000) < this.livingSoundTime++) {
			this.livingSoundTime = -this.getTalkInterval();
			String string1 = this.getLivingSound();
			if(string1 != null) {
				this.worldObj.playSoundAtEntity(this, string1, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}
		}

		if(this.isEntityAlive() && this.isEntityInsideOpaqueBlock()) {
			this.attackEntityFrom((Entity)null, 1);
		}

		int i8;
		if(this.isEntityAlive() && this.isInsideOfMaterial(Material.water)) {
			--this.air;
			if(this.air == -20) {
				this.air = 0;

				for(i8 = 0; i8 < 8; ++i8) {
					float f2 = this.rand.nextFloat() - this.rand.nextFloat();
					float f3 = this.rand.nextFloat() - this.rand.nextFloat();
					float f4 = this.rand.nextFloat() - this.rand.nextFloat();
					this.worldObj.spawnParticle("bubble", this.posX + (double)f2, this.posY + (double)f3, this.posZ + (double)f4, this.motionX, this.motionY, this.motionZ);
				}

				this.attackEntityFrom((Entity)null, 2);
			}

			this.fire = 0;
		} else {
			this.air = this.maxAir;
		}

		this.prevCameraPitch = this.cameraPitch;
		if(this.attackTime > 0) {
			--this.attackTime;
		}

		if(this.hurtTime > 0) {
			--this.hurtTime;
		}

		if(this.heartsLife > 0) {
			--this.heartsLife;
		}

		if(this.health <= 0) {
			++this.deathTime;
			if(this.deathTime > 20) {
				this.onEntityDeath();
				this.setEntityDead();

				for(i8 = 0; i8 < 20; ++i8) {
					double d9 = this.rand.nextGaussian() * 0.02D;
					double d10 = this.rand.nextGaussian() * 0.02D;
					double d6 = this.rand.nextGaussian() * 0.02D;
					this.worldObj.spawnParticle("explode", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d9, d10, d6);
				}
			}
		}

		this.prevRotationUnused = this.rotationUnused;
		this.prevRenderYawOffset = this.renderYawOffset;
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
	}

	public void spawnExplosionParticle() {
		for(int i1 = 0; i1 < 20; ++i1) {
			double d2 = this.rand.nextGaussian() * 0.02D;
			double d4 = this.rand.nextGaussian() * 0.02D;
			double d6 = this.rand.nextGaussian() * 0.02D;
			double d8 = 10.0D;
			this.worldObj.spawnParticle("explode", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d2 * d8, this.posY + (double)(this.rand.nextFloat() * this.height) - d4 * d8, this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d6 * d8, d2, d4, d6);
		}

	}

	public void updateRidden() {
		super.updateRidden();
		this.ridingRotUnused = this.prevRidingRotUnused;
		this.prevRidingRotUnused = 0.0F;
	}

	public void onUpdate() {
		super.onUpdate();
		this.onLivingUpdate();
		double d1 = this.posX - this.prevPosX;
		double d3 = this.posZ - this.prevPosZ;
		float f5 = MathHelper.sqrt_double(d1 * d1 + d3 * d3);
		float f6 = this.renderYawOffset;
		float f7 = 0.0F;
		this.ridingRotUnused = this.prevRidingRotUnused;
		float f8 = 0.0F;
		if(f5 > 0.05F) {
			f8 = 1.0F;
			f7 = f5 * 3.0F;
			f6 = (float)Math.atan2(d3, d1) * 180.0F / (float)Math.PI - 90.0F;
		}

		if(this.swingProgress > 0.0F) {
			f6 = this.rotationYaw;
		}

		if(!this.onGround) {
			f8 = 0.0F;
		}

		this.prevRidingRotUnused += (f8 - this.prevRidingRotUnused) * 0.3F;

		float f9;
		for(f9 = f6 - this.renderYawOffset; f9 < -180.0F; f9 += 360.0F) {
		}

		while(f9 >= 180.0F) {
			f9 -= 360.0F;
		}

		this.renderYawOffset += f9 * 0.3F;

		float f10;
		for(f10 = this.rotationYaw - this.renderYawOffset; f10 < -180.0F; f10 += 360.0F) {
		}

		while(f10 >= 180.0F) {
			f10 -= 360.0F;
		}

		boolean z11 = f10 < -90.0F || f10 >= 90.0F;
		if(f10 < -75.0F) {
			f10 = -75.0F;
		}

		if(f10 >= 75.0F) {
			f10 = 75.0F;
		}

		this.renderYawOffset = this.rotationYaw - f10;
		if(f10 * f10 > 2500.0F) {
			this.renderYawOffset += f10 * 0.2F;
		}

		if(z11) {
			f7 *= -1.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		while(this.renderYawOffset - this.prevRenderYawOffset < -180.0F) {
			this.prevRenderYawOffset -= 360.0F;
		}

		while(this.renderYawOffset - this.prevRenderYawOffset >= 180.0F) {
			this.prevRenderYawOffset += 360.0F;
		}

		while(this.rotationPitch - this.prevRotationPitch < -180.0F) {
			this.prevRotationPitch -= 360.0F;
		}

		while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		this.rotationUnused += f7;
	}

	protected void setSize(float width, float height) {
		super.setSize(width, height);
	}

	public void heal(int health) {
		if(this.health > 0) {
			this.health += health;
			if(this.health > 20) {
				this.health = 20;
			}

			this.heartsLife = this.heartsHalvesLife / 2;
		}
	}

	public boolean attackEntityFrom(Entity entity1, int i2) {
		if(this.worldObj.multiplayerWorld) {
			i2 = 0;
		}

		this.entityAge = 0;
		if(this.health <= 0) {
			return false;
		} else {
			this.limbYaw = 1.5F;
			if((float)this.heartsLife > (float)this.heartsHalvesLife / 2.0F) {
				if(this.prevHealth - i2 >= this.health) {
					return false;
				}

				this.health = this.prevHealth - i2;
			} else {
				this.prevHealth = this.health;
				this.heartsLife = this.heartsHalvesLife;
				this.health -= i2;
				this.hurtTime = this.maxHurtTime = 10;
			}

			this.attackedAtYaw = 0.0F;
			if(entity1 != null) {
				double d3 = entity1.posX - this.posX;

				double d5;
				for(d5 = entity1.posZ - this.posZ; d3 * d3 + d5 * d5 < 1.0E-4D; d5 = (Math.random() - Math.random()) * 0.01D) {
					d3 = (Math.random() - Math.random()) * 0.01D;
				}

				this.attackedAtYaw = (float)(Math.atan2(d5, d3) * 180.0D / (double)(float)Math.PI) - this.rotationYaw;
				this.a(entity1, i2, d3, d5);
			} else {
				this.attackedAtYaw = (float)((int)(Math.random() * 2.0D) * 180);
			}

			if(this.health <= 0) {
				this.worldObj.playSoundAtEntity(this, this.getDeathSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
				this.onDeath(entity1);
			} else {
				this.worldObj.playSoundAtEntity(this, this.getHurtSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}

			return true;
		}
	}

	protected float getSoundVolume() {
		return 1.0F;
	}

	protected String getLivingSound() {
		return null;
	}

	protected String getHurtSound() {
		return "random.hurt";
	}

	protected String getDeathSound() {
		return "random.hurt";
	}

	public void a(Entity entity, int x, double y, double z) {
		float f7 = MathHelper.sqrt_double(y * y + z * z);
		float f8 = 0.4F;
		this.motionX /= 2.0D;
		this.motionY /= 2.0D;
		this.motionZ /= 2.0D;
		this.motionX -= y / (double)f7 * (double)f8;
		this.motionY += (double)0.4F;
		this.motionZ -= z / (double)f7 * (double)f8;
		if(this.motionY > (double)0.4F) {
			this.motionY = (double)0.4F;
		}

	}

	public void onDeath(Entity entity) {
		if(this.scoreValue > 0 && entity != null) {
			entity.addToPlayerScore(this, this.scoreValue);
		}

		this.dead = true;
		int i2 = this.getDropItemId();
		if(i2 > 0) {
			int i3 = this.rand.nextInt(3);

			for(int i4 = 0; i4 < i3; ++i4) {
				this.dropItem(i2, 1);
			}
		}

	}

	protected int getDropItemId() {
		return 0;
	}

	protected void fall(float f1) {
		int i2 = (int)Math.ceil((double)(f1 - 3.0F));
		if(i2 > 0) {
			this.attackEntityFrom((Entity)null, i2);
			int i3 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - (double)0.2F - (double)this.yOffset), MathHelper.floor_double(this.posZ));
			if(i3 > 0) {
				StepSound stepSound4 = Block.blockList[i3].stepSound;
				this.worldObj.playSoundAtEntity(this, stepSound4.getStepSound(), stepSound4.getVolume() * 0.5F, stepSound4.getPitch() * 0.75F);
			}
		}

	}

	public void handleMovement(float f1, float f2) {
		double d3;
		if(this.handleWaterMovement()) {
			d3 = this.posY;
			this.moveFlying(f1, f2, 0.02F);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= (double)0.8F;
			this.motionY *= (double)0.8F;
			this.motionZ *= (double)0.8F;
			this.motionY -= 0.02D;
			if(this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + (double)0.6F - this.posY + d3, this.motionZ)) {
				this.motionY = (double)0.3F;
			}
		} else if(this.handleLavaMovement()) {
			d3 = this.posY;
			this.moveFlying(f1, f2, 0.02F);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.5D;
			this.motionY *= 0.5D;
			this.motionZ *= 0.5D;
			this.motionY -= 0.02D;
			if(this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + (double)0.6F - this.posY + d3, this.motionZ)) {
				this.motionY = (double)0.3F;
			}
		} else {
			float f8 = 0.91F;
			if(this.onGround) {
				f8 = 0.54600006F;
				int i4 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));
				if(i4 > 0) {
					f8 = Block.blockList[i4].slipperiness * 0.91F;
				}
			}

			float f9 = 0.16277136F / (f8 * f8 * f8);
			this.moveFlying(f1, f2, this.onGround ? 0.1F * f9 : 0.02F);
			f8 = 0.91F;
			if(this.onGround) {
				f8 = 0.54600006F;
				int i5 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));
				if(i5 > 0) {
					f8 = Block.blockList[i5].slipperiness * 0.91F;
				}
			}

			if(this.D()) {
				this.fallDistance = 0.0F;
				if(this.motionY < -0.15D) {
					this.motionY = -0.15D;
				}
			}

			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			if(this.isCollidedHorizontally && this.D()) {
				this.motionY = 0.2D;
			}

			this.motionY -= 0.08D;
			this.motionY *= (double)0.98F;
			this.motionX *= (double)f8;
			this.motionZ *= (double)f8;
		}

		this.prevLimbYaw = this.limbYaw;
		d3 = this.posX - this.prevPosX;
		double d10 = this.posZ - this.prevPosZ;
		float f7 = MathHelper.sqrt_double(d3 * d3 + d10 * d10) * 4.0F;
		if(f7 > 1.0F) {
			f7 = 1.0F;
		}

		this.limbYaw += (f7 - this.limbYaw) * 0.4F;
		this.limbSwing += this.limbYaw;
	}

	public boolean D() {
		int i1 = MathHelper.floor_double(this.posX);
		int i2 = MathHelper.floor_double(this.boundingBox.minY);
		int i3 = MathHelper.floor_double(this.posZ);
		return this.worldObj.getBlockId(i1, i2, i3) == Block.ladder.blockID || this.worldObj.getBlockId(i1, i2 + 1, i3) == Block.ladder.blockID;
	}

	public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		nBTTagCompound1.setShort("Health", (short)this.health);
		nBTTagCompound1.setShort("HurtTime", (short)this.hurtTime);
		nBTTagCompound1.setShort("DeathTime", (short)this.deathTime);
		nBTTagCompound1.setShort("AttackTime", (short)this.attackTime);
	}

	public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		this.health = nBTTagCompound1.getShort("Health");
		if(!nBTTagCompound1.hasKey("Health")) {
			this.health = 10;
		}

		this.hurtTime = nBTTagCompound1.getShort("HurtTime");
		this.deathTime = nBTTagCompound1.getShort("DeathTime");
		this.attackTime = nBTTagCompound1.getShort("AttackTime");
	}

	public boolean isEntityAlive() {
		return !this.isDead && this.health > 0;
	}

	public void onLivingUpdate() {
		if(this.newPosRotationIncrements > 0) {
			double d1 = this.posX + (this.newPosX - this.posX) / (double)this.newPosRotationIncrements;
			double d3 = this.posY + (this.newPosY - this.posY) / (double)this.newPosRotationIncrements;
			double d5 = this.posZ + (this.newPosZ - this.posZ) / (double)this.newPosRotationIncrements;

			double d7;
			for(d7 = this.newRotationYaw - (double)this.rotationYaw; d7 < -180.0D; d7 += 360.0D) {
			}

			while(d7 >= 180.0D) {
				d7 -= 360.0D;
			}

			this.rotationYaw = (float)((double)this.rotationYaw + d7 / (double)this.newPosRotationIncrements);
			this.rotationPitch = (float)((double)this.rotationPitch + (this.newRotationPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
			--this.newPosRotationIncrements;
			this.setPosition(d1, d3, d5);
			this.setRotation(this.rotationYaw, this.rotationPitch);
		}

		if(this.health <= 0) {
			this.isJumping = false;
			this.moveStrafing = 0.0F;
			this.moveForward = 0.0F;
			this.randomYawVelocity = 0.0F;
		} else if(!this.isAIEnabled) {
			this.updateEntityActionState();
		}

		boolean z9 = this.handleWaterMovement();
		boolean z2 = this.handleLavaMovement();
		if(this.isJumping) {
			if(z9) {
				this.motionY += (double)0.04F;
			} else if(z2) {
				this.motionY += (double)0.04F;
			} else if(this.onGround) {
				this.E();
			}
		}

		this.moveStrafing *= 0.98F;
		this.moveForward *= 0.98F;
		this.randomYawVelocity *= 0.9F;
		this.handleMovement(this.moveStrafing, this.moveForward);
		List list10 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand((double)0.2F, 0.0D, (double)0.2F));
		if(list10 != null && list10.size() > 0) {
			for(int i4 = 0; i4 < list10.size(); ++i4) {
				Entity entity11 = (Entity)list10.get(i4);
				if(entity11.canBePushed()) {
					entity11.applyEntityCollision(this);
				}
			}
		}

	}

	protected void E() {
		this.motionY = (double)0.42F;
	}

	protected void updateEntityActionState() {
		++this.entityAge;
		EntityPlayer entityPlayer1 = this.worldObj.getClosestPlayerToEntity(this, -1.0D);
		if(entityPlayer1 != null) {
			double d2 = entityPlayer1.posX - this.posX;
			double d4 = entityPlayer1.posY - this.posY;
			double d6 = entityPlayer1.posZ - this.posZ;
			double d8 = d2 * d2 + d4 * d4 + d6 * d6;
			if(d8 > 16384.0D) {
				this.setEntityDead();
			}

			if(this.entityAge > 600 && this.rand.nextInt(800) == 0) {
				if(d8 < 1024.0D) {
					this.entityAge = 0;
				} else {
					this.setEntityDead();
				}
			}
		}

		this.moveStrafing = 0.0F;
		this.moveForward = 0.0F;
		float f10 = 8.0F;
		if(this.rand.nextFloat() < 0.02F) {
			entityPlayer1 = this.worldObj.getClosestPlayerToEntity(this, (double)f10);
			if(entityPlayer1 != null) {
				this.currentTarget = entityPlayer1;
				this.numTicksToChaseTarget = 10 + this.rand.nextInt(20);
			} else {
				this.randomYawVelocity = (this.rand.nextFloat() - 0.5F) * 20.0F;
			}
		}

		if(this.currentTarget != null) {
			this.faceEntity(this.currentTarget, 10.0F);
			if(this.numTicksToChaseTarget-- <= 0 || this.currentTarget.isDead || this.currentTarget.getDistanceSqToEntity(this) > (double)(f10 * f10)) {
				this.currentTarget = null;
			}
		} else {
			if(this.rand.nextFloat() < 0.05F) {
				this.randomYawVelocity = (this.rand.nextFloat() - 0.5F) * 20.0F;
			}

			this.rotationYaw += this.randomYawVelocity;
			this.rotationPitch = this.defaultPitch;
		}

		boolean z3 = this.handleWaterMovement();
		boolean z11 = this.handleLavaMovement();
		if(z3 || z11) {
			this.isJumping = this.rand.nextFloat() < 0.8F;
		}

	}

	public void faceEntity(Entity entity, float f2) {
		double d3 = entity.posX - this.posX;
		double d7 = entity.posZ - this.posZ;
		double d5;
		if(entity instanceof EntityLiving) {
			EntityLiving entityLiving9 = (EntityLiving)entity;
			d5 = entityLiving9.posY + (double)entityLiving9.getEyeHeight() - (this.posY + (double)this.getEyeHeight());
		} else {
			d5 = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0D - (this.posY + (double)this.getEyeHeight());
		}

		double d13 = (double)MathHelper.sqrt_double(d3 * d3 + d7 * d7);
		float f11 = (float)(Math.atan2(d7, d3) * 180.0D / (double)(float)Math.PI) - 90.0F;
		float f12 = (float)(Math.atan2(d5, d13) * 180.0D / (double)(float)Math.PI);
		this.rotationPitch = this.updateRotation(this.rotationPitch, f12, f2);
		this.rotationYaw = this.updateRotation(this.rotationYaw, f11, f2);
	}

	private float updateRotation(float f1, float f2, float f3) {
		float f4;
		for(f4 = f2 - f1; f4 < -180.0F; f4 += 360.0F) {
		}

		while(f4 >= 180.0F) {
			f4 -= 360.0F;
		}

		if(f4 > f3) {
			f4 = f3;
		}

		if(f4 < -f3) {
			f4 = -f3;
		}

		return f1 + f4;
	}

	public void onEntityDeath() {
	}

	public boolean getCanSpawnHere() {
		return this.worldObj.checkIfAABBIsClear(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.getIsAnyLiquid(this.boundingBox);
	}

	protected void kill() {
		this.attackEntityFrom((Entity)null, 4);
	}

	public Vec3D getPosition(float f1) {
		if(f1 == 1.0F) {
			return Vec3D.createVector(this.posX, this.posY, this.posZ);
		} else {
			double d2 = this.prevPosX + (this.posX - this.prevPosX) * (double)f1;
			double d4 = this.prevPosY + (this.posY - this.prevPosY) * (double)f1;
			double d6 = this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f1;
			return Vec3D.createVector(d2, d4, d6);
		}
	}

	public Vec3D getLook(float f1) {
		float f2;
		float f3;
		float f4;
		float f5;
		if(f1 == 1.0F) {
			f2 = MathHelper.cos(-this.rotationYaw * 0.017453292F - (float)Math.PI);
			f3 = MathHelper.sin(-this.rotationYaw * 0.017453292F - (float)Math.PI);
			f4 = -MathHelper.cos(-this.rotationPitch * 0.017453292F);
			f5 = MathHelper.sin(-this.rotationPitch * 0.017453292F);
			return Vec3D.createVector((double)(f3 * f4), (double)f5, (double)(f2 * f4));
		} else {
			f2 = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * f1;
			f3 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * f1;
			f4 = MathHelper.cos(-f3 * 0.017453292F - (float)Math.PI);
			f5 = MathHelper.sin(-f3 * 0.017453292F - (float)Math.PI);
			float f6 = -MathHelper.cos(-f2 * 0.017453292F);
			float f7 = MathHelper.sin(-f2 * 0.017453292F);
			return Vec3D.createVector((double)(f5 * f6), (double)f7, (double)(f4 * f6));
		}
	}

	public MovingObjectPosition rayTrace(double d1, float f3) {
		Vec3D vec3D4 = this.getPosition(f3);
		Vec3D vec3D5 = this.getLook(f3);
		Vec3D vec3D6 = vec3D4.addVector(vec3D5.xCoord * d1, vec3D5.yCoord * d1, vec3D5.zCoord * d1);
		return this.worldObj.rayTraceBlocks(vec3D4, vec3D6);
	}
}
