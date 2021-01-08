package nc.capability.radiation.entity;

import static nc.config.NCConfig.radiation_lowest_rate;

import nc.Global;
import nc.capability.ICapability;
import nc.capability.radiation.IRadiation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.*;

public interface IEntityRads extends IRadiation, ICapability<IEntityRads> {
	
	@CapabilityInject(IEntityRads.class)
	public static Capability<IEntityRads> CAPABILITY_ENTITY_RADS = null;
	
	public static final ResourceLocation CAPABILITY_ENTITY_RADS_NAME = new ResourceLocation(Global.MOD_ID, "capability_entity_rads");
	
	public double getTotalRads();
	
	public void setTotalRads(double newTotalRads, boolean useImmunity);
	
	public default boolean isTotalRadsNegligible() {
		return getTotalRads() < radiation_lowest_rate;
	}
	
	public double getMaxRads();
	
	public default double getRadsPercentage() {
		return Math.min(100D, 100D * getTotalRads() / getMaxRads());
	}
	
	public default double getRawRadiationLevel() {
		return getFullRadiationResistance() > 0D ? 0.5D * (getRadiationLevel() + Math.sqrt(getRadiationLevel() * (getRadiationLevel() + 4D * getFullRadiationResistance()))) : getFullRadiationResistance() < 0D ? getRadiationLevel() / (1D - getFullRadiationResistance()) : getRadiationLevel();
	}
	
	public default boolean isRawRadiationNegligible() {
		return getRawRadiationLevel() < radiation_lowest_rate;
	}
	
	public double getInternalRadiationResistance();
	
	public void setInternalRadiationResistance(double newInternalRadiationResistance);
	
	public double getExternalRadiationResistance();
	
	public void setExternalRadiationResistance(double newExternalRadiationResistance);
	
	public default double getFullRadiationResistance() {
		return getInternalRadiationResistance() + getExternalRadiationResistance();
	}
	
	public boolean getRadXUsed();
	
	public void setRadXUsed(boolean radXUsed);
	
	public boolean getRadXWoreOff();
	
	public void setRadXWoreOff(boolean radXWoreOff);
	
	public double getRadawayBuffer(boolean slow);
	
	public void setRadawayBuffer(boolean slow, double newCount);
	
	public double getPoisonBuffer();
	
	public void setPoisonBuffer(double newBuffer);
	
	public default boolean isFatal() {
		return getTotalRads() >= getMaxRads();
	}
	
	public boolean getConsumedMedicine();
	
	public void setConsumedMedicine(boolean consumed);
	
	public double getRadawayCooldown();
	
	public void setRadawayCooldown(double cooldown);
	
	public boolean canConsumeRadaway();
	
	public double getRecentRadawayAddition();
	
	public void setRecentRadawayAddition(double newRecentRadawayAddition);
	
	public void resetRecentRadawayAddition();
	
	public double getRadXCooldown();
	
	public void setRadXCooldown(double cooldown);
	
	public boolean canConsumeRadX();
	
	public double getRecentRadXAddition();
	
	public void setRecentRadXAddition(double newRecentRadXAddition);
	
	public void resetRecentRadXAddition();
	
	public int getMessageCooldownTime();
	
	public void setMessageCooldownTime(int messageTime);
	
	public double getRecentPoisonAddition();
	
	public void setRecentPoisonAddition(double newRecentPoisonAddition);
	
	public void resetRecentPoisonAddition();
	
	public double getRadiationImmunityTime();
	
	public void setRadiationImmunityTime(double newRadiationImmunityTime);
	
	public boolean getRadiationImmunityStage();
	
	public void setRadiationImmunityStage(boolean newRadiationImmunityStage);
	
	public default boolean isImmune() {
		return getRadiationImmunityStage() || getRadiationImmunityTime() > 0D;
	}
	
	public boolean getShouldWarn();
	
	public void setShouldWarn(boolean shouldWarn);
	
	public boolean getGiveGuidebook();
	
	public void setGiveGuidebook(boolean giveGuidebook);
}
