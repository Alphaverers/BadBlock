package net.minecraft.src;

import java.util.Random;

public class BlockStationary extends BlockFluid {
	protected BlockStationary(int i1, Material material2) {
		super(i1, material2);
		this.setTickOnLoad(false);
		if(material2 == Material.lava) {
			this.setTickOnLoad(true);
		}

	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		super.onNeighborBlockChange(world1, i2, i3, i4, i5);
		if(world1.getBlockId(i2, i3, i4) == this.blockID) {
			this.setNotStationary(world1, i2, i3, i4);
		}

	}

	private void setNotStationary(World world, int x, int y, int z) {
		int i5 = world.getBlockMetadata(x, y, z);
		world.editingBlocks = true;
		world.setBlockAndMetadata(x, y, z, this.blockID - 1, i5);
		world.markBlocksDirty(x, y, z, x, y, z);
		world.scheduleBlockUpdate(x, y, z, this.blockID - 1);
		world.editingBlocks = false;
	}

	public void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		if(this.material == Material.lava) {
			int i6 = random5.nextInt(3);

			for(int i7 = 0; i7 < i6; ++i7) {
				i2 += random5.nextInt(3) - 1;
				++i3;
				i4 += random5.nextInt(3) - 1;
				int i8 = world1.getBlockId(i2, i3, i4);
				if(i8 == 0) {
					if(this.isFlammable(world1, i2 - 1, i3, i4) || this.isFlammable(world1, i2 + 1, i3, i4) || this.isFlammable(world1, i2, i3, i4 - 1) || this.isFlammable(world1, i2, i3, i4 + 1) || this.isFlammable(world1, i2, i3 - 1, i4) || this.isFlammable(world1, i2, i3 + 1, i4)) {
						world1.setBlockWithNotify(i2, i3, i4, Block.fire.blockID);
						return;
					}
				} else if(Block.blockList[i8].material.getIsSolid()) {
					return;
				}
			}
		}

	}

	private boolean isFlammable(World world, int x, int y, int z) {
		return world.getBlockMaterial(x, y, z).getCanBurn();
	}
}
