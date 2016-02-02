package nc.tile.machine;
 
import nc.NuclearCraft;
import nc.block.machine.BlockElectricFurnace;
import nc.item.NCItems;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;

public class TileElectricFurnace extends TileInventory implements IEnergyHandler, ISidedInventory {
	public EnergyStorage energyStorage;
	public boolean flag;
	public boolean flag1 = false;
	public int top;
	public int bottom;
	public int side1;
	public int recipe;
	public int energy;
	public int cookTime = 0;
	public boolean update;
	public static final int[] input = {0, 1};
	public static final int[] output = {0, 1};
	public int currentItemBurnTime;
	public int speedUpgrade = 1;
	public int energyUpgrade = 1;
	public double getFurnaceSpeed = (FurnaceSpeed()/speedUpgrade);
	public double getRequiredEnergy = speedUpgrade*(RequiredEnergy()/energyUpgrade);
	
	public TileElectricFurnace()
	{
	  this.energyStorage = new EnergyStorage(20000, 20000);
	  this.localizedName = "Machine Single Output";
	  this.slots = new ItemStack[4];
	}
	
	public void updateEntity()
	{
		   super.updateEntity();
		   upgradeSpeed();
		   upgradeEnergy();
		   getFurnaceSpeed = (FurnaceSpeed()/speedUpgrade);
		   getRequiredEnergy = speedUpgrade*(RequiredEnergy()/energyUpgrade);
		   
		   if(!this.worldObj.isRemote)
		{
			   canCook();
		   if (canCook() && this.energyStorage.getEnergyStored() >= ((int) Math.ceil(getRequiredEnergy/getFurnaceSpeed))) {
		       this.cookTime += 1;
		       this.energyStorage.extractEnergy((int) (getRequiredEnergy/getFurnaceSpeed), false);
		       
		       if (this.cookTime >= getFurnaceSpeed) {
		         this.cookTime = 0;
		         cookItem();
		       }
		     } else {
		       this.cookTime = 0;
		     }
		}
		   
		   if (flag != flag1) { flag1 = flag; BlockElectricFurnace.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord); }
			markDirty();
	}
	
	public ItemStack getOutput(ItemStack stack) {
		   return FurnaceRecipes.smelting().getSmeltingResult(stack);
	}
	
	public int getInputSize(ItemStack stack, int slot) {
	    return 1;
	}
	
	public boolean canCook()
	{
	  if (this.slots[0] == null) {
	 	 flag = false;
	    return false;
	  }
	  if (this.cookTime >= getFurnaceSpeed) {
	 	 flag = true;
	    return true;
	  }
	  if (this.energyStorage.getEnergyStored() == 0) {
	 	 flag = false;
	    return false;
	  }
	  
	  ItemStack itemstack = getOutput(this.slots[0]);
	  if (itemstack == null) {
	 	 flag = false;
	    return false;
	  }
	  if (this.slots[1] != null)
	  {
	    if (!this.slots[1].isItemEqual(itemstack))
	    {
	 	   flag = false;
	      return false;
	    }
	    
	    if (this.slots[1].stackSize + itemstack.stackSize > this.slots[1].getMaxStackSize())
	    {
	 	   flag = false;
	      return false;
	    }
	  }
	  
	  flag = true;
	  return true;
	}
	
	private void cookItem() {
	  ItemStack itemstack = getOutput(this.slots[0]);
	  if (this.slots[1] == null) {
	    this.slots[1] = itemstack.copy();
	  } else if (this.slots[1].isItemEqual(itemstack)) {
	    this.slots[1].stackSize += itemstack.stackSize;
	  }
	  
	  this.slots[0].stackSize -= getInputSize(this.slots[0], 0);
	  
	
	  if (this.slots[0].stackSize <= 0) {
	    this.slots[0] = null;
	  }
	}
	
	public void upgradeSpeed() {
	    ItemStack stack = this.getStackInSlot(2);
	    if (stack != null && isSpeedUpgrade(stack))
	    {
	 	   if (stack.stackSize == 0)      { speedUpgrade = 1; }
	 	   else if (stack.stackSize == 1) { speedUpgrade = 2; }
	 	   else if (stack.stackSize == 2) { speedUpgrade = 3; }
	 	   else if (stack.stackSize == 3) { speedUpgrade = 4; }
	 	   else if (stack.stackSize == 4) { speedUpgrade = 6; }
	 	   else if (stack.stackSize == 5) { speedUpgrade = 8; }
	 	   else if (stack.stackSize == 6) { speedUpgrade = 11;}
	 	   else if (stack.stackSize == 7) { speedUpgrade = 15;}
	 	   else if (stack.stackSize == 8) { speedUpgrade = 20;}
	
	        if (this.slots[2].stackSize <= 0) { speedUpgrade = 1; }}
	    else { speedUpgrade = 1; }
	}
	
	public static boolean isSpeedUpgrade(ItemStack stack) {
	    return stack.getItem() == NCItems.upgradeSpeed;
	}
	
	public void upgradeEnergy() {
	    ItemStack stack = this.getStackInSlot(3);
	    if (stack != null && isEnergyUpgrade(stack))
	    {
	 	   if (stack.stackSize == 0)      { energyUpgrade = 1; }
	 	   else if (stack.stackSize == 1) { energyUpgrade = 2; }
	 	   else if (stack.stackSize == 2) { energyUpgrade = 3; }
	 	   else if (stack.stackSize == 3) { energyUpgrade = 4; }
	 	   else if (stack.stackSize == 4) { energyUpgrade = 6; }
	 	   else if (stack.stackSize == 5) { energyUpgrade = 8; }
	 	   else if (stack.stackSize == 6) { energyUpgrade = 11;}
	 	   else if (stack.stackSize == 7) { energyUpgrade = 15;}
	 	   else if (stack.stackSize == 8) { energyUpgrade = 20;}
	
	        if (this.slots[3].stackSize <= 0) { energyUpgrade = 1; }}
	    else { energyUpgrade = 1; }
	}
	
	public static boolean isEnergyUpgrade(ItemStack stack) {
	    return stack.getItem() == NCItems.upgradeEnergy;
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
	  super.readFromNBT(nbt);
	  if (nbt.hasKey("energyStorage")) {
	    this.energyStorage.readFromNBT(nbt.getCompoundTag("energyStorage"));
	    this.speedUpgrade = nbt.getInteger("sU");
	    this.energyUpgrade = nbt.getInteger("eU");
	    this.getFurnaceSpeed = nbt.getDouble("s");
	    this.getRequiredEnergy = nbt.getDouble("e");
	  }
	  
	  NBTTagList list = nbt.getTagList("Items", 10);
	  this.slots = new ItemStack[getSizeInventory()];
	  for (int i = 0; i < list.tagCount(); i++) {
	    NBTTagCompound compound = list.getCompoundTagAt(i);
	    byte b = compound.getByte("Slot");
	    if ((b >= 0) && (b < this.slots.length)) {
	      this.slots[b] = ItemStack.loadItemStackFromNBT(compound);
	    }
	  }
	  
	  this.cookTime = nbt.getShort("CookTime");
	  this.top = nbt.getShort("Top");
	  this.bottom = nbt.getShort("Bottom");
	  this.side1 = nbt.getShort("Side1");
	  this.flag = nbt.getBoolean("flag");
	  this.flag1 = nbt.getBoolean("flag1");
	}
	
	public void readSides(NBTTagCompound nbt) {
	  this.top = nbt.getShort("Top");
	  this.bottom = nbt.getShort("Bottom");
	  this.side1 = nbt.getShort("Side1");
	}
	
	public void readEnergy(NBTTagCompound nbt) {
	  if (nbt.hasKey("energyStorage")) {
	    this.energyStorage.readFromNBT(nbt.getCompoundTag("energyStorage"));
	  }
	}
	
	
	public void writeToNBT(NBTTagCompound nbt)
	{
	  super.writeToNBT(nbt);
	  
	  NBTTagCompound energyTag = new NBTTagCompound();
	  this.energyStorage.writeToNBT(energyTag);
	  nbt.setTag("energyStorage", energyTag);
	  NBTTagList list = new NBTTagList();
	  nbt.setBoolean("flag", this.flag);
	  nbt.setBoolean("flag1", this.flag1);
	  nbt.setInteger("sU", this.speedUpgrade);
	  nbt.setInteger("eU", this.energyUpgrade);
	  nbt.setDouble("s", this.getFurnaceSpeed);
	  nbt.setDouble("e", this.getRequiredEnergy);
	
	  for (int i = 0; i < this.slots.length; i++) {
	    if (this.slots[i] != null) {
	      NBTTagCompound compound = new NBTTagCompound();
	      compound.setByte("Slot", (byte)i);
	      this.slots[i].writeToNBT(compound);
	      list.appendTag(compound);
	    }
	  }
	  
	  nbt.setShort("CookTime", (short)this.cookTime);
	  nbt.setShort("Top", (short)this.top);
	  nbt.setShort("Bottom", (short)this.bottom);
	  nbt.setShort("Side1", (short)this.side1);
	  nbt.setTag("Items", list);
	}
	
	
	public void writeSides(NBTTagCompound nbt)
	{
	  nbt.setShort("Top", (short)this.top);
	  nbt.setShort("Bottom", (short)this.bottom);
	  nbt.setShort("Side1", (short)this.side1);
	  this.update = false;
	}
	
	
	public void writeEnergy(NBTTagCompound nbt)
	{
	  NBTTagCompound energyTag = new NBTTagCompound();
	  this.energyStorage.writeToNBT(energyTag);
	  nbt.setTag("energyStorage", energyTag);
	}
	
	public Packet getDescriptionPacket()
	{
	  NBTTagCompound nbtTag = new NBTTagCompound();
	
	  nbtTag.setInteger("Energy", this.energyStorage.getEnergyStored());
	  this.energy = nbtTag.getInteger("Energy");
	  
	  writeSides(nbtTag);
	  writeEnergy(nbtTag);
	  return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbtTag);
	}
	
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
	{
		   super.onDataPacket(net, packet);
	  readSides(packet.func_148857_g());
	  readEnergy(packet.func_148857_g());
	}
	
	public boolean canConnectEnergy(ForgeDirection from) {
	  return true;
	}
	
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
	{
	  return this.energyStorage.receiveEnergy(maxReceive, simulate);
	}
	
	
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
	{
	  return 0;
	}
	
	
	public int getEnergyStored(ForgeDirection from)
	{
	  return this.energyStorage.getEnergyStored();
	}
	
	
	public int getMaxEnergyStored(ForgeDirection from)
	{
	  return this.energyStorage.getMaxEnergyStored();
	}
	
	public int getEnergy() {
	  if (this.energyStorage.getEnergyStored() == 0)
	    return this.energy;
	  return this.energyStorage.getEnergyStored();
	}
	
	public double FurnaceSpeed()
	{
		   return 100*(100/NuclearCraft.electricFurnaceSmeltSpeed);
	}
	
	public double RequiredEnergy()
	{
		   return 2000;
	}

	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		return slot == 0;
	}

	public boolean canInsertItem(int slot, ItemStack stack, int j)
	{
		return this.isItemValidForSlot(slot, stack);
	}

	public boolean canExtractItem(int slot, ItemStack itemstack, int j)
	{
		return slot == 1;
	}
	
	public ItemStack getStackInSlot(int i)
	{
		return this.slots[i];
	}
	
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		this.slots[i] = itemstack;
		
		if(itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
		{
			itemstack.stackSize = this.getInventoryStackLimit();
		}
	}
	
	public ItemStack decrStackSize(int i, int j)
	{
		if(this.slots[i] != null)
		{
			ItemStack itemstack;
				
				if(this.slots[i].stackSize <= j)
				{
					itemstack = this.slots[i];
					
					this.slots[i] = null;
					
					return itemstack;
				}
				else
				{
					itemstack = this.slots[i].splitStack(j);
					
					if(this.slots[i].stackSize == 0)
					{
						this.slots[i] = null;
					}
					
					return itemstack;
					
				}
		}
		else
     {
         return null;
     }
	}

	public ItemStack getStackInSlotOnClosing(int i)
	{
		if(this.slots[i] != null)
		{
			ItemStack itemstack = this.slots[i];
			this.slots[i] = null;
			return itemstack;
		}
		else
     {
         return null;
     }
	}
	
	public int getSizeInventory()
	{
		return this.slots.length;
	}

	public String getName() {
		return this.getBlockType().getUnlocalizedName();
	}
	
	public int getType() {
		return getBlockMetadata();
	}

	public int[] getAccessibleSlotsFromSide(int var1)
	{
		return var1 == 0 ? output : (var1 == 1 ? input : output);
	}
}