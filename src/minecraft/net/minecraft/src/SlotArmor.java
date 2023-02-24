package net.minecraft.src;

class SlotArmor extends SlotInventory {
	final int armorType;
	final GuiInventory guiInventory;

	SlotArmor(GuiInventory guiInventory1, GuiContainer guiContainer2, IInventory iInventory3, int i4, int i5, int i6, int i7) {
		super(guiContainer2, iInventory3, i4, i5, i6);
		this.guiInventory = guiInventory1;
		this.armorType = i7;
	}

	public boolean isItemValid(ItemStack itemStack) {
		return itemStack.getItem() instanceof ItemArmor ? ((ItemArmor)itemStack.getItem()).armorType == this.armorType : false;
	}

	public int getBackgroundIconIndex() {
		return 15 + this.armorType * 16;
	}
}
