package net.minecraft.src;

import java.util.Random;

public class BlockFire extends Block {
	private int[] chanceToEncourageFire = new int[256];
	private int[] abilityToCatchFire = new int[256];

	protected BlockFire(int id, int blockIndex) {
		super(id, blockIndex, Material.fire);
		this.initializeBlock(Block.planks.blockID, 5, 20);
		this.initializeBlock(Block.wood.blockID, 5, 5);
		this.initializeBlock(Block.leaves.blockID, 30, 60);
		this.initializeBlock(Block.bookshelf.blockID, 30, 20);
		this.initializeBlock(Block.tnt.blockID, 15, 100);
		this.initializeBlock(Block.cloth.blockID, 30, 60);
		this.setTickOnLoad(true);
	}

	private void initializeBlock(int blockID, int chance, int ability) {
		this.chanceToEncourageFire[blockID] = chance;
		this.abilityToCatchFire[blockID] = ability;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world1, int i2, int i3, int i4) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 3;
	}

	public int quantityDropped(Random random1) {
		return 0;
	}

	public int tickRate() {
		return 10;
	}

	public void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		int i6 = world1.getBlockMetadata(i2, i3, i4);
		if(i6 < 15) {
			world1.setBlockMetadataWithNotify(i2, i3, i4, i6 + 1);
			world1.scheduleBlockUpdate(i2, i3, i4, this.blockID);
		}

		if(!this.canNeighborBurn(world1, i2, i3, i4)) {
			if(!world1.isBlockNormalCube(i2, i3 - 1, i4) || i6 > 3) {
				world1.setBlockWithNotify(i2, i3, i4, 0);
			}

		} else if(!this.canBlockCatchFire(world1, i2, i3 - 1, i4) && i6 == 15 && random5.nextInt(4) == 0) {
			world1.setBlockWithNotify(i2, i3, i4, 0);
		} else {
			if(i6 % 2 == 0 && i6 > 2) {
				this.tryToCatchBlockOnFire(world1, i2 + 1, i3, i4, 300, random5);
				this.tryToCatchBlockOnFire(world1, i2 - 1, i3, i4, 300, random5);
				this.tryToCatchBlockOnFire(world1, i2, i3 - 1, i4, 200, random5);
				this.tryToCatchBlockOnFire(world1, i2, i3 + 1, i4, 250, random5);
				this.tryToCatchBlockOnFire(world1, i2, i3, i4 - 1, 300, random5);
				this.tryToCatchBlockOnFire(world1, i2, i3, i4 + 1, 300, random5);

				for(int i7 = i2 - 1; i7 <= i2 + 1; ++i7) {
					for(int i8 = i4 - 1; i8 <= i4 + 1; ++i8) {
						for(int i9 = i3 - 1; i9 <= i3 + 4; ++i9) {
							if(i7 != i2 || i9 != i3 || i8 != i4) {
								int i10 = 100;
								if(i9 > i3 + 1) {
									i10 += (i9 - (i3 + 1)) * 100;
								}

								int i11 = this.getChanceOfNeighborsEncouragingFire(world1, i7, i9, i8);
								if(i11 > 0 && random5.nextInt(i10) <= i11) {
									world1.setBlockWithNotify(i7, i9, i8, this.blockID);
								}
							}
						}
					}
				}
			}

		}
	}

	private void tryToCatchBlockOnFire(World world, int x, int y, int z, int chance, Random random) {
		int i7 = this.abilityToCatchFire[world.getBlockId(x, y, z)];
		if(random.nextInt(chance) < i7) {
			boolean z8 = world.getBlockId(x, y, z) == Block.tnt.blockID;
			if(random.nextInt(2) == 0) {
				world.setBlockWithNotify(x, y, z, this.blockID);
			} else {
				world.setBlockWithNotify(x, y, z, 0);
			}

			if(z8) {
				Block.tnt.onBlockDestroyedByPlayer(world, x, y, z, 0);
			}
		}

	}

	private boolean canNeighborBurn(World world, int x, int y, int z) {
		return this.canBlockCatchFire(world, x + 1, y, z) ? true : (this.canBlockCatchFire(world, x - 1, y, z) ? true : (this.canBlockCatchFire(world, x, y - 1, z) ? true : (this.canBlockCatchFire(world, x, y + 1, z) ? true : (this.canBlockCatchFire(world, x, y, z - 1) ? true : this.canBlockCatchFire(world, x, y, z + 1)))));
	}

	private int getChanceOfNeighborsEncouragingFire(World world, int x, int y, int z) {
		byte b5 = 0;
		if(world.getBlockId(x, y, z) != 0) {
			return 0;
		} else {
			int i6 = this.getChanceToEncourageFire(world, x + 1, y, z, b5);
			i6 = this.getChanceToEncourageFire(world, x - 1, y, z, i6);
			i6 = this.getChanceToEncourageFire(world, x, y - 1, z, i6);
			i6 = this.getChanceToEncourageFire(world, x, y + 1, z, i6);
			i6 = this.getChanceToEncourageFire(world, x, y, z - 1, i6);
			i6 = this.getChanceToEncourageFire(world, x, y, z + 1, i6);
			return i6;
		}
	}

	public boolean isCollidable() {
		return false;
	}

	public boolean canBlockCatchFire(IBlockAccess blockAccess, int x, int y, int z) {
		return this.chanceToEncourageFire[blockAccess.getBlockId(x, y, z)] > 0;
	}

	public int getChanceToEncourageFire(World world, int x, int y, int z, int flag) {
		int i6 = this.chanceToEncourageFire[world.getBlockId(x, y, z)];
		return i6 > flag ? i6 : flag;
	}

	public boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		return world1.isBlockNormalCube(i2, i3 - 1, i4) || this.canNeighborBurn(world1, i2, i3, i4);
	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		if(!world1.isBlockNormalCube(i2, i3 - 1, i4) && !this.canNeighborBurn(world1, i2, i3, i4)) {
			world1.setBlockWithNotify(i2, i3, i4, 0);
		}
	}

	public void onBlockAdded(World world1, int i2, int i3, int i4) {
		if(!world1.isBlockNormalCube(i2, i3 - 1, i4) && !this.canNeighborBurn(world1, i2, i3, i4)) {
			world1.setBlockWithNotify(i2, i3, i4, 0);
		} else {
			world1.scheduleBlockUpdate(i2, i3, i4, this.blockID);
		}
	}
}
