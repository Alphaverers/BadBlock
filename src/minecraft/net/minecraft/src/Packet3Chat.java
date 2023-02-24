package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet3Chat extends Packet {
	public String message;

	public Packet3Chat() {
	}

	public Packet3Chat(String msg) {
		this.message = msg;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.message = dataInputStream1.readUTF();
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeUTF(this.message);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleChat(this);
	}

	public int getPacketSize() {
		return this.message.length();
	}
}
