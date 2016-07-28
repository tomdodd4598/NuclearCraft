package nc.tile.generator;

import nc.tile.machine.TileInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;

public abstract class TileGeneratorInventory extends TileInventory implements IEnergyHandler, IEnergyProvider, IEnergyConnection {
	public int maxStorage;
	public boolean flag;
	public boolean flag1 = false;
	public int energy;
	public EnergyStorage storage;
	public int[] automation;
	
	public TileGeneratorInventory(String localName, int energyMax, int slotsNumber) {
		storage = new EnergyStorage(energyMax, energyMax);
		localizedName = localName;
		slots = new ItemStack[slotsNumber];
		
		int[] a = new int[slotsNumber];
		for (int i = 0; i < a.length; i++) {
			a[i] = i;
		}
		automation = a;
	}

	public void addEnergy() {
		for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tile = worldObj.getTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ);
			if (tile instanceof IEnergyReceiver) {
				storage.extractEnergy(((IEnergyReceiver)tile).receiveEnergy(side.getOpposite(), storage.extractEnergy(storage.getMaxEnergyStored(), true), false), false);
			} else if (tile instanceof IEnergyHandler) {
				storage.extractEnergy(((IEnergyHandler)tile).receiveEnergy(side.getOpposite(), storage.extractEnergy(storage.getMaxEnergyStored(), true), false), false);
			}
		}
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("storage")) {
			storage.readFromNBT(nbt.getCompoundTag("storage"));
		}
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
		
		NBTTagCompound energyTag = new NBTTagCompound();
		storage.writeToNBT(energyTag);
		nbt.setTag("storage", energyTag);
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
		nbtTag.setInteger("Energy", storage.getEnergyStored());
		energy = nbtTag.getInteger("Energy");
		writeEnergy(nbtTag);
		writeToNBT(nbtTag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbtTag);
	}

	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		readEnergy(packet.func_148857_g());
		readFromNBT(packet.func_148857_g());
	}
	
	public void readEnergy(NBTTagCompound nbt) {
		if (nbt.hasKey("storage")) {
			storage.readFromNBT(nbt.getCompoundTag("storage"));
		}
	}
	
	public void writeEnergy(NBTTagCompound nbt) {
		NBTTagCompound fluidTag = new NBTTagCompound();
		storage.writeToNBT(fluidTag);
		nbt.setTag("storage", fluidTag);
	}

	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}

	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		return 0;
	}

	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return storage.extractEnergy(maxExtract, simulate);
	}

	public int getEnergyStored(ForgeDirection paramForgeDirection) {
		return storage.getEnergyStored();
	}

	public int getMaxEnergyStored(ForgeDirection paramForgeDirection) {
		return storage.getMaxEnergyStored();
	}

	public int[] getAccessibleSlotsFromSide(int slot) {
		return automation;
	}

	public boolean canInsertItem(int slot, ItemStack stack, int par) {
		return isItemValidForSlot(slot, stack);
	}
}
