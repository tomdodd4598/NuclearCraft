package nc.tile.machine;
 
import nc.crafting.NCRecipeHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public abstract class TileMachineFluidOut extends TileMachineBase implements IFluidHandler {
	public int fluid;
	public FluidTank tank;
	public int fluidAmount;
	public Fluid fluidType;

	public TileMachineFluidOut(String localName, int energyMax, int fluidMax, int inSize, int outSize, boolean usesUpgrades, boolean usesEnergy, double pt, double er, Fluid flType, int flAmount, int smod, int tmod, NCRecipeHelper ncrecipes) {
		super(localName, energyMax, inSize, outSize, usesUpgrades, usesEnergy, pt, er, smod, tmod, ncrecipes);
		tank = new FluidTank(fluidMax);
		fluidAmount = flAmount;
		fluidType = flType;
	}

	public void updateEntity() {
		upgradeSpeed();
		upgradeEnergy();
		if (hasUpgrades) {
			getProcessTime = Math.ceil(ProcessTime()/speedUpgrade);
		} else {
			getProcessTime = ProcessTime();
		}
		if (hasUpgrades) {
			getEnergyRequired = Math.ceil(speedUpgrade*(EnergyRequired()/energyUpgrade));
		} else {
			getEnergyRequired = EnergyRequired();
		}
		if(!this.worldObj.isRemote) {
			canCook();
			if (canCook()) {
				this.cookTime += 1;
				this.energyStorage.extractEnergy((int) Math.ceil(getEnergyRequired/getProcessTime), false);
				if (this.cookTime >= getProcessTime) {
					this.cookTime = 0;
					cookItem();
					tank.fill(new FluidStack(fluidType, fluidAmount), true);
				}
			} else {
				this.cookTime = 0;
			}
		}
	}

	public boolean canCook() {
		for (int i = 0; i < inputSize; i++) {
			if (this.slots[i] == null) {
				flag = false;
				return false;
			}
		}
		if (tank.getFluidAmount() > tank.getCapacity() - fluidAmount) {
			flag = false;
			return false;
		}
		if (this.cookTime >= getProcessTime) {
			flag = true;
			return true;
		}
		if (getEnergyRequired > this.energyStorage.getMaxEnergyStored() && cookTime <= 0 && this.energyStorage.getEnergyStored() < this.energyStorage.getMaxEnergyStored() - (int) Math.ceil(getEnergyRequired/getProcessTime)) {
			flag = false;
			return false;
		}
		if (getEnergyRequired < this.energyStorage.getMaxEnergyStored() && cookTime <= 0 && getEnergyRequired > this.energyStorage.getEnergyStored()) {
			flag = false;
			return false;
		}
		if (hasEnergy) {
			if (this.energyStorage.getEnergyStored() < 1*((int) Math.ceil(getEnergyRequired/getProcessTime))) {
				flag = false;
				return false;
			}
			if (this.energyStorage.getEnergyStored() == 0) {
				flag = false;
				return false;
			}
		}
		ItemStack[] output = getOutput(inputs());
		if (output == null || output.length != outputSize) {
			return false;
		}
		for(int j = 0; j < outputSize; j++) {
			if (output[j] == null) {
				flag = false;
				return false;
			} else {
				if (this.slots[j + inputSize] != null) {
					if (!this.slots[j + inputSize].isItemEqual(output[j])) {
						flag = false;
						return false;
					} else if (this.slots[j + inputSize].stackSize + output[j].stackSize > this.slots[j + inputSize].getMaxStackSize()) {
						flag = false;
						return false;
					}
				}
			}
		}
		flag = true;
		return true;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		tank.readFromNBT(nbt.getCompoundTag("tank"));
	}
 
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagCompound fluidTag = new NBTTagCompound();
		tank.writeToNBT(fluidTag);
		nbt.setTag("tank", fluidTag);
	}

	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		nbtTag.setInteger("Energy", this.energyStorage.getEnergyStored());
		this.energy = nbtTag.getInteger("Energy");
		nbtTag.setInteger("fluid", tank.getFluidAmount());
		fluid = nbtTag.getInteger("fluid");
		writeFluid(nbtTag);
		writeSides(nbtTag);
		writeEnergy(nbtTag);
		writeToNBT(nbtTag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbtTag);
	}

	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		readSides(packet.func_148857_g());
		readEnergy(packet.func_148857_g());
		readFluid(packet.func_148857_g());
		readFromNBT(packet.func_148857_g());
	}
	
	public void readFluid(NBTTagCompound nbt) {
		if (nbt.hasKey("tank")) {
			tank.readFromNBT(nbt.getCompoundTag("tank"));
		}
	}
	
	public void writeFluid(NBTTagCompound nbt) {
		NBTTagCompound fluidTag = new NBTTagCompound();
		tank.writeToNBT(fluidTag);
		nbt.setTag("tank", fluidTag);
	}
	
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return tank.fill(resource, doFill);
	}

	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return tank.drain(resource.amount, doDrain);
	}

	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return tank.drain(maxDrain, doDrain);
	}

	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return false;
	}

	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return true;
	}

	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] {tank.getInfo()};
	}
}