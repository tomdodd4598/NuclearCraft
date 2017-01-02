package nc.item;

import java.util.List;

import nc.NuclearCraft;
import nc.util.InfoNC;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class NCHoe extends ItemHoe {
	
	String[] info;
	String name;
	
	public NCHoe(ToolMaterial material, String nam, String... lines) {
		super(material);
		String[] strings = new String[lines.length];
		for (int i = 0; i < lines.length; i++) {
			strings[i] = lines[i];
		}
		info = strings;
		name = nam;
		setUnlocalizedName(nam);
	}
	
	@SideOnly(Side.CLIENT)
	public CreativeTabs getCreativeTab() {
		return NuclearCraft.tabNC;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("nc:" + "tools/" + name);
	}
	
	@SuppressWarnings({ "rawtypes" })
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean whatIsThis) {
        super.addInformation(itemStack, player, list, whatIsThis);
        InfoNC.infoFull(list, info);
    }
}