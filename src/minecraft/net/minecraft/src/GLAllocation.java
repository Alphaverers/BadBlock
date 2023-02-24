package net.minecraft.src;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class GLAllocation {
	private static List displayLists = new ArrayList();
	private static List textureNames = new ArrayList();

	public static synchronized int generateDisplayLists(int i0) {
		int i1 = GL11.glGenLists(i0);
		displayLists.add(i1);
		displayLists.add(i0);
		return i1;
	}

	public static synchronized void generateTextureNames(IntBuffer intBuffer0) {
		GL11.glGenTextures(intBuffer0);

		for(int i1 = intBuffer0.position(); i1 < intBuffer0.limit(); ++i1) {
			textureNames.add(intBuffer0.get(i1));
		}

	}

	public static synchronized void deleteTexturesAndDisplayLists() {
		for(int i0 = 0; i0 < displayLists.size(); i0 += 2) {
			GL11.glDeleteLists(((Integer)displayLists.get(i0)).intValue(), ((Integer)displayLists.get(i0 + 1)).intValue());
		}

		IntBuffer intBuffer2 = createDirectIntBuffer(textureNames.size());
		intBuffer2.flip();
		GL11.glDeleteTextures(intBuffer2);

		for(int i1 = 0; i1 < textureNames.size(); ++i1) {
			intBuffer2.put(((Integer)textureNames.get(i1)).intValue());
		}

		intBuffer2.flip();
		GL11.glDeleteTextures(intBuffer2);
		displayLists.clear();
		textureNames.clear();
	}

	public static synchronized ByteBuffer createDirectByteBuffer(int i0) {
		ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(i0).order(ByteOrder.nativeOrder());
		return byteBuffer1;
	}

	public static IntBuffer createDirectIntBuffer(int i0) {
		return createDirectByteBuffer(i0 << 2).asIntBuffer();
	}

	public static FloatBuffer createDirectFloatBuffer(int i0) {
		return createDirectByteBuffer(i0 << 2).asFloatBuffer();
	}
}
