package net.minecraft.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import net.minecraft.server.MinecraftServer;

public class ServerConfigurationManager {
	public static Logger logger = Logger.getLogger("Minecraft");
	public List playerEntities = new ArrayList();
	private MinecraftServer mcServer;
	private PlayerManager playerManagerObj;
	private int maxPlayers;
	private Set bannedPlayers = new HashSet();
	private Set bannedIPs = new HashSet();
	private Set ops = new HashSet();
	private File bannedPlayersFile;
	private File ipBanFile;
	private File opFile;
	private PlayerNBTManager playerNBTManagerObj;

	public ServerConfigurationManager(MinecraftServer minecraftServer) {
		this.mcServer = minecraftServer;
		this.bannedPlayersFile = minecraftServer.getFile("banned-players.txt");
		this.ipBanFile = minecraftServer.getFile("banned-ips.txt");
		this.opFile = minecraftServer.getFile("ops.txt");
		this.playerManagerObj = new PlayerManager(minecraftServer);
		this.maxPlayers = minecraftServer.propertyManagerObj.getIntProperty("max-players", 20);
		this.readBannedPlayers();
		this.loadBannedList();
		this.loadOps();
		this.writeBannedPlayers();
		this.saveBannedList();
		this.saveOps();
	}

	public void setPlayerManager(WorldServer worldServer1) {
		this.playerNBTManagerObj = new PlayerNBTManager(new File(worldServer1.saveDirectory, "players"));
	}

	public int getMaxTrackingDistance() {
		return this.playerManagerObj.getMaxTrackingDistance();
	}

	public void playerLoggedIn(EntityPlayerMP entityPlayerMP1) {
		this.playerEntities.add(entityPlayerMP1);
		this.playerNBTManagerObj.readPlayerNBT(entityPlayerMP1);
		this.mcServer.worldMngr.chunkProviderServer.loadChunk((int)entityPlayerMP1.posX >> 4, (int)entityPlayerMP1.posZ >> 4);

		while(this.mcServer.worldMngr.getCollidingBoundingBoxes(entityPlayerMP1, entityPlayerMP1.boundingBox).size() != 0) {
			entityPlayerMP1.setPosition(entityPlayerMP1.posX, entityPlayerMP1.posY + 1.0D, entityPlayerMP1.posZ);
		}

		this.mcServer.worldMngr.spawnEntityInWorld(entityPlayerMP1);
		this.playerManagerObj.addPlayer(entityPlayerMP1);
	}

	public void serverUpdateMountedMovingPlayer(EntityPlayerMP entityPlayerMP1) {
		this.playerManagerObj.updateMountedMovingPlayer(entityPlayerMP1);
	}

	public void playerLoggedOut(EntityPlayerMP entityPlayerMP1) {
		this.playerManagerObj.removePlayer(entityPlayerMP1);
		this.playerNBTManagerObj.writePlayerNBT(entityPlayerMP1);
		this.mcServer.worldMngr.setEntityDead(entityPlayerMP1);
		this.playerEntities.remove(entityPlayerMP1);
	}

	public EntityPlayerMP login(NetLoginHandler netLoginHandler1, String string2, String string3) {
		if(this.bannedPlayers.contains(string2.trim().toLowerCase())) {
			netLoginHandler1.kickUser("You are banned from this server!");
			return null;
		} else {
			String string4 = netLoginHandler1.netManager.getRemoteAddress().toString();
			string4 = string4.substring(string4.indexOf("/") + 1);
			string4 = string4.substring(0, string4.indexOf(":"));
			if(this.bannedIPs.contains(string4)) {
				netLoginHandler1.kickUser("Your IP address is banned from this server!");
				return null;
			} else if(this.playerEntities.size() >= this.maxPlayers) {
				netLoginHandler1.kickUser("The server is full!");
				return null;
			} else {
				for(int i5 = 0; i5 < this.playerEntities.size(); ++i5) {
					EntityPlayerMP entityPlayerMP6 = (EntityPlayerMP)this.playerEntities.get(i5);
					if(entityPlayerMP6.username.equalsIgnoreCase(string2)) {
						entityPlayerMP6.playerNetServerHandler.kickPlayer("You logged in from another location");
					}
				}

				return new EntityPlayerMP(this.mcServer, this.mcServer.worldMngr, string2, new ItemInWorldManager(this.mcServer.worldMngr));
			}
		}
	}

	public void onTick() throws IOException {
		this.playerManagerObj.updatePlayerInstances();
	}

	public void markBlockNeedsUpdate(int i1, int i2, int i3) {
		this.playerManagerObj.markBlockNeedsUpdate(i1, i2, i3);
	}

	public void sendPacketToAllPlayers(Packet packet1) {
		for(int i2 = 0; i2 < this.playerEntities.size(); ++i2) {
			EntityPlayerMP entityPlayerMP3 = (EntityPlayerMP)this.playerEntities.get(i2);
			entityPlayerMP3.playerNetServerHandler.sendPacket(packet1);
		}

	}

	public String getPlayerList() {
		String string1 = "";

		for(int i2 = 0; i2 < this.playerEntities.size(); ++i2) {
			if(i2 > 0) {
				string1 = string1 + ", ";
			}

			string1 = string1 + ((EntityPlayerMP)this.playerEntities.get(i2)).username;
		}

		return string1;
	}

	public void banPlayer(String string1) {
		this.bannedPlayers.add(string1.toLowerCase());
		this.writeBannedPlayers();
	}

	public void pardonPlayer(String string1) {
		this.bannedPlayers.remove(string1.toLowerCase());
		this.writeBannedPlayers();
	}

	private void readBannedPlayers() {
		try {
			this.bannedPlayers.clear();
			BufferedReader bufferedReader1 = new BufferedReader(new FileReader(this.bannedPlayersFile));
			String string2 = "";

			while((string2 = bufferedReader1.readLine()) != null) {
				this.bannedPlayers.add(string2.trim().toLowerCase());
			}

			bufferedReader1.close();
		} catch (Exception exception3) {
			logger.warning("Failed to load ban list: " + exception3);
		}

	}

	private void writeBannedPlayers() {
		try {
			PrintWriter printWriter1 = new PrintWriter(new FileWriter(this.bannedPlayersFile, false));
			Iterator iterator2 = this.bannedPlayers.iterator();

			while(iterator2.hasNext()) {
				String string3 = (String)iterator2.next();
				printWriter1.println(string3);
			}

			printWriter1.close();
		} catch (Exception exception4) {
			logger.warning("Failed to save ban list: " + exception4);
		}

	}

	public void banIP(String string1) {
		this.bannedIPs.add(string1.toLowerCase());
		this.saveBannedList();
	}

	public void pardonIP(String string1) {
		this.bannedIPs.remove(string1.toLowerCase());
		this.saveBannedList();
	}

	private void loadBannedList() {
		try {
			this.bannedIPs.clear();
			BufferedReader bufferedReader1 = new BufferedReader(new FileReader(this.ipBanFile));
			String string2 = "";

			while((string2 = bufferedReader1.readLine()) != null) {
				this.bannedIPs.add(string2.trim().toLowerCase());
			}

			bufferedReader1.close();
		} catch (Exception exception3) {
			logger.warning("Failed to load ip ban list: " + exception3);
		}

	}

	private void saveBannedList() {
		try {
			PrintWriter printWriter1 = new PrintWriter(new FileWriter(this.ipBanFile, false));
			Iterator iterator2 = this.bannedIPs.iterator();

			while(iterator2.hasNext()) {
				String string3 = (String)iterator2.next();
				printWriter1.println(string3);
			}

			printWriter1.close();
		} catch (Exception exception4) {
			logger.warning("Failed to save ip ban list: " + exception4);
		}

	}

	public void opPlayer(String string1) {
		this.ops.add(string1.toLowerCase());
		this.saveOps();
	}

	public void deopPlayer(String string1) {
		this.ops.remove(string1.toLowerCase());
		this.saveOps();
	}

	private void loadOps() {
		try {
			this.ops.clear();
			BufferedReader bufferedReader1 = new BufferedReader(new FileReader(this.opFile));
			String string2 = "";

			while((string2 = bufferedReader1.readLine()) != null) {
				this.ops.add(string2.trim().toLowerCase());
			}

			bufferedReader1.close();
		} catch (Exception exception3) {
			logger.warning("Failed to load ip ban list: " + exception3);
		}

	}

	private void saveOps() {
		try {
			PrintWriter printWriter1 = new PrintWriter(new FileWriter(this.opFile, false));
			Iterator iterator2 = this.ops.iterator();

			while(iterator2.hasNext()) {
				String string3 = (String)iterator2.next();
				printWriter1.println(string3);
			}

			printWriter1.close();
		} catch (Exception exception4) {
			logger.warning("Failed to save ip ban list: " + exception4);
		}

	}

	public boolean isOp(String string1) {
		return this.ops.contains(string1.trim().toLowerCase());
	}

	public EntityPlayerMP getPlayerEntity(String string1) {
		for(int i2 = 0; i2 < this.playerEntities.size(); ++i2) {
			EntityPlayerMP entityPlayerMP3 = (EntityPlayerMP)this.playerEntities.get(i2);
			if(entityPlayerMP3.username.equalsIgnoreCase(string1)) {
				return entityPlayerMP3;
			}
		}

		return null;
	}

	public void sendChatMessageToPlayer(String string1, String string2) {
		EntityPlayerMP entityPlayerMP3 = this.getPlayerEntity(string1);
		if(entityPlayerMP3 != null) {
			entityPlayerMP3.playerNetServerHandler.sendPacket(new Packet3Chat(string2));
		}

	}

	public void sendChatMessageToAllOps(String string1) {
		Packet3Chat packet3Chat2 = new Packet3Chat(string1);

		for(int i3 = 0; i3 < this.playerEntities.size(); ++i3) {
			EntityPlayerMP entityPlayerMP4 = (EntityPlayerMP)this.playerEntities.get(i3);
			if(this.isOp(entityPlayerMP4.username)) {
				entityPlayerMP4.playerNetServerHandler.sendPacket(packet3Chat2);
			}
		}

	}

	public boolean sendPacketToPlayer(String string1, Packet packet2) {
		EntityPlayerMP entityPlayerMP3 = this.getPlayerEntity(string1);
		if(entityPlayerMP3 != null) {
			entityPlayerMP3.playerNetServerHandler.sendPacket(packet2);
			return true;
		} else {
			return false;
		}
	}

	public void sentTileEntityToPlayer(int i1, int i2, int i3, TileEntity tileEntity4) {
		this.playerManagerObj.sendTileEntity(new Packet59ComplexEntity(i1, i2, i3, tileEntity4), i1, i2, i3);
	}

	public void savePlayerStates() {
		for(int i1 = 0; i1 < this.playerEntities.size(); ++i1) {
			this.playerNBTManagerObj.writePlayerNBT((EntityPlayerMP)this.playerEntities.get(i1));
		}

	}
}
