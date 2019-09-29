package nc.integration.tconstruct.trait;

import slimeknights.tconstruct.library.traits.AbstractTrait;

public class NCTraits {
	
	public static final AbstractTrait MOLDABLE_I = new TraitMoldable(1);
	public static final AbstractTrait MOLDABLE_II = new TraitMoldable(2);
	public static final AbstractTrait UPLIFTING = new TraitUplifting();
	public static final AbstractTrait WITHERING = new TraitWithering();
}
