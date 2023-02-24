package net.minecraft.src;

public class CraftingInventoryWorkbenchCB extends CraftingInventoryCB {
	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	public IInventory craftResult = new InventoryCraftResult();

	public void onCraftMatrixChanged(IInventory inventory) {
		int[] i2 = new int[9];

		for(int i3 = 0; i3 < 3; ++i3) {
			for(int i4 = 0; i4 < 3; ++i4) {
				int i5 = i3 + i4 * 3;
				ItemStack itemStack6 = this.craftMatrix.getStackInSlot(i5);
				if(itemStack6 == null) {
					i2[i5] = -1;
				} else {
					i2[i5] = itemStack6.itemID;
				}
			}
		}

		this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(i2));
	}

	public void onCraftGuiClosed(EntityPlayer entityPlayer1) {
		super.onCraftGuiClosed(entityPlayer1);

		for(int i2 = 0; i2 < 9; ++i2) {
			ItemStack itemStack3 = this.craftMatrix.getStackInSlot(i2);
			if(itemStack3 != null) {
				entityPlayer1.dropPlayerItem(itemStack3);
			}
		}

	}
}
