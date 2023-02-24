package net.minecraft.src;

public class TileEntityFurnace extends TileEntity implements IInventory {
	private ItemStack[] furnaceItemStacks = new ItemStack[3];
	private int furnaceBurnTime = 0;
	private int currentItemBurnTime = 0;
	private int furnaceCookTime = 0;

	public int getSizeInventory() {
		return this.furnaceItemStacks.length;
	}

	public ItemStack getStackInSlot(int i1) {
		return this.furnaceItemStacks[i1];
	}

	public void readFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readFromNBT(nBTTagCompound1);
		NBTTagList nBTTagList2 = nBTTagCompound1.getTagList("Items");
		this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];

		for(int i3 = 0; i3 < nBTTagList2.tagCount(); ++i3) {
			NBTTagCompound nBTTagCompound4 = (NBTTagCompound)nBTTagList2.entities(i3);
			byte b5 = nBTTagCompound4.getByte("Slot");
			if(b5 >= 0 && b5 < this.furnaceItemStacks.length) {
				this.furnaceItemStacks[b5] = new ItemStack(nBTTagCompound4);
			}
		}

		this.furnaceBurnTime = nBTTagCompound1.getShort("BurnTime");
		this.furnaceCookTime = nBTTagCompound1.getShort("CookTime");
		this.currentItemBurnTime = this.getItemBurnTime(this.furnaceItemStacks[1]);
	}

	public void writeToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeToNBT(nBTTagCompound1);
		nBTTagCompound1.setShort("BurnTime", (short)this.furnaceBurnTime);
		nBTTagCompound1.setShort("CookTime", (short)this.furnaceCookTime);
		NBTTagList nBTTagList2 = new NBTTagList();

		for(int i3 = 0; i3 < this.furnaceItemStacks.length; ++i3) {
			if(this.furnaceItemStacks[i3] != null) {
				NBTTagCompound nBTTagCompound4 = new NBTTagCompound();
				nBTTagCompound4.setByte("Slot", (byte)i3);
				this.furnaceItemStacks[i3].writeToNBT(nBTTagCompound4);
				nBTTagList2.setTag(nBTTagCompound4);
			}
		}

		nBTTagCompound1.setTag("Items", nBTTagList2);
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean isBurning() {
		return this.furnaceBurnTime > 0;
	}

	public void updateEntity() {
		boolean z1 = this.furnaceBurnTime > 0;
		boolean z2 = false;
		if(this.furnaceBurnTime > 0) {
			--this.furnaceBurnTime;
		}

		if(this.furnaceBurnTime == 0 && this.canSmelt()) {
			this.currentItemBurnTime = this.furnaceBurnTime = this.getItemBurnTime(this.furnaceItemStacks[1]);
			if(this.furnaceBurnTime > 0) {
				z2 = true;
				if(this.furnaceItemStacks[1] != null) {
					--this.furnaceItemStacks[1].stackSize;
					if(this.furnaceItemStacks[1].stackSize == 0) {
						this.furnaceItemStacks[1] = null;
					}
				}
			}
		}

		if(this.isBurning() && this.canSmelt()) {
			++this.furnaceCookTime;
			if(this.furnaceCookTime == 200) {
				this.furnaceCookTime = 0;
				this.smeltItem();
				z2 = true;
			}
		} else {
			this.furnaceCookTime = 0;
		}

		if(z1 != this.furnaceBurnTime > 0) {
			z2 = true;
			BlockFurnace.updateFurnaceBlockState(this.furnaceBurnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}

		if(z2) {
			this.onInventoryChanged();
		}

	}

	private boolean canSmelt() {
		if(this.furnaceItemStacks[0] == null) {
			return false;
		} else {
			int i1 = this.getCookedItem(this.furnaceItemStacks[0].getItem().shiftedIndex);
			return i1 < 0 ? false : (this.furnaceItemStacks[2] == null ? true : (this.furnaceItemStacks[2].itemID != i1 ? false : (this.furnaceItemStacks[2].stackSize < this.getInventoryStackLimit() && this.furnaceItemStacks[2].stackSize < this.furnaceItemStacks[2].getMaxStackSize() ? true : this.furnaceItemStacks[2].stackSize < Item.itemsList[i1].getItemStackLimit())));
		}
	}

	public void smeltItem() {
		if(this.canSmelt()) {
			int i1 = this.getCookedItem(this.furnaceItemStacks[0].getItem().shiftedIndex);
			if(this.furnaceItemStacks[2] == null) {
				this.furnaceItemStacks[2] = new ItemStack(i1, 1);
			} else if(this.furnaceItemStacks[2].itemID == i1) {
				++this.furnaceItemStacks[2].stackSize;
			}

			--this.furnaceItemStacks[0].stackSize;
			if(this.furnaceItemStacks[0].stackSize <= 0) {
				this.furnaceItemStacks[0] = null;
			}

		}
	}

	private int getCookedItem(int blockID) {
		return blockID == Block.oreIron.blockID ? Item.ingotIron.shiftedIndex : (blockID == Block.oreGold.blockID ? Item.ingotGold.shiftedIndex : (blockID == Block.oreDiamond.blockID ? Item.diamond.shiftedIndex : (blockID == Block.sand.blockID ? Block.glass.blockID : (blockID == Item.porkRaw.shiftedIndex ? Item.porkCooked.shiftedIndex : (blockID == Block.cobblestone.blockID ? Block.stone.blockID : (blockID == Item.clay.shiftedIndex ? Item.brick.shiftedIndex : -1))))));
	}

	private int getItemBurnTime(ItemStack stack) {
		if(stack == null) {
			return 0;
		} else {
			int i2 = stack.getItem().shiftedIndex;
			return i2 < 256 && Block.blockList[i2].material == Material.wood ? 300 : (i2 == Item.stick.shiftedIndex ? 100 : (i2 == Item.coal.shiftedIndex ? 1600 : (i2 == Item.bucketLava.shiftedIndex ? 20000 : 0)));
		}
	}
}
