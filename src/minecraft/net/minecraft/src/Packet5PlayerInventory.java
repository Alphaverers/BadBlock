package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet5PlayerInventory extends Packet {
	public int inventoryType;
	public ItemStack[] inventory;

	public Packet5PlayerInventory() {
	}

	public Packet5PlayerInventory(int invType, ItemStack[] invContents) {
		this.inventoryType = invType;
		this.inventory = new ItemStack[invContents.length];

		for(int i3 = 0; i3 < this.inventory.length; ++i3) {
			this.inventory[i3] = invContents[i3] == null ? null : invContents[i3].copy();
		}

	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.inventoryType = dataInputStream1.readInt();
		short s2 = dataInputStream1.readShort();
		this.inventory = new ItemStack[s2];

		for(int i3 = 0; i3 < s2; ++i3) {
			short s4 = dataInputStream1.readShort();
			if(s4 >= 0) {
				byte b5 = dataInputStream1.readByte();
				short s6 = dataInputStream1.readShort();
				this.inventory[i3] = new ItemStack(s4, b5, s6);
			}
		}

	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.inventoryType);
		dataOutputStream1.writeShort(this.inventory.length);

		for(int i2 = 0; i2 < this.inventory.length; ++i2) {
			if(this.inventory[i2] == null) {
				dataOutputStream1.writeShort(-1);
			} else {
				dataOutputStream1.writeShort((short)this.inventory[i2].itemID);
				dataOutputStream1.writeByte((byte)this.inventory[i2].stackSize);
				dataOutputStream1.writeShort((short)this.inventory[i2].itemDmg);
			}
		}

	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handlePlayerInventory(this);
	}

	public int getPacketSize() {
		return 6 + this.inventory.length * 5;
	}
}
