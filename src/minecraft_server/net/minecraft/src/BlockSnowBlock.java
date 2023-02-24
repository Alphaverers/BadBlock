package net.minecraft.src;

import java.util.Random;

public class BlockSnowBlock extends Block {
	protected BlockSnowBlock(int id, int blockIndex) {
		super(id, blockIndex, Material.craftedSnow);
		this.setTickOnLoad(true);
	}

	public int idDropped(int i1, Random random2) {
		return Item.snowball.shiftedIndex;
	}

	public int quantityDropped(Random random1) {
		return 4;
	}

	public void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		if(world1.getSavedLightValue(EnumSkyBlock.Block, i2, i3, i4) > 11) {
			this.dropBlockAsItem(world1, i2, i3, i4, world1.getBlockMetadata(i2, i3, i4));
			world1.setBlockWithNotify(i2, i3, i4, 0);
		}

	}
}
