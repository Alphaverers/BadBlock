package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class CraftingInventoryCB {
	protected List list = new ArrayList();

	public void onCraftGuiClosed(EntityPlayer entityPlayer) {
		InventoryPlayer inventoryPlayer2 = entityPlayer.inventory;
		if(inventoryPlayer2.draggedItemStack != null) {
			entityPlayer.dropPlayerItem(inventoryPlayer2.draggedItemStack);
		}

	}

	public void onCraftMatrixChanged(IInventory inventory) {
	}
}
