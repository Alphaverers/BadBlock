package net.minecraft.src;

import java.util.Random;

public class BlockCactus extends Block {
	protected BlockCactus(int id, int tex) {
		super(id, tex, Material.cactus);
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

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world1, int i2, int i3, int i4) {
		float f5 = 0.0625F;
		return AxisAlignedBB.getBoundingBoxFromPool((double)((float)i2 + f5), (double)i3, (double)((float)i4 + f5), (double)((float)(i2 + 1) - f5), (double)((float)(i3 + 1) - f5), (double)((float)(i4 + 1) - f5));
	}

	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world1, int i2, int i3, int i4) {
		float f5 = 0.0625F;
		return AxisAlignedBB.getBoundingBoxFromPool((double)((float)i2 + f5), (double)i3, (double)((float)i4 + f5), (double)((float)(i2 + 1) - f5), (double)(i3 + 1), (double)((float)(i4 + 1) - f5));
	}

	public int getBlockTextureFromSide(int i1) {
		return i1 == 1 ? this.blockIndexInTexture - 1 : (i1 == 0 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture);
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 13;
	}

	public boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		return !super.canPlaceBlockAt(world1, i2, i3, i4) ? false : this.canBlockStay(world1, i2, i3, i4);
	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		if(!this.canBlockStay(world1, i2, i3, i4)) {
			this.dropBlockAsItem(world1, i2, i3, i4, world1.getBlockMetadata(i2, i3, i4));
			world1.setBlockWithNotify(i2, i3, i4, 0);
		}

	}

	public boolean canBlockStay(World world1, int i2, int i3, int i4) {
		if(world1.getBlockMaterial(i2 - 1, i3, i4).isSolid()) {
			return false;
		} else if(world1.getBlockMaterial(i2 + 1, i3, i4).isSolid()) {
			return false;
		} else if(world1.getBlockMaterial(i2, i3, i4 - 1).isSolid()) {
			return false;
		} else if(world1.getBlockMaterial(i2, i3, i4 + 1).isSolid()) {
			return false;
		} else {
			int i5 = world1.getBlockId(i2, i3 - 1, i4);
			return i5 == Block.cactus.blockID || i5 == Block.sand.blockID;
		}
	}

	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		entity.attackEntityFrom((Entity)null, 1);
	}
}
