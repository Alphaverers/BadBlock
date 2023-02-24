package net.minecraft.src;

import java.util.Random;

public class MapGenBase {
	protected int range = 8;
	protected Random rand = new Random();

	public void generate(ChunkProviderGenerate chunkProviderGenerate, World world, int i3, int i4, byte[] data) {
		int i6 = this.range;
		this.rand.setSeed(world.randomSeed);
		long j7 = this.rand.nextLong() / 2L * 2L + 1L;
		long j9 = this.rand.nextLong() / 2L * 2L + 1L;

		for(int i11 = i3 - i6; i11 <= i3 + i6; ++i11) {
			for(int i12 = i4 - i6; i12 <= i4 + i6; ++i12) {
				this.rand.setSeed((long)i11 * j7 + (long)i12 * j9 ^ world.randomSeed);
				this.recursiveGenerate(world, i11, i12, i3, i4, data);
			}
		}

	}

	protected void recursiveGenerate(World world, int i2, int i3, int i4, int i5, byte[] data) {
	}
}
