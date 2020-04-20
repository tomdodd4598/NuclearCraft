package nc.multiblock.battery;

import nc.config.NCConfig;
import nc.multiblock.battery.tile.TileBattery;
import net.minecraft.tileentity.TileEntity;

public enum BatteryType {
	VOLTAIC_PILE_BASIC(0, 1),
	VOLTAIC_PILE_ADVANCED(1, 2),
	VOLTAIC_PILE_DU(2, 3),
	VOLTAIC_PILE_ELITE(3, 4),
	
	LITHIUM_ION_BATTERY_BASIC(4, 3),
	LITHIUM_ION_BATTERY_ADVANCED(5, 4),
	LITHIUM_ION_BATTERY_DU(6, 5),
	LITHIUM_ION_BATTERY_ELITE(7, 6);
	
	private int id;
	private int energyTier;
	
	private BatteryType(int id, int energyTier) {
		this.id = id;
		this.energyTier = energyTier;
	}
	
	public int getCapacity() {
		return NCConfig.battery_capacity[id];
	}
	
	public int getMaxTransfer() {
		return NCConfig.battery_capacity[id];
	}
	
	public int getEnergyTier() {
		return energyTier;
	}
	
	public TileEntity getTile() {
		switch (this) {
		case VOLTAIC_PILE_BASIC:
			return new TileBattery.VoltaicPileBasic();
		case VOLTAIC_PILE_ADVANCED:
			return new TileBattery.VoltaicPileAdvanced();
		case VOLTAIC_PILE_DU:
			return new TileBattery.VoltaicPileDU();
		case VOLTAIC_PILE_ELITE:
			return new TileBattery.VoltaicPileElite();
			
		case LITHIUM_ION_BATTERY_BASIC:
			return new TileBattery.LithiumIonBatteryBasic();
		case LITHIUM_ION_BATTERY_ADVANCED:
			return new TileBattery.LithiumIonBatteryAdvanced();
		case LITHIUM_ION_BATTERY_DU:
			return new TileBattery.LithiumIonBatteryDU();
		case LITHIUM_ION_BATTERY_ELITE:
			return new TileBattery.LithiumIonBatteryElite();
		
		default:
			return null;
		}
	}
}
