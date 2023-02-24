package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet17AddToInventory extends Packet {
	public int itemID;
	public int count;
	public int itemDamage;

	public Packet17AddToInventory() {
	}

	public Packet17AddToInventory(ItemStack stack, int count) {
		this.itemID = stack.itemID;
		this.count = count;
		this.itemDamage = stack.itemDmg;
		if(count == 0) {
			boolean count1 = true;
		}

	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.itemID = dataInputStream1.readShort();
		this.count = dataInputStream1.readByte();
		this.itemDamage = dataInputStream1.readShort();
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeShort(this.itemID);
		dataOutputStream1.writeByte(this.count);
		dataOutputStream1.writeShort(this.itemDamage);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleAddToInventory(this);
	}

	public int getPacketSize() {
		return 5;
	}
}
