package net.minecraft.src;

public interface IInventory {
	int getSizeInventory();

	ItemStack getStackInSlot(int i1);

	ItemStack decrStackSize(int i1, int i2);

	void setInventorySlotContents(int i1, ItemStack itemStack2);

	String getInvName();

	int getInventoryStackLimit();

	void onInventoryChanged();
}
