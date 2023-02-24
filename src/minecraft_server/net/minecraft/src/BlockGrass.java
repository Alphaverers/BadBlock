package net.minecraft.src;

import java.util.Random;

public class BlockGrass extends Block {
	protected BlockGrass(int id) {
		super(id, Material.grass);
		this.blockIndexInTexture = 3;
		this.setTickOnLoad(true);
	}

	public void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		if(world1.getBlockLightValue(i2, i3 + 1, i4) < 4 && world1.getBlockMaterial(i2, i3 + 1, i4).getCanBlockGrass()) {
			if(random5.nextInt(4) != 0) {
				return;
			}

			world1.setBlockWithNotify(i2, i3, i4, Block.dirt.blockID);
		} else if(world1.getBlockLightValue(i2, i3 + 1, i4) >= 9) {
			int i6 = i2 + random5.nextInt(3) - 1;
			int i7 = i3 + random5.nextInt(5) - 3;
			int i8 = i4 + random5.nextInt(3) - 1;
			if(world1.getBlockId(i6, i7, i8) == Block.dirt.blockID && world1.getBlockLightValue(i6, i7 + 1, i8) >= 4 && !world1.getBlockMaterial(i6, i7 + 1, i8).getCanBlockGrass()) {
				world1.setBlockWithNotify(i6, i7, i8, Block.grass.blockID);
			}
		}

	}

	public int idDropped(int i1, Random random2) {
		return Block.dirt.idDropped(0, random2);
	}
}
