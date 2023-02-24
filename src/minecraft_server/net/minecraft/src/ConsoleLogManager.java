package net.minecraft.src;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsoleLogManager {
	public static Logger logger = Logger.getLogger("Minecraft");

	public static void init() {
		ConsoleLogFormatter consoleLogFormatter0 = new ConsoleLogFormatter();
		logger.setUseParentHandlers(false);
		ConsoleHandler consoleHandler1 = new ConsoleHandler();
		consoleHandler1.setFormatter(consoleLogFormatter0);
		logger.addHandler(consoleHandler1);

		try {
			FileHandler fileHandler2 = new FileHandler("server.log");
			fileHandler2.setFormatter(consoleLogFormatter0);
			logger.addHandler(fileHandler2);
		} catch (Exception exception3) {
			logger.log(Level.WARNING, "Failed to log to server.log", exception3);
		}

	}
}
