package net.minecraft.src;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;

class ThreadDownloadImage extends Thread {
	private ThreadDownloadImageData paramby2;
	private String paramString2;
	private ImageBuffer paramgr2;

	ThreadDownloadImage(ThreadDownloadImageData downloadImageData, String location, ImageBuffer imageBuffer) {
		this.paramby2 = downloadImageData;
		this.paramString2 = location;
		this.paramgr2 = imageBuffer;
	}

	public void run() {
		if(this.paramString2.startsWith("file:///")) {
			try {
				if(this.paramgr2 == null) {
					this.paramby2.image = ImageIO.read(new File(this.paramString2.substring(8)));
				} else {
					this.paramby2.image = this.paramgr2.parseUserSkin(ImageIO.read(new File(this.paramString2.substring(8))));
				}

				System.out.println("Loaded from local file");
			} catch (Exception exception7) {
				exception7.printStackTrace();
			}

		} else {
			HttpURLConnection httpURLConnection1 = null;

			try {
				URL uRL2 = new URL(this.paramString2);
				httpURLConnection1 = (HttpURLConnection)uRL2.openConnection();
				httpURLConnection1.setDoInput(true);
				httpURLConnection1.setDoOutput(false);
				httpURLConnection1.connect();
				if(httpURLConnection1.getResponseCode() != 404) {
					if(this.paramgr2 == null) {
						this.paramby2.image = ImageIO.read(httpURLConnection1.getInputStream());
					} else {
						this.paramby2.image = this.paramgr2.parseUserSkin(ImageIO.read(httpURLConnection1.getInputStream()));
					}

					return;
				}
			} catch (Exception exception8) {
				exception8.printStackTrace();
				return;
			} finally {
				httpURLConnection1.disconnect();
			}

		}
	}
}
