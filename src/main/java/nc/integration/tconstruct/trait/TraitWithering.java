package nc.integration.tconstruct.trait;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitWithering extends AbstractTrait implements ITraitNC {
	
	public TraitWithering() {
		super("withering", TextFormatting.DARK_GRAY);
	}
	
	@Override
	public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
		if (wasHit && target.isEntityAlive()) {
			target.addPotionEffect(new PotionEffect(MobEffects.WITHER, 81));
		}
	}
}
