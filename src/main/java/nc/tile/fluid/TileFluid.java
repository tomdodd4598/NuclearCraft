package nc.tile.fluid;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import nc.ModCheck;
import nc.tile.NCTile;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.FluidTileWrapper;
import nc.tile.internal.fluid.GasTileWrapper;
import nc.tile.internal.fluid.Tank;
import nc.util.GasHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public abstract class TileFluid extends NCTile implements ITileFluid {
	
	private final @Nonnull List<Tank> tanks;
	
	private @Nonnull FluidConnection[] fluidConnections;
	
	private @Nonnull FluidTileWrapper[] fluidSides;
	
	private @Nonnull GasTileWrapper gasWrapper;
	
	private boolean inputTanksSeparated = false;
	private List<Boolean> voidUnusableFluidInputs = null;
	private List<Boolean> voidExcessFluidOutputs = null;
	
	public TileFluid(int capacity, List<String> allowedFluidsList, @Nonnull FluidConnection[] fluidConnections) {
		this(Lists.newArrayList(capacity), Lists.newArrayList(capacity), Lists.<List<String>>newArrayList(allowedFluidsList), fluidConnections);
	}
	
	public TileFluid(@Nonnull List<Integer> capacity, List<List<String>> allowedFluidsLists, @Nonnull FluidConnection[] fluidConnections) {
		this(capacity, capacity, allowedFluidsLists, fluidConnections);
	}
	
	public TileFluid(int capacity, int maxTransfer, List<String> allowedFluidsList, @Nonnull FluidConnection[] fluidConnections) {
		this(Lists.newArrayList(capacity), Lists.newArrayList(maxTransfer), Lists.<List<String>>newArrayList(allowedFluidsList), fluidConnections);
	}
	
	public TileFluid(@Nonnull List<Integer> capacity, @Nonnull List<Integer> maxTransfer, List<List<String>> allowedFluidsLists, @Nonnull FluidConnection[] fluidConnections) {
		super();
		if (capacity.isEmpty()) {
			tanks = new ArrayList<Tank>();
			voidUnusableFluidInputs = voidExcessFluidOutputs = new ArrayList<Boolean>();
		} else {
			List<Tank> tankList = new ArrayList<Tank>();
			List<Boolean> voidList = new ArrayList<Boolean>();
			for (int i = 0; i < capacity.size(); i++) {
				List<String> allowedFluidsList;
				if (allowedFluidsLists == null || allowedFluidsLists.size() <= i) allowedFluidsList = null;
				else allowedFluidsList = allowedFluidsLists.get(i);
				tankList.add(new Tank(capacity.get(i), maxTransfer.get(i), allowedFluidsList));
				voidList.add(false);
			}
			tanks = tankList;
			voidUnusableFluidInputs = voidExcessFluidOutputs = voidList;
		}
		this.fluidConnections = fluidConnections;
		fluidSides = ITileFluid.getDefaultFluidSides(this);
		gasWrapper = new GasTileWrapper(this);
	}
	
	@Override
	public @Nonnull List<Tank> getTanks() {
		return tanks;
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
		return inputTanksSeparated;
	}
	
	@Override
	public void setInputTanksSeparated(boolean shared) {
		inputTanksSeparated = shared;
	}
	
	@Override
	public boolean getVoidUnusableFluidInput(int tankNumber) {
		return voidUnusableFluidInputs.get(tankNumber);
	}
	
	@Override
	public void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput) {
		voidUnusableFluidInputs.set(tankNumber, voidUnusableFluidInput);
	}
	
	@Override
	public boolean getVoidExcessFluidOutput(int tankNumber) {
		return voidExcessFluidOutputs.get(tankNumber);
	}
	
	@Override
	public void setVoidExcessFluidOutput(int tankNumber, boolean voidExcessFluidOutput) {
		voidExcessFluidOutputs.set(tankNumber, voidExcessFluidOutput);
	}
	
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
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (!getTanks().isEmpty() && hasFluidSideCapability(side)) {
			side = nonNullSide(side);
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return getFluidSide(side) != null;
			if (ModCheck.mekanismLoaded()) if (GasHelper.isGasCapability(capability)) return getGasWrapper() != null;
		}
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (!getTanks().isEmpty() && hasFluidSideCapability(side)) {
			side = nonNullSide(side);
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return (T) getFluidSide(side);
			if (ModCheck.mekanismLoaded()) if (GasHelper.isGasCapability(capability)) return (T) getGasWrapper();
		}
		return super.getCapability(capability, side);
	}
}
