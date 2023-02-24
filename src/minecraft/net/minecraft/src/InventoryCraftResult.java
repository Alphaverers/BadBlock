package net.minecraft.src;

public class InventoryCraftResult implements IInventory {
	private ItemStack[] stackResult = new ItemStack[1];

	public int getSizeInventory() {
		return 1;
	}

	public ItemStack getStackInSlot(int i1) {
		return this.stackResult[i1];
	}

	public String getInvName() {
		return "Result";
	}

	public ItemStack decrStackSize(int slot, int stackSize) {
		if(this.stackResult[slot] != null) {
			ItemStack itemStack3 = this.stackResult[slot];
			this.stackResult[slot] = null;
			return itemStack3;
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int i1, ItemStack itemStack2) {
		this.stackResult[i1] = itemStack2;
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public void onInventoryChanged() {
	}
}
