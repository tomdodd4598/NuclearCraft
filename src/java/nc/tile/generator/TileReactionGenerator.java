package nc.tile.generator;

import nc.NuclearCraft;
import nc.block.generator.BlockReactionGenerator;
import nc.item.NCItems;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileReactionGenerator extends TileGenerator {
	public int reactantlevel;
	public int fuellevel;
	public static int power = 2*NuclearCraft.reactionGeneratorRF;
	public int reactantMax = 500000;
	public int requiredReactant = 500/NuclearCraft.reactionGeneratorEfficiency;
	public int lastE;
	public int E;
	public int fuelMax = 500000;
	public int requiredFuel = 500/NuclearCraft.reactionGeneratorEfficiency;
	private static final int[] slots2 = new int[] {0, 1};

	public TileReactionGenerator() {
		super("Reaction Generator", 100000, 2);
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
			this.fuellevel -= this.requiredFuel;
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
			return 100000;
		} else {
		return i == Items.nether_wart ? 5000 : (i == Items.blaze_powder ? 12500 : (i == Items.ghast_tear ? 50000 : (i == Items.gunpowder ? 12500 : (i == Items.redstone ? 5000 : 0))));
		}
	}
	
	public static int fuelValue(ItemStack stack){
		if (stack == null) return 0; else {
			Item item = stack.getItem();
			
			if(item == new ItemStack (NCItems.fuel, 1, 0).getItem() && item.getDamage(stack) == 0) return 50000;
			else if(item == new ItemStack (NCItems.fuel, 1, 1).getItem() && item.getDamage(stack) == 1) return 200000;
			else if(item == new ItemStack (NCItems.fuel, 1, 2).getItem() && item.getDamage(stack) == 2) return 50000;
			else if(item == new ItemStack (NCItems.fuel, 1, 3).getItem() && item.getDamage(stack) == 3) return 200000;
			else if(item == new ItemStack (NCItems.fuel, 1, 4).getItem() && item.getDamage(stack) == 4) return 75000;
			else if(item == new ItemStack (NCItems.fuel, 1, 5).getItem() && item.getDamage(stack) == 5) return 50000;
			else if(item == new ItemStack (NCItems.fuel, 1, 6).getItem() && item.getDamage(stack) == 6) return 50000;
			else if(item == new ItemStack (NCItems.fuel, 1, 7).getItem() && item.getDamage(stack) == 7) return 200000;
			else if(item == new ItemStack (NCItems.fuel, 1, 8).getItem() && item.getDamage(stack) == 8) return 50000;
			else if(item == new ItemStack (NCItems.fuel, 1, 9).getItem() && item.getDamage(stack) == 9) return 200000;
			else if(item == new ItemStack (NCItems.fuel, 1, 10).getItem() && item.getDamage(stack) == 10) return 75000;
			else if(item == new ItemStack (NCItems.fuel, 1, 11).getItem() && item.getDamage(stack) == 11) return 50000;
		 	else if(item == new ItemStack (NCItems.fuel, 1, 12).getItem() && item.getDamage(stack) == 12) return 200000;
		 	else if(item == new ItemStack (NCItems.fuel, 1, 13).getItem() && item.getDamage(stack) == 13) return 50000;
		 	else if(item == new ItemStack (NCItems.fuel, 1, 14).getItem() && item.getDamage(stack) == 14) return 200000;
		 	else if(item == new ItemStack (NCItems.fuel, 1, 15).getItem() && item.getDamage(stack) == 15) return 75000;
		 	else if(item == new ItemStack (NCItems.fuel, 1, 16).getItem() && item.getDamage(stack) == 16) return 50000;
		 	else if(item == new ItemStack (NCItems.fuel, 1, 17).getItem() && item.getDamage(stack) == 17) return 50000;
		 	else if(item == new ItemStack (NCItems.fuel, 1, 18).getItem() && item.getDamage(stack) == 18) return 200000;
		 	else if(item == new ItemStack (NCItems.fuel, 1, 19).getItem() && item.getDamage(stack) == 19) return 50000;
		 	else if(item == new ItemStack (NCItems.fuel, 1, 20).getItem() && item.getDamage(stack) == 20) return 200000;
		 	else if(item == new ItemStack (NCItems.fuel, 1, 21).getItem() && item.getDamage(stack) == 21) return 75000;
			
		 	else if(item == new ItemStack(NCItems.material, 1, 4).getItem() && item.getDamage(stack) == 4) return 10000;
		 	else if(item == new ItemStack(NCItems.material, 1, 5).getItem() && item.getDamage(stack) == 5) return 10000;
		 	else if(item == new ItemStack(NCItems.material, 1, 19).getItem() && item.getDamage(stack) == 19) return 10000;
		 	else if(item == new ItemStack(NCItems.material, 1, 20).getItem() && item.getDamage(stack) == 20) return 10000;
			
		 	else if(item == new ItemStack (NCItems.fuel, 1, 51).getItem() && item.getDamage(stack) == 51) return 75000;
		 	else if(item == new ItemStack (NCItems.fuel, 1, 52).getItem() && item.getDamage(stack) == 52) return 300000;
		 	else if(item == new ItemStack (NCItems.fuel, 1, 53).getItem() && item.getDamage(stack) == 53) return 75000;
		 	else if(item == new ItemStack (NCItems.fuel, 1, 54).getItem() && item.getDamage(stack) == 54) return 300000;
		 	else if(item == new ItemStack (NCItems.fuel, 1, 55).getItem() && item.getDamage(stack) == 55) return 75000;
		 	else if(item == new ItemStack (NCItems.fuel, 1, 56).getItem() && item.getDamage(stack) == 56) return 300000;
		 	else if(item == new ItemStack (NCItems.fuel, 1, 57).getItem() && item.getDamage(stack) == 57) return 75000;
		 	else if(item == new ItemStack (NCItems.fuel, 1, 58).getItem() && item.getDamage(stack) == 58) return 300000;
		 	else if(item == new ItemStack (NCItems.fuel, 1, 59).getItem() && item.getDamage(stack) == 59) return 75000;
		 	else if(item == new ItemStack (NCItems.fuel, 1, 60).getItem() && item.getDamage(stack) == 60) return 300000;
		 	else if(item == new ItemStack (NCItems.fuel, 1, 61).getItem() && item.getDamage(stack) == 61) return 75000;
		 	else if(item == new ItemStack (NCItems.fuel, 1, 62).getItem() && item.getDamage(stack) == 62) return 300000;
		 	else if(item == new ItemStack (NCItems.fuel, 1, 63).getItem() && item.getDamage(stack) == 63) return 75000;
		 	else if(item == new ItemStack (NCItems.fuel, 1, 64).getItem() && item.getDamage(stack) == 64) return 300000;
		 	else if(item == new ItemStack (NCItems.fuel, 1, 65).getItem() && item.getDamage(stack) == 65) return 75000;
		 	else if(item == new ItemStack (NCItems.fuel, 1, 66).getItem() && item.getDamage(stack) == 66) return 300000;
			
		 	else if(item == new ItemStack(NCItems.material, 1, 53).getItem() && item.getDamage(stack) == 53) return 15000;
		 	else if(item == new ItemStack(NCItems.material, 1, 54).getItem() && item.getDamage(stack) == 54) return 15000;
			
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
}
