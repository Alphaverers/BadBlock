package net.minecraft.src;

public class BlockLever extends Block {
	protected BlockLever(int id, int tex) {
		super(id, tex, Material.circuits);
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
		return 12;
	}

	public boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		return world1.isBlockNormalCube(i2 - 1, i3, i4) ? true : (world1.isBlockNormalCube(i2 + 1, i3, i4) ? true : (world1.isBlockNormalCube(i2, i3, i4 - 1) ? true : (world1.isBlockNormalCube(i2, i3, i4 + 1) ? true : world1.isBlockNormalCube(i2, i3 - 1, i4))));
	}

	public void onBlockPlaced(World world1, int i2, int i3, int i4, int i5) {
		int i6 = world1.getBlockMetadata(i2, i3, i4);
		int i7 = i6 & 8;
		i6 &= 7;
		if(i5 == 1 && world1.isBlockNormalCube(i2, i3 - 1, i4)) {
			i6 = 5 + world1.rand.nextInt(2);
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

		world1.setBlockMetadataWithNotify(i2, i3, i4, i6 + i7);
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
			world1.setBlockMetadataWithNotify(i2, i3, i4, 5 + world1.rand.nextInt(2));
		}

		this.checkIfAttachedToBlock(world1, i2, i3, i4);
	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		if(this.checkIfAttachedToBlock(world1, i2, i3, i4)) {
			int i6 = world1.getBlockMetadata(i2, i3, i4) & 7;
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

	public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess1, int i2, int i3, int i4) {
		int i5 = iBlockAccess1.getBlockMetadata(i2, i3, i4) & 7;
		float f6 = 0.1875F;
		if(i5 == 1) {
			this.setBlockBounds(0.0F, 0.2F, 0.5F - f6, f6 * 2.0F, 0.8F, 0.5F + f6);
		} else if(i5 == 2) {
			this.setBlockBounds(1.0F - f6 * 2.0F, 0.2F, 0.5F - f6, 1.0F, 0.8F, 0.5F + f6);
		} else if(i5 == 3) {
			this.setBlockBounds(0.5F - f6, 0.2F, 0.0F, 0.5F + f6, 0.8F, f6 * 2.0F);
		} else if(i5 == 4) {
			this.setBlockBounds(0.5F - f6, 0.2F, 1.0F - f6 * 2.0F, 0.5F + f6, 0.8F, 1.0F);
		} else {
			f6 = 0.25F;
			this.setBlockBounds(0.5F - f6, 0.0F, 0.5F - f6, 0.5F + f6, 0.6F, 0.5F + f6);
		}

	}

	public void onBlockClicked(World worldObj, int x, int y, int z, EntityPlayer entityPlayer) {
		this.blockActivated(worldObj, x, y, z, entityPlayer);
	}

	public boolean blockActivated(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
		int i6 = world1.getBlockMetadata(i2, i3, i4);
		int i7 = i6 & 7;
		int i8 = 8 - (i6 & 8);
		world1.setBlockMetadataWithNotify(i2, i3, i4, i7 + i8);
		world1.markBlocksDirty(i2, i3, i4, i2, i3, i4);
		world1.playSoundEffect((double)i2 + 0.5D, (double)i3 + 0.5D, (double)i4 + 0.5D, "random.click", 0.3F, i8 > 0 ? 0.6F : 0.5F);
		world1.notifyBlocksOfNeighborChange(i2, i3, i4, this.blockID);
		if(i7 == 1) {
			world1.notifyBlocksOfNeighborChange(i2 - 1, i3, i4, this.blockID);
		} else if(i7 == 2) {
			world1.notifyBlocksOfNeighborChange(i2 + 1, i3, i4, this.blockID);
		} else if(i7 == 3) {
			world1.notifyBlocksOfNeighborChange(i2, i3, i4 - 1, this.blockID);
		} else if(i7 == 4) {
			world1.notifyBlocksOfNeighborChange(i2, i3, i4 + 1, this.blockID);
		} else {
			world1.notifyBlocksOfNeighborChange(i2, i3 - 1, i4, this.blockID);
		}

		return true;
	}

	public void onBlockRemoval(World world1, int i2, int i3, int i4) {
		int i5 = world1.getBlockMetadata(i2, i3, i4);
		if((i5 & 8) > 0) {
			world1.notifyBlocksOfNeighborChange(i2, i3, i4, this.blockID);
			int i6 = i5 & 7;
			if(i6 == 1) {
				world1.notifyBlocksOfNeighborChange(i2 - 1, i3, i4, this.blockID);
			} else if(i6 == 2) {
				world1.notifyBlocksOfNeighborChange(i2 + 1, i3, i4, this.blockID);
			} else if(i6 == 3) {
				world1.notifyBlocksOfNeighborChange(i2, i3, i4 - 1, this.blockID);
			} else if(i6 == 4) {
				world1.notifyBlocksOfNeighborChange(i2, i3, i4 + 1, this.blockID);
			} else {
				world1.notifyBlocksOfNeighborChange(i2, i3 - 1, i4, this.blockID);
			}
		}

		super.onBlockRemoval(world1, i2, i3, i4);
	}

	public boolean isPoweringTo(IBlockAccess iBlockAccess1, int i2, int i3, int i4, int i5) {
		return (iBlockAccess1.getBlockMetadata(i2, i3, i4) & 8) > 0;
	}

	public boolean isIndirectlyPoweringTo(World world1, int i2, int i3, int i4, int i5) {
		int i6 = world1.getBlockMetadata(i2, i3, i4);
		if((i6 & 8) == 0) {
			return false;
		} else {
			int i7 = i6 & 7;
			return i7 == 5 && i5 == 1 ? true : (i7 == 4 && i5 == 2 ? true : (i7 == 3 && i5 == 3 ? true : (i7 == 2 && i5 == 4 ? true : i7 == 1 && i5 == 5)));
		}
	}

	public boolean canProvidePower() {
		return true;
	}
}
