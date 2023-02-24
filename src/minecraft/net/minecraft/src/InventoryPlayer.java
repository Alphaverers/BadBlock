package net.minecraft.src;

public class InventoryPlayer implements IInventory {
	public ItemStack[] mainInventory = new ItemStack[36];
	public ItemStack[] armorInventory = new ItemStack[4];
	public ItemStack[] craftingInventory = new ItemStack[4];
	public int currentItem = 0;
	private EntityPlayer player;
	public ItemStack draggedItemStack;
	public boolean inventoryChanged = false;

	public InventoryPlayer(EntityPlayer entityPlayer1) {
		this.player = entityPlayer1;
	}

	public ItemStack getCurrentItem() {
		return this.mainInventory[this.currentItem];
	}

	private int getInventorySlotContainItem(int i1) {
		for(int i2 = 0; i2 < this.mainInventory.length; ++i2) {
			if(this.mainInventory[i2] != null && this.mainInventory[i2].itemID == i1) {
				return i2;
			}
		}

		return -1;
	}

	private int storeItemStack(int i1) {
		for(int i2 = 0; i2 < this.mainInventory.length; ++i2) {
			if(this.mainInventory[i2] != null && this.mainInventory[i2].itemID == i1 && this.mainInventory[i2].stackSize < this.mainInventory[i2].getMaxStackSize() && this.mainInventory[i2].stackSize < this.getInventoryStackLimit()) {
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

	public void setCurrentItem(int i1, boolean z2) {
		int i3 = this.getInventorySlotContainItem(i1);
		if(i3 >= 0 && i3 < 9) {
			this.currentItem = i3;
		}
	}

	public void changeCurrentItem(int i1) {
		if(i1 > 0) {
			i1 = 1;
		}

		if(i1 < 0) {
			i1 = -1;
		}

		for(this.currentItem -= i1; this.currentItem < 0; this.currentItem += 9) {
		}

		while(this.currentItem >= 9) {
			this.currentItem -= 9;
		}

	}

	private int storePartialItemStack(int i1, int i2) {
		int i3 = this.storeItemStack(i1);
		if(i3 < 0) {
			i3 = this.getFirstEmptyStack();
		}

		if(i3 < 0) {
			return i2;
		} else {
			if(this.mainInventory[i3] == null) {
				this.mainInventory[i3] = new ItemStack(i1, 0);
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

	public boolean consumeInventoryItem(int i1) {
		int i2 = this.getInventorySlotContainItem(i1);
		if(i2 < 0) {
			return false;
		} else {
			if(--this.mainInventory[i2].stackSize <= 0) {
				this.mainInventory[i2] = null;
			}

			return true;
		}
	}

	public boolean addItemStackToInventory(ItemStack itemStack1) {
		if(itemStack1.itemDmg == 0) {
			itemStack1.stackSize = this.storePartialItemStack(itemStack1.itemID, itemStack1.stackSize);
			if(itemStack1.stackSize == 0) {
				return true;
			}
		}

		int i2 = this.getFirstEmptyStack();
		if(i2 >= 0) {
			this.mainInventory[i2] = itemStack1;
			this.mainInventory[i2].animationsToGo = 5;
			return true;
		} else {
			return false;
		}
	}

	public ItemStack decrStackSize(int slot, int stackSize) {
		ItemStack[] itemStack3 = this.mainInventory;
		if(slot >= this.mainInventory.length) {
			itemStack3 = this.armorInventory;
			slot -= this.mainInventory.length;
		}

		if(itemStack3[slot] != null) {
			ItemStack itemStack4;
			if(itemStack3[slot].stackSize <= stackSize) {
				itemStack4 = itemStack3[slot];
				itemStack3[slot] = null;
				return itemStack4;
			} else {
				itemStack4 = itemStack3[slot].splitStack(stackSize);
				if(itemStack3[slot].stackSize == 0) {
					itemStack3[slot] = null;
				}

				return itemStack4;
			}
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int i1, ItemStack itemStack2) {
		ItemStack[] itemStack3 = this.mainInventory;
		if(i1 >= itemStack3.length) {
			i1 -= itemStack3.length;
			itemStack3 = this.armorInventory;
		}

		if(i1 >= itemStack3.length) {
			i1 -= itemStack3.length;
			itemStack3 = this.craftingInventory;
		}

		itemStack3[i1] = itemStack2;
	}

	public float getStrVsBlock(Block block1) {
		float f2 = 1.0F;
		if(this.mainInventory[this.currentItem] != null) {
			f2 *= this.mainInventory[this.currentItem].getStrVsBlock(block1);
		}

		return f2;
	}

	public NBTTagList writeToNBT(NBTTagList nBTTagList1) {
		int i2;
		NBTTagCompound nBTTagCompound3;
		for(i2 = 0; i2 < this.mainInventory.length; ++i2) {
			if(this.mainInventory[i2] != null) {
				nBTTagCompound3 = new NBTTagCompound();
				nBTTagCompound3.setByte("Slot", (byte)i2);
				this.mainInventory[i2].writeToNBT(nBTTagCompound3);
				nBTTagList1.setTag(nBTTagCompound3);
			}
		}

		for(i2 = 0; i2 < this.armorInventory.length; ++i2) {
			if(this.armorInventory[i2] != null) {
				nBTTagCompound3 = new NBTTagCompound();
				nBTTagCompound3.setByte("Slot", (byte)(i2 + 100));
				this.armorInventory[i2].writeToNBT(nBTTagCompound3);
				nBTTagList1.setTag(nBTTagCompound3);
			}
		}

		for(i2 = 0; i2 < this.craftingInventory.length; ++i2) {
			if(this.craftingInventory[i2] != null) {
				nBTTagCompound3 = new NBTTagCompound();
				nBTTagCompound3.setByte("Slot", (byte)(i2 + 80));
				this.craftingInventory[i2].writeToNBT(nBTTagCompound3);
				nBTTagList1.setTag(nBTTagCompound3);
			}
		}

		return nBTTagList1;
	}

	public void readFromNBT(NBTTagList nBTTagList1) {
		this.mainInventory = new ItemStack[36];
		this.armorInventory = new ItemStack[4];
		this.craftingInventory = new ItemStack[4];

		for(int i2 = 0; i2 < nBTTagList1.tagCount(); ++i2) {
			NBTTagCompound nBTTagCompound3 = (NBTTagCompound)nBTTagList1.tagAt(i2);
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

	public String getInvName() {
		return "Inventory";
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public int getDamageVsEntity(Entity entity1) {
		ItemStack itemStack2 = this.getStackInSlot(this.currentItem);
		return itemStack2 != null ? itemStack2.getDamageVsEntity(entity1) : 1;
	}

	public boolean canHarvestBlock(Block block1) {
		if(block1.material != Material.rock && block1.material != Material.iron && block1.material != Material.craftedSnow && block1.material != Material.snow) {
			return true;
		} else {
			ItemStack itemStack2 = this.getStackInSlot(this.currentItem);
			return itemStack2 != null ? itemStack2.canHarvestBlock(block1) : false;
		}
	}

	public ItemStack armorItemInSlot(int i1) {
		return this.armorInventory[i1];
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

	public void damageArmor(int i1) {
		for(int i2 = 0; i2 < this.armorInventory.length; ++i2) {
			if(this.armorInventory[i2] != null && this.armorInventory[i2].getItem() instanceof ItemArmor) {
				this.armorInventory[i2].damageItem(i1);
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

	public void onInventoryChanged() {
		this.inventoryChanged = true;
	}

	public boolean getInventoryEqual(InventoryPlayer inventoryPlayer1) {
		int i2;
		for(i2 = 0; i2 < this.mainInventory.length; ++i2) {
			if(!this.getItemStacksEqual(inventoryPlayer1.mainInventory[i2], this.mainInventory[i2])) {
				return false;
			}
		}

		for(i2 = 0; i2 < this.armorInventory.length; ++i2) {
			if(!this.getItemStacksEqual(inventoryPlayer1.armorInventory[i2], this.armorInventory[i2])) {
				return false;
			}
		}

		for(i2 = 0; i2 < this.craftingInventory.length; ++i2) {
			if(!this.getItemStacksEqual(inventoryPlayer1.craftingInventory[i2], this.craftingInventory[i2])) {
				return false;
			}
		}

		return true;
	}

	private boolean getItemStacksEqual(ItemStack itemStack1, ItemStack itemStack2) {
		return itemStack1 == null && itemStack2 == null ? true : (itemStack1 != null && itemStack2 != null ? itemStack1.itemID == itemStack2.itemID && itemStack1.stackSize == itemStack2.stackSize && itemStack1.itemDmg == itemStack2.itemDmg : false);
	}

	public InventoryPlayer copyInventory() {
		InventoryPlayer inventoryPlayer1 = new InventoryPlayer((EntityPlayer)null);

		int i2;
		for(i2 = 0; i2 < this.mainInventory.length; ++i2) {
			inventoryPlayer1.mainInventory[i2] = this.mainInventory[i2] != null ? this.mainInventory[i2].copy() : null;
		}

		for(i2 = 0; i2 < this.armorInventory.length; ++i2) {
			inventoryPlayer1.armorInventory[i2] = this.armorInventory[i2] != null ? this.armorInventory[i2].copy() : null;
		}

		for(i2 = 0; i2 < this.craftingInventory.length; ++i2) {
			inventoryPlayer1.craftingInventory[i2] = this.craftingInventory[i2] != null ? this.craftingInventory[i2].copy() : null;
		}

		return inventoryPlayer1;
	}
}
