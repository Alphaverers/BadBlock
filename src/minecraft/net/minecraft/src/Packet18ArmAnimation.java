package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet18ArmAnimation extends Packet {
	public int entityId;
	public int animate;

	public Packet18ArmAnimation() {
	}

	public Packet18ArmAnimation(Entity entity, int animate) {
		this.entityId = entity.entityID;
		this.animate = animate;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.entityId = dataInputStream1.readInt();
		this.animate = dataInputStream1.readByte();
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.entityId);
		dataOutputStream1.writeByte(this.animate);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleArmAnimation(this);
	}

	public int getPacketSize() {
		return 5;
	}
}
