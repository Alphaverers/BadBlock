package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet53BlockChange extends Packet {
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public int type;
	public int metadata;

	public Packet53BlockChange() {
		this.isChunkDataPacket = true;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.xPosition = dataInputStream1.readInt();
		this.yPosition = dataInputStream1.read();
		this.zPosition = dataInputStream1.readInt();
		this.type = dataInputStream1.read();
		this.metadata = dataInputStream1.read();
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.xPosition);
		dataOutputStream1.write(this.yPosition);
		dataOutputStream1.writeInt(this.zPosition);
		dataOutputStream1.write(this.type);
		dataOutputStream1.write(this.metadata);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleBlockChange(this);
	}

	public int getPacketSize() {
		return 11;
	}
}
