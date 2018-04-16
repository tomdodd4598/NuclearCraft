package nc.tile.energyFluid;

import nc.config.NCConfig;
import nc.tile.energy.TileEnergy;
import nc.tile.fluid.IFluidSpread;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.Tank;
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

public abstract class TileEnergyFluid extends TileEnergy implements ITileFluid, IFluidHandler/*, IGasHandler, ITubeConnection*/ {
	
	public FluidConnection[] fluidConnections;
	public final Tank[] tanks;
	public boolean areTanksShared = false;
	
	public TileEnergyFluid(int capacity, EnergyConnection[] energyConnections, int fluidCapacity, FluidConnection fluidConnections, String[]... allowedFluids) {
		this(capacity, capacity, capacity, energyConnections, new int[] {fluidCapacity}, new int[] {fluidCapacity}, new int[] {fluidCapacity}, new FluidConnection[] {fluidConnections}, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, EnergyConnection[] energyConnections, int[] fluidCapacity, FluidConnection[] fluidConnections, String[]... allowedFluids) {
		this(capacity, capacity, capacity, energyConnections, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, EnergyConnection[] energyConnections, int fluidCapacity, int maxFluidTransfer, FluidConnection fluidConnections, String[]... allowedFluids) {
		this(capacity, capacity, capacity, energyConnections, new int[] {fluidCapacity}, new int[] {maxFluidTransfer}, new int[] {maxFluidTransfer}, new FluidConnection[] {fluidConnections}, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, EnergyConnection[] energyConnections, int[] fluidCapacity, int[] maxFluidTransfer, FluidConnection[] fluidConnections, String[]... allowedFluids) {
		this(capacity, capacity, capacity, energyConnections, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, EnergyConnection[] energyConnections, int fluidCapacity, int maxFluidReceive, int maxFluidExtract, FluidConnection fluidConnections, String[]... allowedFluids) {
		this(capacity, capacity, capacity, energyConnections, new int[] {fluidCapacity}, new int[] {maxFluidReceive}, new int[] {maxFluidExtract}, new FluidConnection[] {fluidConnections}, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, EnergyConnection[] energyConnections, int[] fluidCapacity, int[] maxFluidReceive, int[] maxFluidExtract, FluidConnection[] fluidConnections, String[]... allowedFluids) {
		this(capacity, capacity, capacity, energyConnections, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, int maxTransfer, EnergyConnection[] energyConnections, int fluidCapacity, FluidConnection fluidConnections, String[]... allowedFluids) {
		this(capacity, maxTransfer, maxTransfer, energyConnections, new int[] {fluidCapacity}, new int[] {fluidCapacity}, new int[] {fluidCapacity}, new FluidConnection[] {fluidConnections}, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, int maxTransfer, EnergyConnection[] energyConnections, int[] fluidCapacity, FluidConnection[] fluidConnections, String[]... allowedFluids) {
		this(capacity, maxTransfer, maxTransfer, energyConnections, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, int maxTransfer, EnergyConnection[] energyConnections, int fluidCapacity, int maxFluidTransfer, FluidConnection fluidConnections, String[]... allowedFluids) {
		this(capacity, maxTransfer, maxTransfer, energyConnections, new int[] {fluidCapacity}, new int[] {maxFluidTransfer}, new int[] {maxFluidTransfer}, new FluidConnection[] {fluidConnections}, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, int maxTransfer, EnergyConnection[] energyConnections, int[] fluidCapacity, int[] maxFluidTransfer, FluidConnection[] fluidConnections, String[]... allowedFluids) {
		this(capacity, maxTransfer, maxTransfer, energyConnections, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, int maxTransfer, EnergyConnection[] energyConnections, int fluidCapacity, int maxFluidReceive, int maxFluidExtract, FluidConnection fluidConnections, String[]... allowedFluids) {
		this(capacity, maxTransfer, maxTransfer, energyConnections, new int[] {fluidCapacity}, new int[] {maxFluidReceive}, new int[] {maxFluidExtract}, new FluidConnection[] {fluidConnections}, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, int maxTransfer, EnergyConnection[] energyConnections, int[] fluidCapacity, int[] maxFluidReceive, int[] maxFluidExtract, FluidConnection[] fluidConnections, String[]... allowedFluids) {
		this(capacity, maxTransfer, maxTransfer, energyConnections, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, int maxReceive, int maxExtract, EnergyConnection[] energyConnections, int fluidCapacity, FluidConnection fluidConnections, String[]... allowedFluids) {
		this(capacity, maxReceive, maxExtract, energyConnections, new int[] {fluidCapacity}, new int[] {fluidCapacity}, new int[] {fluidCapacity}, new FluidConnection[] {fluidConnections}, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, int maxReceive, int maxExtract, EnergyConnection[] energyConnections, int[] fluidCapacity, FluidConnection[] fluidConnections, String[]... allowedFluids) {
		this(capacity, maxReceive, maxExtract, energyConnections, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, int maxReceive, int maxExtract, EnergyConnection[] energyConnections, int fluidCapacity, int maxFluidTransfer, FluidConnection fluidConnections, String[]... allowedFluids) {
		this(capacity, maxReceive, maxExtract, energyConnections, new int[] {fluidCapacity}, new int[] {maxFluidTransfer}, new int[] {maxFluidTransfer}, new FluidConnection[] {fluidConnections}, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, int maxReceive, int maxExtract, EnergyConnection[] energyConnections, int[] fluidCapacity, int[] maxFluidTransfer, FluidConnection[] fluidConnections, String[]... allowedFluids) {
		this(capacity, maxReceive, maxExtract, energyConnections, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, int maxReceive, int maxExtract, EnergyConnection[] energyConnections, int fluidCapacity, int maxFluidReceive, int maxFluidExtract, FluidConnection fluidConnections, String[]... allowedFluids) {
		this(capacity, maxReceive, maxExtract, energyConnections, new int[] {fluidCapacity}, new int[] {maxFluidReceive}, new int[] {maxFluidExtract}, new FluidConnection[] {fluidConnections}, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, int maxReceive, int maxExtract, EnergyConnection[] energyConnections, int[] fluidCapacity, int[] maxFluidReceive, int[] maxFluidExtract, FluidConnection[] fluidConnections, String[]... allowedFluids) {
		super(capacity, maxReceive, maxExtract, energyConnections);
		if (fluidCapacity == null || fluidCapacity.length == 0) {
			tanks = null;
		} else {
			Tank[] tankList = new Tank[fluidCapacity.length];
			String[][] fluidWhitelists = new String[fluidCapacity.length][];
			for (int i = 0; i < fluidCapacity.length; i++) {
				if (i < allowedFluids.length) fluidWhitelists[i] = allowedFluids[i];
				else fluidWhitelists[i] = new String[] {};
			}
			for (int i = 0; i < fluidCapacity.length; i++) {
				tankList[i] = new Tank(fluidCapacity[i], maxFluidReceive[i], maxFluidExtract[i], fluidWhitelists[i]);
			}
			tanks = tankList;
		}
		if (fluidConnections == null || fluidConnections.length == 0) {
			this.fluidConnections = null;
		} else {
			FluidConnection[] fluidConnectionList = new FluidConnection[fluidConnections.length];
			for (int i = 0; i < fluidConnections.length; i++) {
				fluidConnectionList[i] = fluidConnections[i];
			}
			this.fluidConnections = fluidConnectionList;
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
		if (getTanks().length == 0 || getTanks() == null) return EmptyFluidHandler.EMPTY_TANK_PROPERTIES_ARRAY;
		IFluidTankProperties[] properties = new IFluidTankProperties[getTanks().length];
		for (int i = 0; i < getTanks().length; i++) {
			properties[i] = new FluidTankProperties(getTanks()[i].getFluid(), getTanks()[i].getCapacity(), getFluidConnections()[i].canFill(), getFluidConnections()[i].canDrain());
		}
		return properties;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (getTanks().length == 0 || getTanks() == null) return 0;
		for (int i = 0; i < getTanks().length; i++) {
			if (getFluidConnections()[i].canFill() && getTanks()[i].isFluidValid(resource) && canFill(resource, i) && getTanks()[i].getFluidAmount() < getTanks()[i].getCapacity() && (getTanks()[i].getFluid() == null || getTanks()[i].getFluid().isFluidEqual(resource))) {
				return getTanks()[i].fill(resource, doFill);
			}
		}
		return 0;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		if (getTanks().length == 0 || getTanks() == null) return null;
		for (int i = 0; i < getTanks().length; i++) {
			if (getFluidConnections()[i].canDrain() && getTanks()[i].getFluid() != null && getTanks()[i].getFluidAmount() > 0) {
				if (resource.isFluidEqual(getTanks()[i].getFluid()) && getTanks()[i].drain(resource, false) != null) return getTanks()[i].drain(resource, doDrain);
			}
		}
		return null;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (getTanks().length == 0 || getTanks() == null) return null;
		for (int i = 0; i < getTanks().length; i++) {
			if (getFluidConnections()[i].canDrain() && getTanks()[i].getFluid() != null && getTanks()[i].getFluidAmount() > 0) {
				if (getTanks()[i].drain(maxDrain, false) != null) return getTanks()[i].drain(maxDrain, doDrain);
			}
		}
		return null;
	}
	
	@Override
	public boolean canFill(FluidStack resource, int tankNumber) {
		if (!areTanksShared) return true;
		
		for (int i = 0; i < getTanks().length; i++) {
			if (i != tankNumber && getFluidConnections()[i].canFill() && getTanks()[i].getFluid() != null) {
				if (getTanks()[i].getFluid().isFluidEqual(resource)) return false;
			}
		}
		return true;
	}
	
	@Override
	public void clearTank(int tankNo) {
		if (tankNo < getTanks().length) getTanks()[tankNo].setFluidStored(null);
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
		
		if (getTanks().length == 0 || getTanks() == null) return 0;
		for (int i = 0; i < getTanks().length; i++) {
			if (fluidConnections[i].canFill() && getTanks()[i].isFluidValid(fluidStack) && canFill(fluidStack, i) && getTanks()[i].getFluidAmount() < getTanks()[i].getCapacity() && (getTanks()[i].getFluid() == null || getTanks()[i].getFluid().isFluidEqual(fluidStack))) {
				return getTanks()[i].fill(fluidStack, doTransfer);
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
		for (int i = 0; i < getTanks().length; i++) {
			if (fluidConnections[i].canFill() && getTanks()[i].getFluid() != null) {
				if (getTanks()[i].getFluidAmount() >= getTanks()[i].getCapacity() && getTanks()[i].getFluid().isFluidEqual(fluidStack)) return false;
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
		if (getTanks().length > 0 && getTanks() != null) for (int i = 0; i < getTanks().length; i++) {
			nbt.setInteger("fluidAmount" + i, getTanks()[i].getFluidAmount());
			nbt.setString("fluidName" + i, getTanks()[i].getFluidName());
		}
		nbt.setBoolean("areTanksShared", areTanksShared);
		return nbt;
	}
		
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		if (getTanks().length > 0 && getTanks() != null) for (int i = 0; i < getTanks().length; i++) {
			if (nbt.getString("fluidName" + i) == "nullFluid" || nbt.getInteger("fluidAmount" + i) == 0) getTanks()[i].setFluidStored(null);
			else getTanks()[i].setFluidStored(FluidRegistry.getFluid(nbt.getString("fluidName" + i)), nbt.getInteger("fluidAmount" + i));
		}
		setTanksShared(nbt.getBoolean("areTanksShared"));
	}
	
	// Fluid Connections
	
	public void setConnection(FluidConnection[] energyConnection) {
		if (getTanks().length > 0 && getTanks() != null) fluidConnections = energyConnection;
	}
	
	public void setConnection(FluidConnection energyConnection, int tankNumber) {
		if (getTanks().length > 0 && getTanks() != null) fluidConnections[tankNumber] = energyConnection;
	}
	
	public void pushFluid() {
		if (getTanks().length > 0 && getTanks() != null) for (int i = 0; i < getTanks().length; i++) {
			if (getTanks()[i].getFluid() == null) return;
			if (getTanks()[i].getFluidAmount() <= 0 || !getFluidConnections()[i].canDrain()) return;
			for (EnumFacing side : EnumFacing.VALUES) {
				TileEntity tile = world.getTileEntity(getPos().offset(side));
				if (tile instanceof ITilePassive) if (!((ITilePassive) tile).canPushFluidsTo()) continue;
				if (tile instanceof ITileFluid) if (!((ITileFluid) tile).getFluidConnections()[i].canFill()) continue;
				IFluidHandler adjStorage = tile == null ? null : tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
				
				if (tile instanceof IFluidHandler) {
					getTanks()[i].drain(((IFluidHandler) tile).fill(getTanks()[i].drain(getTanks()[i].getCapacity(), false), true), true);
				}
				else if (adjStorage != null) {
					getTanks()[i].drain(adjStorage.fill(getTanks()[i].drain(getTanks()[i].getCapacity(), false), true), true);
				}
			}
		}
	}
	
	public void spreadFluid() {
		if (!NCConfig.passive_permeation) return;
		if (getTanks().length > 0 && getTanks() != null) for (int i = 0; i < getTanks().length; i++) {
			if (getTanks()[i].getFluid() == null) return;
			if (getTanks()[i].getFluidAmount() <= 0 || getFluidConnections()[i] == FluidConnection.NON) return;
			for (EnumFacing side : EnumFacing.VALUES) {
				TileEntity tile = world.getTileEntity(getPos().offset(side));
				if (tile instanceof ITilePassive) if (!((ITilePassive) tile).canPushFluidsTo()) continue;
				IFluidHandler adjStorage = tile == null ? null : tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
				
				if (!(tile instanceof IFluidSpread)) continue;
				
				if (tile instanceof IFluidHandler) {
					int maxDrain = getTanks()[i].getFluidAmount()/2;
					FluidStack stack = ((IFluidHandler) tile).getTankProperties()[0].getContents();
					if (stack != null) maxDrain -= stack.amount/2;
					if (maxDrain > 0) getTanks()[i].drain(((IFluidHandler) tile).fill(getTanks()[i].drain(maxDrain, false), true), true);
				}
				else if (adjStorage != null) {
					int maxDrain = getTanks()[i].getFluidAmount()/2;
					FluidStack stack = adjStorage.getTankProperties()[0].getContents();
					if (stack != null) maxDrain -= stack.amount/2;
					if (maxDrain > 0) getTanks()[i].drain(adjStorage.fill(getTanks()[i].drain(getTanks()[i].getCapacity(), false), true), true);
				}
			}
		}
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (getTanks().length > 0 && getTanks() != null) {
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return true;
			//else if (capability == Capabilities.GAS_HANDLER_CAPABILITY) return true;
			//else if (capability == Capabilities.TUBE_CONNECTION_CAPABILITY) return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (getTanks().length > 0 && getTanks() != null) {
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
			//if (capability == Capabilities.GAS_HANDLER_CAPABILITY) return Capabilities.GAS_HANDLER_CAPABILITY.cast(this);
			//if (capability == Capabilities.TUBE_CONNECTION_CAPABILITY) return Capabilities.TUBE_CONNECTION_CAPABILITY.cast(this);
		}
		return super.getCapability(capability, facing);
	}
}
