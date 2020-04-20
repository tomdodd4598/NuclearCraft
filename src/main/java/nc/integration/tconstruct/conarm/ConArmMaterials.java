package nc.integration.tconstruct.conarm;

import c4.conarm.common.armor.traits.ArmorTraits;
import c4.conarm.lib.materials.ArmorMaterialType;
import c4.conarm.lib.materials.ArmorMaterials;
import c4.conarm.lib.materials.CoreMaterialStats;
import c4.conarm.lib.materials.PlatesMaterialStats;
import c4.conarm.lib.materials.TrimMaterialStats;
import nc.config.NCConfig;
import nc.integration.tconstruct.TConstructHelper;
import nc.integration.tconstruct.conarm.trait.NCArmorTraits;
import nc.util.CollectionHelper;
import nc.util.NCMath;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.traits.ITrait;

public class ConArmMaterials extends ArmorMaterials {
	
	public static void preInit() {
		if (NCConfig.register_conarm_armor[0]) addArmorMaterial("boron", 0, NCConfig.armor_boron);
		if (NCConfig.register_conarm_armor[1]) addArmorMaterial("tough", 1, NCConfig.armor_tough);
		if (NCConfig.register_conarm_armor[2]) addArmorMaterial("hard_carbon", 2, NCConfig.armor_hard_carbon);
		if (NCConfig.register_conarm_armor[3]) addArmorMaterial("boron_nitride", 3, NCConfig.armor_boron_nitride);
		
		if (NCConfig.register_conarm_armor[4]) addArmorMaterial("thorium", 19D, 15.5D, 0.8D, 0.75D);
		if (NCConfig.register_conarm_armor[5]) addArmorMaterial("uranium", 20D, 14.5D, 0.6D, 0.8D);
		if (NCConfig.register_conarm_armor[6]) addArmorMaterial("magnesium", 17.5D, 12.5D, 0.2D, 1D);
		if (NCConfig.register_conarm_armor[7]) addArmorMaterial("chocolate", 1.4D, 1.2D, 0D, 0.15D);
	}
	
	private static void addArmorMaterial(String materialName, double durability, double fullSetProtection, double toughness, double modifier) {
		Material material = TinkerRegistry.getMaterial(materialName);
		if (material == Material.UNKNOWN) return;
		
		TConstructHelper.addMaterialStats(material,
				new CoreMaterialStats((float)NCMath.round(0.5D*durability, 1), (float)fullSetProtection),
				new PlatesMaterialStats((float)modifier, (float)NCMath.round(0.25D*modifier*durability, 1), (float)Math.min(5D, NCMath.round(toughness/modifier, 1))),
				new TrimMaterialStats((float)NCMath.round(0.4D*modifier*fullSetProtection, 1)));
	}
	
	private static void addArmorMaterial(String materialName, int armorNumber, int[] protectionArray) {
		addArmorMaterial(materialName, NCConfig.armor_durability[armorNumber], CollectionHelper.sum(protectionArray), NCConfig.armor_toughness[armorNumber], NCConfig.tool_handle_modifier[armorNumber]);
	}
	
	public static void init() {
		addArmorTraits("boron", new ITrait[] {ArmorTraits.dense, ArmorTraits.indomitable}, new ITrait[] {ArmorTraits.indomitable});
		addArmorTraits("tough", new ITrait[] {ArmorTraits.ambitious, ArmorTraits.steady}, new ITrait[] {ArmorTraits.steady});
		addArmorTraits("hard_carbon", new ITrait[] {ArmorTraits.lightweight, ArmorTraits.prideful}, new ITrait[] {ArmorTraits.lightweight});
		addArmorTraits("boron_nitride", new ITrait[] {ArmorTraits.ambitious, ArmorTraits.spiny}, new ITrait[] {ArmorTraits.spiny});
		
		addArmorTraits("thorium", new ITrait[] {NCArmorTraits.WITHERING}, new ITrait[] {ArmorTraits.heavy, NCArmorTraits.WITHERING});
		addArmorTraits("uranium", new ITrait[] {NCArmorTraits.POISONOUS}, new ITrait[] {ArmorTraits.heavy, NCArmorTraits.POISONOUS});
		addArmorTraits("magnesium", new ITrait[] {ArmorTraits.blessed, ArmorTraits.featherweight}, new ITrait[] {ArmorTraits.featherweight});
		addArmorTraits("chocolate", new ITrait[] {NCArmorTraits.MOLDABLE_II, ArmorTraits.tasty}, new ITrait[] {NCArmorTraits.MOLDABLE_I, NCArmorTraits.UPLIFTING});
	}
	
	private static void addArmorTraits(String materialName, ITrait[] coreTraits, ITrait[] extraTraits) {
		Material material = TinkerRegistry.getMaterial(materialName);
		if (material == Material.UNKNOWN) return;
		
		for (ITrait coreTrait : coreTraits) material.addTrait(coreTrait, ArmorMaterialType.CORE);
		for (ITrait extraTrait : extraTraits) {
			material.addTrait(extraTrait, ArmorMaterialType.PLATES);
			material.addTrait(extraTrait, ArmorMaterialType.TRIM);
		}
	}
}
