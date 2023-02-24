package net.minecraft.src;

import java.util.Random;

public class BlockSapling extends BlockFlower {
	protected BlockSapling(int i1, int i2) {
		super(i1, i2);
		float f3 = 0.4F;
		this.setBlockBounds(0.5F - f3, 0.0F, 0.5F - f3, 0.5F + f3, f3 * 2.0F, 0.5F + f3);
	}

	public void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		super.updateTick(world1, i2, i3, i4, random5);
		if(world1.getBlockLightValue(i2, i3 + 1, i4) >= 9 && random5.nextInt(5) == 0) {
			int i6 = world1.getBlockMetadata(i2, i3, i4);
			if(i6 < 15) {
				world1.setBlockMetadataWithNotify(i2, i3, i4, i6 + 1);
			} else {
				world1.setBlock(i2, i3, i4, 0);
				Object object7 = new WorldGenTrees();
				if(random5.nextInt(10) == 0) {
					object7 = new WorldGenBigTree();
				}

				if(!((WorldGenerator)object7).generate(world1, random5, i2, i3, i4)) {
					world1.setBlock(i2, i3, i4, this.blockID);
				}
			}
		}

	}
}
