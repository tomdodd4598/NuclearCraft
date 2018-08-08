package nc.tile.fluid;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import nc.config.NCConfig;
import nc.tile.NCTile;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.Tank;
import nc.tile.passive.ITilePassive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;

public abstract class TileFluid extends NCTile implements ITileFluid, IFluidHandler/*, IGasHandler, ITubeConnection*/ {
	
	public final List<Tank> tanks;
	public List<FluidConnection> fluidConnections;
	public boolean areTanksShared = false;
	public boolean emptyUnusableTankInputs = false;
	public boolean voidExcessFluidOutputs = false;
	
	public TileFluid(int capacity, FluidConnection fluidConnections, List<String> allowedFluids) {
		this(Lists.newArrayList(capacity), Lists.newArrayList(capacity), Lists.newArrayList(capacity), Lists.newArrayList(fluidConnections), Lists.<List<String>>newArrayList(allowedFluids));
	}
	
	public TileFluid(List<Integer> capacity, List<FluidConnection> fluidConnections, List<List<String>> allowedFluids) {
		this(capacity, capacity, capacity, fluidConnections, allowedFluids);
	}
	
	public TileFluid(int capacity, int maxTransfer, FluidConnection fluidConnections, List<String> allowedFluids) {
		this(Lists.newArrayList(capacity), Lists.newArrayList(maxTransfer), Lists.newArrayList(maxTransfer), Lists.newArrayList(fluidConnections), Lists.<List<String>>newArrayList(allowedFluids));
	}
	
	public TileFluid(List<Integer> capacity, List<Integer> maxTransfer, List<FluidConnection> fluidConnections, List<List<String>> allowedFluids) {
		this(capacity, maxTransfer, maxTransfer, fluidConnections, allowedFluids);
	}
	
	public TileFluid(int capacity, int maxReceive, int maxExtract, FluidConnection fluidConnections, List<String> allowedFluids) {
		this(Lists.newArrayList(capacity), Lists.newArrayList(maxReceive), Lists.newArrayList(maxExtract), Lists.newArrayList(fluidConnections), Lists.<List<String>>newArrayList(allowedFluids));
	}
	
	public TileFluid(List<Integer> capacity, List<Integer> maxReceive, List<Integer> maxExtract, List<FluidConnection> fluidConnections, List<List<String>> allowedFluids) {
		super();
		if (capacity == null || capacity.isEmpty()) {
			tanks = new ArrayList<Tank>();
		} else {
			List<Tank> tankList = new ArrayList<Tank>();
			for (int i = 0; i < capacity.size(); i++) {
				List<String> allowedFluidList;
				if (allowedFluids == null || allowedFluids.size() <= i) allowedFluidList = null; else allowedFluidList = allowedFluids.get(i);
				tankList.add(new Tank(capacity.get(i), maxReceive.get(i), maxExtract.get(i), allowedFluidList));
			}
			tanks = tankList;
		}
		if (fluidConnections == null || fluidConnections.isEmpty()) {
			this.fluidConnections = new ArrayList<FluidConnection>();
		} else {
			this.fluidConnections = fluidConnections;
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

	@Override
	public IFluidTankProperties[] getTankProperties() {
		if (getTanks() == null || getTanks().isEmpty()) return EmptyFluidHandler.EMPTY_TANK_PROPERTIES_ARRAY;
		IFluidTankProperties[] properties = new IFluidTankProperties[getTanks().size()];
		for (int i = 0; i < getTanks().size(); i++) {
			properties[i] = new FluidTankProperties(getTanks().get(i).getFluid(), getTanks().get(i).getCapacity(), fluidConnections.get(i).canFill(), fluidConnections.get(i).canDrain());
		}
		return properties;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (getTanks() == null || getTanks().isEmpty()) return 0;
		for (int i = 0; i < getTanks().size(); i++) {
			if (fluidConnections.get(i).canFill() && getTanks().get(i).isFluidValid(resource) && canFill(resource, i) && getTanks().get(i).getFluidAmount() < getTanks().get(i).getCapacity() && (getTanks().get(i).getFluid() == null || getTanks().get(i).getFluid().isFluidEqual(resource))) {
				return getTanks().get(i).fill(resource, doFill);
			}
		}
		return 0;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		if (getTanks() == null || getTanks().isEmpty()) return null;
		for (int i = 0; i < getTanks().size(); i++) {
			if (fluidConnections.get(i).canDrain() && getTanks().get(i).getFluid() != null && getTanks().get(i).getFluidAmount() > 0) {
				if (resource.isFluidEqual(getTanks().get(i).getFluid()) && getTanks().get(i).drain(resource, false) != null) return getTanks().get(i).drain(resource, doDrain);
			}
		}
		return null;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (getTanks() == null || getTanks().isEmpty()) return null;
		for (int i = 0; i < getTanks().size(); i++) {
			if (fluidConnections.get(i).canDrain() && getTanks().get(i).getFluid() != null && getTanks().get(i).getFluidAmount() > 0) {
				if (getTanks().get(i).drain(maxDrain, false) != null) return getTanks().get(i).drain(maxDrain, doDrain);
			}
		}
		return null;
	}
	
	@Override
	public boolean canFill(FluidStack resource, int tankNumber) {
		if (!areTanksShared) return true;
		
		for (int i = 0; i < getTanks().size(); i++) {
			if (i != tankNumber && fluidConnections.get(i).canFill() && getTanks().get(i).getFluid() != null) {
				if (getTanks().get(i).getFluid().isFluidEqual(resource)) return false;
			}
		}
		return true;
	}
	
	@Override
	public void clearTank(int tankNo) {
		if (tankNo < getTanks().size()) getTanks().get(tankNo).setFluidStored(null);
	}
	
	@Override
	public List<Tank> getTanks() {
		return tanks;
	}
	
	@Override
	public List<FluidConnection> getFluidConnections() {
		return fluidConnections;
	}
	
	// Mekanism Gas
	
	/*public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer) {
		String gasName = stack.getGas().getName();
		Fluid fluid = FluidRegistry.getFluid(gasName);
		if (fluid == null) return 0;
		FluidStack fluidStack = new FluidStack(fluid, 1000);
		
		if (getTanks() == null || getTanks().isEmpty()) return 0;
		for (int i = 0; i < getTanks().size(); i++) {
			if (fluidConnections.get(i).canFill() && getTanks().get(i).isFluidValid(fluidStack) && canFill(fluidStack, i) && getTanks().get(i).getFluidAmount() < getTanks().get(i).getCapacity() && (getTanks().get(i).getFluid() == null || getTanks().get(i).getFluid().isFluidEqual(fluidStack))) {
				return getTanks().get(i).fill(fluidStack, doTransfer);
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
		for (int i = 0; i < getTanks().size(); i++) {
			if (fluidConnections.get(i).canFill() && getTanks().get(i).getFluid() != null) {
				if (getTanks().get(i).getFluidAmount() >= getTanks().get(i).getCapacity() && getTanks().get(i).getFluid().isFluidEqual(fluidStack)) return false;
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
		if (getTanks() != null && !getTanks().isEmpty()) for (int i = 0; i < getTanks().size(); i++) {
			nbt.setInteger("fluidAmount" + i, getTanks().get(i).getFluidAmount());
			nbt.setString("fluidName" + i, getTanks().get(i).getFluidName());
		}
		nbt.setBoolean("areTanksShared", areTanksShared);
		nbt.setBoolean("emptyUnusable", emptyUnusableTankInputs);
		nbt.setBoolean("voidExcessOutputs", voidExcessFluidOutputs);
		return nbt;
	}
		
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		if (getTanks() != null && !getTanks().isEmpty()) for (int i = 0; i < getTanks().size(); i++) {
			if (nbt.getString("fluidName" + i) == "nullFluid" || nbt.getInteger("fluidAmount" + i) == 0) getTanks().get(i).setFluidStored(null);
			else getTanks().get(i).setFluidStored(FluidRegistry.getFluid(nbt.getString("fluidName" + i)), nbt.getInteger("fluidAmount" + i));
		}
		setTanksShared(nbt.getBoolean("areTanksShared"));
		setEmptyUnusableTankInputs(nbt.getBoolean("emptyUnusable"));
		setVoidExcessFluidOutputs(nbt.getBoolean("voidExcessOutputs"));
	}
	
	// Fluid Connections
	
	public void setConnection(List<FluidConnection> fluidConnections) {
		if (getTanks() != null && !getTanks().isEmpty()) this.fluidConnections = fluidConnections;
	}
	
	public void setConnection(FluidConnection fluidConnections, int tankNumber) {
		if (getTanks() != null && !getTanks().isEmpty()) this.fluidConnections.set(tankNumber, fluidConnections);
	}
	
	public void pushFluid() {
		if (getTanks() != null && !getTanks().isEmpty()) for (int i = 0; i < getTanks().size(); i++) {
			if (getTanks().get(i).getFluid() == null || getTanks().get(i).getFluid().getFluid() == null) return;
			if (getTanks().get(i).getFluidAmount() <= 0 || !fluidConnections.get(i).canDrain()) return;
			for (EnumFacing side : EnumFacing.VALUES) {
				TileEntity tile = world.getTileEntity(getPos().offset(side));
				if (tile instanceof ITilePassive) if (!((ITilePassive) tile).canPushFluidsTo()) continue;
				if (tile instanceof ITileFluid) if (!((ITileFluid) tile).getFluidConnections().get(i).canFill()) continue;
				IFluidHandler adjStorage = tile == null ? null : tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
				
				if (tile instanceof IFluidHandler) {
					getTanks().get(i).drain(((IFluidHandler) tile).fill(getTanks().get(i).drain(getTanks().get(i).getCapacity(), false), true), true);
				}
				else if (adjStorage != null) {
					getTanks().get(i).drain(adjStorage.fill(getTanks().get(i).drain(getTanks().get(i).getCapacity(), false), true), true);
				}
			}
		}
	}
	
	public void spreadFluid() {
		if (!NCConfig.passive_permeation) return;
		if (getTanks() != null && !getTanks().isEmpty()) for (int i = 0; i < getTanks().size(); i++) {
			if (getTanks().get(i).getFluid() == null || getTanks().get(i).getFluid().getFluid() == null) return;
			if (getTanks().get(i).getFluidAmount() <= 0 || fluidConnections.get(i) == FluidConnection.NON) return;
			for (EnumFacing side : EnumFacing.VALUES) {
				TileEntity tile = world.getTileEntity(getPos().offset(side));
				if (tile instanceof ITilePassive) if (!((ITilePassive) tile).canPushFluidsTo()) continue;
				IFluidHandler adjStorage = tile == null ? null : tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
				
				if (!(tile instanceof IFluidSpread)) continue;
				
				if (tile instanceof IFluidHandler) {
					int maxDrain = getTanks().get(i).getFluidAmount()/2;
					FluidStack stack = ((IFluidHandler) tile).getTankProperties()[0].getContents();
					if (stack != null) maxDrain -= stack.amount/2;
					if (maxDrain > 0) getTanks().get(i).drain(((IFluidHandler) tile).fill(getTanks().get(i).drain(maxDrain, false), true), true);
				}
				else if (adjStorage != null) {
					int maxDrain = getTanks().get(i).getFluidAmount()/2;
					FluidStack stack = adjStorage.getTankProperties()[0].getContents();
					if (stack != null) maxDrain -= stack.amount/2;
					if (maxDrain > 0) getTanks().get(i).drain(adjStorage.fill(getTanks().get(i).drain(getTanks().get(i).getCapacity(), false), true), true);
				}
			}
		}
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (getTanks() != null && !getTanks().isEmpty()) {
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return true;
			//else if (capability == Capabilities.GAS_HANDLER_CAPABILITY) return true;
			//else if (capability == Capabilities.TUBE_CONNECTION_CAPABILITY) return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (getTanks() != null && !getTanks().isEmpty()) {
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
			//if (capability == Capabilities.GAS_HANDLER_CAPABILITY) return Capabilities.GAS_HANDLER_CAPABILITY.cast(this);
			//if (capability == Capabilities.TUBE_CONNECTION_CAPABILITY) return Capabilities.TUBE_CONNECTION_CAPABILITY.cast(this);
		}
		return super.getCapability(capability, facing);
	}
}
