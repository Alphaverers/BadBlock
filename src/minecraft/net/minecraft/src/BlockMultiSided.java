package net.minecraft.src;

import java.util.Random;

public class BlockMultiSided extends Block {
	private int texTop;
	private int texSide;
	private int texBottom;
	private int id;

	protected BlockMultiSided(int i1, int i2, int i3, int i4) {
		super(i1, Material.grass);
		this.blockIndexInTexture = i3;
		this.setTickOnLoad(true);
		this.texTop = i2;
		this.texSide = i3;
		this.texBottom = i4;
		this.id = i1;
	}

	public int getBlockTexture(IBlockAccess iBlockAccess1, int i2, int i3, int i4, int i5) {
		return i5 == 1 ? this.texTop : (i5 == 0 ? this.texBottom : this.texSide);
	}

	public void updateTick(World worldObj, int x, int y, int z, Random rand) {
	}

	public int idDropped(int i1, Random random2) {
		return this.id;
	}
}
