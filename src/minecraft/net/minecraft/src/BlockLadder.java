package net.minecraft.src;

import java.util.Random;

public class BlockLadder extends Block {
	protected BlockLadder(int blockID, int tex) {
		super(blockID, tex, Material.circuits);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world1, int i2, int i3, int i4) {
		int i5 = world1.getBlockMetadata(i2, i3, i4);
		float f6 = 0.125F;
		if(i5 == 2) {
			this.setBlockBounds(0.0F, 0.0F, 1.0F - f6, 1.0F, 1.0F, 1.0F);
		}

		if(i5 == 3) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f6);
		}

		if(i5 == 4) {
			this.setBlockBounds(1.0F - f6, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}

		if(i5 == 5) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, f6, 1.0F, 1.0F);
		}

		return super.getCollisionBoundingBoxFromPool(world1, i2, i3, i4);
	}

	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world1, int i2, int i3, int i4) {
		int i5 = world1.getBlockMetadata(i2, i3, i4);
		float f6 = 0.125F;
		if(i5 == 2) {
			this.setBlockBounds(0.0F, 0.0F, 1.0F - f6, 1.0F, 1.0F, 1.0F);
		}

		if(i5 == 3) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f6);
		}

		if(i5 == 4) {
			this.setBlockBounds(1.0F - f6, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}

		if(i5 == 5) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, f6, 1.0F, 1.0F);
		}

		return super.getSelectedBoundingBoxFromPool(world1, i2, i3, i4);
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 8;
	}

	public boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		return world1.isBlockNormalCube(i2 - 1, i3, i4) ? true : (world1.isBlockNormalCube(i2 + 1, i3, i4) ? true : (world1.isBlockNormalCube(i2, i3, i4 - 1) ? true : world1.isBlockNormalCube(i2, i3, i4 + 1)));
	}

	public void onBlockPlaced(World world1, int i2, int i3, int i4, int i5) {
		int i6 = world1.getBlockMetadata(i2, i3, i4);
		if((i6 == 0 || i5 == 2) && world1.isBlockNormalCube(i2, i3, i4 + 1)) {
			i6 = 2;
		}

		if((i6 == 0 || i5 == 3) && world1.isBlockNormalCube(i2, i3, i4 - 1)) {
			i6 = 3;
		}

		if((i6 == 0 || i5 == 4) && world1.isBlockNormalCube(i2 + 1, i3, i4)) {
			i6 = 4;
		}

		if((i6 == 0 || i5 == 5) && world1.isBlockNormalCube(i2 - 1, i3, i4)) {
			i6 = 5;
		}

		world1.setBlockMetadataWithNotify(i2, i3, i4, i6);
	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		int i6 = world1.getBlockMetadata(i2, i3, i4);
		boolean z7 = false;
		if(i6 == 2 && world1.isBlockNormalCube(i2, i3, i4 + 1)) {
			z7 = true;
		}

		if(i6 == 3 && world1.isBlockNormalCube(i2, i3, i4 - 1)) {
			z7 = true;
		}

		if(i6 == 4 && world1.isBlockNormalCube(i2 + 1, i3, i4)) {
			z7 = true;
		}

		if(i6 == 5 && world1.isBlockNormalCube(i2 - 1, i3, i4)) {
			z7 = true;
		}

		if(!z7) {
			this.dropBlockAsItem(world1, i2, i3, i4, i6);
			world1.setBlockWithNotify(i2, i3, i4, 0);
		}

		super.onNeighborBlockChange(world1, i2, i3, i4, i5);
	}

	public int quantityDropped(Random random1) {
		return 1;
	}
}
