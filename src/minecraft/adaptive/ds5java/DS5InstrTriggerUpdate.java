package adaptive.ds5java;

public class DS5InstrTriggerUpdate implements DS5Instruction {
	public int controllerIndex;
	public DS5InstrTriggerUpdate.TriggerIndex triggerIndex;
	public DS5InstrTriggerUpdate.TriggerMode triggerMode;
	public int[] triggerModeVal;

	public DS5InstrTriggerUpdate(int i1, DS5InstrTriggerUpdate.TriggerIndex dS5InstrTriggerUpdate$TriggerIndex2, DS5InstrTriggerUpdate.TriggerMode dS5InstrTriggerUpdate$TriggerMode3, int... i4) {
		this.controllerIndex = i1;
		this.triggerIndex = dS5InstrTriggerUpdate$TriggerIndex2;
		this.triggerMode = dS5InstrTriggerUpdate$TriggerMode3;
		this.triggerModeVal = i4;
	}

	public String GetJSON() {
		String string1 = "{\"type\":1,\"parameters\":[" + this.controllerIndex + "," + this.triggerIndex.index + "," + this.triggerMode.index + ",";

		for(int i2 = 0; i2 != this.triggerModeVal.length; ++i2) {
			string1 = string1 + this.triggerModeVal[i2] + (i2 != this.triggerModeVal.length - 1 ? "," : "");
		}

		string1 = string1 + "]}";
		return string1;
	}

	public static enum CustomTriggerValueMode {
		OFF(0),
		Rigid(1),
		RigidA(2),
		RigidB(3),
		RigidAB(4),
		Pulse(5),
		PulseA(6),
		PulseB(7),
		PulseAB(8),
		VibrateResistance(9),
		VibrateResistanceA(10),
		VibrateResistanceB(11),
		VibrateResistanceAB(12),
		VibratePulse(13),
		VibratePulseA(14),
		VibratePulsB(15),
		VibratePulseAB(16);

		int index;

		private CustomTriggerValueMode(int i3) {
			this.index = i3;
		}
	}

	public static enum TriggerMode {
		Normal(0),
		GameCube(1),
		VerySoft(2),
		Soft(3),
		Hard(4),
		VeryHard(5),
		Hardest(6),
		Rigid(7),
		VibrateTrigger(8),
		Choppy(9),
		Medium(10),
		VibrateTriggerPulse(11),
		CustomTriggerValue(12),
		Resistance(13),
		Bow(14),
		Galloping(15),
		SemiAutomaticGun(16),
		AutomaticGun(17),
		Machine(18);

		int index;

		private TriggerMode(int i3) {
			this.index = i3;
		}
	}

	public static enum TriggerIndex {
		Invalid(0),
		Left(1),
		Right(2);

		int index;

		private TriggerIndex(int i3) {
			this.index = i3;
		}
	}
}
