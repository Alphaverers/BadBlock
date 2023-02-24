package net.minecraft.src;

import java.util.ArrayList;

public class BlockFence extends Block {
	public BlockFence(int id, int blockIndex) {
		super(id, blockIndex, Material.wood);
	}

	public void getCollidingBoundingBoxes(World world1, int i2, int i3, int i4, AxisAlignedBB axisAlignedBB5, ArrayList arrayList6) {
		arrayList6.add(AxisAlignedBB.getBoundingBoxFromPool((double)i2, (double)i3, (double)i4, (double)(i2 + 1), (double)i3 + 1.5D, (double)(i4 + 1)));
	}

	public boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		return world1.getBlockId(i2, i3 - 1, i4) == this.blockID ? false : (!world1.getBlockMaterial(i2, i3 - 1, i4).isSolid() ? false : super.canPlaceBlockAt(world1, i2, i3, i4));
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 11;
	}
}
