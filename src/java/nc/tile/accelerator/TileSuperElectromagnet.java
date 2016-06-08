package nc.tile.accelerator;

import nc.NuclearCraft;
import nc.block.accelerator.BlockSuperElectromagnet;
import nc.tile.machine.TileInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;

public class TileSuperElectromagnet extends TileInventory implements IEnergyHandler, IEnergyReceiver, ISidedInventory {
	public int maxStorage;
	public boolean flag;
	public boolean flag1;
	public int energy;
	public EnergyStorage storage;
	public static int power = NuclearCraft.superElectromagnetRF*NuclearCraft.EMUpdateRate;
	private int tickCount = 0;
	
	public TileSuperElectromagnet() {
		storage = new EnergyStorage(power*10, power*10);
		localizedName = "Superconducting Electromagnet";
		slots = new ItemStack[1];
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (tickCount >= NuclearCraft.EMUpdateRate) {
			if(!this.worldObj.isRemote) energy();
			if (flag != flag1) { flag1 = flag; BlockSuperElectromagnet.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord); }
			tickCount = 0;
		} else tickCount ++;
		/*if (soundCount >= 67) {
			if (flag1) worldObj.playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "nc:shield5", 0.5F, 1F);
			soundCount = 0;
		} else soundCount ++;*/
		markDirty();
	}
	
	public void energy() {
		 if (this.storage.getEnergyStored() >= power) {
			 this.storage.extractEnergy(power, false);
		 	flag = true;
		 } else {
			 flag = false;
		 }
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("storage")) {
			this.storage.readFromNBT(nbt.getCompoundTag("storage"));
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
		
		NBTTagCompound energyTag = new NBTTagCompound();
		this.storage.writeToNBT(energyTag);
		nbt.setTag("storage", energyTag);
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
	
	public void writeEnergy(NBTTagCompound nbt) {
		NBTTagCompound energyTag = new NBTTagCompound();
		this.storage.writeToNBT(energyTag);
		nbt.setTag("storage", energyTag);
	}

	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		nbtTag.setInteger("Energy", this.storage.getEnergyStored());
		this.energy = nbtTag.getInteger("Energy");
		writeEnergy(nbtTag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbtTag);
	}
	
	public void readEnergy(NBTTagCompound nbt) {
		if (nbt.hasKey("storage")) {
			this.storage.readFromNBT(nbt.getCompoundTag("storage"));
		}
	}

	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		readEnergy(packet.func_148857_g());
	}

	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}

	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		return this.storage.receiveEnergy(maxReceive, simulate);
	}

	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return 0;
	}

	public int getEnergyStored(ForgeDirection paramForgeDirection) {
		return this.storage.getEnergyStored();
	}

	public int getMaxEnergyStored(ForgeDirection paramForgeDirection) {
		return this.storage.getMaxEnergyStored();
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
}
