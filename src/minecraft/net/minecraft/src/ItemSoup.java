package net.minecraft.src;

public class ItemSoup extends ItemFood {
	public ItemSoup(int i1, int i2) {
		super(i1, i2);
	}

	public ItemStack onItemRightClick(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3) {
		super.onItemRightClick(itemStack1, world2, entityPlayer3);
		return new ItemStack(Item.bowlEmpty);
	}
}
