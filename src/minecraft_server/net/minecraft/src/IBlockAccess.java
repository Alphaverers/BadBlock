package net.minecraft.src;

public interface IBlockAccess {
	int getBlockId(int i1, int i2, int i3);

	int getBlockMetadata(int i1, int i2, int i3);

	Material getBlockMaterial(int i1, int i2, int i3);

	boolean isBlockNormalCube(int i1, int i2, int i3);
}
