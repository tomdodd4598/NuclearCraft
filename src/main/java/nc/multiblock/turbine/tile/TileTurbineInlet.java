package nc.multiblock.turbine.tile;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import nc.ModCheck;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.turbine.Turbine;
import nc.tile.fluid.ITileFluid;
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

public class TileTurbineInlet extends TileTurbinePartBase implements ITileFluid {
	
	private final @Nonnull List<Tank> backupTanks = Lists.newArrayList(new Tank(1, TankSorption.NON, new ArrayList<String>()));
	
	private @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(FluidConnection.IN);
	
	private @Nonnull FluidTileWrapper[] fluidSides;
	
	private @Nonnull GasTileWrapper gasWrapper;
	
	public TileTurbineInlet() {
		super(CuboidalPartPositionType.WALL);
		fluidSides = ITileFluid.getDefaultFluidSides(this);
		gasWrapper = new GasTileWrapper(this);
	}
	
	@Override
	public void onMachineAssembled(Turbine controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		if (getWorld().isRemote) return;
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		if (getWorld().isRemote) return;
		//getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()), 2);
	}
	
	// Fluids
	
	@Override
	@Nonnull
	public List<Tank> getTanks() {
		if (!isMultiblockAssembled()) return backupTanks;
		return getMultiblock().tanks.subList(0, 1);
	}

	@Override
	@Nonnull
	public FluidConnection[] getFluidConnections() {
		return fluidConnections;
	}
	
	@Override
	public void setFluidConnections(@Nonnull FluidConnection[] connections) {
		fluidConnections = connections;
	}

	@Override
	@Nonnull
	public FluidTileWrapper[] getFluidSides() {
		return fluidSides;
	}
	
	@Override
	public @Nonnull GasTileWrapper getGasWrapper() {
		return gasWrapper;
	}

	@Override
	public boolean getTanksShared() {
		return false;
	}

	@Override
	public void setTanksShared(boolean shared) {}

	@Override
	public boolean getEmptyUnusableTankInputs() {
		return false;
	}

	@Override
	public void setEmptyUnusableTankInputs(boolean emptyUnusableTankInputs) {}

	@Override
	public boolean getVoidExcessFluidOutputs() {
		return false;
	}

	@Override
	public void setVoidExcessFluidOutputs(boolean voidExcessFluidOutputs) {}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		writeFluidConnections(nbt);
		return nbt;
	}
		
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readFluidConnections(nbt);
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
