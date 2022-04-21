package nc.tile.energyFluid;

import static nc.config.NCConfig.enable_mek_gas;

import java.util.*;

import javax.annotation.*;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.ints.*;
import nc.ModCheck;
import nc.tile.energy.TileEnergy;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.*;
import nc.util.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public abstract class TileEnergyFluid extends TileEnergy implements ITileFluid {
	
	private final @Nonnull List<Tank> tanks;
	
	private @Nonnull FluidConnection[] fluidConnections;
	
	private final @Nonnull FluidTileWrapper[] fluidSides;
	
	private final @Nonnull GasTileWrapper gasWrapper;
	
	private boolean inputTanksSeparated = false;
	private final List<Boolean> voidUnusableFluidInputs;
	private final List<TankOutputSetting> tankOutputSettings;
	
	public TileEnergyFluid(long capacity, @Nonnull EnergyConnection[] energyConnections, int fluidCapacity, List<String> allowedFluidsList, @Nonnull FluidConnection[] fluidConnections) {
		this(capacity, NCMath.toInt(capacity), energyConnections, new IntArrayList(new int[] {fluidCapacity}), Lists.<List<String>>newArrayList(allowedFluidsList), fluidConnections);
	}
	
	public TileEnergyFluid(long capacity, @Nonnull EnergyConnection[] energyConnections, @Nonnull IntList fluidCapacity, List<List<String>> allowedFluidsLists, @Nonnull FluidConnection[] fluidConnections) {
		this(capacity, NCMath.toInt(capacity), energyConnections, fluidCapacity, allowedFluidsLists, fluidConnections);
	}
	
	public TileEnergyFluid(long capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections, int fluidCapacity, List<String> allowedFluidsList, @Nonnull FluidConnection[] fluidConnections) {
		this(capacity, maxTransfer, energyConnections, new IntArrayList(new int[] {fluidCapacity}), Lists.<List<String>>newArrayList(allowedFluidsList), fluidConnections);
	}
	
	public TileEnergyFluid(long capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections, @Nonnull IntList fluidCapacity, List<List<String>> allowedFluidsLists, @Nonnull FluidConnection[] fluidConnections) {
		super(capacity, maxTransfer, energyConnections);
		tanks = new ArrayList<>();
		voidUnusableFluidInputs = new ArrayList<>();
		tankOutputSettings = new ArrayList<>();
		if (!fluidCapacity.isEmpty()) {
			for (int i = 0; i < fluidCapacity.size(); ++i) {
				tanks.add(new Tank(fluidCapacity.get(i), allowedFluidsLists == null || allowedFluidsLists.size() <= i ? null : allowedFluidsLists.get(i)));
				voidUnusableFluidInputs.add(false);
				tankOutputSettings.add(TankOutputSetting.DEFAULT);
			}
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
	public TankOutputSetting getTankOutputSetting(int tankNumber) {
		return tankOutputSettings.get(tankNumber);
	}
	
	@Override
	public void setTankOutputSetting(int tankNumber, TankOutputSetting setting) {
		tankOutputSettings.set(tankNumber, setting);
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
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || ModCheck.mekanismLoaded() && enable_mek_gas && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY) {
			return !getTanks().isEmpty() && hasFluidSideCapability(side);
		}
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			if (!getTanks().isEmpty() && hasFluidSideCapability(side)) {
				return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getFluidSide(nonNullSide(side)));
			}
			return null;
		}
		else if (ModCheck.mekanismLoaded() && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY) {
			if (enable_mek_gas && !getTanks().isEmpty() && hasFluidSideCapability(side)) {
				return CapabilityHelper.GAS_HANDLER_CAPABILITY.cast(getGasWrapper());
			}
			return null;
		}
		return super.getCapability(capability, side);
	}
}
