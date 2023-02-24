package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet11PlayerPosition extends Packet10Flying {
	public Packet11PlayerPosition() {
		this.moving = true;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.xPosition = dataInputStream1.readDouble();
		this.yPosition = dataInputStream1.readDouble();
		this.stance = dataInputStream1.readDouble();
		this.zPosition = dataInputStream1.readDouble();
		super.readPacketData(dataInputStream1);
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeDouble(this.xPosition);
		dataOutputStream1.writeDouble(this.yPosition);
		dataOutputStream1.writeDouble(this.stance);
		dataOutputStream1.writeDouble(this.zPosition);
		super.writePacket(dataOutputStream1);
	}

	public int getPacketSize() {
		return 33;
	}
}
