package net.minecraft.src;

public class ItemSpade extends ItemTool {
	private static Block[] blocksEffectiveAgainst = new Block[]{Block.grass, Block.dirt, Block.sand, Block.gravel, Block.snow, Block.blockSnow, Block.blockClay};

	public ItemSpade(int itemID, int material) {
		super(itemID, 1, material, blocksEffectiveAgainst);
	}

	public boolean canHarvestBlock(Block block1) {
		return block1 == Block.snow ? true : block1 == Block.blockSnow;
	}
}
