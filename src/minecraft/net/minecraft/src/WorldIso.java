package net.minecraft.src;

import java.io.File;

class WorldIso extends World {
	final CanvasIsomPreview isomPreview;

	WorldIso(CanvasIsomPreview canvasIsomPreview1, File file2, String string3) {
		super(file2, string3);
		this.isomPreview = canvasIsomPreview1;
	}

	protected IChunkProvider getChunkProvider(File file1) {
		return new ChunkProviderIso(this, new ChunkLoader(file1, false));
	}
}
