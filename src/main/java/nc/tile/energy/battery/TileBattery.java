package nc.tile.energy.battery;

import nc.config.NCConfig;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energy.IEnergySpread;
import nc.tile.energy.ITileEnergy;
import nc.tile.energy.TileEnergy;
import nc.tile.internal.energy.EnergyConnection;
import nc.util.BlockFinder;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;

public class TileBattery extends TileEnergy implements IBattery, IInterfaceable, IEnergySpread {
	
	public static class VoltaicPileBasic extends TileBattery {
		
		public VoltaicPileBasic() {
			super(BatteryType.VOLTAIC_PILE_BASIC);
		}
	}
	
	public static class VoltaicPileAdvanced extends TileBattery {
		
		public VoltaicPileAdvanced() {
			super(BatteryType.VOLTAIC_PILE_ADVANCED);
		}
	}
	
	public static class VoltaicPileDU extends TileBattery {
		
		public VoltaicPileDU() {
			super(BatteryType.VOLTAIC_PILE_DU);
		}
	}
	
	public static class VoltaicPileElite extends TileBattery {
		
		public VoltaicPileElite() {
			super(BatteryType.VOLTAIC_PILE_ELITE);
		}
	}
	
	public static class LithiumIonBatteryBasic extends TileBattery {
		
		public LithiumIonBatteryBasic() {
			super(BatteryType.LITHIUM_ION_BATTERY_BASIC);
		}
	}
	
	public static class LithiumIonBatteryAdvanced extends TileBattery {
		
		public LithiumIonBatteryAdvanced() {
			super(BatteryType.LITHIUM_ION_BATTERY_ADVANCED);
		}
	}
	
	public static class LithiumIonBatteryDU extends TileBattery {
		
		public LithiumIonBatteryDU() {
			super(BatteryType.LITHIUM_ION_BATTERY_DU);
		}
	}
	
	public static class LithiumIonBatteryElite extends TileBattery {
		
		public LithiumIonBatteryElite() {
			super(BatteryType.LITHIUM_ION_BATTERY_ELITE);
		}
	}
	
	private final BatteryType type;
	private BlockFinder finder;
	protected int batteryCount = 0, comparatorStrength = 0;
	
	public TileBattery(BatteryType type) {
		super(type.getCapacity(), type.getMaxTransfer(), ITileEnergy.energyConnectionAll(EnergyConnection.IN));
		this.type = type;
	}
	
	@Override
	public void onAdded() {
		finder = new BlockFinder(pos, world, getBlockMetadata());
		super.onAdded();
		batteryCount = -1;
	}
	
	@Override
	public void update() {
		super.update();
		if(!world.isRemote) {
			pushEnergy();
			if (batteryCount == 0) {
				spreadEnergy();
			}
			tickBattery();
			boolean shouldUpdate = false;
			int compStrength = getComparatorStrength();
			if (comparatorStrength != compStrength && findAdjacentComparator()) {
				shouldUpdate = true;
			}
			comparatorStrength = compStrength;
			if (shouldUpdate) {
				markDirty();
			}
		}
	}
	
	@Override
	public int getComparatorStrength() {
		return (int) Math.round(15D*getEnergyStored()/getMaxEnergyStored());
	}
	
	public boolean findAdjacentComparator() {
		return finder.adjacent(pos, 1, Blocks.UNPOWERED_COMPARATOR, Blocks.POWERED_COMPARATOR);
	}
	
	public void tickBattery() {
		batteryCount++; batteryCount %= NCConfig.machine_update_rate;
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
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setInteger("comparatorStrength", comparatorStrength);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		comparatorStrength = nbt.getInteger("comparatorStrength");
	}
}
