package net.minecraft.src;

public class MetadataChunkBlock {
	public final EnumSkyBlock skyBlock;
	public int x;
	public int y;
	public int z;
	public int maxX;
	public int maxY;
	public int maxZ;

	public MetadataChunkBlock(EnumSkyBlock skyBlock, int x, int y, int z, int maxX, int maxY, int maxZ) {
		this.skyBlock = skyBlock;
		this.x = x;
		this.y = y;
		this.z = z;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	public void updateLight(World world) {
		int i2 = this.maxX - this.x;
		int i3 = this.maxY - this.y;
		int i4 = this.maxZ - this.z;
		int i5 = i2 * i3 * i4;
		if(i5 <= 32768) {
			for(int i6 = this.x; i6 <= this.maxX; ++i6) {
				for(int i7 = this.z; i7 <= this.maxZ; ++i7) {
					if(world.blockExists(i6, 0, i7)) {
						for(int i8 = this.y; i8 <= this.maxY; ++i8) {
							if(i8 >= 0 && i8 < 128) {
								int i9 = world.getSavedLightValue(this.skyBlock, i6, i8, i7);
								boolean z10 = false;
								int i11 = world.getBlockId(i6, i8, i7);
								int i12 = Block.lightOpacity[i11];
								if(i12 == 0) {
									i12 = 1;
								}

								int i13 = 0;
								if(this.skyBlock == EnumSkyBlock.Sky) {
									if(world.canExistingBlockSeeTheSky(i6, i8, i7)) {
										i13 = 15;
									}
								} else if(this.skyBlock == EnumSkyBlock.Block) {
									i13 = Block.lightValue[i11];
								}

								int i14;
								int i20;
								if(i12 >= 15 && i13 == 0) {
									i20 = 0;
								} else {
									i14 = world.getSavedLightValue(this.skyBlock, i6 - 1, i8, i7);
									int i15 = world.getSavedLightValue(this.skyBlock, i6 + 1, i8, i7);
									int i16 = world.getSavedLightValue(this.skyBlock, i6, i8 - 1, i7);
									int i17 = world.getSavedLightValue(this.skyBlock, i6, i8 + 1, i7);
									int i18 = world.getSavedLightValue(this.skyBlock, i6, i8, i7 - 1);
									int i19 = world.getSavedLightValue(this.skyBlock, i6, i8, i7 + 1);
									i20 = i14;
									if(i15 > i14) {
										i20 = i15;
									}

									if(i16 > i20) {
										i20 = i16;
									}

									if(i17 > i20) {
										i20 = i17;
									}

									if(i18 > i20) {
										i20 = i18;
									}

									if(i19 > i20) {
										i20 = i19;
									}

									i20 -= i12;
									if(i20 < 0) {
										i20 = 0;
									}

									if(i13 > i20) {
										i20 = i13;
									}
								}

								if(i9 != i20) {
									world.setLightValue(this.skyBlock, i6, i8, i7, i20);
									i14 = i20 - 1;
									if(i14 < 0) {
										i14 = 0;
									}

									world.neighborLightPropagationChanged(this.skyBlock, i6 - 1, i8, i7, i14);
									world.neighborLightPropagationChanged(this.skyBlock, i6, i8 - 1, i7, i14);
									world.neighborLightPropagationChanged(this.skyBlock, i6, i8, i7 - 1, i14);
									if(i6 + 1 >= this.maxX) {
										world.neighborLightPropagationChanged(this.skyBlock, i6 + 1, i8, i7, i14);
									}

									if(i8 + 1 >= this.maxY) {
										world.neighborLightPropagationChanged(this.skyBlock, i6, i8 + 1, i7, i14);
									}

									if(i7 + 1 >= this.maxZ) {
										world.neighborLightPropagationChanged(this.skyBlock, i6, i8, i7 + 1, i14);
									}
								}
							}
						}
					}
				}
			}

		}
	}

	public boolean getLightUpdated(int x, int y, int z, int maxX, int maxY, int maxZ) {
		if(x >= this.x && y >= this.y && z >= this.z && maxX <= this.maxX && maxY <= this.maxY && maxZ <= this.maxZ) {
			return true;
		} else {
			byte b7 = 1;
			if(x >= this.x - b7 && y >= this.y - b7 && z >= this.z - b7 && maxX <= this.maxX + b7 && maxY <= this.maxY + b7 && maxZ <= this.maxZ + b7) {
				int i8 = this.maxX - this.x;
				int i9 = this.maxY - this.y;
				int i10 = this.maxZ - this.z;
				if(x > this.x) {
					x = this.x;
				}

				if(y > this.y) {
					y = this.y;
				}

				if(z > this.z) {
					z = this.z;
				}

				if(maxX < this.maxX) {
					maxX = this.maxX;
				}

				if(maxY < this.maxY) {
					maxY = this.maxY;
				}

				if(maxZ < this.maxZ) {
					maxZ = this.maxZ;
				}

				int i11 = maxX - x;
				int i12 = maxY - y;
				int i13 = maxZ - z;
				int i14 = i8 * i9 * i10;
				int i15 = i11 * i12 * i13;
				if(i15 - i14 <= 2) {
					this.x = x;
					this.y = y;
					this.z = z;
					this.maxX = maxX;
					this.maxY = maxY;
					this.maxZ = maxZ;
					return true;
				}
			}

			return false;
		}
	}
}
