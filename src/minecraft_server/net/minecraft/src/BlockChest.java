package net.minecraft.src;

import java.util.Random;

public class BlockChest extends BlockContainer {
	private Random rand = new Random();

	protected BlockChest(int id) {
		super(id, Material.wood);
		this.blockIndexInTexture = 26;
	}

	public int getBlockTextureFromSide(int i1) {
		return i1 == 1 ? this.blockIndexInTexture - 1 : (i1 == 0 ? this.blockIndexInTexture - 1 : (i1 == 3 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture));
	}

	public boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		int i5 = 0;
		if(world1.getBlockId(i2 - 1, i3, i4) == this.blockID) {
			++i5;
		}

		if(world1.getBlockId(i2 + 1, i3, i4) == this.blockID) {
			++i5;
		}

		if(world1.getBlockId(i2, i3, i4 - 1) == this.blockID) {
			++i5;
		}

		if(world1.getBlockId(i2, i3, i4 + 1) == this.blockID) {
			++i5;
		}

		return i5 > 1 ? false : (this.isThereANeighborChest(world1, i2 - 1, i3, i4) ? false : (this.isThereANeighborChest(world1, i2 + 1, i3, i4) ? false : (this.isThereANeighborChest(world1, i2, i3, i4 - 1) ? false : !this.isThereANeighborChest(world1, i2, i3, i4 + 1))));
	}

	private boolean isThereANeighborChest(World world, int x, int y, int z) {
		return world.getBlockId(x, y, z) != this.blockID ? false : (world.getBlockId(x - 1, y, z) == this.blockID ? true : (world.getBlockId(x + 1, y, z) == this.blockID ? true : (world.getBlockId(x, y, z - 1) == this.blockID ? true : world.getBlockId(x, y, z + 1) == this.blockID)));
	}

	public void onBlockRemoval(World world1, int i2, int i3, int i4) {
		TileEntityChest tileEntityChest5 = (TileEntityChest)world1.getBlockTileEntity(i2, i3, i4);

		for(int i6 = 0; i6 < tileEntityChest5.getSizeInventory(); ++i6) {
			ItemStack itemStack7 = tileEntityChest5.getStackInSlot(i6);
			if(itemStack7 != null) {
				float f8 = this.rand.nextFloat() * 0.8F + 0.1F;
				float f9 = this.rand.nextFloat() * 0.8F + 0.1F;
				float f10 = this.rand.nextFloat() * 0.8F + 0.1F;

				while(itemStack7.stackSize > 0) {
					int i11 = this.rand.nextInt(21) + 10;
					if(i11 > itemStack7.stackSize) {
						i11 = itemStack7.stackSize;
					}

					itemStack7.stackSize -= i11;
					EntityItem entityItem12 = new EntityItem(world1, (double)((float)i2 + f8), (double)((float)i3 + f9), (double)((float)i4 + f10), new ItemStack(itemStack7.itemID, i11, itemStack7.itemDmg));
					float f13 = 0.05F;
					entityItem12.motionX = (double)((float)this.rand.nextGaussian() * f13);
					entityItem12.motionY = (double)((float)this.rand.nextGaussian() * f13 + 0.2F);
					entityItem12.motionZ = (double)((float)this.rand.nextGaussian() * f13);
					world1.spawnEntityInWorld(entityItem12);
				}
			}
		}

		super.onBlockRemoval(world1, i2, i3, i4);
	}

	public boolean blockActivated(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
		Object object6 = (TileEntityChest)world1.getBlockTileEntity(i2, i3, i4);
		if(world1.isBlockNormalCube(i2, i3 + 1, i4)) {
			return true;
		} else if(world1.getBlockId(i2 - 1, i3, i4) == this.blockID && world1.isBlockNormalCube(i2 - 1, i3 + 1, i4)) {
			return true;
		} else if(world1.getBlockId(i2 + 1, i3, i4) == this.blockID && world1.isBlockNormalCube(i2 + 1, i3 + 1, i4)) {
			return true;
		} else if(world1.getBlockId(i2, i3, i4 - 1) == this.blockID && world1.isBlockNormalCube(i2, i3 + 1, i4 - 1)) {
			return true;
		} else if(world1.getBlockId(i2, i3, i4 + 1) == this.blockID && world1.isBlockNormalCube(i2, i3 + 1, i4 + 1)) {
			return true;
		} else {
			if(world1.getBlockId(i2 - 1, i3, i4) == this.blockID) {
				object6 = new InventoryLargeChest("Large chest", (TileEntityChest)world1.getBlockTileEntity(i2 - 1, i3, i4), (IInventory)object6);
			}

			if(world1.getBlockId(i2 + 1, i3, i4) == this.blockID) {
				object6 = new InventoryLargeChest("Large chest", (IInventory)object6, (TileEntityChest)world1.getBlockTileEntity(i2 + 1, i3, i4));
			}

			if(world1.getBlockId(i2, i3, i4 - 1) == this.blockID) {
				object6 = new InventoryLargeChest("Large chest", (TileEntityChest)world1.getBlockTileEntity(i2, i3, i4 - 1), (IInventory)object6);
			}

			if(world1.getBlockId(i2, i3, i4 + 1) == this.blockID) {
				object6 = new InventoryLargeChest("Large chest", (IInventory)object6, (TileEntityChest)world1.getBlockTileEntity(i2, i3, i4 + 1));
			}

			entityPlayer5.displayGUIChest((IInventory)object6);
			return true;
		}
	}

	protected TileEntity getBlockEntity() {
		return new TileEntityChest();
	}
}
