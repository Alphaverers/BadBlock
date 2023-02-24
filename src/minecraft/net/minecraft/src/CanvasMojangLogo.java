package net.minecraft.src;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import javax.imageio.ImageIO;

class CanvasMojangLogo extends Canvas {
	private BufferedImage logo;

	public CanvasMojangLogo() {
		try {
			this.logo = ImageIO.read(PanelCrashReport.class.getResource("/gui/logo.png"));
		} catch (IOException iOException2) {
		}

		byte b1 = 100;
		this.setPreferredSize(new Dimension(b1, b1));
		this.setMinimumSize(new Dimension(b1, b1));
	}

	public void paint(Graphics graphics) {
		super.paint(graphics);
		graphics.drawImage(this.logo, this.getWidth() / 2 - this.logo.getWidth() / 2, 32, (ImageObserver)null);
	}
}
