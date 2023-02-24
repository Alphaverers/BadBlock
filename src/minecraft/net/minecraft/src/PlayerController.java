package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class PlayerController {
	protected final Minecraft mc;
	public boolean isInTestMode = false;

	public PlayerController(Minecraft minecraft) {
		this.mc = minecraft;
	}

	public void onWorldChange(World world) {
	}

	public void clickBlock(int x, int y, int z, int side) {
		this.sendBlockRemoved(x, y, z, side);
	}

	public boolean sendBlockRemoved(int x, int y, int z, int side) {
		this.mc.effectRenderer.addBlockDestroyEffects(x, y, z);
		World world5 = this.mc.theWorld;
		Block block6 = Block.blocksList[world5.getBlockId(x, y, z)];
		int i7 = world5.getBlockMetadata(x, y, z);
		boolean z8 = world5.setBlockWithNotify(x, y, z, 0);
		if(block6 != null && z8) {
			this.mc.sndManager.playSound(block6.stepSound.getBreakSound(), (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, (block6.stepSound.getVolume() + 1.0F) / 2.0F, block6.stepSound.getPitch() * 0.8F);
			block6.onBlockDestroyedByPlayer(world5, x, y, z, i7);
		}

		return z8;
	}

	public void sendBlockRemoving(int x, int y, int z, int side) {
	}

	public void resetBlockRemoving() {
	}

	public void setPartialTime(float renderPartialTick) {
	}

	public float getBlockReachDistance() {
		return 5.0F;
	}

	public void flipPlayer(EntityPlayer entityPlayer) {
	}

	public void onUpdate() {
	}

	public boolean shouldDrawHUD() {
		return true;
	}

	public void onRespawn(EntityPlayer entityPlayer) {
	}

	public boolean onPlayerRightClick(EntityPlayer entityPlayer, World world, ItemStack itemStack, int x, int y, int z, int side) {
		int i8 = world.getBlockId(x, y, z);
		return i8 > 0 && Block.blocksList[i8].blockActivated(world, x, y, z, entityPlayer) ? true : (itemStack == null ? false : itemStack.useItem(entityPlayer, world, x, y, z, side));
	}

	public EntityPlayer createPlayer(World world) {
		return new EntityPlayerSP(this.mc, world, this.mc.session);
	}
}
