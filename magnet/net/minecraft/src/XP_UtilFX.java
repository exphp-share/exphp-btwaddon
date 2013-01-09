package net.minecraft.src;

import java.util.Random;

import net.minecraft.client.Minecraft;
import java.util.Random;

public class XP_UtilFX {
	
	private static Minecraft __mc = ModLoader.getMinecraftInstance();
	private static Random __rand = new Random();
	
	private static final double __renderDistanceSq = 256.0D;
	
	// Spawns and returns a new XP_EntityMagnetSparkleFX.
	// Methods on the returned object can be called for further initialization, but note that the returned object may be null.
	public static XP_EntityMagnetSparkleFX spawnMagBeamParticle(double x, double y, double z, double velX, double velY, double velZ, float scale) {
		XP_EntityMagnetSparkleFX particle = null;
		if (!__isParticleSuppressed(x, y, z)) {

			particle = new XP_EntityMagnetSparkleFX(__mc.theWorld, x, y, z, velX, velY, velZ, scale);
			particle.setRBGColorF(0.25F, 0.375F, 0.5F);

			__mc.effectRenderer.addEffect(particle);
		}
		return particle;
	}
	
	// Spawns and returns a new XP_EntityMagnetBeamFX.
	// Methods on the returned object can be called for further initialization, but note that the returned object may be null.
	public static XP_EntityMagnetBeamFX spawnMagBeamCircleParticle(double x, double y, double z, double r, float theta, float omega, float scale) {
		XP_EntityMagnetBeamFX particle = null;
		if (!__isParticleSuppressed(x, y, z)) {

			particle = new XP_EntityMagnetBeamFX(__mc.theWorld, x, y, z, r, theta, omega, scale);
			particle.setRBGColorF(0.25F, 0.375F, 0.5F);

			__mc.effectRenderer.addEffect(particle);
		}
		return particle;
	}
	
	// Pulled out to a method because every particle spawning method will need this.
	private static boolean __isParticleSuppressed(double x, double y, double z) {
		return __isParticleSuppressedBecauseNPEsAreBad() || 
		       __isParticleSuppressedByGameSettings() ||
		       __isParticleSuppressedByDistance(x, y, z);
	}
	
	private static boolean __isParticleSuppressedBecauseNPEsAreBad() {
		return (__mc == null) || (__mc.renderViewEntity == null) || (__mc.effectRenderer == null);
	}
	
	private static boolean __isParticleSuppressedByGameSettings() {
		switch (__mc.gameSettings.particleSetting) {
		case 0:
			return false;
		case 1:
			return __rand.nextInt(3) == 0;
		default:
			return true;
		}
	}
	
	private static boolean __isParticleSuppressedByDistance(double x, double y, double z) {
		return __mc.renderViewEntity.getDistanceSq(x, y, z) > __renderDistanceSq;
	}
	
}
