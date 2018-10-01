package nc.tile.fluid;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import nc.config.NCConfig;
import nc.tile.ITile;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.FluidTileWrapper;
import nc.tile.internal.fluid.GasTileWrapper;
import nc.tile.internal.fluid.Tank;
import nc.tile.passive.ITilePassive;
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
	
	public default boolean isNextToFill(FluidStack resource, int tankNumber) {
		if (!getTanksShared()) return true;
		
		for (int i = 0; i < getTanks().size(); i++) {
			if (i != tankNumber && getTanks().get(i).canFill() && getTanks().get(i).getFluid() != null && getTanks().get(i).getFluid().isFluidEqual(resource)) {
				return false;
			}
		}
		return true;
	}
	
	public default void clearTank(int tankNumber) {
		if (tankNumber < getTanks().size()) getTanks().get(tankNumber).setFluidStored(null);
	}
	
	// Fluid Connections
	
	public @Nonnull FluidConnection[] getFluidConnections();
	
	public default @Nonnull FluidConnection getFluidConnection(@Nonnull EnumFacing side) {
		return getFluidConnections()[side.getIndex()];
	}
	
	public default void setFluidConnection(@Nonnull FluidConnection connection, @Nonnull EnumFacing side) {
		getFluidConnections()[side.getIndex()] = connection;
	}
	
	public default void toggleFluidConnection(@Nonnull EnumFacing side) {
		setFluidConnection(alternativeFluidToggle() ? getFluidConnection(side).nextAlt() : getFluidConnection(side).next(), side);
		markAndRefresh();
	}
	
	public default boolean alternativeFluidToggle() {
		return false;
	}
	
	public default boolean canConnectFluid(@Nonnull EnumFacing side) {
		return getFluidConnection(side).canConnect();
	}
	
	public static FluidConnection[] fluidConnectionAll(@Nonnull FluidConnection connection) {
		FluidConnection[] array = new FluidConnection[6];
		for (int i = 0; i < 6; i++) array[i] = connection;
		return array;
	}
	
	public default boolean hasConfigurableFluidConnections() {
		return false;
	}
	
	// Fluid Connection Wrapper Methods
	
	public default @Nonnull IFluidTankProperties[] getTankProperties(EnumFacing side) {
		if (getTanks().isEmpty()) return EmptyFluidHandler.EMPTY_TANK_PROPERTIES_ARRAY;
		IFluidTankProperties[] properties = new IFluidTankProperties[getTanks().size()];
		for (int i = 0; i < getTanks().size(); i++) properties[i] = getTanks().get(i).getFluidTankProperties();
		return properties;
	}
	
	public default int fill(FluidStack resource, boolean doFill, EnumFacing side) {
		if (getTanks().isEmpty() || !getFluidConnection(side).canFill()) return 0;
		for (int i = 0; i < getTanks().size(); i++) {
			if (getTanks().get(i).canFillFluidType(resource) && isNextToFill(resource, i) && getTanks().get(i).getFluidAmount() < getTanks().get(i).getCapacity() && (getTanks().get(i).getFluid() == null || getTanks().get(i).getFluid().isFluidEqual(resource))) {
				return getTanks().get(i).fill(resource, doFill);
			}
		}
		return 0;
	}
	
	public default FluidStack drain(FluidStack resource, boolean doDrain, EnumFacing side) {
		if (getTanks().isEmpty() || !getFluidConnection(side).canDrain()) return null;
		for (int i = 0; i < getTanks().size(); i++) {
			if (getTanks().get(i).canDrain() && getTanks().get(i).getFluidAmount() > 0 && resource.isFluidEqual(getTanks().get(i).getFluid()) && getTanks().get(i).drain(resource, false) != null) {
				return getTanks().get(i).drain(resource, doDrain);
			}
		}
		return null;
	}
	
	public default FluidStack drain(int maxDrain, boolean doDrain, EnumFacing side) {
		if (getTanks().isEmpty() || !getFluidConnection(side).canDrain()) return null;
		for (int i = 0; i < getTanks().size(); i++) {
			if (getTanks().get(i).canDrain() && getTanks().get(i).getFluidAmount() > 0 && getTanks().get(i).drain(maxDrain, false) != null) {
				return getTanks().get(i).drain(maxDrain, doDrain);
			}
		}
		return null;
	}
	
	// Fluid Wrappers
	
	public @Nonnull FluidTileWrapper[] getFluidSides();
	
	public default @Nonnull FluidTileWrapper getFluidSide(@Nonnull EnumFacing side) {
		return side == null ? getFluidSides()[0] : getFluidSides()[side.getIndex()];
	}
	
	public static @Nonnull FluidTileWrapper[] getDefaultFluidSides(@Nonnull ITileFluid tile) {
		return new FluidTileWrapper[] {new FluidTileWrapper(tile, EnumFacing.DOWN), new FluidTileWrapper(tile, EnumFacing.UP), new FluidTileWrapper(tile, EnumFacing.NORTH), new FluidTileWrapper(tile, EnumFacing.SOUTH), new FluidTileWrapper(tile, EnumFacing.WEST), new FluidTileWrapper(tile, EnumFacing.EAST)};
	}
	
	// Mekanism Gas
	
	public @Nonnull GasTileWrapper getGasWrapper();
	
	// Fluid Distribution
	
	public default void pushFluid() {
		if (getTanks().isEmpty()) return;
		for (EnumFacing side : EnumFacing.VALUES) pushFluidToSide(side);
	}
	
	public default void spreadFluid() {
		if (!NCConfig.passive_permeation || getTanks().isEmpty()) return;
		for (EnumFacing side : EnumFacing.VALUES) spreadFluidToSide(side);
	}
	
	public default void pushFluidToSide(@Nonnull EnumFacing side) {
		if (!getFluidConnection(side).canDrain()) return;
		
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		if (tile instanceof ITilePassive) if (!((ITilePassive) tile).canPushFluidsTo()) return;
		IFluidHandler adjStorage = tile == null ? null : tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
		
		if (adjStorage == null) return;
		
		for (int i = 0; i < getTanks().size(); i++) {
			if (getTanks().get(i).getFluid() == null || !getTanks().get(i).canDrain()) continue;
			
			getTanks().get(i).drain(adjStorage.fill(getTanks().get(i).drain(getTanks().get(i).getCapacity(), false), true), true);
		}
	}
	
	public default void spreadFluidToSide(@Nonnull EnumFacing side) {
		if (!getFluidConnection(side).canConnect()) return;
		
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		if (!(tile instanceof IFluidSpread)) return;
		if (tile instanceof ITilePassive) if (!((ITilePassive) tile).canPushFluidsTo()) return;
		IFluidHandler adjStorage = tile == null ? null : tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
		
		if (adjStorage == null) return;
		
		for (int i = 0; i < getTanks().size(); i++) {
			if (getTanks().get(i).getFluid() == null || !getTanks().get(i).canDistribute()) continue;
			
			int maxDrain = getTanks().get(i).getFluidAmount()/2;
			FluidStack stack = adjStorage.getTankProperties()[0].getContents();
			if (stack != null) maxDrain -= stack.amount/2;
			if (maxDrain > 0) getTanks().get(i).drainInternal(adjStorage.fill(getTanks().get(i).drainInternal(maxDrain, false), true), true);
		}
	}
	
	// NBT
	
	public default NBTTagCompound writeTanks(NBTTagCompound nbt) {
		if (!getTanks().isEmpty()) for (int i = 0; i < getTanks().size(); i++) {
			nbt.setInteger("fluidAmount" + i, getTanks().get(i).getFluidAmount());
			nbt.setString("fluidName" + i, getTanks().get(i).getFluidName());
		}
		return nbt;
	}
	
	public default void readTanks(NBTTagCompound nbt) {
		if (!getTanks().isEmpty()) for (int i = 0; i < getTanks().size(); i++) {
			if (nbt.getString("fluidName" + i) == "nullFluid" || nbt.getInteger("fluidAmount" + i) == 0) getTanks().get(i).setFluidStored(null);
			else getTanks().get(i).setFluidStored(FluidRegistry.getFluid(nbt.getString("fluidName" + i)), nbt.getInteger("fluidAmount" + i));
		}
	}

	public default NBTTagCompound writeFluidConnections(NBTTagCompound nbt) {
		for (int i = 0; i < 6; i++) nbt.setInteger("fluidConnections" + i, getFluidConnections()[i].ordinal());
		return nbt;
	}

	public default void readFluidConnections(NBTTagCompound nbt) {
		if (hasConfigurableFluidConnections()) for (int i = 0; i < 6; i++) {
			if (nbt.hasKey("fluidConnections" + i)) getFluidConnections()[i] = FluidConnection.values()[nbt.getInteger("fluidConnections" + i)];
		}
	}
	
	// Fluid Functions
	
	public boolean getTanksShared();
	
	public void setTanksShared(boolean shared);
	
	public boolean getEmptyUnusableTankInputs();
	
	public void setEmptyUnusableTankInputs(boolean emptyUnusableTankInputs);
	
	public boolean getVoidExcessFluidOutputs();
	
	public void setVoidExcessFluidOutputs(boolean voidExcessFluidOutputs);
	
	// Capabilities
	
	public default boolean hasFluidSideCapability(@Nullable EnumFacing side) {
		return side == null || getFluidConnection(side).canConnect();
	}
}
