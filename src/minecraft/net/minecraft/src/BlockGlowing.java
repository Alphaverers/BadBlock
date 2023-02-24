package net.minecraft.src;

import java.util.Random;

public class BlockGlowing extends Block {
	int renderMode = 1;

	protected BlockGlowing(int i1, int i2, int i3) {
		super(i1, i2, Material.plants);
		this.setTickOnLoad(true);
		this.renderMode = i3;
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
		return this.renderMode;
	}

	public boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		return true;
	}

	public void updateTick(World worldObj, int x, int y, int z, Random rand) {
		super.updateTick(worldObj, x, y, z, rand);
		if(worldObj.getBlockMetadata(x, y, z) == 0) {
			this.onBlockAdded(worldObj, x, y, z);
		}

	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		if(this.h(world1, i2, i3, i4)) {
			int i6 = world1.getBlockMetadata(i2, i3, i4);
			boolean z7 = false;
			if(!world1.isBlockNormalCube(i2 - 1, i3, i4) && i6 == 1) {
				z7 = true;
			}

			if(!world1.isBlockNormalCube(i2 + 1, i3, i4) && i6 == 2) {
				z7 = true;
			}

			if(!world1.isBlockNormalCube(i2, i3, i4 - 1) && i6 == 3) {
				z7 = true;
			}

			if(!world1.isBlockNormalCube(i2, i3, i4 + 1) && i6 == 4) {
				z7 = true;
			}

			if(!world1.isBlockNormalCube(i2, i3 - 1, i4) && i6 == 5) {
				z7 = true;
			}

			if(z7) {
				this.dropBlockAsItem(world1, i2, i3, i4, world1.getBlockMetadata(i2, i3, i4));
				world1.setBlockWithNotify(i2, i3, i4, 0);
			}
		}

	}

	private boolean h(World world1, int i2, int i3, int i4) {
		if(!this.canPlaceBlockAt(world1, i2, i3, i4)) {
			this.dropBlockAsItem(world1, i2, i3, i4, world1.getBlockMetadata(i2, i3, i4));
			world1.setBlockWithNotify(i2, i3, i4, 0);
			return false;
		} else {
			return true;
		}
	}

	public void randomDisplayTick(World world1, int i2, int i3, int i4, Random random5) {
	}
}
