package net.minecraft.src;

public class ItemPainting extends Item {
	public ItemPainting(int i1) {
		super(i1);
		this.maxDamage = 64;
	}

	public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int i4, int i5, int i6, int i7) {
		if(i7 == 0) {
			return false;
		} else if(i7 == 1) {
			return false;
		} else {
			byte b8 = 0;
			if(i7 == 4) {
				b8 = 1;
			}

			if(i7 == 3) {
				b8 = 2;
			}

			if(i7 == 5) {
				b8 = 3;
			}

			EntityPainting entityPainting9 = new EntityPainting(world3, i4, i5, i6, b8);
			if(entityPainting9.onValidSurface()) {
				world3.spawnEntityInWorld(entityPainting9);
				--itemStack1.stackSize;
			}

			return true;
		}
	}
}
