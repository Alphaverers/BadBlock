package net.minecraft.src;

import java.util.List;
import java.util.Random;

public abstract class Entity {
	private static int nextEntityID = 0;
	public int entityID = nextEntityID++;
	public double renderDistanceWeight = 1.0D;
	public boolean preventEntitySpawning = false;
	public Entity riddenByEntity;
	public Entity ridingEntity;
	protected World worldObj;
	public double prevPosX;
	public double prevPosY;
	public double prevPosZ;
	public double posX;
	public double posY;
	public double posZ;
	public double motionX;
	public double motionY;
	public double motionZ;
	public float rotationYaw;
	public float rotationPitch;
	public float prevRotationYaw;
	public float prevRotationPitch;
	public final AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
	public boolean onGround = false;
	public boolean isCollidedHorizontally;
	public boolean isCollidedVertically;
	public boolean isCollided = false;
	public boolean surfaceCollision = true;
	public boolean isDead = false;
	public float yOffset = 0.0F;
	public float width = 0.6F;
	public float height = 1.8F;
	public float prevDistanceWalkedModified = 0.0F;
	public float distanceWalkedModified = 0.0F;
	protected boolean canTriggerWalking = true;
	protected float fallDistance = 0.0F;
	private int nextStepDistance = 1;
	public double lastTickPosX;
	public double lastTickPosY;
	public double lastTickPosZ;
	public float ySize = 0.0F;
	public float stepHeight = 0.0F;
	public boolean noClip = false;
	public float entityCollisionReduction = 0.0F;
	public boolean unusedBool = false;
	protected Random rand = new Random();
	public int ticksExisted = 0;
	public int fireResistance = 1;
	public int fire = 0;
	protected int maxAir = 300;
	protected boolean inWater = false;
	public int heartsLife = 0;
	public int air = 300;
	private boolean firstUpdate = true;
	private double entityRiderPitchDelta;
	private double entityRiderYawDelta;
	public boolean addedToChunk = false;
	public int chunkCoordX;
	public int chunkCoordY;
	public int chunkCoordZ;

	public Entity(World world) {
		this.worldObj = world;
		this.setPosition(0.0D, 0.0D, 0.0D);
	}

	public boolean equals(Object object) {
		return object instanceof Entity ? ((Entity)object).entityID == this.entityID : false;
	}

	public int hashCode() {
		return this.entityID;
	}

	public void setEntityDead() {
		this.isDead = true;
	}

	protected void setSize(float width, float height) {
		this.width = width;
		this.height = height;
	}

	protected void setRotation(float rotationYaw, float rotationPitch) {
		this.rotationYaw = rotationYaw;
		this.rotationPitch = rotationPitch;
	}

	public void setPosition(double posX, double posY, double posZ) {
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		float f7 = this.width / 2.0F;
		float f8 = this.height;
		this.boundingBox.setBounds(posX - (double)f7, posY - (double)this.yOffset + (double)this.ySize, posZ - (double)f7, posX + (double)f7, posY - (double)this.yOffset + (double)this.ySize + (double)f8, posZ + (double)f7);
	}

	public void onUpdate() {
		this.onEntityUpdate();
	}

	public void onEntityUpdate() {
		if(this.ridingEntity != null && this.ridingEntity.isDead) {
			this.ridingEntity = null;
		}

		++this.ticksExisted;
		this.prevDistanceWalkedModified = this.distanceWalkedModified;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.prevRotationPitch = this.rotationPitch;
		this.prevRotationYaw = this.rotationYaw;
		if(this.handleWaterMovement()) {
			if(!this.inWater && !this.firstUpdate) {
				float f1 = MathHelper.sqrt_double(this.motionX * this.motionX * (double)0.2F + this.motionY * this.motionY + this.motionZ * this.motionZ * (double)0.2F) * 0.2F;
				if(f1 > 1.0F) {
					f1 = 1.0F;
				}

				this.worldObj.playSoundAtEntity(this, "random.splash", f1, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
				float f2 = (float)MathHelper.floor_double(this.boundingBox.minY);

				int i3;
				float f4;
				float f5;
				for(i3 = 0; (float)i3 < 1.0F + this.width * 20.0F; ++i3) {
					f4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					f5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					this.worldObj.spawnParticle("bubble", this.posX + (double)f4, (double)(f2 + 1.0F), this.posZ + (double)f5, this.motionX, this.motionY - (double)(this.rand.nextFloat() * 0.2F), this.motionZ);
				}

				for(i3 = 0; (float)i3 < 1.0F + this.width * 20.0F; ++i3) {
					f4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					f5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					this.worldObj.spawnParticle("splash", this.posX + (double)f4, (double)(f2 + 1.0F), this.posZ + (double)f5, this.motionX, this.motionY, this.motionZ);
				}
			}

			this.fallDistance = 0.0F;
			this.inWater = true;
			this.fire = 0;
		} else {
			this.inWater = false;
		}

		if(this.fire > 0) {
			if(this.fire % 20 == 0) {
				this.attackEntityFrom((Entity)null, 1);
			}

			--this.fire;
		}

		if(this.handleLavaMovement()) {
			this.attackEntityFrom((Entity)null, 10);
			this.fire = 600;
		}

		if(this.posY < -64.0D) {
			this.kill();
		}

		this.firstUpdate = false;
	}

	protected void kill() {
		this.setEntityDead();
	}

	public boolean isOffsetPositionInLiquid(double x, double y, double z) {
		AxisAlignedBB axisAlignedBB7 = this.boundingBox.getOffsetBoundingBox(x, y, z);
		List list8 = this.worldObj.getCollidingBoundingBoxes(this, axisAlignedBB7);
		return list8.size() > 0 ? false : !this.worldObj.getIsAnyLiquid(axisAlignedBB7);
	}

	public void moveEntity(double x, double y, double z) {
		if(this.noClip) {
			this.boundingBox.offset(x, y, z);
			this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
			this.posY = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
			this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
		} else {
			double d7 = this.posX;
			double d9 = this.posZ;
			double d11 = x;
			double d13 = y;
			double d15 = z;
			AxisAlignedBB axisAlignedBB17 = this.boundingBox.copy();
			List list18 = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.addCoord(x, y, z));

			for(int i19 = 0; i19 < list18.size(); ++i19) {
				y = ((AxisAlignedBB)list18.get(i19)).calculateYOffset(this.boundingBox, y);
			}

			this.boundingBox.offset(0.0D, y, 0.0D);
			if(!this.surfaceCollision && d13 != y) {
				z = 0.0D;
				y = 0.0D;
				x = 0.0D;
			}

			boolean z34 = this.onGround || d13 != y && d13 < 0.0D;

			int i20;
			for(i20 = 0; i20 < list18.size(); ++i20) {
				x = ((AxisAlignedBB)list18.get(i20)).calculateXOffset(this.boundingBox, x);
			}

			this.boundingBox.offset(x, 0.0D, 0.0D);
			if(!this.surfaceCollision && d11 != x) {
				z = 0.0D;
				y = 0.0D;
				x = 0.0D;
			}

			for(i20 = 0; i20 < list18.size(); ++i20) {
				z = ((AxisAlignedBB)list18.get(i20)).calculateZOffset(this.boundingBox, z);
			}

			this.boundingBox.offset(0.0D, 0.0D, z);
			if(!this.surfaceCollision && d15 != z) {
				z = 0.0D;
				y = 0.0D;
				x = 0.0D;
			}

			double d22;
			int i27;
			double d35;
			if(this.stepHeight > 0.0F && z34 && this.ySize < 0.05F && (d11 != x || d15 != z)) {
				d35 = x;
				d22 = y;
				double d24 = z;
				x = d11;
				y = (double)this.stepHeight;
				z = d15;
				AxisAlignedBB axisAlignedBB26 = this.boundingBox.copy();
				this.boundingBox.setBB(axisAlignedBB17);
				list18 = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.addCoord(d11, y, d15));

				for(i27 = 0; i27 < list18.size(); ++i27) {
					y = ((AxisAlignedBB)list18.get(i27)).calculateYOffset(this.boundingBox, y);
				}

				this.boundingBox.offset(0.0D, y, 0.0D);
				if(!this.surfaceCollision && d13 != y) {
					z = 0.0D;
					y = 0.0D;
					x = 0.0D;
				}

				for(i27 = 0; i27 < list18.size(); ++i27) {
					x = ((AxisAlignedBB)list18.get(i27)).calculateXOffset(this.boundingBox, x);
				}

				this.boundingBox.offset(x, 0.0D, 0.0D);
				if(!this.surfaceCollision && d11 != x) {
					z = 0.0D;
					y = 0.0D;
					x = 0.0D;
				}

				for(i27 = 0; i27 < list18.size(); ++i27) {
					z = ((AxisAlignedBB)list18.get(i27)).calculateZOffset(this.boundingBox, z);
				}

				this.boundingBox.offset(0.0D, 0.0D, z);
				if(!this.surfaceCollision && d15 != z) {
					z = 0.0D;
					y = 0.0D;
					x = 0.0D;
				}

				if(d35 * d35 + d24 * d24 >= x * x + z * z) {
					x = d35;
					y = d22;
					z = d24;
					this.boundingBox.setBB(axisAlignedBB26);
				} else {
					this.ySize = (float)((double)this.ySize + 0.5D);
				}
			}

			this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
			this.posY = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
			this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
			this.isCollidedHorizontally = d11 != x || d15 != z;
			this.isCollidedVertically = d13 != y;
			this.onGround = d13 != y && d13 < 0.0D;
			this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
			if(this.onGround) {
				if(this.fallDistance > 0.0F) {
					this.fall(this.fallDistance);
					this.fallDistance = 0.0F;
				}
			} else if(y < 0.0D) {
				this.fallDistance = (float)((double)this.fallDistance - y);
			}

			if(d11 != x) {
				this.motionX = 0.0D;
			}

			if(d13 != y) {
				this.motionY = 0.0D;
			}

			if(d15 != z) {
				this.motionZ = 0.0D;
			}

			d35 = this.posX - d7;
			d22 = this.posZ - d9;
			this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt_double(d35 * d35 + d22 * d22) * 0.6D);
			int i25;
			int i36;
			int i38;
			if(this.canTriggerWalking) {
				i36 = MathHelper.floor_double(this.posX);
				i25 = MathHelper.floor_double(this.posY - (double)0.2F - (double)this.yOffset);
				i38 = MathHelper.floor_double(this.posZ);
				i27 = this.worldObj.getBlockId(i36, i25, i38);
				if(this.distanceWalkedModified > (float)this.nextStepDistance && i27 > 0) {
					++this.nextStepDistance;
					StepSound stepSound28 = Block.blockList[i27].stepSound;
					if(this.worldObj.getBlockId(i36, i25 + 1, i38) == Block.snow.blockID) {
						stepSound28 = Block.snow.stepSound;
						this.worldObj.playSoundAtEntity(this, stepSound28.getStepSound(), stepSound28.getVolume() * 0.15F, stepSound28.getPitch());
					} else if(!Block.blockList[i27].material.getIsLiquid()) {
						this.worldObj.playSoundAtEntity(this, stepSound28.getStepSound(), stepSound28.getVolume() * 0.15F, stepSound28.getPitch());
					}

					Block.blockList[i27].onEntityWalking(this.worldObj, i36, i25, i38, this);
				}
			}

			i36 = MathHelper.floor_double(this.boundingBox.minX);
			i25 = MathHelper.floor_double(this.boundingBox.minY);
			i38 = MathHelper.floor_double(this.boundingBox.minZ);
			i27 = MathHelper.floor_double(this.boundingBox.maxX);
			int i39 = MathHelper.floor_double(this.boundingBox.maxY);
			int i29 = MathHelper.floor_double(this.boundingBox.maxZ);

			for(int i30 = i36; i30 <= i27; ++i30) {
				for(int i31 = i25; i31 <= i39; ++i31) {
					for(int i32 = i38; i32 <= i29; ++i32) {
						int i33 = this.worldObj.getBlockId(i30, i31, i32);
						if(i33 > 0) {
							Block.blockList[i33].onEntityCollidedWithBlock(this.worldObj, i30, i31, i32, this);
						}
					}
				}
			}

			this.ySize *= 0.4F;
			boolean z37 = this.handleWaterMovement();
			if(this.worldObj.isBoundingBoxBurning(this.boundingBox)) {
				this.dealFireDamage(1);
				if(!z37) {
					++this.fire;
					if(this.fire == 0) {
						this.fire = 300;
					}
				}
			} else if(this.fire <= 0) {
				this.fire = -this.fireResistance;
			}

			if(z37 && this.fire > 0) {
				this.worldObj.playSoundAtEntity(this, "random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
				this.fire = -this.fireResistance;
			}

		}
	}

	public AxisAlignedBB getBoundingBox() {
		return null;
	}

	protected void dealFireDamage(int fireDamage) {
		this.attackEntityFrom((Entity)null, fireDamage);
	}

	protected void fall(float f1) {
	}

	public boolean handleWaterMovement() {
		return this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0D, -0.4000000059604645D, 0.0D), Material.water, this);
	}

	public boolean isInsideOfMaterial(Material material) {
		double d2 = this.posY + (double)this.getEyeHeight();
		int i4 = MathHelper.floor_double(this.posX);
		int i5 = MathHelper.floor_float((float)MathHelper.floor_double(d2));
		int i6 = MathHelper.floor_double(this.posZ);
		int i7 = this.worldObj.getBlockId(i4, i5, i6);
		if(i7 != 0 && Block.blockList[i7].material == material) {
			float f8 = BlockFluid.getFluidHeightPercent(this.worldObj.getBlockMetadata(i4, i5, i6)) - 0.11111111F;
			float f9 = (float)(i5 + 1) - f8;
			return d2 < (double)f9;
		} else {
			return false;
		}
	}

	protected float getEyeHeight() {
		return 0.0F;
	}

	public boolean handleLavaMovement() {
		return this.worldObj.isMaterialInBB(this.boundingBox.expand(0.0D, -0.4000000059604645D, 0.0D), Material.lava);
	}

	public void moveFlying(float f1, float f2, float f3) {
		float f4 = MathHelper.sqrt_float(f1 * f1 + f2 * f2);
		if(f4 >= 0.01F) {
			if(f4 < 1.0F) {
				f4 = 1.0F;
			}

			f4 = f3 / f4;
			f1 *= f4;
			f2 *= f4;
			float f5 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
			float f6 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
			this.motionX += (double)(f1 * f6 - f2 * f5);
			this.motionZ += (double)(f2 * f6 + f1 * f5);
		}
	}

	public float getBrightness(float unused) {
		int i2 = MathHelper.floor_double(this.posX);
		double d3 = (this.boundingBox.maxY - this.boundingBox.minY) * 0.66D;
		int i5 = MathHelper.floor_double(this.posY - (double)this.yOffset + d3);
		int i6 = MathHelper.floor_double(this.posZ);
		return this.worldObj.getBrightness(i2, i5, i6);
	}

	public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
		this.prevPosX = this.posX = x;
		this.prevPosY = this.posY = y;
		this.prevPosZ = this.posZ = z;
		this.rotationYaw = yaw;
		this.rotationPitch = pitch;
		this.ySize = 0.0F;
		double d9 = (double)(this.prevRotationYaw - yaw);
		if(d9 < -180.0D) {
			this.prevRotationYaw += 360.0F;
		}

		if(d9 >= 180.0D) {
			this.prevRotationYaw -= 360.0F;
		}

		this.setPosition(this.posX, this.posY, this.posZ);
	}

	public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
		this.prevPosX = this.posX = x;
		this.prevPosY = this.posY = y + (double)this.yOffset;
		this.prevPosZ = this.posZ = z;
		this.rotationYaw = yaw;
		this.rotationPitch = pitch;
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	public float getDistanceToEntity(Entity entity) {
		float f2 = (float)(this.posX - entity.posX);
		float f3 = (float)(this.posY - entity.posY);
		float f4 = (float)(this.posZ - entity.posZ);
		return MathHelper.sqrt_float(f2 * f2 + f3 * f3 + f4 * f4);
	}

	public double getDistanceSq(double x, double y, double z) {
		double d7 = this.posX - x;
		double d9 = this.posY - y;
		double d11 = this.posZ - z;
		return d7 * d7 + d9 * d9 + d11 * d11;
	}

	public double getDistance(double x, double y, double z) {
		double d7 = this.posX - x;
		double d9 = this.posY - y;
		double d11 = this.posZ - z;
		return (double)MathHelper.sqrt_double(d7 * d7 + d9 * d9 + d11 * d11);
	}

	public double getDistanceSqToEntity(Entity entity) {
		double d2 = this.posX - entity.posX;
		double d4 = this.posY - entity.posY;
		double d6 = this.posZ - entity.posZ;
		return d2 * d2 + d4 * d4 + d6 * d6;
	}

	public void onCollideWithPlayer(EntityPlayer entityPlayer) {
	}

	public void applyEntityCollision(Entity entity) {
		if(entity.riddenByEntity != this && entity.ridingEntity != this) {
			double d2 = entity.posX - this.posX;
			double d4 = entity.posZ - this.posZ;
			double d6 = MathHelper.abs_max(d2, d4);
			if(d6 >= (double)0.01F) {
				d6 = (double)MathHelper.sqrt_double(d6);
				d2 /= d6;
				d4 /= d6;
				double d8 = 1.0D / d6;
				if(d8 > 1.0D) {
					d8 = 1.0D;
				}

				d2 *= d8;
				d4 *= d8;
				d2 *= (double)0.05F;
				d4 *= (double)0.05F;
				d2 *= (double)(1.0F - this.entityCollisionReduction);
				d4 *= (double)(1.0F - this.entityCollisionReduction);
				this.addVelocity(-d2, 0.0D, -d4);
				entity.addVelocity(d2, 0.0D, d4);
			}

		}
	}

	public void addVelocity(double motionX, double motionY, double motionZ) {
		this.motionX += motionX;
		this.motionY += motionY;
		this.motionZ += motionZ;
	}

	public boolean attackEntityFrom(Entity entity, int damage) {
		return false;
	}

	public boolean canBeCollidedWith() {
		return false;
	}

	public boolean canBePushed() {
		return false;
	}

	public void addToPlayerScore(Entity entity, int score) {
	}

	public boolean addEntityID(NBTTagCompound nbttagcompound) {
		String string2 = this.getEntityString();
		if(!this.isDead && string2 != null) {
			nbttagcompound.setString("id", string2);
			this.writeToNBT(nbttagcompound);
			return true;
		} else {
			return false;
		}
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setTag("Pos", this.newDoubleNBTList(new double[]{this.posX, this.posY, this.posZ}));
		nbttagcompound.setTag("Motion", this.newDoubleNBTList(new double[]{this.motionX, this.motionY, this.motionZ}));
		nbttagcompound.setTag("Rotation", this.newFloatNBTList(new float[]{this.rotationYaw, this.rotationPitch}));
		nbttagcompound.setFloat("FallDistance", this.fallDistance);
		nbttagcompound.setShort("Fire", (short)this.fire);
		nbttagcompound.setShort("Air", (short)this.air);
		nbttagcompound.setBoolean("OnGround", this.onGround);
		this.writeEntityToNBT(nbttagcompound);
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		NBTTagList nBTTagList2 = nbttagcompound.getTagList("Pos");
		NBTTagList nBTTagList3 = nbttagcompound.getTagList("Motion");
		NBTTagList nBTTagList4 = nbttagcompound.getTagList("Rotation");
		this.setPosition(0.0D, 0.0D, 0.0D);
		this.motionX = ((NBTTagDouble)nBTTagList3.entities(0)).doubleValue;
		this.motionY = ((NBTTagDouble)nBTTagList3.entities(1)).doubleValue;
		this.motionZ = ((NBTTagDouble)nBTTagList3.entities(2)).doubleValue;
		this.prevPosX = this.lastTickPosX = this.posX = ((NBTTagDouble)nBTTagList2.entities(0)).doubleValue;
		this.prevPosY = this.lastTickPosY = this.posY = ((NBTTagDouble)nBTTagList2.entities(1)).doubleValue;
		this.prevPosZ = this.lastTickPosZ = this.posZ = ((NBTTagDouble)nBTTagList2.entities(2)).doubleValue;
		this.prevRotationYaw = this.rotationYaw = ((NBTTagFloat)nBTTagList4.entities(0)).floatValue;
		this.prevRotationPitch = this.rotationPitch = ((NBTTagFloat)nBTTagList4.entities(1)).floatValue;
		this.fallDistance = nbttagcompound.getFloat("FallDistance");
		this.fire = nbttagcompound.getShort("Fire");
		this.air = nbttagcompound.getShort("Air");
		this.onGround = nbttagcompound.getBoolean("OnGround");
		this.setPosition(this.posX, this.posY, this.posZ);
		this.readEntityFromNBT(nbttagcompound);
	}

	protected final String getEntityString() {
		return EntityList.getEntityString(this);
	}

	protected abstract void readEntityFromNBT(NBTTagCompound nBTTagCompound1);

	protected abstract void writeEntityToNBT(NBTTagCompound nBTTagCompound1);

	protected NBTTagList newDoubleNBTList(double... d1) {
		NBTTagList nBTTagList2 = new NBTTagList();
		double[] d3 = d1;
		int i4 = d1.length;

		for(int i5 = 0; i5 < i4; ++i5) {
			double d6 = d3[i5];
			nBTTagList2.setTag(new NBTTagDouble(d6));
		}

		return nBTTagList2;
	}

	protected NBTTagList newFloatNBTList(float... f1) {
		NBTTagList nBTTagList2 = new NBTTagList();
		float[] f3 = f1;
		int i4 = f1.length;

		for(int i5 = 0; i5 < i4; ++i5) {
			float f6 = f3[i5];
			nBTTagList2.setTag(new NBTTagFloat(f6));
		}

		return nBTTagList2;
	}

	public EntityItem dropItem(int itemID, int count) {
		return this.entityDropItem(itemID, count, 0.0F);
	}

	public EntityItem entityDropItem(int itemID, int count, float velocity) {
		EntityItem entityItem4 = new EntityItem(this.worldObj, this.posX, this.posY + (double)velocity, this.posZ, new ItemStack(itemID, count));
		entityItem4.delayBeforeCanPickup = 10;
		this.worldObj.spawnEntityInWorld(entityItem4);
		return entityItem4;
	}

	public boolean isEntityAlive() {
		return !this.isDead;
	}

	public boolean isEntityInsideOpaqueBlock() {
		int i1 = MathHelper.floor_double(this.posX);
		int i2 = MathHelper.floor_double(this.posY + (double)this.getEyeHeight());
		int i3 = MathHelper.floor_double(this.posZ);
		return this.worldObj.isBlockNormalCube(i1, i2, i3);
	}

	public AxisAlignedBB getCollisionBox(Entity entity) {
		return null;
	}

	public void updateRidden() {
		if(this.ridingEntity.isDead) {
			this.ridingEntity = null;
		} else {
			this.motionX = 0.0D;
			this.motionY = 0.0D;
			this.motionZ = 0.0D;
			this.onUpdate();
			this.ridingEntity.updateRiderPosition();
			this.entityRiderYawDelta += (double)(this.ridingEntity.rotationYaw - this.ridingEntity.prevRotationYaw);

			for(this.entityRiderPitchDelta += (double)(this.ridingEntity.rotationPitch - this.ridingEntity.prevRotationPitch); this.entityRiderYawDelta >= 180.0D; this.entityRiderYawDelta -= 360.0D) {
			}

			while(this.entityRiderYawDelta < -180.0D) {
				this.entityRiderYawDelta += 360.0D;
			}

			while(this.entityRiderPitchDelta >= 180.0D) {
				this.entityRiderPitchDelta -= 360.0D;
			}

			while(this.entityRiderPitchDelta < -180.0D) {
				this.entityRiderPitchDelta += 360.0D;
			}

			double d1 = this.entityRiderYawDelta * 0.5D;
			double d3 = this.entityRiderPitchDelta * 0.5D;
			float f5 = 10.0F;
			if(d1 > (double)f5) {
				d1 = (double)f5;
			}

			if(d1 < (double)(-f5)) {
				d1 = (double)(-f5);
			}

			if(d3 > (double)f5) {
				d3 = (double)f5;
			}

			if(d3 < (double)(-f5)) {
				d3 = (double)(-f5);
			}

			this.entityRiderYawDelta -= d1;
			this.entityRiderPitchDelta -= d3;
			this.rotationYaw = (float)((double)this.rotationYaw + d1);
			this.rotationPitch = (float)((double)this.rotationPitch + d3);
		}
	}

	protected void updateRiderPosition() {
		this.riddenByEntity.setPosition(this.posX, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ);
	}

	public double getYOffset() {
		return (double)this.yOffset;
	}

	public double getMountedYOffset() {
		return (double)this.height * 0.75D;
	}

	public void mountEntity(Entity entity) {
		this.entityRiderPitchDelta = 0.0D;
		this.entityRiderYawDelta = 0.0D;
		if(this.ridingEntity == entity) {
			this.ridingEntity.riddenByEntity = null;
			this.ridingEntity = null;
			this.setLocationAndAngles(entity.posX, entity.boundingBox.minY + (double)entity.height, entity.posZ, this.rotationYaw, this.rotationPitch);
		} else {
			if(this.ridingEntity != null) {
				this.ridingEntity.riddenByEntity = null;
			}

			if(entity.riddenByEntity != null) {
				entity.riddenByEntity.ridingEntity = null;
			}

			this.ridingEntity = entity;
			entity.riddenByEntity = this;
		}
	}
}
