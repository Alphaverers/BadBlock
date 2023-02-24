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

	private void setNotStationary(World worldObj, int x, int y, int z) {
		int i5 = worldObj.getBlockMetadata(x, y, z);
		worldObj.editingBlocks = true;
		worldObj.setBlockAndMetadata(x, y, z, this.blockID - 1, i5);
		worldObj.markBlocksDirty(x, y, z, x, y, z);
		worldObj.scheduleBlockUpdate(x, y, z, this.blockID - 1);
		worldObj.editingBlocks = false;
	}

	public void updateTick(World worldObj, int x, int y, int z, Random rand) {
		if(this.material == Material.lava) {
			int i6 = rand.nextInt(3);

			for(int i7 = 0; i7 < i6; ++i7) {
				x += rand.nextInt(3) - 1;
				++y;
				z += rand.nextInt(3) - 1;
				int i8 = worldObj.getBlockId(x, y, z);
				if(i8 == 0) {
					if(this.isFlammable(worldObj, x - 1, y, z) || this.isFlammable(worldObj, x + 1, y, z) || this.isFlammable(worldObj, x, y, z - 1) || this.isFlammable(worldObj, x, y, z + 1) || this.isFlammable(worldObj, x, y - 1, z) || this.isFlammable(worldObj, x, y + 1, z)) {
						worldObj.setBlockWithNotify(x, y, z, Block.fire.blockID);
						return;
					}
				} else if(Block.blocksList[i8].material.getIsSolid()) {
					return;
				}
			}
		}

	}

	private boolean isFlammable(World worldObj, int x, int y, int z) {
		return worldObj.getBlockMaterial(x, y, z).getCanBurn();
	}
}
