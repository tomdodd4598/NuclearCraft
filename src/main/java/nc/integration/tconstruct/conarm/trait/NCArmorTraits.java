package nc.integration.tconstruct.conarm.trait;

import c4.conarm.lib.traits.AbstractArmorTrait;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class NCArmorTraits {
	
	public static final AbstractTrait MOLDABLE_I = new ArmorTraitMoldable(1);
	public static final AbstractTrait MOLDABLE_II = new ArmorTraitMoldable(2);
	public static final AbstractArmorTrait POISONOUS = new ArmorTraitPoisonous();
	public static final AbstractArmorTrait UPLIFTING = new ArmorTraitUplifting();
	public static final AbstractArmorTrait WITHERING = new ArmorTraitWithering();
}
