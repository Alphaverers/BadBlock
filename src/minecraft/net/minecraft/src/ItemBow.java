package net.minecraft.src;

public class ItemBow extends Item {
	public ItemBow(int i1) {
		super(i1);
		this.maxStackSize = 1;
	}

	public ItemStack onItemRightClick(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3) {
		if(entityPlayer3.inventory.consumeInventoryItem(Item.arrow.shiftedIndex)) {
			world2.playSoundAtEntity(entityPlayer3, "random.bow", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
			world2.spawnEntityInWorld(new EntityArrow(world2, entityPlayer3));
		}

		return itemStack1;
	}
}
