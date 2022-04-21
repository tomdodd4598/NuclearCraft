package nc.multiblock.battery;

import nc.config.NCConfig;
import nc.multiblock.battery.tile.TileBattery;
import net.minecraft.tileentity.TileEntity;

public enum BatteryBlockType implements IBatteryBlockType {
	
	VOLTAIC_PILE_BASIC(0),
	VOLTAIC_PILE_ADVANCED(1),
	VOLTAIC_PILE_DU(2),
	VOLTAIC_PILE_ELITE(3),
	
	LITHIUM_ION_BATTERY_BASIC(4),
	LITHIUM_ION_BATTERY_ADVANCED(5),
	LITHIUM_ION_BATTERY_DU(6),
	LITHIUM_ION_BATTERY_ELITE(7);
	
	private int id;
	
	private BatteryBlockType(int id) {
		this.id = id;
	}
	
	@Override
	public long getCapacity() {
		return NCConfig.battery_block_capacity[id];
	}
	
	@Override
	public int getMaxTransfer() {
		return NCConfig.battery_block_max_transfer[id];
	}
	
	@Override
	public int getEnergyTier() {
		return NCConfig.battery_block_energy_tier[id];
	}
	
	@Override
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
