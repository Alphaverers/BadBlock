package net.minecraft.src;

import java.util.Random;

public class BlockSapling extends BlockFlower {
	protected BlockSapling(int i1, int i2) {
		super(i1, i2);
		float f3 = 0.4F;
		this.setBlockBounds(0.5F - f3, 0.0F, 0.5F - f3, 0.5F + f3, f3 * 2.0F, 0.5F + f3);
	}

	public void updateTick(World worldObj, int x, int y, int z, Random rand) {
		super.updateTick(worldObj, x, y, z, rand);
		if(worldObj.getBlockLightValue(x, y + 1, z) >= 9 && rand.nextInt(5) == 0) {
			int i6 = worldObj.getBlockMetadata(x, y, z);
			if(i6 < 15) {
				worldObj.setBlockMetadataWithNotify(x, y, z, i6 + 1);
			} else {
				worldObj.setBlock(x, y, z, 0);
				Object object7 = new WorldGenTrees();
				if(rand.nextInt(10) == 0) {
					object7 = new WorldGenBigTree();
				}

				if(!((WorldGenerator)object7).generate(worldObj, rand, x, y, z)) {
					worldObj.setBlock(x, y, z, this.blockID);
				}
			}
		}

	}
}
