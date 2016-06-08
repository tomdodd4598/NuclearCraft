package nc.tile.machine;
 
import nc.NuclearCraft;
import nc.block.machine.BlockIrradiator;
import nc.crafting.machine.IrradiatorRecipes;
import nc.item.NCItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TileIrradiator extends TileMachine {
	public static final int[] input = {0, 1, 2, 3, 4};
	public static final int[] output = {0, 1, 2, 3, 4};
	
	public TileIrradiator() {
		super("Irradiator", 250000, 2, 3, true, true, 2000, 200000, NuclearCraft.irradiatorSpeed, NuclearCraft.irradiatorEfficiency, IrradiatorRecipes.instance());
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) {
			flag1 = flag;
			BlockIrradiator.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}
		markDirty();
	}
	
	public boolean isNeutronCapsule(ItemStack stack) {
		if (stack == null) {return false;} else { Item i = stack.getItem();
		if(i == new ItemStack (NCItems.fuel, 1, 47).getItem() && i.getDamage(stack) == 47) {return true;}}
		return false;
	}
	
	public boolean isIrradiator() {
		return true;
	}
	
	public int[] getAccessibleSlotsFromSide(int i) {
		return i == 1 ? input : output;
	}
}