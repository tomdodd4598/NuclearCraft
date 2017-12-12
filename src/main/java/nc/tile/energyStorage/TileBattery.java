package nc.tile.energyStorage;

import nc.energy.EnumStorage.EnergyConnection;
import nc.block.tile.energyStorage.BlockBattery;
import nc.config.NCConfig;
import nc.energy.Storage;
import nc.tile.energy.TileEnergy;

public abstract class TileBattery extends TileEnergy implements IBattery {
	
	private int tickCount;
	
	public TileBattery(int capacity) {
		this(capacity, capacity);
	}

	public TileBattery(int capacity, int maxTransfer) {
		super(capacity, maxTransfer, EnergyConnection.IN);
	}
	
	public void update() {
		super.update();
		if(!worldObj.isRemote) {
			pushEnergy();
		}
		tick();
		if (shouldCheck()) {
			if (NCConfig.update_block_type) {
				updateBlock();
				worldObj.notifyNeighborsOfStateChange(pos, blockType);
			}
		}
	}
	
	public void updateBlock() {
		BlockBattery.update(worldObj, pos);
	}
	
	public void tick() {
		if (tickCount > 20) {
			tickCount = 0;
		} else {
			tickCount++;
		}
	}
	
	public boolean shouldCheck() {
		return tickCount > 20;
	}

	public Storage getBatteryStorage() {
		return storage;
	}
	
	/*public void pushEnergy() {
		if (storage.getEnergyStored() <= 0) return;
		TileEntity tile = worldObj.getTileEntity(getPos().offset(EnumFacing.DOWN));
		IEnergyStorage adjStorage = tile == null ? null : tile.getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP);
		//TileEntity thisTile = world.getTileEntity(getPos());
		
		if (adjStorage != null && storage.canExtract()) {
			storage.extractEnergy(adjStorage.receiveEnergy(storage.extractEnergy(storage.getMaxEnergyStored(), true), false), false);
		}
		else if (tile instanceof IEnergyReceiver) {
			storage.extractEnergy(((IEnergyReceiver) tile).receiveEnergy(EnumFacing.UP, storage.extractEnergy(storage.getMaxEnergyStored(), true), false), false);
		}
		else if (tile instanceof IEnergySink) {
			storage.extractEnergy((int) Math.round(((IEnergySink) tile).injectEnergy(EnumFacing.UP, storage.extractEnergy(storage.getMaxEnergyStored(), true) / 24, getSourceTier())), false);
		}
	}
	
	// Redstone Flux

	public int extractEnergy(int maxExtract, boolean simulate) {
		return storage.extractEnergy(maxExtract, simulate);
	}
	
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return storage.receiveEnergy(maxReceive, simulate);
	}
	
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		return storage.extractEnergy(maxExtract, simulate);
	}

	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		return storage.receiveEnergy(maxReceive, simulate);
	}
	
	public boolean canExtract() {
		return true;
	}

	public boolean canReceive() {
		return true;
	}
	
	// IC2 Energy

	public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
		return true;
	}

	public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
		return true;
	}*/
}
