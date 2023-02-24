package net.minecraft.src;

import java.util.List;

public class EntityMinecart extends Entity implements IInventory {
	private ItemStack[] cargoItems;
	public int damageTaken;
	public int timeSinceHit;
	public int forwardDirection;
	private boolean isInReverse;
	public int minecartType;
	public int fuel;
	public double pushX;
	public double pushZ;
	private static final int[][][] matrix = new int[][][]{{{0, 0, -1}, {0, 0, 1}}, {{-1, 0, 0}, {1, 0, 0}}, {{-1, -1, 0}, {1, 0, 0}}, {{-1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, {-1, 0, 0}}, {{0, 0, -1}, {-1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};
	private int turnProgress;
	private double minecartX;
	private double minecartY;
	private double minecartZ;
	private double minecartYaw;
	private double minecartPitch;

	public EntityMinecart(World world1) {
		super(world1);
		this.cargoItems = new ItemStack[36];
		this.damageTaken = 0;
		this.timeSinceHit = 0;
		this.forwardDirection = 1;
		this.isInReverse = false;
		this.preventEntitySpawning = true;
		this.setSize(0.98F, 0.7F);
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

	public EntityMinecart(World world, double x, double y, double z, int minecartType) {
		this(world);
		this.setPosition(x, y + (double)this.yOffset, z);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
		this.minecartType = minecartType;
	}

	public double getMountedYOffset() {
		return (double)this.height * 0.0D - (double)0.3F;
	}

	public boolean attackEntityFrom(Entity entity1, int i2) {
		this.forwardDirection = -this.forwardDirection;
		this.timeSinceHit = 10;
		this.damageTaken += i2 * 10;
		if(this.damageTaken > 40) {
			this.entityDropItem(Item.minecartEmpty.shiftedIndex, 1, 0.0F);
			if(this.minecartType == 1) {
				this.entityDropItem(Block.crate.blockID, 1, 0.0F);
			} else if(this.minecartType == 2) {
				this.entityDropItem(Block.stoneOvenIdle.blockID, 1, 0.0F);
			}

			this.setEntityDead();
		}

		return true;
	}

	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public void setEntityDead() {
		for(int i1 = 0; i1 < this.getSizeInventory(); ++i1) {
			ItemStack itemStack2 = this.getStackInSlot(i1);
			if(itemStack2 != null) {
				float f3 = this.rand.nextFloat() * 0.8F + 0.1F;
				float f4 = this.rand.nextFloat() * 0.8F + 0.1F;
				float f5 = this.rand.nextFloat() * 0.8F + 0.1F;

				while(itemStack2.stackSize > 0) {
					int i6 = this.rand.nextInt(21) + 10;
					if(i6 > itemStack2.stackSize) {
						i6 = itemStack2.stackSize;
					}

					itemStack2.stackSize -= i6;
					EntityItem entityItem7 = new EntityItem(this.worldObj, this.posX + (double)f3, this.posY + (double)f4, this.posZ + (double)f5, new ItemStack(itemStack2.itemID, i6, itemStack2.itemDmg));
					float f8 = 0.05F;
					entityItem7.motionX = (double)((float)this.rand.nextGaussian() * f8);
					entityItem7.motionY = (double)((float)this.rand.nextGaussian() * f8 + 0.2F);
					entityItem7.motionZ = (double)((float)this.rand.nextGaussian() * f8);
					this.worldObj.spawnEntityInWorld(entityItem7);
				}
			}
		}

		super.setEntityDead();
	}

	public void onUpdate() {
		double d7;
		if(this.worldObj.multiplayerWorld) {
			if(this.turnProgress > 0) {
				double d41 = this.posX + (this.minecartX - this.posX) / (double)this.turnProgress;
				double d42 = this.posY + (this.minecartY - this.posY) / (double)this.turnProgress;
				double d5 = this.posZ + (this.minecartZ - this.posZ) / (double)this.turnProgress;

				for(d7 = this.minecartYaw - (double)this.rotationYaw; d7 < -180.0D; d7 += 360.0D) {
				}

				while(d7 >= 180.0D) {
					d7 -= 360.0D;
				}

				this.rotationYaw = (float)((double)this.rotationYaw + d7 / (double)this.turnProgress);
				this.rotationPitch = (float)((double)this.rotationPitch + (this.minecartPitch - (double)this.rotationPitch) / (double)this.turnProgress);
				--this.turnProgress;
				this.setPosition(d41, d42, d5);
				this.setRotation(this.rotationYaw, this.rotationPitch);
			} else {
				this.setPosition(this.posX, this.posY, this.posZ);
				this.setRotation(this.rotationYaw, this.rotationPitch);
			}

		} else {
			if(this.timeSinceHit > 0) {
				--this.timeSinceHit;
			}

			if(this.damageTaken > 0) {
				--this.damageTaken;
			}

			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;
			this.motionY -= (double)0.04F;
			int i1 = MathHelper.floor_double(this.posX);
			int i2 = MathHelper.floor_double(this.posY);
			int i3 = MathHelper.floor_double(this.posZ);
			if(this.worldObj.getBlockId(i1, i2 - 1, i3) == Block.minecartTrack.blockID) {
				--i2;
			}

			double d4 = 0.4D;
			boolean z6 = false;
			d7 = 2.0D / 256D;
			if(this.worldObj.getBlockId(i1, i2, i3) == Block.minecartTrack.blockID) {
				Vec3D vec3D9 = this.getPos(this.posX, this.posY, this.posZ);
				int i10 = this.worldObj.getBlockMetadata(i1, i2, i3);
				this.posY = (double)i2;
				if(i10 >= 2 && i10 <= 5) {
					this.posY = (double)(i2 + 1);
				}

				if(i10 == 2) {
					this.motionX -= d7;
				}

				if(i10 == 3) {
					this.motionX += d7;
				}

				if(i10 == 4) {
					this.motionZ += d7;
				}

				if(i10 == 5) {
					this.motionZ -= d7;
				}

				int[][] i11 = matrix[i10];
				double d12 = (double)(i11[1][0] - i11[0][0]);
				double d14 = (double)(i11[1][2] - i11[0][2]);
				double d16 = Math.sqrt(d12 * d12 + d14 * d14);
				double d18 = this.motionX * d12 + this.motionZ * d14;
				if(d18 < 0.0D) {
					d12 = -d12;
					d14 = -d14;
				}

				double d20 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
				this.motionX = d20 * d12 / d16;
				this.motionZ = d20 * d14 / d16;
				double d22 = 0.0D;
				double d24 = (double)i1 + 0.5D + (double)i11[0][0] * 0.5D;
				double d26 = (double)i3 + 0.5D + (double)i11[0][2] * 0.5D;
				double d28 = (double)i1 + 0.5D + (double)i11[1][0] * 0.5D;
				double d30 = (double)i3 + 0.5D + (double)i11[1][2] * 0.5D;
				d12 = d28 - d24;
				d14 = d30 - d26;
				double d32;
				double d34;
				double d36;
				if(d12 == 0.0D) {
					this.posX = (double)i1 + 0.5D;
					d22 = this.posZ - (double)i3;
				} else if(d14 == 0.0D) {
					this.posZ = (double)i3 + 0.5D;
					d22 = this.posX - (double)i1;
				} else {
					d32 = this.posX - d24;
					d34 = this.posZ - d26;
					d36 = (d32 * d12 + d34 * d14) * 2.0D;
					d22 = d36;
				}

				this.posX = d24 + d12 * d22;
				this.posZ = d26 + d14 * d22;
				this.setPosition(this.posX, this.posY + (double)this.yOffset, this.posZ);
				d32 = this.motionX;
				d34 = this.motionZ;
				if(this.riddenByEntity != null) {
					d32 *= 0.75D;
					d34 *= 0.75D;
				}

				if(d32 < -d4) {
					d32 = -d4;
				}

				if(d32 > d4) {
					d32 = d4;
				}

				if(d34 < -d4) {
					d34 = -d4;
				}

				if(d34 > d4) {
					d34 = d4;
				}

				this.moveEntity(d32, 0.0D, d34);
				if(i11[0][1] != 0 && MathHelper.floor_double(this.posX) - i1 == i11[0][0] && MathHelper.floor_double(this.posZ) - i3 == i11[0][2]) {
					this.setPosition(this.posX, this.posY + (double)i11[0][1], this.posZ);
				} else if(i11[1][1] != 0 && MathHelper.floor_double(this.posX) - i1 == i11[1][0] && MathHelper.floor_double(this.posZ) - i3 == i11[1][2]) {
					this.setPosition(this.posX, this.posY + (double)i11[1][1], this.posZ);
				}

				if(this.riddenByEntity != null) {
					this.motionX *= (double)0.997F;
					this.motionY *= 0.0D;
					this.motionZ *= (double)0.997F;
				} else {
					if(this.minecartType == 2) {
						d36 = (double)MathHelper.sqrt_double(this.pushX * this.pushX + this.pushZ * this.pushZ);
						if(d36 > 0.01D) {
							z6 = true;
							this.pushX /= d36;
							this.pushZ /= d36;
							double d38 = 0.04D;
							this.motionX *= (double)0.8F;
							this.motionY *= 0.0D;
							this.motionZ *= (double)0.8F;
							this.motionX += this.pushX * d38;
							this.motionZ += this.pushZ * d38;
						} else {
							this.motionX *= (double)0.9F;
							this.motionY *= 0.0D;
							this.motionZ *= (double)0.9F;
						}
					}

					this.motionX *= (double)0.96F;
					this.motionY *= 0.0D;
					this.motionZ *= (double)0.96F;
				}

				Vec3D vec3D46 = this.getPos(this.posX, this.posY, this.posZ);
				if(vec3D46 != null && vec3D9 != null) {
					double d37 = (vec3D9.yCoord - vec3D46.yCoord) * 0.05D;
					d20 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
					if(d20 > 0.0D) {
						this.motionX = this.motionX / d20 * (d20 + d37);
						this.motionZ = this.motionZ / d20 * (d20 + d37);
					}

					this.setPosition(this.posX, vec3D46.yCoord, this.posZ);
				}

				int i47 = MathHelper.floor_double(this.posX);
				int i48 = MathHelper.floor_double(this.posZ);
				if(i47 != i1 || i48 != i3) {
					d20 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
					this.motionX = d20 * (double)(i47 - i1);
					this.motionZ = d20 * (double)(i48 - i3);
				}

				if(this.minecartType == 2) {
					double d39 = (double)MathHelper.sqrt_double(this.pushX * this.pushX + this.pushZ * this.pushZ);
					if(d39 > 0.01D && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.001D) {
						this.pushX /= d39;
						this.pushZ /= d39;
						if(this.pushX * this.motionX + this.pushZ * this.motionZ < 0.0D) {
							this.pushX = 0.0D;
							this.pushZ = 0.0D;
						} else {
							this.pushX = this.motionX;
							this.pushZ = this.motionZ;
						}
					}
				}
			} else {
				if(this.motionX < -d4) {
					this.motionX = -d4;
				}

				if(this.motionX > d4) {
					this.motionX = d4;
				}

				if(this.motionZ < -d4) {
					this.motionZ = -d4;
				}

				if(this.motionZ > d4) {
					this.motionZ = d4;
				}

				if(this.onGround) {
					this.motionX *= 0.5D;
					this.motionY *= 0.5D;
					this.motionZ *= 0.5D;
				}

				this.moveEntity(this.motionX, this.motionY, this.motionZ);
				if(!this.onGround) {
					this.motionX *= (double)0.95F;
					this.motionY *= (double)0.95F;
					this.motionZ *= (double)0.95F;
				}
			}

			this.rotationPitch = 0.0F;
			double d43 = this.prevPosX - this.posX;
			double d44 = this.prevPosZ - this.posZ;
			if(d43 * d43 + d44 * d44 > 0.001D) {
				this.rotationYaw = (float)(Math.atan2(d44, d43) * 180.0D / Math.PI);
				if(this.isInReverse) {
					this.rotationYaw += 180.0F;
				}
			}

			double d13;
			for(d13 = (double)(this.rotationYaw - this.prevRotationYaw); d13 >= 180.0D; d13 -= 360.0D) {
			}

			while(d13 < -180.0D) {
				d13 += 360.0D;
			}

			if(d13 < -170.0D || d13 >= 170.0D) {
				this.rotationYaw += 180.0F;
				this.isInReverse = !this.isInReverse;
			}

			this.setRotation(this.rotationYaw, this.rotationPitch);
			List list15 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand((double)0.2F, 0.0D, (double)0.2F));
			if(list15 != null && list15.size() > 0) {
				for(int i45 = 0; i45 < list15.size(); ++i45) {
					Entity entity17 = (Entity)list15.get(i45);
					if(entity17 != this.riddenByEntity && entity17.canBePushed() && entity17 instanceof EntityMinecart) {
						entity17.applyEntityCollision(this);
					}
				}
			}

			if(this.riddenByEntity != null && this.riddenByEntity.isDead) {
				this.riddenByEntity = null;
			}

			if(z6 && this.rand.nextInt(4) == 0) {
				--this.fuel;
				if(this.fuel < 0) {
					this.pushX = this.pushZ = 0.0D;
				}

				this.worldObj.spawnParticle("largesmoke", this.posX, this.posY + 0.8D, this.posZ, 0.0D, 0.0D, 0.0D);
			}

		}
	}

	public Vec3D getPos(double x, double y, double z) {
		int i7 = MathHelper.floor_double(x);
		int i8 = MathHelper.floor_double(y);
		int i9 = MathHelper.floor_double(z);
		if(this.worldObj.getBlockId(i7, i8 - 1, i9) == Block.minecartTrack.blockID) {
			--i8;
		}

		if(this.worldObj.getBlockId(i7, i8, i9) == Block.minecartTrack.blockID) {
			int i10 = this.worldObj.getBlockMetadata(i7, i8, i9);
			y = (double)i8;
			if(i10 >= 2 && i10 <= 5) {
				y = (double)(i8 + 1);
			}

			int[][] i11 = matrix[i10];
			double d12 = 0.0D;
			double d14 = (double)i7 + 0.5D + (double)i11[0][0] * 0.5D;
			double d16 = (double)i8 + 0.5D + (double)i11[0][1] * 0.5D;
			double d18 = (double)i9 + 0.5D + (double)i11[0][2] * 0.5D;
			double d20 = (double)i7 + 0.5D + (double)i11[1][0] * 0.5D;
			double d22 = (double)i8 + 0.5D + (double)i11[1][1] * 0.5D;
			double d24 = (double)i9 + 0.5D + (double)i11[1][2] * 0.5D;
			double d26 = d20 - d14;
			double d28 = (d22 - d16) * 2.0D;
			double d30 = d24 - d18;
			if(d26 == 0.0D) {
				x = (double)i7 + 0.5D;
				d12 = z - (double)i9;
			} else if(d30 == 0.0D) {
				z = (double)i9 + 0.5D;
				d12 = x - (double)i7;
			} else {
				double d32 = x - d14;
				double d34 = z - d18;
				double d36 = (d32 * d26 + d34 * d30) * 2.0D;
				d12 = d36;
			}

			x = d14 + d26 * d12;
			y = d16 + d28 * d12;
			z = d18 + d30 * d12;
			if(d28 < 0.0D) {
				++y;
			}

			if(d28 > 0.0D) {
				y += 0.5D;
			}

			return Vec3D.createVector(x, y, z);
		} else {
			return null;
		}
	}

	protected void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		nBTTagCompound1.setInteger("Type", this.minecartType);
		if(this.minecartType == 2) {
			nBTTagCompound1.setDouble("PushX", this.pushX);
			nBTTagCompound1.setDouble("PushZ", this.pushZ);
			nBTTagCompound1.setShort("Fuel", (short)this.fuel);
		} else if(this.minecartType == 1) {
			NBTTagList nBTTagList2 = new NBTTagList();

			for(int i3 = 0; i3 < this.cargoItems.length; ++i3) {
				if(this.cargoItems[i3] != null) {
					NBTTagCompound nBTTagCompound4 = new NBTTagCompound();
					nBTTagCompound4.setByte("Slot", (byte)i3);
					this.cargoItems[i3].writeToNBT(nBTTagCompound4);
					nBTTagList2.setTag(nBTTagCompound4);
				}
			}

			nBTTagCompound1.setTag("Items", nBTTagList2);
		}

	}

	protected void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		this.minecartType = nBTTagCompound1.getInteger("Type");
		if(this.minecartType == 2) {
			this.pushX = nBTTagCompound1.getDouble("PushX");
			this.pushZ = nBTTagCompound1.getDouble("PushZ");
			this.fuel = nBTTagCompound1.getShort("Fuel");
		} else if(this.minecartType == 1) {
			NBTTagList nBTTagList2 = nBTTagCompound1.getTagList("Items");
			this.cargoItems = new ItemStack[this.getSizeInventory()];

			for(int i3 = 0; i3 < nBTTagList2.tagCount(); ++i3) {
				NBTTagCompound nBTTagCompound4 = (NBTTagCompound)nBTTagList2.entities(i3);
				int i5 = nBTTagCompound4.getByte("Slot") & 255;
				if(i5 >= 0 && i5 < this.cargoItems.length) {
					this.cargoItems[i5] = new ItemStack(nBTTagCompound4);
				}
			}
		}

	}

	public void applyEntityCollision(Entity entity1) {
		if(entity1 != this.riddenByEntity) {
			if(entity1 instanceof EntityLiving && !(entity1 instanceof EntityPlayer) && this.minecartType == 0 && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.01D && this.riddenByEntity == null && entity1.ridingEntity == null) {
				entity1.mountEntity(this);
			}

			double d2 = entity1.posX - this.posX;
			double d4 = entity1.posZ - this.posZ;
			double d6 = d2 * d2 + d4 * d4;
			if(d6 >= 9.999999747378752E-5D) {
				d6 = (double)MathHelper.sqrt_double(d6);
				d2 /= d6;
				d4 /= d6;
				double d8 = 1.0D / d6;
				if(d8 > 1.0D) {
					d8 = 1.0D;
				}

				d2 *= d8;
				d4 *= d8;
				d2 *= (double)0.1F;
				d4 *= (double)0.1F;
				d2 *= (double)(1.0F - this.entityCollisionReduction);
				d4 *= (double)(1.0F - this.entityCollisionReduction);
				d2 *= 0.5D;
				d4 *= 0.5D;
				if(entity1 instanceof EntityMinecart) {
					double d10 = entity1.motionX + this.motionX;
					double d12 = entity1.motionZ + this.motionZ;
					if(((EntityMinecart)entity1).minecartType == 2 && this.minecartType != 2) {
						this.motionX *= (double)0.2F;
						this.motionZ *= (double)0.2F;
						this.addVelocity(entity1.motionX - d2, 0.0D, entity1.motionZ - d4);
						entity1.motionX *= (double)0.7F;
						entity1.motionZ *= (double)0.7F;
					} else if(((EntityMinecart)entity1).minecartType != 2 && this.minecartType == 2) {
						entity1.motionX *= (double)0.2F;
						entity1.motionZ *= (double)0.2F;
						entity1.addVelocity(this.motionX + d2, 0.0D, this.motionZ + d4);
						this.motionX *= (double)0.7F;
						this.motionZ *= (double)0.7F;
					} else {
						d10 /= 2.0D;
						d12 /= 2.0D;
						this.motionX *= (double)0.2F;
						this.motionZ *= (double)0.2F;
						this.addVelocity(d10 - d2, 0.0D, d12 - d4);
						entity1.motionX *= (double)0.2F;
						entity1.motionZ *= (double)0.2F;
						entity1.addVelocity(d10 + d2, 0.0D, d12 + d4);
					}
				} else {
					this.addVelocity(-d2, 0.0D, -d4);
					entity1.addVelocity(d2 / 4.0D, 0.0D, d4 / 4.0D);
				}
			}

		}
	}

	public int getSizeInventory() {
		return 27;
	}

	public ItemStack getStackInSlot(int i1) {
		return this.cargoItems[i1];
	}
}
