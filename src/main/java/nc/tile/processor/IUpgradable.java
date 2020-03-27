package nc.tile.processor;

import nc.config.NCConfig;

public interface IUpgradable {
	
	public boolean hasUpgrades();
	
	public default int getNumberOfUpgrades() {
		return hasUpgrades() ? 2 : 0;
	}
	
	public int getSpeedCount();
	
	public int getEnergyCount();
	
	public int getSpeedUpgradeSlot();
	
	public int getEnergyUpgradeSlot();
	
	public default double getSpeedMultiplier() {
		return 1D + NCConfig.speed_upgrade_multipliers[0]*powerLawFactor(getSpeedCount(), NCConfig.speed_upgrade_power_laws[0]);
	}
	
	public default double getPowerMultiplier() {
		return (1D + NCConfig.speed_upgrade_multipliers[1]*powerLawFactor(getSpeedCount(), NCConfig.speed_upgrade_power_laws[1])) / (1D + NCConfig.energy_upgrade_multipliers[0]*powerLawFactor(getEnergyCount(), NCConfig.energy_upgrade_power_laws[0]));
	}
	
	public default double powerLawFactor(int upgradeCount, double power) {
		return Math.pow(upgradeCount, power) - 1D;
	}
	
	public void refreshUpgrades();
}
