package nc.integration.tconstruct.conarm.trait;

import c4.conarm.lib.traits.AbstractArmorTrait;
import nc.util.PotionHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ArmorTraitUplifting extends AbstractArmorTrait implements IArmorTraitNC {
	
	public ArmorTraitUplifting() {
		super("uplifting", TextFormatting.LIGHT_PURPLE);
	}
	
	@Override
	public float onHurt(ItemStack armor, EntityPlayer player, DamageSource source, float damage, float newDamage, LivingHurtEvent evt) {
		uplift(player);
		return super.onHurt(armor, player, source, damage, newDamage, evt);
	}
	
	private static void uplift(EntityLivingBase player) {
		if (!player.getEntityWorld().isRemote && random.nextInt(2) == 0) {
			player.addPotionEffect(new PotionEffect(PotionHelper.newEffect(10, 3, 81)));
		}
	}
}
