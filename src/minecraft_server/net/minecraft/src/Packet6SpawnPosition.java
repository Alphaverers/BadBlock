package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet6SpawnPosition extends Packet {
	public int xPosition;
	public int yPosition;
	public int zPosition;

	public Packet6SpawnPosition() {
	}

	public Packet6SpawnPosition(int xPosition, int yPosition, int zPosition) {
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.zPosition = zPosition;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.xPosition = dataInputStream1.readInt();
		this.yPosition = dataInputStream1.readInt();
		this.zPosition = dataInputStream1.readInt();
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.xPosition);
		dataOutputStream1.writeInt(this.yPosition);
		dataOutputStream1.writeInt(this.zPosition);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleSpawnPosition(this);
	}

	public int getPacketSize() {
		return 12;
	}
}
