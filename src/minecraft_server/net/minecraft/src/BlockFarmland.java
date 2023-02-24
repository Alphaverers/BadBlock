package net.minecraft.src;

import java.util.Random;

public class BlockFarmland extends Block {
	protected BlockFarmland(int id) {
		super(id, Material.grass);
		this.blockIndexInTexture = 87;
		this.setTickOnLoad(true);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
		this.setLightOpacity(255);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world1, int i2, int i3, int i4) {
		return AxisAlignedBB.getBoundingBoxFromPool((double)(i2 + 0), (double)(i3 + 0), (double)(i4 + 0), (double)(i2 + 1), (double)(i3 + 1), (double)(i4 + 1));
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		if(random5.nextInt(5) == 0) {
			if(this.isWaterNearby(world1, i2, i3, i4)) {
				world1.setBlockMetadataWithNotify(i2, i3, i4, 7);
			} else {
				int i6 = world1.getBlockMetadata(i2, i3, i4);
				if(i6 > 0) {
					world1.setBlockMetadataWithNotify(i2, i3, i4, i6 - 1);
				} else if(!this.isCropsNearby(world1, i2, i3, i4)) {
					world1.setBlockWithNotify(i2, i3, i4, Block.dirt.blockID);
				}
			}
		}

	}

	public void onEntityWalking(World world1, int i2, int i3, int i4, Entity entity5) {
		if(world1.rand.nextInt(4) == 0) {
			world1.setBlockWithNotify(i2, i3, i4, Block.dirt.blockID);
		}

	}

	private boolean isCropsNearby(World world, int x, int y, int z) {
		byte b5 = 0;

		for(int i6 = x - b5; i6 <= x + b5; ++i6) {
			for(int i7 = z - b5; i7 <= z + b5; ++i7) {
				if(world.getBlockId(i6, y + 1, i7) == Block.crops.blockID) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean isWaterNearby(World world, int x, int y, int z) {
		for(int i5 = x - 4; i5 <= x + 4; ++i5) {
			for(int i6 = y; i6 <= y + 1; ++i6) {
				for(int i7 = z - 4; i7 <= z + 4; ++i7) {
					if(world.getBlockMaterial(i5, i6, i7) == Material.water) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		super.onNeighborBlockChange(world1, i2, i3, i4, i5);
		Material material6 = world1.getBlockMaterial(i2, i3 + 1, i4);
		if(material6.isSolid()) {
			world1.setBlockWithNotify(i2, i3, i4, Block.dirt.blockID);
		}

	}

	public int idDropped(int i1, Random random2) {
		return Block.dirt.idDropped(0, random2);
	}
}
