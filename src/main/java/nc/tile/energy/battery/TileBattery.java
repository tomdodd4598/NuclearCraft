package nc.tile.energy.battery;

import nc.tile.dummy.IInterfaceable;
import nc.tile.energy.IEnergySpread;
import nc.tile.energy.TileEnergy;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.energy.EnergyStorage;

public class TileBattery extends TileEnergy implements IBattery, IInterfaceable, IEnergySpread {
	
	public static class VoltaicPileBasic extends TileBattery {

		public VoltaicPileBasic() {
			super(BatteryType.VOLTAIC_PILE_BASIC);
		}
	}
	
	public static class LithiumIonBatteryBasic extends TileBattery {

		public LithiumIonBatteryBasic() {
			super(BatteryType.LITHIUM_ION_BATTERY_BASIC);
		}
	}
	
	private final BatteryType type;
	
	public TileBattery(BatteryType type) {
		super(type.getCapacity(), type.getMaxTransfer(), energyConnectionAll(EnergyConnection.IN));
		this.type = type;
		configurableEnergyConnections = true;
	}
	
	@Override
	public void update() {
		super.update();
		if(!world.isRemote) {
			pushEnergy();
			spreadEnergy();
		}
	}

	@Override
	public EnergyStorage getBatteryStorage() {
		return getEnergyStorage();
	}
	
	@Override
	public int getSourceTier() {
		return type.getEnergyTier();
	}
	
	@Override
	public int getSinkTier() {
		return type.getEnergyTier();
	}
}
