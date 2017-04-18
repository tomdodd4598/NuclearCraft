package nc.item;

import java.util.List;

import nc.NuclearCraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMeta extends Item {
	
	int subItems;
	String folders;
	String name;
	IIcon[][] icons;

	public ItemMeta(String folder, String nam, int number) {
		super();
		subItems = number;
		folders = folder;
		name = nam;
		setHasSubtypes(true);
		
		IIcon[][] icon = new IIcon[(number + 6 - (number % 6))/6][6];
		icons = icon;
	}
	
	@SideOnly(Side.CLIENT)
	public CreativeTabs getCreativeTab() {
		return NuclearCraft.tabNC;
	}
	
	public String getUnlocalizedName(ItemStack stack) {
		return name;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		for (int i = 0; i < (subItems - (subItems % 6))/6; i++) {
		    for (int j = 0; j < 6; j ++) {
		        icons[i][j] = reg.registerIcon("nc:" + folders + (folders == "" ? "" : "/") + (j + 6*i));
		    }
		}
	    for (int j = 0; j < subItems % 6; j ++) {
	        icons[(subItems - (subItems % 6))/6][j] = reg.registerIcon("nc:" + folders + (folders == "" ? "" : "/") + (j + subItems - (subItems % 6)));
	    }
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
	    if (meta >= subItems) meta = 0;
	    if (meta < subItems - (subItems % 6)) {
		    for (int i = 0; i < (subItems - (subItems % 6))/6; i++) {
		    	if (meta >= 6*i && meta < 6 + 6*i) return this.icons[i][meta - 6*i];
		    }
		}
	    if (meta >= subItems - (subItems % 6) && meta < subItems) return this.icons[(subItems - (subItems % 6))/6][meta - (subItems - (subItems % 6))];
	    return this.icons[0][0];
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getSubItems(Item item, CreativeTabs tab, List list) {
	    for (int i = 0; i < subItems; i ++) {
	        list.add(new ItemStack(item, 1, i));
	    }
	}
}
