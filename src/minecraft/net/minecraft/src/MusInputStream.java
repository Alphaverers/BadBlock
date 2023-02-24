package net.minecraft.src;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

class MusInputStream extends InputStream {
	private int hash;
	private InputStream inputStream;
	byte[] buffer;
	final CodecMus codec;

	public MusInputStream(CodecMus codec, URL url, InputStream inputStream) {
		this.codec = codec;
		this.buffer = new byte[1];
		this.inputStream = inputStream;
		String string4 = url.getPath();
		string4 = string4.substring(string4.lastIndexOf("/") + 1);
		this.hash = string4.hashCode();
	}

	public int read() throws IOException {
		int i1 = this.read(this.buffer, 0, 1);
		return i1 < 0 ? i1 : this.buffer[0];
	}

	public int read(byte[] b1, int i2, int i3) throws IOException {
		i3 = this.inputStream.read(b1, i2, i3);

		for(int i4 = 0; i4 < i3; ++i4) {
			byte b5 = b1[i2 + i4] = (byte)(b1[i2 + i4] ^ this.hash >> 8);
			this.hash = this.hash * 498729871 + 85731 * b5;
		}

		return i3;
	}
}
