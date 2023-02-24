package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet31RelEntityMove extends Packet30Entity {
	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		super.readPacketData(dataInputStream1);
		this.xPosition = dataInputStream1.readByte();
		this.yPosition = dataInputStream1.readByte();
		this.zPosition = dataInputStream1.readByte();
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		super.writePacket(dataOutputStream1);
		dataOutputStream1.writeByte(this.xPosition);
		dataOutputStream1.writeByte(this.yPosition);
		dataOutputStream1.writeByte(this.zPosition);
	}

	public int getPacketSize() {
		return 7;
	}
}
