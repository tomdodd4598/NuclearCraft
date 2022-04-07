package nc.tile.energy;

import static nc.config.NCConfig.enable_gtce_eu;

import javax.annotation.*;

import gregtech.api.capability.GregtechCapabilities;
import ic2.api.energy.tile.*;
import nc.ModCheck;
import nc.tile.NCTile;
import nc.tile.internal.energy.*;
import nc.util.NCMath;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList({@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "ic2"), @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "ic2")})
public abstract class TileEnergy extends NCTile implements ITileEnergy, IEnergySink, IEnergySource {
	
	private final @Nonnull EnergyStorage storage;
	
	private @Nonnull EnergyConnection[] energyConnections;
	
	private @Nonnull final EnergyTileWrapper[] energySides;
	private @Nonnull final EnergyTileWrapperGT[] energySidesGT;
	
	private boolean ic2reg = false;
	
	public TileEnergy(long capacity, @Nonnull EnergyConnection[] energyConnections) {
		this(capacity, NCMath.toInt(capacity), energyConnections);
	}
	
	public TileEnergy(long capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections) {
		super();
		storage = new EnergyStorage(capacity, maxTransfer);
		this.energyConnections = energyConnections;
		energySides = ITileEnergy.getDefaultEnergySides(this);
		energySidesGT = ITileEnergy.getDefaultEnergySidesGT(this);
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		if (ModCheck.ic2Loaded()) {
			addTileToENet();
		}
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		if (ModCheck.ic2Loaded()) {
			removeTileFromENet();
		}
	}
	
	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		if (ModCheck.ic2Loaded()) {
			removeTileFromENet();
		}
	}
	
	@Override
	public EnergyStorage getEnergyStorage() {
		return storage;
	}
	
	@Override
	public EnergyConnection[] getEnergyConnections() {
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
	public boolean getIC2Reg() {
		return ic2reg;
	}
	
	@Override
	public void setIC2Reg(boolean ic2reg) {
		this.ic2reg = ic2reg;
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
		return ITileEnergy.super.acceptsEnergyFrom(emitter, side);
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public double getDemandedEnergy() {
		return ITileEnergy.super.getDemandedEnergy();
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
		return ITileEnergy.super.injectEnergy(directionFrom, amount, voltage);
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
		return ITileEnergy.super.emitsEnergyTo(receiver, side);
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public double getOfferedEnergy() {
		return ITileEnergy.super.getOfferedEnergy();
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public void drawEnergy(double amount) {
		ITileEnergy.super.drawEnergy(amount);
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
		if (capability == CapabilityEnergy.ENERGY || ModCheck.gregtechLoaded() && enable_gtce_eu && capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER) {
			return hasEnergySideCapability(side);
		}
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityEnergy.ENERGY) {
			if (hasEnergySideCapability(side)) {
				return CapabilityEnergy.ENERGY.cast(getEnergySide(nonNullSide(side)));
			}
			return null;
		}
		else if (ModCheck.gregtechLoaded() && capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER) {
			if (enable_gtce_eu && hasEnergySideCapability(side)) {
				return GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER.cast(getEnergySideGT(nonNullSide(side)));
			}
			return null;
		}
		return super.getCapability(capability, side);
	}
}
