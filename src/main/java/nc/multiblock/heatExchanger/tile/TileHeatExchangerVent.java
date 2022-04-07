package nc.multiblock.heatExchanger.tile;

import static nc.block.property.BlockProperties.AXIS_ALL;
import static nc.config.NCConfig.*;

import java.util.List;

import javax.annotation.*;

import com.google.common.collect.Lists;

import nc.ModCheck;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.heatExchanger.*;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.*;
import nc.util.CapabilityHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TileHeatExchangerVent extends TileHeatExchangerPart implements ITickable, ITileFluid {
	
	private final @Nonnull List<Tank> tanks = Lists.newArrayList(new Tank(128000, null));
	
	private @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(TankSorption.BOTH);
	
	private @Nonnull final FluidTileWrapper[] fluidSides;
	
	private @Nonnull final GasTileWrapper gasWrapper;
	
	protected int ventCount;
	
	public TileHeatExchangerVent() {
		super(CuboidalPartPositionType.WALL);
		fluidSides = ITileFluid.getDefaultFluidSides(this);
		gasWrapper = new GasTileWrapper(this);
	}
	
	@Override
	public void onMachineAssembled(HeatExchanger controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		if (!getWorld().isRemote && getPartPosition().getFacing() != null) {
			getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()).withProperty(AXIS_ALL, getPartPosition().getFacing().getAxis()), 2);
		}
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			if (ventCount == 0) {
				pushFluid();
			}
			tickVent();
		}
	}
	
	public void tickVent() {
		++ventCount;
		ventCount %= machine_update_rate / 4;
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
		if (!getTankSorption(side, 0).canDrain()) {
			return;
		}
		
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		if (tile instanceof TileHeatExchangerTube) {
			TileHeatExchangerTube tube = (TileHeatExchangerTube) tile;
			
			if (tube.getTubeSetting(side.getOpposite()) == HeatExchangerTubeSetting.DEFAULT) {
				getTanks().get(0).drain(tube.getTanks().get(0).fill(getTanks().get(0).drain(getTanks().get(0).getCapacity(), false), true), true);
			}
		}
		else if (tile instanceof TileCondenserTube) {
			TileCondenserTube condenserTube = (TileCondenserTube) tile;
			
			if (condenserTube.getTubeSetting(side.getOpposite()) == HeatExchangerTubeSetting.DEFAULT) {
				getTanks().get(0).drain(condenserTube.getTanks().get(0).fill(getTanks().get(0).drain(getTanks().get(0).getCapacity(), false), true), true);
			}
		}
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
