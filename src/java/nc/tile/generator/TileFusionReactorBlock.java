package nc.tile.generator;

import nc.block.NCBlocks;
import nc.block.generator.BlockFusionReactor;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;

public class TileFusionReactorBlock extends TileEntity implements IEnergyHandler, IEnergyConnection, IEnergyReceiver, ISidedInventory {

	public int xOffset;
	public int yOffset;
	public int zOffset;
	public int getBelow;
	
	//private static final int[] slotsBottom = new int[] {0, 1};
	
	public ItemStack[] slots = new ItemStack[2];

	public TileFusionReactorBlock() {
		super();
    }
	
	public void updateEntity() {
    	super.updateEntity();
    	getOffsets(xCoord, yCoord, zCoord);
    	getBelow();
    	markDirty();
    }
	
	public void getOffsets(int x, int y, int z)
	{
		if (worldObj.getBlock(x, y-1, z) == NCBlocks.fusionReactor) {xOffset=0;yOffset=-1;zOffset=0;}
		else if (worldObj.getBlock(x+1, y, z) == NCBlocks.fusionReactor) {xOffset=1;yOffset=0;zOffset=0;}
		else if (worldObj.getBlock(x+1, y, z+1) == NCBlocks.fusionReactor) {xOffset=1;yOffset=0;zOffset=1;}
		else if (worldObj.getBlock(x, y, z+1) == NCBlocks.fusionReactor) {xOffset=0;yOffset=0;zOffset=1;}
		else if (worldObj.getBlock(x-1, y, z+1) == NCBlocks.fusionReactor) {xOffset=-1;yOffset=0;zOffset=1;}
		else if (worldObj.getBlock(x-1, y, z) == NCBlocks.fusionReactor) {xOffset=-1;yOffset=0;zOffset=0;}
		else if (worldObj.getBlock(x-1, y, z-1) == NCBlocks.fusionReactor) {xOffset=-1;yOffset=0;zOffset=-1;}
		else if (worldObj.getBlock(x, y, z-1) == NCBlocks.fusionReactor) {xOffset=0;yOffset=0;zOffset=-1;}
		else if (worldObj.getBlock(x+1, y, z-1) == NCBlocks.fusionReactor) {xOffset=1;yOffset=0;zOffset=-1;}
		
		else if (worldObj.getBlock(x+1, y-1, z) == NCBlocks.fusionReactor) {xOffset=1;yOffset=-1;zOffset=0;}
		else if (worldObj.getBlock(x+1, y-1, z+1) == NCBlocks.fusionReactor) {xOffset=1;yOffset=-1;zOffset=1;}
		else if (worldObj.getBlock(x, y-1, z+1) == NCBlocks.fusionReactor) {xOffset=0;yOffset=-1;zOffset=1;}
		else if (worldObj.getBlock(x-1, y-1, z+1) == NCBlocks.fusionReactor) {xOffset=-1;yOffset=-1;zOffset=1;}
		else if (worldObj.getBlock(x-1, y-1, z) == NCBlocks.fusionReactor) {xOffset=-1;yOffset=-1;zOffset=0;}
		else if (worldObj.getBlock(x-1, y-1, z-1) == NCBlocks.fusionReactor) {xOffset=-1;yOffset=-1;zOffset=-1;}
		else if (worldObj.getBlock(x, y-1, z-1) == NCBlocks.fusionReactor) {xOffset=0;yOffset=-1;zOffset=-1;}
		else if (worldObj.getBlock(x+1, y-1, z-1) == NCBlocks.fusionReactor) {xOffset=1;yOffset=-1;zOffset=-1;}
		else {xOffset=0;yOffset=0;zOffset=0;};
	}
	
	public boolean isNotReady() {
		return (xOffset == 0 && yOffset == 0 && zOffset == 0);
	}
	
	public void getBelow() {
		Block block = worldObj.getBlock(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
		if (block == NCBlocks.fusionReactor) {
			BlockFusionReactor reactor = (BlockFusionReactor)worldObj.getBlock(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
			if (reactor == null || isNotReady()) {getBelow = 0;}
			getBelow = reactor.getBelow(worldObj, xCoord, yCoord, zCoord);
		} else getBelow = 0;
	}
	
	public int[] getAccessibleSlotsFromSide(int side) {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset - 2*getBelow, zCoord + zOffset);
		if (main == null || isNotReady()) {return null;}
		return main.getAccessibleSlotsFromSide(side);
	}
	
	public boolean isItemValidForSlot(int slot, ItemStack s) {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset - 2*getBelow, zCoord + zOffset);
		if (main == null || isNotReady()) {return false;}
		return main.isItemValidForSlot(slot, s);
	}

	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset - 2*getBelow, zCoord + zOffset);
		if (main == null || isNotReady()) {return false;}
		return main.canInsertItem(slot, stack, side);
	}

	public boolean canExtractItem(int slot, ItemStack stack, int slots) {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset - 2*getBelow, zCoord + zOffset);
		if (main == null || isNotReady()) {return false;}
		return main.canExtractItem(slot, stack, slots);
	}

	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}

	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		IEnergyHandler main = (IEnergyHandler)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset - 2*getBelow, zCoord + zOffset);
		if (main == null || isNotReady()) {return 0;}
		return main.receiveEnergy(from, maxReceive, simulate);
	}

	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return 0;
	}

	public int getEnergyStored(ForgeDirection from) {
		return 0;
	}

	public int getMaxEnergyStored(ForgeDirection from) {
		return 0;
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

	 	this.xOffset = nbt.getInteger("xoff");
	  	this.yOffset = nbt.getInteger("yoff");
	  	this.zOffset = nbt.getInteger("zoff");
	  	this.getBelow = nbt.getInteger("getBelow");
	}

	public void writeToNBT(NBTTagCompound nbt) {
	  	super.writeToNBT(nbt);

	  	nbt.setInteger("xoff", this.xOffset);
	 	nbt.setInteger("yoff", this.yOffset);
	   	nbt.setInteger("zoff", this.zOffset);
	   	nbt.setInteger("getBelow", this.getBelow);
	}

	public int getSizeInventory() {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset - 2*getBelow, zCoord + zOffset);
		if (main == null || isNotReady()) {return 0;}
		return slots.length;
	}

	public ItemStack getStackInSlot(int var1) {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset - 2*getBelow, zCoord + zOffset);
		if (main == null || isNotReady()) {return null;}
		return main.getStackInSlot(var1);
	}

	public ItemStack decrStackSize(int var1, int var2) {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset - 2*getBelow, zCoord + zOffset);
		if (main == null || isNotReady()) {return null;}
		return main.decrStackSize(var1, var2);
	}

	public ItemStack getStackInSlotOnClosing(int i) {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset - 2*getBelow, zCoord + zOffset);
		if (main == null || isNotReady()) {return null;}
		return main.getStackInSlotOnClosing(i);
	}

	public void setInventorySlotContents(int i, ItemStack j) {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset - 2*getBelow, zCoord + zOffset);
		if (main == null || isNotReady()) {return;}
		main.setInventorySlotContents(i, j);
	}

	public String getInventoryName() {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset - 2*getBelow, zCoord + zOffset);
		if (main == null || isNotReady()) {return "Fusion Reactor";}
		return main.getInventoryName();
	}

	public boolean hasCustomInventoryName() {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset - 2*getBelow, zCoord + zOffset);
		if (main == null || isNotReady()) {return false;}
		return main.hasCustomInventoryName();
	}

	public int getInventoryStackLimit() {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset - 2*getBelow, zCoord + zOffset);
		if (main == null || isNotReady()) {return 64;}
		return main.getInventoryStackLimit();
	}

	public boolean isUseableByPlayer(EntityPlayer p) {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset - 2*getBelow, zCoord + zOffset);
		if (main == null || isNotReady()) {return false;}
		return main.isUseableByPlayer(p);
	}

	public void openInventory() {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset - 2*getBelow, zCoord + zOffset);
		if (main == null || isNotReady()) {return;}
		main.openInventory();
	}

	public void closeInventory() {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset - 2*getBelow, zCoord + zOffset);
		if (main == null || isNotReady()) {return;}
		main.closeInventory();
	}
}