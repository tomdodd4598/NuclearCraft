package nc.tile.internal.fluid;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.FluidTankPropertiesWrapper;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class Tank extends FluidTank implements INBTSerializable<NBTTagCompound> {
	
	private int maxTransfer;
	private List<String> allowedFluids;
	
	public Tank(int capacity, @Nonnull TankSorption sorption, List<String> allowedFluids) {
		this(capacity, capacity, sorption, allowedFluids);
	}

	public Tank(int capacity, int maxTransfer, @Nonnull TankSorption sorption, List<String> allowedFluids) {
		super(capacity);
		this.maxTransfer = maxTransfer;
		this.allowedFluids = allowedFluids;
		
		canFill = sorption.canFill();
		canDrain = sorption.canDrain();
	}
	
	// FluidTank
	
	@Override
	public boolean canFillFluidType(FluidStack fluid) {
		if (fluid != null && allowedFluids != null && !allowedFluids.contains(fluid.getFluid().getName())) return false;
		return canFill();
	}
	
	public boolean canFillFluidType(Fluid fluid) {
		if (fluid != null && allowedFluids != null && !allowedFluids.contains(fluid.getName())) return false;
		return canFill();
	}
	
	// Tank Methods
	
	public void changeFluidStored(Fluid fluid, int amount) {
		int newAmount = getFluidAmount() + amount;
		if (fluid == null || newAmount <= 0) {
			this.fluid = null;
			return;
		}
		if (newAmount > capacity) newAmount = capacity;
		this.fluid = new FluidStack(fluid, newAmount);
	}
	
	public void changeFluidAmount(int amount) {
		int newAmount = getFluidAmount() + amount;
		if (fluid == null || newAmount <= 0) {
			this.fluid = null;
			return;
		}
		if (newAmount > capacity) newAmount = capacity;
		this.fluid = new FluidStack(fluid, newAmount);
	}
	
	public void setFluidStored(Fluid fluid, int amount) {
		if (amount <= 0) {
			this.fluid = null;
			return;
		}
		this.fluid = new FluidStack(fluid, amount);
		if (getFluidAmount() > capacity) this.fluid = new FluidStack(this.fluid, capacity);
		else if (getFluidAmount() < 0) this.fluid = null;
	}
	
	public void setFluidStored(FluidStack stack) {
		if (stack == null || stack.amount <= 0) {
			fluid = null;
			return;
		}
		if (stack.amount > capacity) stack.amount = capacity;
		fluid = stack;
	}
	
	public void setFluidAmount(int amount) {
		if(fluid == null) return;
		if(amount <= 0) {
			fluid = null;
			return;
		}
		if(amount > capacity) amount = capacity;
		fluid.amount = amount;
    }
	
	public void setTankCapacity(int newCapacity) {
		if(newCapacity == capacity || newCapacity <= 0) return;
		capacity = newCapacity;
		if(newCapacity < getFluidAmount()) setFluidAmount(newCapacity);
    }
	
	public void mergeTank(Tank other) {
		if (fluid == null) {
			fluid = other.fluid;
		}
		else if (!fluid.isFluidEqual(other.getFluid())) {
			setFluidStored(null);
			return;
		}
		setFluidAmount(getFluidAmount() + other.getFluidAmount());
		setTankCapacity(capacity + other.capacity);
	}
	
	public void setMaxTransfer(int newMaxTransfer) {
		if(newMaxTransfer < 0) return;
		if(newMaxTransfer != maxTransfer) maxTransfer = newMaxTransfer;
    }
	
	public boolean isEmpty() {
		return getFluidAmount() == 0;
	}
	
	public boolean isFull() {
		return getFluidAmount() >= capacity;
	}
	
	public boolean canDistribute() {
		return canFill || canDrain;
	}
	
	public IFluidTankProperties getFluidTankProperties() {
		return new FluidTankPropertiesWrapper(this);
	}
	
	public String getFluidName() {
		if (fluid == null || fluid.getFluid() == null) return "nullFluid";
		return FluidRegistry.getFluidName(getFluid());
	}
	
	public String getFluidLocalizedName() {
		if (fluid == null || fluid.getFluid() == null) return "";	
		return fluid.getLocalizedName();
	}

	// NBT
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound fluidTag = new NBTTagCompound();
		writeToNBT(fluidTag);
		NBTTagCompound tankTag = new NBTTagCompound();
		tankTag.setTag("fluidStorage", fluidTag);
		return tankTag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("fluidStorage")) readFromNBT(nbt.getCompoundTag("fluidStorage"));
	}
		
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		if (getFluidAmount() < 0) fluid = null;
		nbt.setInteger("FluidAmount", getFluidAmount());
		nbt.setString("FluidName", getFluidName());
		return nbt;
	}
		
	@Override
	public Tank readFromNBT(NBTTagCompound nbt) {
		if (nbt.getString("FluidName") == "nullFluid" || nbt.getInteger("FluidAmount") == 0) fluid = null;
		else fluid = new FluidStack (FluidRegistry.getFluid(nbt.getString("FluidName")), nbt.getInteger("FluidAmount"));
		if (getFluidAmount() > capacity) fluid.amount = capacity;
		return this;
	}
}
