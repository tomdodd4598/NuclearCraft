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

	public EnergyConnection connection;
	public final Storage storage;
	public boolean isEnergyTileSet = true;
	
	public TileEnergy(int capacity, EnergyConnection connection) {
		this(capacity, capacity, capacity, connection);
	}
	
	public TileEnergy(int capacity, int maxTransfer, EnergyConnection connection) {
		this(capacity, maxTransfer, maxTransfer, connection);
	}
	
	public TileEnergy(int capacity, int maxReceive, int maxExtract, EnergyConnection connection) {
		super();
		storage = new Storage(capacity, maxReceive, maxExtract);
		this.connection = connection;
	}
	
	public void update() {
		super.update();
		if (!isEnergyTileSet && !world.isRemote && ModCheck.ic2Loaded()) {
			//MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			EnergyNet.instance.addTile(this);
			isEnergyTileSet = true;
		}
	}
	
	public void onAdded() {
		super.onAdded();
		if (!world.isRemote && ModCheck.ic2Loaded()) /*MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));*/ EnergyNet.instance.addTile(this);
	}

	public void invalidate() {
		super.invalidate();
		if (!world.isRemote && ModCheck.ic2Loaded()) /*MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));*/ EnergyNet.instance.removeTile(this);
	}
	
	public Storage getStorage() {
		return storage;
	}
	
	public EnergyConnection getEnergyConnection() {
		return connection;
	}
	
	// Redstone Flux
	
	public int getEnergyStored() {
		return storage.getEnergyStored();
	}

	public int getMaxEnergyStored() {
		return storage.getMaxEnergyStored();
	}

	public int extractEnergy(int maxExtract, boolean simulate) {
		return connection.canExtract() ? storage.extractEnergy(maxExtract, simulate) : 0;
	}
	
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return connection.canReceive() ? storage.receiveEnergy(maxReceive, simulate) : 0;
	}
	
	public boolean canExtract() {
		return connection.canExtract();
	}

	public boolean canReceive() {
		return connection.canReceive();
	}
	
	// IC2 Energy

	public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
		return connection.canReceive();
	}

	public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
		return connection.canExtract();
	}

	public double getOfferedEnergy() {
		return Math.min(Math.pow(2, 2*getSourceTier() + 3), storage.takePower(storage.maxExtract, true) / NCConfig.generator_rf_per_eu);
	}
	
	public double getDemandedEnergy() {
		return Math.min(Math.pow(2, 2*getSinkTier() + 3), storage.givePower(storage.maxReceive, true) / NCConfig.processor_rf_per_eu);
	}
	
	/** The normal conversion is 4 RF to 1 EU, but for RF generators, this is OP, so the ratio is instead 16:1 */
	public void drawEnergy(double amount) {
		storage.takePower((long) (NCConfig.generator_rf_per_eu * amount), false);
	}

	public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
		int energyReceived = storage.receiveEnergy((int) (NCConfig.processor_rf_per_eu * amount), true);
		storage.givePower(energyReceived, false);
		return amount - (energyReceived / NCConfig.processor_rf_per_eu);
	}
	
	public abstract int getSourceTier();

	public abstract int getSinkTier();
	
	// NBT
	
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setInteger("energy", storage.getEnergyStored());
		return nbt;
	}
	
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		storage.setEnergyStored(nbt.getInteger("energy"));
	}
	
	// Energy Connections
	
	public void setConnection(EnergyConnection connection) {
		this.connection = connection;
	}
	
	public void pushEnergy() {
		if (storage.getEnergyStored() <= 0 || !connection.canExtract()) return;
		for (EnumFacing side : EnumFacing.VALUES) {
			TileEntity tile = world.getTileEntity(getPos().offset(side));
			IEnergyStorage adjStorage = tile == null ? null : tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
			//TileEntity thisTile = world.getTileEntity(getPos());
			
			if (adjStorage != null && storage.canExtract()) {
				storage.extractEnergy(adjStorage.receiveEnergy(storage.extractEnergy(storage.getMaxEnergyStored(), true), false), false);
			}
			else if (tile instanceof IEnergySink /*&& tile != thisTile*/) {
				storage.extractEnergy((int) Math.round(((IEnergySink) tile).injectEnergy(side.getOpposite(), storage.extractEnergy(storage.getMaxEnergyStored(), true) / 24, getSourceTier())), false);
			}
		}
	}
	
	// Capability
	
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (CapabilityEnergy.ENERGY == capability) {
			return true;
		}
		if (connection != null && ModCheck.teslaLoaded && connection.canConnect()) {
			if ((capability == TeslaCapabilities.CAPABILITY_CONSUMER && connection.canReceive()) || (capability == TeslaCapabilities.CAPABILITY_PRODUCER && connection.canExtract()) || capability == TeslaCapabilities.CAPABILITY_HOLDER)
				return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (CapabilityEnergy.ENERGY == capability) {
			return (T) storage;
		}
		if (connection != null && ModCheck.teslaLoaded && connection.canConnect()) {
			if ((capability == TeslaCapabilities.CAPABILITY_CONSUMER && connection.canReceive()) || (capability == TeslaCapabilities.CAPABILITY_PRODUCER && connection.canExtract()) || capability == TeslaCapabilities.CAPABILITY_HOLDER)
				return (T) storage;
		}
		return super.getCapability(capability, facing);
	}
}
