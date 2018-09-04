package nc.integration.tconstruct;

import nc.NuclearCraft;
import nc.config.NCConfig;
import nc.util.FluidStackHelper;
import net.minecraftforge.fluids.FluidRegistry;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.tools.TinkerTraits;

public class TConstructMaterials {
	
	public static void init() {
		registerMaterial("boron", "Boron", "ingotBoron", "boron", true, TinkerTraits.dense, TinkerTraits.stiff, 0x6C6C6C, 0x7D7D7D, 0x959595, false, 0);
		registerMaterial("tough", "Tough", "ingotTough", "tough", true, TinkerTraits.momentum, TinkerTraits.dense, 0x140E1F, 0x150F21, 0x171221, false, 1);
		registerMaterial("hard_carbon", "HardCarbon", "ingotHardCarbon", "hard_carbon", true, TinkerTraits.sharp, TinkerTraits.lightweight, 0x0C284F, 0x195970, 0x227871, false, 2);
		registerMaterial("boron_nitride", "BoronNitride", "gemBoronNitride", null, false, TinkerTraits.jagged, TinkerTraits.splintering, 0xCE75B269, 0xCE75B269, 0xCE75B269, true, 3);
	}
	
	private static void registerMaterial(String materialName, String oreSuffix, String repairItem, String fluidName, boolean casted, ITrait headTrait, ITrait extraTrait, int colorLow, int colorMed, int colorHigh, boolean transparent, int toolNumber) {
		Material material = new Material(materialName, colorMed);
		material.addTrait(headTrait);
		material.addTrait(extraTrait, MaterialTypes.HANDLE);
		material.addTrait(extraTrait, MaterialTypes.EXTRA);
		material.addItem(repairItem, 1, FluidStackHelper.INGOT_VOLUME);
		material.setCraftable(!casted).setCastable(casted);
		if (transparent) NuclearCraft.proxy.setRenderInfo(material, colorMed);
		else NuclearCraft.proxy.setRenderInfo(material, colorLow, colorMed, colorHigh);
		
		material.setFluid(fluidName == null ? null : FluidRegistry.getFluid(fluidName));
		
		TinkerRegistry.addMaterialStats(material,
				new HeadMaterialStats((int)(NCConfig.tool_durability[2*toolNumber]*0.8D), (float)NCConfig.tool_speed[2*toolNumber], 2F + (float)NCConfig.tool_attack_damage[2*toolNumber], NCConfig.tool_mining_level[2*toolNumber]),
				new HandleMaterialStats((float)NCConfig.tool_handle_modifier[toolNumber], (int)(NCConfig.tool_durability[2*toolNumber]*0.25D)),
				new ExtraMaterialStats((int)(NCConfig.tool_durability[2*toolNumber]*0.0625D/NCConfig.tool_handle_modifier[toolNumber])),
				new BowMaterialStats((float)(NCConfig.tool_handle_modifier[toolNumber]/2D), (float)(NCConfig.tool_handle_modifier[toolNumber]*2D), 2F + (float)NCConfig.tool_attack_damage[2*toolNumber]));
		
		integrateMaterial(material, oreSuffix, casted);
		material.setRepresentativeItem(repairItem);
	}
	
	private static void integrateMaterial(Material material, String oreSuffix, boolean toolForge) {
		MaterialIntegration matInteg;
		if (material.getFluid() != null) matInteg = new MaterialIntegration(material, material.getFluid(), oreSuffix);
		else matInteg = new MaterialIntegration(material);
		if (toolForge) matInteg = matInteg.toolforge();
		TinkerRegistry.integrate(matInteg).preInit();
	}
}
