package nc.tile.internal.fluid;

import java.util.ArrayList;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class Tank extends FluidTank implements INBTSerializable<NBTTagCompound> {
	
	public int maxReceive, maxExtract;
	
	public boolean strictlyInput, strictlyOutput;
	
	protected IFluidTankProperties[] tankProperties;
	
	public ArrayList<String> allowedFluids;
	
	public Tank(int capacity, boolean strictlyIn, boolean strictlyOut, String... allowedFluids) {
		this(capacity, capacity, capacity, strictlyIn, strictlyOut, allowedFluids);
	}
	
	public Tank(int capacity, String... allowedFluids) {
		this(capacity, capacity, capacity, false, false, allowedFluids);
	}
	
	public Tank(int capacity, int maxTransfer, boolean strictlyIn, boolean strictlyOut, String... allowedFluids) {
		this(capacity, maxTransfer, maxTransfer, strictlyIn, strictlyOut, allowedFluids);
	}
	
	public Tank(int capacity, int maxTransfer, String... allowedFluids) {
		this(capacity, maxTransfer, maxTransfer, false, false, allowedFluids);
	}

	public Tank(int capacity, int maxReceive, int maxExtract, boolean strictlyIn, boolean strictlyOut, String... allowedFluids) {
		super(capacity);
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
		
		strictlyInput = strictlyIn;
		strictlyOutput = strictlyOut;
		
		if (allowedFluids == null || allowedFluids.length == 0) this.allowedFluids = null; else {
			String[] fluidList = new String[allowedFluids.length];
			for (int i = 0; i < allowedFluids.length; i++) fluidList[i] = allowedFluids[i];
			this.allowedFluids = Lists.newArrayList(fluidList);
		}
	}
	
	public Tank(int capacity, int maxReceive, int maxExtract, String... allowedFluids) {
		this(capacity, maxReceive, maxExtract, false, false, allowedFluids);
	}
	
	public String getFluidName() {
		if (fluid == null) return "nullFluid";
		if (fluid.getFluid() == null) return "nullFluid";
		
		return FluidRegistry.getFluidName(getFluid());
	}
	
	public String getFluidLocalizedName() {
		if (fluid == null) return "";
		if (fluid.getFluid() == null) return "";
		
		return fluid.getLocalizedName();
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (!isFluidValid(resource.getFluid()) || (fluid != null && (fluid != null ? !fluid.isFluidEqual(resource) : false)) || strictlyOutput) return 0;
		int fluidReceived = Math.min(capacity - getFluidAmount(), Math.min(maxReceive, resource.amount));
		if (doFill) fluid = new FluidStack(resource, getFluidAmount() + fluidReceived);
		return fluidReceived;
	}
	
	@Override
	public int fillInternal(FluidStack resource, boolean doFill) {
		if (strictlyOutput) return 0;
		return super.fillInternal(resource, doFill);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (fluid == null || strictlyInput) return null;
		int fluidExtracted = Math.min(getFluidAmount(), Math.min(maxExtract, maxDrain));
		if (doDrain) fluid = new FluidStack(fluid.getFluid(), getFluidAmount() - fluidExtracted);
		Fluid type = fluid.getFluid();
		if (getFluidAmount() <= 0) fluid = null;
		return new FluidStack(type, fluidExtracted);
	}
	
	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		if (fluid == null || resource == null || !fluid.isFluidEqual(resource) || strictlyInput) return null;
		int fluidExtracted = Math.min(getFluidAmount(), Math.min(maxExtract, resource.amount));
		if (doDrain) fluid = new FluidStack(fluid.getFluid(), getFluidAmount() - fluidExtracted);
		if (getFluidAmount() <= 0) fluid = null;
		return new FluidStack(resource, fluidExtracted);
	}
	
	@Override
	@Nullable
	public FluidStack drainInternal(FluidStack resource, boolean doDrain) {
		if (strictlyInput) return null;
		return super.drainInternal(resource, doDrain);
	}
	
	@Override
	@Nullable
	public FluidStack drainInternal(int maxDrain, boolean doDrain) {
		if (strictlyInput) return null;
		return super.drainInternal(maxDrain, doDrain);
	}
	
	public void changeFluidStored(Fluid fluid, int amount) {
		this.fluid = new FluidStack(fluid, getFluidAmount() + amount);
		if (getFluidAmount() > capacity) this.fluid = new FluidStack(this.fluid, capacity);
		else if (getFluidAmount() < 0) this.fluid = null;
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
	
	public void setFluidStored(FluidStack fluid, int amount) {
		if (amount <= 0 || fluid == null) {
			this.fluid = null;
			return;
		}
		this.fluid = new FluidStack(fluid, amount);
		if (getFluidAmount() > capacity) this.fluid = new FluidStack(this.fluid, capacity);
		else if (getFluidAmount() < 0) this.fluid = null;
	}
	
	public void changeFluidStored(int amount) {
		changeFluidStored(fluid.getFluid(), amount);
	}
	
	public void setFluidStored(FluidStack stack) {
		if (stack == null || stack.amount <= 0) {
			fluid = null;
			return;
		}
		fluid = stack;
		if (getFluidAmount() > capacity) fluid = new FluidStack(stack.getFluid(), capacity);
		else if (getFluidAmount() < 0) fluid = null;
	}
	
	public void setFluidAmount(int amount) {
		if(fluid == null) return;
		if(amount < 0) amount = 0;
		else if(amount > capacity) amount = capacity;
		fluid.amount = amount;
    }
	
	public void setTankCapacity(int newCapacity) {
		if(newCapacity == capacity || newCapacity <= 0) return;
		capacity = newCapacity;
		if(newCapacity < getFluidAmount()) setFluidAmount(newCapacity);
    }
	
	public void mergeTanks(Tank other) {
		if (!getFluid().isFluidEqual(other.getFluid())) {
			setFluidStored(null);
			return;
		}
		setFluidAmount(getFluidAmount() + other.getFluidAmount());
		setTankCapacity(capacity + other.capacity);
	}
	
	public void setMaxTransfer(int newMaxTransfer) {
		if(newMaxTransfer < 0) return;
		if(newMaxTransfer != maxReceive) maxReceive = newMaxTransfer;
		if(newMaxTransfer != maxExtract) maxExtract = newMaxTransfer;
    }
	
	public void setMaxReceive(int newMaxReceive) {
		if(newMaxReceive == maxReceive || newMaxReceive < 0) return;
		maxReceive = newMaxReceive;
    }
	
	public void setMaxExtract(int newMaxExtract) {
		if(newMaxExtract == maxExtract || newMaxExtract < 0) return;
		maxReceive = newMaxExtract;
    }
	
	public boolean isFluidValid(String name) {
		if (allowedFluids == null) return true;
		return allowedFluids.contains(name);
	}
	
	public boolean isFluidValid(Fluid fluid) {
		if (fluid == null) return false;
		else return isFluidValid(fluid.getName());
	}
	
	public boolean isFluidValid(FluidStack fluid) {
		if (fluid == null) return false;
		else return isFluidValid(fluid.getFluid().getName());
	}
	
	public void setStrictlyInput(boolean input) {
		strictlyInput = input;
	}
	
	public void setStrictlyOutput(boolean output) {
		strictlyOutput = output;
	}

	// NBT
	
	@Override
	public NBTTagCompound serializeNBT() {
		return writeAll(new NBTTagCompound());
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		readAll(nbt);
	}
		
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		if (getFluidAmount() < 0) fluid = null;
		nbt.setInteger("FluidAmount", getFluidAmount());
		nbt.setString("FluidName", getFluidName());
		return nbt;
	}
		
	public final NBTTagCompound writeAll(NBTTagCompound nbt) {
		NBTTagCompound fluidTag = new NBTTagCompound();
		writeToNBT(fluidTag);
		nbt.setTag("fluidStorage", fluidTag);
		return nbt;
	}
		
	@Override
	public Tank readFromNBT(NBTTagCompound nbt) {
		if (nbt.getString("FluidName") == "nullFluid" || nbt.getInteger("FluidAmount") == 0) fluid = null;
		else fluid = new FluidStack (FluidRegistry.getFluid(nbt.getString("FluidName")), nbt.getInteger("FluidAmount"));
		if (getFluidAmount() > capacity) fluid.amount = capacity;
		return this;
	}
		
	public final void readAll(NBTTagCompound nbt) {
		if (nbt.hasKey("fluidStorage")) readFromNBT(nbt.getCompoundTag("fluidStorage"));
	}
}
