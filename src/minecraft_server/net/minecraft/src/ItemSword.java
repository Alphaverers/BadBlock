package net.minecraft.src;

public class ItemSword extends Item {
	private int weaponDamage;

	public ItemSword(int itemID, int maxDamage) {
		super(itemID);
		this.maxStackSize = 1;
		this.maxDamage = 32 << maxDamage;
		if(maxDamage == 3) {
			this.maxDamage *= 4;
		}

		this.weaponDamage = 4 + maxDamage * 2;
	}

	public float getStrVsBlock(ItemStack itemStack1, Block block2) {
		return 1.5F;
	}

	public void onBlockDestroyed(ItemStack itemStack1, int i2, int i3, int i4, int i5) {
		itemStack1.damageItem(2);
	}
}
