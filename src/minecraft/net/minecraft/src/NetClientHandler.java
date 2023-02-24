package net.minecraft.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Random;

import net.minecraft.client.Minecraft;

public class NetClientHandler extends NetHandler {
	private boolean disconnected = false;
	private NetworkManager netManager;
	public String loginProgress;
	private Minecraft mc;
	private WorldClient worldClient;
	private boolean posUpdated = false;
	Random rand = new Random();

	public NetClientHandler(Minecraft minecraft, String ip, int port) throws IOException {
		this.mc = minecraft;
		Socket socket4 = new Socket(InetAddress.getByName(ip), port);
		this.netManager = new NetworkManager(socket4, "Client", this);
	}

	public void processReadPackets() {
		if(!this.disconnected) {
			this.netManager.processReadPackets();
		}
	}

	public void handleLogin(Packet1Login packet) {
		this.mc.playerController = new PlayerControllerMP(this.mc, this);
		this.worldClient = new WorldClient(this);
		this.worldClient.multiplayerWorld = true;
		this.mc.changeWorld1(this.worldClient);
		this.mc.displayGuiScreen(new GuiDownloadTerrain(this));
	}

	public void handlePickupSpawn(Packet21PickupSpawn packet) {
		double d2 = (double)packet.xPosition / 32.0D;
		double d4 = (double)packet.yPosition / 32.0D;
		double d6 = (double)packet.zPosition / 32.0D;
		EntityItem entityItem8 = new EntityItem(this.worldClient, d2, d4, d6, new ItemStack(packet.itemID, packet.count));
		entityItem8.motionX = (double)packet.rotation / 128.0D;
		entityItem8.motionY = (double)packet.pitch / 128.0D;
		entityItem8.motionZ = (double)packet.roll / 128.0D;
		entityItem8.serverPosX = packet.xPosition;
		entityItem8.serverPosY = packet.yPosition;
		entityItem8.serverPosZ = packet.zPosition;
		this.worldClient.addEntityToWorld(packet.entityId, entityItem8);
	}

	public void handleVehicleSpawn(Packet23VehicleSpawn packet) {
		double d2 = (double)packet.xPosition / 32.0D;
		double d4 = (double)packet.yPosition / 32.0D;
		double d6 = (double)packet.zPosition / 32.0D;
		Object object8 = null;
		if(packet.type == 10) {
			object8 = new EntityMinecart(this.worldClient, d2, d4, d6, 0);
		}

		if(packet.type == 11) {
			object8 = new EntityMinecart(this.worldClient, d2, d4, d6, 1);
		}

		if(packet.type == 12) {
			object8 = new EntityMinecart(this.worldClient, d2, d4, d6, 2);
		}

		if(packet.type == 1) {
			object8 = new EntityBoat(this.worldClient, d2, d4, d6);
		}

		if(object8 != null) {
			((Entity)object8).serverPosX = packet.xPosition;
			((Entity)object8).serverPosY = packet.yPosition;
			((Entity)object8).serverPosZ = packet.zPosition;
			((Entity)object8).rotationYaw = 0.0F;
			((Entity)object8).rotationPitch = 0.0F;
			((Entity)object8).entityID = packet.entityId;
			this.worldClient.addEntityToWorld(packet.entityId, (Entity)object8);
		}

	}

	public void handleNamedEntitySpawn(Packet20NamedEntitySpawn packet) {
		double d2 = (double)packet.xPosition / 32.0D;
		double d4 = (double)packet.yPosition / 32.0D;
		double d6 = (double)packet.zPosition / 32.0D;
		float f8 = (float)(packet.rotation * 360) / 256.0F;
		float f9 = (float)(packet.pitch * 360) / 256.0F;
		EntityOtherPlayerMP entityOtherPlayerMP10 = new EntityOtherPlayerMP(this.mc.theWorld, packet.name);
		entityOtherPlayerMP10.serverPosX = packet.xPosition;
		entityOtherPlayerMP10.serverPosY = packet.yPosition;
		entityOtherPlayerMP10.serverPosZ = packet.zPosition;
		int i11 = packet.currentItem;
		if(i11 == 0) {
			entityOtherPlayerMP10.inventory.mainInventory[entityOtherPlayerMP10.inventory.currentItem] = null;
		} else {
			entityOtherPlayerMP10.inventory.mainInventory[entityOtherPlayerMP10.inventory.currentItem] = new ItemStack(i11);
		}

		entityOtherPlayerMP10.setPositionAndRotation(d2, d4, d6, f8, f9);
		this.worldClient.addEntityToWorld(packet.entityId, entityOtherPlayerMP10);
	}

	public void handleEntityTeleport(Packet34EntityTeleport packet) {
		Entity entity2 = this.worldClient.getEntityByID(packet.entityId);
		if(entity2 != null) {
			entity2.serverPosX = packet.xPosition;
			entity2.serverPosY = packet.yPosition;
			entity2.serverPosZ = packet.zPosition;
			double d3 = (double)entity2.serverPosX / 32.0D;
			double d5 = (double)entity2.serverPosY / 32.0D;
			double d7 = (double)entity2.serverPosZ / 32.0D;
			float f9 = (float)(packet.yaw * 360) / 256.0F;
			float f10 = (float)(packet.pitch * 360) / 256.0F;
			entity2.setPositionAndRotation(d3, d5, d7, f9, f10, 3);
		}
	}

	public void handleEntity(Packet30Entity packet30Entity1) {
		Entity entity2 = this.worldClient.getEntityByID(packet30Entity1.entityId);
		if(entity2 != null) {
			entity2.serverPosX += packet30Entity1.xPosition;
			entity2.serverPosY += packet30Entity1.yPosition;
			entity2.serverPosZ += packet30Entity1.zPosition;
			double d3 = (double)entity2.serverPosX / 32.0D;
			double d5 = (double)entity2.serverPosY / 32.0D;
			double d7 = (double)entity2.serverPosZ / 32.0D;
			float f9 = packet30Entity1.rotating ? (float)(packet30Entity1.yaw * 360) / 256.0F : entity2.rotationYaw;
			float f10 = packet30Entity1.rotating ? (float)(packet30Entity1.pitch * 360) / 256.0F : entity2.rotationPitch;
			entity2.setPositionAndRotation(d3, d5, d7, f9, f10, 3);
		}
	}

	public void handleDestroyEntity(Packet29DestroyEntity packet29DestroyEntity1) {
		this.worldClient.removeEntityFromWorld(packet29DestroyEntity1.entityId);
	}

	public void handleFlying(Packet10Flying packet) {
		EntityPlayerSP entityPlayerSP2 = this.mc.thePlayer;
		double d3 = entityPlayerSP2.posX;
		double d5 = entityPlayerSP2.posY;
		double d7 = entityPlayerSP2.posZ;
		float f9 = entityPlayerSP2.rotationYaw;
		float f10 = entityPlayerSP2.rotationPitch;
		if(packet.moving) {
			d3 = packet.xPosition;
			d5 = packet.yPosition;
			d7 = packet.zPosition;
		}

		if(packet.rotating) {
			f9 = packet.yaw;
			f10 = packet.pitch;
		}

		entityPlayerSP2.ySize = 0.0F;
		entityPlayerSP2.motionX = entityPlayerSP2.motionY = entityPlayerSP2.motionZ = 0.0D;
		entityPlayerSP2.setPositionAndRotation(d3, d5, d7, f9, f10);
		packet.xPosition = entityPlayerSP2.posX;
		packet.yPosition = entityPlayerSP2.boundingBox.minY;
		packet.zPosition = entityPlayerSP2.posZ;
		packet.stance = entityPlayerSP2.posY;
		this.netManager.addToSendQueue(packet);
		if(!this.posUpdated) {
			this.mc.thePlayer.prevPosX = this.mc.thePlayer.posX;
			this.mc.thePlayer.prevPosY = this.mc.thePlayer.posY;
			this.mc.thePlayer.prevPosZ = this.mc.thePlayer.posZ;
			this.posUpdated = true;
			this.mc.displayGuiScreen((GuiScreen)null);
		}

	}

	public void handlePreChunk(Packet50PreChunk packet) {
		this.worldClient.doPreChunk(packet.xPosition, packet.yPosition, packet.mode);
	}

	public void handleMultiBlockChange(Packet52MultiBlockChange packet52MultiBlockChange1) {
		Chunk chunk2 = this.worldClient.getChunkFromChunkCoords(packet52MultiBlockChange1.xPosition, packet52MultiBlockChange1.zPosition);
		int i3 = packet52MultiBlockChange1.xPosition * 16;
		int i4 = packet52MultiBlockChange1.zPosition * 16;

		for(int i5 = 0; i5 < packet52MultiBlockChange1.size; ++i5) {
			short s6 = packet52MultiBlockChange1.coordinateArray[i5];
			int i7 = packet52MultiBlockChange1.typeArray[i5] & 255;
			byte b8 = packet52MultiBlockChange1.metadataArray[i5];
			int i9 = s6 >> 12 & 15;
			int i10 = s6 >> 8 & 15;
			int i11 = s6 & 255;
			chunk2.setBlockIDWithMetadata(i9, i11, i10, i7, b8);
			this.worldClient.invalidateBlockReceiveRegion(i9 + i3, i11, i10 + i4, i9 + i3, i11, i10 + i4);
			this.worldClient.markBlocksDirty(i9 + i3, i11, i10 + i4, i9 + i3, i11, i10 + i4);
		}

	}

	public void handleMapChunk(Packet51MapChunk packet) {
		this.worldClient.invalidateBlockReceiveRegion(packet.xPosition, packet.yPosition, packet.zPosition, packet.xPosition + packet.xSize - 1, packet.yPosition + packet.ySize - 1, packet.zPosition + packet.zSize - 1);
		this.worldClient.setChunkData(packet.xPosition, packet.yPosition, packet.zPosition, packet.xSize, packet.ySize, packet.zSize, packet.chunkData);
	}

	public void handleBlockChange(Packet53BlockChange packet) {
		this.worldClient.handleBlockChange(packet.xPosition, packet.yPosition, packet.zPosition, packet.type, packet.metadata);
	}

	public void handleKickDisconnect(Packet255KickDisconnect packet) {
		this.netManager.networkShutdown("Got kicked");
		this.disconnected = true;
		this.mc.changeWorld1((World)null);
		this.mc.displayGuiScreen(new GuiConnectFailed("Disconnected by server", packet.reason));
	}

	public void handleErrorMessage(String message) {
		if(!this.disconnected) {
			this.disconnected = true;
			this.mc.changeWorld1((World)null);
			this.mc.displayGuiScreen(new GuiConnectFailed("Connection lost", message));
		}
	}

	public void addToSendQueue(Packet packet) {
		if(!this.disconnected) {
			this.netManager.addToSendQueue(packet);
		}
	}

	public void handleCollect(Packet22Collect packet) {
		EntityItem entityItem2 = (EntityItem)this.worldClient.getEntityByID(packet.collectedEntityId);
		Object object3 = (EntityLiving)this.worldClient.getEntityByID(packet.collectorEntityId);
		if(object3 == null) {
			object3 = this.mc.thePlayer;
		}

		if(entityItem2 != null) {
			this.worldClient.playSoundAtEntity(entityItem2, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			this.mc.effectRenderer.addEffect(new EntityPickupFX(this.mc.theWorld, entityItem2, (Entity)object3, -0.5F));
			this.worldClient.removeEntityFromWorld(packet.collectedEntityId);
		}

	}

	public void handleBlockItemSwitch(Packet16BlockItemSwitch packet) {
		Entity entity2 = this.worldClient.getEntityByID(packet.entityId);
		if(entity2 != null) {
			EntityPlayer entityPlayer3 = (EntityPlayer)entity2;
			int i4 = packet.id;
			if(i4 == 0) {
				entityPlayer3.inventory.mainInventory[entityPlayer3.inventory.currentItem] = null;
			} else {
				entityPlayer3.inventory.mainInventory[entityPlayer3.inventory.currentItem] = new ItemStack(i4);
			}

		}
	}

	public void handleChat(Packet3Chat packet) {
		this.mc.ingameGUI.addChatMessage(packet.message);
	}

	public void handleArmAnimation(Packet18ArmAnimation packet18ArmAnimation1) {
		Entity entity2 = this.worldClient.getEntityByID(packet18ArmAnimation1.entityId);
		if(entity2 != null) {
			EntityPlayer entityPlayer3 = (EntityPlayer)entity2;
			entityPlayer3.swingItem();
		}
	}

	public void handleAddToInventory(Packet17AddToInventory packet) {
		this.mc.thePlayer.inventory.addItemStackToInventory(new ItemStack(packet.itemID, packet.count, packet.itemDamage));
	}

	public void handleHandshake(Packet2Handshake packet) {
		if(packet.username.equals("-")) {
			this.addToSendQueue(new Packet1Login(this.mc.session.username, "Password", 2));
		} else {
			try {
				URL uRL2 = new URL("http://www.minecraft.net/game/joinserver.jsp?user=" + this.mc.session.username + "&sessionId=" + this.mc.session.sessionId + "&serverId=" + packet.username);
				BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(uRL2.openStream()));
				String string4 = bufferedReader3.readLine();
				bufferedReader3.close();
				if(string4.equalsIgnoreCase("ok")) {
					this.addToSendQueue(new Packet1Login(this.mc.session.username, "Password", 2));
				} else {
					this.netManager.networkShutdown("Failed to login: " + string4);
				}
			} catch (Exception exception5) {
				exception5.printStackTrace();
				this.netManager.networkShutdown("Internal client error: " + exception5.toString());
			}
		}

	}

	public void disconnect() {
		this.disconnected = true;
		this.netManager.networkShutdown("Closed");
	}

	public void handleMobSpawn(Packet24MobSpawn packet) {
		double d2 = (double)packet.xPosition / 32.0D;
		double d4 = (double)packet.yPosition / 32.0D;
		double d6 = (double)packet.zPosition / 32.0D;
		float f8 = (float)(packet.yaw * 360) / 256.0F;
		float f9 = (float)(packet.pitch * 360) / 256.0F;
		EntityLiving entityLiving10 = (EntityLiving)EntityList.createEntityByID(packet.type, this.mc.theWorld);
		entityLiving10.serverPosX = packet.xPosition;
		entityLiving10.serverPosY = packet.yPosition;
		entityLiving10.serverPosZ = packet.zPosition;
		entityLiving10.setPositionAndRotation(d2, d4, d6, f8, f9);
		entityLiving10.isAIEnabled = true;
		this.worldClient.addEntityToWorld(packet.entityId, entityLiving10);
	}

	public void handleUpdateTime(Packet4UpdateTime packet) {
		this.mc.theWorld.setWorldTime(packet.time);
	}

	public void handlePlayerInventory(Packet5PlayerInventory packet) {
		EntityPlayerSP entityPlayerSP2 = this.mc.thePlayer;
		if(packet.inventoryType == -1) {
			entityPlayerSP2.inventory.mainInventory = packet.inventory;
		}

		if(packet.inventoryType == -2) {
			entityPlayerSP2.inventory.craftingInventory = packet.inventory;
		}

		if(packet.inventoryType == -3) {
			entityPlayerSP2.inventory.armorInventory = packet.inventory;
		}

	}

	public void handleComplexEntity(Packet59ComplexEntity packet) {
		TileEntity tileEntity2 = this.worldClient.getBlockTileEntity(packet.xCoord, packet.yCoord, packet.zCoord);
		if(tileEntity2 != null) {
			tileEntity2.readFromNBT(packet.tileEntityNBT);
			this.worldClient.markBlocksDirty(packet.xCoord, packet.yCoord, packet.zCoord, packet.xCoord, packet.yCoord, packet.zCoord);
		}

	}

	public void handleSpawnPosition(Packet6SpawnPosition packet) {
		this.worldClient.spawnX = packet.xPosition;
		this.worldClient.spawnY = packet.yPosition;
		this.worldClient.spawnZ = packet.zPosition;
	}
}
