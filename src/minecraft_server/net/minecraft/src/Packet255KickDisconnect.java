package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet255KickDisconnect extends Packet {
	public String reason;

	public Packet255KickDisconnect() {
	}

	public Packet255KickDisconnect(String reason) {
		this.reason = reason;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.reason = dataInputStream1.readUTF();
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeUTF(this.reason);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleKickDisconnect(this);
	}

	public int getPacketSize() {
		return this.reason.length();
	}
}
