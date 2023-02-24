package net.minecraft.src;

public class EntityItem extends Entity {
	public ItemStack item;
	private int age2;
	public int age = 0;
	public int delayBeforeCanPickup;
	private int health = 5;
	public float hoverStart = (float)(Math.random() * Math.PI * 2.0D);

	public EntityItem(World worldObj, double x, double y, double z, ItemStack item) {
		super(worldObj);
		this.setSize(0.25F, 0.25F);
		this.yOffset = this.height / 2.0F;
		this.setPosition(x, y, z);
		this.item = item;
		this.rotationYaw = (float)(Math.random() * 360.0D);
		this.motionX = (double)((float)(Math.random() * (double)0.2F - (double)0.1F));
		this.motionY = (double)0.2F;
		this.motionZ = (double)((float)(Math.random() * (double)0.2F - (double)0.1F));
		this.canTriggerWalking = false;
	}

	public EntityItem(World world1) {
		super(world1);
		this.setSize(0.25F, 0.25F);
		this.yOffset = this.height / 2.0F;
	}

	public void onUpdate() {
		super.onUpdate();
		if(this.delayBeforeCanPickup > 0) {
			--this.delayBeforeCanPickup;
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= (double)0.04F;
		if(this.worldObj.getBlockMaterial(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) == Material.lava) {
			this.motionY = (double)0.2F;
			this.motionX = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			this.motionZ = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			this.worldObj.playSoundAtEntity(this, "random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
		}

		this.pushOutOfBlocks(this.posX, this.posY, this.posZ);
		this.handleWaterMovement();
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		float f1 = 0.98F;
		if(this.onGround) {
			f1 = 0.58800006F;
			int i2 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));
			if(i2 > 0) {
				f1 = Block.blocksList[i2].slipperiness * 0.98F;
			}
		}

		this.motionX *= (double)f1;
		this.motionY *= (double)0.98F;
		this.motionZ *= (double)f1;
		if(this.onGround) {
			this.motionY *= -0.5D;
		}

		++this.age2;
		++this.age;
		if(this.age >= 6000) {
			this.setEntityDead();
		}

	}

	public boolean handleWaterMovement() {
		return this.worldObj.handleMaterialAcceleration(this.boundingBox, Material.water, this);
	}

	private boolean pushOutOfBlocks(double x, double y, double z) {
		int i7 = MathHelper.floor_double(x);
		int i8 = MathHelper.floor_double(y);
		int i9 = MathHelper.floor_double(z);
		double d10 = x - (double)i7;
		double d12 = y - (double)i8;
		double d14 = z - (double)i9;
		if(Block.opaqueCubeLookup[this.worldObj.getBlockId(i7, i8, i9)]) {
			boolean z16 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(i7 - 1, i8, i9)];
			boolean z17 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(i7 + 1, i8, i9)];
			boolean z18 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(i7, i8 - 1, i9)];
			boolean z19 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(i7, i8 + 1, i9)];
			boolean z20 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(i7, i8, i9 - 1)];
			boolean z21 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(i7, i8, i9 + 1)];
			byte b22 = -1;
			double d23 = 9999.0D;
			if(z16 && d10 < d23) {
				d23 = d10;
				b22 = 0;
			}

			if(z17 && 1.0D - d10 < d23) {
				d23 = 1.0D - d10;
				b22 = 1;
			}

			if(z18 && d12 < d23) {
				d23 = d12;
				b22 = 2;
			}

			if(z19 && 1.0D - d12 < d23) {
				d23 = 1.0D - d12;
				b22 = 3;
			}

			if(z20 && d14 < d23) {
				d23 = d14;
				b22 = 4;
			}

			if(z21 && 1.0D - d14 < d23) {
				d23 = 1.0D - d14;
				b22 = 5;
			}

			float f25 = this.rand.nextFloat() * 0.2F + 0.1F;
			if(b22 == 0) {
				this.motionX = (double)(-f25);
			}

			if(b22 == 1) {
				this.motionX = (double)f25;
			}

			if(b22 == 2) {
				this.motionY = (double)(-f25);
			}

			if(b22 == 3) {
				this.motionY = (double)f25;
			}

			if(b22 == 4) {
				this.motionZ = (double)(-f25);
			}

			if(b22 == 5) {
				this.motionZ = (double)f25;
			}
		}

		return false;
	}

	protected void dealFireDamage(int damage) {
		this.attackEntityFrom((Entity)null, damage);
	}

	public boolean attackEntityFrom(Entity entity1, int i2) {
		this.health -= i2;
		if(this.health <= 0) {
			this.setEntityDead();
		}

		return false;
	}

	public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		nBTTagCompound1.setShort("Health", (short)((byte)this.health));
		nBTTagCompound1.setShort("Age", (short)this.age);
		nBTTagCompound1.setCompoundTag("Item", this.item.writeToNBT(new NBTTagCompound()));
	}

	public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		this.health = nBTTagCompound1.getShort("Health") & 255;
		this.age = nBTTagCompound1.getShort("Age");
		NBTTagCompound nBTTagCompound2 = nBTTagCompound1.getCompoundTag("Item");
		this.item = new ItemStack(nBTTagCompound2);
	}

	public void onCollideWithPlayer(EntityPlayer entityPlayer1) {
		if(!this.worldObj.multiplayerWorld) {
			int i2 = this.item.stackSize;
			if(this.delayBeforeCanPickup == 0 && entityPlayer1.inventory.addItemStackToInventory(this.item)) {
				this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				entityPlayer1.onItemPickup(this, i2);
				this.setEntityDead();
			}

		}
	}
}
