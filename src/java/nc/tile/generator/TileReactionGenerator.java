package nc.tile.generator;

import nc.NuclearCraft;
import nc.block.generator.BlockReactionGenerator;
import nc.item.NCItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileReactionGenerator extends TileGeneratorInventory {
	public int reactantlevel;
	public int fuellevel;
	public static int power = NuclearCraft.reactionGeneratorRF;
	public int reactantMax = 500000;
	public int requiredReactant = 500/NuclearCraft.reactionGeneratorEfficiency;
	public int lastE;
	public int E;
	public int fuelMax = 500000;
	public int requiredFuel = 500/NuclearCraft.reactionGeneratorEfficiency;
	private static final int[] slots2 = new int[] {0, 1};

	public TileReactionGenerator() {
		super("reactionGenerator", 100000, 2);
	}

	public void updateEntity() {
		super.updateEntity();
		if(!this.worldObj.isRemote) {
			reactant();
			fuel();
			energy();
			addEnergy();
		}
		if (flag != flag1) { flag1 = flag; BlockReactionGenerator.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord); }
		markDirty();
	}

	private void energy() {
		lastE = storage.getEnergyStored();
		if (this.fuellevel >= this.requiredFuel && this.storage.getEnergyStored() <= this.storage.getMaxEnergyStored() - TileReactionGenerator.power && this.reactantlevel >= this.requiredReactant) {
			this.storage.receiveEnergy(TileReactionGenerator.power, false);
			this.reactantlevel -= this.requiredReactant;
			this.fuellevel -= 2*this.requiredFuel;
			flag = true;
		}
		else { flag = false; }
		E = storage.getEnergyStored();
		
		if (E != lastE) { BlockReactionGenerator.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord); }
	}

	private void reactant() {
		ItemStack stack = this.getStackInSlot(1);

		if (stack != null && isReactant(stack) && this.reactantlevel + reactantValue(stack) <= this.reactantMax) {
			this.addReactant(reactantValue(stack));
			--this.slots[1].stackSize;

			if (this.slots[1].stackSize <= 0) {
				this.slots[1] = null;
			}
		}
	}
	
	private void fuel() {
		ItemStack stack = this.getStackInSlot(0);

		if (stack != null && isFuel(stack) && this.fuellevel + fuelValue(stack) <= this.fuelMax) {
			this.addFuel(fuelValue(stack));
			--this.slots[0].stackSize;

			if (this.slots[0].stackSize <= 0) {
				this.slots[0] = null;
			}
		}
	}

	public static boolean isReactant(ItemStack stack) {
		return reactantValue(stack) > 0;
	}
	
	public static boolean isFuel(ItemStack stack) {
		return fuelValue(stack) > 0;
	}

	public static int reactantValue(ItemStack stack) {
		Item i = stack.getItem();
		if(i == new ItemStack (NCItems.parts, 1, 4).getItem() && i.getDamage(stack) == 4) {
			return 50000;
		} else {
		return 0;
		}
	}
	
	public static int fuelValue(ItemStack stack){
		if (stack == null) return 0; else {
			Item item = stack.getItem();
			
			if(item == new ItemStack(NCItems.material, 1, 4).getItem() && item.getDamage(stack) == 4) return 10000;
		 	else if(item == new ItemStack(NCItems.material, 1, 5).getItem() && item.getDamage(stack) == 5) return 10000;
		 	else if(item == new ItemStack(NCItems.material, 1, 19).getItem() && item.getDamage(stack) == 19) return 10000;
		 	else if(item == new ItemStack(NCItems.material, 1, 20).getItem() && item.getDamage(stack) == 20) return 10000;
		 	else if(item == new ItemStack(NCItems.material, 1, 53).getItem() && item.getDamage(stack) == 53) return 15000;
		 	else if(item == new ItemStack(NCItems.material, 1, 54).getItem() && item.getDamage(stack) == 54) return 15000;
		 	else if(item == new ItemStack(NCItems.material, 1, 126).getItem() && item.getDamage(stack) == 126) return 15000;
		 	else if(item == new ItemStack(NCItems.material, 1, 127).getItem() && item.getDamage(stack) == 127) return 15000;
			
		 	else return 0;
		}
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("storage")) {
			this.storage.readFromNBT(nbt.getCompoundTag("storage"));
		}
		this.reactantlevel = nbt.getInteger("Reactant");
		this.fuellevel = nbt.getInteger("Fuel");
		this.lastE = nbt.getInteger("lE");
		this.E = nbt.getInteger("E");
	}

	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagCompound energyTag = new NBTTagCompound();
		this.storage.writeToNBT(energyTag);
		nbt.setTag("storage", energyTag);
		nbt.setInteger("Reactant", this.reactantlevel);
		nbt.setInteger("Fuel", this.fuellevel);
		nbt.setInteger("lE", this.lastE);
		nbt.setInteger("E", this.E);
	}
	
	public void addReactant(int add) {
		this.reactantlevel += add;
	}
	
	public void addFuel(int add) {
		this.fuellevel += add;
	}

	public void removeReactant(int remove) {
		this.reactantlevel -= remove;
	}
	
	public void removeFuel(int remove) {
		this.fuellevel -= remove;
	}

	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (slot == 0) return isFuel(stack);
		else if (slot == 1) return isReactant(stack);
		else return false;
	}
	
	public int[] getAccessibleSlotsFromSide(int slot) {
		return slots2;
	}

	public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
		return false;
	}
}
