package adaptive.ds5java;

public class DS5InstrRGBUpdate implements DS5Instruction {
	public int r;
	public int g;
	public int b;
	public int controllerIndex;

	public DS5InstrRGBUpdate(int i1, int i2, int i3, int i4) {
		this.r = i2;
		this.g = i3;
		this.b = i4;
		this.controllerIndex = i1;
	}

	public String GetJSON() {
		return "{\"type\":2,\"parameters\":[" + this.controllerIndex + "," + this.r + "," + this.g + "," + this.b + "]}";
	}
}
