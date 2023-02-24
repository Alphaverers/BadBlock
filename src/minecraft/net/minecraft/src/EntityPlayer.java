package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class EntityPlayer extends EntityLiving {
	public InventoryPlayer inventory = new InventoryPlayer(this);
	public byte unusedMiningCooldown = 0;
	public int score = 0;
	public float prevCameraYaw;
	public float cameraYaw;
	public boolean isSwinging = false;
	public int swingProgressInt = 0;
	public String username;
	private int damageRemainder = 0;
	public Random rand;

	public EntityPlayer(World world1) {
		super(world1);
		this.yOffset = 1.62F;
		this.setLocationAndAngles((double)world1.spawnX + 0.5D, (double)(world1.spawnY + 1), (double)world1.spawnZ + 0.5D, 0.0F, 0.0F);
		this.health = 20;
		this.entityType = "humanoid";
		this.unusedRotation = 180.0F;
		this.fireResistance = 20;
		this.texture = "/char.png";
		this.rand = new Random();
	}

	public void updateRidden() {
		super.updateRidden();
		this.prevCameraYaw = this.cameraYaw;
		this.cameraYaw = 0.0F;
	}

	public void preparePlayerToSpawn() {
		this.yOffset = 1.62F;
		this.setSize(0.6F, 1.8F);
		super.preparePlayerToSpawn();
		this.health = 20;
		this.deathTime = 0;
	}

	protected void updateEntityActionState() {
		if(this.isSwinging) {
			++this.swingProgressInt;
			if(this.swingProgressInt == 8) {
				this.swingProgressInt = 0;
				this.isSwinging = false;
			}
		} else {
			this.swingProgressInt = 0;
		}

		this.swingProgress = (float)this.swingProgressInt / 8.0F;
	}

	public void onLivingUpdate() {
		if(this.worldObj.difficultySetting == 0 && this.health < 20 && this.ticksExisted % 20 * 4 == 0) {
			this.heal(1);
		}

		this.inventory.decrementAnimations();
		this.prevCameraYaw = this.cameraYaw;
		super.onLivingUpdate();
		float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
		float f2 = (float)Math.atan(-this.motionY * (double)0.2F) * 15.0F;
		if(f1 > 0.1F) {
			f1 = 0.1F;
		}

		if(!this.onGround || this.health <= 0) {
			f1 = 0.0F;
		}

		if(this.onGround || this.health <= 0) {
			f2 = 0.0F;
		}

		this.cameraYaw += (f1 - this.cameraYaw) * 0.4F;
		this.cameraPitch += (f2 - this.cameraPitch) * 0.8F;
		if(this.health > 0) {
			List list3 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(1.0D, 0.0D, 1.0D));
			if(list3 != null) {
				for(int i4 = 0; i4 < list3.size(); ++i4) {
					this.collideWithPlayer((Entity)list3.get(i4));
				}
			}
		}

		if(this.posY < -1.0D) {
			this.posZ += (double)(this.rand.nextFloat() * 500.0F + 500.0F);
			int i6 = 80;
			boolean z7 = false;

			label47:
			while(true) {
				while(true) {
					if(i6 == 0) {
						break label47;
					}

					int i5 = this.worldObj.getBlockId((int)this.posX, i6, (int)this.posZ);
					if(i5 != 0 && i5 != 116) {
						if(z7) {
							++i6;
							break label47;
						}

						++i6;
					} else {
						--i6;
						z7 = true;
					}
				}
			}

			this.posY = (double)i6;
			this.setPosition(this.posX, this.posY, this.posZ);
			System.out.println("Sending player to Brazil at: " + this.posX + " " + this.posY + " " + this.posZ);
		}

	}

	private void collideWithPlayer(Entity entity) {
		entity.onCollideWithPlayer(this);
	}

	public int getScore() {
		return this.score;
	}

	public void onDeath(Entity entity1) {
		this.setSize(0.2F, 0.2F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.motionY = (double)0.1F;
		if(this.username.equals("Notch")) {
			this.dropPlayerItemWithRandomChoice(new ItemStack(Item.appleRed, 1), true);
		}

		this.inventory.dropAllItems();
		if(entity1 != null) {
			this.motionX = (double)(-MathHelper.cos((this.attackedAtYaw + this.rotationYaw) * (float)Math.PI / 180.0F) * 0.1F);
			this.motionZ = (double)(-MathHelper.sin((this.attackedAtYaw + this.rotationYaw) * (float)Math.PI / 180.0F) * 0.1F);
		} else {
			this.motionZ = 0.0D;
			this.motionX = 0.0D;
		}

		this.yOffset = 0.1F;
	}

	public void addToPlayerScore(Entity entity1, int i2) {
		this.score += i2;
	}

	public void dropPlayerItem(ItemStack itemStack) {
		this.dropPlayerItemWithRandomChoice(itemStack, false);
	}

	public void dropPlayerItemWithRandomChoice(ItemStack itemStack, boolean isRandom) {
		if(itemStack != null) {
			EntityItem entityItem3 = new EntityItem(this.worldObj, this.posX, this.posY - (double)0.3F + (double)this.getEyeHeight(), this.posZ, itemStack);
			entityItem3.delayBeforeCanPickup = 40;
			if(isRandom) {
				float f4 = this.rand.nextFloat() * 0.5F;
				float f5 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
				entityItem3.motionX = (double)(-MathHelper.sin(f5) * f4);
				entityItem3.motionZ = (double)(MathHelper.cos(f5) * f4);
				entityItem3.motionY = (double)0.2F;
			} else {
				entityItem3.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * 0.3F);
				entityItem3.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * 0.3F);
				entityItem3.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI) * 0.3F + 0.1F);
				float f6 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
				float f7 = 0.02F * this.rand.nextFloat();
				entityItem3.motionX += Math.cos((double)f6) * (double)f7;
				entityItem3.motionY += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
				entityItem3.motionZ += Math.sin((double)f6) * (double)f7;
			}

			this.joinEntityItemWithWorld(entityItem3);
		}
	}

	protected void joinEntityItemWithWorld(EntityItem entityItem) {
		this.worldObj.spawnEntityInWorld(entityItem);
	}

	public float getCurrentPlayerStrVsBlock(Block block) {
		float f2 = this.inventory.getStrVsBlock(block);
		if(this.isInsideOfMaterial(Material.water)) {
			f2 /= 5.0F;
		}

		if(!this.onGround) {
			f2 /= 5.0F;
		}

		return f2;
	}

	public boolean canHarvestBlock(Block block) {
		return this.inventory.canHarvestBlock(block);
	}

	public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readEntityFromNBT(nBTTagCompound1);
		this.inventory.readFromNBT(nBTTagCompound1.getTagList("Inventory"));
	}

	public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeEntityToNBT(nBTTagCompound1);
		nBTTagCompound1.setTag("Inventory", this.inventory.writeToNBT(new NBTTagList()));
	}

	public void displayGUIChest(IInventory inventory) {
	}

	public void displayWorkbenchGUI() {
	}

	public void onItemPickup(Entity entity, int i2) {
	}

	protected float getEyeHeight() {
		return 0.12F;
	}

	public boolean attackEntityFrom(Entity entity1, int i2) {
		this.entityAge = 0;
		if(this.health <= 0) {
			return false;
		} else if((float)this.heartsLife > (float)this.heartsHalvesLife / 2.0F) {
			return false;
		} else {
			if(entity1 instanceof EntityMob || entity1 instanceof EntityArrow) {
				if(this.worldObj.difficultySetting == 0) {
					i2 = 0;
				}

				if(this.worldObj.difficultySetting == 1) {
					i2 = i2 / 3 + 1;
				}

				if(this.worldObj.difficultySetting == 3) {
					i2 = i2 * 3 / 2;
				}
			}

			int i3 = i2 * (25 - this.inventory.getTotalArmorValue()) + this.damageRemainder;
			this.inventory.damageArmor(i2);
			i2 = i3 / 25;
			this.damageRemainder = i3 % 25;
			return i2 != 0 && super.attackEntityFrom(entity1, i2);
		}
	}

	public void displayGUIFurnace(TileEntityFurnace furnaceTileEntity) {
	}

	public void displayGUIEditSign(TileEntitySign signTileEntity) {
	}

	public void interactWithEntity(Entity entity) {
	}

	public ItemStack getCurrentEquippedItem() {
		return this.inventory.getCurrentItem();
	}

	public void destroyCurrentEquippedItem() {
		this.inventory.setInventorySlotContents(this.inventory.currentItem, (ItemStack)null);
	}

	public double getYOffset() {
		return (double)(this.yOffset - 0.5F);
	}

	public void swingItem() {
		this.swingProgressInt = -1;
		this.isSwinging = true;
	}
}
