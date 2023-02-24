package net.minecraft.src;

public class ItemMinecart extends Item {
	public int minecartType;

	public ItemMinecart(int itemID, int minecartType) {
		super(itemID);
		this.maxStackSize = 1;
		this.minecartType = minecartType;
	}

	public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int i4, int i5, int i6, int i7) {
		int i8 = world3.getBlockId(i4, i5, i6);
		if(i8 == Block.minecartTrack.blockID) {
			world3.spawnEntityInWorld(new EntityMinecart(world3, (double)((float)i4 + 0.5F), (double)((float)i5 + 0.5F), (double)((float)i6 + 0.5F), this.minecartType));
			--itemStack1.stackSize;
			return true;
		} else {
			return false;
		}
	}
}
