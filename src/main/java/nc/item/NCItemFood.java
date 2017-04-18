package nc.item;

import java.util.List;

import nc.Global;
import nc.util.NCInfo;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class NCItemFood extends ItemFood {
	
	private PotionEffect[] effects;
	public final String[] info;

	public NCItemFood(String unlocalizedName, String registryName, int amount, boolean isWolfFood, PotionEffect[] potionEffects, Object... tooltip) {
		super(amount, isWolfFood);
		setUnlocalizedName(unlocalizedName);
		setRegistryName(new ResourceLocation(Global.MOD_ID, registryName));
		effects = potionEffects;
		
		if (tooltip.length == 0) {
			String[] strings = {};
			info = strings;
		} else if (tooltip[0] instanceof String) {
			String[] strings = new String[tooltip.length];
			for (int i = 0; i < tooltip.length; i++) {
				strings[i] = (String) tooltip[i];
			}
			info = strings;
		} else if (tooltip[0] instanceof Integer) {
			String[] strings = new String[(int) tooltip[0]];
			for (int i = 0; i < (int) tooltip[0]; i++) {
				strings[i] = I18n.translateToLocalFormatted("item." + unlocalizedName + ".des" + i);
			}
			info = strings;
		} else {
			String[] strings = {};
			info = strings;
		}
	}
	
	public NCItemFood(String unlocalizedName, String registryName, int amount, float saturation, boolean isWolfFood, PotionEffect[] potionEffects, Object... tooltip) {
		super(amount, saturation, isWolfFood);
		setUnlocalizedName(unlocalizedName);
		setRegistryName(new ResourceLocation(Global.MOD_ID, registryName));
		effects = potionEffects;
		
		if (tooltip.length == 0) {
			String[] strings = {};
			info = strings;
		} else if (tooltip[0] instanceof String) {
			String[] strings = new String[tooltip.length];
			for (int i = 0; i < tooltip.length; i++) {
				strings[i] = (String) tooltip[i];
			}
			info = strings;
		} else if (tooltip[0] instanceof Integer) {
			String[] strings = new String[(int) tooltip[0]];
			for (int i = 0; i < (int) tooltip[0]; i++) {
				strings[i] = I18n.translateToLocalFormatted("item." + unlocalizedName + ".des" + i);
			}
			info = strings;
		} else {
			String[] strings = {};
			info = strings;
		}
	}

	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		for(PotionEffect effect : effects) {
			player.addPotionEffect(effect);
		}
	}
	
	public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        super.addInformation(itemStack, player, tooltip, advanced);
        if (info.length > 0) NCInfo.infoFull(tooltip, info);
    }
}
