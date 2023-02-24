package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet22Collect extends Packet {
	public int collectedEntityId;
	public int collectorEntityId;

	public Packet22Collect() {
	}

	public Packet22Collect(int collectedEntityID, int collectorEntityID) {
		this.collectedEntityId = collectedEntityID;
		this.collectorEntityId = collectorEntityID;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.collectedEntityId = dataInputStream1.readInt();
		this.collectorEntityId = dataInputStream1.readInt();
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.collectedEntityId);
		dataOutputStream1.writeInt(this.collectorEntityId);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleCollect(this);
	}

	public int getPacketSize() {
		return 8;
	}
}
