package net.minecraft.src;

public class ItemFood extends Item {
	private int healAmount;

	public ItemFood(int id, int healAmount) {
		super(id);
		this.healAmount = healAmount;
		this.maxStackSize = 1;
	}

	public ItemStack onItemRightClick(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3) {
		--itemStack1.stackSize;
		entityPlayer3.heal(this.healAmount);
		return itemStack1;
	}
}
