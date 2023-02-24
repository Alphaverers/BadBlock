package adaptive.ds5java;

public class DS5InstrPlayerUpdate implements DS5Instruction {
	public int controllerIndex;
	public boolean p1;
	public boolean p2;
	public boolean p3;
	public boolean p4;
	public boolean p5 = false;

	public DS5InstrPlayerUpdate(int i1, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6) {
		this.controllerIndex = i1;
		this.p1 = z2;
		this.p2 = z3;
		this.p3 = z4;
		this.p4 = z5;
		this.p5 = z6;
	}

	public String GetJSON() {
		return "{\"type\":3,\"parameters\":[" + this.controllerIndex + "," + this.p1 + "," + this.p2 + "," + this.p3 + "," + this.p4 + "," + this.p5 + "]}";
	}
}
