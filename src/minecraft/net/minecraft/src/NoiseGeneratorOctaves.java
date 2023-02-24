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

	public double generateNoiseOctaves(double d1, double d3) {
		double d5 = 0.0D;
		double d7 = 1.0D;

		for(int i9 = 0; i9 < this.octaves; ++i9) {
			d5 += this.generatorCollection[i9].generateNoise(d1 * d7, d3 * d7) / d7;
			d7 /= 2.0D;
		}

		return d5;
	}

	public double[] generateNoiseOctaves(double[] d1, double d2, double d4, double d6, int i8, int i9, int i10, double d11, double d13, double d15) {
		if(d1 == null) {
			d1 = new double[i8 * i9 * i10];
		} else {
			for(int i17 = 0; i17 < d1.length; ++i17) {
				d1[i17] = 0.0D;
			}
		}

		double d20 = 1.0D;

		for(int i19 = 0; i19 < this.octaves; ++i19) {
			this.generatorCollection[i19].populateNoiseArray(d1, d2, d4, d6, i8, i9, i10, d11 * d20, d13 * d20, d15 * d20, d20);
			d20 /= 2.0D;
		}

		return d1;
	}
}
