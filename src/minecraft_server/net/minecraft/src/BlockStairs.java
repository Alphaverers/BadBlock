package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class BlockStairs extends Block {
	private Block modelBlock;

	protected BlockStairs(int id, Block modelBlock) {
		super(id, modelBlock.blockIndexInTexture, modelBlock.material);
		this.modelBlock = modelBlock;
		this.setHardness(modelBlock.hardness);
		this.setResistance(modelBlock.resistance / 3.0F);
		this.setStepSound(modelBlock.stepSound);
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 10;
	}

	public boolean shouldSideBeRendered(IBlockAccess iBlockAccess1, int i2, int i3, int i4, int i5) {
		return super.shouldSideBeRendered(iBlockAccess1, i2, i3, i4, i5);
	}

	public void getCollidingBoundingBoxes(World world1, int i2, int i3, int i4, AxisAlignedBB axisAlignedBB5, ArrayList arrayList6) {
		int i7 = world1.getBlockMetadata(i2, i3, i4);
		if(i7 == 0) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F);
			super.getCollidingBoundingBoxes(world1, i2, i3, i4, axisAlignedBB5, arrayList6);
			this.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(world1, i2, i3, i4, axisAlignedBB5, arrayList6);
		} else if(i7 == 1) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(world1, i2, i3, i4, axisAlignedBB5, arrayList6);
			this.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
			super.getCollidingBoundingBoxes(world1, i2, i3, i4, axisAlignedBB5, arrayList6);
		} else if(i7 == 2) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F);
			super.getCollidingBoundingBoxes(world1, i2, i3, i4, axisAlignedBB5, arrayList6);
			this.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(world1, i2, i3, i4, axisAlignedBB5, arrayList6);
		} else if(i7 == 3) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
			super.getCollidingBoundingBoxes(world1, i2, i3, i4, axisAlignedBB5, arrayList6);
			this.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
			super.getCollidingBoundingBoxes(world1, i2, i3, i4, axisAlignedBB5, arrayList6);
		}

		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		if(!world1.multiplayerWorld) {
			if(world1.getBlockMaterial(i2, i3 + 1, i4).isSolid()) {
				world1.setBlockWithNotify(i2, i3, i4, this.modelBlock.blockID);
			} else {
				this.g(world1, i2, i3, i4);
				this.g(world1, i2 + 1, i3 - 1, i4);
				this.g(world1, i2 - 1, i3 - 1, i4);
				this.g(world1, i2, i3 - 1, i4 - 1);
				this.g(world1, i2, i3 - 1, i4 + 1);
				this.g(world1, i2 + 1, i3 + 1, i4);
				this.g(world1, i2 - 1, i3 + 1, i4);
				this.g(world1, i2, i3 + 1, i4 - 1);
				this.g(world1, i2, i3 + 1, i4 + 1);
			}

			this.modelBlock.onNeighborBlockChange(world1, i2, i3, i4, i5);
		}
	}

	private void g(World world, int x, int y, int z) {
		if(this.isBlockStair(world, x, y, z)) {
			byte b5 = -1;
			if(this.isBlockStair(world, x + 1, y + 1, z)) {
				b5 = 0;
			}

			if(this.isBlockStair(world, x - 1, y + 1, z)) {
				b5 = 1;
			}

			if(this.isBlockStair(world, x, y + 1, z + 1)) {
				b5 = 2;
			}

			if(this.isBlockStair(world, x, y + 1, z - 1)) {
				b5 = 3;
			}

			if(b5 < 0) {
				if(this.isBlockSolid(world, x + 1, y, z) && !this.isBlockSolid(world, x - 1, y, z)) {
					b5 = 0;
				}

				if(this.isBlockSolid(world, x - 1, y, z) && !this.isBlockSolid(world, x + 1, y, z)) {
					b5 = 1;
				}

				if(this.isBlockSolid(world, x, y, z + 1) && !this.isBlockSolid(world, x, y, z - 1)) {
					b5 = 2;
				}

				if(this.isBlockSolid(world, x, y, z - 1) && !this.isBlockSolid(world, x, y, z + 1)) {
					b5 = 3;
				}
			}

			if(b5 < 0) {
				if(this.isBlockStair(world, x - 1, y - 1, z)) {
					b5 = 0;
				}

				if(this.isBlockStair(world, x + 1, y - 1, z)) {
					b5 = 1;
				}

				if(this.isBlockStair(world, x, y - 1, z - 1)) {
					b5 = 2;
				}

				if(this.isBlockStair(world, x, y - 1, z + 1)) {
					b5 = 3;
				}
			}

			if(b5 >= 0) {
				world.setBlockMetadataWithNotify(x, y, z, b5);
			}

		}
	}

	private boolean isBlockSolid(World world, int x, int y, int z) {
		return world.getBlockMaterial(x, y, z).isSolid();
	}

	private boolean isBlockStair(World world, int x, int y, int z) {
		int i5 = world.getBlockId(x, y, z);
		return i5 == 0 ? false : Block.blockList[i5].getRenderType() == 10;
	}

	public void onBlockClicked(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
		this.modelBlock.onBlockClicked(world1, i2, i3, i4, entityPlayer5);
	}

	public void onBlockDestroyedByPlayer(World world1, int i2, int i3, int i4, int i5) {
		this.modelBlock.onBlockDestroyedByPlayer(world1, i2, i3, i4, i5);
	}

	public float getExplosionResistance(Entity entity1) {
		return this.modelBlock.getExplosionResistance(entity1);
	}

	public int idDropped(int i1, Random random2) {
		return this.modelBlock.idDropped(i1, random2);
	}

	public int quantityDropped(Random random1) {
		return this.modelBlock.quantityDropped(random1);
	}

	public int getBlockTextureFromSide(int i1) {
		return this.modelBlock.getBlockTextureFromSide(i1);
	}

	public int tickRate() {
		return this.modelBlock.tickRate();
	}

	public void velocityToAddToEntity(World world1, int i2, int i3, int i4, Entity entity5, Vec3D vec3D6) {
		this.modelBlock.velocityToAddToEntity(world1, i2, i3, i4, entity5, vec3D6);
	}

	public boolean isCollidable() {
		return this.modelBlock.isCollidable();
	}

	public boolean canCollideCheck(int i1, boolean z2) {
		return this.modelBlock.canCollideCheck(i1, z2);
	}

	public boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		return this.modelBlock.canPlaceBlockAt(world1, i2, i3, i4);
	}

	public void onBlockAdded(World world1, int i2, int i3, int i4) {
		this.onNeighborBlockChange(world1, i2, i3, i4, 0);
		this.modelBlock.onBlockAdded(world1, i2, i3, i4);
	}

	public void onBlockRemoval(World world1, int i2, int i3, int i4) {
		this.modelBlock.onBlockRemoval(world1, i2, i3, i4);
	}

	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int i5, float f6) {
		this.modelBlock.dropBlockAsItemWithChance(world, x, y, z, i5, f6);
	}

	public void dropBlockAsItem(World world, int i2, int i3, int i4, int i5) {
		this.modelBlock.dropBlockAsItem(world, i2, i3, i4, i5);
	}

	public void onEntityWalking(World world1, int i2, int i3, int i4, Entity entity5) {
		this.modelBlock.onEntityWalking(world1, i2, i3, i4, entity5);
	}

	public void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		this.modelBlock.updateTick(world1, i2, i3, i4, random5);
	}

	public boolean blockActivated(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
		return this.modelBlock.blockActivated(world1, i2, i3, i4, entityPlayer5);
	}

	public void onBlockDestroyedByExplosion(World world, int x, int y, int z) {
		this.modelBlock.onBlockDestroyedByExplosion(world, x, y, z);
	}
}
