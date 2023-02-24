package net.minecraft.src;

import java.util.Random;

public class BlockFlower extends Block {
	protected BlockFlower(int id, int tex) {
		super(id, Material.plants);
		this.blockIndexInTexture = tex;
		this.setTickOnLoad(true);
		float f3 = 0.2F;
		this.setBlockBounds(0.5F - f3, 0.0F, 0.5F - f3, 0.5F + f3, f3 * 3.0F, 0.5F + f3);
	}

	public boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		return this.canThisPlantGrowOnThisBlockID(world1.getBlockId(i2, i3 - 1, i4));
	}

	protected boolean canThisPlantGrowOnThisBlockID(int blockID) {
		return blockID == Block.grass.blockID || blockID == Block.dirt.blockID || blockID == Block.tilledField.blockID;
	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		super.onNeighborBlockChange(world1, i2, i3, i4, i5);
		this.checkFlowerChange(world1, i2, i3, i4);
	}

	public void updateTick(World worldObj, int x, int y, int z, Random rand) {
		this.checkFlowerChange(worldObj, x, y, z);
	}

	protected final void checkFlowerChange(World worldObj, int x, int y, int z) {
		if(!this.canBlockStay(worldObj, x, y, z)) {
			this.dropBlockAsItem(worldObj, x, y, z, worldObj.getBlockMetadata(x, y, z));
			worldObj.setBlockWithNotify(x, y, z, 0);
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

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 1;
	}
}
