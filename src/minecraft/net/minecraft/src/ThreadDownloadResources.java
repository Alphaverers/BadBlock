package net.minecraft.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.minecraft.client.Minecraft;

public class ThreadDownloadResources extends Thread {
	public File resourcesFolder;
	private Minecraft mc;
	private boolean closing = false;

	public ThreadDownloadResources(File file, Minecraft minecraft) {
		this.mc = minecraft;
		this.setName("Resource unpack thread");
		this.setDaemon(true);
		this.resourcesFolder = new File(file, "resources/");
		if(!this.resourcesFolder.exists() && !this.resourcesFolder.mkdirs()) {
			throw new RuntimeException("The working directory could not be created: " + this.resourcesFolder);
		}
	}

	void deleteDirectoryWalkTree(java.nio.file.Path path1) throws IOException {
		SimpleFileVisitor simpleFileVisitor2 = new SimpleFileVisitor() {
			public FileVisitResult visitFile(java.nio.file.Path path1, BasicFileAttributes basicFileAttributes2) throws IOException {
				Files.delete(path1);
				return FileVisitResult.CONTINUE;
			}

			public FileVisitResult visitFileFailed(java.nio.file.Path path1, IOException iOException2) throws IOException {
				Files.delete(path1);
				return FileVisitResult.CONTINUE;
			}

			public FileVisitResult postVisitDirectory(java.nio.file.Path path1, IOException iOException2) throws IOException {
				if(iOException2 != null) {
					throw iOException2;
				} else {
					Files.delete(path1);
					return FileVisitResult.CONTINUE;
				}
			}

			public FileVisitResult postVisitDirectory(Object object1, IOException iOException2) throws IOException {
				return this.postVisitDirectory((java.nio.file.Path)object1, iOException2);
			}

			public FileVisitResult visitFileFailed(Object object1, IOException iOException2) throws IOException {
				return this.visitFileFailed((java.nio.file.Path)object1, iOException2);
			}

			public FileVisitResult visitFile(Object object1, BasicFileAttributes basicFileAttributes2) throws IOException {
				return this.visitFile((java.nio.file.Path)object1, basicFileAttributes2);
			}
		};
		Files.walkFileTree(path1, simpleFileVisitor2);
	}

	public void UnpackReader(BufferedReader bufferedReader1) throws IOException {
		String string2;
		while((string2 = bufferedReader1.readLine()) != null) {
			if(string2 != "") {
				String string3 = string2.replace("worldstatic", "saves").replace("sounddata", "resources");
				InputStream inputStream4 = this.getClass().getResourceAsStream(string2);
				File file5 = new File(System.getProperty("user.dir") + string3);
				file5.mkdirs();
				Files.copy(inputStream4, Paths.get(System.getProperty("user.dir") + string3, new String[0]), new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
				inputStream4.close();
			}
		}

		System.out.println("Resources unpacked");
		bufferedReader1.close();
	}

	public void run() {
		try {
			try {
				this.deleteDirectoryWalkTree(Paths.get(System.getProperty("user.dir") + "/resources/", new String[0]));
				this.resourcesFolder.mkdirs();
			} catch (Exception exception5) {
				System.out.println("Error removing existing resources...");
			}

			SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH");
			int i2 = Integer.parseInt(simpleDateFormat1.format(Calendar.getInstance().getTime()));
			InputStream inputStream3 = this.getClass().getResourceAsStream(i2 <= 22 && i2 >= 5 ? "/soundres.txt" : "/soundres_a.txt");
			BufferedReader bufferedReader4 = new BufferedReader(new InputStreamReader(inputStream3));
			this.UnpackReader(bufferedReader4);
			System.out.println(System.getProperty("user.dir") + "/saves/World2/");
			if(!(new File(System.getProperty("user.dir") + "/saves/World2/")).exists()) {
				bufferedReader4 = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/wstatic_data.txt")));
				this.UnpackReader(bufferedReader4);
			}

			this.loadResource(this.resourcesFolder, "");
		} catch (Exception exception6) {
			exception6.printStackTrace();
			this.loadResource(this.resourcesFolder, "");
		}

	}

	public void closeMinecraft() {
		this.loadResource(this.resourcesFolder, "");
	}

	public void b() {
		this.closing = true;
	}

	private void loadResource(File file, String path) {
		File[] file3 = file.listFiles();

		for(byte b4 = 0; b4 < file3.length; ++b4) {
			if(file3[b4].isDirectory()) {
				this.loadResource(file3[b4], path + file3[b4].getName() + "/");
			} else {
				try {
					this.mc.installResource(path + file3[b4].getName(), file3[b4]);
				} catch (Exception exception6) {
					System.out.println("Failed to add " + path + file3[b4].getName());
				}
			}
		}

	}

	private void a(URL uRL1, String string2, long j3, int i5) {
		try {
			int i6 = string2.indexOf("/");
			String string7 = string2.substring(0, i6);
			if(!string7.equals("sound") && !string7.equals("newsound")) {
				if(i5 != 1) {
					return;
				}
			} else if(i5 != 0) {
				return;
			}

			File file8 = new File(this.resourcesFolder, string2);
			if(!file8.exists() || file8.length() != j3) {
				file8.getParentFile().mkdirs();
				String string9 = string2.replaceAll(" ", "%20");
				this.a(new URL(uRL1, string9), file8, j3);
				if(this.closing) {
					return;
				}
			}

			this.mc.installResource(string2, file8);
		} catch (Exception exception10) {
			exception10.printStackTrace();
		}

	}

	private void a(URL uRL1, File file2, long j3) {
	}
}
