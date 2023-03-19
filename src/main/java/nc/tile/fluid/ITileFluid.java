package nc.tile.fluid;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import nc.config.NCConfig;
import nc.tile.ITile;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.FluidTileWrapper;
import nc.tile.internal.fluid.GasTileWrapper;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.TankOutputSetting;
import nc.tile.internal.fluid.TankSorption;
import nc.tile.passive.ITilePassive;
import nc.util.BlockHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
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
		for (int i = 0; i < getTanks().size(); i++) {
			if (i != tankNumber && getTankSorption(side, i).canFill() && getTanks().get(i).getFluid() != null && getTanks().get(i).getFluid().isFluidEqual(resource)) {
				return false;
			}
		}
		return true;
	}
	
	public default void clearTank(int tankNumber) {
		getTanks().get(tankNumber).setFluidStored(null);
	}
	
	public default void clearAllTanks() {
		for (Tank tank : getTanks()) tank.setFluidStored(null);
	}
	
	// Fluid Connections
	
	public @Nonnull FluidConnection[] getFluidConnections();
	
	public void setFluidConnections(@Nonnull FluidConnection[] connections);
	
	public default @Nonnull FluidConnection getFluidConnection(@Nonnull EnumFacing side) {
		return getFluidConnections()[side.getIndex()];
	}
	
	/*public default void setFluidConnection(@Nonnull EnumFacing side, @Nonnull FluidConnection connection) {
		getFluidConnections()[side.getIndex()] = connection.copy();
	}*/
	
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
		markDirtyAndNotify();
	}
	
	public default boolean canConnectFluid(@Nonnull EnumFacing side) {
		return getFluidConnection(side).canConnect();
	}
	
	public static FluidConnection[] fluidConnectionAll(@Nonnull List<TankSorption> sorptionList) {
		FluidConnection[] array = new FluidConnection[6];
		for (int i = 0; i < 6; i++) {
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
		for (int i = 0; i < getTanks().size(); i++) {
			properties[i] = getTanks().get(i).getFluidTankProperties();
		}
		return properties;
	}
	
	public default int fill(@Nonnull EnumFacing side, FluidStack resource, boolean doFill) {
		for (int i = 0; i < getTanks().size(); i++) {
			if (getTankSorption(side, i).canFill() && getTanks().get(i).canFillFluidType(resource) && isNextToFill(side, i, resource) && getTanks().get(i).getFluidAmount() < getTanks().get(i).getCapacity() && (getTanks().get(i).getFluid() == null || getTanks().get(i).getFluid().isFluidEqual(resource))) {
				return getTanks().get(i).fill(resource, doFill);
			}
		}
		return 0;
	}
	
	public default FluidStack drain(@Nonnull EnumFacing side, FluidStack resource, boolean doDrain) {
		for (int i = 0; i < getTanks().size(); i++) {
			if (getTankSorption(side, i).canDrain() && getTanks().get(i).getFluidAmount() > 0 && resource.isFluidEqual(getTanks().get(i).getFluid()) && getTanks().get(i).drain(resource, false) != null) {
				if (getTanks().get(i).drain(resource, false) != null) {
					return getTanks().get(i).drain(resource, doDrain);
				}
			}
		}
		return null;
	}
	
	public default FluidStack drain(@Nonnull EnumFacing side, int maxDrain, boolean doDrain) {
		for (int i = 0; i < getTanks().size(); i++) {
			if (getTankSorption(side, i).canDrain() && getTanks().get(i).getFluidAmount() > 0 && getTanks().get(i).drain(maxDrain, false) != null) {
				if (getTanks().get(i).drain(maxDrain, false) != null) {
					return getTanks().get(i).drain(maxDrain, doDrain);
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
	
	public default void spreadFluid() {
		if (!NCConfig.passive_permeation || getTanks().isEmpty()) {
			return;
		}
		for (EnumFacing side : EnumFacing.VALUES) {
			spreadFluidToSide(side);
		}
	}
	
	public default void pushFluidToSide(@Nonnull EnumFacing side) {
		if (!getFluidConnection(side).canConnect()) {
			return;
		}
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		if (tile == null || (tile instanceof ITilePassive && !((ITilePassive) tile).canPushFluidsTo())) {
			return;
		}
		IFluidHandler adjStorage = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
		if (adjStorage == null) {
			return;
		}
		for (int i = 0; i < getTanks().size(); i++) {
			if (!getTankSorption(side, i).canDrain() || getTanks().get(i).getFluid() == null /*|| !getTanks().get(i).canDrain()*/) {
				continue;
			}
			getTanks().get(i).drain(adjStorage.fill(getTanks().get(i).drain(getTanks().get(i).getCapacity(), false), true), true);
		}
	}
	
	public default void spreadFluidToSide(@Nonnull EnumFacing side) {
		if (!getFluidConnection(side).canConnect()) return;
		
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		
		if (tile instanceof IFluidSpread) {
			if (tile instanceof ITilePassive && !((ITilePassive)tile).canPushFluidsTo()) return;
			
			IFluidSpread other = (IFluidSpread) tile;
			
			for (int i = 0; i < getTanks().size(); i++) {
				int diff = getTanks().get(i).getFluidAmount() - other.getTanks().get(0).getFluidAmount();
				if (diff > 1) {
					getTanks().get(i).drain(other.getTanks().get(0).fillInternal(getTanks().get(i).drain(diff/2, false), true), true);
				}
			}
		}
	}
	
	// NBT
	
	public default NBTTagCompound writeTanks(NBTTagCompound nbt) {
		for (int i = 0; i < getTanks().size(); i++) {
			getTanks().get(i).writeToNBT(nbt, i);
		}
		return nbt;
	}
	
	public default void readTanks(NBTTagCompound nbt) {
		if (nbt.hasKey("fluidName0")) {
			for (int i = 0; i < getTanks().size(); i++) {
				if (nbt.getString("fluidName" + i).equals("nullFluid") || nbt.getInteger("fluidAmount" + i) == 0) {
					getTanks().get(i).setFluidStored(null);
				}
				else {
					getTanks().get(i).setFluidStored(FluidRegistry.getFluid(nbt.getString("fluidName" + i)), nbt.getInteger("fluidAmount" + i));
				}
			}
		}
		else {
			for (int i = 0; i < getTanks().size(); i++) {
				getTanks().get(i).readFromNBT(nbt, i);
			}
		}
	}

	public default NBTTagCompound writeFluidConnections(NBTTagCompound nbt) {
		return writeFluidConnectionsNormalized(nbt, EnumFacing.WEST);
	}

	public default NBTTagCompound writeFluidConnectionsNormalized(NBTTagCompound nbt, EnumFacing facing) {
		getFluidConnection(BlockHelper.bottom(facing)).writeToNBT(nbt, EnumFacing.DOWN);
		getFluidConnection(BlockHelper.top(facing)).writeToNBT(nbt, EnumFacing.UP);
		getFluidConnection(BlockHelper.left(facing)).writeToNBT(nbt, EnumFacing.NORTH);
		getFluidConnection(BlockHelper.right(facing)).writeToNBT(nbt, EnumFacing.SOUTH);
		getFluidConnection(BlockHelper.front(facing)).writeToNBT(nbt, EnumFacing.WEST);
		getFluidConnection(BlockHelper.back(facing)).writeToNBT(nbt, EnumFacing.EAST);
		return nbt;
	}

	public default void readFluidConnections(NBTTagCompound nbt) {
		if (!hasConfigurableFluidConnections()) {
			return;
		}
		if (nbt.hasKey("fluidConnections0")) {
			for (EnumFacing side : EnumFacing.VALUES) {
				if (nbt.hasKey("fluidConnections" + side.getIndex())) {
					for (int i = 0; i < getTanks().size(); i++) {
						getFluidConnection(side).setTankSorption(i, TankSorption.values()[nbt.getInteger("fluidConnections" + side.getIndex())]);
					}
				}
			}
		}
		else {
			readFluidConnectionsNormalized(nbt, EnumFacing.WEST);
		}
	}

	public default void readFluidConnectionsNormalized(NBTTagCompound nbt, EnumFacing facing) {
		if (!hasConfigurableFluidConnections()) {
			return;
		}
		getFluidConnection(BlockHelper.bottom(facing)).readFromNBT(nbt, EnumFacing.DOWN);
		getFluidConnection(BlockHelper.top(facing)).readFromNBT(nbt, EnumFacing.UP);
		getFluidConnection(BlockHelper.left(facing)).readFromNBT(nbt, EnumFacing.NORTH);
		getFluidConnection(BlockHelper.right(facing)).readFromNBT(nbt, EnumFacing.SOUTH);
		getFluidConnection(BlockHelper.front(facing)).readFromNBT(nbt, EnumFacing.WEST);
		getFluidConnection(BlockHelper.back(facing)).readFromNBT(nbt, EnumFacing.EAST);
	}
	
	public default NBTTagCompound writeTankSettings(NBTTagCompound nbt) {
		nbt.setBoolean("inputTanksSeparated", getInputTanksSeparated());
		for (int i = 0; i < getTanks().size(); i++) {
			nbt.setBoolean("voidUnusableFluidInput" + i, getVoidUnusableFluidInput(i));
			nbt.setInteger("tankOutputSetting" + i, getTankOutputSetting(i).ordinal());
		}
		return nbt;
	}
	
	public default void readTankSettings(NBTTagCompound nbt) {
		if (nbt.hasKey("areTanksShared")) {
			setInputTanksSeparated(nbt.getBoolean("areTanksShared"));
			for (int i = 0; i < getTanks().size(); i++) {
				setVoidUnusableFluidInput(i, nbt.getBoolean("emptyUnusable"));
				int ordinal = nbt.hasKey("voidExcessOutputs") ? (nbt.getBoolean("voidExcessOutputs") ? 1 : 0) : nbt.getInteger("tankOutputSetting" + i);
				setTankOutputSetting(i, TankOutputSetting.values()[ordinal]);
			}
		}
		else {
			setInputTanksSeparated(nbt.getBoolean("inputTanksSeparated"));
			for (int i = 0; i < getTanks().size(); i++) {
				setVoidUnusableFluidInput(i, nbt.getBoolean("voidUnusableFluidInput" + i));
				int ordinal = nbt.hasKey("voidExcessFluidOutput" + i) ? (nbt.getBoolean("voidExcessFluidOutput" + i) ? 1 : 0) : nbt.getInteger("tankOutputSetting" + i);
				setTankOutputSetting(i, TankOutputSetting.values()[ordinal]);
			}
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
