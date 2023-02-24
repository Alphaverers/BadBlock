package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class PlayerControllerMP extends PlayerController {
	private int currentBlockX = -1;
	private int currentBlockY = -1;
	private int currentBlockZ = -1;
	private float curBlockDamageMP = 0.0F;
	private float prevBlockDamageMP = 0.0F;
	private float stepSoundTickCounter = 0.0F;
	private int blockHitDelay = 0;
	private boolean isHittingBlock = false;
	private NetClientHandler netClientHandler;
	private int currentPlayerItem = 0;

	public PlayerControllerMP(Minecraft minecraft, NetClientHandler netClientHandler) {
		super(minecraft);
		this.netClientHandler = netClientHandler;
	}

	public void flipPlayer(EntityPlayer entityPlayer1) {
		entityPlayer1.rotationYaw = -180.0F;
	}

	public boolean sendBlockRemoved(int x, int y, int z, int side) {
		this.netClientHandler.addToSendQueue(new Packet14BlockDig(3, x, y, z, side));
		int i5 = this.mc.theWorld.getBlockId(x, y, z);
		int i6 = this.mc.theWorld.getBlockMetadata(x, y, z);
		boolean z7 = super.sendBlockRemoved(x, y, z, side);
		ItemStack itemStack8 = this.mc.thePlayer.getCurrentEquippedItem();
		if(itemStack8 != null) {
			itemStack8.onDestroyBlock(i5, x, y, z);
			if(itemStack8.stackSize == 0) {
				itemStack8.onItemDestroyedByUse(this.mc.thePlayer);
				this.mc.thePlayer.destroyCurrentEquippedItem();
			}
		}

		if(z7 && this.mc.thePlayer.canHarvestBlock(Block.blocksList[i5])) {
			Block.blocksList[i5].dropBlockAsItem(this.mc.theWorld, x, y, z, i6);
		}

		return z7;
	}

	public void clickBlock(int i1, int i2, int i3, int i4) {
		this.isHittingBlock = true;
		this.netClientHandler.addToSendQueue(new Packet14BlockDig(0, i1, i2, i3, i4));
		int i5 = this.mc.theWorld.getBlockId(i1, i2, i3);
		if(i5 > 0 && this.curBlockDamageMP == 0.0F) {
			Block.blocksList[i5].onBlockClicked(this.mc.theWorld, i1, i2, i3, this.mc.thePlayer);
		}

		if(i5 > 0 && Block.blocksList[i5].blockStrength(this.mc.thePlayer) >= 1.0F) {
			this.sendBlockRemoved(i1, i2, i3, i4);
		}

	}

	public void resetBlockRemoving() {
		if(this.isHittingBlock) {
			this.isHittingBlock = false;
			this.netClientHandler.addToSendQueue(new Packet14BlockDig(2, 0, 0, 0, 0));
			this.curBlockDamageMP = 0.0F;
			this.blockHitDelay = 0;
		}
	}

	public void sendBlockRemoving(int i1, int i2, int i3, int i4) {
		this.isHittingBlock = true;
		this.syncCurrentPlayItem();
		this.netClientHandler.addToSendQueue(new Packet14BlockDig(1, i1, i2, i3, i4));
		if(this.blockHitDelay > 0) {
			--this.blockHitDelay;
		} else {
			if(i1 == this.currentBlockX && i2 == this.currentBlockY && i3 == this.currentBlockZ) {
				int i5 = this.mc.theWorld.getBlockId(i1, i2, i3);
				if(i5 == 0) {
					return;
				}

				Block block6 = Block.blocksList[i5];
				this.curBlockDamageMP += block6.blockStrength(this.mc.thePlayer);
				if(this.stepSoundTickCounter % 4.0F == 0.0F && block6 != null) {
					this.mc.sndManager.playSound(block6.stepSound.getStepSound(), (float)i1 + 0.5F, (float)i2 + 0.5F, (float)i3 + 0.5F, (block6.stepSound.getVolume() + 1.0F) / 8.0F, block6.stepSound.getPitch() * 0.5F);
				}

				++this.stepSoundTickCounter;
				if(this.curBlockDamageMP >= 1.0F) {
					this.sendBlockRemoved(i1, i2, i3, i4);
					this.curBlockDamageMP = 0.0F;
					this.prevBlockDamageMP = 0.0F;
					this.stepSoundTickCounter = 0.0F;
					this.blockHitDelay = 5;
				}
			} else {
				this.curBlockDamageMP = 0.0F;
				this.prevBlockDamageMP = 0.0F;
				this.stepSoundTickCounter = 0.0F;
				this.currentBlockX = i1;
				this.currentBlockY = i2;
				this.currentBlockZ = i3;
			}

		}
	}

	public void setPartialTime(float f1) {
		if(this.curBlockDamageMP <= 0.0F) {
			this.mc.ingameGUI.damageGuiPartialTime = 0.0F;
			this.mc.renderGlobal.damagePartialTime = 0.0F;
		} else {
			float f2 = this.prevBlockDamageMP + (this.curBlockDamageMP - this.prevBlockDamageMP) * f1;
			this.mc.ingameGUI.damageGuiPartialTime = f2;
			this.mc.renderGlobal.damagePartialTime = f2;
		}

	}

	public float getBlockReachDistance() {
		return 4.0F;
	}

	public void onWorldChange(World world1) {
		super.onWorldChange(world1);
	}

	public void onUpdate() {
		this.syncCurrentPlayItem();
		this.prevBlockDamageMP = this.curBlockDamageMP;
	}

	private void syncCurrentPlayItem() {
		ItemStack itemStack1 = this.mc.thePlayer.inventory.getCurrentItem();
		int i2 = 0;
		if(itemStack1 != null) {
			i2 = itemStack1.itemID;
		}

		if(i2 != this.currentPlayerItem) {
			this.currentPlayerItem = i2;
			this.netClientHandler.addToSendQueue(new Packet16BlockItemSwitch(0, this.currentPlayerItem));
		}

	}

	public boolean onPlayerRightClick(EntityPlayer entityPlayer1, World world2, ItemStack itemStack3, int i4, int i5, int i6, int i7) {
		this.syncCurrentPlayItem();
		this.netClientHandler.addToSendQueue(new Packet15Place(itemStack3 != null ? itemStack3.itemID : -1, i4, i5, i6, i7));
		return super.onPlayerRightClick(entityPlayer1, world2, itemStack3, i4, i5, i6, i7);
	}

	public EntityPlayer createPlayer(World world) {
		return new EntityClientPlayerMP(this.mc, world, this.mc.session, this.netClientHandler);
	}
}
