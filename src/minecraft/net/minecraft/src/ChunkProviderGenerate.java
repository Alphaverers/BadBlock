package net.minecraft.src;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class ChunkProviderGenerate implements IChunkProvider {
	private Random rand;
	private NoiseGeneratorOctaves noiseGen1;
	private NoiseGeneratorOctaves noiseGen2;
	private NoiseGeneratorOctaves noiseGen3;
	private NoiseGeneratorOctaves noiseGen4;
	private NoiseGeneratorOctaves noiseGen5;
	public NoiseGeneratorOctaves noiseGen6;
	public NoiseGeneratorOctaves noiseGen7;
	public NoiseGeneratorOctaves mobSpawnerNoise;
	private World worldObj;
	private double[] noiseArray;
	private double[] sandNoise = new double[256];
	private double[] gravelNoise = new double[256];
	private double[] stoneNoise = new double[256];
	private MapGenBase caveGenerator = new MapGenCaves();
	double[] noise3;
	double[] noise1;
	double[] noise2;
	double[] noise6;
	double[] noise7;
	int[][] unused = new int[32][32];
	private int time_hr = 0;
	private long lastUpdate = 0L;

	public ChunkProviderGenerate(World worldObj, long seed) {
		this.worldObj = worldObj;
		this.rand = new Random(seed);
		this.noiseGen1 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen2 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen3 = new NoiseGeneratorOctaves(this.rand, 8);
		this.noiseGen4 = new NoiseGeneratorOctaves(this.rand, 4);
		this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 4);
		this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 10);
		this.noiseGen7 = new NoiseGeneratorOctaves(this.rand, 16);
		this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
	}

	public void generateTerrain(int chunkX, int chunkZ, byte[] blocks) {
		this.noiseArray = this.initializeNoiseField(this.noiseArray, chunkX * 4, 0, chunkZ * 4, 5, 17, 5);

		for(int i9 = 0; i9 < 4; ++i9) {
			for(int i10 = 0; i10 < 4; ++i10) {
				for(int i11 = 0; i11 < 16; ++i11) {
					double d14 = this.noiseArray[((i9 + 0) * 5 + i10 + 0) * 17 + i11 + 0];
					double d16 = this.noiseArray[((i9 + 0) * 5 + i10 + 1) * 17 + i11 + 0];
					double d18 = this.noiseArray[((i9 + 1) * 5 + i10 + 0) * 17 + i11 + 0];
					double d20 = this.noiseArray[((i9 + 1) * 5 + i10 + 1) * 17 + i11 + 0];
					double d22 = (this.noiseArray[((i9 + 0) * 5 + i10 + 0) * 17 + i11 + 1] - d14) * 0.125D;
					double d24 = (this.noiseArray[((i9 + 0) * 5 + i10 + 1) * 17 + i11 + 1] - d16) * 0.125D;
					double d26 = (this.noiseArray[((i9 + 1) * 5 + i10 + 0) * 17 + i11 + 1] - d18) * 0.125D;
					double d28 = (this.noiseArray[((i9 + 1) * 5 + i10 + 1) * 17 + i11 + 1] - d20) * 0.125D;

					for(int i30 = 0; i30 < 8; ++i30) {
						double d33 = d14;
						double d35 = d16;
						double d37 = (d18 - d14) * 0.25D;
						double d39 = (d20 - d16) * 0.25D;

						for(int i41 = 0; i41 < 4; ++i41) {
							int i42 = i41 + i9 * 4 << 11 | 0 + i10 * 4 << 7 | i11 * 8 + i30;
							double d46 = d33;
							double d48 = (d35 - d33) * 0.25D;

							for(int i50 = 0; i50 < 4; ++i50) {
								int i51 = 0;
								if(i11 * 8 + i30 < 64) {
									if(this.worldObj.snowCovered && i11 * 8 + i30 >= 63) {
										i51 = Block.ice.blockID;
									} else {
										i51 = Block.waterStill.blockID;
									}
								}

								if(d46 > 0.0D) {
									i51 = Block.stone.blockID;
								}

								blocks[i42] = (byte)i51;
								i42 += 128;
								d46 += d48;
							}

							d33 += d37;
							d35 += d39;
						}

						d14 += d22;
						d16 += d24;
						d18 += d26;
						d20 += d28;
					}
				}
			}
		}

	}

	public void replaceSurfaceBlocks(int chunkX, int chunkZ, byte[] blocks) {
		this.sandNoise = this.noiseGen4.generateNoiseOctaves(this.sandNoise, (double)(chunkX * 16), (double)(chunkZ * 16), 0.0D, 16, 16, 1, 8.0D / 256D, 8.0D / 256D, 1.0D);
		this.gravelNoise = this.noiseGen4.generateNoiseOctaves(this.gravelNoise, (double)(chunkZ * 16), 109.0134D, (double)(chunkX * 16), 16, 1, 16, 8.0D / 256D, 1.0D, 8.0D / 256D);
		this.stoneNoise = this.noiseGen5.generateNoiseOctaves(this.stoneNoise, (double)(chunkX * 16), (double)(chunkZ * 16), 0.0D, 16, 16, 1, 0.0625D, 0.0625D, 0.0625D);

		for(int i7 = 0; i7 < 16; ++i7) {
			for(int i8 = 0; i8 < 16; ++i8) {
				boolean z9 = this.sandNoise[i7 + i8 * 16] + this.rand.nextDouble() * 0.2D > 0.0D;
				boolean z10 = this.gravelNoise[i7 + i8 * 16] + this.rand.nextDouble() * 0.2D > 3.0D;
				int i11 = (int)(this.stoneNoise[i7 + i8 * 16] / 3.0D + 3.0D + this.rand.nextDouble() * 0.25D);
				int i12 = -1;
				byte b13 = (byte)Block.grass.blockID;
				byte b14 = (byte)Block.dirt.blockID;

				for(int i15 = 127; i15 >= 0; --i15) {
					int i16 = (i7 * 16 + i8) * 128 + i15;
					if(i15 <= 0 + this.rand.nextInt(6) - 1) {
						blocks[i16] = (byte)Block.bedrock.blockID;
					} else {
						byte b17 = blocks[i16];
						if(b17 == 0) {
							i12 = -1;
						} else if(b17 == Block.stone.blockID) {
							if(i12 == -1) {
								if(i11 <= 0) {
									b13 = 0;
									b14 = (byte)Block.stone.blockID;
								} else if(i15 >= 60 && i15 <= 65) {
									b13 = (byte)Block.grass.blockID;
									b14 = (byte)Block.dirt.blockID;
									if(z10) {
										b13 = 0;
									}

									if(z10) {
										b14 = (byte)Block.gravel.blockID;
									}

									if(z9) {
										b13 = (byte)Block.sand.blockID;
									}

									if(z9) {
										b14 = (byte)Block.sand.blockID;
									}
								}

								if(i15 < 64 && b13 == 0) {
									b13 = (byte)Block.waterStill.blockID;
								}

								i12 = i11;
								if(i15 >= 63) {
									blocks[i16] = b13;
								} else {
									blocks[i16] = b14;
								}
							} else if(i12 > 0) {
								--i12;
								blocks[i16] = b14;
							}
						}
					}
				}
			}
		}

	}

	public Chunk provideChunk(int i1, int i2) {
		if(Math.abs(System.currentTimeMillis() - this.lastUpdate) > 5000L) {
			SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("HH");
			this.time_hr = Integer.parseInt(simpleDateFormat3.format(Calendar.getInstance().getTime()));
			this.lastUpdate = System.currentTimeMillis();
		}

		boolean z8 = this.time_hr > 22 || this.time_hr < 5;
		this.rand.setSeed((long)i1 * 341873128712L + (long)i2 * 132897987541L);
		byte[] b4 = new byte[32768];
		Chunk chunk5 = new Chunk(this.worldObj, b4, i1, i2);
		int i6 = i1 + (z8 ? this.rand.nextInt(2000) - this.rand.nextInt(1000) : 0);
		int i7 = i2 + (z8 ? this.rand.nextInt(2000) - this.rand.nextInt(1000) : 0);
		this.generateTerrain(i6, i7, b4);
		this.replaceSurfaceBlocks(i6, i7, b4);
		this.caveGenerator.generate(this, this.worldObj, i6, i7, b4);
		chunk5.generateSkylightMap();
		return chunk5;
	}

	private double[] initializeNoiseField(double[] d1, int i2, int i3, int i4, int i5, int i6, int i7) {
		if(d1 == null) {
			d1 = new double[i5 * i6 * i7];
		}

		this.noise6 = this.noiseGen6.generateNoiseOctaves(this.noise6, (double)i2, (double)i3, (double)i4, i5, 1, i7, 1.0D, 0.0D, 1.0D);
		this.noise7 = this.noiseGen7.generateNoiseOctaves(this.noise7, (double)i2, (double)i3, (double)i4, i5, 1, i7, 100.0D, 0.0D, 100.0D);
		this.noise3 = this.noiseGen3.generateNoiseOctaves(this.noise3, (double)i2, (double)i3, (double)i4, i5, i6, i7, 8.555150000000001D, 4.277575000000001D, 8.555150000000001D);
		this.noise1 = this.noiseGen1.generateNoiseOctaves(this.noise1, (double)i2, (double)i3, (double)i4, i5, i6, i7, 684.412D, 684.412D, 684.412D);
		this.noise2 = this.noiseGen2.generateNoiseOctaves(this.noise2, (double)i2, (double)i3, (double)i4, i5, i6, i7, 684.412D, 684.412D, 684.412D);
		int i12 = 0;
		int i13 = 0;

		for(int i14 = 0; i14 < i5; ++i14) {
			for(int i15 = 0; i15 < i7; ++i15) {
				double d16 = (this.noise6[i13] + 256.0D) / 512.0D;
				if(d16 > 1.0D) {
					d16 = 1.0D;
				}

				double d20 = this.noise7[i13] / 8000.0D;
				if(d20 < 0.0D) {
					d20 = -d20;
				}

				double d22 = d20 * 3.0D - 3.0D;
				double d24;
				double d26;
				if(d22 < 0.0D) {
					d26 = d22 / 2.0D;
					if(d26 < -1.0D) {
						d26 = -1.0D;
					}

					d24 = d26 / 1.4D / 2.0D;
					d16 = 0.0D;
				} else {
					if(d22 > 1.0D) {
						d22 = 1.0D;
					}

					d24 = d22 / 6.0D;
				}

				d26 = d16 + 0.5D;
				double d28 = (double)i6 / 2.0D + d24 * (double)i6 / 16.0D * 4.0D;
				++i13;

				for(int i30 = 0; i30 < i6; ++i30) {
					double d31 = ((double)i30 - d28) * 12.0D / d26;
					if(d31 < 0.0D) {
						d31 *= 4.0D;
					}

					double d33 = this.noise1[i12] / 512.0D;
					double d35 = this.noise2[i12] / 512.0D;
					double d37 = (this.noise3[i12] / 10.0D + 1.0D) / 2.0D;
					double d39;
					if(d37 < 0.0D) {
						d39 = d33;
					} else if(d37 > 1.0D) {
						d39 = d35;
					} else {
						d39 = d33 + (d35 - d33) * d37;
					}

					double d41 = d39 - d31;
					double d43;
					if(i30 > i6 - 4) {
						d43 = (double)((float)(i30 - (i6 - 4)) / 3.0F);
						d41 = d41 * (1.0D - d43) + -10.0D * d43;
					}

					if((double)i30 < 0.0D) {
						d43 = (0.0D - (double)i30) / 4.0D;
						if(d43 < 0.0D) {
							d43 = 0.0D;
						}

						if(d43 > 1.0D) {
							d43 = 1.0D;
						}

						d41 = d41 * (1.0D - d43) + -10.0D * d43;
					}

					d1[i12] = d41;
					++i12;
				}
			}
		}

		return d1;
	}

	public boolean chunkExists(int i1, int i2) {
		return true;
	}

	public void populate(IChunkProvider iChunkProvider1, int i2, int i3) {
		BlockSand.fallInstantly = true;
		int i4 = i2 * 16;
		int i5 = i3 * 16;
		this.rand.setSeed(this.worldObj.randomSeed);
		this.rand.setSeed((long)i2 * (this.rand.nextLong() / 2L * 2L + 1L) + (long)i3 * (this.rand.nextLong() / 2L * 2L + 1L) ^ this.worldObj.randomSeed);

		int i6;
		for(i6 = 0; i6 < 8; ++i6) {
			(new WorldGenDungeons()).generate(this.worldObj, this.rand, i4 + this.rand.nextInt(16) + 8, this.rand.nextInt(128), i5 + this.rand.nextInt(16) + 8);
		}

		for(i6 = 0; i6 < 10; ++i6) {
			(new WorldGenClay(32)).generate(this.worldObj, this.rand, i4 + this.rand.nextInt(16), this.rand.nextInt(128), i5 + this.rand.nextInt(16));
		}

		for(i6 = 0; i6 < 20; ++i6) {
			(new WorldGenMinable(Block.dirt.blockID, 32)).generate(this.worldObj, this.rand, i4 + this.rand.nextInt(16), this.rand.nextInt(128), i5 + this.rand.nextInt(16));
		}

		for(i6 = 0; i6 < 10; ++i6) {
			(new WorldGenMinable(Block.gravel.blockID, 32)).generate(this.worldObj, this.rand, i4 + this.rand.nextInt(16), this.rand.nextInt(128), i5 + this.rand.nextInt(16));
		}

		for(i6 = 0; i6 < 20; ++i6) {
			(new WorldGenMinable(Block.oreCoal.blockID, 16)).generate(this.worldObj, this.rand, i4 + this.rand.nextInt(16), this.rand.nextInt(128), i5 + this.rand.nextInt(16));
		}

		for(i6 = 0; i6 < 20; ++i6) {
			(new WorldGenMinable(Block.oreIron.blockID, 8)).generate(this.worldObj, this.rand, i4 + this.rand.nextInt(16), this.rand.nextInt(64), i5 + this.rand.nextInt(16));
		}

		for(i6 = 0; i6 < 2; ++i6) {
			(new WorldGenMinable(Block.oreGold.blockID, 8)).generate(this.worldObj, this.rand, i4 + this.rand.nextInt(16), this.rand.nextInt(32), i5 + this.rand.nextInt(16));
		}

		for(i6 = 0; i6 < 8; ++i6) {
			(new WorldGenMinable(Block.oreRedstone.blockID, 7)).generate(this.worldObj, this.rand, i4 + this.rand.nextInt(16), this.rand.nextInt(16), i5 + this.rand.nextInt(16));
		}

		for(i6 = 0; i6 < 1; ++i6) {
			(new WorldGenMinable(Block.oreDiamond.blockID, 7)).generate(this.worldObj, this.rand, i4 + this.rand.nextInt(16), this.rand.nextInt(16), i5 + this.rand.nextInt(16));
		}

		int i8 = (int)((this.mobSpawnerNoise.generateNoiseOctaves((double)i4 * 0.5D, (double)i5 * 0.5D) / 8.0D + this.rand.nextDouble() * 4.0D + 4.0D) / 3.0D);
		if(i8 < 0) {
			i8 = 0;
		}

		if(this.rand.nextInt(10) == 0) {
			++i8;
		}

		Object object9 = new WorldGenTrees();
		if(this.rand.nextInt(10) == 0) {
			object9 = new WorldGenBigTree();
		}

		int i10;
		int i11;
		int i12;
		for(i10 = 0; i10 < i8; ++i10) {
			i11 = i4 + this.rand.nextInt(16) + 8;
			i12 = i5 + this.rand.nextInt(16) + 8;
			((WorldGenerator)object9).setScale(1.0D, 1.0D, 1.0D);
			((WorldGenerator)object9).generate(this.worldObj, this.rand, i11, this.worldObj.getHeightValue(i11, i12), i12);
		}

		for(i10 = 0; i10 < 2; ++i10) {
			(new WorldGenFlowers(Block.plantYellow.blockID)).generate(this.worldObj, this.rand, i4 + this.rand.nextInt(16) + 8, this.rand.nextInt(128), i5 + this.rand.nextInt(16) + 8);
		}

		if(this.rand.nextInt(2) == 0) {
			(new WorldGenFlowers(Block.plantRed.blockID)).generate(this.worldObj, this.rand, i4 + this.rand.nextInt(16) + 8, this.rand.nextInt(128), i5 + this.rand.nextInt(16) + 8);
		}

		if(this.rand.nextInt(4) == 0) {
			(new WorldGenFlowers(Block.mushroomBrown.blockID)).generate(this.worldObj, this.rand, i4 + this.rand.nextInt(16) + 8, this.rand.nextInt(128), i5 + this.rand.nextInt(16) + 8);
		}

		if(this.rand.nextInt(8) == 0) {
			(new WorldGenFlowers(Block.mushroomRed.blockID)).generate(this.worldObj, this.rand, i4 + this.rand.nextInt(16) + 8, this.rand.nextInt(128), i5 + this.rand.nextInt(16) + 8);
		}

		for(i10 = 0; i10 < 3; ++i10) {
			(new PillarGen(Block.saltBlock.blockID)).generate(this.worldObj, this.rand, i4 + this.rand.nextInt(16) + 8, this.rand.nextInt(128), i5 + this.rand.nextInt(16) + 8);
		}

		for(i10 = 0; i10 < 3; ++i10) {
			(new OnWaterGen(Block.glowingFlower.blockID)).generate(this.worldObj, this.rand, i4 + this.rand.nextInt(16) + 8, this.rand.nextInt(128), i5 + this.rand.nextInt(16) + 8);
		}

		for(i10 = 0; i10 < 2; ++i10) {
			(new SkyGen(Block.blueFireIdk.blockID)).generate(this.worldObj, this.rand, i4 + this.rand.nextInt(16) + 8, this.rand.nextInt(128), i5 + this.rand.nextInt(16) + 8);
		}

		for(i10 = 0; i10 < 10; ++i10) {
			(new WorldGenReed()).generate(this.worldObj, this.rand, i4 + this.rand.nextInt(16) + 8, this.rand.nextInt(128), i5 + this.rand.nextInt(16) + 8);
		}

		for(i10 = 0; i10 < 1; ++i10) {
			(new WorldGenCactus()).generate(this.worldObj, this.rand, i4 + this.rand.nextInt(16) + 8, this.rand.nextInt(128), i5 + this.rand.nextInt(16) + 8);
		}

		for(i10 = 0; i10 < 50; ++i10) {
			(new WorldGenLiquids(Block.waterMoving.blockID)).generate(this.worldObj, this.rand, i4 + this.rand.nextInt(16) + 8, this.rand.nextInt(this.rand.nextInt(120) + 8), i5 + this.rand.nextInt(16) + 8);
		}

		for(i10 = 0; i10 < 20; ++i10) {
			(new WorldGenLiquids(Block.lavaMoving.blockID)).generate(this.worldObj, this.rand, i4 + this.rand.nextInt(16) + 8, this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(112) + 8) + 8), i5 + this.rand.nextInt(16) + 8);
		}

		for(i10 = i4 + 8 + 0; i10 < i4 + 8 + 16; ++i10) {
			for(i11 = i5 + 8 + 0; i11 < i5 + 8 + 16; ++i11) {
				i12 = this.worldObj.getTopSolidOrLiquidBlock(i10, i11);
				if(this.worldObj.snowCovered && i12 > 0 && i12 < 128 && this.worldObj.getBlockId(i10, i12, i11) == 0 && this.worldObj.getBlockMaterial(i10, i12 - 1, i11).getIsSolid() && this.worldObj.getBlockMaterial(i10, i12 - 1, i11) != Material.ice) {
					this.worldObj.setBlockWithNotify(i10, i12, i11, Block.snow.blockID);
				}
			}
		}

		BlockSand.fallInstantly = false;
	}

	public boolean saveChunks(boolean z1, IProgressUpdate iProgressUpdate2) {
		return true;
	}

	public boolean unload100OldestChunks() {
		return false;
	}

	public boolean canSave() {
		return true;
	}
}
