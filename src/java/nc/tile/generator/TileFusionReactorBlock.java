package nc.tile.generator;

import java.util.Random;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasHandler;
import mekanism.api.gas.ITubeConnection;
import nc.NuclearCraft;
import nc.block.NCBlocks;
import nc.handler.BombType;
import nc.handler.EntityBomb;
import nc.handler.NCExplosion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;

public class TileFusionReactorBlock extends TileEntity implements IEnergyHandler, IEnergyConnection, IEnergyReceiver, IEnergyProvider, ISidedInventory, IGasHandler, ITubeConnection {

	public int xOffset;
	public int yOffset;
	public int zOffset;
	private Random rand = new Random();
	
	//private static final int[] slotsBottom = new int[] {0, 1};
	
	public ItemStack[] slots = new ItemStack[2];

	public TileFusionReactorBlock() {
		super();
    }
	
	public boolean ppp(int x, int y, int z) {
		return this.worldObj.getBlock(x, y, z) == NCBlocks.blockFusionPlasma;
	}
	
	public void updateEntity() {
    	super.updateEntity();
    	getOffsets(xCoord, yCoord, zCoord);
    	if (this.worldObj.getBlock(xCoord, yCoord, zCoord) == NCBlocks.fusionReactorBlock && (ppp(xCoord + 1, yCoord, zCoord) || ppp(xCoord - 1, yCoord, zCoord) || ppp(xCoord, yCoord + 1, zCoord) || ppp(xCoord, yCoord - 1, zCoord) || ppp(xCoord, yCoord, zCoord + 1) || ppp(xCoord, yCoord, zCoord - 1))) {
			if (rand.nextFloat() > 0.9975) NCExplosion.createExplosion(new EntityBomb(worldObj).setType(BombType.BOMB_STANDARD), worldObj, (double)this.xCoord, (double)this.yCoord, (double)this.zCoord, NuclearCraft.fusionMeltdowns ? 12.5F : 0F, 20F, true);
		}
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
	
	public int[] getAccessibleSlotsFromSide(int side) {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
		if (main == null || isNotReady()) {return null;}
		return main.getAccessibleSlotsFromSide(side);
	}
	
	public boolean isItemValidForSlot(int slot, ItemStack s) {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
		if (main == null || isNotReady()) {return false;}
		return main.isItemValidForSlot(slot, s);
	}

	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
		if (main == null || isNotReady()) {return false;}
		return main.canInsertItem(slot, stack, side);
	}

	public boolean canExtractItem(int slot, ItemStack stack, int slots) {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
		if (main == null || isNotReady()) {return false;}
		return main.canExtractItem(slot, stack, slots);
	}

	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}

	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		IEnergyHandler main = (IEnergyHandler)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
		if (main == null || isNotReady()) {return 0;}
		return main.receiveEnergy(from, maxReceive, simulate);
	}

	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		IEnergyHandler main = (IEnergyHandler)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
		if (main == null || isNotReady()) {return 0;}
		return main.extractEnergy(from, maxExtract, simulate);
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
	}

	public void writeToNBT(NBTTagCompound nbt) {
	  	super.writeToNBT(nbt);

	  	nbt.setInteger("xoff", this.xOffset);
	 	nbt.setInteger("yoff", this.yOffset);
	   	nbt.setInteger("zoff", this.zOffset);
	}

	public int getSizeInventory() {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
		if (main == null || isNotReady()) {return 0;}
		return slots.length;
	}

	public ItemStack getStackInSlot(int var1) {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
		if (main == null || isNotReady()) {return null;}
		return main.getStackInSlot(var1);
	}

	public ItemStack decrStackSize(int var1, int var2) {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
		if (main == null || isNotReady()) {return null;}
		return main.decrStackSize(var1, var2);
	}

	public ItemStack getStackInSlotOnClosing(int i) {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
		if (main == null || isNotReady()) {return null;}
		return main.getStackInSlotOnClosing(i);
	}

	public void setInventorySlotContents(int i, ItemStack j) {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
		if (main == null || isNotReady()) {return;}
		main.setInventorySlotContents(i, j);
	}

	public String getInventoryName() {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
		if (main == null || isNotReady()) {return "Fusion Reactor";}
		return main.getInventoryName();
	}

	public boolean hasCustomInventoryName() {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
		if (main == null || isNotReady()) {return false;}
		return main.hasCustomInventoryName();
	}

	public int getInventoryStackLimit() {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
		if (main == null || isNotReady()) {return 64;}
		return main.getInventoryStackLimit();
	}

	public boolean isUseableByPlayer(EntityPlayer p) {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
		if (main == null || isNotReady()) {return false;}
		return main.isUseableByPlayer(p);
	}

	public void openInventory() {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
		if (main == null || isNotReady()) {return;}
		main.openInventory();
	}

	public void closeInventory() {
		ISidedInventory main = (ISidedInventory)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
		if (main == null || isNotReady()) {return;}
		main.closeInventory();
	}

	@Override
	public int receiveGas(ForgeDirection side, GasStack stack, boolean doTransfer) {
		IGasHandler main = (IGasHandler)worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
		if (main == null || isNotReady()) {return 0;}
		return main.receiveGas(side, stack, doTransfer);
	}

	@Deprecated
	public int receiveGas(ForgeDirection side, GasStack stack) {
		return 0;
	}

	public GasStack drawGas(ForgeDirection side, int amount, boolean doTransfer) {
		return null;
	}

	@Deprecated
	public GasStack drawGas(ForgeDirection side, int amount) {
		return null;
	}

	public boolean canReceiveGas(ForgeDirection side, Gas type) {
		return (type == GasRegistry.getGas("hydrogen") || type == GasRegistry.getGas("deuterium") || type == GasRegistry.getGas("tritium") || type == GasRegistry.getGas("lithium"));
	}

	public boolean canDrawGas(ForgeDirection side, Gas type) {
		return false;
	}
	
	public boolean canTubeConnect(ForgeDirection side) {
		return true;
	}
}