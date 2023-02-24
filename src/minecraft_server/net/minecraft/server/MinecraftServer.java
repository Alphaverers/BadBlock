package net.minecraft.server;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.ConsoleLogManager;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.EntityTracker;
import net.minecraft.src.ICommandListener;
import net.minecraft.src.IProgressUpdate;
import net.minecraft.src.IUpdatePlayerListBox;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NetworkListenThread;
import net.minecraft.src.Packet3Chat;
import net.minecraft.src.Packet4UpdateTime;
import net.minecraft.src.PropertyManager;
import net.minecraft.src.ServerCommand;
import net.minecraft.src.ServerConfigurationManager;
import net.minecraft.src.ServerGUI;
import net.minecraft.src.ThreadCommandReader;
import net.minecraft.src.ThreadServerApplication;
import net.minecraft.src.ThreadSleepForever;
import net.minecraft.src.Vec3D;
import net.minecraft.src.WorldManager;
import net.minecraft.src.WorldServer;

public class MinecraftServer implements ICommandListener, Runnable {
	public static Logger logger = Logger.getLogger("Minecraft");
	public static HashMap playerList = new HashMap();
	public NetworkListenThread networkServer;
	public PropertyManager propertyManagerObj;
	public WorldServer worldMngr;
	public ServerConfigurationManager configManager;
	private boolean serverRunning = true;
	public boolean serverStopped = false;
	int deathTime = 0;
	public String currentTask;
	public int percentDone;
	private List playersOnline = new ArrayList();
	private List commands = Collections.synchronizedList(new ArrayList());
	public EntityTracker entityTracker;
	public boolean onlineMode;

	public MinecraftServer() {
		new ThreadSleepForever(this);
	}

	private boolean startServer() throws IOException {
		ThreadCommandReader threadCommandReader1 = new ThreadCommandReader(this);
		threadCommandReader1.setDaemon(true);
		threadCommandReader1.start();
		ConsoleLogManager.init();
		logger.info("Starting minecraft server version 0.2.0_01");
		if(Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
			logger.warning("**** NOT ENOUGH RAM!");
			logger.warning("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
		}

		logger.info("Loading properties");
		this.propertyManagerObj = new PropertyManager(new File("server.properties"));
		String string2 = this.propertyManagerObj.getStringProperty("server-ip", "");
		this.onlineMode = this.propertyManagerObj.getBooleanProperty("online-mode", true);
		InetAddress inetAddress3 = null;
		if(string2.length() > 0) {
			inetAddress3 = InetAddress.getByName(string2);
		}

		int i4 = this.propertyManagerObj.getIntProperty("server-port", 25565);
		logger.info("Starting Minecraft server on " + (string2.length() == 0 ? "*" : string2) + ":" + i4);

		try {
			this.networkServer = new NetworkListenThread(this, inetAddress3, i4);
		} catch (IOException iOException6) {
			logger.warning("**** FAILED TO BIND TO PORT!");
			logger.log(Level.WARNING, "The exception was: " + iOException6.toString());
			logger.warning("Perhaps a server is already running on that port?");
			return false;
		}

		if(!this.onlineMode) {
			logger.warning("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
			logger.warning("The server will make no attempt to authenticate usernames. Beware.");
			logger.warning("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
			logger.warning("To change this, set \"online-mode\" to \"true\" in the server.settings file.");
		}

		this.configManager = new ServerConfigurationManager(this);
		this.entityTracker = new EntityTracker(this);
		String string5 = this.propertyManagerObj.getStringProperty("level-name", "world");
		logger.info("Preparing level \"" + string5 + "\"");
		this.initWorld(string5);
		logger.info("Done! For help, type \"help\" or \"?\"");
		return true;
	}

	private void initWorld(String worldName) {
		logger.info("Preparing start region");
		this.worldMngr = new WorldServer(new File("."), worldName, this.propertyManagerObj.getBooleanProperty("monsters", false));
		this.worldMngr.addWorldAccess(new WorldManager(this));
		this.worldMngr.difficultySetting = 1;
		this.configManager.setPlayerManager(this.worldMngr);
		byte b2 = 10;

		for(int i3 = -b2; i3 <= b2; ++i3) {
			this.outputPercentRemaining("Preparing spawn area", (i3 + b2) * 100 / (b2 + b2 + 1));

			for(int i4 = -b2; i4 <= b2; ++i4) {
				if(!this.serverRunning) {
					return;
				}

				this.worldMngr.chunkProviderServer.loadChunk((this.worldMngr.spawnX >> 4) + i3, (this.worldMngr.spawnZ >> 4) + i4);
			}
		}

		this.clearCurrentTask();
	}

	private void outputPercentRemaining(String currentTask, int percent) {
		this.currentTask = currentTask;
		this.percentDone = percent;
		System.out.println(currentTask + ": " + percent + "%");
	}

	private void clearCurrentTask() {
		this.currentTask = null;
		this.percentDone = 0;
	}

	private void save() {
		logger.info("Saving chunks");
		this.worldMngr.saveWorld(true, (IProgressUpdate)null);
	}

	private void stop() {
		logger.info("Stopping server");
		if(this.configManager != null) {
			this.configManager.savePlayerStates();
		}

		if(this.worldMngr != null) {
			this.save();
		}

	}

	public void stopRunning() {
		this.serverRunning = false;
	}

	public void run() {
		try {
			if(this.startServer()) {
				long j1 = System.currentTimeMillis();
				long j3 = 0L;

				while(this.serverRunning) {
					long j5 = System.currentTimeMillis();
					long j7 = j5 - j1;
					if(j7 > 2000L) {
						logger.warning("Can\'t keep up! Did the system time change, or is the server overloaded?");
						j7 = 2000L;
					}

					if(j7 < 0L) {
						logger.warning("Time ran backwards! Did the system time change?");
						j7 = 0L;
					}

					j3 += j7;
					j1 = j5;

					while(j3 > 50L) {
						j3 -= 50L;
						this.doTick();
					}

					Thread.sleep(1L);
				}
			} else {
				while(this.serverRunning) {
					this.commandLineParser();

					try {
						Thread.sleep(10L);
					} catch (InterruptedException interruptedException15) {
						interruptedException15.printStackTrace();
					}
				}
			}
		} catch (Exception exception16) {
			exception16.printStackTrace();
			logger.log(Level.SEVERE, "Unexpected exception", exception16);

			while(this.serverRunning) {
				this.commandLineParser();

				try {
					Thread.sleep(10L);
				} catch (InterruptedException interruptedException14) {
					interruptedException14.printStackTrace();
				}
			}
		} finally {
			this.stop();
			this.serverStopped = true;
			System.exit(0);
		}

	}

	private void doTick() throws IOException {
		ArrayList arrayList1 = new ArrayList();
		Iterator iterator2 = playerList.keySet().iterator();

		while(iterator2.hasNext()) {
			String string3 = (String)iterator2.next();
			int i4 = ((Integer)playerList.get(string3)).intValue();
			if(i4 > 0) {
				playerList.put(string3, i4 - 1);
			} else {
				arrayList1.add(string3);
			}
		}

		int i6;
		for(i6 = 0; i6 < arrayList1.size(); ++i6) {
			playerList.remove(arrayList1.get(i6));
		}

		AxisAlignedBB.clearBoundingBoxPool();
		Vec3D.initialize();
		++this.deathTime;
		if(this.deathTime % 20 == 0) {
			this.configManager.sendPacketToAllPlayers(new Packet4UpdateTime(this.worldMngr.worldTime));
		}

		this.worldMngr.tick();

		while(this.worldMngr.updatingLighting()) {
		}

		this.worldMngr.updateEntities();
		this.networkServer.handleNetworkListenThread();
		this.configManager.onTick();
		this.entityTracker.updateTrackedEntities();

		for(i6 = 0; i6 < this.playersOnline.size(); ++i6) {
			((IUpdatePlayerListBox)this.playersOnline.get(i6)).addAllPlayers();
		}

		try {
			this.commandLineParser();
		} catch (Exception exception5) {
			logger.log(Level.WARNING, "Unexpected exception while parsing console command", exception5);
		}

	}

	public void addCommand(String command, ICommandListener commandListener) {
		this.commands.add(new ServerCommand(command, commandListener));
	}

	public void commandLineParser() {
		while(this.commands.size() > 0) {
			ServerCommand serverCommand1 = (ServerCommand)this.commands.remove(0);
			String string2 = serverCommand1.command;
			ICommandListener iCommandListener3 = serverCommand1.commandListener;
			String string4 = iCommandListener3.getUsername();
			if(!string2.toLowerCase().startsWith("help") && !string2.toLowerCase().startsWith("?")) {
				if(string2.toLowerCase().startsWith("list")) {
					iCommandListener3.addHelpCommandMessage("Connected players: " + this.configManager.getPlayerList());
				} else if(string2.toLowerCase().startsWith("stop")) {
					this.print(string4, "Stopping the server..");
					this.serverRunning = false;
				} else if(string2.toLowerCase().startsWith("save-all")) {
					this.print(string4, "Forcing save..");
					this.worldMngr.saveWorld(true, (IProgressUpdate)null);
					this.print(string4, "Save complete.");
				} else if(string2.toLowerCase().startsWith("save-off")) {
					this.print(string4, "Disabling level saving..");
					this.worldMngr.levelSaving = true;
				} else if(string2.toLowerCase().startsWith("save-on")) {
					this.print(string4, "Enabling level saving..");
					this.worldMngr.levelSaving = false;
				} else {
					String string11;
					if(string2.toLowerCase().startsWith("op ")) {
						string11 = string2.substring(string2.indexOf(" ")).trim();
						this.configManager.opPlayer(string11);
						this.print(string4, "Opping " + string11);
						this.configManager.sendChatMessageToPlayer(string11, "\u00a7eYou are now op!");
					} else if(string2.toLowerCase().startsWith("deop ")) {
						string11 = string2.substring(string2.indexOf(" ")).trim();
						this.configManager.deopPlayer(string11);
						this.configManager.sendChatMessageToPlayer(string11, "\u00a7eYou are no longer op!");
						this.print(string4, "De-opping " + string11);
					} else if(string2.toLowerCase().startsWith("ban-ip ")) {
						string11 = string2.substring(string2.indexOf(" ")).trim();
						this.configManager.banIP(string11);
						this.print(string4, "Banning ip " + string11);
					} else if(string2.toLowerCase().startsWith("pardon-ip ")) {
						string11 = string2.substring(string2.indexOf(" ")).trim();
						this.configManager.pardonIP(string11);
						this.print(string4, "Pardoning ip " + string11);
					} else {
						EntityPlayerMP entityPlayerMP12;
						if(string2.toLowerCase().startsWith("ban ")) {
							string11 = string2.substring(string2.indexOf(" ")).trim();
							this.configManager.banPlayer(string11);
							this.print(string4, "Banning " + string11);
							entityPlayerMP12 = this.configManager.getPlayerEntity(string11);
							if(entityPlayerMP12 != null) {
								entityPlayerMP12.playerNetServerHandler.kickPlayer("Banned by admin");
							}
						} else if(string2.toLowerCase().startsWith("pardon ")) {
							string11 = string2.substring(string2.indexOf(" ")).trim();
							this.configManager.pardonPlayer(string11);
							this.print(string4, "Pardoning " + string11);
						} else if(string2.toLowerCase().startsWith("kick ")) {
							string11 = string2.substring(string2.indexOf(" ")).trim();
							entityPlayerMP12 = null;

							for(int i13 = 0; i13 < this.configManager.playerEntities.size(); ++i13) {
								EntityPlayerMP entityPlayerMP14 = (EntityPlayerMP)this.configManager.playerEntities.get(i13);
								if(entityPlayerMP14.username.equalsIgnoreCase(string11)) {
									entityPlayerMP12 = entityPlayerMP14;
								}
							}

							if(entityPlayerMP12 != null) {
								entityPlayerMP12.playerNetServerHandler.kickPlayer("Kicked by admin");
								this.print(string4, "Kicking " + entityPlayerMP12.username);
							} else {
								iCommandListener3.addHelpCommandMessage("Can\'t find user " + string11 + ". No kick.");
							}
						} else {
							String[] string5;
							EntityPlayerMP entityPlayerMP7;
							if(string2.toLowerCase().startsWith("tp ")) {
								string5 = string2.split(" ");
								if(string5.length == 3) {
									entityPlayerMP12 = this.configManager.getPlayerEntity(string5[1]);
									entityPlayerMP7 = this.configManager.getPlayerEntity(string5[2]);
									if(entityPlayerMP12 == null) {
										iCommandListener3.addHelpCommandMessage("Can\'t find user " + string5[1] + ". No tp.");
									} else if(entityPlayerMP7 == null) {
										iCommandListener3.addHelpCommandMessage("Can\'t find user " + string5[2] + ". No tp.");
									} else {
										entityPlayerMP12.playerNetServerHandler.teleportTo(entityPlayerMP7.posX, entityPlayerMP7.posY, entityPlayerMP7.posZ, entityPlayerMP7.rotationYaw, entityPlayerMP7.rotationPitch);
										this.print(string4, "Teleporting " + string5[1] + " to " + string5[2] + ".");
									}
								} else {
									iCommandListener3.addHelpCommandMessage("Syntax error, please provice a source and a target.");
								}
							} else if(string2.toLowerCase().startsWith("give ")) {
								string5 = string2.split(" ");
								if(string5.length != 3 && string5.length != 4) {
									return;
								}

								String string6 = string5[1];
								entityPlayerMP7 = this.configManager.getPlayerEntity(string6);
								if(entityPlayerMP7 != null) {
									try {
										int i8 = Integer.parseInt(string5[2]);
										if(Item.itemsList[i8] != null) {
											this.print(string4, "Giving " + entityPlayerMP7.username + " some " + i8);
											int i9 = 1;
											if(string5.length > 3) {
												i9 = this.parseInt(string5[3], 1);
											}

											if(i9 < 1) {
												i9 = 1;
											}

											if(i9 > 64) {
												i9 = 64;
											}

											entityPlayerMP7.dropPlayerItem(new ItemStack(i8, i9));
										} else {
											iCommandListener3.addHelpCommandMessage("There\'s no item with id " + i8);
										}
									} catch (NumberFormatException numberFormatException10) {
										iCommandListener3.addHelpCommandMessage("There\'s no item with id " + string5[2]);
									}
								} else {
									iCommandListener3.addHelpCommandMessage("Can\'t find user " + string6);
								}
							} else if(string2.toLowerCase().startsWith("say ")) {
								string2 = string2.substring(string2.indexOf(" ")).trim();
								logger.info("[" + string4 + "] " + string2);
								this.configManager.sendPacketToAllPlayers(new Packet3Chat("\u00a7d[Server] " + string2));
							} else if(string2.toLowerCase().startsWith("tell ")) {
								string5 = string2.split(" ");
								if(string5.length >= 3) {
									string2 = string2.substring(string2.indexOf(" ")).trim();
									string2 = string2.substring(string2.indexOf(" ")).trim();
									logger.info("[" + string4 + "->" + string5[1] + "] " + string2);
									this.configManager.sendPacketToAllPlayers(new Packet3Chat("\u00a7d[Server] " + string2));
									string2 = "\u00a77" + string4 + " whispers " + string2;
									logger.info(string2);
									if(!this.configManager.sendPacketToPlayer(string5[1], new Packet3Chat(string2))) {
										iCommandListener3.addHelpCommandMessage("There\'s no player by that name online.");
									}
								}
							} else {
								logger.info("Unknown console command. Type \"help\" for help.");
							}
						}
					}
				}
			} else {
				iCommandListener3.addHelpCommandMessage("To run the server without a gui, start it like this:");
				iCommandListener3.addHelpCommandMessage("   java -Xmx1024M -Xms1024M -jar minecraft_server.jar nogui");
				iCommandListener3.addHelpCommandMessage("Console commands:");
				iCommandListener3.addHelpCommandMessage("   help  or  ?               shows this message");
				iCommandListener3.addHelpCommandMessage("   kick <player>             removes a player from the server");
				iCommandListener3.addHelpCommandMessage("   ban <player>              bans a player from the server");
				iCommandListener3.addHelpCommandMessage("   pardon <player>           pardons a banned player so that they can connect again");
				iCommandListener3.addHelpCommandMessage("   ban-ip <ip>               bans an IP address from the server");
				iCommandListener3.addHelpCommandMessage("   pardon-ip <ip>            pardons a banned IP address so that they can connect again");
				iCommandListener3.addHelpCommandMessage("   op <player>               turns a player into an op");
				iCommandListener3.addHelpCommandMessage("   deop <player>             removes op status from a player");
				iCommandListener3.addHelpCommandMessage("   tp <player1> <player2>    moves one player to the same location as another player");
				iCommandListener3.addHelpCommandMessage("   give <player> <id> [num]  gives a player a resource");
				iCommandListener3.addHelpCommandMessage("   tell <player> <message>   sends a private message to a player");
				iCommandListener3.addHelpCommandMessage("   stop                      gracefully stops the server");
				iCommandListener3.addHelpCommandMessage("   save-all                  forces a server-wide level save");
				iCommandListener3.addHelpCommandMessage("   save-off                  disables terrain saving (useful for backup scripts)");
				iCommandListener3.addHelpCommandMessage("   save-on                   re-enables terrain saving");
				iCommandListener3.addHelpCommandMessage("   list                      lists all currently connected players");
				iCommandListener3.addHelpCommandMessage("   say <message>             broadcasts a message to all players");
			}
		}

	}

	private void print(String string1, String string2) {
		String string3 = string1 + ": " + string2;
		this.configManager.sendChatMessageToAllOps("\u00a77(" + string3 + ")");
		logger.info(string3);
	}

	private int parseInt(String string, int defaultValue) {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException numberFormatException4) {
			return defaultValue;
		}
	}

	public void addToOnlinePlayerList(IUpdatePlayerListBox playerListBox) {
		this.playersOnline.add(playerListBox);
	}

	public static void main(String[] args) {
		try {
			MinecraftServer minecraftServer1 = new MinecraftServer();
			if(!GraphicsEnvironment.isHeadless() && (args.length <= 0 || !args[0].equals("nogui"))) {
				ServerGUI.initGui(minecraftServer1);
			}

			(new ThreadServerApplication("Server thread", minecraftServer1)).start();
		} catch (Exception exception2) {
			logger.log(Level.SEVERE, "Failed to start the minecraft server", exception2);
		}

	}

	public File getFile(String fileName) {
		return new File(fileName);
	}

	public void addHelpCommandMessage(String string1) {
		logger.info(string1);
	}

	public String getUsername() {
		return "CONSOLE";
	}

	public static boolean isServerRunning(MinecraftServer mcServer) {
		return mcServer.serverRunning;
	}
}
