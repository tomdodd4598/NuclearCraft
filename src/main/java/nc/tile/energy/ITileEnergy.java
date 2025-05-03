package nc.tile.energy;

import gregtech.api.capability.*;
import ic2.api.energy.event.*;
import ic2.api.energy.tile.*;
import mcjty.lib.api.power.IBigPower;
import nc.ModCheck;
import nc.tile.ITile;
import nc.tile.internal.energy.EnergyStorage;
import nc.tile.internal.energy.*;
import nc.tile.passive.ITilePassive;
import nc.util.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.energy.*;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.*;

import static nc.config.NCConfig.*;

@Optional.Interface(iface = "mcjty.lib.api.power.IBigPower", modid = "theoneprobe")
public interface ITileEnergy extends ITile, IBigPower {
	
	// Storage
	
	EnergyStorage getEnergyStorage();
	
	// Energy Connection
	
	EnergyConnection[] getEnergyConnections();
	
	default EnergyConnection getEnergyConnection(@Nonnull EnumFacing side) {
		return getEnergyConnections()[side.getIndex()];
	}
	
	default void setEnergyConnection(@Nonnull EnergyConnection energyConnection, @Nonnull EnumFacing side) {
		getEnergyConnections()[side.getIndex()] = energyConnection;
	}
	
	default void toggleEnergyConnection(@Nonnull EnumFacing side, @Nonnull EnergyConnection.Type type) {
		if (ModCheck.ic2Loaded()) {
			removeTileFromENet();
		}
		setEnergyConnection(getEnergyConnection(side).next(type), side);
		markDirtyAndNotify(true);
		if (ModCheck.ic2Loaded()) {
			addTileToENet();
		}
	}
	
	default boolean canConnectEnergy(@Nonnull EnumFacing side) {
		return getEnergyConnection(side).canConnect();
	}
	
	static @Nonnull EnergyConnection[] energyConnectionAll(@Nonnull EnergyConnection connection) {
		EnergyConnection[] array = new EnergyConnection[6];
		for (int i = 0; i < 6; ++i) {
			array[i] = connection;
		}
		return array;
	}
	
	default boolean hasConfigurableEnergyConnections() {
		return false;
	}
	
	// Energy Connection Wrapper Methods
	
	default int getEnergyStored() {
		return getEnergyStorage().getEnergyStored();
	}
	
	default int getMaxEnergyStored() {
		return getEnergyStorage().getMaxEnergyStored();
	}
	
	default long getEnergyStoredLong() {
		return getEnergyStorage().getEnergyStoredLong();
	}
	
	default long getMaxEnergyStoredLong() {
		return getEnergyStorage().getMaxEnergyStoredLong();
	}
	
	default boolean canReceiveEnergy(EnumFacing side) {
		return getEnergyConnection(side).canReceive();
	}
	
	default boolean canExtractEnergy(EnumFacing side) {
		return getEnergyConnection(side).canExtract();
	}
	
	default int receiveEnergy(int maxReceive, EnumFacing side, boolean simulate) {
		return canReceiveEnergy(side) ? getEnergyStorage().receiveEnergy(maxReceive, simulate) : 0;
	}
	
	default int extractEnergy(int maxExtract, EnumFacing side, boolean simulate) {
		return canExtractEnergy(side) ? getEnergyStorage().extractEnergy(maxExtract, simulate) : 0;
	}
	
	// IC2 EU
	
	boolean getIC2Reg();
	
	void setIC2Reg(boolean ic2reg);
	
	@Optional.Method(modid = "ic2")
	default void addTileToENet() {
		if (!getTileWorld().isRemote && enable_ic2_eu && !getIC2Reg() && this instanceof IEnergyTile) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent((IEnergyTile) this));
			setIC2Reg(true);
		}
	}
	
	@Optional.Method(modid = "ic2")
	default void removeTileFromENet() {
		if (!getTileWorld().isRemote && getIC2Reg() && this instanceof IEnergyTile) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent((IEnergyTile) this));
			setIC2Reg(false);
		}
	}
	
	int getSinkTier();
	
	int getSourceTier();
	
	@Optional.Method(modid = "ic2")
	default boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
		return getEnergyConnection(side).canReceive();
	}
	
	@Optional.Method(modid = "ic2")
	default double getDemandedEnergy() {
		return Math.min(Math.pow(2, 2 * getSinkTier() + 3), (double) getEnergyStorage().receiveEnergy(getEnergyStorage().getMaxTransfer(), true) / (double) rf_per_eu);
	}
	
	@Optional.Method(modid = "ic2")
	default double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
		int energyReceived = getEnergyStorage().receiveEnergy(NCMath.toInt(rf_per_eu * amount), true);
		getEnergyStorage().receiveEnergy(energyReceived, false);
		return amount - (double) energyReceived / (double) rf_per_eu;
	}
	
	@Optional.Method(modid = "ic2")
	default boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
		return getEnergyConnection(side).canExtract();
	}
	
	@Optional.Method(modid = "ic2")
	default double getOfferedEnergy() {
		return Math.min(Math.pow(2, 2 * getSourceTier() + 3), (double) getEnergyStorage().extractEnergy(getEnergyStorage().getMaxTransfer(), true) / (double) rf_per_eu);
	}
	
	@Optional.Method(modid = "ic2")
	default void drawEnergy(double amount) {
		getEnergyStorage().extractEnergy(NCMath.toInt(rf_per_eu * amount), false);
	}
	
	// Energy Wrappers
	
	@Nonnull
	EnergyTileWrapper[] getEnergySides();
	
	@Nonnull
	EnergyTileWrapperGT[] getEnergySidesGT();
	
	default @Nonnull EnergyTileWrapper getEnergySide(@Nonnull EnumFacing side) {
		return getEnergySides()[side.getIndex()];
	}
	
	default @Nonnull EnergyTileWrapperGT getEnergySideGT(@Nonnull EnumFacing side) {
		return getEnergySidesGT()[side.getIndex()];
	}
	
	static @Nonnull EnergyTileWrapper[] getDefaultEnergySides(@Nonnull ITileEnergy tile) {
		return new EnergyTileWrapper[] {new EnergyTileWrapper(tile, EnumFacing.DOWN), new EnergyTileWrapper(tile, EnumFacing.UP), new EnergyTileWrapper(tile, EnumFacing.NORTH), new EnergyTileWrapper(tile, EnumFacing.SOUTH), new EnergyTileWrapper(tile, EnumFacing.WEST), new EnergyTileWrapper(tile, EnumFacing.EAST)};
	}
	
	static @Nonnull EnergyTileWrapperGT[] getDefaultEnergySidesGT(@Nonnull ITileEnergy tile) {
		return new EnergyTileWrapperGT[] {new EnergyTileWrapperGT(tile, EnumFacing.DOWN), new EnergyTileWrapperGT(tile, EnumFacing.UP), new EnergyTileWrapperGT(tile, EnumFacing.NORTH), new EnergyTileWrapperGT(tile, EnumFacing.SOUTH), new EnergyTileWrapperGT(tile, EnumFacing.WEST), new EnergyTileWrapperGT(tile, EnumFacing.EAST)};
	}
	
	// Energy Distribution
	
	default void pushEnergy() {
		for (EnumFacing side : EnumFacing.VALUES) {
			if (getEnergyStoredLong() <= 0) {
				return;
			}
			pushEnergyToSide(side);
		}
	}
	
	default void pushEnergyToSide(@Nonnull EnumFacing side) {
		if (!getEnergyConnection(side).canExtract()) {
			return;
		}
		
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		if (tile == null) {
			return;
		}
		
		if (tile instanceof ITileEnergy) {
			if (!((ITileEnergy) tile).getEnergyConnection(side.getOpposite()).canReceive()) {
				return;
			}
		}
		if (tile instanceof ITilePassive tilePassive) {
			if (!tilePassive.canPushEnergyTo()) {
				return;
			}
		}
		
		IEnergyStorage adjStorage = tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
		
		if (adjStorage != null && getEnergyStorage().canExtract()) {
			getEnergyStorage().extractEnergy(adjStorage.receiveEnergy(getEnergyStorage().extractEnergy(getMaxEnergyStored(), true), false), false);
			return;
		}
		
		if (getEnergyStoredLong() < rf_per_eu) {
			return;
		}
		
		if (ModCheck.ic2Loaded() && enable_ic2_eu) {
			if (tile instanceof IEnergySink) {
				getEnergyStorage().extractEnergy(NCMath.toInt(Math.round(((IEnergySink) tile).injectEnergy(side.getOpposite(), (double) getEnergyStorage().extractEnergy(getMaxEnergyStored(), true) / rf_per_eu, getSourceTier()) * rf_per_eu)), false);
				return;
			}
		}
		if (enable_gtce_eu && ModCheck.gregtechLoaded()) {
			IEnergyContainer adjStorageGT = tile.getCapability(GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER, side.getOpposite());
			if (adjStorageGT != null && getEnergyStorage().canExtract()) {
				long voltage = NCMath.clamp(getEnergyStoredLong() / rf_per_eu, 1L, EnergyHelper.getMaxEUFromTier(getSourceTier()));
				getEnergyStorage().extractEnergy(NCMath.toInt(voltage * adjStorageGT.acceptEnergyFromNetwork(side.getOpposite(), voltage, 1L) * rf_per_eu), false);
			}
		}
	}
	
	// NBT
	
	default NBTTagCompound writeEnergy(NBTTagCompound nbt) {
		getEnergyStorage().writeToNBT(nbt, "energyStorage");
		return nbt;
	}
	
	default void readEnergy(NBTTagCompound nbt) {
		getEnergyStorage().readFromNBT(nbt, "energyStorage");
	}
	
	default NBTTagCompound writeEnergyConnections(NBTTagCompound nbt) {
		for (int i = 0; i < 6; ++i) {
			nbt.setInteger("energyConnections" + i, getEnergyConnections()[i].ordinal());
		}
		return nbt;
	}
	
	default void readEnergyConnections(NBTTagCompound nbt) {
		if (hasConfigurableEnergyConnections()) {
			for (int i = 0; i < 6; ++i) {
				if (nbt.hasKey("energyConnections" + i)) {
					getEnergyConnections()[i] = EnergyConnection.values()[nbt.getInteger("energyConnections" + i)];
				}
			}
		}
	}
	
	// Capabilities
	
	default boolean hasEnergySideCapability(@Nullable EnumFacing side) {
		return side == null || getEnergyConnection(side).canConnect();
	}
	
	// TOP
	
	@Override
	@Optional.Method(modid = "theoneprobe")
	default long getStoredPower() {
		return getEnergyStoredLong();
	}
	
	@Override
	@Optional.Method(modid = "theoneprobe")
	default long getCapacity() {
		return getMaxEnergyStoredLong();
	}
}
