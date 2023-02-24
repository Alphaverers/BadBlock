package net.minecraft.src;

import java.util.Random;

public class BlockStep extends Block {
	private boolean blockType;

	public BlockStep(int id, boolean blockType) {
		super(id, 6, Material.rock);
		this.blockType = blockType;
		if(!blockType) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		}

		this.setLightOpacity(255);
	}

	public int getBlockTextureFromSide(int i1) {
		return i1 <= 1 ? 6 : 5;
	}

	public boolean isOpaqueCube() {
		return this.blockType;
	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		if(this == Block.stairSingle) {
			;
		}
	}

	public void onBlockAdded(World world1, int i2, int i3, int i4) {
		if(this != Block.stairSingle) {
			super.onBlockAdded(world1, i2, i3, i4);
		}

		int i5 = world1.getBlockId(i2, i3 - 1, i4);
		if(i5 == stairSingle.blockID) {
			world1.setBlockWithNotify(i2, i3, i4, 0);
			world1.setBlockWithNotify(i2, i3 - 1, i4, Block.stairDouble.blockID);
		}

	}

	public int idDropped(int i1, Random random2) {
		return Block.stairSingle.blockID;
	}

	public boolean renderAsNormalBlock() {
		return this.blockType;
	}

	public boolean shouldSideBeRendered(IBlockAccess iBlockAccess1, int i2, int i3, int i4, int i5) {
		if(this != Block.stairSingle) {
			super.shouldSideBeRendered(iBlockAccess1, i2, i3, i4, i5);
		}

		return i5 == 1 ? true : (!super.shouldSideBeRendered(iBlockAccess1, i2, i3, i4, i5) ? false : (i5 == 0 ? true : iBlockAccess1.getBlockId(i2, i3, i4) != this.blockID));
	}
}
