package net.minecraft.src;

public class ItemSeeds extends Item {
	private int blockType;

	public ItemSeeds(int itemID, int blockType) {
		super(itemID);
		this.blockType = blockType;
	}

	public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int i4, int i5, int i6, int i7) {
		if(i7 != 1) {
			return false;
		} else {
			int i8 = world3.getBlockId(i4, i5, i6);
			if(i8 == Block.tilledField.blockID) {
				world3.setBlockWithNotify(i4, i5 + 1, i6, this.blockType);
				--itemStack1.stackSize;
				return true;
			} else {
				return false;
			}
		}
	}
}
