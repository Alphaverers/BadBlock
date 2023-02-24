package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet12PlayerLook extends Packet10Flying {
	public Packet12PlayerLook() {
		this.rotating = true;
	}

	public Packet12PlayerLook(float yaw, float pitch, boolean onGround) {
		this.yaw = yaw;
		this.pitch = pitch;
		this.onGround = onGround;
		this.rotating = true;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.yaw = dataInputStream1.readFloat();
		this.pitch = dataInputStream1.readFloat();
		super.readPacketData(dataInputStream1);
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeFloat(this.yaw);
		dataOutputStream1.writeFloat(this.pitch);
		super.writePacket(dataOutputStream1);
	}

	public int getPacketSize() {
		return 9;
	}
}
