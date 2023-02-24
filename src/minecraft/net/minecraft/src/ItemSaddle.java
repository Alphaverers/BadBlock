package net.minecraft.src;

public class ItemSaddle extends Item {
	public ItemSaddle(int i1) {
		super(i1);
		this.maxStackSize = 1;
		this.maxDamage = 64;
	}

	public void saddleEntity(ItemStack itemStack1, EntityLiving entityLiving2) {
		if(entityLiving2 instanceof EntityPig) {
			EntityPig entityPig3 = (EntityPig)entityLiving2;
			if(!entityPig3.saddled) {
				entityPig3.saddled = true;
				--itemStack1.stackSize;
			}
		}

	}

	public void hitEntity(ItemStack itemStack1, EntityLiving entityLiving2) {
		this.saddleEntity(itemStack1, entityLiving2);
	}
}
