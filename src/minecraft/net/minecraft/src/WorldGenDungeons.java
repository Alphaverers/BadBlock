package net.minecraft.src;

import java.util.Random;

public class WorldGenDungeons extends WorldGenerator {
	public boolean generate(World world1, Random random2, int i3, int i4, int i5) {
		byte b6 = 3;
		int i7 = random2.nextInt(2) + 2;
		int i8 = random2.nextInt(2) + 2;
		int i9 = 0;

		int i10;
		int i11;
		int i12;
		for(i10 = i3 - i7 - 1; i10 <= i3 + i7 + 1; ++i10) {
			for(i11 = i4 - 1; i11 <= i4 + b6 + 1; ++i11) {
				for(i12 = i5 - i8 - 1; i12 <= i5 + i8 + 1; ++i12) {
					Material material13 = world1.getBlockMaterial(i10, i11, i12);
					if(i11 == i4 - 1 && !material13.isSolid()) {
						return false;
					}

					if(i11 == i4 + b6 + 1 && !material13.isSolid()) {
						return false;
					}

					if((i10 == i3 - i7 - 1 || i10 == i3 + i7 + 1 || i12 == i5 - i8 - 1 || i12 == i5 + i8 + 1) && i11 == i4 && world1.getBlockId(i10, i11, i12) == 0 && world1.getBlockId(i10, i11 + 1, i12) == 0) {
						++i9;
					}
				}
			}
		}

		if(i9 >= 1 && i9 <= 5) {
			for(i10 = i3 - i7 - 1; i10 <= i3 + i7 + 1; ++i10) {
				for(i11 = i4 + b6; i11 >= i4 - 1; --i11) {
					for(i12 = i5 - i8 - 1; i12 <= i5 + i8 + 1; ++i12) {
						if(i10 != i3 - i7 - 1 && i11 != i4 - 1 && i12 != i5 - i8 - 1 && i10 != i3 + i7 + 1 && i11 != i4 + b6 + 1 && i12 != i5 + i8 + 1) {
							world1.setBlockWithNotify(i10, i11, i12, 0);
						} else if(i11 >= 0 && !world1.getBlockMaterial(i10, i11 - 1, i12).isSolid()) {
							world1.setBlockWithNotify(i10, i11, i12, 0);
						} else if(world1.getBlockMaterial(i10, i11, i12).isSolid()) {
							if(i11 == i4 - 1 && random2.nextInt(4) != 0) {
								world1.setBlockWithNotify(i10, i11, i12, Block.cobblestoneMossy.blockID);
							} else {
								world1.setBlockWithNotify(i10, i11, i12, Block.cobblestone.blockID);
							}
						}
					}
				}
			}

			label110:
			for(i10 = 0; i10 < 2; ++i10) {
				for(i11 = 0; i11 < 3; ++i11) {
					i12 = i3 + random2.nextInt(i7 * 2 + 1) - i7;
					int i14 = i5 + random2.nextInt(i8 * 2 + 1) - i8;
					if(world1.getBlockId(i12, i4, i14) == 0) {
						int i15 = 0;
						if(world1.getBlockMaterial(i12 - 1, i4, i14).isSolid()) {
							++i15;
						}

						if(world1.getBlockMaterial(i12 + 1, i4, i14).isSolid()) {
							++i15;
						}

						if(world1.getBlockMaterial(i12, i4, i14 - 1).isSolid()) {
							++i15;
						}

						if(world1.getBlockMaterial(i12, i4, i14 + 1).isSolid()) {
							++i15;
						}

						if(i15 == 1) {
							world1.setBlockWithNotify(i12, i4, i14, Block.chest.blockID);
							TileEntityChest tileEntityChest16 = (TileEntityChest)world1.getBlockTileEntity(i12, i4, i14);
							int i17 = 0;

							while(true) {
								if(i17 >= 8) {
									continue label110;
								}

								ItemStack itemStack18 = this.pickCheckLootItem(random2);
								if(itemStack18 != null) {
									tileEntityChest16.setInventorySlotContents(random2.nextInt(tileEntityChest16.getSizeInventory()), itemStack18);
								}

								++i17;
							}
						}
					}
				}
			}

			world1.setBlockWithNotify(i3, i4, i5, Block.mobSpawner.blockID);
			TileEntityMobSpawner tileEntityMobSpawner19 = (TileEntityMobSpawner)world1.getBlockTileEntity(i3, i4, i5);
			tileEntityMobSpawner19.mobID = this.pickMobSpawner(random2);
			return true;
		} else {
			return false;
		}
	}

	private ItemStack pickCheckLootItem(Random random1) {
		int i2 = random1.nextInt(11);
		return i2 == 0 ? new ItemStack(Item.saddle) : (i2 == 1 ? new ItemStack(Item.ingotIron, random1.nextInt(4) + 1) : (i2 == 2 ? new ItemStack(Item.bread) : (i2 == 3 ? new ItemStack(Item.wheat, random1.nextInt(4) + 1) : (i2 == 4 ? new ItemStack(Item.gunpowder, random1.nextInt(4) + 1) : (i2 == 5 ? new ItemStack(Item.silk, random1.nextInt(4) + 1) : (i2 == 6 ? new ItemStack(Item.bucketEmpty) : (i2 == 7 && random1.nextInt(100) == 0 ? new ItemStack(Item.appleGold) : (i2 == 8 && random1.nextInt(2) == 0 ? new ItemStack(Item.redstone, random1.nextInt(4) + 1) : (i2 == 9 && random1.nextInt(10) == 0 ? new ItemStack(Item.itemsList[Item.record13.shiftedIndex + random1.nextInt(2)]) : null)))))))));
	}

	private String pickMobSpawner(Random random1) {
		int i2 = random1.nextInt(4);
		return i2 == 0 ? "Skeleton" : (i2 == 1 ? "Zombie" : (i2 == 2 ? "Zombie" : (i2 == 3 ? "Spider" : "")));
	}
}
