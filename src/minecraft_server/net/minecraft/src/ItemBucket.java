package net.minecraft.src;

public class ItemBucket extends Item {
	private int isFull;

	public ItemBucket(int itemID, int isFull) {
		super(itemID);
		this.maxStackSize = 1;
		this.maxDamage = 64;
		this.isFull = isFull;
	}
}
