package nc.tile.storage;

import nc.packet.PacketHandler;
import nc.packet.PacketSideConfig;
import net.minecraft.nbt.NBTTagCompound;
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
import cofh.api.tileentity.IReconfigurableSides;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

public abstract class TileStorage extends TileEntity implements IEnergyHandler, IEnergyReceiver, IEnergyProvider, IEnergyConnection, IReconfigurableSides {
	public int maxStorage;
	public boolean flag;
	public boolean flag1 = false;
	public int energy;
	public EnergyStorage storage;
	public int[] sideMode = new int[6];
	public int[] input, output, disabled;
	
	public TileStorage(int energyMax) {
		storage = new EnergyStorage(energyMax, energyMax);
	}
	
	public void updateEntity() {
		super.updateEntity();
		if(!worldObj.isRemote) {
    		addEnergy();
    	}
		markDirty();
	}

	public void addEnergy() {
		for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tile = worldObj.getTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ);
			if (sideMode[side.ordinal()] == 1) {
				if ((tile instanceof IEnergyReceiver)) {
					storage.extractEnergy(((IEnergyReceiver)tile).receiveEnergy(side.getOpposite(), storage.extractEnergy(storage.getMaxEnergyStored(), true), false), false);
				} else if ((tile instanceof IEnergyHandler)) {
					storage.extractEnergy(((IEnergyHandler)tile).receiveEnergy(side.getOpposite(), storage.extractEnergy(storage.getMaxEnergyStored(), true), false), false);
				}
			}
		}
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		flag = nbt.getBoolean("flag");
		flag1 = nbt.getBoolean("flag1");
		readNBT(nbt);
	}
	
	public void readNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("storage")) {
			storage.readFromNBT(nbt.getCompoundTag("storage"));
		}
		energy = nbt.getInteger("Energy");
		sideMode = nbt.getIntArray("sideMode");
		if (sideMode == null || sideMode.length != 6) {
			sideMode = new int[6];
		}
	}

	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("flag", flag);
		nbt.setBoolean("flag1", flag1);
		writeNBT(nbt);
	}
	
	public void writeNBT(NBTTagCompound nbt) {
		NBTTagCompound energyTag = new NBTTagCompound();
		storage.writeToNBT(energyTag);
		nbt.setTag("storage", energyTag);
		nbt.setInteger("Energy", storage.getEnergyStored());
		nbt.setIntArray("sideMode", sideMode);
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
		NBTTagCompound energyTag = new NBTTagCompound();
		storage.writeToNBT(energyTag);
		nbt.setTag("storage", energyTag);
	}

	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}

	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		return sideMode[from.ordinal()] == 0 ? storage.receiveEnergy(maxReceive, simulate) : 0;
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

	public boolean decrSide(int side) {
		if (!this.getWorldObj().isRemote) {
			if (sideMode[side] >= 3 || sideMode[side] <= 0) {
				sideMode[side] = 2;
			} else if (sideMode[side] == 2) {
				sideMode[side] = 1;
			} else if (sideMode[side] == 1) {
				sideMode[side] = 0;
			}
			PacketHandler.INSTANCE.sendToAllAround(new PacketSideConfig(xCoord, yCoord, zCoord, side, sideMode[side]), new TargetPoint(this.worldObj.provider.dimensionId, xCoord, yCoord, zCoord, 32));
		}
		return false;
	}

	public boolean incrSide(int side) {
		if (!this.getWorldObj().isRemote) {
			if (sideMode[side] >= 2) {
				sideMode[side] = 0;
			} else if (sideMode[side] <= 0) {
				sideMode[side] = 1;
			} else if (sideMode[side] == 1) {
				sideMode[side] = 2;
			}
			PacketHandler.INSTANCE.sendToAllAround(new PacketSideConfig(xCoord, yCoord, zCoord, side, sideMode[side]), new TargetPoint(this.worldObj.provider.dimensionId, xCoord, yCoord, zCoord, 32));
		}
		return false;
	}

	public boolean resetSides() {
		return false;
	}

	public int getNumConfig(int side) {
		return 3;
	}
	
	public boolean setSide(int side, int config) {
		sideMode[side] = config;
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		return true;
	}
}
