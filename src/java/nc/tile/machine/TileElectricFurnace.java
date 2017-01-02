package nc.tile.machine;
 
import nc.NuclearCraft;
import nc.block.machine.BlockElectricFurnace;
import nc.item.NCItems;
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
import cofh.api.energy.IEnergyReceiver;

public class TileElectricFurnace extends TileInventory implements IEnergyHandler, IEnergyReceiver {
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
	public static final int[] automation = {0, 1};
	public int currentItemBurnTime;
	public double speedUpgrade = 1;
	public double energyUpgrade = 1;
	public double getFurnaceSpeed = Math.ceil(FurnaceSpeed()/speedUpgrade);
	public double getRequiredEnergy = Math.ceil(speedUpgrade*(RequiredEnergy()/energyUpgrade));
	
	public TileElectricFurnace() {
	  energyStorage = new EnergyStorage(250000, 250000);
	  localizedName = "electricFurnace";
	  slots = new ItemStack[4];
	}
	
	public void updateEntity() {
		super.updateEntity();
		upgradeSpeed();
		upgradeEnergy();
		getFurnaceSpeed = Math.ceil(FurnaceSpeed()/speedUpgrade);
		getRequiredEnergy = Math.ceil(speedUpgrade*(RequiredEnergy()/energyUpgrade));
		   
		if(!worldObj.isRemote) {
			canCook();
			if (canCook()) {
				cookTime += 1;
				energyStorage.extractEnergy((int) Math.ceil(getRequiredEnergy/getFurnaceSpeed), false);
				if (cookTime >= getFurnaceSpeed) {
					cookTime = 0;
					cookItem();
				}
			} else {
				cookTime = 0;
			}
	}
		   
		   if (flag != flag1) { flag1 = flag; BlockElectricFurnace.updateBlockState(flag, worldObj, xCoord, yCoord, zCoord); }
			markDirty();
	}
	
	public ItemStack getOutput(ItemStack stack) {
		   return FurnaceRecipes.smelting().getSmeltingResult(stack);
	}
	
	public int getInputSize(ItemStack stack, int slot) {
	    return 1;
	}
	
	public boolean canCook() {
		if (slots[0] == null) {
			flag = false;
			return false;
		}
		if (cookTime >= getFurnaceSpeed) {
			flag = true;
			return true;
		}
		if (getRequiredEnergy > energyStorage.getMaxEnergyStored() && cookTime <= 0 && energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored() - (int) Math.ceil(getRequiredEnergy/getFurnaceSpeed)) {
			flag = false;
			return false;
		}
		if (getRequiredEnergy < energyStorage.getMaxEnergyStored() && cookTime <= 0 && getRequiredEnergy > energyStorage.getEnergyStored()) {
			flag = false;
			return false;
		}
		if (energyStorage.getEnergyStored() < 1*((int) Math.ceil(getRequiredEnergy/getFurnaceSpeed))) {
			flag = false;
			return false;
		}
		if (energyStorage.getEnergyStored() == 0) {
			flag = false;
			return false;
		}
	  
		ItemStack itemstack = getOutput(slots[0]);
		if (itemstack == null) {
			flag = false;
			return false;
		}
		if (slots[1] != null) {
			if (!slots[1].isItemEqual(itemstack)) {
				flag = false;
				return false;
			}
			if (slots[1].stackSize + itemstack.stackSize > slots[1].getMaxStackSize()) {
				flag = false;
				return false;
			}
		}
		flag = true;
		return true;
	}
	
	private void cookItem() {
	  ItemStack itemstack = getOutput(slots[0]);
	  if (slots[1] == null) {
	    slots[1] = itemstack.copy();
	  } else if (slots[1].isItemEqual(itemstack)) {
	    slots[1].stackSize += itemstack.stackSize;
	  }
	  
	  slots[0].stackSize -= getInputSize(slots[0], 0);
	  
	
	  if (slots[0].stackSize <= 0) {
	    slots[0] = null;
	  }
	}
	
	public void upgradeSpeed() {
		ItemStack stack = getStackInSlot(2);
		if (stack != null && isSpeedUpgrade(stack) /*&& speedUpgrade != Math.pow(1.8, stack.stackSize)*/) {
			speedUpgrade = Math.pow(1.8, stack.stackSize);
		} else speedUpgrade = 1;
	}

	public static boolean isSpeedUpgrade(ItemStack stack) {
		return stack.getItem() == NCItems.upgradeSpeed;
	}

	public void upgradeEnergy() {
		ItemStack stack = getStackInSlot(3);
		if (stack != null && isEnergyUpgrade(stack) /*&& energyUpgrade != Math.pow(1.7, stack.stackSize)*/) {
			energyUpgrade = Math.pow(1.7, stack.stackSize);
		} else energyUpgrade = 1;
	}
	
	public static boolean isEnergyUpgrade(ItemStack stack) {
	    return stack.getItem() == NCItems.upgradeEnergy;
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
	  super.readFromNBT(nbt);
	  if (nbt.hasKey("energyStorage")) {
	    energyStorage.readFromNBT(nbt.getCompoundTag("energyStorage"));
	    speedUpgrade = nbt.getDouble("sU");
	    energyUpgrade = nbt.getDouble("eU");
	    getFurnaceSpeed = nbt.getDouble("s");
	    getRequiredEnergy = nbt.getDouble("e");
	  }
	  
	  NBTTagList list = nbt.getTagList("Items", 10);
	  slots = new ItemStack[getSizeInventory()];
	  for (int i = 0; i < list.tagCount(); i++) {
	    NBTTagCompound compound = list.getCompoundTagAt(i);
	    byte b = compound.getByte("Slot");
	    if ((b >= 0) && (b < slots.length)) {
	      slots[b] = ItemStack.loadItemStackFromNBT(compound);
	    }
	  }
	  
	  cookTime = nbt.getShort("CookTime");
	  top = nbt.getShort("Top");
	  bottom = nbt.getShort("Bottom");
	  side1 = nbt.getShort("Side1");
	  flag = nbt.getBoolean("flag");
	  flag1 = nbt.getBoolean("flag1");
	}
	
	public void readSides(NBTTagCompound nbt) {
	  top = nbt.getShort("Top");
	  bottom = nbt.getShort("Bottom");
	  side1 = nbt.getShort("Side1");
	}
	
	public void readEnergy(NBTTagCompound nbt) {
	  if (nbt.hasKey("energyStorage")) {
	    energyStorage.readFromNBT(nbt.getCompoundTag("energyStorage"));
	  }
	}
	
	
	public void writeToNBT(NBTTagCompound nbt)
	{
	  super.writeToNBT(nbt);
	  
	  NBTTagCompound energyTag = new NBTTagCompound();
	  energyStorage.writeToNBT(energyTag);
	  nbt.setTag("energyStorage", energyTag);
	  NBTTagList list = new NBTTagList();
	  nbt.setBoolean("flag", flag);
	  nbt.setBoolean("flag1", flag1);
	  nbt.setDouble("sU", speedUpgrade);
	  nbt.setDouble("eU", energyUpgrade);
	  nbt.setDouble("s", getFurnaceSpeed);
	  nbt.setDouble("e", getRequiredEnergy);
	
	  for (int i = 0; i < slots.length; i++) {
	    if (slots[i] != null) {
	      NBTTagCompound compound = new NBTTagCompound();
	      compound.setByte("Slot", (byte)i);
	      slots[i].writeToNBT(compound);
	      list.appendTag(compound);
	    }
	  }
	  
	  nbt.setShort("CookTime", (short)cookTime);
	  nbt.setShort("Top", (short)top);
	  nbt.setShort("Bottom", (short)bottom);
	  nbt.setShort("Side1", (short)side1);
	  nbt.setTag("Items", list);
	}
	
	
	public void writeSides(NBTTagCompound nbt)
	{
	  nbt.setShort("Top", (short)top);
	  nbt.setShort("Bottom", (short)bottom);
	  nbt.setShort("Side1", (short)side1);
	  update = false;
	}
	
	
	public void writeEnergy(NBTTagCompound nbt)
	{
	  NBTTagCompound energyTag = new NBTTagCompound();
	  energyStorage.writeToNBT(energyTag);
	  nbt.setTag("energyStorage", energyTag);
	}
	
	public Packet getDescriptionPacket()
	{
	  NBTTagCompound nbtTag = new NBTTagCompound();
	
	  nbtTag.setInteger("Energy", energyStorage.getEnergyStored());
	  energy = nbtTag.getInteger("Energy");
	  
	  writeSides(nbtTag);
	  writeEnergy(nbtTag);
	  return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbtTag);
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
	
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
	  return energyStorage.receiveEnergy(maxReceive, simulate);
	}
	
	
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
	  return 0;
	}
	
	
	public int getEnergyStored(ForgeDirection from) {
		return energyStorage.getEnergyStored();
	}
	
	
	public int getMaxEnergyStored(ForgeDirection from) {
		return energyStorage.getMaxEnergyStored();
	}
	
	public int getEnergy() {
		if (energyStorage.getEnergyStored() == 0) return energy;
		return energyStorage.getEnergyStored();
	}
	
	public double FurnaceSpeed() {
		return 100*(100/NuclearCraft.electricFurnaceSmeltSpeed);
	}
	
	public double RequiredEnergy() {
		return 2000*(100/NuclearCraft.electricFurnaceSmeltEfficiency);
	}

	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		return slot == 0;
	}

	public boolean canInsertItem(int slot, ItemStack stack, int j){
		return isItemValidForSlot(slot, stack);
	}

	public boolean canExtractItem(int slot, ItemStack itemstack, int j) {
		return slot == 1;
	}

	public String getName() {
		return getBlockType().getUnlocalizedName();
	}
	
	public int getType() {
		return getBlockMetadata();
	}

	public int[] getAccessibleSlotsFromSide(int var1) {
		return automation;
	}
}