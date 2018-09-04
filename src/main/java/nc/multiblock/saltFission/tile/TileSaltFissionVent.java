package nc.multiblock.saltFission.tile;

import java.util.ArrayList;

import nc.config.NCConfig;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.saltFission.SaltFissionReactor;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.Tank;
import nc.util.FluidStackHelper;
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

public class TileSaltFissionVent extends TileSaltFissionPartBase implements IFluidHandler {
	
	public FluidConnection fluidConnection = FluidConnection.BOTH;
	public final Tank tank = new Tank(FluidStackHelper.INGOT_BLOCK_VOLUME, new ArrayList<String>());
	
	public TileSaltFissionVent() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public void onMachineAssembled(SaltFissionReactor controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		if (getWorld().isRemote) return;
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		if (getWorld().isRemote) return;
		//getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()), 2);
	}
	
	@Override
	public void update() {
		super.update();
		if(!world.isRemote) {
			tickTile();
			if (shouldTileCheck()) pushFluid();
		}
	}
	
	@Override
	public void tickTile() {
		tickCount++; tickCount %= NCConfig.machine_update_rate / 4;
	}
	
	// Fluid

	@Override
	public IFluidTankProperties[] getTankProperties() {
		if (tank == null) return EmptyFluidHandler.EMPTY_TANK_PROPERTIES_ARRAY;
		IFluidTankProperties property = new FluidTankProperties(tank.getFluid(), tank.getCapacity(), fluidConnection.canFill(), fluidConnection.canDrain());
		return new IFluidTankProperties[] {property};
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (tank == null) return 0;
		if (fluidConnection.canFill() && tank.isFluidValid(resource) && tank.getFluidAmount() < tank.getCapacity() && (tank.getFluid() == null || tank.getFluid().isFluidEqual(resource))) {
			return tank.fill(resource, doFill);
		}
		return 0;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		if (tank == null) return null;
		if (fluidConnection.canDrain() && tank.getFluid() != null && tank.getFluidAmount() > 0) {
			if (resource.isFluidEqual(tank.getFluid()) && tank.drain(resource, false) != null) return tank.drain(resource, doDrain);
		}
		return null;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (tank == null) return null;
		if (fluidConnection.canDrain() && tank.getFluid() != null && tank.getFluidAmount() > 0) {
			if (tank.drain(maxDrain, false) != null) return tank.drain(maxDrain, doDrain);
		}
		return null;
	}
	
	public void pushFluid() {
		if (tank != null) {
			if (tank.getFluidAmount() <= 0 || !fluidConnection.canDrain()) return;
			for (EnumFacing side : EnumFacing.VALUES) {
				TileEntity tile = world.getTileEntity(getPos().offset(side));
				if (!(tile instanceof TileSaltFissionVessel) && !(tile instanceof TileSaltFissionHeater)) continue;
				if (tile instanceof IFluidHandler) {
					tank.drain(((IFluidHandler) tile).fill(tank.drain(tank.getCapacity(), false), true), true);
				}
				if (tank.getFluidAmount() <= 0) return;
			}
		}
	}
	
	// Mekanism Gas
	
	/*
	 * 
	 */
	
	// NBT
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if (tank != null) {
			nbt.setInteger("fluidAmount", tank.getFluidAmount());
			nbt.setString("fluidName", tank.getFluidName());
		}
		return nbt;
	}
		
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (tank != null) {
			if (nbt.getString("fluidName") == "nullFluid" || nbt.getInteger("fluidAmount") == 0) tank.setFluidStored(null);
			else tank.setFluidStored(FluidRegistry.getFluid(nbt.getString("fluidName")), nbt.getInteger("fluidAmount"));
		}
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (tank != null) {
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return true;
			//else if (capability == Capabilities.GAS_HANDLER_CAPABILITY) return true;
			//else if (capability == Capabilities.TUBE_CONNECTION_CAPABILITY) return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (tank != null) {
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
			//if (capability == Capabilities.GAS_HANDLER_CAPABILITY) return Capabilities.GAS_HANDLER_CAPABILITY.cast(this);
			//if (capability == Capabilities.TUBE_CONNECTION_CAPABILITY) return Capabilities.TUBE_CONNECTION_CAPABILITY.cast(this);
		}
		return super.getCapability(capability, facing);
	}
}
