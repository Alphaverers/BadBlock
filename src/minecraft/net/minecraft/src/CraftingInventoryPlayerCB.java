package net.minecraft.src;

public class CraftingInventoryPlayerCB extends CraftingInventoryCB {
	public InventoryCrafting craftMatrix;
	public IInventory craftResult = new InventoryCraftResult();

	public CraftingInventoryPlayerCB(ItemStack[] itemStack1) {
		this.craftMatrix = new InventoryCrafting(this, itemStack1);
		this.onCraftMatrixChanged(this.craftMatrix);
	}

	public void onCraftMatrixChanged(IInventory inventory) {
		int[] i2 = new int[9];

		for(int i3 = 0; i3 < 3; ++i3) {
			for(int i4 = 0; i4 < 3; ++i4) {
				int i5 = -1;
				if(i3 < 2 && i4 < 2) {
					ItemStack itemStack6 = this.craftMatrix.getStackInSlot(i3 + i4 * 2);
					if(itemStack6 != null) {
						i5 = itemStack6.itemID;
					}
				}

				i2[i3 + i4 * 3] = i5;
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
