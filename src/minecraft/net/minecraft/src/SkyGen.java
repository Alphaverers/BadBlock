package net.minecraft.src;

import java.util.Random;

public class SkyGen extends WorldGenerator {
	private int a;

	public SkyGen(int i1) {
		this.a = i1;
	}

	public boolean generate(World world1, Random random2, int i3, int i4, int i5) {
		for(int i6 = 0; i6 < 4; ++i6) {
			int i7 = i3 + random2.nextInt(8) - random2.nextInt(8);
			int i8 = i4 + random2.nextInt(4) - random2.nextInt(4);
			int i9 = i5 + random2.nextInt(8) - random2.nextInt(8);
			boolean z10 = false;
			if(i8 > 90) {
				z10 = true;
			} else if(i8 > 80) {
				z10 = random2.nextInt(100) > 80;
			}

			if(z10 && world1.getBlockId(i7, i8, i9) == 0) {
				world1.setBlock(i7, i8, i9, this.a);
			}
		}

		return true;
	}
}
