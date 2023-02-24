package net.minecraft.src;

import java.util.Random;

public class BlockFlower extends Block {
	protected BlockFlower(int id, int blockIndex) {
		super(id, Material.plants);
		this.blockIndexInTexture = blockIndex;
		this.setTickOnLoad(true);
		float f3 = 0.2F;
		this.setBlockBounds(0.5F - f3, 0.0F, 0.5F - f3, 0.5F + f3, f3 * 3.0F, 0.5F + f3);
	}

	public boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		return this.canThisPlantGrowOnThisBlockID(world1.getBlockId(i2, i3 - 1, i4));
	}

	protected boolean canThisPlantGrowOnThisBlockID(int id) {
		return id == Block.grass.blockID || id == Block.dirt.blockID || id == Block.tilledField.blockID;
	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		super.onNeighborBlockChange(world1, i2, i3, i4, i5);
		this.dropIfNeighborPlaced(world1, i2, i3, i4);
	}

	public void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		this.dropIfNeighborPlaced(world1, i2, i3, i4);
	}

	protected final void dropIfNeighborPlaced(World world, int x, int y, int z) {
		if(!this.canBlockStay(world, x, y, z)) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
			world.setBlockWithNotify(x, y, z, 0);
		}

	}

	public boolean canBlockStay(World world1, int i2, int i3, int i4) {
		return (world1.getBlockLightValue(i2, i3, i4) >= 8 || world1.canBlockSeeTheSky(i2, i3, i4)) && this.canThisPlantGrowOnThisBlockID(world1.getBlockId(i2, i3 - 1, i4));
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world1, int i2, int i3, int i4) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 1;
	}
}
