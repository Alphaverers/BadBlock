package net.minecraft.src;

public class MetadataChunkBlock {
	public final EnumSkyBlock skyBlock;
	public int minX;
	public int minY;
	public int minZ;
	public int maxX;
	public int maxY;
	public int maxZ;

	public MetadataChunkBlock(EnumSkyBlock skyBlock, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		this.skyBlock = skyBlock;
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	public void updateLight(World world) {
		int i2 = this.maxX - this.minX;
		int i3 = this.maxY - this.minY;
		int i4 = this.maxZ - this.minZ;
		int i5 = i2 * i3 * i4;
		if(i5 <= 32768) {
			for(int i6 = this.minX; i6 <= this.maxX; ++i6) {
				for(int i7 = this.minZ; i7 <= this.maxZ; ++i7) {
					if(world.blockExists(i6, 0, i7)) {
						for(int i8 = this.minY; i8 <= this.maxY; ++i8) {
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

	public boolean getLightUpdated(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		if(minX >= this.minX && minY >= this.minY && minZ >= this.minZ && maxX <= this.maxX && maxY <= this.maxY && maxZ <= this.maxZ) {
			return true;
		} else {
			byte b7 = 1;
			if(minX >= this.minX - b7 && minY >= this.minY - b7 && minZ >= this.minZ - b7 && maxX <= this.maxX + b7 && maxY <= this.maxY + b7 && maxZ <= this.maxZ + b7) {
				int i8 = this.maxX - this.minX;
				int i9 = this.maxY - this.minY;
				int i10 = this.maxZ - this.minZ;
				if(minX > this.minX) {
					minX = this.minX;
				}

				if(minY > this.minY) {
					minY = this.minY;
				}

				if(minZ > this.minZ) {
					minZ = this.minZ;
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

				int i11 = maxX - minX;
				int i12 = maxY - minY;
				int i13 = maxZ - minZ;
				int i14 = i8 * i9 * i10;
				int i15 = i11 * i12 * i13;
				if(i15 - i14 <= 2) {
					this.minX = minX;
					this.minY = minY;
					this.minZ = minZ;
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
