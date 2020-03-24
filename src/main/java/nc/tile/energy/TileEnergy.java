package nc.tile.energy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import gregtech.api.capability.GregtechCapabilities;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.tile.NCTile;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.energy.EnergyStorage;
import nc.tile.internal.energy.EnergyTileWrapper;
import nc.tile.internal.energy.EnergyTileWrapperGT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList({@Optional.Interface(iface = "ic2.api.energy.tile.IEnergyTile", modid = "ic2"), @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "ic2"), @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "ic2")})
public abstract class TileEnergy extends NCTile implements ITileEnergy, IEnergyTile, IEnergySink, IEnergySource {
	
	private final @Nonnull EnergyStorage storage;
	
	private @Nonnull EnergyConnection[] energyConnections;
	
	private @Nonnull EnergyTileWrapper[] energySides;
	private @Nonnull EnergyTileWrapperGT[] energySidesGT;
	
	private boolean ic2reg = false;
	
	public TileEnergy(int capacity, @Nonnull EnergyConnection[] energyConnections) {
		this(capacity, capacity, energyConnections);
	}
	
	public TileEnergy(int capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections) {
		super();
		storage = new EnergyStorage(capacity, maxTransfer);
		this.energyConnections = energyConnections;
		energySides = ITileEnergy.getDefaultEnergySides(this);
		energySidesGT = ITileEnergy.getDefaultEnergySidesGT(this);
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		if (ModCheck.ic2Loaded()) addTileToENet();
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		if (ModCheck.ic2Loaded()) removeTileFromENet();
	}
	
	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		if (ModCheck.ic2Loaded()) removeTileFromENet();
	}
	
	@Override
	public EnergyStorage getEnergyStorage() {
		return storage;
	}
	
	@Override
	public @Nonnull EnergyConnection[] getEnergyConnections() {
		return energyConnections;
	}

	@Override
	public @Nonnull EnergyTileWrapper[] getEnergySides() {
		return energySides;
	}

	@Override
	public @Nonnull EnergyTileWrapperGT[] getEnergySidesGT() {
		return energySidesGT;
	}
	
	// IC2 Energy
	
	@Override
	@Optional.Method(modid = "ic2")
	public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
		return getEnergyConnection(side).canReceive();
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
		return getEnergyConnection(side).canExtract();
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public double getOfferedEnergy() {
		return Math.min(Math.pow(2, 2*getSourceTier() + 3), (double)getEnergyStorage().extractEnergy(getEnergyStorage().getMaxTransfer(), true) / (double)NCConfig.rf_per_eu);
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public double getDemandedEnergy() {
		return Math.min(Math.pow(2, 2*getSinkTier() + 3), (double)getEnergyStorage().receiveEnergy(getEnergyStorage().getMaxTransfer(), true) / (double)NCConfig.rf_per_eu);
	}
	
	/* The normal conversion is 4 RF to 1 EU, but for RF generators, this is OP, so the ratio is instead 16:1 */
	@Override
	@Optional.Method(modid = "ic2")
	public void drawEnergy(double amount) {
		getEnergyStorage().extractEnergy((int) (NCConfig.rf_per_eu * amount), false);
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
		int energyReceived = getEnergyStorage().receiveEnergy((int) (NCConfig.rf_per_eu * amount), true);
		getEnergyStorage().receiveEnergy(energyReceived, false);
		return amount - (double)energyReceived/(double)NCConfig.rf_per_eu;
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public int getSourceTier() {
		return getEUSourceTier();
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public int getSinkTier() {
		return getEUSinkTier();
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public void addTileToENet() {
		if (!world.isRemote && ModCheck.ic2Loaded() && !ic2reg) {
			EnergyNet.instance.addTile(this);
			ic2reg = true;
		}
	}
	
	@Override
	@Optional.Method(modid = "ic2")
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
		writeEnergy(nbt);
		writeEnergyConnections(nbt);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readEnergy(nbt);
		readEnergyConnections(nbt);
	}
	
	// Energy Connections
	
	public void setEnergyConnectionAll(EnergyConnection energyConnection) {
		energyConnections = ITileEnergy.energyConnectionAll(energyConnection);
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityEnergy.ENERGY || (ModCheck.gregtechLoaded() && NCConfig.enable_gtce_eu && capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER)) {
			return hasEnergySideCapability(side);
		}
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityEnergy.ENERGY) {
			if (hasEnergySideCapability(side)) {
				return (T) getEnergySide(nonNullSide(side));
			}
			return null;
		}
		else if (ModCheck.gregtechLoaded() && capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER) {
			if (NCConfig.enable_gtce_eu && hasEnergySideCapability(side)) {
				return (T) getEnergySideGT(nonNullSide(side));
			}
			return null;
		}
		return super.getCapability(capability, side);
	}
}
