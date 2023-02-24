package net.minecraft.src;

import java.util.Random;

public class BlockFurnace extends BlockContainer {
	private final boolean isActive;

	protected BlockFurnace(int blockID, boolean isActive) {
		super(blockID, Material.rock);
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

	private void setDefaultDirection(World worldObj, int x, int y, int z) {
		int i5 = worldObj.getBlockId(x, y, z - 1);
		int i6 = worldObj.getBlockId(x, y, z + 1);
		int i7 = worldObj.getBlockId(x - 1, y, z);
		int i8 = worldObj.getBlockId(x + 1, y, z);
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

		worldObj.setBlockMetadataWithNotify(x, y, z, b9);
	}

	public int getBlockTexture(IBlockAccess iBlockAccess1, int i2, int i3, int i4, int i5) {
		if(i5 == 1) {
			return Block.stone.blockIndexInTexture;
		} else if(i5 == 0) {
			return Block.stone.blockIndexInTexture;
		} else {
			int i6 = iBlockAccess1.getBlockMetadata(i2, i3, i4);
			return i5 != i6 ? this.blockIndexInTexture : (this.isActive ? this.blockIndexInTexture + 16 : this.blockIndexInTexture - 1);
		}
	}

	public void randomDisplayTick(World world1, int i2, int i3, int i4, Random random5) {
		if(this.isActive) {
			int i6 = world1.getBlockMetadata(i2, i3, i4);
			float f7 = (float)i2 + 0.5F;
			float f8 = (float)i3 + 0.0F + random5.nextFloat() * 6.0F / 16.0F;
			float f9 = (float)i4 + 0.5F;
			float f10 = 0.52F;
			float f11 = random5.nextFloat() * 0.6F - 0.3F;
			if(i6 == 4) {
				world1.spawnParticle("smoke", (double)(f7 - f10), (double)f8, (double)(f9 + f11), 0.0D, 0.0D, 0.0D);
				world1.spawnParticle("flame", (double)(f7 - f10), (double)f8, (double)(f9 + f11), 0.0D, 0.0D, 0.0D);
			} else if(i6 == 5) {
				world1.spawnParticle("smoke", (double)(f7 + f10), (double)f8, (double)(f9 + f11), 0.0D, 0.0D, 0.0D);
				world1.spawnParticle("flame", (double)(f7 + f10), (double)f8, (double)(f9 + f11), 0.0D, 0.0D, 0.0D);
			} else if(i6 == 2) {
				world1.spawnParticle("smoke", (double)(f7 + f11), (double)f8, (double)(f9 - f10), 0.0D, 0.0D, 0.0D);
				world1.spawnParticle("flame", (double)(f7 + f11), (double)f8, (double)(f9 - f10), 0.0D, 0.0D, 0.0D);
			} else if(i6 == 3) {
				world1.spawnParticle("smoke", (double)(f7 + f11), (double)f8, (double)(f9 + f10), 0.0D, 0.0D, 0.0D);
				world1.spawnParticle("flame", (double)(f7 + f11), (double)f8, (double)(f9 + f10), 0.0D, 0.0D, 0.0D);
			}

		}
	}

	public int getBlockTextureFromSide(int i1) {
		return i1 == 1 ? Block.stone.blockID : (i1 == 0 ? Block.stone.blockID : (i1 == 3 ? this.blockIndexInTexture - 1 : this.blockIndexInTexture));
	}

	public boolean blockActivated(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
		TileEntityFurnace tileEntityFurnace6 = (TileEntityFurnace)world1.getBlockTileEntity(i2, i3, i4);
		entityPlayer5.displayGUIFurnace(tileEntityFurnace6);
		return true;
	}

	public static void updateFurnaceBlockState(boolean isActive, World worldObj, int x, int y, int z) {
		int i5 = worldObj.getBlockMetadata(x, y, z);
		TileEntity tileEntity6 = worldObj.getBlockTileEntity(x, y, z);
		if(isActive) {
			worldObj.setBlockWithNotify(x, y, z, Block.stoneOvenActive.blockID);
		} else {
			worldObj.setBlockWithNotify(x, y, z, Block.stoneOvenIdle.blockID);
		}

		worldObj.setBlockMetadataWithNotify(x, y, z, i5);
		worldObj.setBlockTileEntity(x, y, z, tileEntity6);
	}

	protected TileEntity getBlockEntity() {
		return new TileEntityFurnace();
	}
}
