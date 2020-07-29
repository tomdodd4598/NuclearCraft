package nc.capability.radiation.entity;

import static nc.config.NCConfig.max_player_rads;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

public class PlayerRads implements IEntityRads {
	
	protected double maxRads = 0D;
	protected double totalRads = 0D;
	protected double radiationLevel = 0D;
	protected double internalRadiationResistance = 0D, externalRadiationResistance = 0D;
	protected boolean radXUsed = false;
	protected boolean radXWoreOff = false;
	protected double radawayBuffer = 0D, radawayBufferSlow = 0D;
	protected double poisonBuffer = 0D;
	protected boolean consumed = false;
	protected double radawayCooldown = 0D;
	protected double recentRadawayAddition = 0D;
	protected double radXCooldown = 0D;
	protected double recentRadXAddition = 0D;
	protected int messageCooldownTime = 0;
	protected double recentPoisonAddition = 0D;
	protected double radiationImmunityTime = 0D;
	protected boolean radiationImmunityStage = false;
	protected boolean shouldWarn = false;
	protected boolean giveGuidebook = true;
	
	public PlayerRads() {
		maxRads = max_player_rads;
	}
	
	@Override
	public NBTTagCompound writeNBT(IEntityRads instance, EnumFacing side, NBTTagCompound nbt) {
		nbt.setDouble("totalRads", totalRads);
		nbt.setDouble("radiationLevel", radiationLevel);
		nbt.setDouble("internalRadiationResistance", internalRadiationResistance);
		nbt.setDouble("externalRadiationResistance", externalRadiationResistance);
		nbt.setBoolean("radXUsed", radXUsed);
		nbt.setBoolean("radXWoreOff", radXWoreOff);
		nbt.setDouble("radawayBuffer", radawayBuffer);
		nbt.setDouble("radawayBufferSlow", radawayBufferSlow);
		nbt.setDouble("poisonBuffer", poisonBuffer);
		nbt.setBoolean("consumed", consumed);
		nbt.setDouble("radawayCooldown", radawayCooldown);
		nbt.setDouble("recentRadawayAddition", recentRadawayAddition);
		nbt.setDouble("radXCooldown", radXCooldown);
		nbt.setDouble("recentRadXAddition", recentRadXAddition);
		nbt.setInteger("messageCooldownTime", messageCooldownTime);
		nbt.setDouble("recentPoisonAddition", recentPoisonAddition);
		nbt.setDouble("radiationImmunityTime", radiationImmunityTime);
		nbt.setBoolean("radiationImmunityStage", radiationImmunityStage);
		nbt.setBoolean("shouldWarn", shouldWarn);
		nbt.setBoolean("giveGuidebook", giveGuidebook);
		return nbt;
	}
	
	@Override
	public void readNBT(IEntityRads instance, EnumFacing side, NBTTagCompound nbt) {
		totalRads = nbt.getDouble("totalRads");
		radiationLevel = nbt.getDouble("radiationLevel");
		internalRadiationResistance = nbt.getDouble("internalRadiationResistance");
		externalRadiationResistance = nbt.getDouble("externalRadiationResistance");
		radXUsed = nbt.getBoolean("radXUsed");
		radXWoreOff = nbt.getBoolean("radXWoreOff");
		radawayBuffer = nbt.getDouble("radawayBuffer");
		radawayBufferSlow = nbt.getDouble("radawayBufferSlow");
		poisonBuffer = nbt.getDouble("poisonBuffer");
		consumed = nbt.getBoolean("consumed");
		radawayCooldown = nbt.getDouble("radawayCooldown");
		recentRadawayAddition = nbt.getDouble("recentRadawayAddition");
		radXCooldown = nbt.getDouble("radXCooldown");
		recentRadXAddition = nbt.getDouble("recentRadXAddition");
		messageCooldownTime = nbt.getInteger("messageCooldownTime");
		recentPoisonAddition = nbt.getDouble("recentPoisonAddition");
		radiationImmunityTime = nbt.getDouble("radiationImmunityTime");
		radiationImmunityStage = nbt.getBoolean("radiationImmunityStage");
		shouldWarn = nbt.getBoolean("shouldWarn");
		if (nbt.hasKey("giveGuidebook")) {
			giveGuidebook = nbt.getBoolean("giveGuidebook");
		}
	}
	
	@Override
	public double getTotalRads() {
		return totalRads;
	}
	
	@Override
	public void setTotalRads(double newTotalRads, boolean useImmunity) {
		if (!useImmunity || !isImmune()) {
			totalRads = MathHelper.clamp(newTotalRads, 0D, maxRads);
		}
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
	public double getInternalRadiationResistance() {
		return internalRadiationResistance;
	}
	
	@Override
	public void setInternalRadiationResistance(double newInternalRadiationResistance) {
		internalRadiationResistance = newInternalRadiationResistance;
	}
	
	@Override
	public double getExternalRadiationResistance() {
		return externalRadiationResistance;
	}
	
	@Override
	public void setExternalRadiationResistance(double newExternalRadiationResistance) {
		externalRadiationResistance = Math.max(newExternalRadiationResistance, 0D);
	}
	
	@Override
	public boolean getRadXUsed() {
		return radXUsed;
	}
	
	@Override
	public void setRadXUsed(boolean radXUsed) {
		this.radXUsed = radXUsed;
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
		if (slow) {
			radawayBufferSlow = Math.max(newBuffer, 0D);
		}
		else {
			radawayBuffer = Math.max(newBuffer, 0D);
		}
	}
	
	@Override
	public double getPoisonBuffer() {
		return poisonBuffer;
	}
	
	@Override
	public void setPoisonBuffer(double newBuffer) {
		poisonBuffer = Math.max(newBuffer, 0D);
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
	public double getRecentRadawayAddition() {
		return recentRadawayAddition;
	}
	
	@Override
	public void setRecentRadawayAddition(double newRecentRadawayAddition) {
		recentRadawayAddition = Math.max(recentRadawayAddition, newRecentRadawayAddition);
	}
	
	@Override
	public void resetRecentRadawayAddition() {
		recentRadawayAddition = 0D;
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
	public double getRecentRadXAddition() {
		return recentRadXAddition;
	}
	
	@Override
	public void setRecentRadXAddition(double newRecentRadXAddition) {
		recentRadXAddition = Math.max(recentRadXAddition, newRecentRadXAddition);
	}
	
	@Override
	public void resetRecentRadXAddition() {
		recentRadXAddition = 0D;
	}
	
	@Override
	public int getMessageCooldownTime() {
		return messageCooldownTime;
	}
	
	@Override
	public void setMessageCooldownTime(int messageTime) {
		messageCooldownTime = Math.max(messageTime, 0);
	}
	
	@Override
	public double getRecentPoisonAddition() {
		return recentPoisonAddition;
	}
	
	@Override
	public void setRecentPoisonAddition(double newRecentPoisonAddition) {
		recentPoisonAddition = Math.max(recentPoisonAddition, newRecentPoisonAddition);
	}
	
	@Override
	public void resetRecentPoisonAddition() {
		recentPoisonAddition = 0D;
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
	public boolean getRadiationImmunityStage() {
		return radiationImmunityStage;
	}
	
	@Override
	public void setRadiationImmunityStage(boolean newRadiationImmunityStage) {
		radiationImmunityStage = newRadiationImmunityStage;
	}
	
	@Override
	public boolean getShouldWarn() {
		return shouldWarn;
	}
	
	@Override
	public void setShouldWarn(boolean shouldWarn) {
		this.shouldWarn = shouldWarn;
	}
	
	@Override
	public boolean getGiveGuidebook() {
		return giveGuidebook;
	}
	
	@Override
	public void setGiveGuidebook(boolean giveGuidebook) {
		this.giveGuidebook = giveGuidebook;
	}
}
