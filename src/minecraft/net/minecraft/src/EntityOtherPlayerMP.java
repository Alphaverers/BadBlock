package net.minecraft.src;

public class EntityOtherPlayerMP extends EntityPlayer {
	private int otherPlayerMPPosRotationIncrements;
	private double otherPlayerMPX;
	private double otherPlayerMPY;
	private double otherPlayerMPZ;
	private double otherPlayerMPYaw;
	private double otherPlayerMPPitch;
	float unusedFloat = 0.0F;

	public EntityOtherPlayerMP(World worldObj, String username) {
		super(worldObj);
		this.username = username;
		this.yOffset = 0.0F;
		this.stepHeight = 0.0F;
		if(username != null && username.length() > 0) {
			boolean z3 = System.getProperty("os.name").toLowerCase().indexOf("windows") == -1;
			this.skinUrl = "file:///" + (z3 ? "." : "C:") + "/skincache/" + username + ".png";
			System.out.println("Loading texture " + this.skinUrl);
		}

		this.noClip = true;
		this.renderDistanceWeight = 10.0D;
	}

	public boolean attackEntityFrom(Entity entity1, int i2) {
		return true;
	}

	public void setPositionAndRotation(double d1, double d3, double d5, float f7, float f8, int i9) {
		this.yOffset = 0.0F;
		this.otherPlayerMPX = d1;
		this.otherPlayerMPY = d3;
		this.otherPlayerMPZ = d5;
		this.otherPlayerMPYaw = (double)f7;
		this.otherPlayerMPPitch = (double)f8;
		this.otherPlayerMPPosRotationIncrements = i9;
	}

	public void onUpdate() {
		super.onUpdate();
		this.prevLimbYaw = this.limbYaw;
		double d1 = this.posX - this.prevPosX;
		double d3 = this.posZ - this.prevPosZ;
		float f5 = MathHelper.sqrt_double(d1 * d1 + d3 * d3) * 4.0F;
		if(f5 > 1.0F) {
			f5 = 1.0F;
		}

		this.limbYaw += (f5 - this.limbYaw) * 0.4F;
		this.limbSwing += this.limbYaw;
	}

	public float getShadowSize() {
		return 0.0F;
	}

	public void onLivingUpdate() {
		super.updateEntityActionState();
		if(this.otherPlayerMPPosRotationIncrements > 0) {
			double d1 = this.posX + (this.otherPlayerMPX - this.posX) / (double)this.otherPlayerMPPosRotationIncrements;
			double d3 = this.posY + (this.otherPlayerMPY - this.posY) / (double)this.otherPlayerMPPosRotationIncrements;
			double d5 = this.posZ + (this.otherPlayerMPZ - this.posZ) / (double)this.otherPlayerMPPosRotationIncrements;

			double d7;
			for(d7 = this.otherPlayerMPYaw - (double)this.rotationYaw; d7 < -180.0D; d7 += 360.0D) {
			}

			while(d7 >= 180.0D) {
				d7 -= 360.0D;
			}

			this.rotationYaw += (float)(d7 / (double)this.otherPlayerMPPosRotationIncrements);
			this.rotationPitch += (float)((this.otherPlayerMPPitch - (double)this.rotationPitch) / (double)this.otherPlayerMPPosRotationIncrements);
			--this.otherPlayerMPPosRotationIncrements;
			this.setPosition(d1, d3, d5);
			this.setRotation(this.rotationYaw, this.rotationPitch);
		}

		this.prevCameraYaw = this.cameraYaw;
		float f9 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
		float f2 = (float)Math.atan(-this.motionY * (double)0.2F) * 15.0F;
		if(f9 > 0.1F) {
			f9 = 0.1F;
		}

		if(!this.onGround || this.health <= 0) {
			f9 = 0.0F;
		}

		if(this.onGround || this.health <= 0) {
			f2 = 0.0F;
		}

		this.cameraYaw += (f9 - this.cameraYaw) * 0.4F;
		this.cameraPitch += (f2 - this.cameraPitch) * 0.8F;
	}
}
