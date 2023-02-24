package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet15Place extends Packet {
	public int id;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public int direction;

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.id = dataInputStream1.readShort();
		this.xPosition = dataInputStream1.readInt();
		this.yPosition = dataInputStream1.read();
		this.zPosition = dataInputStream1.readInt();
		this.direction = dataInputStream1.read();
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeShort(this.id);
		dataOutputStream1.writeInt(this.xPosition);
		dataOutputStream1.write(this.yPosition);
		dataOutputStream1.writeInt(this.zPosition);
		dataOutputStream1.write(this.direction);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handlePlace(this);
	}

	public int getPacketSize() {
		return 12;
	}
}
