package net.minecraft.src;

import java.util.Random;

public class WorldGenCactus extends WorldGenerator {
	public boolean generate(World world1, Random random2, int i3, int i4, int i5) {
		for(int i6 = 0; i6 < 10; ++i6) {
			int i7 = i3 + random2.nextInt(8) - random2.nextInt(8);
			int i8 = i4 + random2.nextInt(4) - random2.nextInt(4);
			int i9 = i5 + random2.nextInt(8) - random2.nextInt(8);
			if(world1.getBlockId(i7, i8, i9) == 0) {
				int i10 = 1 + random2.nextInt(random2.nextInt(3) + 1);

				for(int i11 = 0; i11 < i10; ++i11) {
					if(Block.cactus.canBlockStay(world1, i7, i8 + i11, i9)) {
						world1.setBlock(i7, i8 + i11, i9, Block.cactus.blockID);
					}
				}
			}
		}

		return true;
	}
}
