package net.minecraft.src;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressedStreamTools {
	public static NBTTagCompound readCompressed(InputStream inputStream) throws IOException {
		DataInputStream dataInputStream1 = new DataInputStream(new GZIPInputStream(inputStream));

		NBTTagCompound nBTTagCompound2;
		try {
			nBTTagCompound2 = read(dataInputStream1);
		} finally {
			dataInputStream1.close();
		}

		return nBTTagCompound2;
	}

	public static void writeCompressed(NBTTagCompound nbttagcompound, OutputStream outputStream) throws IOException {
		DataOutputStream dataOutputStream2 = new DataOutputStream(new GZIPOutputStream(outputStream));

		try {
			write(nbttagcompound, dataOutputStream2);
		} finally {
			dataOutputStream2.close();
		}

	}

	public static NBTTagCompound decompress(byte[] data) throws IOException {
		DataInputStream dataInputStream1 = new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(data)));

		NBTTagCompound nBTTagCompound2;
		try {
			nBTTagCompound2 = read(dataInputStream1);
		} finally {
			dataInputStream1.close();
		}

		return nBTTagCompound2;
	}

	public static byte[] compress(NBTTagCompound nbttagcompound) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream2 = new DataOutputStream(new GZIPOutputStream(byteArrayOutputStream1));

		try {
			write(nbttagcompound, dataOutputStream2);
		} finally {
			dataOutputStream2.close();
		}

		return byteArrayOutputStream1.toByteArray();
	}

	public static NBTTagCompound read(DataInput dataInput) throws IOException {
		NBTBase nBTBase1 = NBTBase.readNamedTag(dataInput);
		if(nBTBase1 instanceof NBTTagCompound) {
			return (NBTTagCompound)nBTBase1;
		} else {
			throw new IOException("Root tag must be a named compound tag");
		}
	}

	public static void write(NBTTagCompound nbttagcompound, DataOutput dataOutput) throws IOException {
		NBTBase.writeNamedTag(nbttagcompound, dataOutput);
	}
}
