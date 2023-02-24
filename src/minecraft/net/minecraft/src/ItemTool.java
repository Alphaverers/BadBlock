package net.minecraft.src;

public class ItemTool extends Item {
	private Block[] blocksEffectiveAgainst;
	private float efficiencyOnProperMaterial = 4.0F;
	private int damageVsEntity;
	protected int toolMaterial;

	public ItemTool(int id, int attackDmg, int strength, Block[] blocks) {
		super(id);
		this.toolMaterial = strength;
		this.blocksEffectiveAgainst = blocks;
		this.maxStackSize = 1;
		this.maxDamage = 32 << strength;
		if(strength == 3) {
			this.maxDamage *= 4;
		}

		this.efficiencyOnProperMaterial = (float)((strength + 1) * 2);
		this.damageVsEntity = attackDmg + strength;
	}

	public float getStrVsBlock(ItemStack itemStack1, Block block2) {
		for(int i3 = 0; i3 < this.blocksEffectiveAgainst.length; ++i3) {
			if(this.blocksEffectiveAgainst[i3] == block2) {
				return this.efficiencyOnProperMaterial;
			}
		}

		return 1.0F;
	}

	public void hitEntity(ItemStack itemStack1, EntityLiving entityLiving2) {
		itemStack1.damageItem(2);
	}

	public void onBlockDestroyed(ItemStack itemStack1, int i2, int i3, int i4, int i5) {
		itemStack1.damageItem(1);
	}

	public int getDamageVsEntity(Entity entity) {
		return this.damageVsEntity;
	}

	public boolean isFull3D() {
		return true;
	}
}
