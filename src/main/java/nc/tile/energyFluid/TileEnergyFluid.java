package nc.tile.energyFluid;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import nc.ModCheck;
import nc.tile.energy.TileEnergy;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.FluidTileWrapper;
import nc.tile.internal.fluid.GasTileWrapper;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.TankSorption;
import nc.util.GasHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public abstract class TileEnergyFluid extends TileEnergy implements ITileFluid {
	
	private final @Nonnull List<Tank> tanks;
	
	private @Nonnull FluidConnection[] fluidConnections;
	
	private @Nonnull FluidTileWrapper[] fluidSides;
	
	private @Nonnull GasTileWrapper gasWrapper;
	
	private boolean areTanksShared = false;
	private boolean emptyUnusableTankInputs = false;
	private boolean voidExcessFluidOutputs = false;
	
	public TileEnergyFluid(int capacity, @Nonnull EnergyConnection[] energyConnections, int fluidCapacity, @Nonnull TankSorption tankSorption, List<String> allowedFluidsList, @Nonnull FluidConnection[] fluidConnections) {
		this(capacity, capacity, energyConnections, Lists.newArrayList(fluidCapacity), Lists.newArrayList(fluidCapacity), Lists.newArrayList(tankSorption), Lists.<List<String>>newArrayList(allowedFluidsList), fluidConnections);
	}
	
	public TileEnergyFluid(int capacity, @Nonnull EnergyConnection[] energyConnections, @Nonnull List<Integer> fluidCapacity, @Nonnull List<TankSorption> tankSorptions, List<List<String>> allowedFluidsLists, @Nonnull FluidConnection[] fluidConnections) {
		this(capacity, capacity, energyConnections, fluidCapacity, fluidCapacity, tankSorptions, allowedFluidsLists, fluidConnections);
	}
	
	public TileEnergyFluid(int capacity, @Nonnull EnergyConnection[] energyConnections, int fluidCapacity, int maxFluidTransfer, @Nonnull TankSorption tankSorption, List<String> allowedFluidsList, @Nonnull FluidConnection[] fluidConnections) {
		this(capacity, capacity, energyConnections, Lists.newArrayList(fluidCapacity), Lists.newArrayList(maxFluidTransfer), Lists.newArrayList(tankSorption), Lists.<List<String>>newArrayList(allowedFluidsList), fluidConnections);
	}
	
	public TileEnergyFluid(int capacity, @Nonnull EnergyConnection[] energyConnections, @Nonnull List<Integer> fluidCapacity, @Nonnull List<Integer> maxFluidTransfer, @Nonnull List<TankSorption> tankSorptions, List<List<String>> allowedFluidsLists, @Nonnull FluidConnection[] fluidConnections) {
		this(capacity, capacity, energyConnections, fluidCapacity, maxFluidTransfer, tankSorptions, allowedFluidsLists, fluidConnections);
	}
	
	public TileEnergyFluid(int capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections, int fluidCapacity, @Nonnull TankSorption tankSorption, List<String> allowedFluidsList, @Nonnull FluidConnection[] fluidConnections) {
		this(capacity, maxTransfer, energyConnections, Lists.newArrayList(fluidCapacity), Lists.newArrayList(fluidCapacity), Lists.newArrayList(tankSorption), Lists.<List<String>>newArrayList(allowedFluidsList), fluidConnections);
	}
	
	public TileEnergyFluid(int capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections, @Nonnull List<Integer> fluidCapacity, @Nonnull List<TankSorption> tankSorptions, List<List<String>> allowedFluidsLists, @Nonnull FluidConnection[] fluidConnections) {
		this(capacity, maxTransfer, energyConnections, fluidCapacity, fluidCapacity, tankSorptions, allowedFluidsLists, fluidConnections);
	}
	
	public TileEnergyFluid(int capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections, int fluidCapacity, int maxFluidTransfer, @Nonnull TankSorption tankSorption, List<String> allowedFluidsList, @Nonnull FluidConnection[] fluidConnections) {
		this(capacity, maxTransfer, energyConnections, Lists.newArrayList(fluidCapacity), Lists.newArrayList(maxFluidTransfer), Lists.newArrayList(tankSorption), Lists.<List<String>>newArrayList(allowedFluidsList), fluidConnections);
	}
	
	public TileEnergyFluid(int capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections, @Nonnull List<Integer> fluidCapacity, @Nonnull List<Integer> maxFluidTransfer, @Nonnull List<TankSorption> tankSorptions, List<List<String>> allowedFluidsLists, @Nonnull FluidConnection[] fluidConnections) {
		super(capacity, maxTransfer, energyConnections);
		if (fluidCapacity.isEmpty()) {
			tanks = new ArrayList<Tank>();
		} else {
			List<Tank> tankList = new ArrayList<Tank>();
			for (int i = 0; i < fluidCapacity.size(); i++) {
				List<String> allowedFluidsList;
				if (allowedFluidsLists == null || allowedFluidsLists.size() <= i) allowedFluidsList = null;
				else allowedFluidsList = allowedFluidsLists.get(i);
				tankList.add(new Tank(fluidCapacity.get(i), tankSorptions.get(i), allowedFluidsList));
			}
			tanks = tankList;
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
	public @Nonnull FluidTileWrapper[] getFluidSides() {
		return fluidSides;
	}
	
	@Override
	public @Nonnull GasTileWrapper getGasWrapper() {
		return gasWrapper;
	}
	
	@Override
	public boolean getTanksShared() {
		return areTanksShared;
	}
	
	@Override
	public void setTanksShared(boolean shared) {
		areTanksShared = shared;
	}
	
	@Override
	public boolean getEmptyUnusableTankInputs() {
		return emptyUnusableTankInputs;
	}
	
	@Override
	public void setEmptyUnusableTankInputs(boolean emptyUnusableTankInputs) {
		this.emptyUnusableTankInputs = emptyUnusableTankInputs;
	}
	
	@Override
	public boolean getVoidExcessFluidOutputs() {
		return voidExcessFluidOutputs;
	}
	
	@Override
	public void setVoidExcessFluidOutputs(boolean voidExcessFluidOutputs) {
		this.voidExcessFluidOutputs = voidExcessFluidOutputs;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		writeTanks(nbt);
		writeFluidConnections(nbt);
		nbt.setBoolean("areTanksShared", areTanksShared);
		nbt.setBoolean("emptyUnusable", emptyUnusableTankInputs);
		nbt.setBoolean("voidExcessOutputs", voidExcessFluidOutputs);
		return nbt;
	}
		
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readTanks(nbt);
		readFluidConnections(nbt);
		setTanksShared(nbt.getBoolean("areTanksShared"));
		setEmptyUnusableTankInputs(nbt.getBoolean("emptyUnusable"));
		setVoidExcessFluidOutputs(nbt.getBoolean("voidExcessOutputs"));
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
