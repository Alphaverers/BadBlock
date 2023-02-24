package net.minecraft.src;

import java.util.Random;

public abstract class WorldGenerator {
	public abstract boolean generate(World world1, Random random2, int i3, int i4, int i5);

	public void setScale(double scaleX, double scaleY, double scaleZ) {
	}
}
