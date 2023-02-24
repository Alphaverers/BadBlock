package net.minecraft.src;

public class ItemFood extends Item {
	private int healAmount;

	public ItemFood(int itemID, int healAmount) {
		super(itemID);
		this.healAmount = healAmount;
		this.maxStackSize = 1;
	}
}
