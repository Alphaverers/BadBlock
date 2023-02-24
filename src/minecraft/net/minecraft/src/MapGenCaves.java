package net.minecraft.src;

import java.util.Random;

public class MapGenCaves extends MapGenBase {
	protected void generateLargeCaveNode(int i1, int i2, byte[] b3, double d4, double d6, double d8) {
		this.generateCaveNode(i1, i2, b3, d4, d6, d8, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
	}

	protected void generateCaveNode(int i1, int i2, byte[] b3, double d4, double d6, double d8, float f10, float f11, float f12, int i13, int i14, double d15) {
		double d17 = (double)(i1 * 16 + 8);
		double d19 = (double)(i2 * 16 + 8);
		float f21 = 0.0F;
		float f22 = 0.0F;
		Random random23 = new Random(this.rand.nextLong());
		if(i14 <= 0) {
			int i24 = this.range * 16 - 16;
			i14 = i24 - random23.nextInt(i24 / 4);
		}

		boolean z52 = false;
		if(i13 == -1) {
			i13 = i14 / 2;
			z52 = true;
		}

		int i25 = random23.nextInt(i14 / 2) + i14 / 4;

		for(boolean z26 = random23.nextInt(6) == 0; i13 < i14; ++i13) {
			double d27 = 1.5D + (double)(MathHelper.sin((float)i13 * (float)Math.PI / (float)i14) * f10 * 1.0F);
			double d29 = d27 * d15;
			float f31 = MathHelper.cos(f12);
			float f32 = MathHelper.sin(f12);
			d4 += (double)(MathHelper.cos(f11) * f31);
			d6 += (double)f32;
			d8 += (double)(MathHelper.sin(f11) * f31);
			if(z26) {
				f12 *= 0.92F;
			} else {
				f12 *= 0.7F;
			}

			f12 += f22 * 0.1F;
			f11 += f21 * 0.1F;
			f22 *= 0.9F;
			f21 *= 0.75F;
			f22 += (random23.nextFloat() - random23.nextFloat()) * random23.nextFloat() * 2.0F;
			f21 += (random23.nextFloat() - random23.nextFloat()) * random23.nextFloat() * 4.0F;
			if(!z52 && i13 == i25 && f10 > 1.0F) {
				this.generateCaveNode(i1, i2, b3, d4, d6, d8, random23.nextFloat() * 0.5F + 0.5F, f11 - (float)Math.PI / 2F, f12 / 3.0F, i13, i14, 1.0D);
				this.generateCaveNode(i1, i2, b3, d4, d6, d8, random23.nextFloat() * 0.5F + 0.5F, f11 + (float)Math.PI / 2F, f12 / 3.0F, i13, i14, 1.0D);
				return;
			}

			if(z52 || random23.nextInt(4) != 0) {
				double d33 = d4 - d17;
				double d35 = d8 - d19;
				double d37 = (double)(i14 - i13);
				double d39 = (double)(f10 + 2.0F + 16.0F);
				if(d33 * d33 + d35 * d35 - d37 * d37 > d39 * d39) {
					return;
				}

				if(d4 >= d17 - 16.0D - d27 * 2.0D && d8 >= d19 - 16.0D - d27 * 2.0D && d4 <= d17 + 16.0D + d27 * 2.0D && d8 <= d19 + 16.0D + d27 * 2.0D) {
					int i53 = MathHelper.floor_double(d4 - d27) - i1 * 16 - 1;
					int i34 = MathHelper.floor_double(d4 + d27) - i1 * 16 + 1;
					int i54 = MathHelper.floor_double(d6 - d29) - 1;
					int i36 = MathHelper.floor_double(d6 + d29) + 1;
					int i55 = MathHelper.floor_double(d8 - d27) - i2 * 16 - 1;
					int i38 = MathHelper.floor_double(d8 + d27) - i2 * 16 + 1;
					if(i53 < 0) {
						i53 = 0;
					}

					if(i34 > 16) {
						i34 = 16;
					}

					if(i54 < 1) {
						i54 = 1;
					}

					if(i36 > 120) {
						i36 = 120;
					}

					if(i55 < 0) {
						i55 = 0;
					}

					if(i38 > 16) {
						i38 = 16;
					}

					boolean z56 = false;

					int i40;
					int i43;
					for(i40 = i53; !z56 && i40 < i34; ++i40) {
						for(int i41 = i55; !z56 && i41 < i38; ++i41) {
							for(int i42 = i36 + 1; !z56 && i42 >= i54 - 1; --i42) {
								i43 = (i40 * 16 + i41) * 128 + i42;
								if(i42 >= 0 && i42 < 128) {
									if(b3[i43] == Block.waterMoving.blockID || b3[i43] == Block.waterStill.blockID) {
										z56 = true;
									}

									if(i42 != i54 - 1 && i40 != i53 && i40 != i34 - 1 && i41 != i55 && i41 != i38 - 1) {
										i42 = i54;
									}
								}
							}
						}
					}

					if(!z56) {
						for(i40 = i53; i40 < i34; ++i40) {
							double d57 = ((double)(i40 + i1 * 16) + 0.5D - d4) / d27;

							for(i43 = i55; i43 < i38; ++i43) {
								double d44 = ((double)(i43 + i2 * 16) + 0.5D - d8) / d27;
								int i46 = (i40 * 16 + i43) * 128 + i36;
								boolean z47 = false;

								for(int i48 = i36 - 1; i48 >= i54; --i48) {
									double d49 = ((double)i48 + 0.5D - d6) / d29;
									if(d49 > -0.7D && d57 * d57 + d49 * d49 + d44 * d44 < 1.0D) {
										byte b51 = b3[i46];
										if(b51 == Block.grass.blockID) {
											z47 = true;
										}

										if(b51 == Block.stone.blockID || b51 == Block.dirt.blockID || b51 == Block.grass.blockID) {
											if(i48 < 10) {
												b3[i46] = (byte)Block.lavaMoving.blockID;
											} else {
												b3[i46] = 0;
												if(z47 && b3[i46 - 1] == Block.dirt.blockID) {
													b3[i46 - 1] = (byte)Block.grass.blockID;
												}
											}
										}
									}

									--i46;
								}
							}
						}

						if(z52) {
							break;
						}
					}
				}
			}
		}

	}

	protected void recursiveGenerate(World world1, int i2, int i3, int i4, int i5, byte[] b6) {
		int i7 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(40) + 1) + 1);
		if(this.rand.nextInt(15) != 0) {
			i7 = 0;
		}

		for(int i8 = 0; i8 < i7; ++i8) {
			double d9 = (double)(i2 * 16 + this.rand.nextInt(16));
			double d11 = (double)this.rand.nextInt(this.rand.nextInt(120) + 8);
			double d13 = (double)(i3 * 16 + this.rand.nextInt(16));
			int i15 = 1;
			if(this.rand.nextInt(4) == 0) {
				this.generateLargeCaveNode(i4, i5, b6, d9, d11, d13);
				i15 += this.rand.nextInt(4);
			}

			for(int i16 = 0; i16 < i15; ++i16) {
				float f17 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
				float f18 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
				float f19 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();
				this.generateCaveNode(i4, i5, b6, d9, d11, d13, f19, f17, f18, 0, 0, 1.0D);
			}
		}

	}
}
