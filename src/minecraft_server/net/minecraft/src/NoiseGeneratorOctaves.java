package net.minecraft.src;

import java.util.Random;

public class NoiseGeneratorOctaves extends NoiseGenerator {
	private NoiseGeneratorPerlin[] generatorCollection;
	private int octaves;

	public NoiseGeneratorOctaves(Random random, int octaves) {
		this.octaves = octaves;
		this.generatorCollection = new NoiseGeneratorPerlin[octaves];

		for(int i3 = 0; i3 < octaves; ++i3) {
			this.generatorCollection[i3] = new NoiseGeneratorPerlin(random);
		}

	}

	public double generateNoiseOctaves(double x, double z) {
		double d5 = 0.0D;
		double d7 = 1.0D;

		for(int i9 = 0; i9 < this.octaves; ++i9) {
			d5 += this.generatorCollection[i9].generateNoise(x * d7, z * d7) / d7;
			d7 /= 2.0D;
		}

		return d5;
	}

	public double[] generateNoiseOctaves(double[] data, double d2, double d4, double d6, int x, int y, int z, double d11, double d13, double d15) {
		if(data == null) {
			data = new double[x * y * z];
		} else {
			for(int i17 = 0; i17 < data.length; ++i17) {
				data[i17] = 0.0D;
			}
		}

		double d20 = 1.0D;

		for(int i19 = 0; i19 < this.octaves; ++i19) {
			this.generatorCollection[i19].populateNoiseArray(data, d2, d4, d6, x, y, z, d11 * d20, d13 * d20, d15 * d20, d20);
			d20 /= 2.0D;
		}

		return data;
	}
}
