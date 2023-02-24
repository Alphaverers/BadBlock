package net.minecraft.src;

public class BlockBreakable extends Block {
	private boolean localFlag;

	protected BlockBreakable(int id, int blockIndex, Material material, boolean localFlag) {
		super(id, blockIndex, material);
		this.localFlag = localFlag;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean shouldSideBeRendered(IBlockAccess iBlockAccess1, int i2, int i3, int i4, int i5) {
		int i6 = iBlockAccess1.getBlockId(i2, i3, i4);
		return !this.localFlag && i6 == this.blockID ? false : super.shouldSideBeRendered(iBlockAccess1, i2, i3, i4, i5);
	}
}
