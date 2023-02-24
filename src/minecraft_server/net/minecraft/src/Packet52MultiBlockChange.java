package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet52MultiBlockChange extends Packet {
	public int xPosition;
	public int zPosition;
	public short[] coordinateArray;
	public byte[] typeArray;
	public byte[] metadataArray;
	public int size;

	public Packet52MultiBlockChange() {
		this.isChunkDataPacket = true;
	}

	public Packet52MultiBlockChange(int xPosition, int zPosition, short[] data, int size, World world) {
		this.isChunkDataPacket = true;
		this.xPosition = xPosition;
		this.zPosition = zPosition;
		this.size = size;
		this.coordinateArray = new short[size];
		this.typeArray = new byte[size];
		this.metadataArray = new byte[size];
		Chunk chunk6 = world.getChunkFromChunkCoords(xPosition, zPosition);

		for(int i7 = 0; i7 < size; ++i7) {
			int i8 = data[i7] >> 12 & 15;
			int i9 = data[i7] >> 8 & 15;
			int i10 = data[i7] & 255;
			this.coordinateArray[i7] = data[i7];
			this.typeArray[i7] = (byte)chunk6.getBlockID(i8, i10, i9);
			this.metadataArray[i7] = (byte)chunk6.getBlockMetadata(i8, i10, i9);
		}

	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.xPosition = dataInputStream1.readInt();
		this.zPosition = dataInputStream1.readInt();
		this.size = dataInputStream1.readShort() & 65535;
		this.coordinateArray = new short[this.size];
		this.typeArray = new byte[this.size];
		this.metadataArray = new byte[this.size];

		for(int i2 = 0; i2 < this.size; ++i2) {
			this.coordinateArray[i2] = dataInputStream1.readShort();
		}

		dataInputStream1.readFully(this.typeArray);
		dataInputStream1.readFully(this.metadataArray);
	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.xPosition);
		dataOutputStream1.writeInt(this.zPosition);
		dataOutputStream1.writeShort((short)this.size);

		for(int i2 = 0; i2 < this.size; ++i2) {
			dataOutputStream1.writeShort(this.coordinateArray[i2]);
		}

		dataOutputStream1.write(this.typeArray);
		dataOutputStream1.write(this.metadataArray);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleMultiBlockChange(this);
	}

	public int getPacketSize() {
		return 10 + this.size * 4;
	}
}
