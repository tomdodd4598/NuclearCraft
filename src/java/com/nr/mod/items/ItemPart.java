package com.nr.mod.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.nr.mod.NuclearRelativistics;

public class ItemPart extends Item {

	public ItemPart(String unlocalizedName) {
		super();
		this.setHasSubtypes(true);
		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(NuclearRelativistics.tabNR);
	}

	public IIcon[] icons = new IIcon[6];
	public IIcon[] icons1 = new IIcon[6];
	public IIcon[] icons2 = new IIcon[5];

	public void registerIcons(IIconRegister reg) {
	    for (int i = 0; i < 6; i ++) {
	        this.icons[i] = reg.registerIcon("nr:parts/" + (i));
	    }
	    for (int i = 0; i < 6; i ++) {
	        this.icons1[i] = reg.registerIcon("nr:parts/" + (i+6));
	    }
	    for (int i = 0; i < 5; i ++) {
	        this.icons2[i] = reg.registerIcon("nr:parts/" + (i+12));
	    }
	}

	public IIcon getIconFromDamage(int meta) {
	    if (meta > 16) meta = 0;
	    if (meta >= 0 && meta < 6) { return this.icons[meta]; }
	    else if (meta >= 6 && meta < 12) { return this.icons1[meta-6]; }
	    else if (meta >= 12 && meta < 17) { return this.icons2[meta-12]; }
	    else { return null; }
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getSubItems(Item item, CreativeTabs tab, List list) {
	    for (int i = 0; i < 17; i ++) {
	        list.add(new ItemStack(item, 1, i));
	    }
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
	    default:
	        return this.getUnlocalizedName();
	    }
	}
}