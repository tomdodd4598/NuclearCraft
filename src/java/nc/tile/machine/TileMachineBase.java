package nc.tile.machine;
 
import nc.crafting.NCRecipeHelper;
import nc.item.NCItems;
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

public abstract class TileMachineBase extends TileInventory implements IEnergyHandler, IEnergyReceiver {
	public double processTime;
	public double energyRequired;
	public int inputSize;
	public int outputSize;
	public int speedMod = 100;
	public int timeMod = 100;
	public String localizedName;
    public EnergyStorage energyStorage;
	public boolean flag;
	public boolean flag1 = false;
	public boolean hasUpgrades;
	public boolean hasEnergy;
	public NCRecipeHelper recipes;
	public int top;
	public int bottom;
	public int side1;
	public int recipe;
	public int energy;
	public int cookTime = 0;
	public boolean update;
	public int[] automation;
	public int currentItemBurnTime;
	public double speedUpgrade = 1;
	public double energyUpgrade = 1;
	public double getProcessTime = Math.ceil(ProcessTime()/speedUpgrade);
	public double getEnergyRequired = Math.ceil(speedUpgrade*(EnergyRequired()/energyUpgrade));

	public TileMachineBase(String localName, int energyMax, int inSize, int outSize, boolean usesUpgrades, boolean usesEnergy, double pt, double er, int smod, int tmod, NCRecipeHelper ncrecipes) {
		energyStorage = new EnergyStorage(energyMax, energyMax);
		localizedName = localName;
		inputSize = inSize;
		outputSize = outSize;
		hasUpgrades = usesUpgrades;
		hasEnergy = usesEnergy;
		slots = new ItemStack[inSize + outSize + (usesUpgrades ? 2 : 0)];
		processTime = pt;
		energyRequired = er;
		speedMod = smod;
		timeMod = tmod;
		recipes = ncrecipes;
		
		int[] a = new int[inSize + outSize];
		for (int i = 0; i < a.length; i++) {
			a[i] = i;
		}
		automation = a;
	}

	public void updateEntity() {
		super.updateEntity();
		upgradeSpeed();
		upgradeEnergy();
		if (hasUpgrades) {
			getProcessTime = Math.ceil(ProcessTime()/speedUpgrade);
		} else {
			getProcessTime = ProcessTime();
		}
		if (hasUpgrades) {
			getEnergyRequired = Math.ceil(speedUpgrade*(EnergyRequired()/energyUpgrade));
		} else {
			getEnergyRequired = EnergyRequired();
		}
		if(!this.worldObj.isRemote) {
			canCook();
			if (canCook()) {
				this.cookTime += 1;
				this.energyStorage.extractEnergy((int) Math.ceil(getEnergyRequired/getProcessTime), false);
				if (this.cookTime >= getProcessTime) {
					this.cookTime = 0;
					cookItem();
				}
			} else {
				this.cookTime = 0;
			}
		}
	}
	
	public boolean isOxygen(ItemStack stack) {
		return false;
	}
	
	public boolean isOxidiser() {
		return false;
	}
	
	public boolean isNeutronCapsule(ItemStack stack) {
		return false;
	}
	
	public boolean isIrradiator() {
		return false;
	}
	
	public boolean isHydrogen(ItemStack stack) {
		return false;
	}
	
	public boolean isIoniser() {
		return false;
	}
	
	public ItemStack[] inputs() {
		ItemStack[] input = new ItemStack[inputSize];
		for (int i = 0; i < inputSize; i++) {
			input[i] = slots[i];
		}
		return input;
	}

	public boolean canCook() {
		for (int i = 0; i < inputSize; i++) {
			if (this.slots[i] == null) {
				flag = false;
				return false;
			}
		}
		if (this.cookTime >= getProcessTime) {
			flag = true;
			return true;
		}
		if (getEnergyRequired > this.energyStorage.getMaxEnergyStored() && cookTime <= 0 && this.energyStorage.getEnergyStored() < this.energyStorage.getMaxEnergyStored() - (int) Math.ceil(getEnergyRequired/getProcessTime)) {
			flag = false;
			return false;
		}
		if (getEnergyRequired < this.energyStorage.getMaxEnergyStored() && cookTime <= 0 && getEnergyRequired > this.energyStorage.getEnergyStored()) {
			flag = false;
			return false;
		}
		if (hasEnergy) {
			if (this.energyStorage.getEnergyStored() < 1*((int) Math.ceil(getEnergyRequired/getProcessTime))) {
				flag = false;
				return false;
			}
			if (this.energyStorage.getEnergyStored() == 0) {
				flag = false;
				return false;
			}
		}
		ItemStack[] output = getOutput(inputs());
		if (output == null || output.length != outputSize) {
			return false;
		}
		for(int j = 0; j < outputSize; j++) {
			if (output[j] == null) {
				flag = false;
				return false;
			} else {
				if (this.slots[j + inputSize] != null) {
					if (!this.slots[j + inputSize].isItemEqual(output[j])) {
						flag = false;
						return false;
					} else if (this.slots[j + inputSize].stackSize + output[j].stackSize > this.slots[j + inputSize].getMaxStackSize()) {
						flag = false;
						return false;
					}
				}
			}
		}
		flag = true;
		return true;
	}
	
	public ItemStack[] getOutput(ItemStack... itemstacks) {
		return recipes.getOutput(itemstacks);
	}

	public double ProcessTime() {
		return processTime*(100D/speedMod);
	}

	public double EnergyRequired() {
		return energyRequired*(100D/timeMod);
	}

	public int getInputSize(ItemStack stack, int slot) {
		ItemStack[] outputs = recipes.getOutput(stack);
		if(outputs != null) {
			return recipes.getInputSize(slot, outputs);
		}
		return 1;
	}

	public void cookItem() {
		ItemStack[] output = getOutput(inputs());
		for (int j = 0; j < outputSize; j++) {
			if (output[j] != null) {
				if (this.slots[j + inputSize] == null) {
					ItemStack outputStack = output[j].copy();
					this.slots[j + inputSize] = outputStack;
				} else if (this.slots[j + inputSize].isItemEqual(output[j])) {
					this.slots[j + inputSize].stackSize += output[j].stackSize;
				}
			}
		}
		for (int i = 0; i < inputSize; i++) {
			if (recipes != null) {
				this.slots[i].stackSize -= recipes.getInputSize(i, output);
			} else {
				this.slots[i].stackSize -= 1;
			}
			if (this.slots[i].stackSize <= 0) {
				this.slots[i] = null;
			}
		}
	}

	public void upgradeSpeed() {
		if (hasUpgrades) {
			ItemStack stack = this.getStackInSlot(inputSize + outputSize);
			if (stack != null && isSpeedUpgrade(stack) /*&& speedUpgrade != Math.pow(1.8, stack.stackSize)*/) {
				speedUpgrade = Math.pow(1.8, stack.stackSize);
			} else speedUpgrade = 1;
		}
	}

	public static boolean isSpeedUpgrade(ItemStack stack) {
		return stack.getItem() == NCItems.upgradeSpeed;
	}

	public void upgradeEnergy() {
		if (hasUpgrades) {
			ItemStack stack = this.getStackInSlot(inputSize + outputSize + 1);
			if (stack != null && isEnergyUpgrade(stack) /*&& energyUpgrade != Math.pow(1.7, stack.stackSize)*/) {
				energyUpgrade = Math.pow(1.7, stack.stackSize);
			} else energyUpgrade = 1;
		}
	}

	public static boolean isEnergyUpgrade(ItemStack stack) {
		return stack.getItem() == NCItems.upgradeEnergy;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("energyStorage")) {
			this.energyStorage.readFromNBT(nbt.getCompoundTag("energyStorage"));
			this.speedUpgrade = nbt.getDouble("sU");
			this.energyUpgrade = nbt.getDouble("eU");
			this.getProcessTime = nbt.getDouble("s");
			this.getEnergyRequired = nbt.getDouble("e");
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
 
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagCompound energyTag = new NBTTagCompound();
		this.energyStorage.writeToNBT(energyTag);
		nbt.setTag("energyStorage", energyTag);
		NBTTagList list = new NBTTagList();
		nbt.setBoolean("flag", this.flag);
		nbt.setBoolean("flag1", this.flag1);
		nbt.setDouble("sU", this.speedUpgrade);
		nbt.setDouble("eU", this.energyUpgrade);
		nbt.setDouble("s", this.getProcessTime);
		nbt.setDouble("e", this.getEnergyRequired);
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

	public void writeSides(NBTTagCompound nbt) {
		nbt.setShort("Top", (short)this.top);
		nbt.setShort("Bottom", (short)this.bottom);
		nbt.setShort("Side1", (short)this.side1);
		this.update = false;
	}

	public void writeEnergy(NBTTagCompound nbt) {
		NBTTagCompound energyTag = new NBTTagCompound();
		this.energyStorage.writeToNBT(energyTag);
		nbt.setTag("energyStorage", energyTag);
	}

	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		nbtTag.setInteger("Energy", this.energyStorage.getEnergyStored());
		this.energy = nbtTag.getInteger("Energy");
		writeSides(nbtTag);
		writeEnergy(nbtTag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbtTag);
	}

	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		readSides(packet.func_148857_g());
		readEnergy(packet.func_148857_g());
	}

	public boolean canConnectEnergy(ForgeDirection from) {
		return hasEnergy ? true : false;
	}

	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		return this.energyStorage.receiveEnergy(maxReceive, simulate);
	}

	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return 0;
	}

	public int getEnergyStored(ForgeDirection from) {
		return this.energyStorage.getEnergyStored();
	}

	public int getMaxEnergyStored(ForgeDirection from) {
		return this.energyStorage.getMaxEnergyStored();
	}

	public int getEnergy() {
		if (this.energyStorage.getEnergyStored() == 0) {
			return this.energy;
		}
		return this.energyStorage.getEnergyStored();
	}

	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		if (isOxidiser()) {
			if (isOxygen(itemstack)) return slot == 1;
			else return slot == 0;
		}
		else if (isIrradiator()) {
			if (isNeutronCapsule(itemstack)) return slot == 1;
			else return slot == 0;
		}
		else if (isIoniser()) {
			if (isHydrogen(itemstack)) return slot == 1;
			else return slot == 0;
		}
		else return slot < inputSize;
	}
	
	public int[] getAccessibleSlotsFromSide(int i) {
		return automation;
	}

	public boolean canInsertItem(int slot, ItemStack itemstack, int side) {
		return isItemValidForSlot(slot, itemstack);
	}

	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return (slot > inputSize-1 && slot < outputSize+inputSize);
	}

	public int getSizeInventory() {
		return this.slots.length;
	}

	public String getName() {
		return this.getBlockType().getUnlocalizedName();
	}

	public int getType() {
		return getBlockMetadata();
	}
}