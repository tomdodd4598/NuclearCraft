package nc.tile.energyFluid;

import nc.ModCheck;
import nc.energy.EnumStorage.EnergyConnection;
import nc.fluid.EnumTank.FluidConnection;
import nc.fluid.Tank;
import nc.tile.energy.TileEnergy;
import nc.tile.fluid.ITileFluid;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;

public abstract class TileEnergyFluid extends TileEnergy implements ITileFluid, IFluidHandler {
	
	public FluidConnection[] fluidConnection;
	public final Tank[] tanks;
	
	public TileEnergyFluid(int capacity, EnergyConnection connection, int fluidCapacity, FluidConnection fluidConnection, String[]... allowedFluids) {
		this(capacity, capacity, capacity, connection, new int[] {fluidCapacity}, new int[] {fluidCapacity}, new int[] {fluidCapacity}, new FluidConnection[] {fluidConnection}, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, EnergyConnection connection, int[] fluidCapacity, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		this(capacity, capacity, capacity, connection, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, EnergyConnection connection, int fluidCapacity, int maxFluidTransfer, FluidConnection fluidConnection, String[]... allowedFluids) {
		this(capacity, capacity, capacity, connection, new int[] {fluidCapacity}, new int[] {maxFluidTransfer}, new int[] {maxFluidTransfer}, new FluidConnection[] {fluidConnection}, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, EnergyConnection connection, int[] fluidCapacity, int[] maxFluidTransfer, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		this(capacity, capacity, capacity, connection, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, EnergyConnection connection, int fluidCapacity, int maxFluidReceive, int maxFluidExtract, FluidConnection fluidConnection, String[]... allowedFluids) {
		this(capacity, capacity, capacity, connection, new int[] {fluidCapacity}, new int[] {maxFluidReceive}, new int[] {maxFluidExtract}, new FluidConnection[] {fluidConnection}, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, EnergyConnection connection, int[] fluidCapacity, int[] maxFluidReceive, int[] maxFluidExtract, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		this(capacity, capacity, capacity, connection, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, int maxTransfer, EnergyConnection connection, int fluidCapacity, FluidConnection fluidConnection, String[]... allowedFluids) {
		this(capacity, maxTransfer, maxTransfer, connection, new int[] {fluidCapacity}, new int[] {fluidCapacity}, new int[] {fluidCapacity}, new FluidConnection[] {fluidConnection}, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, int maxTransfer, EnergyConnection connection, int[] fluidCapacity, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		this(capacity, maxTransfer, maxTransfer, connection, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, int maxTransfer, EnergyConnection connection, int fluidCapacity, int maxFluidTransfer, FluidConnection fluidConnection, String[]... allowedFluids) {
		this(capacity, maxTransfer, maxTransfer, connection, new int[] {fluidCapacity}, new int[] {maxFluidTransfer}, new int[] {maxFluidTransfer}, new FluidConnection[] {fluidConnection}, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, int maxTransfer, EnergyConnection connection, int[] fluidCapacity, int[] maxFluidTransfer, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		this(capacity, maxTransfer, maxTransfer, connection, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, int maxTransfer, EnergyConnection connection, int fluidCapacity, int maxFluidReceive, int maxFluidExtract, FluidConnection fluidConnection, String[]... allowedFluids) {
		this(capacity, maxTransfer, maxTransfer, connection, new int[] {fluidCapacity}, new int[] {maxFluidReceive}, new int[] {maxFluidExtract}, new FluidConnection[] {fluidConnection}, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, int maxTransfer, EnergyConnection connection, int[] fluidCapacity, int[] maxFluidReceive, int[] maxFluidExtract, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		this(capacity, maxTransfer, maxTransfer, connection, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, int maxReceive, int maxExtract, EnergyConnection connection, int fluidCapacity, FluidConnection fluidConnection, String[]... allowedFluids) {
		this(capacity, maxReceive, maxExtract, connection, new int[] {fluidCapacity}, new int[] {fluidCapacity}, new int[] {fluidCapacity}, new FluidConnection[] {fluidConnection}, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, int maxReceive, int maxExtract, EnergyConnection connection, int[] fluidCapacity, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		this(capacity, maxReceive, maxExtract, connection, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, int maxReceive, int maxExtract, EnergyConnection connection, int fluidCapacity, int maxFluidTransfer, FluidConnection fluidConnection, String[]... allowedFluids) {
		this(capacity, maxReceive, maxExtract, connection, new int[] {fluidCapacity}, new int[] {maxFluidTransfer}, new int[] {maxFluidTransfer}, new FluidConnection[] {fluidConnection}, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, int maxReceive, int maxExtract, EnergyConnection connection, int[] fluidCapacity, int[] maxFluidTransfer, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		this(capacity, maxReceive, maxExtract, connection, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, int maxReceive, int maxExtract, EnergyConnection connection, int fluidCapacity, int maxFluidReceive, int maxFluidExtract, FluidConnection fluidConnection, String[]... allowedFluids) {
		this(capacity, maxReceive, maxExtract, connection, new int[] {fluidCapacity}, new int[] {maxFluidReceive}, new int[] {maxFluidExtract}, new FluidConnection[] {fluidConnection}, allowedFluids);
	}
	
	public TileEnergyFluid(int capacity, int maxReceive, int maxExtract, EnergyConnection connection, int[] fluidCapacity, int[] maxFluidReceive, int[] maxFluidExtract, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		super(capacity, maxReceive, maxExtract, connection);
		if (fluidCapacity == null || fluidCapacity.length == 0) {
			tanks = null;
		} else {
			Tank[] tankList = new Tank[fluidCapacity.length];
			for (int i = 0; i < fluidCapacity.length; i++) {
				tankList[i] = new Tank(fluidCapacity[i], maxFluidReceive[i], maxFluidExtract[i], allowedFluids[i]);
			}
			tanks = tankList;
		}
		if (fluidConnection == null || fluidConnection.length == 0) {
			this.connection = null;
		} else {
			FluidConnection[] connectionList = new FluidConnection[fluidConnection.length];
			for (int i = 0; i < fluidConnection.length; i++) {
				connectionList[i] = fluidConnection[i];
			}
			this.fluidConnection = connectionList;
		}
	}
	
	public IFluidTankProperties[] getTankProperties() {
		if (tanks.length == 0 || tanks == null) return EmptyFluidHandler.EMPTY_TANK_PROPERTIES_ARRAY;
		IFluidTankProperties[] properties = new IFluidTankProperties[tanks.length];
		for (int i = 0; i < tanks.length; i++) {
			properties[i] = new FluidTankProperties(tanks[i].getFluid(), tanks[i].getCapacity(), fluidConnection[i].canFill(), fluidConnection[i].canDrain());
		}
		return properties;
	}

	public int fill(FluidStack resource, boolean doFill) {
		if (tanks.length == 0 || tanks == null) return 0;
		for (int i = 0; i < tanks.length; i++) {
			if (fluidConnection[i].canFill() && tanks[i].isFluidValid(resource) && canFill(resource, i) && tanks[i].getFluidAmount() < tanks[i].getCapacity() && (tanks[i].getFluid() == null || tanks[i].getFluid().isFluidEqual(resource))) {
				return tanks[i].fill(resource, doFill);
			}
		}
		return 0;
	}

	public FluidStack drain(FluidStack resource, boolean doDrain) {
		if (tanks.length == 0 || tanks == null) return null;
		for (int i = 0; i < tanks.length; i++) {
			if (fluidConnection[i].canDrain() && tanks[i].getFluid() != null && tanks[i].getFluidAmount() > 0) {
				return tanks[i].drain(resource.amount, doDrain);
			}
		}
		return null;
	}

	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (tanks.length == 0 || tanks == null) return null;
		for (int i = 0; i < tanks.length; i++) {
			if (fluidConnection[i].canDrain() && tanks[i].getFluid() != null && tanks[i].getFluidAmount() > 0) {
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
		return fluidConnection;
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
		if (tanks.length > 0 && tanks != null) fluidConnection = connection;
	}
	
	public void setConnection(FluidConnection connection, int tankNumber) {
		if (tanks.length > 0 && tanks != null) fluidConnection[tankNumber] = connection;
	}
	
	public void pushFluid() {
		if (tanks.length > 0 && tanks != null) for (int i = 0; i < tanks.length; i++) {
			if (tanks[i].getFluidAmount() <= 0 || !fluidConnection[i].canDrain()) return;
			for (EnumFacing side : EnumFacing.VALUES) {
				TileEntity tile = worldObj.getTileEntity(getPos().offset(side));
				//TileEntity thisTile = world.getTileEntity(getPos());
				
				if (tile instanceof IFluidHandler /*&& tile != thisTile*/) {
					tanks[i].drain(((IFluidHandler) tile).fill(tanks[i].drain(tanks[i].getCapacity(), false), true), true);
				}
			}
		}
	}
	
	// Capability
	
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (CapabilityEnergy.ENERGY == capability) {
			return true;
		}
		if (connection != null && ModCheck.teslaLoaded && connection.canConnect()) {
			if ((capability == TeslaCapabilities.CAPABILITY_CONSUMER && connection.canReceive()) || (capability == TeslaCapabilities.CAPABILITY_PRODUCER && connection.canExtract()) || capability == TeslaCapabilities.CAPABILITY_HOLDER)
				return true;
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && tanks.length > 0 && tanks != null) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (CapabilityEnergy.ENERGY == capability) {
			return (T) storage;
		}
		if (connection != null && ModCheck.teslaLoaded && connection.canConnect()) {
			if ((capability == TeslaCapabilities.CAPABILITY_CONSUMER && connection.canReceive()) || (capability == TeslaCapabilities.CAPABILITY_PRODUCER && connection.canExtract()) || capability == TeslaCapabilities.CAPABILITY_HOLDER)
				return (T) storage;
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && tanks.length > 0 && tanks != null) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
		}
		return super.getCapability(capability, facing);
	}
}
