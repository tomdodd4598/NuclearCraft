package nc.item.armour;

import nc.NuclearCraft;
import nc.item.NCItems;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ToughArmour extends ItemArmor {
	
	String[] info;
	String name;

	public ToughArmour(ArmorMaterial material, int id, int slot, String nam, String... lines) {
		super(material, id, slot);
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
		itemIcon = iconRegister.registerIcon("nc:" + "armour/" + name);
	}
	
	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, String type) {
		if (itemstack.getItem() == NCItems.toughHelm || itemstack.getItem() == NCItems.toughChest || itemstack.getItem() == NCItems.toughBoots) {
			return "nc:textures/model/" + "tough" + "1.png";
		} else if (itemstack.getItem() == NCItems.toughLegs) {
			return "nc:textures/model/" + "tough" + "2.png";
		} else {
			return null;
		}
	}
}
