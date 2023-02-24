package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet1Login extends Packet {
	public int protocolVersion;
	public String username;
	public String password;

	public Packet1Login() {
	}

	public Packet1Login(String username, String mppass, int protocolVer) {
		this.username = username;
		this.password = mppass;
		this.protocolVersion = protocolVer;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.protocolVersion = dataInputStream1.readInt();
		this.username = dataInputStream1.readUTF();
		this.password = dataInputStream1.readUTF();
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.protocolVersion);
		dataOutputStream1.writeUTF(this.username);
		dataOutputStream1.writeUTF(this.password);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleLogin(this);
	}

	public int getPacketSize() {
		return 4 + this.username.length() + this.password.length() + 4;
	}
}
