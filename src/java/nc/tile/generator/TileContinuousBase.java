package nc.tile.generator;
 
import nc.NuclearCraft;
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

public abstract class TileContinuousBase extends TileEntity implements IEnergyHandler, IEnergyProvider, IEnergyConnection {
	public int maxStorage;
	public int energy;
	public int power = NuclearCraft.WRTGRF;
	public EnergyStorage storage;
	
	public TileContinuousBase(String localName, int pow) {
		storage = new EnergyStorage(pow*2, pow*2);
		power = pow;
	}
	
	public void updateEntity() {
    	super.updateEntity();
    	if(!worldObj.isRemote) {
    		energy();
    		addEnergy();
    	}
        markDirty();
    }
	
	public void energy() {
		storage.receiveEnergy(power, false);
	}

	public void addEnergy() {
		for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tile = worldObj.getTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ);
			if ((tile instanceof IEnergyReceiver)) {
				storage.extractEnergy(((IEnergyReceiver)tile).receiveEnergy(side.getOpposite(), storage.extractEnergy(storage.getMaxEnergyStored(), true), false), false);
			} else if ((tile instanceof IEnergyHandler)) {
				storage.extractEnergy(((IEnergyHandler)tile).receiveEnergy(side.getOpposite(), storage.extractEnergy(storage.getMaxEnergyStored(), true), false), false);
			}
		}
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
	}

	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
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
}