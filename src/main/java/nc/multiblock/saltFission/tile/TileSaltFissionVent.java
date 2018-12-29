package nc.multiblock.saltFission.tile;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import nc.ModCheck;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.saltFission.SaltFissionReactor;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.FluidTileWrapper;
import nc.tile.internal.fluid.GasTileWrapper;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.TankSorption;
import nc.util.FluidStackHelper;
import nc.util.GasHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TileSaltFissionVent extends TileSaltFissionPartBase implements ITileFluid {
	
	private final @Nonnull List<Tank> tanks = Lists.newArrayList(new Tank(FluidStackHelper.INGOT_BLOCK_VOLUME*4, TankSorption.BOTH, null));

	private @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(FluidConnection.BOTH);
	
	private @Nonnull FluidTileWrapper[] fluidSides;
	
	private @Nonnull GasTileWrapper gasWrapper;
	
	public TileSaltFissionVent() {
		super(CuboidalPartPositionType.WALL);
		fluidSides = ITileFluid.getDefaultFluidSides(this);
		gasWrapper = new GasTileWrapper(this);
	}
	
	@Override
	public void onMachineAssembled(SaltFissionReactor controller) {
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
	
	@Override
	public void update() {
		super.update();
		if(!world.isRemote) {
			pushFluid();
		}
	}
	
	// Fluids
	
	@Override
	@Nonnull
	public List<Tank> getTanks() {
		return tanks;
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
	public void pushFluidToSide(@Nonnull EnumFacing side) {
		if (!getFluidConnection(side).canDrain()) return;
		
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		if (!(tile instanceof TileSaltFissionVessel) && !(tile instanceof TileSaltFissionHeater)) return;
		ITileFluid holder = (ITileFluid) tile;
		
		if (holder.getFluidConnection(side.getOpposite()) == FluidConnection.BOTH) {
			getTanks().get(0).drainInternal(holder.getTanks().get(0).fill(getTanks().get(0).drainInternal(getTanks().get(0).getCapacity(), false), true), true);
		}
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
		writeTanks(nbt);
		writeFluidConnections(nbt);
		return nbt;
	}
		
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readTanks(nbt);
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
