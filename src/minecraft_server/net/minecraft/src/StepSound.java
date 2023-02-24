package net.minecraft.src;

public class StepSound {
	public final String stepSoundName;
	public final float stepSoundVolume;
	public final float stepSoundPitch;

	public StepSound(String string1, float f2, float f3) {
		this.stepSoundName = string1;
		this.stepSoundVolume = f2;
		this.stepSoundPitch = f3;
	}

	public float getVolume() {
		return this.stepSoundVolume;
	}

	public float getPitch() {
		return this.stepSoundPitch;
	}

	public String getStepSound() {
		return "step." + this.stepSoundName;
	}
}
