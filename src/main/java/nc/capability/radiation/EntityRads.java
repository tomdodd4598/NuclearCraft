package nc.capability.radiation;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

public class EntityRads implements IEntityRads {
	
	private final double maxRads;
	
	private double totalRads = 0D;
	private double radiationLevel = 0D;
	private double radiationResistance = 0D;
	private boolean radXWoreOff = false;
	private double radawayBuffer = 0;
	private boolean consumed;
	private double cooldown;
	
	public EntityRads(double maxRads) {
		this.maxRads = maxRads;
	}

	@Override
	public NBTTagCompound writeNBT(IEntityRads instance, EnumFacing side, NBTTagCompound nbt) {
		nbt.setDouble("totalRads", getTotalRads());
		nbt.setDouble("radiationLevel", getRadiationLevel());
		nbt.setDouble("radiationResistance", getRadiationResistance());
		nbt.setBoolean("radXWoreOff", getRadXWoreOff());
		nbt.setDouble("radawayBuffer", getRadawayBuffer());
		return nbt;
	}

	@Override
	public void readNBT(IEntityRads instance, EnumFacing side, NBTTagCompound nbt) {
		setTotalRads(nbt.getDouble("totalRads"));
		setRadiationLevel(nbt.getDouble("radiationLevel"));
		setRadiationResistance(nbt.getDouble("radiationResistance"));
		setRadXWoreOff(nbt.getBoolean("radXWoreOff"));
		setRadawayBuffer(nbt.getDouble("radawayBuffer"));
	}

	@Override
	public double getTotalRads() {
		return totalRads;
	}

	@Override
	public void setTotalRads(double newTotalRads) {
		totalRads = MathHelper.clamp(newTotalRads, 0D, maxRads);
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
	public double getMedicineCooldown() {
		return cooldown;
	}

	@Override
	public void setMedicineCooldown(double cooldown) {
		this.cooldown = Math.max(cooldown, 0D);
	}
	
	@Override
	public boolean canConsumeMedicine() {
		return !consumed && cooldown <= 0D;
	}
}
