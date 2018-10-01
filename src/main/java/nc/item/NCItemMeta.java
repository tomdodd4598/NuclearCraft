package nc.item;

import java.util.List;

import javax.annotation.Nullable;

import nc.Global;
import nc.enumm.IItemMeta;
import nc.init.NCItems;
import nc.util.InfoHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NCItemMeta<T extends Enum<T> & IStringSerializable & IItemMeta> extends Item {
	
	public final T[] values;
	public final String[][] info;
	
	public NCItemMeta(String nameIn, Class<T> enumm, String[]... tooltips) {
		setUnlocalizedName(Global.MOD_ID + "." + nameIn);
		setRegistryName(new ResourceLocation(Global.MOD_ID, nameIn));
		setHasSubtypes(true);
		values = enumm.getEnumConstants();
		info = InfoHelper.buildInfo(getUnlocalizedName(), enumm, tooltips);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (isInCreativeTab(tab)) for (int i = 0; i < values.length; i++) {
			items.add(new ItemStack(this, 1, i));
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		for (int i = 0; i < values.length; i++) {
			if (stack.getItemDamage() == i) return getUnlocalizedName() + "." + values[i].getName();
			else continue;
		}
		return getUnlocalizedName() + "." + values[0].getName();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(itemStack, world, tooltip, flag);
		int meta = itemStack.getMetadata();
		if (info.length != 0 && info.length > meta) if (info[meta].length > 0) {
			InfoHelper.infoFull(tooltip, info[meta]);
		}
	}
	
	// Allow upgrades to be right-clicked into machines
	@Override
	public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return stack.getItem() == NCItems.upgrade;
	}
}
