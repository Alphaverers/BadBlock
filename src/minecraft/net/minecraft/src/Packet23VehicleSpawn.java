package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet23VehicleSpawn extends Packet {
	public int entityId;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public int type;

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.entityId = dataInputStream1.readInt();
		this.type = dataInputStream1.readByte();
		this.xPosition = dataInputStream1.readInt();
		this.yPosition = dataInputStream1.readInt();
		this.zPosition = dataInputStream1.readInt();
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.entityId);
		dataOutputStream1.writeByte(this.type);
		dataOutputStream1.writeInt(this.xPosition);
		dataOutputStream1.writeInt(this.yPosition);
		dataOutputStream1.writeInt(this.zPosition);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleVehicleSpawn(this);
	}

	public int getPacketSize() {
		return 17;
	}
}
