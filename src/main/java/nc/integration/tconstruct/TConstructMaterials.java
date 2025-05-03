package nc.integration.tconstruct;

import nc.*;
import nc.integration.tconstruct.trait.NCTraits;
import nc.util.NCMath;
import net.minecraftforge.fluids.FluidRegistry;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.tools.TinkerTraits;

import static nc.config.NCConfig.*;
import static nc.util.FluidStackHelper.INGOT_VOLUME;

public class TConstructMaterials {
	
	public static void init() {
		if (register_tic_tool[0] || ModCheck.constructsArmoryLoaded() && register_conarm_armor[0]) {
			registerMaterial("boron", "Boron", "ingotBoron", "boron", true, new ITrait[] {TinkerTraits.dense, TinkerTraits.stiff}, new ITrait[] {TinkerTraits.stiff}, 0x6C6C6C, 0x7D7D7D, 0x959595, false, 0);
		}
		if (register_tic_tool[1] || ModCheck.constructsArmoryLoaded() && register_conarm_armor[1]) {
			registerMaterial("tough", "Tough", "ingotTough", "tough", true, new ITrait[] {TinkerTraits.dense, TinkerTraits.momentum}, new ITrait[] {TinkerTraits.dense}, 0x140E1F, 0x150F21, 0x171221, false, 1);
		}
		if (register_tic_tool[2] || ModCheck.constructsArmoryLoaded() && register_conarm_armor[2]) {
			registerMaterial("hard_carbon", "HardCarbon", "ingotHardCarbon", "hard_carbon", true, new ITrait[] {TinkerTraits.lightweight, TinkerTraits.sharp}, new ITrait[] {TinkerTraits.lightweight}, 0x0C284F, 0x195970, 0x227871, false, 2);
		}
		if (register_tic_tool[3] || ModCheck.constructsArmoryLoaded() && register_conarm_armor[3]) {
			registerMaterial("boron_nitride", "BoronNitride", "gemBoronNitride", null, false, new ITrait[] {TinkerTraits.jagged, TinkerTraits.splintering}, new ITrait[] {TinkerTraits.splintering}, 0xCE75B269, 0xCE75B269, 0xCE75B269, true, 3);
		}
		
		if (register_tic_tool[4] || ModCheck.constructsArmoryLoaded() && register_conarm_armor[4]) {
			registerMaterial("thorium", "Thorium", "ingotThorium", "thorium", true, new ITrait[] {NCTraits.WITHERING}, new ITrait[] {TinkerTraits.poisonous, NCTraits.WITHERING}, 0x222222, 0x292929, 0x2E2E2E, false, 225, 9D, 2.5D, 2, 0.75D);
		}
		if (register_tic_tool[5] || ModCheck.constructsArmoryLoaded() && register_conarm_armor[5]) {
			registerMaterial("uranium", "Uranium", "ingotUranium", "uranium", true, new ITrait[] {TinkerTraits.poisonous}, new ITrait[] {TinkerTraits.poisonous, NCTraits.WITHERING}, 0x344F33, 0x406340, 0x4C6B4C, false, 242, 7.5D, 2D, 2, 0.8D);
		}
		if (register_tic_tool[6] || ModCheck.constructsArmoryLoaded() && register_conarm_armor[6]) {
			registerMaterial("magnesium", "Magnesium", "ingotMagnesium", "magnesium", true, new ITrait[] {TinkerTraits.holy, TinkerTraits.lightweight}, new ITrait[] {TinkerTraits.lightweight}, 0xB898B5, 0xE3BDDF, 0xF7DFF9, false, 197, 11D, 1.5D, 1, 1D);
		}
		if (register_tic_tool[7] || ModCheck.constructsArmoryLoaded() && register_conarm_armor[7]) {
			registerMaterial("chocolate", "Chocolate", "ingotChocolate", "milk_chocolate", true, new ITrait[] {NCTraits.MOLDABLE_II, TinkerTraits.tasty}, new ITrait[] {NCTraits.MOLDABLE_I, NCTraits.UPLIFTING}, 0x4D180A, 0x581E0C, 0x62230E, false, 32, 2D, 0D, 0, 0.15D);
		}
	}
	
	private static void registerMaterial(String materialName, String oreSuffix, String repairItem, String fluidName, boolean casted, ITrait[] headTraits, ITrait[] extraTraits, int colorLow, int colorMed, int colorHigh, boolean transparent, int durability, double miningSpeed, double attackDamage, int miningLevel, double handleModifier) {
		for (MaterialIntegration matInteg : TinkerRegistry.getMaterialIntegrations()) {
			if (matInteg != null && matInteg.material != null) {
				if (matInteg.material.getIdentifier().equals(materialName)) {
					return;
				}
			}
		}
		if (TinkerRegistry.getMaterial(materialName) != Material.UNKNOWN) {
			return;
		}
		
		Material material = new Material(materialName, colorMed);
		for (ITrait headTrait : headTraits) {
			material.addTrait(headTrait);
		}
		for (ITrait extraTrait : extraTraits) {
			material.addTrait(extraTrait, MaterialTypes.HANDLE);
			material.addTrait(extraTrait, MaterialTypes.EXTRA);
		}
		material.addItem(repairItem, 1, INGOT_VOLUME);
		material.setCraftable(!casted).setCastable(casted);
		if (transparent) {
			NuclearCraft.proxy.setRenderInfo(material, colorMed);
		}
		else {
			NuclearCraft.proxy.setRenderInfo(material, colorLow, colorMed, colorHigh);
		}
		
		material.setFluid(fluidName == null ? null : FluidRegistry.getFluid(fluidName));
		
		TConstructHelper.addMaterialStats(material, new HeadMaterialStats(NCMath.toInt(durability * 0.8D), (float) miningSpeed, 2F + (float) attackDamage, miningLevel), new HandleMaterialStats((float) handleModifier, NCMath.toInt(durability * (casted ? 0.25D : 0.1D))), new ExtraMaterialStats((int) (durability * (casted ? 0.0375D : 0.0625D) / handleModifier)), new BowMaterialStats((float) (handleModifier / 2D), (float) (handleModifier * 2D), 2F + (float) attackDamage));
		
		integrateMaterial(material, oreSuffix, casted);
		material.setRepresentativeItem(repairItem);
	}
	
	private static void registerMaterial(String materialName, String oreSuffix, String repairItem, String fluidName, boolean casted, ITrait[] headTraits, ITrait[] extraTraits, int colorLow, int colorMed, int colorHigh, boolean transparent, int toolNumber) {
		registerMaterial(materialName, oreSuffix, repairItem, fluidName, casted, headTraits, extraTraits, colorLow, colorMed, colorHigh, transparent, tool_durability[2 * toolNumber], tool_speed[2 * toolNumber], tool_attack_damage[2 * toolNumber], tool_mining_level[2 * toolNumber], tool_handle_modifier[toolNumber]);
	}
	
	private static void integrateMaterial(Material material, String oreSuffix, boolean toolForge) {
		MaterialIntegration matInteg;
		if (material.getFluid() != null) {
			matInteg = new MaterialIntegration(material, material.getFluid(), oreSuffix);
		}
		else {
			matInteg = new MaterialIntegration(material);
		}
		if (toolForge) {
			matInteg = matInteg.toolforge();
		}
		TinkerRegistry.integrate(matInteg).preInit();
	}
}
