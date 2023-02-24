package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet13PlayerLookMove extends Packet10Flying {
	public Packet13PlayerLookMove() {
		this.rotating = true;
		this.moving = true;
	}

	public Packet13PlayerLookMove(double xPosition, double yPosition, double stance, double zPosition, float yaw, float pitch, boolean onGround) {
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.stance = stance;
		this.zPosition = zPosition;
		this.yaw = yaw;
		this.pitch = pitch;
		this.onGround = onGround;
		this.rotating = true;
		this.moving = true;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.xPosition = dataInputStream1.readDouble();
		this.yPosition = dataInputStream1.readDouble();
		this.stance = dataInputStream1.readDouble();
		this.zPosition = dataInputStream1.readDouble();
		this.yaw = dataInputStream1.readFloat();
		this.pitch = dataInputStream1.readFloat();
		super.readPacketData(dataInputStream1);
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeDouble(this.xPosition);
		dataOutputStream1.writeDouble(this.yPosition);
		dataOutputStream1.writeDouble(this.stance);
		dataOutputStream1.writeDouble(this.zPosition);
		dataOutputStream1.writeFloat(this.yaw);
		dataOutputStream1.writeFloat(this.pitch);
		super.writePacket(dataOutputStream1);
	}

	public int getPacketSize() {
		return 41;
	}
}
