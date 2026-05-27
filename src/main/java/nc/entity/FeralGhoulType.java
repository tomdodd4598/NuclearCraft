package nc.entity;

import static nc.config.NCConfig.*;

public enum FeralGhoulType {
	
	STANDARD(0, 16D, 4D, 2D, 0.5D, 0.5D, 16D, radiation_feral_ghoul_attack, true, 16F, 1F),
	GLOWING(1, 32D, 8D, 4D, 0.5D, 0.75D, 32D, radiation_glowing_ghoul_attack, false, 32F, 0F);
	
	public final int id;
	public final double health;
	public final double armor;
	public final double damage;
	public final double speed;
	public final double knockbackResistance;
	public final double followRange;
	public final double attackRadiation;
	public final boolean canLeap;
	public final float watchDistance;
	public final float baseSoundPitch;
	
	FeralGhoulType(int id, double health, double armor, double damage, double speed, double knockbackResistance, double followRange, double attackRadiation, boolean canLeap, float watchDistance, float baseSoundPitch) {
		this.id = id;
		this.health = health;
		this.armor = armor;
		this.damage = damage;
		this.speed = speed;
		this.knockbackResistance = knockbackResistance;
		this.followRange = followRange;
		this.attackRadiation = attackRadiation;
		this.canLeap = canLeap;
		this.watchDistance = watchDistance;
		this.baseSoundPitch = baseSoundPitch;
	}
}
