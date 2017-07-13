package nc.tile.fluid;

import nc.fluid.EnumTank.FluidConnection;
import nc.fluid.Tank;
import nc.tile.NCTile;
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
	
	public FluidConnection[] connection;
	public final Tank[] tanks;
	
	public TileFluid(int capacity, FluidConnection connection, String[]... allowedFluids) {
		this(new int[] {capacity}, new int[] {capacity}, new int[] {capacity}, new FluidConnection[] {connection}, allowedFluids);
	}
	
	public TileFluid(int[] capacity, FluidConnection[] connection, String[]... allowedFluids) {
		this(capacity, capacity, capacity, connection, allowedFluids);
	}
	
	public TileFluid(int capacity, int maxTransfer, FluidConnection connection, String[]... allowedFluids) {
		this(new int[] {capacity}, new int[] {maxTransfer}, new int[] {maxTransfer}, new FluidConnection[] {connection}, allowedFluids);
	}
	
	public TileFluid(int[] capacity, int[] maxTransfer, FluidConnection[] connection, String[]... allowedFluids) {
		this(capacity, maxTransfer, maxTransfer, connection, allowedFluids);
	}
	
	public TileFluid(int capacity, int maxReceive, int maxExtract, FluidConnection connection, String[]... allowedFluids) {
		this(new int[] {capacity}, new int[] {maxReceive}, new int[] {maxExtract}, new FluidConnection[] {connection}, allowedFluids);
	}
	
	public TileFluid(int[] capacity, int[] maxReceive, int[] maxExtract, FluidConnection[] connection, String[]... allowedFluids) {
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
		if (connection == null || connection.length == 0) {
			this.connection = null;
		} else {
			FluidConnection[] connectionList = new FluidConnection[connection.length];
			for (int i = 0; i < connection.length; i++) {
				connectionList[i] = connection[i];
			}
			this.connection = connectionList;
		}
	}

	public IFluidTankProperties[] getTankProperties() {
		if (tanks.length == 0 || tanks == null) return EmptyFluidHandler.EMPTY_TANK_PROPERTIES_ARRAY;
		IFluidTankProperties[] properties = new IFluidTankProperties[tanks.length];
		for (int i = 0; i < tanks.length; i++) {
			properties[i] = new FluidTankProperties(tanks[i].getFluid(), tanks[i].getCapacity(), connection[i].canFill(), connection[i].canDrain());
		}
		return properties;
	}

	public int fill(FluidStack resource, boolean doFill) {
		if (tanks.length == 0 || tanks == null) return 0;
		for (int i = 0; i < tanks.length; i++) {
			if (connection[i].canFill() && tanks[i].isFluidValid(resource) && canFill(resource, i) && tanks[i].getFluidAmount() < tanks[i].getCapacity() && (tanks[i].getFluid() == null || tanks[i].getFluid().isFluidEqual(resource))) {
				return tanks[i].fill(resource, doFill);
			}
		}
		return 0;
	}

	public FluidStack drain(FluidStack resource, boolean doDrain) {
		if (tanks.length == 0 || tanks == null) return null;
		for (int i = 0; i < tanks.length; i++) {
			if (connection[i].canDrain() && tanks[i].getFluid() != null && tanks[i].getFluidAmount() > 0) {
				return tanks[i].drain(resource.amount, doDrain);
			}
		}
		return null;
	}

	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (tanks.length == 0 || tanks == null) return null;
		for (int i = 0; i < tanks.length; i++) {
			if (connection[i].canDrain() && tanks[i].getFluid() != null && tanks[i].getFluidAmount() > 0) {
				return tanks[i].drain(maxDrain, doDrain);
			}
		}
		return null;
	}
	
	public boolean canFill(FluidStack resource, int tankNumber) {
		return true;
	}
	
	public Tank[] getTanks() {
		return tanks;
	}
	
	public FluidConnection[] getFluidConnections() {
		return connection;
	}
	
	// NBT
	
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		if (tanks.length > 0 && tanks != null) for (int i = 0; i < tanks.length; i++) {
			nbt.setInteger("fluidAmount" + i, tanks[i].getFluidAmount());
			nbt.setString("fluidName" + i, tanks[i].getFluidName());
		}
		return nbt;
	}
		
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		if (tanks.length > 0 && tanks != null) for (int i = 0; i < tanks.length; i++) {
			if (nbt.getString("fluidName" + i) == "nullFluid" || nbt.getInteger("fluidAmount" + i) == 0) tanks[i].setFluidStored(null);
			else tanks[i].setFluidStored(FluidRegistry.getFluid(nbt.getString("fluidName" + i)), nbt.getInteger("fluidAmount" + i));
		}
	}
	
	// Fluid Connections
	
	public void setConnection(FluidConnection[] connection) {
		if (tanks.length > 0 && tanks != null) this.connection = connection;
	}
	
	public void setConnection(FluidConnection connection, int tankNumber) {
		if (tanks.length > 0 && tanks != null) this.connection[tankNumber] = connection;
	}
	
	public void pushFluid() {
		if (tanks.length > 0 && tanks != null) for (int i = 0; i < tanks.length; i++) {
			if (tanks[i].getFluidAmount() <= 0 || !connection[i].canDrain()) return;
			for (EnumFacing side : EnumFacing.VALUES) {
				TileEntity tile = worldObj.getTileEntity(getPos().offset(side));
				//TileEntity thisTile = world.getTileEntity(getPos());
				
				if (tile instanceof IFluidHandler /*&& tile != thisTile*/) {
					tanks[i].drain(((IFluidHandler) tile).fill(tanks[i].drain(tanks[i].getCapacity(), false), true), true);
				}
			}
		}
	}
	
	// Mekanism Gas
	
	/*public Fluid fluidFromGas(String gasString) {
		List<String> fluidList = new ArrayList<String>(FluidRegistry.getRegisteredFluids().keySet());
		if (fluidList.contains(gasString)) return FluidRegistry.getFluid(gasString);
		else return null;
	}
	
	public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer) {
		String gasName = stack.getGas().getName();
		Fluid fluid = fluidFromGas(gasName);
		if (fluid == null) return 0;
		return fill(new FluidStack(fluid, stack.amount), doTransfer);
	}

	public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer) {
		return null;
	}

	public boolean canReceiveGas(EnumFacing side, Gas type) {
		for (int i = 0; i < connection.length; i++) {
			if (connection[i].canFill()) return true;
		}
		return false;
	}

	public boolean canDrawGas(EnumFacing side, Gas type) {
		return false;
	}
	
	public boolean canTubeConnect(EnumFacing side) {
		for (FluidConnection con : this.connection) {
			if (con.canConnect()) return true;
		}
		return false;
	}*/
	
	// Capability
	
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && tanks.length > 0 && tanks != null) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && tanks.length > 0 && tanks != null) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
		}
		return super.getCapability(capability, facing);
	}
}
