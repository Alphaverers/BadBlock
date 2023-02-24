package net.minecraft.src;

import java.util.Random;

public class BlockDoor extends Block {
	protected BlockDoor(int i1, Material material2) {
		super(i1, material2);
		this.blockIndexInTexture = 97;
		if(material2 == Material.iron) {
			++this.blockIndexInTexture;
		}

		float f3 = 0.5F;
		float f4 = 1.0F;
		this.setBlockBounds(0.5F - f3, 0.0F, 0.5F - f3, 0.5F + f3, f4, 0.5F + f3);
	}

	public int getBlockTextureFromSideAndMetadata(int i1, int i2) {
		if(i1 != 0 && i1 != 1) {
			int i3 = this.getState(i2);
			if((i3 == 0 || i3 == 2) ^ i1 <= 3) {
				return this.blockIndexInTexture;
			} else {
				int i4 = i3 / 2 + (i1 & 1 ^ i3);
				i4 += (i2 & 4) / 4;
				int i5 = this.blockIndexInTexture - (i2 & 8) * 2;
				if((i4 & 1) != 0) {
					i5 = -i5;
				}

				return i5;
			}
		} else {
			return this.blockIndexInTexture;
		}
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 7;
	}

	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world1, int i2, int i3, int i4) {
		this.setBlockBoundsBasedOnState(world1, i2, i3, i4);
		return super.getSelectedBoundingBoxFromPool(world1, i2, i3, i4);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world1, int i2, int i3, int i4) {
		this.setBlockBoundsBasedOnState(world1, i2, i3, i4);
		return super.getCollisionBoundingBoxFromPool(world1, i2, i3, i4);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess1, int i2, int i3, int i4) {
		this.setDoorRotation(this.getState(iBlockAccess1.getBlockMetadata(i2, i3, i4)));
	}

	public void setDoorRotation(int metadata) {
		float f2 = 0.1875F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
		if(metadata == 0) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f2);
		}

		if(metadata == 1) {
			this.setBlockBounds(1.0F - f2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}

		if(metadata == 2) {
			this.setBlockBounds(0.0F, 0.0F, 1.0F - f2, 1.0F, 1.0F, 1.0F);
		}

		if(metadata == 3) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, f2, 1.0F, 1.0F);
		}

	}

	public void onBlockClicked(World worldObj, int x, int y, int z, EntityPlayer entityPlayer) {
		this.blockActivated(worldObj, x, y, z, entityPlayer);
	}

	public boolean blockActivated(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
		if(this.material == Material.iron) {
			return true;
		} else {
			int i6 = world1.getBlockMetadata(i2, i3, i4);
			if((i6 & 8) != 0) {
				if(world1.getBlockId(i2, i3 - 1, i4) == this.blockID) {
					this.blockActivated(world1, i2, i3 - 1, i4, entityPlayer5);
				}

				return true;
			} else {
				if(world1.getBlockId(i2, i3 + 1, i4) == this.blockID) {
					world1.setBlockMetadataWithNotify(i2, i3 + 1, i4, (i6 ^ 4) + 8);
				}

				world1.setBlockMetadataWithNotify(i2, i3, i4, i6 ^ 4);
				world1.markBlocksDirty(i2, i3 - 1, i4, i2, i3, i4);
				if(Math.random() < 0.5D) {
					world1.playSoundEffect((double)i2 + 0.5D, (double)i3 + 0.5D, (double)i4 + 0.5D, "random.door_open", 1.0F, world1.rand.nextFloat() * 0.1F + 0.9F);
				} else {
					world1.playSoundEffect((double)i2 + 0.5D, (double)i3 + 0.5D, (double)i4 + 0.5D, "random.door_close", 1.0F, world1.rand.nextFloat() * 0.1F + 0.9F);
				}

				return true;
			}
		}
	}

	public void onPoweredBlockChange(World worldObj, int x, int y, int z, boolean z5) {
		int i6 = worldObj.getBlockMetadata(x, y, z);
		if((i6 & 8) != 0) {
			if(worldObj.getBlockId(x, y - 1, z) == this.blockID) {
				this.onPoweredBlockChange(worldObj, x, y - 1, z, z5);
			}

		} else {
			boolean z7 = (worldObj.getBlockMetadata(x, y, z) & 4) > 0;
			if(z7 != z5) {
				if(worldObj.getBlockId(x, y + 1, z) == this.blockID) {
					worldObj.setBlockMetadataWithNotify(x, y + 1, z, (i6 ^ 4) + 8);
				}

				worldObj.setBlockMetadataWithNotify(x, y, z, i6 ^ 4);
				worldObj.markBlocksDirty(x, y - 1, z, x, y, z);
				if(Math.random() < 0.5D) {
					worldObj.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.door_open", 1.0F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
				} else {
					worldObj.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.door_close", 1.0F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
				}

			}
		}
	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		int i6 = world1.getBlockMetadata(i2, i3, i4);
		if((i6 & 8) != 0) {
			if(world1.getBlockId(i2, i3 - 1, i4) != this.blockID) {
				world1.setBlockWithNotify(i2, i3, i4, 0);
			}

			if(i5 > 0 && Block.blocksList[i5].canProvidePower()) {
				this.onNeighborBlockChange(world1, i2, i3 - 1, i4, i5);
			}
		} else {
			boolean z7 = false;
			if(world1.getBlockId(i2, i3 + 1, i4) != this.blockID) {
				world1.setBlockWithNotify(i2, i3, i4, 0);
				z7 = true;
			}

			if(!world1.isBlockNormalCube(i2, i3 - 1, i4)) {
				world1.setBlockWithNotify(i2, i3, i4, 0);
				z7 = true;
				if(world1.getBlockId(i2, i3 + 1, i4) == this.blockID) {
					world1.setBlockWithNotify(i2, i3 + 1, i4, 0);
				}
			}

			if(z7) {
				this.dropBlockAsItem(world1, i2, i3, i4, i6);
			} else if(i5 > 0 && Block.blocksList[i5].canProvidePower()) {
				boolean z8 = world1.isBlockIndirectlyGettingPowered(i2, i3, i4) || world1.isBlockIndirectlyGettingPowered(i2, i3 + 1, i4);
				this.onPoweredBlockChange(world1, i2, i3, i4, z8);
			}
		}

	}

	public int idDropped(int i1, Random random2) {
		return (i1 & 8) != 0 ? 0 : (this.material == Material.iron ? Item.doorSteel.shiftedIndex : Item.doorWood.shiftedIndex);
	}

	public MovingObjectPosition collisionRayTrace(World world1, int i2, int i3, int i4, Vec3D vec3D5, Vec3D vec3D6) {
		this.setBlockBoundsBasedOnState(world1, i2, i3, i4);
		return super.collisionRayTrace(world1, i2, i3, i4, vec3D5, vec3D6);
	}

	public int getState(int metadata) {
		return (metadata & 4) == 0 ? metadata - 1 & 3 : metadata & 3;
	}

	public boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		return i3 >= 127 ? false : world1.isBlockNormalCube(i2, i3 - 1, i4) && super.canPlaceBlockAt(world1, i2, i3, i4) && super.canPlaceBlockAt(world1, i2, i3 + 1, i4);
	}
}
