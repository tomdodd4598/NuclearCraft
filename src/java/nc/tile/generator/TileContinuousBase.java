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

public abstract class TileContinuousBase extends TileEntity implements IEnergyHandler, IEnergyConnection {
	public int maxStorage;
	public int energy;
	public int power = NuclearCraft.WRTGRF;
	public EnergyStorage storage;
	
	public TileContinuousBase(String localName, int energyMax, int slotsNumber) {
		storage = new EnergyStorage(energyMax, energyMax);
	}
	
	public void updateEntity() {
    	super.updateEntity();
    	if(!this.worldObj.isRemote) {
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
			TileEntity tile = this.worldObj.getTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ);
			
			if (!(tile instanceof TileContinuousBase) && !(tile instanceof TileReactionGenerator) && !(tile instanceof TileContinuousBase) && !(tile instanceof TileRTG) && !(tile instanceof TileWRTG) && !(tile instanceof TileFusionReactor) && !(tile instanceof TileFusionReactorBlock) && !(tile instanceof TileFissionReactor)) {
				if ((tile instanceof IEnergyHandler)) {
					storage.extractEnergy(((IEnergyHandler)tile).receiveEnergy(side.getOpposite(), storage.extractEnergy(storage.getMaxEnergyStored(), true), false), false);
					}
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
		nbtTag.setInteger("Energy", this.storage.getEnergyStored());
		this.energy = nbtTag.getInteger("Energy");
		this.writeToNBT(nbtTag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbtTag);
	}

	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		this.readFromNBT(packet.func_148857_g());
	}

	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}

	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		return 0;
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
}