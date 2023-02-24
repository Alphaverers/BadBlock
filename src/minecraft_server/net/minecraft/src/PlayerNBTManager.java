package net.minecraft.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.logging.Logger;

public class PlayerNBTManager {
	public static Logger logger = Logger.getLogger("Minecraft");
	private File playerNBT;

	public PlayerNBTManager(File playerNBT) {
		this.playerNBT = playerNBT;
		playerNBT.mkdir();
	}

	public void writePlayerNBT(EntityPlayerMP entityPlayerMP) {
		try {
			NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
			entityPlayerMP.writeToNBT(nBTTagCompound2);
			File file3 = new File(this.playerNBT, "_tmp_.dat");
			File file4 = new File(this.playerNBT, entityPlayerMP.username + ".dat");
			CompressedStreamTools.writeCompressed(nBTTagCompound2, new FileOutputStream(file3));
			if(file4.exists()) {
				file4.delete();
			}

			file3.renameTo(file4);
		} catch (Exception exception5) {
			logger.warning("Failed to save player data for " + entityPlayerMP.username);
		}

	}

	public void readPlayerNBT(EntityPlayerMP entityPlayerMP) {
		try {
			File file2 = new File(this.playerNBT, entityPlayerMP.username + ".dat");
			if(file2.exists()) {
				NBTTagCompound nBTTagCompound3 = CompressedStreamTools.readCompressed(new FileInputStream(file2));
				if(nBTTagCompound3 != null) {
					entityPlayerMP.readFromNBT(nBTTagCompound3);
				}
			}
		} catch (Exception exception4) {
			logger.warning("Failed to load player data for " + entityPlayerMP.username);
		}

	}
}
