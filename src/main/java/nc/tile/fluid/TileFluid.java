package nc.tile.fluid;

import nc.config.NCConfig;
import nc.tile.NCTile;
import nc.tile.internal.EnumTank.FluidConnection;
import nc.tile.internal.Tank;
import nc.tile.passive.ITilePassive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;

public abstract class TileFluid extends NCTile implements ITileFluid, IFluidHandler/*, IGasHandler, ITubeConnection*/ {
	
	public FluidConnection[] fluidConnections;
	public final Tank[] tanks;
	public boolean areTanksShared = false;
	
	public TileFluid(int capacity, FluidConnection fluidConnections, String[]... allowedFluids) {
		this(new int[] {capacity}, new int[] {capacity}, new int[] {capacity}, new FluidConnection[] {fluidConnections}, allowedFluids);
	}
	
	public TileFluid(int[] capacity, FluidConnection[] fluidConnections, String[]... allowedFluids) {
		this(capacity, capacity, capacity, fluidConnections, allowedFluids);
	}
	
	public TileFluid(int capacity, int maxTransfer, FluidConnection fluidConnections, String[]... allowedFluids) {
		this(new int[] {capacity}, new int[] {maxTransfer}, new int[] {maxTransfer}, new FluidConnection[] {fluidConnections}, allowedFluids);
	}
	
	public TileFluid(int[] capacity, int[] maxTransfer, FluidConnection[] fluidConnections, String[]... allowedFluids) {
		this(capacity, maxTransfer, maxTransfer, fluidConnections, allowedFluids);
	}
	
	public TileFluid(int capacity, int maxReceive, int maxExtract, FluidConnection fluidConnections, String[]... allowedFluids) {
		this(new int[] {capacity}, new int[] {maxReceive}, new int[] {maxExtract}, new FluidConnection[] {fluidConnections}, allowedFluids);
	}
	
	public TileFluid(int[] capacity, int[] maxReceive, int[] maxExtract, FluidConnection[] fluidConnections, String[]... allowedFluids) {
		super();
		if (capacity == null || capacity.length == 0) {
			tanks = null;
		} else {
			Tank[] tankList = new Tank[capacity.length];
			String[][] fluidWhitelists = new String[capacity.length][];
			for (int i = 0; i < capacity.length; i++) {
				if (i < allowedFluids.length) fluidWhitelists[i] = allowedFluids[i];
				else fluidWhitelists[i] = new String[] {};
			}
			for (int i = 0; i < capacity.length; i++) {
				tankList[i] = new Tank(capacity[i], maxReceive[i], maxExtract[i], fluidWhitelists[i]);
			}
			tanks = tankList;
		}
		if (fluidConnections == null || fluidConnections.length == 0) {
			this.fluidConnections = null;
		} else {
			FluidConnection[] fluidConnectionsList = new FluidConnection[fluidConnections.length];
			for (int i = 0; i < fluidConnections.length; i++) {
				fluidConnectionsList[i] = fluidConnections[i];
			}
			this.fluidConnections = fluidConnectionsList;
		}
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
	public IFluidTankProperties[] getTankProperties() {
		if (tanks.length == 0 || tanks == null) return EmptyFluidHandler.EMPTY_TANK_PROPERTIES_ARRAY;
		IFluidTankProperties[] properties = new IFluidTankProperties[tanks.length];
		for (int i = 0; i < tanks.length; i++) {
			properties[i] = new FluidTankProperties(tanks[i].getFluid(), tanks[i].getCapacity(), fluidConnections[i].canFill(), fluidConnections[i].canDrain());
		}
		return properties;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (tanks.length == 0 || tanks == null) return 0;
		for (int i = 0; i < tanks.length; i++) {
			if (fluidConnections[i].canFill() && tanks[i].isFluidValid(resource) && canFill(resource, i) && tanks[i].getFluidAmount() < tanks[i].getCapacity() && (tanks[i].getFluid() == null || tanks[i].getFluid().isFluidEqual(resource))) {
				return tanks[i].fill(resource, doFill);
			}
		}
		return 0;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		if (tanks.length == 0 || tanks == null) return null;
		for (int i = 0; i < tanks.length; i++) {
			if (fluidConnections[i].canDrain() && tanks[i].getFluid() != null && tanks[i].getFluidAmount() > 0) {
				if (resource.isFluidEqual(tanks[i].getFluid()) && tanks[i].drain(resource, false) != null) return tanks[i].drain(resource, doDrain);
			}
		}
		return null;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (tanks.length == 0 || tanks == null) return null;
		for (int i = 0; i < tanks.length; i++) {
			if (fluidConnections[i].canDrain() && tanks[i].getFluid() != null && tanks[i].getFluidAmount() > 0) {
				if (tanks[i].drain(maxDrain, false) != null) return tanks[i].drain(maxDrain, doDrain);
			}
		}
		return null;
	}
	
	@Override
	public boolean canFill(FluidStack resource, int tankNumber) {
		if (!areTanksShared) return true;
		
		for (int i = 0; i < tanks.length; i++) {
			if (i != tankNumber && fluidConnections[i].canFill() && tanks[i].getFluid() != null) {
				if (tanks[i].getFluid().isFluidEqual(resource)) return false;
			}
		}
		return true;
	}
	
	@Override
	public void clearTank(int tankNo) {
		if (tankNo < tanks.length) tanks[tankNo].setFluidStored(null);
	}
	
	@Override
	public Tank[] getTanks() {
		return tanks;
	}
	
	@Override
	public FluidConnection[] getFluidConnections() {
		return fluidConnections;
	}
	
	@Override
	public BlockPos getBlockPos() {
		return pos;
	}
	
	// Mekanism Gas
	
	/*public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer) {
		String gasName = stack.getGas().getName();
		Fluid fluid = FluidRegistry.getFluid(gasName);
		if (fluid == null) return 0;
		FluidStack fluidStack = new FluidStack(fluid, 1000);
		
		if (tanks.length == 0 || tanks == null) return 0;
		for (int i = 0; i < tanks.length; i++) {
			if (fluidConnections[i].canFill() && tanks[i].isFluidValid(fluidStack) && canFill(fluidStack, i) && tanks[i].getFluidAmount() < tanks[i].getCapacity() && (tanks[i].getFluid() == null || tanks[i].getFluid().isFluidEqual(fluidStack))) {
				return tanks[i].fill(fluidStack, doTransfer);
			}
		}
		return 0;
	}
	
	public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer) {
		return null;
	}
	
	public boolean canReceiveGas(EnumFacing side, Gas gas) {
		Fluid fluid = FluidRegistry.getFluid(gas.getName());
		if (fluid == null) return false;
		if (!areTanksShared) return true;
		
		FluidStack fluidStack = new FluidStack(fluid, 1000);
		for (int i = 0; i < tanks.length; i++) {
			if (fluidConnections[i].canFill() && tanks[i].getFluid() != null) {
				if (tanks[i].getFluidAmount() >= tanks[i].getCapacity() && tanks[i].getFluid().isFluidEqual(fluidStack)) return false;
			}
		}
		return true;
	}

	public boolean canDrawGas(EnumFacing side, Gas type) {
		return false;
	}
	
	public boolean canTubeConnect(EnumFacing side) {
		for (FluidConnection con : this.fluidConnections) {
			if (con.canFill()) return true;
		}
		return false;
	}*/
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		if (tanks.length > 0 && tanks != null) for (int i = 0; i < tanks.length; i++) {
			nbt.setInteger("fluidAmount" + i, tanks[i].getFluidAmount());
			nbt.setString("fluidName" + i, tanks[i].getFluidName());
		}
		nbt.setBoolean("areTanksShared", areTanksShared);
		return nbt;
	}
		
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		if (tanks.length > 0 && tanks != null) for (int i = 0; i < tanks.length; i++) {
			if (nbt.getString("fluidName" + i) == "nullFluid" || nbt.getInteger("fluidAmount" + i) == 0) tanks[i].setFluidStored(null);
			else tanks[i].setFluidStored(FluidRegistry.getFluid(nbt.getString("fluidName" + i)), nbt.getInteger("fluidAmount" + i));
		}
		setTanksShared(nbt.getBoolean("areTanksShared"));
	}
	
	// Fluid Connections
	
	public void setConnection(FluidConnection[] fluidConnections) {
		if (tanks.length > 0 && tanks != null) this.fluidConnections = fluidConnections;
	}
	
	public void setConnection(FluidConnection fluidConnections, int tankNumber) {
		if (tanks.length > 0 && tanks != null) this.fluidConnections[tankNumber] = fluidConnections;
	}
	
	public void pushFluid() {
		if (tanks.length > 0 && tanks != null) for (int i = 0; i < tanks.length; i++) {
			if (tanks[i].getFluid() == null) return;
			if (tanks[i].getFluidAmount() <= 0 || !fluidConnections[i].canDrain()) return;
			for (EnumFacing side : EnumFacing.VALUES) {
				TileEntity tile = world.getTileEntity(getPos().offset(side));
				if (tile instanceof ITilePassive) if (!((ITilePassive) tile).canPushFluidsTo()) continue;
				if (tile instanceof ITileFluid) if (!((ITileFluid) tile).getFluidConnections()[i].canFill()) continue;
				IFluidHandler adjStorage = tile == null ? null : tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
				
				if (tile instanceof IFluidHandler) {
					tanks[i].drain(((IFluidHandler) tile).fill(tanks[i].drain(tanks[i].getCapacity(), false), true), true);
				}
				else if (adjStorage != null) {
					tanks[i].drain(adjStorage.fill(tanks[i].drain(tanks[i].getCapacity(), false), true), true);
				}
			}
		}
	}
	
	public void spreadFluid() {
		if (!NCConfig.passive_permeation) return;
		if (tanks.length > 0 && tanks != null) for (int i = 0; i < tanks.length; i++) {
			if (tanks[i].getFluid() == null) return;
			if (tanks[i].getFluidAmount() <= 0 || fluidConnections[i] == FluidConnection.NON) return;
			for (EnumFacing side : EnumFacing.VALUES) {
				TileEntity tile = world.getTileEntity(getPos().offset(side));
				if (tile instanceof ITilePassive) if (!((ITilePassive) tile).canPushFluidsTo()) continue;
				IFluidHandler adjStorage = tile == null ? null : tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
				
				if (!(tile instanceof IFluidSpread)) continue;
				
				if (tile instanceof IFluidHandler) {
					int maxDrain = tanks[i].getFluidAmount()/2;
					FluidStack stack = ((IFluidHandler) tile).getTankProperties()[0].getContents();
					if (stack != null) maxDrain -= stack.amount/2;
					if (maxDrain > 0) tanks[i].drain(((IFluidHandler) tile).fill(tanks[i].drain(maxDrain, false), true), true);
				}
				else if (adjStorage != null) {
					int maxDrain = tanks[i].getFluidAmount()/2;
					FluidStack stack = adjStorage.getTankProperties()[0].getContents();
					if (stack != null) maxDrain -= stack.amount/2;
					if (maxDrain > 0) tanks[i].drain(adjStorage.fill(tanks[i].drain(tanks[i].getCapacity(), false), true), true);
				}
			}
		}
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && tanks.length > 0 && tanks != null) {
			return true;
		}
		if (tanks.length > 0 && tanks != null) {
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return true;
			//else if (capability == Capabilities.GAS_HANDLER_CAPABILITY) return true;
			//else if (capability == Capabilities.TUBE_CONNECTION_CAPABILITY) return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (tanks.length > 0 && tanks != null) {
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
			//if (capability == Capabilities.GAS_HANDLER_CAPABILITY) return Capabilities.GAS_HANDLER_CAPABILITY.cast(this);
			//if (capability == Capabilities.TUBE_CONNECTION_CAPABILITY) return Capabilities.TUBE_CONNECTION_CAPABILITY.cast(this);
		}
		return super.getCapability(capability, facing);
	}
}
