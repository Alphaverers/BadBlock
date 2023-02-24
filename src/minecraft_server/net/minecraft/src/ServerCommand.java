package net.minecraft.src;

public class ServerCommand {
	public final String command;
	public final ICommandListener commandListener;

	public ServerCommand(String command, ICommandListener commandListener) {
		this.command = command;
		this.commandListener = commandListener;
	}
}
