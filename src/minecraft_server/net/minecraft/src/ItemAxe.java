package net.minecraft.src;

public class ItemAxe extends ItemTool {
	private static Block[] blocksEffectiveAgainst = new Block[]{Block.planks, Block.bookshelf, Block.wood, Block.crate};

	public ItemAxe(int itemID, int material) {
		super(itemID, 3, material, blocksEffectiveAgainst);
	}
}
