package net.minecraft.src;

import java.util.Random;

public class BlockMinecartTrack extends Block {
	protected BlockMinecartTrack(int id, int blockIndex) {
		super(id, blockIndex, Material.circuits);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world1, int i2, int i3, int i4) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public MovingObjectPosition collisionRayTrace(World world1, int i2, int i3, int i4, Vec3D vec3D5, Vec3D vec3D6) {
		this.setBlockBoundsBasedOnState(world1, i2, i3, i4);
		return super.collisionRayTrace(world1, i2, i3, i4, vec3D5, vec3D6);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess1, int i2, int i3, int i4) {
		int i5 = iBlockAccess1.getBlockMetadata(i2, i3, i4);
		if(i5 >= 2 && i5 <= 5) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
		} else {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		}

	}

	public int getRenderType() {
		return 9;
	}

	public int quantityDropped(Random random1) {
		return 1;
	}

	public boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		return world1.isBlockNormalCube(i2, i3 - 1, i4);
	}

	public void onBlockAdded(World world1, int i2, int i3, int i4) {
		world1.setBlockMetadataWithNotify(i2, i3, i4, 15);
		this.refreshTrackShape(world1, i2, i3, i4);
	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		int i6 = world1.getBlockMetadata(i2, i3, i4);
		boolean z7 = false;
		if(!world1.isBlockNormalCube(i2, i3 - 1, i4)) {
			z7 = true;
		}

		if(i6 == 2 && !world1.isBlockNormalCube(i2 + 1, i3, i4)) {
			z7 = true;
		}

		if(i6 == 3 && !world1.isBlockNormalCube(i2 - 1, i3, i4)) {
			z7 = true;
		}

		if(i6 == 4 && !world1.isBlockNormalCube(i2, i3, i4 - 1)) {
			z7 = true;
		}

		if(i6 == 5 && !world1.isBlockNormalCube(i2, i3, i4 + 1)) {
			z7 = true;
		}

		if(z7) {
			this.dropBlockAsItem(world1, i2, i3, i4, world1.getBlockMetadata(i2, i3, i4));
			world1.setBlockWithNotify(i2, i3, i4, 0);
		} else if(i5 > 0 && Block.blockList[i5].canProvidePower() && MinecartTrackLogic.getAdjacentTracks(new MinecartTrackLogic(this, world1, i2, i3, i4)) == 3) {
			this.refreshTrackShape(world1, i2, i3, i4);
		}

	}

	private void refreshTrackShape(World world, int x, int y, int z) {
		(new MinecartTrackLogic(this, world, x, y, z)).place(world.isBlockIndirectlyGettingPowered(x, y, z));
	}
}
