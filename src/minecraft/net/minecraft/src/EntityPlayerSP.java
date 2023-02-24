package net.minecraft.src;

import java.util.Random;

import net.minecraft.client.Minecraft;

public class EntityPlayerSP extends EntityPlayer {
	public MovementInput movementInput;
	private Minecraft mc;
	private Random rng = new Random();
	public int hunger = 0;
	public int dashTimer = 0;
	private int lastGroupX = 0;
	private int lastGroupY = 0;

	public EntityPlayerSP(Minecraft mc, World worldObj, Session session) {
		super(worldObj);
		this.mc = mc;
		if(session != null && session.username != null && session.username.length() > 0) {
			boolean z4 = System.getProperty("os.name").toLowerCase().indexOf("windows") == -1;
			this.skinUrl = "file:///" + (z4 ? "." : "C:") + "/skincache/" + session.username + ".png";
			System.out.println("Loading texture " + this.skinUrl);
		}

		this.username = session.username;
	}

	public void updateEntityActionState() {
		super.updateEntityActionState();
		this.moveStrafing = this.movementInput.moveStrafe;
		this.moveForward = this.movementInput.moveForward;
		this.isJumping = this.movementInput.jump;
	}

	public void onLivingUpdate() {
		this.movementInput.updatePlayerMoveState(this);
		if(this.mc.options.difficulty != 4 && this.mc.options.difficulty != 0) {
			++this.hunger;
			if(this.hunger >= 1200) {
				this.attackEntityFrom((Entity)null, 2);
				this.hunger = 0;
			}
		}

		if(this.dashTimer > 0) {
			--this.dashTimer;
			if(this.dashTimer == 0) {
				this.mc.theWorld.playSoundEffect(this.posX, this.posY, this.posZ, "ext.recharg", 0.6F, 1.0F);
			}
		}

		int i1 = (int)this.posX / 32;
		int i2 = (int)this.posZ / 32;
		if(this.lastGroupX != i1 || this.lastGroupY != i2) {
			this.lastGroupX = i1;
			this.lastGroupY = i2;
			if(InputHandler.IsKeyDown(207) || this.rng.nextInt(100) > 90) {
				if(InputHandler.IsKeyDown(207)) {
					System.out.println("Taken the Titan\'s challenge. Good luck.");
				}

				this.worldObj.CueSpawnBossFrom((int)this.posX, (int)this.posZ);
			}
		}

		if(this.worldObj.milestone >= 10L) {
			;
		}

		super.onLivingUpdate();
	}

	public void resetPlayerKeyState() {
		this.movementInput.resetKeyState();
	}

	public boolean attackEntityFrom(Entity entity1, int i2) {
		if(this.mc.options.difficulty != 4) {
			return super.attackEntityFrom(entity1, i2);
		} else if(this.health <= 0) {
			return false;
		} else if((float)this.heartsLife > (float)this.heartsHalvesLife / 2.0F) {
			return false;
		} else {
			this.heartsLife = this.heartsHalvesLife;
			System.out.println("Damage taken: " + i2);
			if(this.inventory.getTotalArmorValue() == 0) {
				this.health = 0;
				this.onDeath((Entity)null);
				this.worldObj.playSoundAtEntity(this, "random.glass", 1.0F, 1.0F);
			} else {
				this.worldObj.playSoundAtEntity(this, "ext.crack", 1.0F, 1.0F);
				int i3 = 0;

				for(int i4 = 0; i4 != 4; ++i4) {
					i3 += this.inventory.armorItemInSlot(i4) == null ? 0 : 1;
				}

				this.inventory.damageArmor(i2 * 16 / i3);
			}

			return true;
		}
	}

	public void handleKeyPress(int i1, boolean z2) {
		this.movementInput.checkKeyForMovementInput(i1, z2);
	}

	public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeEntityToNBT(nBTTagCompound1);
		nBTTagCompound1.setInteger("Score", this.score);
	}

	public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readEntityFromNBT(nBTTagCompound1);
		this.score = nBTTagCompound1.getInteger("Score");
	}

	public void displayGUIChest(IInventory iInventory1) {
		this.mc.displayGuiScreen(new GuiChest(this.inventory, iInventory1));
	}

	public void displayGUIEditSign(TileEntitySign signTileEntity) {
		this.mc.displayGuiScreen(new GuiEditSign(signTileEntity));
	}

	public void displayWorkbenchGUI() {
		this.mc.displayGuiScreen(new GuiCrafting(this.inventory));
	}

	public void displayGUIFurnace(TileEntityFurnace tileEntityFurnace1) {
		this.mc.displayGuiScreen(new GuiFurnace(this.inventory, tileEntityFurnace1));
	}

	public void attackEntity(Entity entity) {
		int i2 = this.inventory.getDamageVsEntity(entity);
		if(i2 > 0) {
			entity.attackEntityFrom(this, i2);
			ItemStack itemStack3 = this.getCurrentEquippedItem();
			if(itemStack3 != null && entity instanceof EntityLiving) {
				itemStack3.hitEntity((EntityLiving)entity);
				if(itemStack3.stackSize <= 0) {
					itemStack3.onItemDestroyedByUse(this);
					this.destroyCurrentEquippedItem();
				}
			}
		}

	}

	public void onItemPickup(Entity entity1, int i2) {
		this.mc.effectRenderer.addEffect(new EntityPickupFX(this.mc.theWorld, entity1, this, -0.5F));
	}

	public int getPlayerArmorValue() {
		return this.inventory.getTotalArmorValue();
	}

	public void interactWithEntity(Entity entity) {
		if(!entity.interact(this)) {
			ItemStack itemStack2 = this.getCurrentEquippedItem();
			if(itemStack2 != null && entity instanceof EntityLiving) {
				itemStack2.useItemOnEntity((EntityLiving)entity);
				if(itemStack2.stackSize <= 0) {
					itemStack2.onItemDestroyedByUse(this);
					this.destroyCurrentEquippedItem();
				}
			}

		}
	}

	public void sendChatMessage(String chatMessage) {
	}

	public void onPlayerUpdate() {
	}
}
