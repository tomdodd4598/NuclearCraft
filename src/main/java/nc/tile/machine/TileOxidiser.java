package nc.tile.machine;
 
import nc.NuclearCraft;
import nc.block.machine.BlockOxidiser;
import nc.crafting.machine.OxidiserRecipes;
import nc.item.NCItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TileOxidiser extends TileMachineBase {
	public static final int[] input = {0, 1, 2, 3};
	public static final int[] output = {0, 1, 2, 3};
	
	public TileOxidiser() {
		super("oxidiser", 250000, 2, 2, true, true, 600, 60000, NuclearCraft.oxidiserSpeed, NuclearCraft.oxidiserEfficiency, OxidiserRecipes.instance());
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) {
			flag1 = flag;
			BlockOxidiser.updateBlockState(flag, worldObj, xCoord, yCoord, zCoord);
		}
		markDirty();
	}
	
	public boolean isOxygen(ItemStack stack) {
		if (stack == null) return false; else {
			Item i = stack.getItem();
			if(i == new ItemStack (NCItems.fuel, 1, 35).getItem() && i.getDamage(stack) == 35) return true;
		}
		return false;
	}
	
	public boolean isOxidiser() {
		return true;
	}
	
	public int[] getAccessibleSlotsFromSide(int i) {
		return i == 1 ? input : output;
	}
}