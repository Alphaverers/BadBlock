package net.minecraft.src;

public abstract class TileEntitySpecialRenderer {
	protected TileEntityRenderer tileEntityRenderer;

	public abstract void renderTileEntityAt(TileEntity tileEntity1, double d2, double d4, double d6, float f8);

	protected void bindTextureByName(String name) {
		RenderEngine renderEngine2 = this.tileEntityRenderer.renderEngine;
		renderEngine2.bindTexture(renderEngine2.getTexture(name));
	}

	public void setTileEntityRenderer(TileEntityRenderer tileEntityRenderer) {
		this.tileEntityRenderer = tileEntityRenderer;
	}

	public FontRenderer getFontRenderer() {
		return this.tileEntityRenderer.getFontRenderer();
	}
}
