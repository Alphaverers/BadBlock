package net.minecraft.src;

public final class ItemStack {
	public int stackSize;
	public int animationsToGo;
	public int itemID;
	public int itemDmg;

	public ItemStack(Block block) {
		this((Block)block, 1);
	}

	public ItemStack(Block block, int count) {
		this(block.blockID, count);
	}

	public ItemStack(Item item) {
		this((Item)item, 1);
	}

	public ItemStack(Item item, int count) {
		this(item.shiftedIndex, count);
	}

	public ItemStack(int itemID) {
		this(itemID, 1);
	}

	public ItemStack(int itemID, int stackSize) {
		this.stackSize = 0;
		this.itemID = itemID;
		this.stackSize = stackSize;
	}

	public ItemStack(int itemID, int stackSize, int itemDmg) {
		this.stackSize = 0;
		this.itemID = itemID;
		this.stackSize = stackSize;
		this.itemDmg = itemDmg;
	}

	public ItemStack(NBTTagCompound nbttagcompound) {
		this.stackSize = 0;
		this.readFromNBT(nbttagcompound);
	}

	public Item getItem() {
		return Item.itemsList[this.itemID];
	}

	public boolean useItem(EntityPlayer entityPlayer, World world, int x, int y, int z, int i6) {
		return this.getItem().onItemUse(this, entityPlayer, world, x, y, z, i6);
	}

	public float getStrVsBlock(Block block) {
		return this.getItem().getStrVsBlock(this, block);
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setShort("id", (short)this.itemID);
		nbttagcompound.setByte("Count", (byte)this.stackSize);
		nbttagcompound.setShort("Damage", (short)this.itemDmg);
		return nbttagcompound;
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		this.itemID = nbttagcompound.getShort("id");
		this.stackSize = nbttagcompound.getByte("Count");
		this.itemDmg = nbttagcompound.getShort("Damage");
	}

	public int getMaxStackSize() {
		return this.getItem().getItemStackLimit();
	}

	public int getMaxDamage() {
		return Item.itemsList[this.itemID].getMaxDamage();
	}

	public void damageItem(int damage) {
		this.itemDmg += damage;
		if(this.itemDmg > this.getMaxDamage()) {
			--this.stackSize;
			if(this.stackSize < 0) {
				this.stackSize = 0;
			}

			this.itemDmg = 0;
		}

	}

	public void onDestroyBlock(int x, int y, int z, int i4) {
		Item.itemsList[this.itemID].onBlockDestroyed(this, x, y, z, i4);
	}

	public boolean canHarvestBlock(Block block) {
		return Item.itemsList[this.itemID].canHarvestBlock(block);
	}

	public void onItemDestroyedByUse(EntityPlayer entityPlayer) {
	}

	public ItemStack copy() {
		return new ItemStack(this.itemID, this.stackSize, this.itemDmg);
	}
}
