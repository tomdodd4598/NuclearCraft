package nc.tile.machine;
 
import nc.NuclearCraft;
import nc.block.machine.BlockAssembler;
import nc.crafting.machine.AssemblerRecipes;
import net.minecraft.item.ItemStack;

public class TileAssembler extends TileMachine {
	public static final int[] in = {0, 1, 2, 3, 4};
	
	public TileAssembler() {
		super("Assembler", 250000, 4, 1, true, true, 100, 2000, NuclearCraft.assemblerSpeed, NuclearCraft.assemblerEfficiency, AssemblerRecipes.instance());
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) {
			flag1 = flag;
			BlockAssembler.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}
		markDirty();
	}
	
	public boolean canCook() {
		for (int i = 0; i < inputSize; i++) {
			if (this.slots[i] == null) {
				flag = false;
				return false;
			}
		}
		ItemStack[] output = getOutput(inputs());
		for (int i = 0; i < inputSize; i++) {
			if (recipes != null) {
				if (recipes.getInputSize(i, output) >= this.slots[i].stackSize) { 
					flag = false;
					return false;
				}
			}
		}
		if (this.cookTime >= getFurnaceSpeed) {
			flag = true;
			return true;
		}
		if (getRequiredEnergy > this.energyStorage.getMaxEnergyStored() && cookTime <= 0 && this.energyStorage.getEnergyStored() < this.energyStorage.getMaxEnergyStored() - (int) Math.ceil(getRequiredEnergy/getFurnaceSpeed)) {
			flag = false;
			return false;
		}
		if (hasEnergy) {
			if (this.energyStorage.getEnergyStored() < 1*((int) Math.ceil(getRequiredEnergy/getFurnaceSpeed))) {
				flag = false;
				return false;
			}
			if (this.energyStorage.getEnergyStored() == 0) {
				flag = false;
				return false;
			}
		}
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
	
	public int[] getAccessibleSlotsFromSide(int i) {
		return in;
	}
}