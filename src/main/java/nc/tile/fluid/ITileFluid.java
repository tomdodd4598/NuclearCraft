package nc.tile.fluid;

import com.google.common.collect.Lists;
import mekanism.api.gas.GasStack;
import nc.tile.ITile;
import nc.tile.hx.IHeatExchangerPart;
import nc.tile.internal.fluid.*;
import nc.tile.machine.IMachinePart;
import nc.tile.multiblock.port.ITilePort;
import nc.tile.passive.ITilePassive;
import nc.tile.processor.IProcessor;
import nc.tile.processor.IProcessor.HandlerPair;
import nc.util.BlockHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.*;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;

import javax.annotation.*;
import java.util.List;

public interface ITileFluid extends ITile {
	
	// Tanks
	
	@Nonnull List<Tank> getTanks();
	
	// Tank Logic
	
	/**
	 * Only concerns ordering, not whether fluid is actually valid for the tank due to filters or sorption
	 */
	default boolean isNextToFill(@Nonnull EnumFacing side, int tankNumber, FluidStack resource) {
		if (!getInputTanksSeparated()) {
			return true;
		}
		
		List<Tank> tanks = getTanks();
		for (int i = 0; i < tanks.size(); ++i) {
			Tank tank = tanks.get(i);
			if (i != tankNumber && getTankSorption(side, i).canFill()) {
				FluidStack fluid = tank.getFluid();
				if (fluid != null && fluid.isFluidEqual(resource)) {
					return false;
				}
			}
		}
		return true;
	}
	
	default boolean isFluidValidForTank(int tankNumber, FluidStack stack) {
		return getTanks().get(tankNumber).canFillFluidType(stack);
	}
	
	default void clearTank(int tankNumber) {
		getTanks().get(tankNumber).setFluidStored(null);
	}
	
	default void clearAllTanks() {
		for (Tank tank : getTanks()) {
			tank.setFluidStored(null);
		}
	}
	
	// Fluid Connections
	
	@Nonnull
	FluidConnection[] getFluidConnections();
	
	void setFluidConnections(@Nonnull FluidConnection[] connections);
	
	default @Nonnull FluidConnection getFluidConnection(@Nonnull EnumFacing side) {
		return getFluidConnections()[side.getIndex()];
	}
	
	default @Nonnull TankSorption getTankSorption(@Nonnull EnumFacing side, int tankNumber) {
		return getFluidConnections()[side.getIndex()].getTankSorption(tankNumber);
	}
	
	default void setTankSorption(@Nonnull EnumFacing side, int tankNumber, @Nonnull TankSorption sorption) {
		getFluidConnections()[side.getIndex()].setTankSorption(tankNumber, sorption);
	}
	
	default void toggleTankSorption(@Nonnull EnumFacing side, int tankNumber, TankSorption.Type type, boolean reverse) {
		if (!hasConfigurableFluidConnections()) {
			return;
		}
		getFluidConnection(side).toggleTankSorption(tankNumber, type, reverse);
		markDirtyAndNotify(true);
	}
	
	default boolean canConnectFluid(@Nonnull EnumFacing side) {
		return getFluidConnection(side).canConnect();
	}
	
	static FluidConnection[] fluidConnectionAll(@Nonnull List<TankSorption> sorptionList) {
		FluidConnection[] array = new FluidConnection[6];
		for (int i = 0; i < 6; ++i) {
			array[i] = new FluidConnection(sorptionList);
		}
		return array;
	}
	
	static FluidConnection[] fluidConnectionAll(TankSorption sorption) {
		return fluidConnectionAll(Lists.newArrayList(sorption));
	}
	
	default boolean hasConfigurableFluidConnections() {
		return false;
	}
	
	// Fluid Wrapper Methods
	
	default @Nonnull IFluidTankProperties[] getTankProperties(@Nonnull EnumFacing side) {
		List<Tank> tanks = getTanks();
		if (tanks.isEmpty()) {
			return EmptyFluidHandler.EMPTY_TANK_PROPERTIES_ARRAY;
		}
		IFluidTankProperties[] properties = new IFluidTankProperties[tanks.size()];
		for (int i = 0; i < tanks.size(); ++i) {
			properties[i] = tanks.get(i).getFluidTankProperties();
		}
		return properties;
	}
	
	default int fill(@Nonnull EnumFacing side, FluidStack resource, boolean doFill) {
		List<Tank> tanks = getTanks();
		for (int i = 0; i < tanks.size(); ++i) {
			if (getTankSorption(side, i).canFill()) {
				Tank tank;
				if (isFluidValidForTank(i, resource) && isNextToFill(side, i, resource) && (tank = tanks.get(i)).getFluidAmount() < tank.getCapacity()) {
					FluidStack fluid = tank.getFluid();
					if (fluid == null || fluid.isFluidEqual(resource)) {
						return tank.fill(resource, doFill);
					}
				}
			}
		}
		return 0;
	}
	
	default FluidStack drain(@Nonnull EnumFacing side, FluidStack resource, boolean doDrain) {
		List<Tank> tanks = getTanks();
		for (int i = 0; i < tanks.size(); ++i) {
			if (getTankSorption(side, i).canDrain()) {
				Tank tank = tanks.get(i);
				if (tank.getFluidAmount() > 0 && resource.isFluidEqual(tank.getFluid()) && tank.drain(resource, false) != null) {
					if (tank.drain(resource, false) != null) {
						return tank.drain(resource, doDrain);
					}
				}
			}
		}
		return null;
	}
	
	default FluidStack drain(@Nonnull EnumFacing side, int maxDrain, boolean doDrain) {
		List<Tank> tanks = getTanks();
		for (int i = 0; i < tanks.size(); ++i) {
			if (getTankSorption(side, i).canDrain()) {
				Tank tank = tanks.get(i);
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
	
	@Nonnull
	FluidTileWrapper[] getFluidSides();
	
	default @Nonnull FluidTileWrapper getFluidSide(@Nonnull EnumFacing side) {
		return getFluidSides()[side.getIndex()];
	}
	
	static @Nonnull FluidTileWrapper[] getDefaultFluidSides(@Nonnull ITileFluid tile) {
		return new FluidTileWrapper[] {new FluidTileWrapper(tile, EnumFacing.DOWN), new FluidTileWrapper(tile, EnumFacing.UP), new FluidTileWrapper(tile, EnumFacing.NORTH), new FluidTileWrapper(tile, EnumFacing.SOUTH), new FluidTileWrapper(tile, EnumFacing.WEST), new FluidTileWrapper(tile, EnumFacing.EAST)};
	}
	
	default void onWrapperFill(int fillAmount, boolean doFill) {
		if (doFill && fillAmount != 0) {
			if (this instanceof IProcessor<?, ?, ?> processor) {
				processor.refreshRecipe();
				processor.refreshActivity();
			}
			else if (this instanceof IMachinePart part) {
				part.refreshMachineRecipe();
				part.refreshMachineActivity();
			}
			else if (this instanceof IHeatExchangerPart part) {
				part.refreshHeatExchangerRecipe();
				part.refreshHeatExchangerActivity();
			}
			
			if (this instanceof ITilePort<?, ?, ?, ?, ?> port) {
				port.setRefreshTargetsFlag(true);
			}
		}
	}
	
	default void onWrapperDrain(FluidStack drainStack, boolean doDrain) {
		if (doDrain && drainStack != null && drainStack.amount != 0) {
			if (this instanceof IProcessor<?, ?, ?> processor) {
				processor.refreshActivity();
			}
			else if (this instanceof IMachinePart part) {
				part.refreshMachineActivity();
			}
			else if (this instanceof IHeatExchangerPart part) {
				part.refreshHeatExchangerActivity();
			}
			
			if (this instanceof ITilePort<?, ?, ?, ?, ?> port) {
				port.setRefreshTargetsFlag(true);
			}
		}
	}
	
	default void onWrapperReceiveGas(int receiveAmount, boolean doTransfer) {
		if (doTransfer && receiveAmount != 0) {
			if (this instanceof IProcessor<?, ?, ?> processor) {
				processor.refreshRecipe();
				processor.refreshActivity();
			}
			else if (this instanceof IMachinePart part) {
				part.refreshMachineRecipe();
				part.refreshMachineActivity();
			}
			else if (this instanceof IHeatExchangerPart part) {
				part.refreshHeatExchangerRecipe();
				part.refreshHeatExchangerActivity();
			}
			
			if (this instanceof ITilePort<?, ?, ?, ?, ?> port) {
				port.setRefreshTargetsFlag(true);
			}
		}
	}
	
	default void onWrapperDrawGas(GasStack drawStack, boolean doTransfer) {
		if (doTransfer && drawStack != null && drawStack.amount != 0) {
			if (this instanceof IProcessor<?, ?, ?> processor) {
				processor.refreshActivity();
			}
			else if (this instanceof IMachinePart part) {
				part.refreshMachineActivity();
			}
			else if (this instanceof IHeatExchangerPart part) {
				part.refreshHeatExchangerActivity();
			}
			
			if (this instanceof ITilePort<?, ?, ?, ?, ?> port) {
				port.setRefreshTargetsFlag(true);
			}
		}
	}
	
	// Mekanism Gas Wrapper
	
	@Nonnull
	GasTileWrapper getGasWrapper();
	
	// Fluid Distribution
	
	default void pushFluid() {
		if (getTanks().isEmpty()) {
			return;
		}
		for (EnumFacing side : EnumFacing.VALUES) {
			pushFluidToSide(side);
		}
	}
	
	default void pushFluidToSide(@Nonnull EnumFacing side) {
		if (!getFluidConnection(side).canConnect()) {
			return;
		}
		
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		if (tile == null || (tile instanceof ITilePassive tilePassive && !tilePassive.canPushFluidsTo())) {
			return;
		}
		
		IFluidHandler adjStorage = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
		if (adjStorage == null) {
			return;
		}
		
		boolean drained = false;
		
		List<Tank> tanks = getTanks();
		int tankCount = tanks.size();
		for (int i = 0; i < tankCount; ++i) {
			drained |= pushTankToHandler(adjStorage, tanks, side, i);
		}
		
		if (drained) {
			if (this instanceof IProcessor<?, ?, ?> processor) {
				processor.refreshActivity();
			}
			else if (this instanceof IMachinePart part) {
				part.refreshMachineActivity();
			}
			else if (this instanceof IHeatExchangerPart part) {
				part.refreshHeatExchangerActivity();
			}
			
			if (this instanceof ITilePort<?, ?, ?, ?, ?> port) {
				port.setRefreshTargetsFlag(true);
			}
		}
	}
	
	default boolean pushTankToHandler(IFluidHandler handler, List<Tank> tanks, @Nonnull EnumFacing side, int tankNumber) {
		Tank tank;
		if (!getTankSorption(side, tankNumber).canDrain() || (tank = tanks.get(tankNumber)).getFluid() == null) {
			return false;
		}
		
		FluidStack drain = tank.drain(handler.fill(tank.drain(tank.getCapacity(), false), true), true);
		return drain != null && drain.amount != 0;
	}
	
	default boolean tryPushTank(HandlerPair[] adjacentHandlers, List<Tank> tanks, int tankIndex, List<EnumFacing> dirs, int dirCount, int indexOffset) {
		if (!tanks.get(tankIndex).isEmpty()) {
			for (int i = 0; i < dirCount; ++i) {
				EnumFacing dir = dirs.get((i + indexOffset) % dirCount);
				if (getTankSorption(dir, tankIndex).equals(TankSorption.AUTO_OUT)) {
					IFluidHandler handler = adjacentHandlers[dir.getIndex()].fluidHandler;
					if (handler != null && pushTankToHandler(handler, tanks, dir, tankIndex)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	// NBT
	
	default NBTTagCompound writeTanks(NBTTagCompound nbt) {
		List<Tank> tanks = getTanks();
		for (int i = 0; i < tanks.size(); ++i) {
			tanks.get(i).writeToNBT(nbt, "tanks" + i);
		}
		return nbt;
	}
	
	default void readTanks(NBTTagCompound nbt) {
		List<Tank> tanks = getTanks();
		for (int i = 0; i < tanks.size(); ++i) {
			tanks.get(i).readFromNBT(nbt, "tanks" + i);
		}
	}
	
	default NBTTagCompound writeFluidConnections(NBTTagCompound nbt) {
		return writeFluidConnectionsDirectional(nbt, null);
	}
	
	default NBTTagCompound writeFluidConnectionsDirectional(NBTTagCompound nbt, @Nullable EnumFacing facing) {
		for (int i = 0; i < 6; ++i) {
			getFluidConnection(BlockHelper.DIR_FROM_FACING[i].apply(facing)).writeToNBT(nbt, EnumFacing.byIndex(i));
		}
		return nbt;
	}
	
	default void readFluidConnections(NBTTagCompound nbt) {
		readFluidConnectionsDirectional(nbt, null);
	}
	
	default void readFluidConnectionsDirectional(NBTTagCompound nbt, @Nullable EnumFacing facing) {
		if (!hasConfigurableFluidConnections()) {
			return;
		}
		for (int i = 0; i < 6; ++i) {
			getFluidConnection(BlockHelper.DIR_FROM_FACING[i].apply(facing)).readFromNBT(nbt, EnumFacing.byIndex(i));
		}
	}
	
	default NBTTagCompound writeTankSettings(NBTTagCompound nbt) {
		nbt.setBoolean("inputTanksSeparated", getInputTanksSeparated());
		int tankCount = getTanks().size();
		for (int i = 0; i < tankCount; ++i) {
			nbt.setBoolean("voidUnusableFluidInput" + i, getVoidUnusableFluidInput(i));
			nbt.setInteger("tankOutputSetting" + i, getTankOutputSetting(i).ordinal());
		}
		return nbt;
	}
	
	default void readTankSettings(NBTTagCompound nbt) {
		setInputTanksSeparated(nbt.getBoolean("inputTanksSeparated"));
		int tankCount = getTanks().size();
		for (int i = 0; i < tankCount; ++i) {
			setVoidUnusableFluidInput(i, nbt.getBoolean("voidUnusableFluidInput" + i));
			int ordinal = nbt.hasKey("voidExcessFluidOutput" + i) ? nbt.getBoolean("voidExcessFluidOutput" + i) ? 1 : 0 : nbt.getInteger("tankOutputSetting" + i);
			setTankOutputSetting(i, TankOutputSetting.values()[ordinal]);
		}
	}
	
	// Fluid Functions
	
	boolean getInputTanksSeparated();
	
	void setInputTanksSeparated(boolean separated);
	
	boolean getVoidUnusableFluidInput(int tankNumber);
	
	void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput);
	
	TankOutputSetting getTankOutputSetting(int tankNumber);
	
	void setTankOutputSetting(int tankNumber, TankOutputSetting setting);
	
	// Capabilities
	
	default boolean hasFluidSideCapability(@Nullable EnumFacing side) {
		return side == null || getFluidConnection(side).canConnect();
	}
}
