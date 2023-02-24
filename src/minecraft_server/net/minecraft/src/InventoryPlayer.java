package net.minecraft.src;

public class InventoryPlayer implements IInventory {
	public ItemStack[] mainInventory = new ItemStack[37];
	public ItemStack[] armorInventory = new ItemStack[4];
	public ItemStack[] craftingInventory = new ItemStack[4];
	public int currentItem = 0;
	private EntityPlayer player;
	public boolean inventoryChanged = false;

	public InventoryPlayer(EntityPlayer entityPlayer) {
		this.player = entityPlayer;
	}

	public ItemStack getCurrentItem() {
		return this.mainInventory[this.currentItem];
	}

	private int storeItemStack(int itemID) {
		for(int i2 = 0; i2 < this.mainInventory.length; ++i2) {
			if(this.mainInventory[i2] != null && this.mainInventory[i2].itemID == itemID && this.mainInventory[i2].stackSize < this.mainInventory[i2].getMaxStackSize() && this.mainInventory[i2].stackSize < this.getInventoryStackLimit()) {
				return i2;
			}
		}

		return -1;
	}

	private int getFirstEmptyStack() {
		for(int i1 = 0; i1 < this.mainInventory.length; ++i1) {
			if(this.mainInventory[i1] == null) {
				return i1;
			}
		}

		return -1;
	}

	private int storePartialItemStack(int itemID, int i2) {
		int i3 = this.storeItemStack(itemID);
		if(i3 < 0) {
			i3 = this.getFirstEmptyStack();
		}

		if(i3 < 0) {
			return i2;
		} else {
			if(this.mainInventory[i3] == null) {
				this.mainInventory[i3] = new ItemStack(itemID, 0);
			}

			int i4 = i2;
			if(i2 > this.mainInventory[i3].getMaxStackSize() - this.mainInventory[i3].stackSize) {
				i4 = this.mainInventory[i3].getMaxStackSize() - this.mainInventory[i3].stackSize;
			}

			if(i4 > this.getInventoryStackLimit() - this.mainInventory[i3].stackSize) {
				i4 = this.getInventoryStackLimit() - this.mainInventory[i3].stackSize;
			}

			if(i4 == 0) {
				return i2;
			} else {
				i2 -= i4;
				this.mainInventory[i3].stackSize += i4;
				this.mainInventory[i3].animationsToGo = 5;
				return i2;
			}
		}
	}

	public void decrementAnimations() {
		for(int i1 = 0; i1 < this.mainInventory.length; ++i1) {
			if(this.mainInventory[i1] != null && this.mainInventory[i1].animationsToGo > 0) {
				--this.mainInventory[i1].animationsToGo;
			}
		}

	}

	public boolean addItemStackToInventory(ItemStack stack) {
		if(stack.itemDmg == 0) {
			stack.stackSize = this.storePartialItemStack(stack.itemID, stack.stackSize);
			if(stack.stackSize == 0) {
				return true;
			}
		}

		int i2 = this.getFirstEmptyStack();
		if(i2 >= 0) {
			this.mainInventory[i2] = stack;
			this.mainInventory[i2].animationsToGo = 5;
			return true;
		} else {
			return false;
		}
	}

	public void setInventorySlotContents(int slots, ItemStack stack) {
		ItemStack[] itemStack3 = this.mainInventory;
		if(slots >= itemStack3.length) {
			slots -= itemStack3.length;
			itemStack3 = this.armorInventory;
		}

		if(slots >= itemStack3.length) {
			slots -= itemStack3.length;
			itemStack3 = this.craftingInventory;
		}

		itemStack3[slots] = stack;
	}

	public float getStrVsBlock(Block block) {
		float f2 = 1.0F;
		if(this.mainInventory[this.currentItem] != null) {
			f2 *= this.mainInventory[this.currentItem].getStrVsBlock(block);
		}

		return f2;
	}

	public NBTTagList writeToNBT(NBTTagList nbttaglist) {
		int i2;
		NBTTagCompound nBTTagCompound3;
		for(i2 = 0; i2 < this.mainInventory.length; ++i2) {
			if(this.mainInventory[i2] != null) {
				nBTTagCompound3 = new NBTTagCompound();
				nBTTagCompound3.setByte("Slot", (byte)i2);
				this.mainInventory[i2].writeToNBT(nBTTagCompound3);
				nbttaglist.setTag(nBTTagCompound3);
			}
		}

		for(i2 = 0; i2 < this.armorInventory.length; ++i2) {
			if(this.armorInventory[i2] != null) {
				nBTTagCompound3 = new NBTTagCompound();
				nBTTagCompound3.setByte("Slot", (byte)(i2 + 100));
				this.armorInventory[i2].writeToNBT(nBTTagCompound3);
				nbttaglist.setTag(nBTTagCompound3);
			}
		}

		for(i2 = 0; i2 < this.craftingInventory.length; ++i2) {
			if(this.craftingInventory[i2] != null) {
				nBTTagCompound3 = new NBTTagCompound();
				nBTTagCompound3.setByte("Slot", (byte)(i2 + 80));
				this.craftingInventory[i2].writeToNBT(nBTTagCompound3);
				nbttaglist.setTag(nBTTagCompound3);
			}
		}

		return nbttaglist;
	}

	public void readFromNBT(NBTTagList nbttaglist) {
		this.mainInventory = new ItemStack[36];
		this.armorInventory = new ItemStack[4];
		this.craftingInventory = new ItemStack[4];

		for(int i2 = 0; i2 < nbttaglist.tagCount(); ++i2) {
			NBTTagCompound nBTTagCompound3 = (NBTTagCompound)nbttaglist.entities(i2);
			int i4 = nBTTagCompound3.getByte("Slot") & 255;
			if(i4 >= 0 && i4 < this.mainInventory.length) {
				this.mainInventory[i4] = new ItemStack(nBTTagCompound3);
			}

			if(i4 >= 80 && i4 < this.craftingInventory.length + 80) {
				this.craftingInventory[i4 - 80] = new ItemStack(nBTTagCompound3);
			}

			if(i4 >= 100 && i4 < this.armorInventory.length + 100) {
				this.armorInventory[i4 - 100] = new ItemStack(nBTTagCompound3);
			}
		}

	}

	public int getSizeInventory() {
		return this.mainInventory.length + 4;
	}

	public ItemStack getStackInSlot(int i1) {
		ItemStack[] itemStack2 = this.mainInventory;
		if(i1 >= itemStack2.length) {
			i1 -= itemStack2.length;
			itemStack2 = this.armorInventory;
		}

		if(i1 >= itemStack2.length) {
			i1 -= itemStack2.length;
			itemStack2 = this.craftingInventory;
		}

		return itemStack2[i1];
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean canHarvestBlock(Block block) {
		if(block.material != Material.rock && block.material != Material.iron && block.material != Material.craftedSnow && block.material != Material.snow) {
			return true;
		} else {
			ItemStack itemStack2 = this.getStackInSlot(this.currentItem);
			return itemStack2 != null ? itemStack2.canHarvestBlock(block) : false;
		}
	}

	public int getTotalArmorValue() {
		int i1 = 0;
		int i2 = 0;
		int i3 = 0;

		for(int i4 = 0; i4 < this.armorInventory.length; ++i4) {
			if(this.armorInventory[i4] != null && this.armorInventory[i4].getItem() instanceof ItemArmor) {
				int i5 = this.armorInventory[i4].getMaxDamage();
				int i6 = this.armorInventory[i4].itemDmg;
				int i7 = i5 - i6;
				i2 += i7;
				i3 += i5;
				int i8 = ((ItemArmor)this.armorInventory[i4].getItem()).damageReduceAmount;
				i1 += i8;
			}
		}

		if(i3 == 0) {
			return 0;
		} else {
			return (i1 - 1) * i2 / i3 + 1;
		}
	}

	public void damageArmor(int damage) {
		for(int i2 = 0; i2 < this.armorInventory.length; ++i2) {
			if(this.armorInventory[i2] != null && this.armorInventory[i2].getItem() instanceof ItemArmor) {
				this.armorInventory[i2].damageItem(damage);
				if(this.armorInventory[i2].stackSize == 0) {
					this.armorInventory[i2].onItemDestroyedByUse(this.player);
					this.armorInventory[i2] = null;
				}
			}
		}

	}

	public void dropAllItems() {
		int i1;
		for(i1 = 0; i1 < this.mainInventory.length; ++i1) {
			if(this.mainInventory[i1] != null) {
				this.player.dropPlayerItemWithRandomChoice(this.mainInventory[i1], true);
				this.mainInventory[i1] = null;
			}
		}

		for(i1 = 0; i1 < this.armorInventory.length; ++i1) {
			if(this.armorInventory[i1] != null) {
				this.player.dropPlayerItemWithRandomChoice(this.armorInventory[i1], true);
				this.armorInventory[i1] = null;
			}
		}

	}
}
