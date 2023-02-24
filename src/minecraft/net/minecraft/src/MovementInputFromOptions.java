package net.minecraft.src;

public class MovementInputFromOptions extends MovementInput {
	private boolean[] movementKeyStates;
	private GameSettings gameSettings;

	public MovementInputFromOptions(GameSettings gameSettings) {
		InputHandler.CaptureGamepad();
		this.movementKeyStates = new boolean[10];
		this.gameSettings = gameSettings;
	}

	public void checkKeyForMovementInput(int key, boolean state) {
		byte b3 = -1;
		if(key == this.gameSettings.keyBindForward.keyCode) {
			b3 = 0;
		}

		if(key == this.gameSettings.keyBindBack.keyCode) {
			b3 = 1;
		}

		if(key == this.gameSettings.keyBindLeft.keyCode) {
			b3 = 2;
		}

		if(key == this.gameSettings.keyBindRight.keyCode) {
			b3 = 3;
		}

		if(key == this.gameSettings.keyBindJump.keyCode) {
			b3 = 4;
		}

		if(b3 >= 0) {
			this.movementKeyStates[b3] = state;
		}

	}

	public void resetKeyState() {
		for(int i1 = 0; i1 < 10; ++i1) {
			this.movementKeyStates[i1] = false;
		}

	}

	public void updatePlayerMoveState(EntityPlayer entityPlayer1) {
		this.moveStrafe = 0.0F;
		this.moveForward = 0.0F;
		this.jump = this.movementKeyStates[4];
		if(InputHandler.gamepads != null) {
			for(int i2 = 0; i2 != InputHandler.gamepads.length; ++i2) {
				if(InputHandler.gamepads[i2] != null && (InputHandler.gamepads[i2].getXAxisValue() != -1.0F || InputHandler.gamepads[i2].getYAxisValue() != -1.0F)) {
					this.moveStrafe = -InputHandler.gamepads[i2].getXAxisValue();
					if((double)this.moveStrafe > -0.15D && (double)this.moveStrafe < 0.15D) {
						this.moveStrafe = 0.0F;
					}

					this.moveForward = -InputHandler.gamepads[i2].getYAxisValue();
					if((double)this.moveForward > -0.15D && (double)this.moveForward < 0.15D) {
						this.moveForward = 0.0F;
					}

					if(this.moveStrafe != 0.0F || this.moveForward != 0.0F) {
						return;
					}
				}
			}
		}

		if(this.movementKeyStates[0]) {
			++this.moveForward;
		}

		if(this.movementKeyStates[1]) {
			--this.moveForward;
		}

		if(this.movementKeyStates[2]) {
			++this.moveStrafe;
		}

		if(this.movementKeyStates[3]) {
			--this.moveStrafe;
		}

	}
}
