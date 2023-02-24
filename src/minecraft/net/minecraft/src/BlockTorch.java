package net.minecraft.src;

import java.util.Random;

public class BlockTorch extends Block {
	protected BlockTorch(int id, int tex) {
		super(id, tex, Material.circuits);
		this.setTickOnLoad(true);
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
		return 2;
	}

	public boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		return world1.isBlockNormalCube(i2 - 1, i3, i4) ? true : (world1.isBlockNormalCube(i2 + 1, i3, i4) ? true : (world1.isBlockNormalCube(i2, i3, i4 - 1) ? true : (world1.isBlockNormalCube(i2, i3, i4 + 1) ? true : world1.isBlockNormalCube(i2, i3 - 1, i4))));
	}

	public void onBlockPlaced(World world1, int i2, int i3, int i4, int i5) {
		int i6 = world1.getBlockMetadata(i2, i3, i4);
		if(i5 == 1 && world1.isBlockNormalCube(i2, i3 - 1, i4)) {
			i6 = 5;
		}

		if(i5 == 2 && world1.isBlockNormalCube(i2, i3, i4 + 1)) {
			i6 = 4;
		}

		if(i5 == 3 && world1.isBlockNormalCube(i2, i3, i4 - 1)) {
			i6 = 3;
		}

		if(i5 == 4 && world1.isBlockNormalCube(i2 + 1, i3, i4)) {
			i6 = 2;
		}

		if(i5 == 5 && world1.isBlockNormalCube(i2 - 1, i3, i4)) {
			i6 = 1;
		}

		world1.setBlockMetadataWithNotify(i2, i3, i4, i6);
	}

	public void updateTick(World worldObj, int x, int y, int z, Random rand) {
		super.updateTick(worldObj, x, y, z, rand);
		if(worldObj.getBlockMetadata(x, y, z) == 0) {
			this.onBlockAdded(worldObj, x, y, z);
		}

	}

	public void onBlockAdded(World world1, int i2, int i3, int i4) {
		if(world1.isBlockNormalCube(i2 - 1, i3, i4)) {
			world1.setBlockMetadataWithNotify(i2, i3, i4, 1);
		} else if(world1.isBlockNormalCube(i2 + 1, i3, i4)) {
			world1.setBlockMetadataWithNotify(i2, i3, i4, 2);
		} else if(world1.isBlockNormalCube(i2, i3, i4 - 1)) {
			world1.setBlockMetadataWithNotify(i2, i3, i4, 3);
		} else if(world1.isBlockNormalCube(i2, i3, i4 + 1)) {
			world1.setBlockMetadataWithNotify(i2, i3, i4, 4);
		} else if(world1.isBlockNormalCube(i2, i3 - 1, i4)) {
			world1.setBlockMetadataWithNotify(i2, i3, i4, 5);
		}

		this.checkIfAttachedToBlock(world1, i2, i3, i4);
	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		if(this.checkIfAttachedToBlock(world1, i2, i3, i4)) {
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

	private boolean checkIfAttachedToBlock(World worldObj, int x, int y, int z) {
		if(!this.canPlaceBlockAt(worldObj, x, y, z)) {
			this.dropBlockAsItem(worldObj, x, y, z, worldObj.getBlockMetadata(x, y, z));
			worldObj.setBlockWithNotify(x, y, z, 0);
			return false;
		} else {
			return true;
		}
	}

	public MovingObjectPosition collisionRayTrace(World world1, int i2, int i3, int i4, Vec3D vec3D5, Vec3D vec3D6) {
		int i7 = world1.getBlockMetadata(i2, i3, i4) & 7;
		float f8 = 0.15F;
		if(i7 == 1) {
			this.setBlockBounds(0.0F, 0.2F, 0.5F - f8, f8 * 2.0F, 0.8F, 0.5F + f8);
		} else if(i7 == 2) {
			this.setBlockBounds(1.0F - f8 * 2.0F, 0.2F, 0.5F - f8, 1.0F, 0.8F, 0.5F + f8);
		} else if(i7 == 3) {
			this.setBlockBounds(0.5F - f8, 0.2F, 0.0F, 0.5F + f8, 0.8F, f8 * 2.0F);
		} else if(i7 == 4) {
			this.setBlockBounds(0.5F - f8, 0.2F, 1.0F - f8 * 2.0F, 0.5F + f8, 0.8F, 1.0F);
		} else {
			f8 = 0.1F;
			this.setBlockBounds(0.5F - f8, 0.0F, 0.5F - f8, 0.5F + f8, 0.6F, 0.5F + f8);
		}

		return super.collisionRayTrace(world1, i2, i3, i4, vec3D5, vec3D6);
	}

	public void randomDisplayTick(World world1, int i2, int i3, int i4, Random random5) {
		int i6 = world1.getBlockMetadata(i2, i3, i4);
		double d7 = (double)((float)i2 + 0.5F);
		double d9 = (double)((float)i3 + 0.7F);
		double d11 = (double)((float)i4 + 0.5F);
		double d13 = (double)0.22F;
		double d15 = (double)0.27F;
		if(i6 == 1) {
			world1.spawnParticle("smoke", d7 - d15, d9 + d13, d11, 0.0D, 0.0D, 0.0D);
			world1.spawnParticle("flame", d7 - d15, d9 + d13, d11, 0.0D, 0.0D, 0.0D);
		} else if(i6 == 2) {
			world1.spawnParticle("smoke", d7 + d15, d9 + d13, d11, 0.0D, 0.0D, 0.0D);
			world1.spawnParticle("flame", d7 + d15, d9 + d13, d11, 0.0D, 0.0D, 0.0D);
		} else if(i6 == 3) {
			world1.spawnParticle("smoke", d7, d9 + d13, d11 - d15, 0.0D, 0.0D, 0.0D);
			world1.spawnParticle("flame", d7, d9 + d13, d11 - d15, 0.0D, 0.0D, 0.0D);
		} else if(i6 == 4) {
			world1.spawnParticle("smoke", d7, d9 + d13, d11 + d15, 0.0D, 0.0D, 0.0D);
			world1.spawnParticle("flame", d7, d9 + d13, d11 + d15, 0.0D, 0.0D, 0.0D);
		} else {
			world1.spawnParticle("smoke", d7, d9, d11, 0.0D, 0.0D, 0.0D);
			world1.spawnParticle("flame", d7, d9, d11, 0.0D, 0.0D, 0.0D);
		}

	}
}
