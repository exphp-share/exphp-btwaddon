package net.minecraft.src;

// Particle FX that travels in a lateral circle with a fixed angular motion
public class XP_EntityMagnetBeamFX extends EntitySmokeFX {
	
	public double centerX;
	public double centerY;
	public double centerZ;
	public double theta;
	public double omega;
	public double radius;

	// Time Factors; a value <= 1 that a speed is multiplied by every tick (to slow it down)
	public double tfVelY;
	public double tfOmega;
	
	public XP_EntityMagnetBeamFX(World world, double centerX, double centerY, double centerZ, double radius, float theta, float omega, float scale) {
		super(world, centerX + Math.cos(theta) * radius, centerY, centerZ + Math.sin(theta) * radius, 0.0D, 0.0D, 0.0D, scale);
		
		this.centerX = centerX;
		this.centerY = centerY;
		this.centerZ = centerZ;
		this.radius  = radius;
		this.theta   = theta;
		this.omega   = omega;
		this.tfVelY  = 1.00D;
		this.tfOmega = 0.94D;
	}
	
	public void setPathRadius(double r) {
		this.radius = r;
		
		// Update position immediately so that prevPos uses the new radius.
		this.posX   = centerX + (r * Math.cos(theta));
		this.posY   = centerY;
		this.posZ   = centerZ + (r * Math.sin(theta));
	}
	
	public void setMotionY(double velY) {
		this.motionY = velY;
	}
	
	public void setMotionYSlowdown(double slowFactor) {
		this.tfVelY = slowFactor;
	}
	
	public void setOmegaSlowdown(double slowFactor) {
		this.tfOmega = slowFactor;
	}
	
	@Override // EntitySmokeFX
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		
		if (this.particleAge++ >= this.particleMaxAge) {
			this.setDead();
		}
		
		// Move along the circle and find new X, Z
		this.theta += this.omega;
		
		double nextPosX = this.centerX + (Math.cos(theta) * radius);
		double nextPosZ = this.centerZ + (Math.sin(theta) * radius);
		
		// "Magic equation" to do a 7-frame sparkle animation using the first 4 frames of particles.png, line #10.
		this.setParticleTextureIndex(0x94 - Math.abs(4 - this.particleAge * 7 / this.particleMaxAge));
		
		// Move with collision checking.
		this.moveEntity(nextPosX - this.posX, this.motionY, nextPosZ - this.posZ);

		// Bounce against floor
		if (this.posY == this.prevPosY) {
			this.motionY *= -0.8D;
		}
		
		// Slow down motion or rotation speed, if applicable.
		this.motionY *= this.tfVelY;
		this.omega   *= this.tfOmega;
	}
}
