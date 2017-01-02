package nc.tile.machine;
 
import nc.NuclearCraft;
import nc.block.machine.BlockIoniser;
import nc.crafting.machine.IoniserRecipes;
import nc.item.NCItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TileIoniser extends TileMachineBase {
	
	public TileIoniser() {
		super("ioniser", 250000, 2, 2, true, true, 600, 60000, NuclearCraft.ioniserSpeed, NuclearCraft.ioniserEfficiency, IoniserRecipes.instance());
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) {
			flag1 = flag;
			BlockIoniser.updateBlockState(flag, worldObj, xCoord, yCoord, zCoord);
		}
		markDirty();
	}
	
	public boolean isHydrogen(ItemStack stack) {
		if (stack == null) return false; else {
			Item i = stack.getItem();
			if(i == new ItemStack (NCItems.fuel, 1, 36).getItem() && i.getDamage(stack) == 36) return true;
		}
		return false;
	}
	
	public boolean isIoniser() {
		return true;
	}
}