package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet21PickupSpawn extends Packet {
	public int entityId;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public byte rotation;
	public byte pitch;
	public byte roll;
	public int itemID;
	public int count;

	public Packet21PickupSpawn() {
	}

	public Packet21PickupSpawn(EntityItem entityItem) {
		this.entityId = entityItem.entityID;
		this.itemID = entityItem.item.itemID;
		this.count = entityItem.item.stackSize;
		this.xPosition = MathHelper.floor_double(entityItem.posX * 32.0D);
		this.yPosition = MathHelper.floor_double(entityItem.posY * 32.0D);
		this.zPosition = MathHelper.floor_double(entityItem.posZ * 32.0D);
		this.rotation = (byte)((int)(entityItem.motionX * 128.0D));
		this.pitch = (byte)((int)(entityItem.motionY * 128.0D));
		this.roll = (byte)((int)(entityItem.motionZ * 128.0D));
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.entityId = dataInputStream1.readInt();
		this.itemID = dataInputStream1.readShort();
		this.count = dataInputStream1.readByte();
		this.xPosition = dataInputStream1.readInt();
		this.yPosition = dataInputStream1.readInt();
		this.zPosition = dataInputStream1.readInt();
		this.rotation = dataInputStream1.readByte();
		this.pitch = dataInputStream1.readByte();
		this.roll = dataInputStream1.readByte();
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.entityId);
		dataOutputStream1.writeShort(this.itemID);
		dataOutputStream1.writeByte(this.count);
		dataOutputStream1.writeInt(this.xPosition);
		dataOutputStream1.writeInt(this.yPosition);
		dataOutputStream1.writeInt(this.zPosition);
		dataOutputStream1.writeByte(this.rotation);
		dataOutputStream1.writeByte(this.pitch);
		dataOutputStream1.writeByte(this.roll);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handlePickupSpawn(this);
	}

	public int getPacketSize() {
		return 22;
	}
}
