package nc.block.battery;

import nc.config.NCConfig;
import nc.tile.battery.TileBattery;
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
	
	private final int id;
	
	BatteryBlockType(int id) {
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
        return switch (this) {
            case VOLTAIC_PILE_BASIC -> new TileBattery.VoltaicPileBasic();
            case VOLTAIC_PILE_ADVANCED -> new TileBattery.VoltaicPileAdvanced();
            case VOLTAIC_PILE_DU -> new TileBattery.VoltaicPileDU();
            case VOLTAIC_PILE_ELITE -> new TileBattery.VoltaicPileElite();
            case LITHIUM_ION_BATTERY_BASIC -> new TileBattery.LithiumIonBatteryBasic();
            case LITHIUM_ION_BATTERY_ADVANCED -> new TileBattery.LithiumIonBatteryAdvanced();
            case LITHIUM_ION_BATTERY_DU -> new TileBattery.LithiumIonBatteryDU();
            case LITHIUM_ION_BATTERY_ELITE -> new TileBattery.LithiumIonBatteryElite();
        };
	}
}
