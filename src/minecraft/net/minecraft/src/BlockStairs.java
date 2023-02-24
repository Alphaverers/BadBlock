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

	public boolean renderAsNormalBlock() {
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
				this.updateState(world1, i2, i3, i4);
				this.updateState(world1, i2 + 1, i3 - 1, i4);
				this.updateState(world1, i2 - 1, i3 - 1, i4);
				this.updateState(world1, i2, i3 - 1, i4 - 1);
				this.updateState(world1, i2, i3 - 1, i4 + 1);
				this.updateState(world1, i2 + 1, i3 + 1, i4);
				this.updateState(world1, i2 - 1, i3 + 1, i4);
				this.updateState(world1, i2, i3 + 1, i4 - 1);
				this.updateState(world1, i2, i3 + 1, i4 + 1);
			}

			this.modelBlock.onNeighborBlockChange(world1, i2, i3, i4, i5);
		}
	}

	private void updateState(World worldObj, int x, int y, int z) {
		if(this.isBlockStair(worldObj, x, y, z)) {
			byte b5 = -1;
			if(this.isBlockStair(worldObj, x + 1, y + 1, z)) {
				b5 = 0;
			}

			if(this.isBlockStair(worldObj, x - 1, y + 1, z)) {
				b5 = 1;
			}

			if(this.isBlockStair(worldObj, x, y + 1, z + 1)) {
				b5 = 2;
			}

			if(this.isBlockStair(worldObj, x, y + 1, z - 1)) {
				b5 = 3;
			}

			if(b5 < 0) {
				if(this.isBlockSolid(worldObj, x + 1, y, z) && !this.isBlockSolid(worldObj, x - 1, y, z)) {
					b5 = 0;
				}

				if(this.isBlockSolid(worldObj, x - 1, y, z) && !this.isBlockSolid(worldObj, x + 1, y, z)) {
					b5 = 1;
				}

				if(this.isBlockSolid(worldObj, x, y, z + 1) && !this.isBlockSolid(worldObj, x, y, z - 1)) {
					b5 = 2;
				}

				if(this.isBlockSolid(worldObj, x, y, z - 1) && !this.isBlockSolid(worldObj, x, y, z + 1)) {
					b5 = 3;
				}
			}

			if(b5 < 0) {
				if(this.isBlockStair(worldObj, x - 1, y - 1, z)) {
					b5 = 0;
				}

				if(this.isBlockStair(worldObj, x + 1, y - 1, z)) {
					b5 = 1;
				}

				if(this.isBlockStair(worldObj, x, y - 1, z - 1)) {
					b5 = 2;
				}

				if(this.isBlockStair(worldObj, x, y - 1, z + 1)) {
					b5 = 3;
				}
			}

			if(b5 >= 0) {
				worldObj.setBlockMetadataWithNotify(x, y, z, b5);
			}

		}
	}

	private boolean isBlockSolid(World worldObj, int x, int y, int z) {
		return worldObj.getBlockMaterial(x, y, z).isSolid();
	}

	private boolean isBlockStair(World worldObj, int x, int y, int z) {
		int i5 = worldObj.getBlockId(x, y, z);
		return i5 == 0 ? false : Block.blocksList[i5].getRenderType() == 10;
	}

	public void randomDisplayTick(World world1, int i2, int i3, int i4, Random random5) {
		this.modelBlock.randomDisplayTick(world1, i2, i3, i4, random5);
	}

	public void onBlockClicked(World worldObj, int x, int y, int z, EntityPlayer entityPlayer) {
		this.modelBlock.onBlockClicked(worldObj, x, y, z, entityPlayer);
	}

	public void onBlockDestroyedByPlayer(World world1, int i2, int i3, int i4, int i5) {
		this.modelBlock.onBlockDestroyedByPlayer(world1, i2, i3, i4, i5);
	}

	public float getBlockBrightness(IBlockAccess blockAccess, int x, int y, int z) {
		return this.modelBlock.getBlockBrightness(blockAccess, x, y, z);
	}

	public float getExplosionResistance(Entity entity1) {
		return this.modelBlock.getExplosionResistance(entity1);
	}

	public int getRenderBlockPass() {
		return this.modelBlock.getRenderBlockPass();
	}

	public int idDropped(int i1, Random random2) {
		return this.modelBlock.idDropped(i1, random2);
	}

	public int quantityDropped(Random random1) {
		return this.modelBlock.quantityDropped(random1);
	}

	public int getBlockTextureFromSideAndMetadata(int i1, int i2) {
		return this.modelBlock.getBlockTextureFromSideAndMetadata(i1, i2);
	}

	public int getBlockTextureFromSide(int i1) {
		return this.modelBlock.getBlockTextureFromSide(i1);
	}

	public int getBlockTexture(IBlockAccess iBlockAccess1, int i2, int i3, int i4, int i5) {
		return this.modelBlock.getBlockTexture(iBlockAccess1, i2, i3, i4, i5);
	}

	public int tickRate() {
		return this.modelBlock.tickRate();
	}

	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world1, int i2, int i3, int i4) {
		return this.modelBlock.getSelectedBoundingBoxFromPool(world1, i2, i3, i4);
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

	public void dropBlockAsItemWithChance(World worldObj, int x, int y, int z, int metadata, float chance) {
		this.modelBlock.dropBlockAsItemWithChance(worldObj, x, y, z, metadata, chance);
	}

	public void dropBlockAsItem(World world1, int i2, int i3, int i4, int i5) {
		this.modelBlock.dropBlockAsItem(world1, i2, i3, i4, i5);
	}

	public void onEntityWalking(World worldObj, int x, int y, int z, Entity entity) {
		this.modelBlock.onEntityWalking(worldObj, x, y, z, entity);
	}

	public void updateTick(World worldObj, int x, int y, int z, Random rand) {
		this.modelBlock.updateTick(worldObj, x, y, z, rand);
	}

	public boolean blockActivated(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
		return this.modelBlock.blockActivated(world1, i2, i3, i4, entityPlayer5);
	}

	public void onBlockDestroyedByExplosion(World world1, int i2, int i3, int i4) {
		this.modelBlock.onBlockDestroyedByExplosion(world1, i2, i3, i4);
	}
}
