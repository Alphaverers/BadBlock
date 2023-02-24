package net.minecraft.src;

import java.util.Random;

public class BlockCrops extends BlockFlower {
	protected BlockCrops(int i1, int i2) {
		super(i1, i2);
		this.blockIndexInTexture = i2;
		this.setTickOnLoad(true);
		float f3 = 0.5F;
		this.setBlockBounds(0.5F - f3, 0.0F, 0.5F - f3, 0.5F + f3, 0.25F, 0.5F + f3);
	}

	protected boolean canThisPlantGrowOnThisBlockID(int i1) {
		return i1 == Block.tilledField.blockID;
	}

	public void updateTick(World worldObj, int x, int y, int z, Random rand) {
		super.updateTick(worldObj, x, y, z, rand);
		if(worldObj.getBlockLightValue(x, y + 1, z) >= 9) {
			int i6 = worldObj.getBlockMetadata(x, y, z);
			if(i6 < 7) {
				float f7 = this.getGrowthRate(worldObj, x, y, z);
				if(rand.nextInt((int)(100.0F / f7)) == 0) {
					++i6;
					worldObj.setBlockMetadataWithNotify(x, y, z, i6);
				}
			}
		}

	}

	private float getGrowthRate(World worldObj, int x, int y, int z) {
		float f5 = 1.0F;
		int i6 = worldObj.getBlockId(x, y, z - 1);
		int i7 = worldObj.getBlockId(x, y, z + 1);
		int i8 = worldObj.getBlockId(x - 1, y, z);
		int i9 = worldObj.getBlockId(x + 1, y, z);
		int i10 = worldObj.getBlockId(x - 1, y, z - 1);
		int i11 = worldObj.getBlockId(x + 1, y, z - 1);
		int i12 = worldObj.getBlockId(x + 1, y, z + 1);
		int i13 = worldObj.getBlockId(x - 1, y, z + 1);
		boolean z14 = i8 == this.blockID || i9 == this.blockID;
		boolean z15 = i6 == this.blockID || i7 == this.blockID;
		boolean z16 = i10 == this.blockID || i11 == this.blockID || i12 == this.blockID || i13 == this.blockID;

		for(int i17 = x - 1; i17 <= x + 1; ++i17) {
			for(int i18 = z - 1; i18 <= z + 1; ++i18) {
				int i19 = worldObj.getBlockId(i17, y - 1, i18);
				float f20 = 0.0F;
				if(i19 == Block.tilledField.blockID) {
					f20 = 1.0F;
					if(worldObj.getBlockMetadata(i17, y - 1, i18) > 0) {
						f20 = 3.0F;
					}
				}

				if(i17 != x || i18 != z) {
					f20 /= 4.0F;
				}

				f5 += f20;
			}
		}

		if(z16 || z14 && z15) {
			f5 /= 2.0F;
		}

		return f5;
	}

	public int getBlockTextureFromSideAndMetadata(int i1, int i2) {
		if(i2 < 0) {
			i2 = 7;
		}

		return this.blockIndexInTexture + i2;
	}

	public int getRenderType() {
		return 6;
	}

	public void onBlockDestroyedByPlayer(World world1, int i2, int i3, int i4, int i5) {
		super.onBlockDestroyedByPlayer(world1, i2, i3, i4, i5);

		for(int i6 = 0; i6 < 3; ++i6) {
			if(world1.rand.nextInt(15) <= i5) {
				float f7 = 0.7F;
				float f8 = world1.rand.nextFloat() * f7 + (1.0F - f7) * 0.5F;
				float f9 = world1.rand.nextFloat() * f7 + (1.0F - f7) * 0.5F;
				float f10 = world1.rand.nextFloat() * f7 + (1.0F - f7) * 0.5F;
				EntityItem entityItem11 = new EntityItem(world1, (double)((float)i2 + f8), (double)((float)i3 + f9), (double)((float)i4 + f10), new ItemStack(Item.seeds));
				entityItem11.delayBeforeCanPickup = 10;
				world1.spawnEntityInWorld(entityItem11);
			}
		}

	}

	public int idDropped(int i1, Random random2) {
		System.out.println("Get resource: " + i1);
		return i1 == 7 ? Item.wheat.shiftedIndex : -1;
	}

	public int quantityDropped(Random random1) {
		return 1;
	}
}
