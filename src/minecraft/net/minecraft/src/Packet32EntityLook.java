package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet32EntityLook extends Packet30Entity {
	public Packet32EntityLook() {
		this.rotating = true;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		super.readPacketData(dataInputStream1);
		this.yaw = dataInputStream1.readByte();
		this.pitch = dataInputStream1.readByte();
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		super.writePacket(dataOutputStream1);
		dataOutputStream1.writeByte(this.yaw);
		dataOutputStream1.writeByte(this.pitch);
	}

	public int getPacketSize() {
		return 6;
	}
}
