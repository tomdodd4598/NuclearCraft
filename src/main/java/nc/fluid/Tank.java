package nc.fluid;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

public class Tank implements IFluidTank, INBTSerializable<NBTTagCompound> {
	
	public int maxReceive;
	public int maxExtract;
	
	public FluidStack fluidStored;
	public int fluidCapacity;
	
	private final static String[] ALL = {"ALL"};
	public String[] allowedFluids;
	
	public Tank(int capacity) {
		this(capacity, capacity, capacity, ALL);
	}
	
	public Tank(int capacity, String... allowedFluids) {
		this(capacity, capacity, capacity, allowedFluids);
	}

	public Tank(int capacity, int maxTransfer) {
		this(capacity, maxTransfer, maxTransfer, ALL);
	}
	
	public Tank(int capacity, int maxTransfer, String... allowedFluids) {
		this(capacity, maxTransfer, maxTransfer, allowedFluids);
	}
	
	public Tank(int capacity, int maxReceive, int maxExtract) {
		this(capacity, maxReceive, maxExtract, ALL);
	}

	public Tank(int capacity, int maxReceive, int maxExtract, String... allowedFluids) {
		fluidCapacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
		
		if (allowedFluids == null || allowedFluids.length == 0) this.allowedFluids = ALL; else {
			String[] fluidList = new String[allowedFluids.length];
			for (int i = 0; i < allowedFluids.length; i++) fluidList[i] = allowedFluids[i];
			this.allowedFluids = fluidList;
		}
	}

	public FluidStack getFluid() {
		return fluidStored;
	}

	public int getFluidAmount() {
		return Math.min(fluidStored.amount, Integer.MAX_VALUE);
	}

	public int getCapacity() {
		return Math.min(fluidCapacity, Integer.MAX_VALUE);
	}

	public FluidTankInfo getInfo() {
		return new FluidTankInfo(this);
	}

	public int fill(FluidStack resource, boolean doFill) {
		if (!isFluidValid(resource.getFluid())) return 0;
		int fluidReceived = Math.min(fluidCapacity - fluidStored.amount, Math.min(maxReceive, resource.amount));
		if (doFill) fluidStored = new FluidStack(fluidStored.getFluid(), fluidStored.amount + fluidReceived);
		return (int) fluidReceived;
	}

	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (fluidStored == null) return null;
		int fluidExtracted = Math.min(fluidStored.amount, Math.min(maxExtract, maxDrain));
		if (doDrain) fluidStored = new FluidStack(fluidStored.getFluid(), fluidStored.amount - fluidExtracted);
		Fluid type = fluidStored.getFluid();
		if (fluidStored.amount <= 0) fluidStored = null;
		return new FluidStack(type, fluidExtracted);
	}
	
	public void changeFluidStored(Fluid fluid, int amount) {
		fluidStored = new FluidStack(fluid, fluidStored.amount + amount);
		if (fluidStored.amount > fluidCapacity) fluidStored = new FluidStack(fluid, fluidCapacity);
		else if (fluidStored.amount < 0) fluidStored = null;
	}
	
	public void setFluidStored(Fluid fluid, int amount) {
		fluidStored = new FluidStack(fluid, amount);
		if (fluidStored.amount > fluidCapacity) fluidStored = new FluidStack(fluid, fluidCapacity);
		else if (fluidStored.amount < 0) fluidStored = null;
	}
	
	public void changeFluidStored(int amount) {
		changeFluidStored(fluidStored.getFluid(), amount);
	}
	
	public void setFluidStored(FluidStack stack) {
		fluidStored = stack;
		if (fluidStored.amount > fluidCapacity) fluidStored = new FluidStack(stack.getFluid(), fluidCapacity);
		else if (fluidStored.amount < 0) fluidStored = null;
	}
	
	public boolean isFluidValid(String name) {
		if (allowedFluids == ALL) return true;
		for (int i = 0; i < allowedFluids.length; i++) {
			if (allowedFluids[i] == name) return true;
		}
		return false;
	}
	
	public boolean isFluidValid(Fluid fluid) {
		return isFluidValid(fluid.getName());
	}
	
	public boolean isFluidValid(FluidStack fluid) {
		return isFluidValid(fluid.getFluid().getName());
	}

	// NBT
	
	public NBTTagCompound serializeNBT() {
		return writeToNBT(new NBTTagCompound());
	}

	public void deserializeNBT(NBTTagCompound nbt) {
		readAll(nbt);
	}
		
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		if (fluidStored.amount < 0) fluidStored = null;
		nbt.setInteger("FluidAmount", fluidStored.amount);
		nbt.setString("FluidName", fluidStored.getFluid().getName());
		return nbt;
	}
		
	public final NBTTagCompound writeAll(NBTTagCompound nbt) {
		NBTTagCompound fluidTag = new NBTTagCompound();
		writeToNBT(fluidTag);
		nbt.setTag("fluidStorage", fluidTag);
		return nbt;
	}
		
	public Tank readFromNBT(NBTTagCompound nbt) {
		fluidStored = new FluidStack (FluidRegistry.getFluid(nbt.getString("FluidName")), nbt.getInteger("FluidAmount"));
		if (fluidStored.amount > fluidCapacity) fluidStored.amount = fluidCapacity;
		return this;
	}
		
	public final void readAll(NBTTagCompound nbt) {
		if (nbt.hasKey("fluidStorage")) readFromNBT(nbt.getCompoundTag("fluidStorage"));
	}
}
