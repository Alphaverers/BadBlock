package net.minecraft.src;

public class EntityGiantZombie extends EntityMobs {
	public EntityGiantZombie(World world1) {
		super(world1);
		this.texture = "/mob/zombie.png";
		this.moveSpeed = 0.5F;
		this.attackStrength = 50;
		this.health *= 10;
		this.yOffset *= 6.0F;
		this.setSize(this.width * 6.0F, this.height * 6.0F);
	}

	protected float getBlockPathWeight(int i1, int i2, int i3) {
		return this.worldObj.getBrightness(i1, i2, i3) - 0.5F;
	}
}
