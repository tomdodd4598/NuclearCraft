package nc.capability.radiation.entity;

import static nc.config.NCConfig.radiation_lowest_rate;

import nc.Global;
import nc.capability.ICapability;
import nc.capability.radiation.IRadiation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.*;

public interface IEntityRads extends IRadiation, ICapability<IEntityRads> {
	
	@CapabilityInject(IEntityRads.class)
    Capability<IEntityRads> CAPABILITY_ENTITY_RADS = null;
	
	ResourceLocation CAPABILITY_ENTITY_RADS_NAME = new ResourceLocation(Global.MOD_ID, "capability_entity_rads");
	
	double getTotalRads();
	
	void setTotalRads(double newTotalRads, boolean useImmunity);
	
	default boolean isTotalRadsNegligible() {
		return getTotalRads() < radiation_lowest_rate;
	}
	
	double getMaxRads();
	
	default double getRadsPercentage() {
		return Math.min(100D, 100D * getTotalRads() / getMaxRads());
	}
	
	default double getRawRadiationLevel() {
		return getFullRadiationResistance() > 0D ? 0.5D * (getRadiationLevel() + Math.sqrt(getRadiationLevel() * (getRadiationLevel() + 4D * getFullRadiationResistance()))) : getFullRadiationResistance() < 0D ? getRadiationLevel() / (1D - getFullRadiationResistance()) : getRadiationLevel();
	}
	
	default boolean isRawRadiationNegligible() {
		return getRawRadiationLevel() < radiation_lowest_rate;
	}
	
	double getInternalRadiationResistance();
	
	void setInternalRadiationResistance(double newInternalRadiationResistance);
	
	double getExternalRadiationResistance();
	
	void setExternalRadiationResistance(double newExternalRadiationResistance);
	
	default double getFullRadiationResistance() {
		return getInternalRadiationResistance() + getExternalRadiationResistance();
	}
	
	boolean getRadXUsed();
	
	void setRadXUsed(boolean radXUsed);
	
	boolean getRadXWoreOff();
	
	void setRadXWoreOff(boolean radXWoreOff);
	
	double getRadawayBuffer(boolean slow);
	
	void setRadawayBuffer(boolean slow, double newCount);
	
	double getPoisonBuffer();
	
	void setPoisonBuffer(double newBuffer);
	
	default boolean isFatal() {
		return getTotalRads() >= getMaxRads();
	}
	
	boolean getConsumedMedicine();
	
	void setConsumedMedicine(boolean consumed);
	
	double getRadawayCooldown();
	
	void setRadawayCooldown(double cooldown);
	
	boolean canConsumeRadaway();
	
	double getRecentRadawayAddition();
	
	void setRecentRadawayAddition(double newRecentRadawayAddition);
	
	void resetRecentRadawayAddition();
	
	double getRadXCooldown();
	
	void setRadXCooldown(double cooldown);
	
	boolean canConsumeRadX();
	
	double getRecentRadXAddition();
	
	void setRecentRadXAddition(double newRecentRadXAddition);
	
	void resetRecentRadXAddition();
	
	int getMessageCooldownTime();
	
	void setMessageCooldownTime(int messageTime);
	
	double getRecentPoisonAddition();
	
	void setRecentPoisonAddition(double newRecentPoisonAddition);
	
	void resetRecentPoisonAddition();
	
	double getRadiationImmunityTime();
	
	void setRadiationImmunityTime(double newRadiationImmunityTime);
	
	boolean getRadiationImmunityStage();
	
	void setRadiationImmunityStage(boolean newRadiationImmunityStage);
	
	default boolean isImmune() {
		return getRadiationImmunityStage() || getRadiationImmunityTime() > 0D;
	}
	
	boolean getShouldWarn();
	
	void setShouldWarn(boolean shouldWarn);
	
	boolean getGiveGuidebook();
	
	void setGiveGuidebook(boolean giveGuidebook);
}
