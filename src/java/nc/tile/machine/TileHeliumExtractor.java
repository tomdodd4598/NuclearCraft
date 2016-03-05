package nc.tile.machine;
 
import nc.NuclearCraft;
import nc.block.machine.BlockHeliumExtractor;
import nc.crafting.machine.HeliumExtractorRecipes;
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

public class TileHeliumExtractor extends TileMachine implements IFluidHandler {
	public static final int[] input = {0, 1};
	public static final int[] output = {0, 1};
	public int maxFluid;
	public int fluid;
	public FluidTank tank = new FluidTank(16000);
	
	public TileHeliumExtractor() {
		super("Helium Extractor", 250000, 1, 1, false, true, 400, 16000, NuclearCraft.heliumExtractorSpeed, NuclearCraft.heliumExtractorEfficiency, HeliumExtractorRecipes.instance());
	}
	
	public void updateEntity() {
		getFurnaceSpeed = FurnaceSpeed();
		getRequiredEnergy = RequiredEnergy();
		if(!this.worldObj.isRemote) {
			canCook();
			if (canCook() && this.energyStorage.getEnergyStored() >= ((int) Math.ceil(getRequiredEnergy/getFurnaceSpeed))) {
				this.cookTime += 1;
				this.energyStorage.extractEnergy((int) (getRequiredEnergy/getFurnaceSpeed), false);
				if (this.cookTime >= getFurnaceSpeed) {
					this.cookTime = 0;
					cookItem();
					this.tank.fill(new FluidStack(NuclearCraft.liquidHelium, 1000), true);
				}
			} else {
				this.cookTime = 0;
			}
		}
		if (flag != flag1) {
			flag1 = flag;
			BlockHeliumExtractor.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}
		markDirty();
	}
	
	public boolean canCook() {
		for (int i = 0; i < inputSize; i++) {
			if (this.slots[i] == null) {
				flag = false;
				return false;
			}
		}
		if (this.tank.getFluidAmount() > 15000) {
			flag = false;
			return false;
		}
		if (this.cookTime >= getFurnaceSpeed) {
			flag = true;
			return true;
		}
		if (hasEnergy) {
			if (this.energyStorage.getEnergyStored() < 1*((int) Math.ceil(getRequiredEnergy/getFurnaceSpeed))) {
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
		this.tank.readFromNBT(nbt.getCompoundTag("tank"));
	}
	
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagCompound fluidTag = new NBTTagCompound();
		this.tank.writeToNBT(fluidTag);
		nbt.setTag("tank", fluidTag);
	}
	
	public void writeFluid(NBTTagCompound nbt) {
		NBTTagCompound fluidTag = new NBTTagCompound();
		this.tank.writeToNBT(fluidTag);
		nbt.setTag("tank", fluidTag);
	}
	
	public Packet getDescriptionPacket() {
		super.getDescriptionPacket();
		NBTTagCompound nbtTag = new NBTTagCompound();
		nbtTag.setInteger("fluid", this.tank.getFluidAmount());
		this.fluid = nbtTag.getInteger("fluid");
		nbtTag.setInteger("Energy", this.energyStorage.getEnergyStored());
		this.energy = nbtTag.getInteger("Energy");
		writeSides(nbtTag);
		writeEnergy(nbtTag);
		writeFluid(nbtTag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbtTag);
	}
	
	public void readFluid(NBTTagCompound nbt) {
		if (nbt.hasKey("tank")) {
			this.tank.readFromNBT(nbt.getCompoundTag("tank"));
		}
	}

	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		readFluid(packet.func_148857_g());
	}
	
	public int[] getAccessibleSlotsFromSide(int i) {
		return i == 1 ? input : output;
	}
	
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return this.tank.fill(resource, doFill);
	}

	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return this.tank.drain(resource.amount, doDrain);
	}

	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return this.tank.drain(maxDrain, doDrain);
	}

	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return false;
	}

	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return true;
	}

	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] {this.tank.getInfo()};
	}
}