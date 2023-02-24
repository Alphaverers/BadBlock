package net.minecraft.src;

public class MobGiant extends EntityMob {
	public int maxHP = -1;

	public MobGiant(World world1, int i2) {
		super(world1);
		if(i2 <= 0) {
			i2 = 1;
		}

		this.texture = "/mob/collosal_a.png";
		this.moveSpeed = 0.5F;
		this.attackStrength = 50;
		this.health *= Math.min(4 * i2, 800);
		this.maxHP = this.health;
		this.yOffset *= 6.0F;
		this.setSize(this.width * 6.0F, this.height * 6.0F);
	}

	protected float getBlockPathWeight(int i1, int i2, int i3) {
		return this.worldObj.getBrightness(i1, i2, i3) - 0.5F;
	}

	protected void fall(float f1) {
	}

	protected String getLivingSound() {
		return "ext.giantambient";
	}

	protected String getHurtSound() {
		return "ext.gianthurt";
	}

	protected String getDeathSound() {
		return "ext.giantdead";
	}

	public void onDeath(Entity entity1) {
		super.onDeath(entity1);
		if(this.rand.nextInt(50) > 10) {
			this.dropItem(Item.flameberge.shiftedIndex, 1);
		}

	}
}
