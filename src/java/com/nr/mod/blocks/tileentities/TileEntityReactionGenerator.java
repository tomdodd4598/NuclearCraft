package com.nr.mod.blocks.tileentities;

import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
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
import cofh.api.energy.IEnergyReceiver;
import cofh.lib.util.helpers.EnergyHelper;

import com.nr.mod.NuclearRelativistics;
import com.nr.mod.items.NRItems;

public class TileEntityReactionGenerator extends TileEntityInventory implements IEnergyHandler, IEnergyConnection, ISidedInventory, IEnergyReceiver
{
    public EnergyStorage storage = new EnergyStorage(100000, 100000);
    public int energy;
    public int reactantlevel;
    public int fuellevel;
    public int power = 2*NuclearRelativistics.reactionGeneratorRF;
    public int reactantMax = 500000;
    public int requiredReactant = 1000/NuclearRelativistics.reactionGeneratorEfficiency;
    public int lastE;
    public int E;
    public boolean flag;
    public boolean flag1 = false;
    
    public int fuelMax = 500000;
    public int requiredFuel = 500/NuclearRelativistics.reactionGeneratorEfficiency;
    
    public int maxTransfer = 10000;
    public String direction;
    private static final int[] slots2 = new int[] {0, 1};

    public TileEntityReactionGenerator()
    {
        super.slots = new ItemStack[2];
        super.localizedName = "Reaction Generator";
    }

    public void updateEntity()
    {
    	super.updateEntity();
    	if(!this.worldObj.isRemote){
    	reactant();
    	fuel();
    	energy();
    	addEnergy();
    }
    	if (flag != flag1) { flag1 = flag; BlockReactionGenerator.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord); }
    	markDirty();
    }

    private void energy()
    {
    	lastE = storage.getEnergyStored();
        if (this.fuellevel >= this.requiredFuel && this.storage.getEnergyStored() <= this.storage.getMaxEnergyStored() - this.power && this.reactantlevel >= this.requiredReactant)
        {
        	this.storage.receiveEnergy(this.power, false);
            this.reactantlevel -= this.requiredReactant;
            this.fuellevel -= this.requiredFuel;
            flag = true;
        }
        else { flag = false; }
        E = storage.getEnergyStored();
        
        if (E != lastE) { BlockReactionGenerator.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord); }
    }

	private void addEnergy() {
		lastE = storage.getEnergyStored();
		for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tile = this.worldObj.getTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ);
			
			if (!(tile instanceof TileEntityFissionReactorGraphite) && !(tile instanceof TileEntityReactionGenerator) && !(tile instanceof TileEntityWRTG) && !(tile instanceof TileEntityRTG) && !(tile instanceof TileEntityFusionReactor))
			{
				if ((tile instanceof IEnergyHandler)) {
					storage.extractEnergy(((IEnergyHandler)tile).receiveEnergy(side.getOpposite(), storage.extractEnergy(storage.getMaxEnergyStored(), true), false), false);
					}
				}
			}
		E = storage.getEnergyStored();
        
        if (E != lastE) { BlockReactionGenerator.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord); }
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
		return this.isInvNameLocalized() ? this.localizedName : "Reaction Generator";
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

    private void reactant()
    {
        ItemStack stack = this.getStackInSlot(1);

        if (stack != null && isReactant(stack) && this.reactantlevel + reactantValue(stack) <= this.reactantMax)
        {
            this.addReactant(reactantValue(stack));
            --this.slots[1].stackSize;

            if (this.slots[1].stackSize <= 0)
            {
                this.slots[1] = null;
            }
        }
    }
    
    private void fuel()
    {
        ItemStack stack = this.getStackInSlot(0);

        if (stack != null && isFuel(stack) && this.fuellevel + fuelValue(stack) <= this.fuelMax)
        {
            this.addFuel(fuelValue(stack));
            --this.slots[0].stackSize;

            if (this.slots[0].stackSize <= 0)
            {
                this.slots[0] = null;
            }
        }
    }

    public static boolean isReactant(ItemStack stack)
    {
        return reactantValue(stack) > 0;
    }
    
    public static boolean isFuel(ItemStack stack)
    {
        return fuelValue(stack) > 0;
    }

    public static int reactantValue(ItemStack stack)
    {
        Item i = stack.getItem();
        if(i == new ItemStack (NRItems.parts, 1, 4).getItem() && i.getDamage(stack) == 4)
		   {
				return 100000;
		   }
        else {
        return i == Items.nether_wart ? 5000 : (i == Items.blaze_powder ? 12500 : (i == Items.ghast_tear ? 50000 : (i == Items.gunpowder ? 12500 : (i == Items.redstone ? 5000 : 0))));
    } }
    
	public static int fuelValue(ItemStack stack){
		if (stack == null) return 0; else {
        	Item item = stack.getItem();
        	
        	if(item == new ItemStack (NRItems.fuel, 1, 0).getItem() && item.getDamage(stack) == 0) return 50000;
        	else if(item == new ItemStack (NRItems.fuel, 1, 1).getItem() && item.getDamage(stack) == 1) return 200000;
        	else if(item == new ItemStack (NRItems.fuel, 1, 2).getItem() && item.getDamage(stack) == 2) return 50000;
        	else if(item == new ItemStack (NRItems.fuel, 1, 3).getItem() && item.getDamage(stack) == 3) return 200000;
        	else if(item == new ItemStack (NRItems.fuel, 1, 4).getItem() && item.getDamage(stack) == 4) return 75000;
        	else if(item == new ItemStack (NRItems.fuel, 1, 5).getItem() && item.getDamage(stack) == 5) return 50000;
        	else if(item == new ItemStack (NRItems.fuel, 1, 6).getItem() && item.getDamage(stack) == 6) return 50000;
        	else if(item == new ItemStack (NRItems.fuel, 1, 7).getItem() && item.getDamage(stack) == 7) return 200000;
        	else if(item == new ItemStack (NRItems.fuel, 1, 8).getItem() && item.getDamage(stack) == 8) return 50000;
        	else if(item == new ItemStack (NRItems.fuel, 1, 9).getItem() && item.getDamage(stack) == 9) return 200000;
        	else if(item == new ItemStack (NRItems.fuel, 1, 10).getItem() && item.getDamage(stack) == 10) return 75000;
        	else if(item == new ItemStack (NRItems.fuel, 1, 11).getItem() && item.getDamage(stack) == 11) return 50000;
         	else if(item == new ItemStack (NRItems.fuel, 1, 12).getItem() && item.getDamage(stack) == 12) return 200000;
         	else if(item == new ItemStack (NRItems.fuel, 1, 13).getItem() && item.getDamage(stack) == 13) return 50000;
         	else if(item == new ItemStack (NRItems.fuel, 1, 14).getItem() && item.getDamage(stack) == 14) return 200000;
         	else if(item == new ItemStack (NRItems.fuel, 1, 15).getItem() && item.getDamage(stack) == 15) return 75000;
         	else if(item == new ItemStack (NRItems.fuel, 1, 16).getItem() && item.getDamage(stack) == 16) return 50000;
         	else if(item == new ItemStack (NRItems.fuel, 1, 17).getItem() && item.getDamage(stack) == 17) return 50000;
         	else if(item == new ItemStack (NRItems.fuel, 1, 18).getItem() && item.getDamage(stack) == 18) return 200000;
         	else if(item == new ItemStack (NRItems.fuel, 1, 19).getItem() && item.getDamage(stack) == 19) return 50000;
         	else if(item == new ItemStack (NRItems.fuel, 1, 20).getItem() && item.getDamage(stack) == 20) return 200000;
         	else if(item == new ItemStack (NRItems.fuel, 1, 21).getItem() && item.getDamage(stack) == 21) return 75000;
        	
         	else if(item == new ItemStack(NRItems.material, 1, 4).getItem() && item.getDamage(stack) == 4) return 10000;
         	else if(item == new ItemStack(NRItems.material, 1, 5).getItem() && item.getDamage(stack) == 5) return 10000;
         	else if(item == new ItemStack(NRItems.material, 1, 19).getItem() && item.getDamage(stack) == 19) return 10000;
         	else if(item == new ItemStack(NRItems.material, 1, 20).getItem() && item.getDamage(stack) == 20) return 10000;
        	
         	else if(item == new ItemStack (NRItems.fuel, 1, 51).getItem() && item.getDamage(stack) == 51) return 75000;
         	else if(item == new ItemStack (NRItems.fuel, 1, 52).getItem() && item.getDamage(stack) == 52) return 300000;
         	else if(item == new ItemStack (NRItems.fuel, 1, 53).getItem() && item.getDamage(stack) == 53) return 75000;
         	else if(item == new ItemStack (NRItems.fuel, 1, 54).getItem() && item.getDamage(stack) == 54) return 300000;
         	else if(item == new ItemStack (NRItems.fuel, 1, 55).getItem() && item.getDamage(stack) == 55) return 75000;
         	else if(item == new ItemStack (NRItems.fuel, 1, 56).getItem() && item.getDamage(stack) == 56) return 300000;
         	else if(item == new ItemStack (NRItems.fuel, 1, 57).getItem() && item.getDamage(stack) == 57) return 75000;
         	else if(item == new ItemStack (NRItems.fuel, 1, 58).getItem() && item.getDamage(stack) == 58) return 300000;
         	else if(item == new ItemStack (NRItems.fuel, 1, 59).getItem() && item.getDamage(stack) == 59) return 75000;
         	else if(item == new ItemStack (NRItems.fuel, 1, 60).getItem() && item.getDamage(stack) == 60) return 300000;
         	else if(item == new ItemStack (NRItems.fuel, 1, 61).getItem() && item.getDamage(stack) == 61) return 75000;
         	else if(item == new ItemStack (NRItems.fuel, 1, 62).getItem() && item.getDamage(stack) == 62) return 300000;
         	else if(item == new ItemStack (NRItems.fuel, 1, 63).getItem() && item.getDamage(stack) == 63) return 75000;
         	else if(item == new ItemStack (NRItems.fuel, 1, 64).getItem() && item.getDamage(stack) == 64) return 300000;
         	else if(item == new ItemStack (NRItems.fuel, 1, 65).getItem() && item.getDamage(stack) == 65) return 75000;
         	else if(item == new ItemStack (NRItems.fuel, 1, 66).getItem() && item.getDamage(stack) == 66) return 300000;
        	
         	else if(item == new ItemStack(NRItems.material, 1, 53).getItem() && item.getDamage(stack) == 53) return 15000;
         	else if(item == new ItemStack(NRItems.material, 1, 54).getItem() && item.getDamage(stack) == 54) return 15000;
        	
         	else return 0;
        }
	}

    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        if (nbt.hasKey("storage"))
        {
            this.storage.readFromNBT(nbt.getCompoundTag("storage"));
        }

        this.direction = nbt.getString("facing");
        this.reactantlevel = nbt.getInteger("Reactant");
        this.fuellevel = nbt.getInteger("Fuel");
        this.power = nbt.getInteger("Power");
        this.flag = nbt.getBoolean("flag");
        this.flag1 = nbt.getBoolean("flag1");
        this.lastE = nbt.getInteger("lE");
        this.E = nbt.getInteger("E");
        NBTTagList list = nbt.getTagList("Items", 10);
        this.slots = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < list.tagCount(); ++i)
        {
            NBTTagCompound compound = list.getCompoundTagAt(i);
            byte b = compound.getByte("Slot");

            if (b >= 0 && b < this.slots.length)
            {
                this.slots[b] = ItemStack.loadItemStackFromNBT(compound);
            }
        }
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        
        NBTTagCompound energyTag = new NBTTagCompound();
        this.storage.writeToNBT(energyTag);
        nbt.setTag("storage", energyTag);
        nbt.setInteger("Reactant", this.reactantlevel);
        nbt.setInteger("Fuel", this.fuellevel);
        nbt.setInteger("Power", this.power);
        nbt.setBoolean("flag", this.flag);
        nbt.setBoolean("flag1", this.flag1);
        nbt.setInteger("lE", this.lastE);
        nbt.setInteger("E", this.E);
        NBTTagList list = new NBTTagList();

        for (int i = 0; i < this.slots.length; ++i)
        {
            if (this.slots[i] != null)
            {
                NBTTagCompound compound = new NBTTagCompound();
                compound.setByte("Slot", (byte)i);
                this.slots[i].writeToNBT(compound);
                list.appendTag(compound);
            }
        }

        nbt.setTag("Items", list);
        
        if(this.isInvNameLocalized())
		{
			nbt.setString("CustomName", this.localizedName);
		}
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbtTag = new NBTTagCompound();
        nbtTag.setInteger("Energy", this.storage.getEnergyStored());
        this.energy = nbtTag.getInteger("Energy");
        this.writeToNBT(nbtTag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbtTag);
    }

    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
    {
    	super.onDataPacket(net, packet);
        this.readFromNBT(packet.func_148857_g());
    }
	
    public void addReactant(int add)
    {
        this.reactantlevel += add;
    }
    
    public void addFuel(int add)
    {
        this.fuellevel += add;
    }

    public void removeReactant(int remove)
    {
        this.reactantlevel -= remove;
    }
    
    public void removeFuel(int remove)
    {
        this.fuellevel -= remove;
    }

    public boolean canConnectEnergy(ForgeDirection from)
    {
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

    public int getEnergyStored(ForgeDirection paramForgeDirection)
    {
        return this.storage.getEnergyStored();
    }

    public int getMaxEnergyStored(ForgeDirection paramForgeDirection)
    {
        return this.storage.getMaxEnergyStored();
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (slot == 0) return isFuel(stack);
        else if (slot == 1) return isReactant(stack);
        else return false;
    }

    /**
     * Returns an array containing the indices of the slots that can be accessed by automation on the given side of this
     * block.
     */
    public int[] getAccessibleSlotsFromSide(int slot)
    {
        return slots2;
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    public boolean canInsertItem(int slot, ItemStack stack, int par)
    {
        return this.isItemValidForSlot(slot, stack);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    public boolean canExtractItem(int slot, ItemStack stack, int slots)
    {
        return false;
    }
}
