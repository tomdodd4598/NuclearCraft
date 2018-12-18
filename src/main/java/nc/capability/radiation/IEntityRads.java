package nc.capability.radiation;

import nc.Global;
import nc.capability.ICapability;
import nc.config.NCConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface IEntityRads extends IRadiation, ICapability<IEntityRads> {
	
	@CapabilityInject(IEntityRads.class)
	public static Capability<IEntityRads> CAPABILITY_ENTITY_RADS = null;
	
	public static final ResourceLocation CAPABILITY_ENTITY_RADS_NAME = new ResourceLocation(Global.MOD_ID, "capability_entity_rads");
	
	public double getTotalRads();
	
	public void setTotalRads(double newTotalRads);
	
	public default boolean isTotalRadsNegligible() {
		return getTotalRads() < NCConfig.radiation_lowest_rate;
	}
	
	public double getMaxRads();
	
	public default int getRadsPercentage() {
		return Math.min(100, (int)(100D*getTotalRads()/getMaxRads()));
	}
	
	public double getRadiationResistance();
	
	public void setRadiationResistance(double newRadiationResistance);
	
	public boolean getRadXWoreOff();
	
	public void setRadXWoreOff(boolean radXWoreOff);
	
	public double getRadawayBuffer();
	
	public void setRadawayBuffer(double newCount);
	
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
}
