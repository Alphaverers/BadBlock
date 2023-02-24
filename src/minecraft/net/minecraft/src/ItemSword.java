package net.minecraft.src;

public class ItemSword extends Item {
	private int weaponDamage;

	public ItemSword(int id, int strength) {
		super(id);
		this.maxStackSize = 1;
		this.maxDamage = 32 << strength;
		if(strength == 3) {
			this.maxDamage *= 4;
		}

		this.weaponDamage = 4 + strength * 2;
	}

	public float getStrVsBlock(ItemStack itemStack1, Block block2) {
		return 1.5F;
	}

	public void hitEntity(ItemStack itemStack1, EntityLiving entityLiving2) {
		itemStack1.damageItem(1);
	}

	public void onBlockDestroyed(ItemStack itemStack1, int i2, int i3, int i4, int i5) {
		itemStack1.damageItem(2);
	}

	public int getDamageVsEntity(Entity entity) {
		return this.weaponDamage;
	}

	public boolean isFull3D() {
		return true;
	}
}
