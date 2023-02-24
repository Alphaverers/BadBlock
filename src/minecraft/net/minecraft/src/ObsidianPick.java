package net.minecraft.src;

public class ObsidianPick extends ItemTool {
	private static Block[] aW = new Block[]{Block.cobblestone, Block.bedrock, Block.stairDouble, Block.stairSingle, Block.stone, Block.cobblestoneMossy, Block.oreIron, Block.blockSteel, Block.oreCoal, Block.blockGold, Block.oreGold, Block.oreDiamond, Block.blockDiamond, Block.ice};
	private int aX;

	public ObsidianPick(int i1, int i2) {
		super(i1, 2, i2, aW);
		this.aX = i2;
	}

	public boolean canHarvestBlock(Block block1) {
		return block1 == Block.bedrock ? true : (block1 == Block.obsidian ? this.aX == 3 : (block1 != Block.blockDiamond && block1 != Block.oreDiamond ? (block1 != Block.blockGold && block1 != Block.oreGold ? (block1 != Block.blockSteel && block1 != Block.oreIron ? (block1 != Block.oreRedstone && block1 != Block.oreRedstoneGlowing ? block1.material == Material.rock || block1.material == Material.iron : this.aX >= 2) : this.aX >= 1) : this.aX >= 2) : this.aX >= 2));
	}
}
