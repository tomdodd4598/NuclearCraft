package nc.item;

import java.util.List;

import javax.annotation.Nullable;

import nc.Global;
import nc.NuclearCraft;
import nc.util.InfoHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NCItemFood extends ItemFood {
	
	private final PotionEffect[] effects;
	public final String[] info;

	public NCItemFood(String nameIn, int amount, boolean isWolfFood, PotionEffect[] potionEffects, String... tooltip) {
		this(nameIn, amount, 0.6F, isWolfFood, potionEffects, tooltip);
	}
	
	public NCItemFood(String nameIn, int amount, float saturation, PotionEffect[] potionEffects, String... tooltip) {
		this(nameIn, amount, saturation, false, potionEffects, tooltip);
	}
	
	public NCItemFood(String nameIn, int amount, float saturation, boolean isWolfFood, PotionEffect[] potionEffects, String... tooltip) {
		super(amount, saturation, isWolfFood);
		setTranslationKey(Global.MOD_ID + "." + nameIn);
		if (NuclearCraft.regName) setRegistryName(new ResourceLocation(Global.MOD_ID, nameIn));
		effects = potionEffects;
		info = InfoHelper.buildInfo(getTranslationKey(), tooltip);
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		if (effects != null && effects.length > 0) for (PotionEffect effect : effects) {
			player.addPotionEffect(new PotionEffect(effect));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(itemStack, world, tooltip, flag);
		if (info.length > 0) InfoHelper.infoFull(tooltip, info);
	}
}
