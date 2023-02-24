package net.minecraft.src;

public class BlockLeavesBase extends Block {
	protected boolean graphicsLevel;

	protected BlockLeavesBase(int id, int blockIndex, Material material, boolean graphicsLevel) {
		super(id, blockIndex, material);
		this.graphicsLevel = graphicsLevel;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean shouldSideBeRendered(IBlockAccess iBlockAccess1, int i2, int i3, int i4, int i5) {
		int i6 = iBlockAccess1.getBlockId(i2, i3, i4);
		return !this.graphicsLevel && i6 == this.blockID ? false : super.shouldSideBeRendered(iBlockAccess1, i2, i3, i4, i5);
	}
}
