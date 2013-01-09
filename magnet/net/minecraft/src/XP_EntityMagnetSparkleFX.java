package net.minecraft.src;

// A motionless twinkle FX
public class XP_EntityMagnetSparkleFX extends EntitySmokeFX {

	public XP_EntityMagnetSparkleFX(World world, double x, double y, double z, double velX, double velY, double velZ, float scale) {
		super(world, x, y, z, velX, velY, velZ, scale);
		
		// EntityFX's constructor randomizes the motion a bit, so let's fix it.
		this.motionX = velX;
		this.motionY = velY;
		this.motionZ = velZ;
		this.noClip  = true;
	}

	public XP_EntityMagnetSparkleFX(World world, double x, double y, double z, double velX, double velY, double velZ) {
		this(world, x, y, z, velX, velY, velZ, 1.0F);
	}
	
	@Override // EntitySmokeFX
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.particleAge++ >= this.particleMaxAge) {
			this.setDead();
		}

		this.setParticleTextureIndex(0x94 - Math.abs(4 - this.particleAge * 7 / this.particleMaxAge));
		this.moveEntity(this.motionX, this.motionY, this.motionZ);

		if (this.posY == this.prevPosY) {
			this.motionY *= -0.8D;
		}

		this.motionX *= 0.9599999785423279D;
		this.motionZ *= 0.9599999785423279D;
	}
}
