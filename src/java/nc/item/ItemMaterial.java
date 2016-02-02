package nc.item;

import java.util.List;

import nc.NuclearCraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemMaterial extends Item {
	
	public ItemMaterial(String unlocalizedName) {
		super();
		this.setHasSubtypes(true);
		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(NuclearCraft.tabNC);
	}

	public IIcon[] icons = new IIcon[6];
	public IIcon[] icons1 = new IIcon[6];
	public IIcon[] icons2 = new IIcon[6];
	public IIcon[] icons3 = new IIcon[6];
	public IIcon[] icons4 = new IIcon[6];
	public IIcon[] icons5 = new IIcon[6];
	public IIcon[] icons6 = new IIcon[6];
	public IIcon[] icons7 = new IIcon[6];
	public IIcon[] icons8 = new IIcon[6];
	public IIcon[] icons9 = new IIcon[6];
	public IIcon[] icons10 = new IIcon[6];
	public IIcon[] icons11 = new IIcon[6];
	public IIcon[] icons12 = new IIcon[1];

	public void registerIcons(IIconRegister reg) {
	    for (int i = 0; i < 6; i ++) {
	        this.icons[i] = reg.registerIcon("nc:materials/" + (i));
	    }
	    for (int i = 0; i < 6; i ++) {
	        this.icons1[i] = reg.registerIcon("nc:materials/" + (i+6));
	    }
	    for (int i = 0; i < 6; i ++) {
	        this.icons2[i] = reg.registerIcon("nc:materials/" + (i+12));
	    }
	    for (int i = 0; i < 6; i ++) {
	        this.icons3[i] = reg.registerIcon("nc:materials/" + (i+18));
	    }
	    for (int i = 0; i < 6; i ++) {
	        this.icons4[i] = reg.registerIcon("nc:materials/" + (i+24));
	    }
	    for (int i = 0; i < 6; i ++) {
	        this.icons5[i] = reg.registerIcon("nc:materials/" + (i+30));
	    }
	    for (int i = 0; i < 6; i ++) {
	        this.icons6[i] = reg.registerIcon("nc:materials/" + (i+36));
	    }
	    for (int i = 0; i < 6; i ++) {
	        this.icons7[i] = reg.registerIcon("nc:materials/" + (i+42));
	    }
	    for (int i = 0; i < 6; i ++) {
	        this.icons8[i] = reg.registerIcon("nc:materials/" + (i+48));
	    }
	    for (int i = 0; i < 6; i ++) {
	        this.icons9[i] = reg.registerIcon("nc:materials/" + (i+54));
	    }
	    for (int i = 0; i < 6; i ++) {
	        this.icons10[i] = reg.registerIcon("nc:materials/" + (i+60));
	    }
	    for (int i = 0; i < 6; i ++) {
	        this.icons11[i] = reg.registerIcon("nc:materials/" + (i+66));
	    }
	    for (int i = 0; i < 1; i ++) {
	        this.icons12[i] = reg.registerIcon("nc:materials/" + (i+72));
	    }
	}
	
	public IIcon getIconFromDamage(int meta) {
	    if (meta > 72) meta = 0;
	    if 		(meta >= 0 && meta < 6) { return this.icons[meta]; }
	    else if (meta >= 6 && meta < 12) { return this.icons1[meta-6]; }
	    else if (meta >= 12 && meta < 18) { return this.icons2[meta-12]; }
	    else if (meta >= 18 && meta < 24) { return this.icons3[meta-18]; }
	    else if (meta >= 24 && meta < 30) { return this.icons4[meta-24]; }
	    else if (meta >= 30 && meta < 36) { return this.icons5[meta-30]; }
	    else if (meta >= 36 && meta < 42) { return this.icons6[meta-36]; }
	    else if (meta >= 42 && meta < 48) { return this.icons7[meta-42]; }
	    else if (meta >= 48 && meta < 54) { return this.icons8[meta-48]; }
	    else if (meta >= 54 && meta < 60) { return this.icons9[meta-54]; }
	    else if (meta >= 60 && meta < 66) { return this.icons10[meta-60];}
	    else if (meta >= 66 && meta < 72) { return this.icons11[meta-66];}
	    else if (meta >= 72 && meta < 73) { return this.icons12[meta-72];}
	    else { return this.icons[0]; }
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getSubItems(Item item, CreativeTabs tab, List list) {
	    for (int i = 0; i < 73; i ++) {
	        list.add(new ItemStack(item, 1, i));
	    }
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
	    default:
	        return this.getUnlocalizedName();
	    }
	}
}