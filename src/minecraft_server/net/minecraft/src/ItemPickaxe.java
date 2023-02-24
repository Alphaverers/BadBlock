package net.minecraft.src;

public class ItemPickaxe extends ItemTool {
	private static Block[] blocksEffectiveAgainst = new Block[]{Block.cobblestone, Block.stairDouble, Block.stairSingle, Block.stone, Block.cobblestoneMossy, Block.oreIron, Block.blockSteel, Block.oreCoal, Block.blockGold, Block.oreGold, Block.oreDiamond, Block.blockDiamond, Block.ice};
	private int harvestLevel;

	public ItemPickaxe(int itemID, int harvestLevelAndMaterial) {
		super(itemID, 2, harvestLevelAndMaterial, blocksEffectiveAgainst);
		this.harvestLevel = harvestLevelAndMaterial;
	}

	public boolean canHarvestBlock(Block block1) {
		return block1 == Block.obsidian ? this.harvestLevel == 3 : (block1 != Block.blockDiamond && block1 != Block.oreDiamond ? (block1 != Block.blockGold && block1 != Block.oreGold ? (block1 != Block.blockSteel && block1 != Block.oreIron ? (block1 != Block.oreRedstone && block1 != Block.oreRedstoneGlowing ? (block1.material == Material.rock ? true : block1.material == Material.iron) : this.harvestLevel >= 2) : this.harvestLevel >= 1) : this.harvestLevel >= 2) : this.harvestLevel >= 2);
	}
}
