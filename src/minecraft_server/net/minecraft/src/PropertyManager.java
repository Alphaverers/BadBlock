package net.minecraft.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertyManager {
	public static Logger logger = Logger.getLogger("Minecraft");
	private Properties serverProperties = new Properties();
	private File propertiesFile;

	public PropertyManager(File propertiesFile) {
		this.propertiesFile = propertiesFile;
		if(propertiesFile.exists()) {
			try {
				this.serverProperties.load(new FileInputStream(propertiesFile));
			} catch (Exception exception3) {
				logger.log(Level.WARNING, "Failed to load " + propertiesFile, exception3);
				this.generateAndSaveProperties();
			}
		} else {
			logger.log(Level.WARNING, propertiesFile + " does not exist");
			this.generateAndSaveProperties();
		}

	}

	public void generateAndSaveProperties() {
		logger.log(Level.INFO, "Generating new properties file");
		this.saveProperties();
	}

	public void saveProperties() {
		try {
			this.serverProperties.store(new FileOutputStream(this.propertiesFile), "Minecraft server properties");
		} catch (Exception exception2) {
			logger.log(Level.WARNING, "Failed to save " + this.propertiesFile, exception2);
			this.generateAndSaveProperties();
		}

	}

	public String getStringProperty(String key, String value) {
		if(!this.serverProperties.containsKey(key)) {
			this.serverProperties.setProperty(key, value);
			this.saveProperties();
		}

		return this.serverProperties.getProperty(key, value);
	}

	public int getIntProperty(String key, int value) {
		try {
			return Integer.parseInt(this.getStringProperty(key, "" + value));
		} catch (Exception exception4) {
			this.serverProperties.setProperty(key, "" + value);
			return value;
		}
	}

	public boolean getBooleanProperty(String key, boolean value) {
		try {
			return Boolean.parseBoolean(this.getStringProperty(key, "" + value));
		} catch (Exception exception4) {
			this.serverProperties.setProperty(key, "" + value);
			return value;
		}
	}
}
