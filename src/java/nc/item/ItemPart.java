package nc.item;

import java.util.List;

import nc.util.InfoNC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPart extends ItemMeta {

	public ItemPart() {
		super("parts", "parts", 21);
	}
	
	@SuppressWarnings({ "rawtypes" })
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean whatIsThis) {
        super.addInformation(itemStack, player, list, whatIsThis);
        if (info(itemStack.getItemDamage()) == InfoNC.nul); else if (info(itemStack.getItemDamage()).length > 0) InfoNC.infoFull(list, info(itemStack.getItemDamage()));
    }
	
	public String[] info(int m) {
		if (m == 18 || m == 19) return info("Only craftable in the Assembler.");
		else return InfoNC.nul;
	}
	
	public String[] info(String string) {
		String[] inf = {
			string
		};
		return inf;
	}

	public String getUnlocalizedName(ItemStack stack) {
	    switch (stack.getItemDamage()) {
		    case 0: return "simpleNeutronReflector";
		    case 1: return "ironPlating";
		    case 2: return "conGrind";
		    case 3: return "advancedNeutronReflector";
		    case 4: return "universalReactant";
		    case 5: return "reactionVessel";
		    case 6: return "tinPlating";
		    case 7: return "iWater";
		    case 8: return "dUPlating";
		    case 9: return "aPlating";
		    case 10: return "ironGrating";
		    case 11: return "goldContacts";
		    case 12: return "copperWiring";
		    case 13: return "tinTubing";
		    case 14: return "leadPlating";
		    case 15: return "silverPanel";
		    case 16: return "bronzeBearing";
		    case 17: return "MgBWiring";
		    case 18: return "computerPlate";
		    case 19: return "mechanicalPart";
		    case 20: return "plastic";
		    default: return this.getUnlocalizedName();
	    }
	}
}