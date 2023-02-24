package net.minecraft.src;

import java.util.Random;

public class WorldGenClay extends WorldGenerator {
	private int clayBlockId = Block.blockClay.blockID;
	private int numberOfBlocks;

	public WorldGenClay(int i1) {
		this.numberOfBlocks = i1;
	}

	public boolean generate(World world1, Random random2, int i3, int i4, int i5) {
		if(world1.getBlockMaterial(i3, i4, i5) != Material.water) {
			return false;
		} else {
			float f6 = random2.nextFloat() * (float)Math.PI;
			double d7 = (double)((float)(i3 + 8) + MathHelper.sin(f6) * (float)this.numberOfBlocks / 8.0F);
			double d9 = (double)((float)(i3 + 8) - MathHelper.sin(f6) * (float)this.numberOfBlocks / 8.0F);
			double d11 = (double)((float)(i5 + 8) + MathHelper.cos(f6) * (float)this.numberOfBlocks / 8.0F);
			double d13 = (double)((float)(i5 + 8) - MathHelper.cos(f6) * (float)this.numberOfBlocks / 8.0F);
			double d15 = (double)(i4 + random2.nextInt(3) + 2);
			double d17 = (double)(i4 + random2.nextInt(3) + 2);

			for(int i19 = 0; i19 <= this.numberOfBlocks; ++i19) {
				double d20 = d7 + (d9 - d7) * (double)i19 / (double)this.numberOfBlocks;
				double d22 = d15 + (d17 - d15) * (double)i19 / (double)this.numberOfBlocks;
				double d24 = d11 + (d13 - d11) * (double)i19 / (double)this.numberOfBlocks;
				double d26 = random2.nextDouble() * (double)this.numberOfBlocks / 16.0D;
				double d28 = (double)(MathHelper.sin((float)i19 * (float)Math.PI / (float)this.numberOfBlocks) + 1.0F) * d26 + 1.0D;
				double d30 = (double)(MathHelper.sin((float)i19 * (float)Math.PI / (float)this.numberOfBlocks) + 1.0F) * d26 + 1.0D;

				for(int i32 = (int)(d20 - d28 / 2.0D); i32 <= (int)(d20 + d28 / 2.0D); ++i32) {
					for(int i33 = (int)(d22 - d30 / 2.0D); i33 <= (int)(d22 + d30 / 2.0D); ++i33) {
						for(int i34 = (int)(d24 - d28 / 2.0D); i34 <= (int)(d24 + d28 / 2.0D); ++i34) {
							double d35 = ((double)i32 + 0.5D - d20) / (d28 / 2.0D);
							double d37 = ((double)i33 + 0.5D - d22) / (d30 / 2.0D);
							double d39 = ((double)i34 + 0.5D - d24) / (d28 / 2.0D);
							if(d35 * d35 + d37 * d37 + d39 * d39 < 1.0D) {
								int i41 = world1.getBlockId(i32, i33, i34);
								if(i41 == Block.sand.blockID) {
									world1.setBlock(i32, i33, i34, this.clayBlockId);
								}
							}
						}
					}
				}
			}

			return true;
		}
	}
}
