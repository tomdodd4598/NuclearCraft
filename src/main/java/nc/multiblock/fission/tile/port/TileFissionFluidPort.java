package nc.multiblock.fission.tile.port;

import static nc.util.BlockPosHelper.DEFAULT_NON;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import nc.ModCheck;
import nc.config.NCConfig;
import nc.recipe.ProcessorRecipeHandler;
import nc.tile.fluid.ITileFilteredFluid;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.FluidTileWrapper;
import nc.tile.internal.fluid.GasTileWrapper;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.TankOutputSetting;
import nc.tile.internal.fluid.TankSorption;
import nc.util.GasHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public abstract class TileFissionFluidPort<PORT extends TileFissionFluidPort<PORT, TARGET> & ITileFilteredFluid, TARGET extends IFissionPortTarget<PORT, TARGET> & ITileFilteredFluid> extends TileFissionPort<PORT, TARGET> implements ITileFilteredFluid {
	
	protected final @Nonnull List<Tank> tanks;
	protected final @Nonnull List<Tank> filterTanks;
	protected final int capacity;
	
	protected @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(Lists.newArrayList(TankSorption.IN, TankSorption.OUT));
	
	protected @Nonnull FluidTileWrapper[] fluidSides;
	
	protected @Nonnull GasTileWrapper gasWrapper;
	
	protected final ProcessorRecipeHandler recipeHandler;
	
	public TileFissionFluidPort(Class<PORT> portClass, int capacity, List<String> validFluids, ProcessorRecipeHandler recipeHandler) {
		super(portClass);
		tanks = Lists.newArrayList(new Tank(capacity, validFluids), new Tank(capacity, new ArrayList<>()));
		filterTanks = Lists.newArrayList(new Tank(1000, validFluids), new Tank(1000, new ArrayList<>()));
		this.capacity = capacity;
		fluidSides = ITileFluid.getDefaultFluidSides(this);
		gasWrapper = new GasTileWrapper(this);
		this.recipeHandler = recipeHandler;
	}
	
	@Override
	public void setInventoryStackLimit(int stackLimit) {}
	
	@Override
	public int getTankBaseCapacity() {
		return capacity;
	}
	
	@Override
	public void setTankCapacity(int capacity) {
		tanks.get(0).setCapacity(capacity);
		tanks.get(1).setCapacity(capacity);
	}
	
	@Override
	public boolean canModifyFilter(int tank) {
		return getMultiblock() != null ? !getMultiblock().isAssembled() : true;
	}
	
	@Override
	public void onFilterChanged(int tank) {
		/*if (!canModifyFilter(tank)) {
			getMultiblock().getLogic().refreshPorts();
		}*/
		markDirty();
	}
	
	@Override
	public int getFilterID() {
		return getFilterTanks().get(0).getFluidName().hashCode();
	}
	
	/*@Override
	public void markDirty() {
		super.markDirty();
	}*/
	
	// Fluids
	
	@Override
	public @Nonnull List<Tank> getTanks() {
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getTanks() : tanks;
	}
	
	@Override
	public @Nonnull List<Tank> getTanksInternal() {
		return tanks;
	}
	
	@Override
	public @Nonnull List<Tank> getFilterTanks() {
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getFilterTanks() : filterTanks;
	}
	
	@Override
	public @Nonnull FluidConnection[] getFluidConnections() {
		return fluidConnections;
	}
	
	@Override
	public void setFluidConnections(@Nonnull FluidConnection[] connections) {
		fluidConnections = connections;
	}
	
	@Override
	public @Nonnull FluidTileWrapper[] getFluidSides() {
		return fluidSides;
	}

	@Override
	public @Nonnull GasTileWrapper getGasWrapper() {
		return gasWrapper;
	}
	
	@Override
	public boolean getInputTanksSeparated() {
		return false;
	}

	@Override
	public void setInputTanksSeparated(boolean separated) {}

	@Override
	public boolean getVoidUnusableFluidInput(int tankNumber) {
		return false;
	}

	@Override
	public void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput) {}
	
	@Override
	public TankOutputSetting getTankOutputSetting(int tankNumber) {
		return TankOutputSetting.DEFAULT;
	}
	
	@Override
	public void setTankOutputSetting(int tankNumber, TankOutputSetting setting) {}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		writeTanks(nbt);
		writeFluidConnections(nbt);
		writeTankSettings(nbt);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readTanks(nbt);
		readFluidConnections(nbt);
		readTankSettings(nbt);
	}
	
	@Override
	public NBTTagCompound writeTanks(NBTTagCompound nbt) {
		ITileFilteredFluid.super.writeTanks(nbt);
		for (int i = 0; i < filterTanks.size(); i++) {
			getTanks().get(i).writeToNBT(nbt, "filterTanks" + i + filterTanks.size());
		}
		return nbt;
	}
	
	@Override
	public void readTanks(NBTTagCompound nbt) {
		ITileFilteredFluid.super.readTanks(nbt);
		for (int i = 0; i < filterTanks.size(); i++) {
			getTanks().get(i).readFromNBT(nbt, "filterTanks" + i + filterTanks.size());
		}
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || (ModCheck.mekanismLoaded() && NCConfig.enable_mek_gas && capability == GasHelper.GAS_HANDLER_CAPABILITY)) {
			return !getTanks().isEmpty() && hasFluidSideCapability(side);
		}
		return super.hasCapability(capability, side);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			if (!getTanks().isEmpty() && hasFluidSideCapability(side)) {
				return (T) getFluidSide(nonNullSide(side));
			}
			return null;
		}
		else if (ModCheck.mekanismLoaded() && capability == GasHelper.GAS_HANDLER_CAPABILITY) {
			if (NCConfig.enable_mek_gas && !getTanks().isEmpty() && hasFluidSideCapability(side)) {
				return (T) getGasWrapper();
			}
			return null;
		}
		return super.getCapability(capability, side);
	}
}
