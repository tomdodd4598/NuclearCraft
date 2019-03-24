package nc.capability.radiation.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

public class EntityRads implements IEntityRads {
	
	private final double maxRads;
	
	private double totalRads = 0D;
	private double radiationLevel = 0D;
	private double radiationResistance = 0D;
	private boolean radXWoreOff = false;
	private double radawayBuffer = 0D;
	private boolean consumed = false;
	private double radawayCooldown = 0D;
	private double radXCooldown = 0D;
	private double radiationImmunityTime = 0D;
	
	public EntityRads(double maxRads) {
		this.maxRads = maxRads;
	}

	@Override
	public NBTTagCompound writeNBT(IEntityRads instance, EnumFacing side, NBTTagCompound nbt) {
		nbt.setDouble("totalRads", getTotalRads());
		nbt.setDouble("radiationLevel", getRadiationLevel());
		nbt.setDouble("radiationResistance", getRadiationResistance());
		nbt.setBoolean("radXWoreOff", getRadXWoreOff());
		nbt.setDouble("radawayCooldown", getRadawayCooldown());
		nbt.setDouble("radXCooldown", getRadXCooldown());
		nbt.setDouble("radiationImmunityTime", getRadiationImmunityTime());
		return nbt;
	}

	@Override
	public void readNBT(IEntityRads instance, EnumFacing side, NBTTagCompound nbt) {
		setTotalRads(nbt.getDouble("totalRads"), false);
		setRadiationLevel(nbt.getDouble("radiationLevel"));
		setRadiationResistance(nbt.getDouble("radiationResistance"));
		setRadXWoreOff(nbt.getBoolean("radXWoreOff"));
		setRadawayBuffer(nbt.getDouble("radawayBuffer"));
		setRadawayCooldown(nbt.getDouble("radawayCooldown"));
		setRadXCooldown(nbt.getDouble("radXCooldown"));
		setRadiationImmunityTime(nbt.getDouble("radiationImmunityTime"));
	}
	
	@Override
	public double getTotalRads() {
		return totalRads;
	}

	@Override
	public void setTotalRads(double newTotalRads, boolean useImmunity) {
		if (!useImmunity || getRadiationImmunityTime() <= 0D) totalRads = MathHelper.clamp(newTotalRads, 0D, maxRads);
	}
	
	@Override
	public double getMaxRads() {
		return maxRads;
	}

	@Override
	public double getRadiationLevel() {
		return radiationLevel;
	}

	@Override
	public void setRadiationLevel(double newRadiationLevel) {
		radiationLevel = Math.max(newRadiationLevel, 0D);
	}
	
	@Override
	public double getRadiationResistance() {
		return radiationResistance;
	}

	@Override
	public void setRadiationResistance(double newRadiationResistance) {
		radiationResistance = Math.max(newRadiationResistance, 0D);
	}
	
	@Override
	public boolean getRadXWoreOff() {
		return radXWoreOff;
	}
	
	@Override
	public void setRadXWoreOff(boolean radXWoreOff) {
		this.radXWoreOff = radXWoreOff;
	}
	
	@Override
	public double getRadawayBuffer() {
		return radawayBuffer;
	}
	
	@Override
	public void setRadawayBuffer(double newBuffer) {
		radawayBuffer = Math.max(newBuffer, 0D);
	}

	@Override
	public boolean getConsumedMedicine() {
		return consumed;
	}

	@Override
	public void setConsumedMedicine(boolean consumed) {
		this.consumed = consumed;
	}
	
	@Override
	public double getRadawayCooldown() {
		return radawayCooldown;
	}

	@Override
	public void setRadawayCooldown(double cooldown) {
		radawayCooldown = Math.max(cooldown, 0D);
	}
	
	@Override
	public boolean canConsumeRadaway() {
		return !consumed && radawayCooldown <= 0D;
	}
	
	@Override
	public double getRadXCooldown() {
		return radXCooldown;
	}

	@Override
	public void setRadXCooldown(double cooldown) {
		radXCooldown = Math.max(cooldown, 0D);
	}
	
	@Override
	public boolean canConsumeRadX() {
		return !consumed && radXCooldown <= 0D;
	}
	
	@Override
	public double getRadiationImmunityTime() {
		return radiationImmunityTime;
	}

	@Override
	public void setRadiationImmunityTime(double newRadiationImmunityTime) {
		radiationImmunityTime = Math.max(newRadiationImmunityTime, 0D);
	}
}
