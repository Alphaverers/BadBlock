package net.minecraft.src;

public class ItemRecord extends Item {
	private String recordName;

	protected ItemRecord(int id, String record) {
		super(id);
		this.recordName = record;
		this.maxStackSize = 1;
	}

	public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int i4, int i5, int i6, int i7) {
		if(world3.getBlockId(i4, i5, i6) == Block.jukebox.blockID && world3.getBlockMetadata(i4, i5, i6) == 0) {
			world3.setBlockMetadataWithNotify(i4, i5, i6, this.shiftedIndex - Item.record13.shiftedIndex + 1);
			world3.playRecord(this.recordName, i4, i5, i6);
			--itemStack1.stackSize;
			return true;
		} else {
			return false;
		}
	}
}
