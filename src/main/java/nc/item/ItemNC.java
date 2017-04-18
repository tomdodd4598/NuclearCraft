package nc.item;

import java.util.List;

import nc.NuclearCraft;
import nc.util.InfoNC;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemNC extends Item {
	
	String[] info;
	String folders;
	String name;
	boolean tab = true;
	
	public ItemNC(String folder, String nam, String... lines) {
		super();
		String[] strings = new String[lines.length];
		for (int i = 0; i < lines.length; i++) {
			strings[i] = lines[i];
		}
		info = strings;
		folders = folder;
		name = nam;
		setUnlocalizedName(nam);
		tab = true;
	}
	
	public ItemNC(String folder, String nam, boolean tb, String... lines) {
		super();
		String[] strings = new String[lines.length];
		for (int i = 0; i < lines.length; i++) {
			strings[i] = lines[i];
		}
		info = strings;
		folders = folder;
		name = nam;
		setUnlocalizedName(nam);
		if (tb) tab = true; else tab = false;
	}
	
	@SideOnly(Side.CLIENT)
	public CreativeTabs getCreativeTab() {
		return tab ? NuclearCraft.tabNC : null;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("nc:" + folders + (folders == "" ? "" : "/") + name);
	}
	
	@SuppressWarnings({ "rawtypes" })
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean whatIsThis) {
        super.addInformation(itemStack, player, list, whatIsThis);
        if (info.length > 0) InfoNC.infoFull(list, info);
    }
}
