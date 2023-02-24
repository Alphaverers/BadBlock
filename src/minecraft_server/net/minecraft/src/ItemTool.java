package net.minecraft.src;

public class ItemTool extends Item {
	private Block[] blocksEffectiveAgainst;
	private float efficiencyOnProperMaterial = 4.0F;
	private int damageVsEntity;
	protected int toolMaterial;

	public ItemTool(int itemID, int damage, int toolMaterial, Block[] effectiveBlocks) {
		super(itemID);
		this.toolMaterial = toolMaterial;
		this.blocksEffectiveAgainst = effectiveBlocks;
		this.maxStackSize = 1;
		this.maxDamage = 32 << toolMaterial;
		if(toolMaterial == 3) {
			this.maxDamage *= 4;
		}

		this.efficiencyOnProperMaterial = (float)((toolMaterial + 1) * 2);
		this.damageVsEntity = damage + toolMaterial;
	}

	public float getStrVsBlock(ItemStack itemStack1, Block block2) {
		for(int i3 = 0; i3 < this.blocksEffectiveAgainst.length; ++i3) {
			if(this.blocksEffectiveAgainst[i3] == block2) {
				return this.efficiencyOnProperMaterial;
			}
		}

		return 1.0F;
	}

	public void onBlockDestroyed(ItemStack itemStack1, int i2, int i3, int i4, int i5) {
		itemStack1.damageItem(1);
	}
}
