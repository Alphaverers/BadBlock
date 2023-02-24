package net.minecraft.src;

public class BlockSponge extends Block {
	protected BlockSponge(int id) {
		super(id, Material.sponge);
		this.blockIndexInTexture = 48;
	}

	public void onBlockAdded(World world1, int i2, int i3, int i4) {
		byte b5 = 2;

		for(int i6 = i2 - b5; i6 <= i2 + b5; ++i6) {
			for(int i7 = i3 - b5; i7 <= i3 + b5; ++i7) {
				for(int i8 = i4 - b5; i8 <= i4 + b5; ++i8) {
					if(world1.getBlockMaterial(i6, i7, i8) == Material.water) {
						;
					}
				}
			}
		}

	}

	public void onBlockRemoval(World world1, int i2, int i3, int i4) {
		byte b5 = 2;

		for(int i6 = i2 - b5; i6 <= i2 + b5; ++i6) {
			for(int i7 = i3 - b5; i7 <= i3 + b5; ++i7) {
				for(int i8 = i4 - b5; i8 <= i4 + b5; ++i8) {
					world1.notifyBlocksOfNeighborChange(i6, i7, i8, world1.getBlockId(i6, i7, i8));
				}
			}
		}

	}
}
