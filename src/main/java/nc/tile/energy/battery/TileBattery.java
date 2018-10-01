package nc.tile.energy.battery;

import nc.tile.dummy.IInterfaceable;
import nc.tile.energy.IEnergySpread;
import nc.tile.energy.ITileEnergy;
import nc.tile.energy.TileEnergy;
import nc.tile.internal.energy.EnergyConnection;
import nc.util.BlockFinder;
import net.minecraft.init.Blocks;

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
	private BlockFinder finder;
	
	public TileBattery(BatteryType type) {
		super(type.getCapacity(), type.getMaxTransfer(), ITileEnergy.energyConnectionAll(EnergyConnection.IN));
		this.type = type;
	}
	
	@Override
	public void onAdded() {
		finder = new BlockFinder(pos, world, getBlockMetadata());
		super.onAdded();
		tickCount = -1;
	}
	
	@Override
	public void update() {
		super.update();
		pushEnergy();
		boolean shouldUpdate = false;
		if(!world.isRemote) {
			if(shouldTileCheck()) spreadEnergy();
			if (findAdjacentComparator() && shouldTileCheck()) shouldUpdate = true;
			tickTile();
		}
		if (shouldUpdate) markDirty();
	}
	
	public boolean findAdjacentComparator() {
		return finder.adjacent(pos, 1, Blocks.UNPOWERED_COMPARATOR, Blocks.POWERED_COMPARATOR);
	}
	
	@Override
	public int getEUSourceTier() {
		return type.getEnergyTier();
	}
	
	@Override
	public int getEUSinkTier() {
		return type.getEnergyTier();
	}
	
	@Override
	public boolean hasConfigurableEnergyConnections() {
		return true;
	}
}
