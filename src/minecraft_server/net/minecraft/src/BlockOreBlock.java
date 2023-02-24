package net.minecraft.src;

public class BlockOreBlock extends Block {
	public BlockOreBlock(int id, int blockIndex) {
		super(id, Material.iron);
		this.blockIndexInTexture = blockIndex;
	}

	public int getBlockTextureFromSide(int i1) {
		return i1 == 1 ? this.blockIndexInTexture - 16 : (i1 == 0 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture);
	}
}
