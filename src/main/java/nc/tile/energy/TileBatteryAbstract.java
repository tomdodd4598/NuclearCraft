package nc.tile.energy;

import ic2.api.energy.tile.IEnergySink;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.tile.dummy.IInterfaceable;
import nc.tile.internal.EnergyStorage;
import nc.tile.internal.EnumEnergyStorage.EnergyConnection;
import nc.tile.passive.ITilePassive;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class TileBatteryAbstract extends TileEnergy implements IBattery, IInterfaceable, IEnergySpread {
	
	public TileBatteryAbstract(int capacity) {
		this(capacity, capacity);
	}

	public TileBatteryAbstract(int capacity, int maxTransfer) {
		super(capacity, maxTransfer, EnergyConnection.IN);
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
		return storage;
	}
	
	// Energy Connections
	
	@Override
	public void pushEnergy() {
		if (storage.getEnergyStored() <= 0) return;
		for (EnumFacing side : EnumFacing.HORIZONTALS) {
			TileEntity tile = world.getTileEntity(getPos().offset(side));
			if (tile instanceof ITilePassive) if (!((ITilePassive) tile).canPushEnergyTo()) continue;
			if (tile instanceof ITileEnergy) if (!((ITileEnergy) tile).getEnergyConnection().canReceive()) continue;
			IEnergyStorage adjStorage = tile == null ? null : tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
			
			if (adjStorage != null && storage.canExtract()) {
				storage.extractEnergy(adjStorage.receiveEnergy(storage.extractEnergy(storage.getMaxEnergyStored(), true), false), false);
			}
			else if (ModCheck.ic2Loaded()) {
				if (tile instanceof IEnergySink) {
					storage.extractEnergy((int) Math.round(((IEnergySink) tile).injectEnergy(side.getOpposite(), storage.extractEnergy(storage.getMaxEnergyStored(), true) / NCConfig.generator_rf_per_eu, getSourceTier())), false);
				}
			}
		}
	}
}
