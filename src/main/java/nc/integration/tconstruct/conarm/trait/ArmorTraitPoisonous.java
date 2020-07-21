package nc.integration.tconstruct.conarm.trait;

import c4.conarm.lib.traits.AbstractArmorTrait;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ArmorTraitPoisonous extends AbstractArmorTrait implements IArmorTraitNC {
	
	public ArmorTraitPoisonous() {
		super("poisonous", 0xFFFFFF);
	}
	
	@Override
	public float onHurt(ItemStack armor, EntityPlayer player, DamageSource source, float damage, float newDamage, LivingHurtEvent evt) {
		Entity damager = source.getImmediateSource();
		if (damager instanceof EntityLivingBase) {
			((EntityLivingBase) damager).addPotionEffect(new PotionEffect(MobEffects.POISON, 81));
		}
		return super.onHurt(armor, player, source, damage, newDamage, evt);
	}
}
