package nc.tile.internal.fluid;

import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.*;

import java.util.*;

public class Tank extends FluidTank {
	
	protected Set<String> allowedFluids;
	
	public Tank(int capacity, Set<String> allowedFluids) {
		super(capacity);
		this.allowedFluids = allowedFluids;
	}
	
	// FluidTank
	
	@Override
	public boolean canFillFluidType(FluidStack fluidIn) {
		return fluidIn != null && canFillFluidType(fluidIn.getFluid());
	}
	
	public boolean canFillFluidType(Fluid fluidIn) {
		return fluidIn != null && (allowedFluids == null || allowedFluids.contains(fluidIn.getName()));
	}
	
	public void setAllowedFluids(Set<String> allowedFluids) {
		if (allowedFluids == null) {
			this.allowedFluids = null;
		}
		else {
			if (this.allowedFluids == null) {
				this.allowedFluids = new ObjectOpenHashSet<>();
			}
			else {
				this.allowedFluids.clear();
			}
			this.allowedFluids.addAll(allowedFluids);
		}
	}
	
	// Tank Methods
	
	public void changeFluidStored(Fluid fluidIn, int amount) {
		amount += getFluidAmount();
		if (fluidIn == null || amount <= 0) {
			this.fluid = null;
			return;
		}
		this.fluid = new FluidStack(fluidIn, Math.min(amount, getCapacity()));
	}
	
	public void changeFluidAmount(int amount) {
		amount += getFluidAmount();
		if (fluid == null || amount <= 0) {
			fluid = null;
			return;
		}
		fluid = new FluidStack(fluid, Math.min(amount, getCapacity()));
	}
	
	public void setFluidStored(FluidStack stack) {
		if (stack == null || stack.amount <= 0) {
			fluid = null;
			return;
		}
		if (stack.amount > getCapacity()) {
			stack.amount = getCapacity();
		}
		fluid = stack;
	}
	
	/**
	 * Ignores fluid capacity!
	 */
	public void setFluidAmount(int amount) {
		if (amount <= 0) {
			fluid = null;
		}
		if (fluid == null) {
			return;
		}
		fluid.amount = amount;
	}
	
	/**
	 * Ignores fluid amount!
	 */
	public void setTankCapacity(int newCapacity) {
		setCapacity(Math.max(0, newCapacity));
	}
	
	public void clampTankAmount() {
		if (isFull()) {
			setFluidAmount(getCapacity());
		}
		if (getFluidAmount() <= 0) {
			fluid = null;
		}
	}
	
	public boolean isFull() {
		return getFluidAmount() >= getCapacity();
	}
	
	public boolean isEmpty() {
		return getFluidAmount() == 0;
	}
	
	public void mergeTank(Tank other) {
		if (fluid == null) {
			fluid = other.fluid;
		}
		else if (!fluid.isFluidEqual(other.getFluid())) {
			return;
		}
		setFluidAmount(getFluidAmount() + other.getFluidAmount());
		setTankCapacity(getCapacity() + other.getCapacity());
		other.setFluidStored(null);
	}
	
	public IFluidTankProperties getFluidTankProperties() {
		return new FluidTankPropertiesWrapper(this);
	}
	
	public String getFluidName() {
		return fluid == null ? "null" : fluid.getFluid().getName();
	}
	
	public String getFluidLocalizedName() {
		return fluid == null ? "" : fluid.getLocalizedName();
	}
	
	public double getFluidAmountFraction() {
		return (double) getFluidAmount() / (double) getCapacity();
	}
	
	@Override
	public String toString() {
		return "Tank[" + getFluidName() + " x " + getFluidAmount() + "/" + getCapacity() + "]";
	}
	
	// NBT
	
	public final NBTTagCompound writeToNBT(NBTTagCompound nbt, String name) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("capacity", getCapacity());
		writeToNBT(tag);
		nbt.setTag(name, tag);
		return nbt;
	}
	
	public final Tank readFromNBT(NBTTagCompound nbt, String name) {
		if (nbt.hasKey(name, 10)) {
			NBTTagCompound tag = nbt.getCompoundTag(name);
			setCapacity(tag.getInteger("capacity"));
			readFromNBT(tag);
		}
		return this;
	}
	
	// Packets
	
	public void readInfo(TankInfo info) {
		setFluid(info.name.equals("null") ? null : new FluidStack(FluidRegistry.getFluid(info.name), info.amount));
	}
	
	public static class TankInfo {
		
		public final String name;
		public final int amount;
		
		public TankInfo(String name, int amount) {
			this.name = name;
			this.amount = amount;
		}
		
		public TankInfo(Tank tank) {
			this(tank.getFluidName(), tank.getFluidAmount());
		}
		
		public static List<TankInfo> getInfoList(List<Tank> tanks) {
			List<TankInfo> tankInfos = new ArrayList<>();
			for (Tank tank : tanks) {
				tankInfos.add(new TankInfo(tank));
			}
			return tankInfos;
		}
		
		public static void readInfoList(List<TankInfo> tankInfos, List<Tank> tanks) {
			for (int i = 0; i < tanks.size(); ++i) {
				tanks.get(i).readInfo(tankInfos.get(i));
			}
		}
	}
}
