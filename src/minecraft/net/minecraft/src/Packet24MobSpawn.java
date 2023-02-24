package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet24MobSpawn extends Packet {
	public int entityId;
	public byte type;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public byte yaw;
	public byte pitch;

	public Packet24MobSpawn() {
	}

	public Packet24MobSpawn(EntityLiving entityLiving) {
		this.entityId = entityLiving.entityID;
		this.type = (byte)EntityList.getEntityID(entityLiving);
		this.xPosition = MathHelper.floor_double(entityLiving.posX * 32.0D);
		this.yPosition = MathHelper.floor_double(entityLiving.posY * 32.0D);
		this.zPosition = MathHelper.floor_double(entityLiving.posZ * 32.0D);
		this.yaw = (byte)((int)(entityLiving.rotationYaw * 256.0F / 360.0F));
		this.pitch = (byte)((int)(entityLiving.rotationPitch * 256.0F / 360.0F));
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.entityId = dataInputStream1.readInt();
		this.type = dataInputStream1.readByte();
		this.xPosition = dataInputStream1.readInt();
		this.yPosition = dataInputStream1.readInt();
		this.zPosition = dataInputStream1.readInt();
		this.yaw = dataInputStream1.readByte();
		this.pitch = dataInputStream1.readByte();
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.entityId);
		dataOutputStream1.writeByte(this.type);
		dataOutputStream1.writeInt(this.xPosition);
		dataOutputStream1.writeInt(this.yPosition);
		dataOutputStream1.writeInt(this.zPosition);
		dataOutputStream1.writeByte(this.yaw);
		dataOutputStream1.writeByte(this.pitch);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleMobSpawn(this);
	}

	public int getPacketSize() {
		return 19;
	}
}
