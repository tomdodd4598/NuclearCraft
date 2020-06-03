package nc.item.armor;

import javax.annotation.Nonnull;

import ic2.api.item.IHazmatLike;
import nc.ModCheck;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList({@Optional.Interface(iface = "ic2.api.item.IHazmatLike", modid = "ic2")})
public class ItemHazmatSuit extends NCItemArmor implements ISpecialArmor, IHazmatLike {
	
	public final double radiationProtection;
	
	public ItemHazmatSuit(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn, double radiationProtection, TextFormatting infoColor, String... tooltip) {
		super(materialIn, renderIndexIn, equipmentSlotIn, infoColor, tooltip);
		this.radiationProtection = radiationProtection;
	}
	
	@Override
	public ArmorProperties getProperties(EntityLivingBase player, @Nonnull ItemStack armor, DamageSource source, double damage, int slot) {
		if (source.damageType.equals("radiation") || source.damageType.equals("sulphuric_acid") || source.damageType.equals("acid_burn") || source.damageType.equals("corium_burn") || source.damageType.equals("hot_coolant_burn")) {
			return new ArmorProperties(0, radiationProtection, Integer.MAX_VALUE);
		}
		return new ArmorProperties(0, 0, Integer.MAX_VALUE);
	}
	
	@Override
	public int getArmorDisplay(EntityPlayer player, @Nonnull ItemStack armor, int slot) {
		return 0;
	}
	
	@Override
	public void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {
		if (ModCheck.ic2Loaded()) {
			Potion radiation = Potion.getPotionFromResourceLocation("ic2:radiation");
			if (radiation != null && entity.isPotionActive(radiation)) {
				entity.removePotionEffect(radiation);
			}
		}
	}
	
	@Override
	public boolean handleUnblockableDamage(EntityLivingBase entity, @Nonnull ItemStack armor, DamageSource source, double damage, int slot) {
		return source.damageType.equals("radiation") || source.damageType.equals("sulphuric_acid") || source.damageType.equals("acid_burn") || source.damageType.equals("corium_burn") || source.damageType.equals("hot_coolant_burn");
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public boolean addsProtection(EntityLivingBase entity, EntityEquipmentSlot slot, ItemStack stack) {
		return true;
	}
}
