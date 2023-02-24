package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class PlayerControllerCreative extends PlayerController {
	public PlayerControllerCreative(Minecraft minecraft1) {
		super(minecraft1);
		this.isInTestMode = true;
	}

	public void onRespawn(EntityPlayer entityPlayer1) {
		for(int i2 = 0; i2 < 9; ++i2) {
			if(entityPlayer1.inventory.mainInventory[i2] == null) {
				this.mc.thePlayer.inventory.mainInventory[i2] = new ItemStack(((Block)Session.registeredBlocksList.get(i2)).blockID);
			} else {
				this.mc.thePlayer.inventory.mainInventory[i2].stackSize = 1;
			}
		}

	}

	public boolean shouldDrawHUD() {
		return false;
	}

	public void onWorldChange(World world1) {
		super.onWorldChange(world1);
	}

	public void onUpdate() {
	}
}
