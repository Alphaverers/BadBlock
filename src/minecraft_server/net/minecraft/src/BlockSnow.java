package net.minecraft.src;

import java.util.Random;

public class BlockSnow extends Block {
	protected BlockSnow(int id, int blockIndex) {
		super(id, blockIndex, Material.snow);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		this.setTickOnLoad(true);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world1, int i2, int i3, int i4) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		int i5 = world1.getBlockId(i2, i3 - 1, i4);
		return i5 != 0 && Block.blockList[i5].isOpaqueCube() ? world1.getBlockMaterial(i2, i3 - 1, i4).getIsSolid() : false;
	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		this.canSnowStay(world1, i2, i3, i4);
	}

	private boolean canSnowStay(World world, int x, int y, int z) {
		if(!this.canPlaceBlockAt(world, x, y, z)) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
			world.setBlockWithNotify(x, y, z, 0);
			return false;
		} else {
			return true;
		}
	}

	public int idDropped(int i1, Random random2) {
		return Item.snowball.shiftedIndex;
	}

	public int quantityDropped(Random random1) {
		return 0;
	}

	public void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		if(world1.getSavedLightValue(EnumSkyBlock.Block, i2, i3, i4) > 11) {
			this.dropBlockAsItem(world1, i2, i3, i4, world1.getBlockMetadata(i2, i3, i4));
			world1.setBlockWithNotify(i2, i3, i4, 0);
		}

	}

	public boolean shouldSideBeRendered(IBlockAccess iBlockAccess1, int i2, int i3, int i4, int i5) {
		Material material6 = iBlockAccess1.getBlockMaterial(i2, i3, i4);
		return i5 == 1 ? true : (material6 == this.material ? false : super.shouldSideBeRendered(iBlockAccess1, i2, i3, i4, i5));
	}
}
