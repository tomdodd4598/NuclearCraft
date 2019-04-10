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
	
	private int maxTransfer;
	private List<String> allowedFluids;
	
	public Tank(int capacity, List<String> allowedFluids) {
		this(capacity, capacity, allowedFluids);
	}
	
	public Tank(int capacity, int maxTransfer, List<String> allowedFluids) {
		super(capacity);
		this.maxTransfer = maxTransfer;
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
			fluid = null;
			return;
		}
		if (newAmount > capacity) newAmount = capacity;
		fluid = new FluidStack(fluid, newAmount);
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
	
	public final NBTTagCompound writeToNBT(NBTTagCompound nbt, int tankNumber) {
		NBTTagCompound tankTag = new NBTTagCompound();
		if (getFluidAmount() < 0) {
			fluid = null;
		}
		tankTag.setInteger("fluidAmount", getFluidAmount());
		tankTag.setString("fluidName", getFluidName());
		nbt.setTag("tank" + tankNumber, tankTag);
		return nbt;
	}
	
	public final Tank readFromNBT(NBTTagCompound nbt, int tankNumber) {
		if (nbt.hasKey("tank" + tankNumber)) {
			NBTTagCompound tankTag = nbt.getCompoundTag("tank" + tankNumber);
			if (tankTag.getString("fluidName").equals("nullFluid") || tankTag.getInteger("fluidAmount") == 0) {
				fluid = null;
			}
			else {
				fluid = new FluidStack(FluidRegistry.getFluid(tankTag.getString("fluidName")), tankTag.getInteger("fluidAmount"));
			}
			if (getFluidAmount() > capacity) {
				fluid.amount = capacity;
			}
		}
		return this;
	}
	
	// Packets
	
	public void readInfo(TankInfo info) {
		if (info.name().equals("nullFluid")) {
			setFluid(null);
		}
		else {
			setFluid(new FluidStack(FluidRegistry.getFluid(info.name()), info.amount()));
		}
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
			List<TankInfo> infoList = new ArrayList<TankInfo>();
			if (tanks == null || tanks.isEmpty()) return infoList;
			for (Tank tank : tanks) infoList.add(new TankInfo(tank));
			return infoList;
		}
		
		public static List<TankInfo> readBuf(ByteBuf buf, byte numberOfTanks) {
			List<TankInfo> infoList = new ArrayList<TankInfo>();
			for (byte i = 0; i < numberOfTanks; i++) infoList.add(new TankInfo(ByteBufUtils.readUTF8String(buf), buf.readInt()));
			return infoList;
		}
		
		public void writeBuf(ByteBuf buf) {
			ByteBufUtils.writeUTF8String(buf, name);
			buf.writeInt(amount);
		}
	}
}
