package nc.tile.fluid;

import java.util.List;

import javax.annotation.*;

import com.google.common.collect.Lists;

import mekanism.api.gas.GasStack;
import nc.multiblock.tile.port.ITilePort;
import nc.tile.ITile;
import nc.tile.internal.fluid.*;
import nc.tile.passive.ITilePassive;
import nc.tile.processor.IProcessor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.*;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;

public interface ITileFluid extends ITile {
	
	// Tanks
	
	public @Nonnull List<Tank> getTanks();
	
	// Tank Logic
	
	/** Only concerns ordering, not whether fluid is actually valid for the tank due to filters or sorption */
	public default boolean isNextToFill(@Nonnull EnumFacing side, int tankNumber, FluidStack resource) {
		if (!getInputTanksSeparated()) {
			return true;
		}
		for (int i = 0; i < getTanks().size(); ++i) {
			Tank tank = getTanks().get(i);
			if (i != tankNumber && getTankSorption(side, i).canFill()) {
				FluidStack fluid = tank.getFluid();
				if (fluid != null && fluid.isFluidEqual(resource)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public default void clearTank(int tankNumber) {
		getTanks().get(tankNumber).setFluidStored(null);
	}
	
	public default void clearAllTanks() {
		for (Tank tank : getTanks()) {
			tank.setFluidStored(null);
		}
	}
	
	// Fluid Connections
	
	public @Nonnull FluidConnection[] getFluidConnections();
	
	public void setFluidConnections(@Nonnull FluidConnection[] connections);
	
	public default @Nonnull FluidConnection getFluidConnection(@Nonnull EnumFacing side) {
		return getFluidConnections()[side.getIndex()];
	}
	
	public default @Nonnull TankSorption getTankSorption(@Nonnull EnumFacing side, int tankNumber) {
		return getFluidConnections()[side.getIndex()].getTankSorption(tankNumber);
	}
	
	public default void setTankSorption(@Nonnull EnumFacing side, int tankNumber, @Nonnull TankSorption sorption) {
		getFluidConnections()[side.getIndex()].setTankSorption(tankNumber, sorption);
	}
	
	public default void toggleTankSorption(@Nonnull EnumFacing side, int tankNumber, TankSorption.Type type, boolean reverse) {
		if (!hasConfigurableFluidConnections()) {
			return;
		}
		getFluidConnection(side).toggleTankSorption(tankNumber, type, reverse);
		markDirtyAndNotify(true);
	}
	
	public default boolean canConnectFluid(@Nonnull EnumFacing side) {
		return getFluidConnection(side).canConnect();
	}
	
	public static FluidConnection[] fluidConnectionAll(@Nonnull List<TankSorption> sorptionList) {
		FluidConnection[] array = new FluidConnection[6];
		for (int i = 0; i < 6; ++i) {
			array[i] = new FluidConnection(sorptionList);
		}
		return array;
	}
	
	public static FluidConnection[] fluidConnectionAll(TankSorption sorption) {
		return fluidConnectionAll(Lists.newArrayList(sorption));
	}
	
	public default boolean hasConfigurableFluidConnections() {
		return false;
	}
	
	// Fluid Wrapper Methods
	
	public default @Nonnull IFluidTankProperties[] getTankProperties(@Nonnull EnumFacing side) {
		if (getTanks().isEmpty()) {
			return EmptyFluidHandler.EMPTY_TANK_PROPERTIES_ARRAY;
		}
		IFluidTankProperties[] properties = new IFluidTankProperties[getTanks().size()];
		for (int i = 0; i < getTanks().size(); ++i) {
			properties[i] = getTanks().get(i).getFluidTankProperties();
		}
		return properties;
	}
	
	public default int fill(@Nonnull EnumFacing side, FluidStack resource, boolean doFill) {
		for (int i = 0; i < getTanks().size(); ++i) {
			if (getTankSorption(side, i).canFill()) {
				Tank tank = getTanks().get(i);
				if (tank.canFillFluidType(resource) && isNextToFill(side, i, resource) && tank.getFluidAmount() < tank.getCapacity()) {
					FluidStack fluid = tank.getFluid();
					if (fluid == null || fluid.isFluidEqual(resource)) {
						return tank.fill(resource, doFill);
					}
				}
			}
		}
		return 0;
	}
	
	public default FluidStack drain(@Nonnull EnumFacing side, FluidStack resource, boolean doDrain) {
		for (int i = 0; i < getTanks().size(); ++i) {
			if (getTankSorption(side, i).canDrain()) {
				Tank tank = getTanks().get(i);
				if (tank.getFluidAmount() > 0 && resource.isFluidEqual(tank.getFluid()) && tank.drain(resource, false) != null) {
					if (tank.drain(resource, false) != null) {
						return tank.drain(resource, doDrain);
					}
				}
			}
		}
		return null;
	}
	
	public default FluidStack drain(@Nonnull EnumFacing side, int maxDrain, boolean doDrain) {
		for (int i = 0; i < getTanks().size(); ++i) {
			if (getTankSorption(side, i).canDrain()) {
				Tank tank = getTanks().get(i);
				if (tank.getFluidAmount() > 0 && tank.drain(maxDrain, false) != null) {
					if (tank.drain(maxDrain, false) != null) {
						return tank.drain(maxDrain, doDrain);
					}
				}
			}
		}
		return null;
	}
	
	// Fluid Wrappers
	
	public @Nonnull FluidTileWrapper[] getFluidSides();
	
	public default @Nonnull FluidTileWrapper getFluidSide(@Nonnull EnumFacing side) {
		return getFluidSides()[side.getIndex()];
	}
	
	public static @Nonnull FluidTileWrapper[] getDefaultFluidSides(@Nonnull ITileFluid tile) {
		return new FluidTileWrapper[] {new FluidTileWrapper(tile, EnumFacing.DOWN), new FluidTileWrapper(tile, EnumFacing.UP), new FluidTileWrapper(tile, EnumFacing.NORTH), new FluidTileWrapper(tile, EnumFacing.SOUTH), new FluidTileWrapper(tile, EnumFacing.WEST), new FluidTileWrapper(tile, EnumFacing.EAST)};
	}
	
	public default void onWrapperFill(int fillAmount, boolean doFill) {
		if (doFill && fillAmount != 0) {
			if (this instanceof IProcessor) {
				((IProcessor<?, ?>) this).refreshRecipe();
				((IProcessor<?, ?>) this).refreshActivity();
			}
			if (this instanceof ITilePort) {
				((ITilePort<?, ?, ?, ?, ?>) this).setRefreshTargetsFlag(true);
			}
		}
	}
	
	public default void onWrapperDrain(FluidStack drainStack, boolean doDrain) {
		if (doDrain && drainStack != null && drainStack.amount != 0) {
			if (this instanceof IProcessor) {
				((IProcessor<?, ?>) this).refreshActivity();
			}
			if (this instanceof ITilePort) {
				((ITilePort<?, ?, ?, ?, ?>) this).setRefreshTargetsFlag(true);
			}
		}
	}
	
	public default void onWrapperReceiveGas(int receiveAmount, boolean doTransfer) {
		if (doTransfer && receiveAmount != 0) {
			if (this instanceof IProcessor) {
				((IProcessor<?, ?>) this).refreshRecipe();
				((IProcessor<?, ?>) this).refreshActivity();
			}
			if (this instanceof ITilePort) {
				((ITilePort<?, ?, ?, ?, ?>) this).setRefreshTargetsFlag(true);
			}
		}
	}
	
	public default void onWrapperDrawGas(GasStack drawStack, boolean doTransfer) {
		if (doTransfer && drawStack != null && drawStack.amount != 0) {
			if (this instanceof IProcessor) {
				((IProcessor<?, ?>) this).refreshActivity();
			}
			if (this instanceof ITilePort) {
				((ITilePort<?, ?, ?, ?, ?>) this).setRefreshTargetsFlag(true);
			}
		}
	}
	
	// Mekanism Gas Wrapper
	
	public @Nonnull GasTileWrapper getGasWrapper();
	
	// Fluid Distribution
	
	public default void pushFluid() {
		if (getTanks().isEmpty()) {
			return;
		}
		for (EnumFacing side : EnumFacing.VALUES) {
			pushFluidToSide(side);
		}
	}
	
	public default void pushFluidToSide(@Nonnull EnumFacing side) {
		if (!getFluidConnection(side).canConnect()) {
			return;
		}
		
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		if (tile == null || tile instanceof ITilePassive && !((ITilePassive) tile).canPushFluidsTo()) {
			return;
		}
		
		IFluidHandler adjStorage = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
		if (adjStorage == null) {
			return;
		}
		
		boolean drained = false;
		
		for (int i = 0; i < getTanks().size(); ++i) {
			Tank tank;
			if (!getTankSorption(side, i).canDrain() || (tank = getTanks().get(i)).getFluid() == null) {
				continue;
			}
			
			FluidStack drain = tank.drain(adjStorage.fill(tank.drain(tank.getCapacity(), false), true), true);
			if (drain != null && drain.amount != 0) {
				drained = true;
			}
		}
		
		if (drained) {
			if (this instanceof IProcessor) {
				((IProcessor<?, ?>) this).refreshActivity();
			}
			if (this instanceof ITilePort) {
				((ITilePort<?, ?, ?, ?, ?>) this).setRefreshTargetsFlag(true);
			}
		}
	}
	
	// NBT
	
	public default NBTTagCompound writeTanks(NBTTagCompound nbt) {
		for (int i = 0; i < getTanks().size(); ++i) {
			getTanks().get(i).writeToNBT(nbt, "tanks" + i);
		}
		return nbt;
	}
	
	public default void readTanks(NBTTagCompound nbt) {
		for (int i = 0; i < getTanks().size(); ++i) {
			getTanks().get(i).readFromNBT(nbt, "tanks" + i);
		}
	}
	
	public default NBTTagCompound writeFluidConnections(NBTTagCompound nbt) {
		for (EnumFacing side : EnumFacing.VALUES) {
			getFluidConnection(side).writeToNBT(nbt, side);
		}
		return nbt;
	}
	
	public default void readFluidConnections(NBTTagCompound nbt) {
		if (!hasConfigurableFluidConnections()) {
			return;
		}
		for (EnumFacing side : EnumFacing.VALUES) {
			getFluidConnection(side).readFromNBT(nbt, side);
		}
	}
	
	public default NBTTagCompound writeTankSettings(NBTTagCompound nbt) {
		nbt.setBoolean("inputTanksSeparated", getInputTanksSeparated());
		for (int i = 0; i < getTanks().size(); ++i) {
			nbt.setBoolean("voidUnusableFluidInput" + i, getVoidUnusableFluidInput(i));
			nbt.setInteger("tankOutputSetting" + i, getTankOutputSetting(i).ordinal());
		}
		return nbt;
	}
	
	public default void readTankSettings(NBTTagCompound nbt) {
		setInputTanksSeparated(nbt.getBoolean("inputTanksSeparated"));
		for (int i = 0; i < getTanks().size(); ++i) {
			setVoidUnusableFluidInput(i, nbt.getBoolean("voidUnusableFluidInput" + i));
			int ordinal = nbt.hasKey("voidExcessFluidOutput" + i) ? nbt.getBoolean("voidExcessFluidOutput" + i) ? 1 : 0 : nbt.getInteger("tankOutputSetting" + i);
			setTankOutputSetting(i, TankOutputSetting.values()[ordinal]);
		}
	}
	
	// Fluid Functions
	
	public boolean getInputTanksSeparated();
	
	public void setInputTanksSeparated(boolean separated);
	
	public boolean getVoidUnusableFluidInput(int tankNumber);
	
	public void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput);
	
	public TankOutputSetting getTankOutputSetting(int tankNumber);
	
	public void setTankOutputSetting(int tankNumber, TankOutputSetting setting);
	
	// Capabilities
	
	public default boolean hasFluidSideCapability(@Nullable EnumFacing side) {
		return side == null || getFluidConnection(side).canConnect();
	}
}
