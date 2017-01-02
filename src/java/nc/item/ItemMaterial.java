package nc.item;

import java.util.List;

import nc.util.InfoNC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMaterial extends ItemMeta {
	
	public ItemMaterial() {
		super("materials", "materials", 128);
	}
	
	@SuppressWarnings({ "rawtypes" })
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean whatIsThis) {
        super.addInformation(itemStack, player, list, whatIsThis);
        if (info(itemStack.getItemDamage()) == InfoNC.nul); else if (info(itemStack.getItemDamage()).length > 0) InfoNC.infoFull(list, info(itemStack.getItemDamage()));
    }
	
	public String[] info(int m) {
		if (m == 73) return info("Rarely drops from Redstone Ore.");
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
		    case 0: return "copperIngot";
		    case 1: return "tinIngot";
		    case 2: return "leadIngot";
		    case 3: return "silverIngot";
		    case 4: return "uraniumIngot";
		    case 5: return "thoriumIngot";
		    case 6: return "bronzeIngot";
		    case 7: return "toughAlloy";
		    case 8: return "crushedIron";
		    case 9: return "crushedGold";
		    case 10: return "crushedLapis";
		    case 11: return "crushedDiamond";
		    case 12: return "crushedEmerald";
		    case 13: return "crushedQuartz";
		    case 14: return "crushedCoal";
		    case 15: return "crushedCopper";
		    case 16: return "crushedTin";
		    case 17: return "crushedLead";
		    case 18: return "crushedSilver";
		    case 19: return "crushedUranium";
		    case 20: return "crushedThorium";
		    case 21: return "crushedBronze";
		    case 22: return "toughDust";
		    case 23: return "tinyCrushedLead";
		    case 24: return "U238";
		    case 25: return "tinyU238";
		    case 26: return "U235";
		    case 27: return "tinyU235";
		    case 28: return "U233";
		    case 29: return "tinyU233";
		    case 30: return "Pu238";
		    case 31: return "tinyPu238";
		    case 32: return "Pu239";
		    case 33: return "tinyPu239";
		    case 34: return "Pu242";
		    case 35: return "tinyPu242";
		    case 36: return "Pu241";
		    case 37: return "tinyPu241";
		    case 38: return "Th232";
		    case 39: return "tinyTh232";
		    case 40: return "Th230";
		    case 41: return "tinyTh230";
		    case 42: return "lithiumIngot";
		    case 43: return "boronIngot";
		    case 44: return "crushedLithium";
		    case 45: return "crushedBoron";
		    case 46: return "Li6";
		    case 47: return "Li7";
		    case 48: return "B10";
		    case 49: return "B11";
		    case 50: return "magnesiumIngot";
		    case 51: return "crushedMagnesium";
		    case 52: return "crushedObsidian";
		    case 53: return "uraniumOxideIngot";
		    case 54: return "crushedUraniumOxide";
		    case 55: return "U238Oxide";
		    case 56: return "tinyU238Oxide";
		    case 57: return "U235Oxide";
		    case 58: return "tinyU235Oxide";
		    case 59: return "U233Oxide";
		    case 60: return "tinyU233Oxide";
		    case 61: return "Pu238Oxide";
		    case 62: return "tinyPu238Oxide";
		    case 63: return "Pu239Oxide";
		    case 64: return "tinyPu239Oxide";
		    case 65: return "Pu242Oxide";
		    case 66: return "tinyPu242Oxide";
		    case 67: return "Pu241Oxide";
		    case 68: return "tinyPu241Oxide";
		    case 69: return "tinyLi6";
		    case 70: return "tinyB10";
		    case 71: return "MgBIngot";
		    case 72: return "crushedMgB";
		    case 73: return "rhodochrosite";
		    case 74: return "crushedMnO";
		    case 75: return "crushedMnO2";
		    case 76: return "graphiteIngot";
		    case 77: return "crushedGraphite";
		    case 78: return "hardCarbonIngot";
		    case 79: return "crushedHardCarbon";
		    case 80: return "LiMnO2Ingot";
		    case 81: return "crushedLiMnO2";
		    case 82: return "Th232Oxide";
		    case 83: return "tinyTh232Oxide";
		    case 84: return "Th230Oxide";
		    case 85: return "tinyTh230Oxide";
		    case 86: return "Np236";
		    case 87: return "tinyNp236";
		    case 88: return "Np237";
		    case 89: return "tinyNp237";
		    case 90: return "Am241";
		    case 91: return "tinyAm241";
		    case 92: return "Am242";
		    case 93: return "tinyAm242";
		    case 94: return "Am243";
		    case 95: return "tinyAm243";
		    case 96: return "Cm243";
		    case 97: return "tinyCm243";
		    case 98: return "Cm245";
		    case 99: return "tinyCm245";
		    case 100: return "Cm246";
		    case 101: return "tinyCm246";
		    case 102: return "Cm247";
		    case 103: return "tinyCm247";
		    case 104: return "Np236Oxide";
		    case 105: return "tinyNp236Oxide";
		    case 106: return "Np237Oxide";
		    case 107: return "tinyNp237Oxide";
		    case 108: return "Am241Oxide";
		    case 109: return "tinyAm241Oxide";
		    case 110: return "Am242Oxide";
		    case 111: return "tinyAm242Oxide";
		    case 112: return "Am243Oxide";
		    case 113: return "tinyAm243Oxide";
		    case 114: return "Cm243Oxide";
		    case 115: return "tinyCm243Oxide";
		    case 116: return "Cm245Oxide";
		    case 117: return "tinyCm245Oxide";
		    case 118: return "Cm246Oxide";
		    case 119: return "tinyCm246Oxide";
		    case 120: return "Cm247Oxide";
		    case 121: return "tinyCm247Oxide";
		    case 122: return "Cf250";
		    case 123: return "tinyCf250";
		    case 124: return "Cf250Oxide";
		    case 125: return "tinyCf250Oxide";
		    case 126: return "thoriumOxideIngot";
		    case 127: return "crushedThoriumOxide";
		    default: return this.getUnlocalizedName();
	    }
	}
}