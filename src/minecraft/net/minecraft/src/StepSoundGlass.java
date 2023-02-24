package net.minecraft.src;

final class StepSoundGlass extends StepSound {
	StepSoundGlass(String string1, float f2, float f3) {
		super(string1, f2, f3);
	}

	public String getBreakSound() {
		return "random.glass";
	}
}
