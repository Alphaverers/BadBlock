package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet16BlockItemSwitch extends Packet {
	public int entityId;
	public int id;

	public Packet16BlockItemSwitch() {
	}

	public Packet16BlockItemSwitch(int entityId, int id) {
		this.entityId = entityId;
		this.id = id;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.entityId = dataInputStream1.readInt();
		this.id = dataInputStream1.readShort();
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.entityId);
		dataOutputStream1.writeShort(this.id);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleBlockItemSwitch(this);
	}

	public int getPacketSize() {
		return 6;
	}
}
