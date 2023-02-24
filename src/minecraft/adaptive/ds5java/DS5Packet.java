package adaptive.ds5java;

import java.util.ArrayList;
import java.util.List;

public class DS5Packet {
	public List ds5Instructions = new ArrayList();

	public void AddInstruction(DS5Instruction dS5Instruction1) {
		this.ds5Instructions.add(dS5Instruction1);
	}

	public String buildJSON() {
		String string1 = "{\"instructions\":[";

		for(int i2 = 0; i2 != this.ds5Instructions.size(); ++i2) {
			string1 = string1 + ((DS5Instruction)this.ds5Instructions.get(i2)).GetJSON();
			if(i2 != this.ds5Instructions.size() - 1) {
				string1 = string1 + ",";
			}
		}

		string1 = string1 + "]}";
		return string1;
	}
}
