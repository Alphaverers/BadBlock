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
	public String skinUrl;
	private double entityRiderPitchDelta;
	private double entityRiderYawDelta;
	public boolean addedToChunk = false;
	public int chunkCoordX;
	public int chunkCoordY;
	public int chunkCoordZ;
	public int serverPosX;
	public int serverPosY;
	public int serverPosZ;

	public Entity(World world) {
		this.worldObj = world;
		this.setPosition(0.0D, 0.0D, 0.0D);
	}

	public boolean equals(Object entity) {
		return entity instanceof Entity ? ((Entity)entity).entityID == this.entityID : false;
	}

	public int hashCode() {
		return this.entityID;
	}

	protected void preparePlayerToSpawn() {
		if(this.worldObj != null) {
			while(this.posY > 0.0D) {
				this.setPosition(this.posX, this.posY, this.posZ);
				if(this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0) {
					break;
				}

				++this.posY;
			}

			this.motionX = this.motionY = this.motionZ = 0.0D;
			this.rotationPitch = 0.0F;
		}
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

	public void setAngles(float yaw, float pitch) {
		float f3 = this.rotationPitch;
		float f4 = this.rotationYaw;
		this.rotationYaw = (float)((double)this.rotationYaw + (double)yaw * 0.15D);
		this.rotationPitch = (float)((double)this.rotationPitch - (double)pitch * 0.15D);
		if(this.rotationPitch < -90.0F) {
			this.rotationPitch = -90.0F;
		}

		if(this.rotationPitch > 90.0F) {
			this.rotationPitch = 90.0F;
		}

		this.prevRotationPitch += this.rotationPitch - f3;
		this.prevRotationYaw += this.rotationYaw - f4;
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

	public boolean isOffsetPositionInLiquid(double offsetX, double offsetY, double offsetZ) {
		AxisAlignedBB axisAlignedBB7 = this.boundingBox.getOffsetBoundingBox(offsetX, offsetY, offsetZ);
		List list8 = this.worldObj.getCollidingBoundingBoxes(this, axisAlignedBB7);
		return list8.size() > 0 ? false : !this.worldObj.getIsAnyLiquid(axisAlignedBB7);
	}

	public void moveEntity(double moveX, double moveY, double moveZ) {
		if(this.noClip) {
			this.boundingBox.offset(moveX, moveY, moveZ);
			this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
			this.posY = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
			this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
		} else {
			double d7 = this.posX;
			double d9 = this.posZ;
			double d11 = moveX;
			double d13 = moveY;
			double d15 = moveZ;
			AxisAlignedBB axisAlignedBB17 = this.boundingBox.copy();
			List list18 = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.addCoord(moveX, moveY, moveZ));

			for(int i19 = 0; i19 < list18.size(); ++i19) {
				moveY = ((AxisAlignedBB)list18.get(i19)).calculateYOffset(this.boundingBox, moveY);
			}

			this.boundingBox.offset(0.0D, moveY, 0.0D);
			if(!this.surfaceCollision && d13 != moveY) {
				moveZ = 0.0D;
				moveY = 0.0D;
				moveX = 0.0D;
			}

			boolean z34 = this.onGround || d13 != moveY && d13 < 0.0D;

			int i20;
			for(i20 = 0; i20 < list18.size(); ++i20) {
				moveX = ((AxisAlignedBB)list18.get(i20)).calculateXOffset(this.boundingBox, moveX);
			}

			this.boundingBox.offset(moveX, 0.0D, 0.0D);
			if(!this.surfaceCollision && d11 != moveX) {
				moveZ = 0.0D;
				moveY = 0.0D;
				moveX = 0.0D;
			}

			for(i20 = 0; i20 < list18.size(); ++i20) {
				moveZ = ((AxisAlignedBB)list18.get(i20)).calculateZOffset(this.boundingBox, moveZ);
			}

			this.boundingBox.offset(0.0D, 0.0D, moveZ);
			if(!this.surfaceCollision && d15 != moveZ) {
				moveZ = 0.0D;
				moveY = 0.0D;
				moveX = 0.0D;
			}

			double d22;
			int i27;
			double d35;
			if(this.stepHeight > 0.0F && z34 && this.ySize < 0.05F && (d11 != moveX || d15 != moveZ)) {
				d35 = moveX;
				d22 = moveY;
				double d24 = moveZ;
				moveX = d11;
				moveY = (double)this.stepHeight;
				moveZ = d15;
				AxisAlignedBB axisAlignedBB26 = this.boundingBox.copy();
				this.boundingBox.setBB(axisAlignedBB17);
				list18 = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.addCoord(d11, moveY, d15));

				for(i27 = 0; i27 < list18.size(); ++i27) {
					moveY = ((AxisAlignedBB)list18.get(i27)).calculateYOffset(this.boundingBox, moveY);
				}

				this.boundingBox.offset(0.0D, moveY, 0.0D);
				if(!this.surfaceCollision && d13 != moveY) {
					moveZ = 0.0D;
					moveY = 0.0D;
					moveX = 0.0D;
				}

				for(i27 = 0; i27 < list18.size(); ++i27) {
					moveX = ((AxisAlignedBB)list18.get(i27)).calculateXOffset(this.boundingBox, moveX);
				}

				this.boundingBox.offset(moveX, 0.0D, 0.0D);
				if(!this.surfaceCollision && d11 != moveX) {
					moveZ = 0.0D;
					moveY = 0.0D;
					moveX = 0.0D;
				}

				for(i27 = 0; i27 < list18.size(); ++i27) {
					moveZ = ((AxisAlignedBB)list18.get(i27)).calculateZOffset(this.boundingBox, moveZ);
				}

				this.boundingBox.offset(0.0D, 0.0D, moveZ);
				if(!this.surfaceCollision && d15 != moveZ) {
					moveZ = 0.0D;
					moveY = 0.0D;
					moveX = 0.0D;
				}

				if(d35 * d35 + d24 * d24 >= moveX * moveX + moveZ * moveZ) {
					moveX = d35;
					moveY = d22;
					moveZ = d24;
					this.boundingBox.setBB(axisAlignedBB26);
				} else {
					this.ySize = (float)((double)this.ySize + 0.5D);
				}
			}

			this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
			this.posY = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
			this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
			this.isCollidedHorizontally = d11 != moveX || d15 != moveZ;
			this.isCollidedVertically = d13 != moveY;
			this.onGround = d13 != moveY && d13 < 0.0D;
			this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
			if(this.onGround) {
				if(this.fallDistance > 0.0F) {
					this.fall(this.fallDistance);
					this.fallDistance = 0.0F;
				}
			} else if(moveY < 0.0D) {
				this.fallDistance = (float)((double)this.fallDistance - moveY);
			}

			if(d11 != moveX) {
				this.motionX = 0.0D;
			}

			if(d13 != moveY) {
				this.motionY = 0.0D;
			}

			if(d15 != moveZ) {
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
					StepSound stepSound28 = Block.blocksList[i27].stepSound;
					if(this.worldObj.getBlockId(i36, i25 + 1, i38) == Block.snow.blockID) {
						stepSound28 = Block.snow.stepSound;
						this.worldObj.playSoundAtEntity(this, stepSound28.getStepSound(), stepSound28.getVolume() * 0.15F, stepSound28.getPitch());
					} else if(!Block.blocksList[i27].material.getIsLiquid()) {
						this.worldObj.playSoundAtEntity(this, stepSound28.getStepSound(), stepSound28.getVolume() * 0.15F, stepSound28.getPitch());
					}

					Block.blocksList[i27].onEntityWalking(this.worldObj, i36, i25, i38, this);
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
							Block.blocksList[i33].onEntityCollidedWithBlock(this.worldObj, i30, i31, i32, this);
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

	protected void dealFireDamage(int damage) {
		this.attackEntityFrom((Entity)null, damage);
	}

	protected void fall(float distance) {
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
		if(i7 != 0 && Block.blocksList[i7].material == material) {
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

	public void moveFlying(float moveX, float moveZ, float moveY) {
		float f4 = MathHelper.sqrt_float(moveX * moveX + moveZ * moveZ);
		if(f4 >= 0.01F) {
			if(f4 < 1.0F) {
				f4 = 1.0F;
			}

			f4 = moveY / f4;
			moveX *= f4;
			moveZ *= f4;
			float f5 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
			float f6 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
			this.motionX += (double)(moveX * f6 - moveZ * f5);
			this.motionZ += (double)(moveZ * f6 + moveX * f5);
		}
	}

	public float getBrightness(float renderPartialTick) {
		int i2 = MathHelper.floor_double(this.posX);
		double d3 = (this.boundingBox.maxY - this.boundingBox.minY) * 0.66D;
		int i5 = MathHelper.floor_double(this.posY - (double)this.yOffset + d3);
		int i6 = MathHelper.floor_double(this.posZ);
		return this.worldObj.getBrightness(i2, i5, i6);
	}

	public void setWorld(World world) {
		this.worldObj = world;
	}

	public void setPositionAndRotation(double posX, double posY, double posZ, float rotationYaw, float rotationPitch) {
		this.prevPosX = this.posX = posX;
		this.prevPosY = this.posY = posY;
		this.prevPosZ = this.posZ = posZ;
		this.rotationYaw = rotationYaw;
		this.rotationPitch = rotationPitch;
		this.ySize = 0.0F;
		double d9 = (double)(this.prevRotationYaw - rotationYaw);
		if(d9 < -180.0D) {
			this.prevRotationYaw += 360.0F;
		}

		if(d9 >= 180.0D) {
			this.prevRotationYaw -= 360.0F;
		}

		this.setPosition(this.posX, this.posY, this.posZ);
	}

	public void setLocationAndAngles(double posX, double posY, double posZ, float rotationYaw, float rotationPitch) {
		this.prevPosX = this.posX = posX;
		this.prevPosY = this.posY = posY + (double)this.yOffset;
		this.prevPosZ = this.posZ = posZ;
		this.rotationYaw = rotationYaw;
		this.rotationPitch = rotationPitch;
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

	public boolean isInRangeToRenderVec3D(Vec3D vector) {
		double d2 = this.posX - vector.xCoord;
		double d4 = this.posY - vector.yCoord;
		double d6 = this.posZ - vector.zCoord;
		double d8 = d2 * d2 + d4 * d4 + d6 * d6;
		return this.isInRangeToRenderDist(d8);
	}

	public boolean isInRangeToRenderDist(double distance) {
		double d3 = this.boundingBox.getAverageEdgeLength();
		d3 *= 64.0D * this.renderDistanceWeight;
		return distance < d3 * d3;
	}

	public String getTexture() {
		return null;
	}

	public boolean addEntityID(NBTTagCompound nbtCompound) {
		String string2 = this.getEntityString();
		if(!this.isDead && string2 != null) {
			nbtCompound.setString("id", string2);
			this.writeToNBT(nbtCompound);
			return true;
		} else {
			return false;
		}
	}

	public void writeToNBT(NBTTagCompound nbtCompound) {
		nbtCompound.setTag("Pos", this.newDoubleNBTList(new double[]{this.posX, this.posY, this.posZ}));
		nbtCompound.setTag("Motion", this.newDoubleNBTList(new double[]{this.motionX, this.motionY, this.motionZ}));
		nbtCompound.setTag("Rotation", this.newFloatNBTList(new float[]{this.rotationYaw, this.rotationPitch}));
		nbtCompound.setFloat("FallDistance", this.fallDistance);
		nbtCompound.setShort("Fire", (short)this.fire);
		nbtCompound.setShort("Air", (short)this.air);
		nbtCompound.setBoolean("OnGround", this.onGround);
		this.writeEntityToNBT(nbtCompound);
	}

	public void readFromNBT(NBTTagCompound nbtCompound) {
		NBTTagList nBTTagList2 = nbtCompound.getTagList("Pos");
		NBTTagList nBTTagList3 = nbtCompound.getTagList("Motion");
		NBTTagList nBTTagList4 = nbtCompound.getTagList("Rotation");
		this.setPosition(0.0D, 0.0D, 0.0D);
		this.motionX = ((NBTTagDouble)nBTTagList3.tagAt(0)).doubleValue;
		this.motionY = ((NBTTagDouble)nBTTagList3.tagAt(1)).doubleValue;
		this.motionZ = ((NBTTagDouble)nBTTagList3.tagAt(2)).doubleValue;
		this.prevPosX = this.lastTickPosX = this.posX = ((NBTTagDouble)nBTTagList2.tagAt(0)).doubleValue;
		this.prevPosY = this.lastTickPosY = this.posY = ((NBTTagDouble)nBTTagList2.tagAt(1)).doubleValue;
		this.prevPosZ = this.lastTickPosZ = this.posZ = ((NBTTagDouble)nBTTagList2.tagAt(2)).doubleValue;
		this.prevRotationYaw = this.rotationYaw = ((NBTTagFloat)nBTTagList4.tagAt(0)).floatValue;
		this.prevRotationPitch = this.rotationPitch = ((NBTTagFloat)nBTTagList4.tagAt(1)).floatValue;
		this.fallDistance = nbtCompound.getFloat("FallDistance");
		this.fire = nbtCompound.getShort("Fire");
		this.air = nbtCompound.getShort("Air");
		this.onGround = nbtCompound.getBoolean("OnGround");
		this.setPosition(this.posX, this.posY, this.posZ);
		this.readEntityFromNBT(nbtCompound);
	}

	protected final String getEntityString() {
		return EntityList.getEntityString(this);
	}

	protected abstract void readEntityFromNBT(NBTTagCompound nBTTagCompound1);

	protected abstract void writeEntityToNBT(NBTTagCompound nBTTagCompound1);

	protected NBTTagList newDoubleNBTList(double... doubleArray) {
		NBTTagList nBTTagList2 = new NBTTagList();
		double[] d3 = doubleArray;
		int i4 = doubleArray.length;

		for(int i5 = 0; i5 < i4; ++i5) {
			double d6 = d3[i5];
			nBTTagList2.setTag(new NBTTagDouble(d6));
		}

		return nBTTagList2;
	}

	protected NBTTagList newFloatNBTList(float... floatArray) {
		NBTTagList nBTTagList2 = new NBTTagList();
		float[] f3 = floatArray;
		int i4 = floatArray.length;

		for(int i5 = 0; i5 < i4; ++i5) {
			float f6 = f3[i5];
			nBTTagList2.setTag(new NBTTagFloat(f6));
		}

		return nBTTagList2;
	}

	public float getShadowSize() {
		return this.height / 2.0F;
	}

	public EntityItem dropItem(int i1, int i2) {
		return this.entityDropItem(i1, i2, 0.0F);
	}

	public EntityItem entityDropItem(int i1, int i2, float f3) {
		EntityItem entityItem4 = new EntityItem(this.worldObj, this.posX, this.posY + (double)f3, this.posZ, new ItemStack(i1, i2));
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

	public boolean interact(EntityPlayer entityPlayer) {
		return false;
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

	public void setPositionAndRotation(double x, double y, double z, float rotationYaw, float rotationPitch, int newPosRotationIncrements) {
		this.setPosition(x, y, z);
		this.setRotation(rotationYaw, rotationPitch);
	}
}
