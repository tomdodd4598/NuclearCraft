package nc.capability.radiation.entity;

import nc.Global;
import nc.capability.ICapability;
import nc.capability.radiation.IRadiation;
import nc.config.NCConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface IEntityRads extends IRadiation, ICapability<IEntityRads> {
	
	@CapabilityInject(IEntityRads.class)
	public static Capability<IEntityRads> CAPABILITY_ENTITY_RADS = null;
	
	public static final ResourceLocation CAPABILITY_ENTITY_RADS_NAME = new ResourceLocation(Global.MOD_ID, "capability_entity_rads");
	
	public double getTotalRads();
	
	public void setTotalRads(double newTotalRads, boolean useImmunity);
	
	public default boolean isTotalRadsNegligible() {
		return getTotalRads() < NCConfig.radiation_lowest_rate;
	}
	
	public double getMaxRads();
	
	public default double getRadsPercentage() {
		return Math.min(100D, 100D*getTotalRads()/getMaxRads());
	}
	
	public double getRadiationResistance();
	
	public void setRadiationResistance(double newRadiationResistance);
	
	public boolean getRadXWoreOff();
	
	public void setRadXWoreOff(boolean radXWoreOff);
	
	public double getRadawayBuffer(boolean slow);
	
	public void setRadawayBuffer(boolean slow, double newCount);
	
	public double getPoisonBuffer();
	
	public void setPoisonBuffer(double newBuffer, double maxLevel);
	
	public default boolean isFatal() {
		return getTotalRads() >= getMaxRads();
	}
	
	public boolean getConsumedMedicine();
	
	public void setConsumedMedicine(boolean consumed);
	
	public double getRadawayCooldown();
	
	public void setRadawayCooldown(double cooldown);
	
	public boolean canConsumeRadaway();
	
	public double getRadXCooldown();
	
	public void setRadXCooldown(double cooldown);
	
	public boolean canConsumeRadX();
	
	public double getRadiationImmunityTime();
	
	public void setRadiationImmunityTime(double newRadiationImmunityTime);
	
	public default boolean isImmune() {
		return getRadiationImmunityTime() > 0D;
	}
	
	public boolean getShouldWarn();
	
	public void setShouldWarn(boolean shouldWarn);
}
