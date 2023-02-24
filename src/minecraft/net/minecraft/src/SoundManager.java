package net.minecraft.src;

import java.io.File;
import java.util.Random;

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

public class SoundManager {
	private SoundSystem sndSystem;
	private SoundPool soundPoolSounds = new SoundPool();
	private SoundPool soundPoolStreaming = new SoundPool();
	private SoundPool soundPoolMusic = new SoundPool();
	private int playedSoundsCount = 0;
	private GameSettings options;
	private boolean loaded = false;
	private Random rand = new Random();
	private int ticksBeforeMusic = this.rand.nextInt(12000);

	public void loadSoundSettings(GameSettings settings) {
		this.soundPoolStreaming.isGetRandomSound = false;
		this.options = settings;
		if(!this.loaded && (settings == null || settings.b || settings.a)) {
			this.tryToSetLibraryAndCodecs();
		}

	}

	private void tryToSetLibraryAndCodecs() {
		try {
			boolean z1 = this.options.b;
			boolean z2 = this.options.a;
			this.options.b = false;
			this.options.a = false;
			this.options.saveOptions();
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
			SoundSystemConfig.setCodec("mus", CodecMus.class);
			SoundSystemConfig.setCodec("wav", CodecWav.class);
			this.sndSystem = new SoundSystem();
			this.options.b = z1;
			this.options.a = z2;
			this.options.saveOptions();
		} catch (Throwable throwable3) {
			throwable3.printStackTrace();
			System.err.println("error linking with the LibraryJavaSound plug-in");
		}

		this.loaded = true;
	}

	public void onSoundOptionsChanged() {
		if(!this.loaded && (this.options.b || this.options.a)) {
			this.tryToSetLibraryAndCodecs();
		}

		if(!this.options.a) {
			this.sndSystem.stop("BgMusic");
		}

	}

	public void closeMinecraft() {
		if(this.loaded) {
			this.sndSystem.cleanup();
		}

	}

	public void addSound(String name, File file) {
		this.soundPoolSounds.addSound(name, file);
	}

	public void addStreaming(String name, File file) {
		this.soundPoolStreaming.addSound(name, file);
	}

	public void addMusic(String name, File file) {
		this.soundPoolMusic.addSound(name, file);
	}

	public void playRandomMusicIfReady() {
		if(this.loaded && this.options.a) {
			if(!this.sndSystem.playing("BgMusic") && !this.sndSystem.playing("streaming")) {
				if(this.ticksBeforeMusic > 0) {
					--this.ticksBeforeMusic;
					return;
				}

				SoundPoolEntry soundPoolEntry1 = this.soundPoolMusic.getRandomSound();
				if(soundPoolEntry1 != null) {
					this.ticksBeforeMusic = this.rand.nextInt(24000) + 24000;
					this.sndSystem.backgroundMusic("BgMusic", soundPoolEntry1.soundUrl, soundPoolEntry1.soundName, false);
					this.sndSystem.play("BgMusic");
				}
			}

		}
	}

	public void setListener(EntityLiving listener, float partialTick) {
		if(this.loaded && this.options.b) {
			if(listener != null) {
				float f3 = listener.prevRotationYaw + (listener.rotationYaw - listener.prevRotationYaw) * partialTick;
				double d4 = listener.prevPosX + (listener.posX - listener.prevPosX) * (double)partialTick;
				double d6 = listener.prevPosY + (listener.posY - listener.prevPosY) * (double)partialTick;
				double d8 = listener.prevPosZ + (listener.posZ - listener.prevPosZ) * (double)partialTick;
				float f10 = MathHelper.cos(-f3 * 0.017453292F - (float)Math.PI);
				float f11 = MathHelper.sin(-f3 * 0.017453292F - (float)Math.PI);
				float f12 = -f11;
				float f13 = 0.0F;
				float f14 = -f10;
				float f15 = 0.0F;
				float f16 = 1.0F;
				float f17 = 0.0F;
				this.sndSystem.setListenerPosition((float)d4, (float)d6, (float)d8);
				this.sndSystem.setListenerOrientation(f12, f13, f14, f15, f16, f17);
			}
		}
	}

	public void playStreaming(String sound, float posX, float posY, float posZ, float volume, float pitch) {
		if(this.loaded && this.options.b) {
			String string7 = "streaming";
			if(this.sndSystem.playing("streaming")) {
				this.sndSystem.stop("streaming");
			}

			if(sound != null) {
				SoundPoolEntry soundPoolEntry8 = this.soundPoolStreaming.getRandomSoundFromSoundPool(sound);
				if(soundPoolEntry8 != null && volume > 0.0F) {
					if(this.sndSystem.playing("BgMusic")) {
						this.sndSystem.stop("BgMusic");
					}

					float f9 = 16.0F;
					this.sndSystem.newStreamingSource(true, string7, soundPoolEntry8.soundUrl, soundPoolEntry8.soundName, false, posX, posY, posZ, 2, f9 * 4.0F);
					this.sndSystem.setVolume(string7, 0.5F);
					this.sndSystem.play(string7);
				}

			}
		}
	}

	public void playSound(String sound, float posX, float posY, float posZ, float volume, float pitch) {
		if(this.loaded && this.options.b) {
			SoundPoolEntry soundPoolEntry7 = this.soundPoolSounds.getRandomSoundFromSoundPool(sound);
			if(soundPoolEntry7 != null && volume > 0.0F) {
				this.playedSoundsCount = (this.playedSoundsCount + 1) % 256;
				String string8 = "sound_" + this.playedSoundsCount;
				float f9 = 16.0F;
				if(volume > 1.0F) {
					f9 *= volume;
				}

				this.sndSystem.newSource(volume > 1.0F, string8, soundPoolEntry7.soundUrl, soundPoolEntry7.soundName, false, posX, posY, posZ, 2, f9);
				this.sndSystem.setPitch(string8, pitch);
				if(volume > 1.0F) {
					volume = 1.0F;
				}

				this.sndSystem.setVolume(string8, volume);
				this.sndSystem.play(string8);
			}

		}
	}

	public void playSoundFX(String sound, float volume, float pitch) {
		if(this.loaded && this.options.b) {
			SoundPoolEntry soundPoolEntry4 = this.soundPoolSounds.getRandomSoundFromSoundPool(sound);
			if(soundPoolEntry4 != null) {
				this.playedSoundsCount = (this.playedSoundsCount + 1) % 256;
				String string5 = "sound_" + this.playedSoundsCount;
				this.sndSystem.newSource(false, string5, soundPoolEntry4.soundUrl, soundPoolEntry4.soundName, false, 0.0F, 0.0F, 0.0F, 0, 0.0F);
				if(volume > 1.0F) {
					volume = 1.0F;
				}

				volume *= 0.25F;
				this.sndSystem.setPitch(string5, pitch);
				this.sndSystem.setVolume(string5, volume);
				this.sndSystem.play(string5);
			}

		}
	}
}
