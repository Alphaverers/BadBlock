package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet20NamedEntitySpawn extends Packet {
	public int entityId;
	public String name;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public byte rotation;
	public byte pitch;
	public int currentItem;

	public Packet20NamedEntitySpawn() {
	}

	public Packet20NamedEntitySpawn(EntityPlayer entityPlayer) {
		this.entityId = entityPlayer.entityID;
		this.name = entityPlayer.username;
		this.xPosition = MathHelper.floor_double(entityPlayer.posX * 32.0D);
		this.yPosition = MathHelper.floor_double(entityPlayer.posY * 32.0D);
		this.zPosition = MathHelper.floor_double(entityPlayer.posZ * 32.0D);
		this.rotation = (byte)((int)(entityPlayer.rotationYaw * 256.0F / 360.0F));
		this.pitch = (byte)((int)(entityPlayer.rotationPitch * 256.0F / 360.0F));
		ItemStack itemStack2 = entityPlayer.inventory.getCurrentItem();
		this.currentItem = itemStack2 == null ? 0 : itemStack2.itemID;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.entityId = dataInputStream1.readInt();
		this.name = dataInputStream1.readUTF();
		this.xPosition = dataInputStream1.readInt();
		this.yPosition = dataInputStream1.readInt();
		this.zPosition = dataInputStream1.readInt();
		this.rotation = dataInputStream1.readByte();
		this.pitch = dataInputStream1.readByte();
		this.currentItem = dataInputStream1.readShort();
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.entityId);
		dataOutputStream1.writeUTF(this.name);
		dataOutputStream1.writeInt(this.xPosition);
		dataOutputStream1.writeInt(this.yPosition);
		dataOutputStream1.writeInt(this.zPosition);
		dataOutputStream1.writeByte(this.rotation);
		dataOutputStream1.writeByte(this.pitch);
		dataOutputStream1.writeShort(this.currentItem);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleNamedEntitySpawn(this);
	}

	public int getPacketSize() {
		return 28;
	}
}
