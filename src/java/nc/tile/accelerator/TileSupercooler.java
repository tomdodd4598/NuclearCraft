package nc.tile.accelerator;

import nc.NuclearCraft;
import nc.block.accelerator.BlockSupercooler;
import nc.tile.machine.TileInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileSupercooler extends TileInventory implements IFluidHandler, ISidedInventory {
	public int maxFluid;
	public boolean flag;
	public boolean flag1;
	public int fluid;
	public FluidTank tank;
	public static int usage = NuclearCraft.electromagnetHe*NuclearCraft.EMUpdateRate/20;
	private int tickCount = 0;
	
	public TileSupercooler() {
		tank = new FluidTank(usage*10);
		localizedName = "Supercooler";
		slots = new ItemStack[1];
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (tickCount >= NuclearCraft.EMUpdateRate) {
			if(!this.worldObj.isRemote) helium();
			if (flag != flag1) { flag1 = flag; BlockSupercooler.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord); }
			tickCount = 0;
		} else tickCount ++;
		/*if (soundCount >= 67) {
			if (flag1) worldObj.playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "nc:shield2", 0.5F, 1F);
			soundCount = 0;
		} else soundCount ++;*/
		markDirty();
	}
	
	public void helium() {
		if (this.tank.getFluidAmount() >= usage) {
			this.tank.fill(new FluidStack(NuclearCraft.liquidHelium, -usage), true);
			flag = true;
		} else {
			flag = false;
		}
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("tank")) {
			this.tank.readFromNBT(nbt.getCompoundTag("tank"));
		}
		this.flag = nbt.getBoolean("flag");
		this.flag1 = nbt.getBoolean("flag1");
		NBTTagList list = nbt.getTagList("Items", 10);
		this.slots = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < list.tagCount(); ++i) {
			NBTTagCompound compound = list.getCompoundTagAt(i);
			byte b = compound.getByte("Slot");

			if (b >= 0 && b < this.slots.length) {
				this.slots[b] = ItemStack.loadItemStackFromNBT(compound);
			}
		}
	}

	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		
		NBTTagCompound fluidTag = new NBTTagCompound();
		this.tank.writeToNBT(fluidTag);
		nbt.setTag("tank", fluidTag);
		nbt.setBoolean("flag", this.flag);
		nbt.setBoolean("flag1", this.flag1);
		NBTTagList list = new NBTTagList();

		for (int i = 0; i < this.slots.length; ++i) {
			if (this.slots[i] != null) {
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("Slot", (byte)i);
				this.slots[i].writeToNBT(compound);
				list.appendTag(compound);
			}
		}

		nbt.setTag("Items", list);
		
		if(this.isInventoryNameLocalized()) {
			nbt.setString("CustomName", this.localizedName);
		}
	}

	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		nbtTag.setInteger("fluid", this.tank.getFluidAmount());
		this.fluid = nbtTag.getInteger("fluid");
		this.writeToNBT(nbtTag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbtTag);
	}

	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		this.readFromNBT(packet.func_148857_g());
	}
	
	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		return false;
	}

	public int[] getAccessibleSlotsFromSide(int slot) {
		return null;
	}

	public boolean canInsertItem(int slot, ItemStack stack, int par) {
		return this.isItemValidForSlot(slot, stack);
	}

	public boolean canExtractItem(int slot, ItemStack stack, int slots) {
		return false;
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
		return fluid == NuclearCraft.liquidHelium ? true : false;
	}

	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return false;
	}

	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] {this.tank.getInfo()};
	}
}
