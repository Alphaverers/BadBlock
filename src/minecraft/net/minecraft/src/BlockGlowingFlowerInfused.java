package net.minecraft.src;

public class BlockGlowingFlowerInfused extends BlockGlowing {
	public int power = 0;

	protected BlockGlowingFlowerInfused(int i1, int i2, int i3, int i4) {
		super(i1, i2, i3);
		this.power = i4;
	}

	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if(entity instanceof EntityMob) {
			((EntityMob)entity).attackEntityFrom((Entity)null, 2 * this.power);
			((EntityMob)entity).fire = 300;
		}

	}
}
