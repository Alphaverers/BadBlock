package net.minecraft.src;

import java.io.IOException;
import java.util.logging.Logger;

import net.minecraft.server.MinecraftServer;

public class NetServerHandler extends NetHandler implements ICommandListener {
	public static Logger logger = Logger.getLogger("Minecraft");
	public NetworkManager netManager;
	public boolean connectionClosed = false;
	private MinecraftServer mcServer;
	private EntityPlayerMP playerEntity;
	private int playerInAirTime = 0;
	private double lastPosX;
	private double lastPosY;
	private double lastPosZ;
	private boolean hasMoved = true;
	private ItemStack heldItem = null;

	public NetServerHandler(MinecraftServer minecraftServer1, NetworkManager networkManager2, EntityPlayerMP entityPlayerMP3) {
		this.mcServer = minecraftServer1;
		this.netManager = networkManager2;
		networkManager2.setNetHandler(this);
		this.playerEntity = entityPlayerMP3;
		entityPlayerMP3.playerNetServerHandler = this;
	}

	public void handlePackets() throws IOException {
		this.netManager.processReadPackets();
		if(this.playerInAirTime++ % 20 == 0) {
			this.netManager.addToSendQueue(new Packet0KeepAlive());
		}

	}

	public void kickPlayer(String string1) {
		this.netManager.addToSendQueue(new Packet255KickDisconnect(string1));
		this.netManager.serverShutdown();
		this.mcServer.configManager.playerLoggedOut(this.playerEntity);
		this.connectionClosed = true;
	}

	public void handleFlying(Packet10Flying packet10Flying1) {
		double d2;
		if(!this.hasMoved) {
			d2 = packet10Flying1.yPosition - this.lastPosY;
			if(packet10Flying1.xPosition == this.lastPosX && d2 * d2 < 0.01D && packet10Flying1.zPosition == this.lastPosZ) {
				this.hasMoved = true;
			}
		}

		if(this.hasMoved) {
			this.lastPosX = this.playerEntity.posX;
			this.lastPosY = this.playerEntity.posY;
			this.lastPosZ = this.playerEntity.posZ;
			d2 = this.playerEntity.posX;
			double d4 = this.playerEntity.posY;
			double d6 = this.playerEntity.posZ;
			float f8 = this.playerEntity.rotationYaw;
			float f9 = this.playerEntity.rotationPitch;
			double d10;
			if(packet10Flying1.moving) {
				d2 = packet10Flying1.xPosition;
				d4 = packet10Flying1.yPosition;
				d6 = packet10Flying1.zPosition;
				d10 = packet10Flying1.stance - packet10Flying1.yPosition;
				if(d10 > 1.65D || d10 < 0.1D) {
					this.kickPlayer("Illegal stance");
					logger.warning(this.playerEntity.username + " had an illegal stance: " + d10);
				}

				this.playerEntity.managedPosY = packet10Flying1.stance;
			}

			if(packet10Flying1.rotating) {
				f8 = packet10Flying1.yaw;
				f9 = packet10Flying1.pitch;
			}

			this.playerEntity.onUpdateEntity();
			this.playerEntity.ySize = 0.0F;
			this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, f8, f9);
			d10 = d2 - this.playerEntity.posX;
			double d12 = d4 - this.playerEntity.posY;
			double d14 = d6 - this.playerEntity.posZ;
			float f16 = 0.0625F;
			boolean z17 = this.mcServer.worldMngr.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.boundingBox.copy().removeCoord((double)f16, (double)f16, (double)f16)).size() == 0;
			this.playerEntity.moveEntity(d10, d12, d14);
			d10 = d2 - this.playerEntity.posX;
			d12 = d4 - this.playerEntity.posY;
			if(d12 > -0.5D || d12 < 0.5D) {
				d12 = 0.0D;
			}

			d14 = d6 - this.playerEntity.posZ;
			double d18 = d10 * d10 + d12 * d12 + d14 * d14;
			boolean z20 = false;
			if(d18 > 0.0625D) {
				z20 = true;
				logger.warning(this.playerEntity.username + " moved wrongly!");
			}

			this.playerEntity.setPositionAndRotation(d2, d4, d6, f8, f9);
			boolean z21 = this.mcServer.worldMngr.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.boundingBox.copy().removeCoord((double)f16, (double)f16, (double)f16)).size() == 0;
			if(z17 && (z20 || !z21)) {
				this.teleportTo(this.lastPosX, this.lastPosY, this.lastPosZ, f8, f9);
				return;
			}

			this.playerEntity.onGround = packet10Flying1.onGround;
			this.mcServer.configManager.serverUpdateMountedMovingPlayer(this.playerEntity);
		}

	}

	public void teleportTo(double d1, double d3, double d5, float f7, float f8) {
		this.hasMoved = false;
		this.lastPosX = d1;
		this.lastPosY = d3;
		this.lastPosZ = d5;
		this.playerEntity.setPositionAndRotation(d1, d3, d5, f7, f8);
		this.playerEntity.playerNetServerHandler.sendPacket(new Packet13PlayerLookMove(d1, d3 + (double)1.62F, d3, d5, f7, f8, false));
	}

	public void handleBlockDig(Packet14BlockDig packet14BlockDig1) {
		this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem] = this.heldItem;
		boolean z2 = this.mcServer.worldMngr.disableSpawnProtection = this.mcServer.configManager.isOp(this.playerEntity.username);
		boolean z3 = false;
		if(packet14BlockDig1.status == 0) {
			z3 = true;
		}

		if(packet14BlockDig1.status == 1) {
			z3 = true;
		}

		if(z3) {
			double d4 = this.playerEntity.posY;
			this.playerEntity.posY = this.playerEntity.managedPosY;
			MovingObjectPosition movingObjectPosition6 = this.playerEntity.rayTrace(4.0D, 1.0F);
			this.playerEntity.posY = d4;
			if(movingObjectPosition6 == null) {
				return;
			}

			if(movingObjectPosition6.blockX != packet14BlockDig1.xPosition || movingObjectPosition6.blockY != packet14BlockDig1.yPosition || movingObjectPosition6.blockZ != packet14BlockDig1.zPosition || movingObjectPosition6.sideHit != packet14BlockDig1.face) {
				return;
			}
		}

		int i18 = packet14BlockDig1.xPosition;
		int i5 = packet14BlockDig1.yPosition;
		int i19 = packet14BlockDig1.zPosition;
		int i7 = packet14BlockDig1.face;
		int i8 = (int)MathHelper.abs((float)(i18 - this.mcServer.worldMngr.spawnX));
		int i9 = (int)MathHelper.abs((float)(i19 - this.mcServer.worldMngr.spawnZ));
		if(i8 > i9) {
			i9 = i8;
		}

		if(packet14BlockDig1.status == 0) {
			if(i9 > 16 || z2) {
				this.playerEntity.theItemInWorldManager.onBlockClicked(i18, i5, i19);
			}
		} else if(packet14BlockDig1.status == 2) {
			this.playerEntity.theItemInWorldManager.blockRemoving();
		} else if(packet14BlockDig1.status == 1) {
			if(i9 > 16 || z2) {
				this.playerEntity.theItemInWorldManager.updateBlockRemoving(i18, i5, i19, i7);
			}
		} else if(packet14BlockDig1.status == 3) {
			double d10 = this.playerEntity.posX - ((double)i18 + 0.5D);
			double d12 = this.playerEntity.posY - ((double)i5 + 0.5D);
			double d14 = this.playerEntity.posZ - ((double)i19 + 0.5D);
			double d16 = d10 * d10 + d12 * d12 + d14 * d14;
			if(d16 < 256.0D) {
				this.playerEntity.playerNetServerHandler.sendPacket(new Packet53BlockChange(i18, i5, i19, this.mcServer.worldMngr));
			}
		}

		this.mcServer.worldMngr.disableSpawnProtection = false;
	}

	public void handlePlace(Packet15Place packet) {
		boolean z2 = this.mcServer.worldMngr.disableSpawnProtection = this.mcServer.configManager.isOp(this.playerEntity.username);
		int i3 = packet.xPosition;
		int i4 = packet.yPosition;
		int i5 = packet.zPosition;
		int i6 = packet.direction;
		int i7 = (int)MathHelper.abs((float)(i3 - this.mcServer.worldMngr.spawnX));
		int i8 = (int)MathHelper.abs((float)(i5 - this.mcServer.worldMngr.spawnZ));
		if(i7 > i8) {
			i8 = i7;
		}

		if(i8 > 16 || z2) {
			ItemStack itemStack9 = packet.id >= 0 ? new ItemStack(packet.id) : null;
			this.playerEntity.theItemInWorldManager.activeBlockOrUseItem(this.playerEntity, this.mcServer.worldMngr, itemStack9, i3, i4, i5, i6);
		}

		this.playerEntity.playerNetServerHandler.sendPacket(new Packet53BlockChange(i3, i4, i5, this.mcServer.worldMngr));
		this.mcServer.worldMngr.disableSpawnProtection = false;
	}

	public void handleErrorMessage(String string1) {
		logger.info(this.playerEntity.username + " lost connection: " + string1);
		this.mcServer.configManager.playerLoggedOut(this.playerEntity);
		this.connectionClosed = true;
	}

	public void registerPacket(Packet packet) {
		logger.warning(this.getClass() + " wasn\'t prepared to deal with a " + packet.getClass());
		this.kickPlayer("Protocol error, unexpected packet");
	}

	public void sendPacket(Packet packet1) {
		this.netManager.addToSendQueue(packet1);
	}

	public void handleBlockItemSwitch(Packet16BlockItemSwitch packet) {
		int i2 = packet.id;
		this.playerEntity.inventory.currentItem = this.playerEntity.inventory.mainInventory.length - 1;
		if(i2 == 0) {
			this.heldItem = null;
		} else {
			this.heldItem = new ItemStack(i2);
		}

		this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem] = this.heldItem;
		this.mcServer.entityTracker.sendPacketToTrackedPlayers(this.playerEntity, new Packet16BlockItemSwitch(this.playerEntity.entityID, i2));
	}

	public void handlePickupSpawn(Packet21PickupSpawn packet21PickupSpawn1) {
		double d2 = (double)packet21PickupSpawn1.xPosition / 32.0D;
		double d4 = (double)packet21PickupSpawn1.yPosition / 32.0D;
		double d6 = (double)packet21PickupSpawn1.zPosition / 32.0D;
		EntityItem entityItem8 = new EntityItem(this.mcServer.worldMngr, d2, d4, d6, new ItemStack(packet21PickupSpawn1.itemID, packet21PickupSpawn1.count));
		entityItem8.motionX = (double)packet21PickupSpawn1.rotation / 128.0D;
		entityItem8.motionY = (double)packet21PickupSpawn1.pitch / 128.0D;
		entityItem8.motionZ = (double)packet21PickupSpawn1.roll / 128.0D;
		entityItem8.delayBeforeCanPickup = 10;
		this.mcServer.worldMngr.spawnEntityInWorld(entityItem8);
	}

	public void handleChat(Packet3Chat packet) {
		String string2 = packet.message;
		if(string2.length() > 100) {
			this.kickPlayer("Chat message too long");
		} else {
			string2 = string2.trim();

			for(int i3 = 0; i3 < string2.length(); ++i3) {
				if(" !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_\'abcdefghijklmnopqrstuvwxyz{|}~\u2302\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb".indexOf(string2.charAt(i3)) < 0) {
					this.kickPlayer("Illegal characters in chat");
					return;
				}
			}

			if(string2.startsWith("/")) {
				this.handleSlashCommand(string2);
			} else {
				string2 = "<" + this.playerEntity.username + "> " + string2;
				logger.info(string2);
				this.mcServer.configManager.sendPacketToAllPlayers(new Packet3Chat(string2));
			}

		}
	}

	private void handleSlashCommand(String string1) {
		if(string1.toLowerCase().startsWith("/me ")) {
			string1 = "* " + this.playerEntity.username + " " + string1.substring(string1.indexOf(" ")).trim();
			logger.info(string1);
			this.mcServer.configManager.sendPacketToAllPlayers(new Packet3Chat(string1));
		} else if(string1.toLowerCase().startsWith("/tell ")) {
			String[] string2 = string1.split(" ");
			if(string2.length >= 3) {
				string1 = string1.substring(string1.indexOf(" ")).trim();
				string1 = string1.substring(string1.indexOf(" ")).trim();
				string1 = "\u00a77" + this.playerEntity.username + " whispers " + string1;
				logger.info(string1 + " to " + string2[1]);
				if(!this.mcServer.configManager.sendPacketToPlayer(string2[1], new Packet3Chat(string1))) {
					this.sendPacket(new Packet3Chat("\u00a7cThere\'s no player by that name online."));
				}
			}
		} else {
			int i3;
			if(string1.toLowerCase().equalsIgnoreCase("/home")) {
				logger.info(this.playerEntity.username + " returned home");
				i3 = this.mcServer.worldMngr.getTopSolidOrLiquidBlock(this.mcServer.worldMngr.spawnX, this.mcServer.worldMngr.spawnZ);
				this.teleportTo((double)this.mcServer.worldMngr.spawnX + 0.5D, (double)i3 + 1.5D, (double)this.mcServer.worldMngr.spawnZ + 0.5D, 0.0F, 0.0F);
			} else if(string1.toLowerCase().equalsIgnoreCase("/iron")) {
				if(MinecraftServer.playerList.containsKey(this.playerEntity.username)) {
					logger.info(this.playerEntity.username + " failed to iron!");
					this.sendPacket(new Packet3Chat("\u00a7cYou can\'t /iron again so soon!"));
				} else {
					MinecraftServer.playerList.put(this.playerEntity.username, 6000);
					logger.info(this.playerEntity.username + " ironed!");

					for(i3 = 0; i3 < 4; ++i3) {
						this.playerEntity.dropPlayerItem(new ItemStack(Item.ingotIron, 1));
					}
				}
			} else if(string1.toLowerCase().equalsIgnoreCase("/wood")) {
				if(MinecraftServer.playerList.containsKey(this.playerEntity.username)) {
					logger.info(this.playerEntity.username + " failed to wood!");
					this.sendPacket(new Packet3Chat("\u00a7cYou can\'t /wood again so soon!"));
				} else {
					MinecraftServer.playerList.put(this.playerEntity.username, 6000);
					logger.info(this.playerEntity.username + " wooded!");

					for(i3 = 0; i3 < 4; ++i3) {
						this.playerEntity.dropPlayerItem(new ItemStack(Block.sapling, 1));
					}
				}
			} else {
				String string4;
				if(this.mcServer.configManager.isOp(this.playerEntity.username)) {
					string4 = string1.substring(1);
					logger.info(this.playerEntity.username + " issued server command: " + string4);
					this.mcServer.addCommand(string4, this);
				} else {
					string4 = string1.substring(1);
					logger.info(this.playerEntity.username + " tried command: " + string4);
				}
			}
		}

	}

	public void handleArmAnimation(Packet18ArmAnimation packet18ArmAnimation1) {
		if(packet18ArmAnimation1.animate == 1) {
			this.playerEntity.swingItem();
		}

	}

	public void handleKickDisconnect(Packet255KickDisconnect packet255KickDisconnect1) {
		this.netManager.networkShutdown("Quitting");
	}

	public int getNumChunkDataPackets() {
		return this.netManager.getNumChunkDataPackets();
	}

	public void addHelpCommandMessage(String string1) {
		this.sendPacket(new Packet3Chat("\u00a77" + string1));
	}

	public String getUsername() {
		return this.playerEntity.username;
	}

	public void handlePlayerInventory(Packet5PlayerInventory packet) {
		if(packet.inventoryType == -1) {
			this.playerEntity.inventory.mainInventory = packet.inventory;
		}

		if(packet.inventoryType == -2) {
			this.playerEntity.inventory.craftingInventory = packet.inventory;
		}

		if(packet.inventoryType == -3) {
			this.playerEntity.inventory.armorInventory = packet.inventory;
		}

	}

	public void sendInventoryPackets() {
		this.netManager.addToSendQueue(new Packet5PlayerInventory(-1, this.playerEntity.inventory.mainInventory));
		this.netManager.addToSendQueue(new Packet5PlayerInventory(-2, this.playerEntity.inventory.craftingInventory));
		this.netManager.addToSendQueue(new Packet5PlayerInventory(-3, this.playerEntity.inventory.armorInventory));
	}

	public void handleComplexEntity(Packet59ComplexEntity packet59ComplexEntity1) {
		TileEntity tileEntity2 = this.mcServer.worldMngr.getBlockTileEntity(packet59ComplexEntity1.xCoord, packet59ComplexEntity1.yCoord, packet59ComplexEntity1.zCoord);
		if(tileEntity2 != null) {
			tileEntity2.readFromNBT(packet59ComplexEntity1.tileEntityNBT);
			tileEntity2.onInventoryChanged();
		}

	}
}
