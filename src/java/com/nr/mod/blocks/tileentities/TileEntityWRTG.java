 package com.nr.mod.blocks.tileentities;
 
 import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cofh.lib.util.helpers.EnergyHelper;

import com.nr.mod.NuclearRelativistics;

 public class TileEntityWRTG
   extends TileEntityInventory
   implements IEnergyHandler, ISidedInventory
 {
   public EnergyStorage storage;
   public int power = NuclearRelativistics.WRTGRF;
   public int energy;
   public String direction;
   
   public TileEntityWRTG()
   {
     this.storage = new EnergyStorage(power*2, power*2);
     this.localizedName = "WRTG";
   }
   
   public void updateEntity()
   {
	   super.updateEntity();
	   if(!this.worldObj.isRemote){
	    	energy();
	    	addEnergy();
	    }
	   markDirty();
   }
   
   private void energy()
   {
       if (this.storage.getEnergyStored() == 0)
       {
       	this.storage.receiveEnergy(this.power, false);
       }
   }

   private void addEnergy() {
		for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tile = this.worldObj.getTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ);
			
			if (!(tile instanceof TileEntityFissionReactorGraphite) && !(tile instanceof TileEntityReactionGenerator) && !(tile instanceof TileEntityWRTG) && !(tile instanceof TileEntityRTG) && !(tile instanceof TileEntityFusionReactor))
			{
				if ((tile instanceof IEnergyHandler)) {
					storage.extractEnergy(((IEnergyHandler)tile).receiveEnergy(side.getOpposite(), storage.extractEnergy(storage.getMaxEnergyStored(), true), false), false);
					}
				}
			}
		}

   @SuppressWarnings("unused")
	private boolean canAddEnergy()
   {
	   return this.storage.getEnergyStored() == 0 ? false : this.direction != "none";
   }
   
   public void updateHandlers()
   {
       String d = this.getHandlers();

       if (d == null)
       {
           this.direction = "none";
       }
       else
       {
           this.direction = d;
       }
   }
   
   public String getInvName()
	{
		return this.isInvNameLocalized() ? this.localizedName : "WRTG";
	}
	
	public boolean isInvNameLocalized()
	{
		return this.localizedName != null && this.localizedName.length() > 0;
	}

   public String getHandlers()
   {
       TileEntity down = this.worldObj.getTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
       TileEntity up = this.worldObj.getTileEntity(this.xCoord, this.yCoord + 1, this.zCoord);
       TileEntity north = this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord - 1);
       TileEntity south = this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord + 1);
       TileEntity east = this.worldObj.getTileEntity(this.xCoord + 1, this.yCoord, this.zCoord);
       TileEntity west = this.worldObj.getTileEntity(this.xCoord - 1, this.yCoord, this.zCoord);
       return EnergyHelper.isEnergyReceiverFromSide(down, ForgeDirection.DOWN) ? "down" : (EnergyHelper.isEnergyReceiverFromSide(up, ForgeDirection.UP) ? "up" : (EnergyHelper.isEnergyReceiverFromSide(west, ForgeDirection.WEST) ? "west" : (EnergyHelper.isEnergyReceiverFromSide(east, ForgeDirection.EAST) ? "east" : (EnergyHelper.isEnergyReceiverFromSide(north, ForgeDirection.NORTH) ? "north" : (EnergyHelper.isEnergyReceiverFromSide(south, ForgeDirection.SOUTH) ? "south" : "none")))));
   }
   
   public void readFromNBT(NBTTagCompound nbt)
   {
       super.readFromNBT(nbt);

       if (nbt.hasKey("storage"))
       {
           this.storage.readFromNBT(nbt.getCompoundTag("storage"));
       }

       this.direction = nbt.getString("facing");
       this.power = nbt.getInteger("Power");
   }

   public void writeToNBT(NBTTagCompound nbt)
   {
       super.writeToNBT(nbt);
       
       NBTTagCompound energyTag = new NBTTagCompound();
       this.storage.writeToNBT(energyTag);
       nbt.setTag("storage", energyTag);
       nbt.setInteger("Power", this.power);
       
       if(this.isInvNameLocalized())
		{
			nbt.setString("CustomName", this.localizedName);
		}
   }
   
   public Packet getDescriptionPacket()
   {
       NBTTagCompound nbtTag = new NBTTagCompound();
       nbtTag.setInteger("Energy", this.storage.getEnergyStored());
       this.energy = nbtTag.getInteger("Energy");
       this.writeToNBT(nbtTag);
       return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbtTag);
   }

   public boolean canConnectEnergy(ForgeDirection from) {
     return true;
   }
   
   public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
   {
     return 0;
   }
   
 
   public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
   {
     return 0;
   }
   
 
   public int getEnergyStored(ForgeDirection from)
   {
     return this.storage.getEnergyStored();
   }
   
 
   public int getMaxEnergyStored(ForgeDirection from)
   {
     return this.storage.getMaxEnergyStored();
   }

	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return false;
	}

	public boolean canInsertItem(int i, ItemStack itemstack, int j)
	{
		return false;
	}

	public boolean canExtractItem(int i, ItemStack itemstack, int j)
	{
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		return null;
	}
}