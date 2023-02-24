package net.minecraft.src;

public class BlockMushroom extends BlockFlower {
	protected BlockMushroom(int i1, int i2) {
		super(i1, i2);
		float f3 = 0.2F;
		this.setBlockBounds(0.5F - f3, 0.0F, 0.5F - f3, 0.5F + f3, f3 * 2.0F, 0.5F + f3);
	}

	protected boolean canThisPlantGrowOnThisBlockID(int i1) {
		return Block.opaqueCubeLookup[i1];
	}

	public boolean canBlockStay(World world1, int i2, int i3, int i4) {
		return world1.getBlockLightValue(i2, i3, i4) <= 13 && this.canThisPlantGrowOnThisBlockID(world1.getBlockId(i2, i3 - 1, i4));
	}
}
