package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Packet51MapChunk extends Packet {
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public int xSize;
	public int ySize;
	public int zSize;
	public byte[] chunkData;
	private int tempLength;

	public Packet51MapChunk() {
		this.isChunkDataPacket = true;
	}

	public Packet51MapChunk(int xPosition, int yPosition, int zPosition, int xSize, int ySize, int zSize, World world) {
		this.isChunkDataPacket = true;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.zPosition = zPosition;
		this.xSize = xSize;
		this.ySize = ySize;
		this.zSize = zSize;
		byte[] b8 = world.getChunkData(xPosition, yPosition, zPosition, xSize, ySize, zSize);
		Deflater deflater9 = new Deflater(1);

		try {
			deflater9.setInput(b8);
			deflater9.finish();
			this.chunkData = new byte[xSize * ySize * zSize * 5 / 2];
			this.tempLength = deflater9.deflate(this.chunkData);
		} finally {
			deflater9.end();
		}

	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.xPosition = dataInputStream1.readInt();
		this.yPosition = dataInputStream1.readShort();
		this.zPosition = dataInputStream1.readInt();
		this.xSize = dataInputStream1.read() + 1;
		this.ySize = dataInputStream1.read() + 1;
		this.zSize = dataInputStream1.read() + 1;
		int i2 = dataInputStream1.readInt();
		byte[] b3 = new byte[i2];
		dataInputStream1.readFully(b3);
		this.chunkData = new byte[this.xSize * this.ySize * this.zSize * 5 / 2];
		Inflater inflater4 = new Inflater();
		inflater4.setInput(b3);

		try {
			inflater4.inflate(this.chunkData);
		} catch (DataFormatException dataFormatException9) {
			throw new IOException("Bad compressed data format");
		} finally {
			inflater4.end();
		}

	}

	public void writePacket(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.xPosition);
		dataOutputStream1.writeShort(this.yPosition);
		dataOutputStream1.writeInt(this.zPosition);
		dataOutputStream1.write(this.xSize - 1);
		dataOutputStream1.write(this.ySize - 1);
		dataOutputStream1.write(this.zSize - 1);
		dataOutputStream1.writeInt(this.tempLength);
		dataOutputStream1.write(this.chunkData, 0, this.tempLength);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleMapChunk(this);
	}

	public int getPacketSize() {
		return 17 + this.tempLength;
	}
}
