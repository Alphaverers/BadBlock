package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet29DestroyEntity extends Packet {
	public int entityId;

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.entityId = dataInputStream1.readInt();
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.entityId);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleDestroyEntity(this);
	}

	public int getPacketSize() {
		return 4;
	}
}
