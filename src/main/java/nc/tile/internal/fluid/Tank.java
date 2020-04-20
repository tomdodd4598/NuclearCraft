package nc.tile.internal.fluid;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.FluidTankPropertiesWrapper;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class Tank extends FluidTank {
	
	private List<String> allowedFluids;
	
	public Tank(int capacity, List<String> allowedFluids) {
		super(capacity);
		this.allowedFluids = allowedFluids;
	}
	
	// FluidTank
	
	@Override
	public boolean canFillFluidType(FluidStack fluid) {
		return fluid != null && canFillFluidType(fluid.getFluid());
	}
	
	public boolean canFillFluidType(Fluid fluid) {
		return fluid != null && (allowedFluids == null || allowedFluids.contains(fluid.getName()));
	}
	
	// Tank Methods
	
	public void changeFluidStored(Fluid fluid, int amount) {
		amount += getFluidAmount();
		if (fluid == null || amount <= 0) {
			this.fluid = null;
			return;
		}
		this.fluid = new FluidStack(fluid, amount > capacity ? capacity : amount);
	}
	
	public void changeFluidAmount(int amount) {
		amount += getFluidAmount();
		if (fluid == null || amount <= 0) {
			fluid = null;
			return;
		}
		fluid = new FluidStack(fluid, amount > capacity ? capacity : amount);
	}
	
	public void setFluidStored(FluidStack stack) {
		if (stack == null || stack.amount <= 0) {
			fluid = null;
			return;
		}
		if (stack.amount > capacity) {
			stack.amount = capacity;
		}
		fluid = stack;
	}
	
	/** Ignores fluid capacity! */
	public void setFluidAmount(int amount) {
		if (amount <= 0) {
			fluid = null;
		}
		if (fluid == null) {
			return;
		}
		fluid.amount = amount;
	}
	
	/** Ignores fluid amount! */
	public void setTankCapacity(int newCapacity) {
		capacity = Math.max(0, newCapacity);
		/*if (capacity < getFluidAmount()) {
			setFluidAmount(capacity);
		}*/
	}
	
	public boolean isFull() {
		return getFluidAmount() >= capacity;
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
		setTankCapacity(capacity + other.capacity);
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
	
	// NBT
	
	public final NBTTagCompound writeToNBT(NBTTagCompound nbt, String name) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("capacity", capacity);
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
		String name = info.name();
		setFluid(name.equals("null") ? null : new FluidStack(FluidRegistry.getFluid(name), info.amount()));
	}
	
	public static class TankInfo {
		
		private String name;
		private int amount;
		
		public TankInfo(Tank tank) {
			name = tank.getFluidName();
			amount = tank.getFluidAmount();
		}
		
		private TankInfo(String name, int amount) {
			this.name = name;
			this.amount = amount;
		}
		
		public String name() {
			return name;
		}
		
		public int amount() {
			return amount;
		}
		
		public static List<TankInfo> infoList(List<Tank> tanks) {
			List<TankInfo> infoList = new ArrayList<>();
			for (Tank tank : tanks) {
				infoList.add(new TankInfo(tank));
			}
			return infoList;
		}
		
		public static List<TankInfo> readBuf(ByteBuf buf, byte numberOfTanks) {
			List<TankInfo> infoList = new ArrayList<>();
			for (byte i = 0; i < numberOfTanks; i++) {
				infoList.add(new TankInfo(ByteBufUtils.readUTF8String(buf), buf.readInt()));
			}
			return infoList;
		}
		
		public void writeBuf(ByteBuf buf) {
			ByteBufUtils.writeUTF8String(buf, name);
			buf.writeInt(amount);
		}
	}
}
