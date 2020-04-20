package nc.tile.energy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import gregtech.api.capability.GregtechCapabilities;
import gregtech.api.capability.IEnergyContainer;
import ic2.api.energy.tile.IEnergySink;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.tile.ITile;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.energy.EnergyStorage;
import nc.tile.internal.energy.EnergyTileWrapper;
import nc.tile.internal.energy.EnergyTileWrapperGT;
import nc.tile.passive.ITilePassive;
import nc.util.EnergyHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;

public interface ITileEnergy extends ITile {
	
	// Storage
	
	public EnergyStorage getEnergyStorage();
	
	// Energy Connection
	
	public EnergyConnection[] getEnergyConnections();
	
	public default EnergyConnection getEnergyConnection(@Nonnull EnumFacing side) {
		return getEnergyConnections()[side.getIndex()];
	}
	
	public default void setEnergyConnection(@Nonnull EnergyConnection energyConnection, @Nonnull EnumFacing side) {
		getEnergyConnections()[side.getIndex()] = energyConnection;
	}
	
	public default void toggleEnergyConnection(@Nonnull EnumFacing side, @Nonnull EnergyConnection.Type type) {
		if (ModCheck.ic2Loaded()) removeTileFromENet();
		setEnergyConnection(getEnergyConnection(side).next(type), side);
		markDirtyAndNotify();
		if (ModCheck.ic2Loaded()) addTileToENet();
	}
	
	public default boolean canConnectEnergy(@Nonnull EnumFacing side) {
		return getEnergyConnection(side).canConnect();
	}
	
	public static EnergyConnection[] energyConnectionAll(@Nonnull EnergyConnection connection) {
		EnergyConnection[] array = new EnergyConnection[6];
		for (int i = 0; i < 6; i++) array[i] = connection;
		return array;
	}
	
	public default boolean hasConfigurableEnergyConnections() {
		return false;
	}
	
	// Energy Connection Wrapper Methods
	
	public default int getEnergyStored() {
		return getEnergyStorage().getEnergyStored();
	}
	
	public default int getMaxEnergyStored() {
		return getEnergyStorage().getMaxEnergyStored();
	}
	
	public default boolean canReceiveEnergy(EnumFacing side) {
		return getEnergyConnection(side).canReceive();
	}
	
	public default boolean canExtractEnergy(EnumFacing side) {
		return getEnergyConnection(side).canExtract();
	}
	
	public default int receiveEnergy(int maxReceive, EnumFacing side, boolean simulate) {
		return canReceiveEnergy(side) ? getEnergyStorage().receiveEnergy(maxReceive, simulate) : 0;
	}
	
	public default int extractEnergy(int maxExtract, EnumFacing side, boolean simulate) {
		return canExtractEnergy(side) ? getEnergyStorage().extractEnergy(maxExtract, simulate) : 0;
	}
	
	// IC2 EU
	
	public abstract int getEUSourceTier();
	
	public abstract int getEUSinkTier();
	
	@Optional.Method(modid = "ic2")
	public void addTileToENet();
	
	@Optional.Method(modid = "ic2")
	public void removeTileFromENet();
	
	// Energy Wrappers
	
	public @Nonnull EnergyTileWrapper[] getEnergySides();
	
	public @Nonnull EnergyTileWrapperGT[] getEnergySidesGT();
	
	public default @Nonnull EnergyTileWrapper getEnergySide(@Nonnull EnumFacing side) {
		return getEnergySides()[side.getIndex()];
	}
	
	public default @Nonnull EnergyTileWrapperGT getEnergySideGT(@Nonnull EnumFacing side) {
		return getEnergySidesGT()[side.getIndex()];
	}
	
	public static @Nonnull EnergyTileWrapper[] getDefaultEnergySides(@Nonnull ITileEnergy tile) {
		return new EnergyTileWrapper[] {new EnergyTileWrapper(tile, EnumFacing.DOWN), new EnergyTileWrapper(tile, EnumFacing.UP), new EnergyTileWrapper(tile, EnumFacing.NORTH), new EnergyTileWrapper(tile, EnumFacing.SOUTH), new EnergyTileWrapper(tile, EnumFacing.WEST), new EnergyTileWrapper(tile, EnumFacing.EAST)};
	}
	
	public static @Nonnull EnergyTileWrapperGT[] getDefaultEnergySidesGT(@Nonnull ITileEnergy tile) {
		return new EnergyTileWrapperGT[] {new EnergyTileWrapperGT(tile, EnumFacing.DOWN), new EnergyTileWrapperGT(tile, EnumFacing.UP), new EnergyTileWrapperGT(tile, EnumFacing.NORTH), new EnergyTileWrapperGT(tile, EnumFacing.SOUTH), new EnergyTileWrapperGT(tile, EnumFacing.WEST), new EnergyTileWrapperGT(tile, EnumFacing.EAST)};
	}
	
	// Energy Distribution
	
	public default void pushEnergy() {
		for (EnumFacing side : EnumFacing.VALUES) {
			if (getEnergyStorage().getEnergyStored() <= 0) return;
			pushEnergyToSide(side);
		}
	}
	
	/*public default void spreadEnergy() {
		if (!NCConfig.passive_permeation) return;
		for (EnumFacing side : EnumFacing.VALUES) {
			if (getEnergyStorage().getEnergyStored() <= 0) return;
			spreadEnergyToSide(side);
		}
	}*/
	
	public default void pushEnergyToSide(@Nonnull EnumFacing side) {
		if (!getEnergyConnection(side).canExtract()) return;
		
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		if (tile == null) return;
		
		if (tile instanceof ITileEnergy) if (!((ITileEnergy)tile).getEnergyConnection(side.getOpposite()).canReceive()) return;
		if (tile instanceof ITilePassive) if (!((ITilePassive)tile).canPushEnergyTo()) return;
		
		IEnergyStorage adjStorage = tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
		
		if (adjStorage != null && getEnergyStorage().canExtract()) {
			getEnergyStorage().extractEnergy(adjStorage.receiveEnergy(getEnergyStorage().extractEnergy(getEnergyStorage().getMaxEnergyStored(), true), false), false);
			return;
		}
		
		if (getEnergyStorage().getEnergyStored() < NCConfig.rf_per_eu) return;
		
		if (ModCheck.ic2Loaded()) {
			if (tile instanceof IEnergySink) {
				getEnergyStorage().extractEnergy((int) Math.round(((IEnergySink)tile).injectEnergy(side.getOpposite(), getEnergyStorage().extractEnergy(getEnergyStorage().getMaxEnergyStored(), true)/NCConfig.rf_per_eu, getEUSourceTier())*NCConfig.rf_per_eu), false);
				return;
			}
		}
		if (NCConfig.enable_gtce_eu && ModCheck.gregtechLoaded()) {
			IEnergyContainer adjStorageGT = tile.getCapability(GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER, side.getOpposite());
			if (adjStorageGT != null && getEnergyStorage().canExtract()) {
				int voltage = MathHelper.clamp(getEnergyStorage().getEnergyStored()/NCConfig.rf_per_eu, 1, EnergyHelper.getMaxEUFromTier(getEUSourceTier()));
				getEnergyStorage().extractEnergy((int)Math.min(voltage*adjStorageGT.acceptEnergyFromNetwork(side.getOpposite(), voltage, 1)*NCConfig.rf_per_eu, Integer.MAX_VALUE), false);
				return;
			}
		}
	}
	
	public default void spreadEnergyToSide(@Nonnull EnumFacing side) {
		if (!getEnergyConnection(side).canConnect()) return;
		
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		
		if (tile instanceof IEnergySpread) {
			if (tile instanceof ITilePassive && !((ITilePassive)tile).canPushEnergyTo()) return;
			
			IEnergySpread other = (IEnergySpread) tile;
			
			int diff = getEnergyStorage().getEnergyStored() - other.getEnergyStorage().getEnergyStored();
			if (diff > 1) {
				getEnergyStorage().extractEnergy(other.getEnergyStorage().receiveEnergy(getEnergyStorage().extractEnergy(diff/2, true), false), false);
			}
		}
	}
	
	// NBT
	
	public default NBTTagCompound writeEnergy(NBTTagCompound nbt) {
		nbt.setInteger("energy", getEnergyStorage().getEnergyStored());
		nbt.setInteger("capacity", getEnergyStorage().getMaxEnergyStored());
		nbt.setInteger("maxTransfer", getEnergyStorage().getMaxTransfer());
		return nbt;
	}
	
	public default void readEnergy(NBTTagCompound nbt) {
		getEnergyStorage().setEnergyStored(nbt.getInteger("energy"));
		getEnergyStorage().setStorageCapacity(nbt.getInteger("capacity"));
		getEnergyStorage().setMaxTransfer(nbt.getInteger("maxTransfer"));
	}
	
	public default NBTTagCompound writeEnergyConnections(NBTTagCompound nbt) {
		for (int i = 0; i < 6; i++) nbt.setInteger("energyConnections" + i, getEnergyConnections()[i].ordinal());
		return nbt;
	}
	
	public default void readEnergyConnections(NBTTagCompound nbt) {
		if (hasConfigurableEnergyConnections()) for (int i = 0; i < 6; i++) {
			if (nbt.hasKey("energyConnections" + i)) getEnergyConnections()[i] = EnergyConnection.values()[nbt.getInteger("energyConnections" + i)];
		}
	}
	
	// Capabilities
	
	public default boolean hasEnergySideCapability(@Nullable EnumFacing side) {
		return side == null || getEnergyConnection(side).canConnect();
	}
}
