package net.minecraft.src;

public class BlockHidable extends BlockGlass {
	public boolean render = true;
	public int id;
	public int tRes;

	public BlockHidable(int i1, int i2, Material material3, boolean z4) {
		super(i1, i2, material3, z4);
		this.id = i1;
		this.tRes = i2;
	}

	public int getBlockTexture(IBlockAccess iBlockAccess1, int i2, int i3, int i4, int i5) {
		return this.render ? this.tRes : 160;
	}

	public boolean blockActivated(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
		ItemStack itemStack6 = entityPlayer5.inventory.getCurrentItem();
		if(itemStack6 == null) {
			this.render = !this.render;
			world1.setBlockWithNotify(i2, i3, i4, this.id);
			return true;
		} else {
			return false;
		}
	}
}
