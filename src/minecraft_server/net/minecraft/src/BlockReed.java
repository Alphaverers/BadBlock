package net.minecraft.src;

import java.util.Random;

public class BlockReed extends Block {
	protected BlockReed(int id, int blockIndex) {
		super(id, Material.plants);
		this.blockIndexInTexture = blockIndex;
		float f3 = 0.375F;
		this.setBlockBounds(0.5F - f3, 0.0F, 0.5F - f3, 0.5F + f3, 1.0F, 0.5F + f3);
		this.setTickOnLoad(true);
	}

	public void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		if(world1.getBlockId(i2, i3 + 1, i4) == 0) {
			int i6;
			for(i6 = 1; world1.getBlockId(i2, i3 - i6, i4) == this.blockID; ++i6) {
			}

			if(i6 < 3) {
				int i7 = world1.getBlockMetadata(i2, i3, i4);
				if(i7 == 15) {
					world1.setBlockWithNotify(i2, i3 + 1, i4, this.blockID);
					world1.setBlockMetadataWithNotify(i2, i3, i4, 0);
				} else {
					world1.setBlockMetadataWithNotify(i2, i3, i4, i7 + 1);
				}
			}
		}

	}

	public boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		int i5 = world1.getBlockId(i2, i3 - 1, i4);
		return i5 == this.blockID ? true : (i5 != Block.grass.blockID && i5 != Block.dirt.blockID ? false : (world1.getBlockMaterial(i2 - 1, i3 - 1, i4) == Material.water ? true : (world1.getBlockMaterial(i2 + 1, i3 - 1, i4) == Material.water ? true : (world1.getBlockMaterial(i2, i3 - 1, i4 - 1) == Material.water ? true : world1.getBlockMaterial(i2, i3 - 1, i4 + 1) == Material.water))));
	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		this.fallInstantly(world1, i2, i3, i4);
	}

	protected final void fallInstantly(World world, int x, int y, int z) {
		if(!this.canBlockStay(world, x, y, z)) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
			world.setBlockWithNotify(x, y, z, 0);
		}

	}

	public boolean canBlockStay(World world1, int i2, int i3, int i4) {
		return this.canPlaceBlockAt(world1, i2, i3, i4);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world1, int i2, int i3, int i4) {
		return null;
	}

	public int idDropped(int i1, Random random2) {
		return Item.reed.shiftedIndex;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 1;
	}
}
