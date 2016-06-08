package nc.tile.machine;
 
import nc.NuclearCraft;
import nc.block.machine.BlockIoniser;
import nc.crafting.machine.IoniserRecipes;
import nc.item.NCItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TileIoniser extends TileMachine {
	public static final int[] input = {0, 1, 2, 3};
	public static final int[] output = {0, 1, 2, 3};
	
	public TileIoniser() {
		super("Ioniser", 250000, 2, 2, true, true, 600, 60000, NuclearCraft.ioniserSpeed, NuclearCraft.ioniserEfficiency, IoniserRecipes.instance());
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) {
			flag1 = flag;
			BlockIoniser.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}
		markDirty();
	}
	
	public boolean isHydrogen(ItemStack stack) {
		if (stack == null) {return false;} else { Item i = stack.getItem();
		if(i == new ItemStack (NCItems.fuel, 1, 36).getItem() && i.getDamage(stack) == 36) {return true;}}
		return false;
	}
	
	public boolean isIoniser() {
		return true;
	}
	
	public int[] getAccessibleSlotsFromSide(int i) {
		return i == 1 ? input : output;
	}
}