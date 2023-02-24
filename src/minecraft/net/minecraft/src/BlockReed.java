package net.minecraft.src;

import java.util.Random;

public class BlockReed extends Block {
	protected BlockReed(int id, int tex) {
		super(id, Material.plants);
		this.blockIndexInTexture = tex;
		float f3 = 0.375F;
		this.setBlockBounds(0.5F - f3, 0.0F, 0.5F - f3, 0.5F + f3, 1.0F, 0.5F + f3);
		this.setTickOnLoad(true);
	}

	public void updateTick(World worldObj, int x, int y, int z, Random rand) {
		if(worldObj.getBlockId(x, y + 1, z) == 0) {
			int i6;
			for(i6 = 1; worldObj.getBlockId(x, y - i6, z) == this.blockID; ++i6) {
			}

			if(i6 < 3) {
				int i7 = worldObj.getBlockMetadata(x, y, z);
				if(i7 == 15) {
					worldObj.setBlockWithNotify(x, y + 1, z, this.blockID);
					worldObj.setBlockMetadataWithNotify(x, y, z, 0);
				} else {
					worldObj.setBlockMetadataWithNotify(x, y, z, i7 + 1);
				}
			}
		}

	}

	public boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		int i5 = world1.getBlockId(i2, i3 - 1, i4);
		return i5 == this.blockID ? true : (i5 != Block.grass.blockID && i5 != Block.dirt.blockID ? false : (world1.getBlockMaterial(i2 - 1, i3 - 1, i4) == Material.water ? true : (world1.getBlockMaterial(i2 + 1, i3 - 1, i4) == Material.water ? true : (world1.getBlockMaterial(i2, i3 - 1, i4 - 1) == Material.water ? true : world1.getBlockMaterial(i2, i3 - 1, i4 + 1) == Material.water))));
	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		this.checkBlockCoordValid(world1, i2, i3, i4);
	}

	protected final void checkBlockCoordValid(World worldObj, int x, int y, int z) {
		if(!this.canBlockStay(worldObj, x, y, z)) {
			this.dropBlockAsItem(worldObj, x, y, z, worldObj.getBlockMetadata(x, y, z));
			worldObj.setBlockWithNotify(x, y, z, 0);
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

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 1;
	}
}
