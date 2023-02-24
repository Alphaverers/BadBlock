package net.minecraft.src;

class LogoEffectRandomizer {
	public double height;
	public double prevHeight;
	public double dropSpeed;
	final GuiMainMenu mainMenu;

	public LogoEffectRandomizer(GuiMainMenu mainMenu, int i2, int i3) {
		this.mainMenu = mainMenu;
		this.height = this.prevHeight = (double)(10 + i3) + GuiMainMenu.getRandom().nextDouble() * 32.0D + (double)i2;
	}

	public void updateLogoEffects() {
		this.prevHeight = this.height;
		if(this.height > 0.0D) {
			this.dropSpeed -= 0.6D;
		}

		this.height += this.dropSpeed;
		this.dropSpeed *= 0.9D;
		if(this.height < 0.0D) {
			this.height = 0.0D;
			this.dropSpeed = 0.0D;
		}

	}
}
