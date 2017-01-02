package nc.tile.generator;

import nc.tile.machine.TileInventory;
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

public abstract class TileSteamProducer extends TileInventory implements IFluidHandler {
	public boolean flag;
	public boolean flag1 = false;
	public int fluid;
	public FluidTank tank;
	public int[] automation;
	
	public TileSteamProducer(String localName, int fluidMax, int slotsNumber) {
		tank = new FluidTank(fluidMax);
		localizedName = localName;
		slots = new ItemStack[slotsNumber];
		
		int[] a = new int[slotsNumber];
		for (int i = 0; i < a.length; i++) {
			a[i] = i;
		}
		automation = a;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		tank.readFromNBT(nbt.getCompoundTag("tank"));
		flag = nbt.getBoolean("flag");
		flag1 = nbt.getBoolean("flag1");
		NBTTagList list = nbt.getTagList("Items", 10);
		slots = new ItemStack[getSizeInventory()];

		for (int i = 0; i < list.tagCount(); ++i) {
			NBTTagCompound compound = list.getCompoundTagAt(i);
			byte b = compound.getByte("Slot");

			if (b >= 0 && b < slots.length) {
				slots[b] = ItemStack.loadItemStackFromNBT(compound);
			}
		}
	}

	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagCompound fluidTag = new NBTTagCompound();
		tank.writeToNBT(fluidTag);
		nbt.setTag("tank", fluidTag);
		nbt.setBoolean("flag", flag);
		nbt.setBoolean("flag1", flag1);
		NBTTagList list = new NBTTagList();

		for (int i = 0; i < slots.length; ++i) {
			if (slots[i] != null) {
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("Slot", (byte)i);
				slots[i].writeToNBT(compound);
				list.appendTag(compound);
			}
		}

		nbt.setTag("Items", list);
		
		if(isInventoryNameLocalized()) {
			nbt.setString("CustomName", localizedName);
		}
	}

	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		nbtTag.setInteger("fluid", tank.getFluidAmount());
		fluid = nbtTag.getInteger("fluid");
		writeFluid(nbtTag);
		writeToNBT(nbtTag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbtTag);
	}

	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
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

	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		return false;
	}

	public int[] getAccessibleSlotsFromSide(int slot) {
		return automation;
	}

	public boolean canInsertItem(int slot, ItemStack stack, int par) {
		return isItemValidForSlot(slot, stack);
	}

	public boolean canExtractItem(int slot, ItemStack stack, int slots) {
		return false;
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
