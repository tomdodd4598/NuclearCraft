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
	private double radawayBuffer = 0D, radawayBufferSlow = 0D;
	private double poisonBuffer = 0D;
	private boolean consumed = false;
	private double radawayCooldown = 0D;
	private double radXCooldown = 0D;
	private double radiationImmunityTime = 0D;
	private boolean shouldWarn = false;
	
	public EntityRads(double maxRads) {
		this.maxRads = maxRads;
	}

	@Override
	public NBTTagCompound writeNBT(IEntityRads instance, EnumFacing side, NBTTagCompound nbt) {
		nbt.setDouble("totalRads", totalRads);
		nbt.setDouble("radiationLevel", radiationLevel);
		nbt.setDouble("radiationResistance", radiationResistance);
		nbt.setBoolean("radXWoreOff", radXWoreOff);
		nbt.setDouble("radawayBuffer", radawayBuffer);
		nbt.setDouble("radawayBufferSlow", radawayBufferSlow);
		nbt.setDouble("poisonBuffer", poisonBuffer);
		nbt.setBoolean("consumed", consumed);
		nbt.setDouble("radawayCooldown", radawayCooldown);
		nbt.setDouble("radXCooldown", radXCooldown);
		nbt.setDouble("radiationImmunityTime", radiationImmunityTime);
		nbt.setBoolean("shouldWarn", shouldWarn);
		return nbt;
	}

	@Override
	public void readNBT(IEntityRads instance, EnumFacing side, NBTTagCompound nbt) {
		totalRads = nbt.getDouble("totalRads");
		radiationLevel = nbt.getDouble("radiationLevel");
		radiationResistance = nbt.getDouble("radiationResistance");
		radXWoreOff = nbt.getBoolean("radXWoreOff");
		radawayBuffer = nbt.getDouble("radawayBuffer");
		radawayBufferSlow = nbt.getDouble("radawayBufferSlow");
		poisonBuffer = nbt.getDouble("poisonBuffer");
		consumed = nbt.getBoolean("consumed");
		radawayCooldown = nbt.getDouble("radawayCooldown");
		radXCooldown = nbt.getDouble("radXCooldown");
		radiationImmunityTime = nbt.getDouble("radiationImmunityTime");
		shouldWarn = nbt.getBoolean("shouldWarn");
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
	public double getRadawayBuffer(boolean slow) {
		return slow ? radawayBufferSlow : radawayBuffer;
	}
	
	@Override
	public void setRadawayBuffer(boolean slow, double newBuffer) {
		if (slow) radawayBufferSlow = Math.max(newBuffer, 0D);
		else radawayBuffer = Math.max(newBuffer, 0D);
	}
	
	@Override
	public double getPoisonBuffer() {
		return poisonBuffer;
	}
	
	@Override
	public void setPoisonBuffer(double newBuffer, double maxLevel) {
		poisonBuffer = MathHelper.clamp(newBuffer, 0D, maxLevel);
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
	
	@Override
	public boolean getShouldWarn() {
		return shouldWarn;
	}
	
	@Override
	public void setShouldWarn(boolean shouldWarn) {
		this.shouldWarn = shouldWarn;
	}
}
