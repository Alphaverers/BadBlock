package net.minecraft.src;

import java.util.Random;

public class BlockFurnace extends BlockContainer {
	private final boolean isActive;

	protected BlockFurnace(int id, boolean isActive) {
		super(id, Material.rock);
		this.isActive = isActive;
		this.blockIndexInTexture = 45;
	}

	public int idDropped(int i1, Random random2) {
		return Block.stoneOvenIdle.blockID;
	}

	public void onBlockAdded(World world1, int i2, int i3, int i4) {
		super.onBlockAdded(world1, i2, i3, i4);
		this.setDefaultDirection(world1, i2, i3, i4);
	}

	private void setDefaultDirection(World world, int x, int y, int z) {
		int i5 = world.getBlockId(x, y, z - 1);
		int i6 = world.getBlockId(x, y, z + 1);
		int i7 = world.getBlockId(x - 1, y, z);
		int i8 = world.getBlockId(x + 1, y, z);
		byte b9 = 3;
		if(Block.opaqueCubeLookup[i5] && !Block.opaqueCubeLookup[i6]) {
			b9 = 3;
		}

		if(Block.opaqueCubeLookup[i6] && !Block.opaqueCubeLookup[i5]) {
			b9 = 2;
		}

		if(Block.opaqueCubeLookup[i7] && !Block.opaqueCubeLookup[i8]) {
			b9 = 5;
		}

		if(Block.opaqueCubeLookup[i8] && !Block.opaqueCubeLookup[i7]) {
			b9 = 4;
		}

		world.setBlockMetadataWithNotify(x, y, z, b9);
	}

	public int getBlockTextureFromSide(int i1) {
		return i1 == 1 ? Block.stone.blockID : (i1 == 0 ? Block.stone.blockID : (i1 == 3 ? this.blockIndexInTexture - 1 : this.blockIndexInTexture));
	}

	public boolean blockActivated(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
		TileEntityFurnace tileEntityFurnace6 = (TileEntityFurnace)world1.getBlockTileEntity(i2, i3, i4);
		entityPlayer5.displayGUIFurnace(tileEntityFurnace6);
		return true;
	}

	public static void updateFurnaceBlockState(boolean isActive, World world, int x, int y, int z) {
		int i5 = world.getBlockMetadata(x, y, z);
		TileEntity tileEntity6 = world.getBlockTileEntity(x, y, z);
		if(isActive) {
			world.setBlockWithNotify(x, y, z, Block.stoneOvenActive.blockID);
		} else {
			world.setBlockWithNotify(x, y, z, Block.stoneOvenIdle.blockID);
		}

		world.setBlockMetadataWithNotify(x, y, z, i5);
		world.setBlockTileEntity(x, y, z, tileEntity6);
	}

	protected TileEntity getBlockEntity() {
		return new TileEntityFurnace();
	}
}
