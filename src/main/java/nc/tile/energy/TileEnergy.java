package nc.tile.energy;

import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.energy.EnumStorage.EnergyConnection;
import nc.energy.Storage;
import nc.tile.NCTile;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class TileEnergy extends NCTile implements ITileEnergy, IEnergyStorage, IEnergyTile, IEnergySink, IEnergySource {

	public EnergyConnection energyConnection;
	public final Storage storage;
	public boolean isEnergyTileSet = true;
	public boolean ic2reg = false;
	
	public TileEnergy(int capacity, EnergyConnection energyConnection) {
		this(capacity, capacity, capacity, energyConnection);
	}
	
	public TileEnergy(int capacity, int maxTransfer, EnergyConnection energyConnection) {
		this(capacity, maxTransfer, maxTransfer, energyConnection);
	}
	
	public TileEnergy(int capacity, int maxReceive, int maxExtract, EnergyConnection energyConnection) {
		super();
		storage = new Storage(capacity, maxReceive, maxExtract);
		this.energyConnection = energyConnection;
	}
	
	@Override
	public void update() {
		super.update();
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		addTileToENet();
	}
	
	@Override
	public void validate() {
		super.validate();
	}

	@Override
	public void invalidate() {
		super.invalidate();
		removeTileFromENet();
	}
	
	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		removeTileFromENet();
	}
	
	@Override
	public Storage getStorage() {
		return storage;
	}
	
	@Override
	public EnergyConnection getEnergyConnection() {
		return energyConnection;
	}
	
	// Redstone Flux
	
	@Override
	public int getEnergyStored() {
		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored() {
		return storage.getMaxEnergyStored();
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return energyConnection.canExtract() ? storage.extractEnergy(maxExtract, simulate) : 0;
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return energyConnection.canReceive() ? storage.receiveEnergy(maxReceive, simulate) : 0;
	}
	
	@Override
	public boolean canExtract() {
		return energyConnection.canExtract();
	}

	@Override
	public boolean canReceive() {
		return energyConnection.canReceive();
	}
	
	// IC2 Energy

	@Override
	public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
		return energyConnection.canReceive();
	}

	@Override
	public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
		return energyConnection.canExtract();
	}

	@Override
	public double getOfferedEnergy() {
		return Math.min(Math.pow(2, 2*getSourceTier() + 3), storage.takePower(storage.maxExtract, true) / NCConfig.generator_rf_per_eu);
	}
	
	@Override
	public double getDemandedEnergy() {
		return Math.min(Math.pow(2, 2*getSinkTier() + 3), storage.givePower(storage.maxReceive, true) / NCConfig.processor_rf_per_eu);
	}
	
	/** The normal conversion is 4 RF to 1 EU, but for RF generators, this is OP, so the ratio is instead 16:1 */
	@Override
	public void drawEnergy(double amount) {
		storage.takePower((long) (NCConfig.generator_rf_per_eu * amount), false);
	}

	@Override
	public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
		int energyReceived = storage.receiveEnergy((int) (NCConfig.processor_rf_per_eu * amount), true);
		storage.givePower(energyReceived, false);
		return amount - (energyReceived / NCConfig.processor_rf_per_eu);
	}
	
	@Override
	public abstract int getSourceTier();

	@Override
	public abstract int getSinkTier();
	
	public void addTileToENet() {
		if (!world.isRemote && ModCheck.ic2Loaded() && !ic2reg) {
			EnergyNet.instance.addTile(this);
			ic2reg = true;
		}
	}
	
	public void removeTileFromENet() {
		if (!world.isRemote && ModCheck.ic2Loaded() && ic2reg) {
			EnergyNet.instance.removeTile(this);
			ic2reg = false;
		}
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setInteger("energy", storage.getEnergyStored());
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		storage.setEnergyStored(nbt.getInteger("energy"));
	}
	
	// Energy Connections
	
	public void setConnection(EnergyConnection energyConnection) {
		this.energyConnection = energyConnection;
	}
	
	public void pushEnergy() {
		if (storage.getEnergyStored() <= 0 || !energyConnection.canExtract()) return;
		for (EnumFacing side : EnumFacing.VALUES) {
			TileEntity tile = world.getTileEntity(getPos().offset(side));
			IEnergyStorage adjStorage = tile == null ? null : tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
			
			if (adjStorage != null && storage.canExtract()) {
				storage.extractEnergy(adjStorage.receiveEnergy(storage.extractEnergy(storage.getMaxEnergyStored(), true), false), false);
			}
			else if (tile instanceof IEnergySink) {
				storage.extractEnergy((int) Math.round(((IEnergySink) tile).injectEnergy(side.getOpposite(), storage.extractEnergy(storage.getMaxEnergyStored(), true) / NCConfig.generator_rf_per_eu, getSourceTier())), false);
			}
		}
	}
	
	public void spreadEnergy() {
		if (!NCConfig.passive_permeation) return;
		if (storage.getEnergyStored() <= 0 || energyConnection == EnergyConnection.NON) return;
		for (EnumFacing side : EnumFacing.VALUES) {
			TileEntity tile = world.getTileEntity(getPos().offset(side));
			IEnergyStorage adjStorage = tile == null ? null : tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
			
			if (!(tile instanceof IEnergySpread)) continue;
			
			if (adjStorage != null && storage.canExtract()) {
				int maxExtract = (storage.getEnergyStored() - adjStorage.getEnergyStored())/2;
				if (maxExtract > 0) storage.extractEnergy(adjStorage.receiveEnergy(storage.extractEnergy(maxExtract, true), false), false);
			}
		}
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (CapabilityEnergy.ENERGY == capability && energyConnection.canConnect()) {
			return true;
		}
		if (energyConnection != null && ModCheck.teslaLoaded() && energyConnection.canConnect()) {
			if ((capability == TeslaCapabilities.CAPABILITY_CONSUMER && energyConnection.canReceive()) || (capability == TeslaCapabilities.CAPABILITY_PRODUCER && energyConnection.canExtract()) || capability == TeslaCapabilities.CAPABILITY_HOLDER)
				return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (CapabilityEnergy.ENERGY == capability && energyConnection.canConnect()) {
			return (T) storage;
		}
		if (energyConnection != null && ModCheck.teslaLoaded() && energyConnection.canConnect()) {
			if ((capability == TeslaCapabilities.CAPABILITY_CONSUMER && energyConnection.canReceive()) || (capability == TeslaCapabilities.CAPABILITY_PRODUCER && energyConnection.canExtract()) || capability == TeslaCapabilities.CAPABILITY_HOLDER)
				return (T) storage;
		}
		return super.getCapability(capability, facing);
	}
}
