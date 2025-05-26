package nc.init;

import nc.*;
import nc.block.fluid.NCBlockFluid;
import nc.block.item.NCItemBlock;
import nc.enumm.FluidType;
import nc.util.*;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

import static nc.config.NCConfig.register_cofh_fluids;

public class NCFluids {
	
	public static List<Pair<Fluid, NCBlockFluid>> fluidPairList = new ArrayList<>();
	
	public static void init() {
		addFluidPair(FluidType.GAS, "oxygen", 0x7E8CC8);
		addFluidPair(FluidType.GAS, "hydrogen", 0xB37AC4);
		addFluidPair(FluidType.GAS, "deuterium", 0x9E6FEF);
		addFluidPair(FluidType.GAS, "tritium", 0x5DBBD6);
		addFluidPair(FluidType.GAS, "helium_3", 0xCBBB67);
		addFluidPair(FluidType.GAS, "helium", 0xC57B81);
		
		addFluidPair(FluidType.MOLTEN, "boron_10", 0x7D7D7D);
		addFluidPair(FluidType.MOLTEN, "boron_11", 0x7D7D7D);
		addFluidPair(FluidType.MOLTEN, "lithium_6", 0xEFEFEF);
		addFluidPair(FluidType.MOLTEN, "lithium_7", 0xEFEFEF);
		
		addFluidPair(FluidType.MOLTEN, "steel", 0x7B7B7B);
		addFluidPair(FluidType.MOLTEN, "ferroboron", 0x4A4A4A);
		addFluidPair(FluidType.MOLTEN, "tough", 0x150F21);
		addFluidPair(FluidType.MOLTEN, "hard_carbon", 0x195970);
		
		if (registerCoFHAlt()) {
			addFluidPair(FluidType.MOLTEN, "coal", 0x202020);
		}
		addFluidPair(FluidType.MOLTEN, "beryllium", 0xD4DBC2);
		addFluidPair(FluidType.MOLTEN, "zirconium", 0xE0E0B8);
		addFluidPair(FluidType.MOLTEN, "manganese_dioxide", 0x28211E);
		addFluidPair(FluidType.MOLTEN, "sulfur", 0xDEDE7A);
		
		addFluidPair(FluidType.SUPERFLUID, "liquid_helium");
		addFluidPair(FluidType.LIQUID, "liquid_nitrogen", false, 0x31C23A, 810, 70, 170, 0);
		addFluidPair(FluidType.LIQUID, "le_water");
		addFluidPair(FluidType.LIQUID, "he_water");
		addFluidPair(FluidType.LIQUID, "heavy_water");
		addFluidPair(FluidType.LIQUID, "hydrogen_peroxide");
		
		if (registerCoFHAlt()) {
			addFluidPair(FluidType.LIQUID, "ender", true, 0x14584D, 4000, 300, 2500, 3);
		}
		addFluidPair(FluidType.MOLTEN, "lead_platinum", 0x415B60);
		addFluidPair(FluidType.MOLTEN, "enderium", 0x0B5B5C);
		if (registerCoFHAlt()) {
			addFluidPair(FluidType.CRYOTHEUM, "cryotheum", 0x0099C1);
		}
		
		addFluidPair(FluidType.PLASMA, "plasma");
		
		addFluidPair(FluidType.FLAMMABLE, "ethanol", 0x655140);
		addFluidPair(FluidType.FLAMMABLE, "methanol", 0x71524C);
		addFluidPair(FluidType.FLAMMABLE, "benzene", 0x999999);
		addFluidPair(FluidType.FLAMMABLE, "phenol", 0xF2F2F2);
		addFluidPair(FluidType.FLAMMABLE, "fluorobenzene", 0xBAB58B);
		addFluidPair(FluidType.FLAMMABLE, "difluorobenzene", 0x8CB57B);
		addFluidPair(FluidType.FLAMMABLE, "dimethyldifluorosilane", 0xAEAF80);
		
		addFluidPair(FluidType.LIQUID, "radaway");
		addFluidPair(FluidType.LIQUID, "radaway_slow");
		addFluidPair(FluidType.LIQUID, "redstone_ethanol");
		
		addFluidPair(FluidType.GAS, "nitrogen", 0x7CC37B);
		addFluidPair(FluidType.GAS, "fluorine", 0xD3C75D);
		
		addFluidPair(FluidType.GAS, "carbon_dioxide", 0x5C635A);
		addFluidPair(FluidType.GAS, "carbon_monoxide", 0x4C5649);
		addFluidPair(FluidType.GAS, "ethene", 0xFFE4A3);
		addFluidPair(FluidType.GAS, "ethyne", 0xFFE442);
		
		addFluidPair(FluidType.GAS, "fluoromethane", 0x424C05);
		addFluidPair(FluidType.GAS, "ammonia", 0x7AC3A0);
		addFluidPair(FluidType.GAS, "oxygen_difluoride", 0xEA1B01);
		addFluidPair(FluidType.GAS, "diborane", 0xCC6E8C);
		addFluidPair(FluidType.GAS, "sulfur_dioxide", 0xC3BC7A);
		addFluidPair(FluidType.GAS, "sulfur_trioxide", 0xD3AE5D);
		addFluidPair(FluidType.GAS, "tetrafluoroethene", 0x7EA542);
		addFluidPair(FluidType.GAS, "hydrogen_sulfide", 0x785830);
		addFluidPair(FluidType.GAS, "depleted_hydrogen_sulfide", 0x59514E);
		
		addFluidPair(FluidType.ACID, "hydrofluoric_acid", 0x004C05);
		addFluidPair(FluidType.ACID, "boric_acid", 0x696939);
		addFluidPair(FluidType.ACID, "sulfuric_acid", 0x454500);
		
		addFluidPair(FluidType.SALT_SOLUTION, "boron_nitride_solution", ColorHelper.waterBlend(0x6F8E5C, 0.5F));
		addFluidPair(FluidType.SALT_SOLUTION, "fluorite_water", ColorHelper.waterBlend(0x8AB492, 0.5F));
		addFluidPair(FluidType.SALT_SOLUTION, "calcium_sulfate_solution", ColorHelper.waterBlend(0xB8B0A6, 0.5F));
		addFluidPair(FluidType.SALT_SOLUTION, "sodium_fluoride_solution", ColorHelper.waterBlend(0xC2B1A1, 0.5F));
		addFluidPair(FluidType.SALT_SOLUTION, "potassium_fluoride_solution", ColorHelper.waterBlend(0xC1C99D, 0.5F));
		addFluidPair(FluidType.SALT_SOLUTION, "sodium_hydroxide_solution", ColorHelper.waterBlend(0xC2B7BB, 0.5F));
		addFluidPair(FluidType.SALT_SOLUTION, "potassium_hydroxide_solution", ColorHelper.waterBlend(0xB8C6B0, 0.5F));
		addFluidPair(FluidType.SALT_SOLUTION, "borax_solution", ColorHelper.waterBlend(0xEEEEEE, 0.5F));
		addFluidPair(FluidType.SALT_SOLUTION, "irradiated_borax_solution", ColorHelper.waterBlend(0xFFD0A3, 0.5F));
		addFluidPair(FluidType.SALT_SOLUTION, "ammonium_sulfate_solution", ColorHelper.waterBlend(0x6CA377, 0.5F));
		addFluidPair(FluidType.SALT_SOLUTION, "ammonium_bisulfate_solution", ColorHelper.waterBlend(0x5F8450, 0.5F));
		addFluidPair(FluidType.SALT_SOLUTION, "ammonium_persulfate_solution", ColorHelper.waterBlend(0x74A364, 0.5F));
		addFluidPair(FluidType.SALT_SOLUTION, "hydroquinone_solution", ColorHelper.waterBlend(0xB7B7B7, 0.5F));
		addFluidPair(FluidType.SALT_SOLUTION, "sodium_hydroquinone_solution", ColorHelper.waterBlend(0xC9B9BA, 0.5F));
		addFluidPair(FluidType.SALT_SOLUTION, "potassium_hydroquinone_solution", ColorHelper.waterBlend(0xC6CA94, 0.5F));
		
		addFluidPair(FluidType.CORIUM, "corium", 0x7C7C6F);
		
		addFluidPair(FluidType.LIQUID, "ice", false, 0xAFF1FF, 1000, 250, 2000, 0);
		addFluidPair(FluidType.LIQUID, "slurry_ice", false, 0x7EAEB7, 950, 270, 4000, 0);
		
		addFluidPair(FluidType.CHOCOLATE, "chocolate_liquor", 0x41241C);
		addFluidPair(FluidType.CHOCOLATE, "cocoa_butter", 0xF6EEBF);
		addFluidPair(FluidType.CHOCOLATE, "unsweetened_chocolate", 0x2C0A08);
		addFluidPair(FluidType.CHOCOLATE, "dark_chocolate", 0x2C0B06);
		addFluidPair(FluidType.CHOCOLATE, "milk_chocolate", 0x884121);
		addFluidPair(FluidType.SUGAR, "sugar", 0xFFD59A);
		addFluidPair(FluidType.SUGAR, "gelatin", 0xDDD09C);
		addFluidPair(FluidType.SUGAR, "hydrated_gelatin", ColorHelper.waterBlend(0xDDD09C, 0.75F));
		addFluidPair(FluidType.CHOCOLATE, "marshmallow", 0xE1E1E3);
		addFluidPair(FluidType.LIQUID, "milk");
		
		addFluidPair(FluidType.MOLTEN, "lif", 0xCDCDCB);
		addFluidPair(FluidType.MOLTEN, "bef2", 0xBEC6AA);
		addFluidPair(FluidType.MOLTEN, "flibe", 0xC1C8B0);
		addFluidPair(FluidType.MOLTEN, "naoh", 0xC2B7BB);
		addFluidPair(FluidType.MOLTEN, "koh", 0xB8C6B0);
		
		addFluidPair(FluidType.MOLTEN, "dfdps", 0xB4B3A7);
		addFluidPair(FluidType.MOLTEN, "polyphenylene_sulfide", 0x3F3D3E);
		addFluidPair(FluidType.MOLTEN, "polydimethylsilylene", 0x774F60);
		addFluidPair(FluidType.MOLTEN, "polymethylsilylene_methylene", 0x5A5246);
		addFluidPair(FluidType.MOLTEN, "polyethersulfone", 0xC9B8A6);
		addFluidPair(FluidType.MOLTEN, "polytetrafluoroethene", 0x7F9F4D);
		
		if (registerCoFHAlt()) {
			addFluidPair(FluidType.STEAM, "steam", 0x929292, 550);
		}
		addFluidPair(FluidType.STEAM, "high_pressure_steam", 0xBDBDBD, 600);
		addFluidPair(FluidType.STEAM, "exhaust_steam", 0x7E7E7E, 400);
		addFluidPair(FluidType.STEAM, "low_pressure_steam", 0xA8A8A8, 550);
		addFluidPair(FluidType.STEAM, "low_quality_steam", 0x828282, 350);
		
		addFluidPair(FluidType.LIQUID, "preheated_water", false, 0x2F43F4, 1000, 400, 250, 0);
		addFluidPair(FluidType.LIQUID, "condensate_water", false, 0x2F43F4, 1000, 350, 850, 0);
		
		addFluidPair(FluidType.MOLTEN, "sodium", 0xC1898C);
		addFluidPair(FluidType.MOLTEN, "potassium", 0xB8C503);
		
		addFluidPair(FluidType.MOLTEN, "sodium_sulfide", 0x9A8B0B);
		addFluidPair(FluidType.MOLTEN, "potassium_sulfide", 0x917C34);
		
		addFluidPair(FluidType.COOLANT, "nak", 0xFFE5BC);
		addFluidPair(FluidType.HOT_COOLANT, "nak_hot", 0xFFD5AC);
		addFluidPair(FluidType.LIQUID, "emergency_coolant", true, 0x6DD0E7, 2000, 250, 2000, 0);
		addFluidPair(FluidType.LIQUID, "emergency_coolant_heated", true, 0xCDBEE7, 2000, 400, 1500, 7);
		
		addFluidPair(FluidType.MOLTEN, "silicon", 0x676767);
		addFluidPair(FluidType.HOT_GAS, "arsenic", 0x818475);
		addFluidPair(FluidType.MOLTEN, "bas", 0x9B9B89);
		addFluidPair(FluidType.HOT_GAS, "sic_vapor", 0x78746A);
		
		addFluidPair(FluidType.MOLTEN, "alugentum", 0xB5C9CB);
		addFluidPair(FluidType.MOLTEN, "alumina", 0x919880);
		
		addFluidPair(FluidType.FISSION, "strontium_90", 0xB8BE88);
		addFluidPair(FluidType.FISSION, "molybdenum", 0xBCC5E4);
		addFluidPair(FluidType.FISSION, "ruthenium_106", 0xA3A3A3);
		addFluidPair(FluidType.FISSION, "caesium_137", 0xADADAD);
		addFluidPair(FluidType.FISSION, "promethium_147", 0x96C199);
		addFluidPair(FluidType.FISSION, "europium_155", 0x74664A);
	}
	
	private static <T extends Fluid, V extends NCBlockFluid> void addFluidPair(FluidType type, Object... args) {
		T fluid = ReflectionHelper.newInstance(type.getFluidClass(), args);
		V block = ReflectionHelper.newInstance(type.getBlockClass(), fluid);
		fluidPairList.add(Pair.of(fluid, block));
	}
	
	public static void register() {
		for (Pair<Fluid, NCBlockFluid> fluidPair : fluidPairList) {
			Fluid fluid = fluidPair.getLeft();
			if (!FluidRegistry.registerFluid(fluid)) {
				fluid = FluidRegistry.getFluid(fluid.getName());
			}
			FluidRegistry.addBucketForFluid(fluid);
			
			NCBlockFluid fluidBlock = fluidPair.getRight();
			if (fluidBlock != null) {
				registerBlock(Global.MOD_ID, fluidBlock);
			}
		}
	}
	
	private static void registerBlock(String modId, NCBlockFluid block) {
		ForgeRegistries.BLOCKS.register(withName(modId, block));
		ForgeRegistries.ITEMS.register(new NCItemBlock(block, TextFormatting.AQUA).setRegistryName(block.getRegistryName()));
		NuclearCraft.proxy.registerFluidBlockRendering(block, block.name);
	}
	
	private static <T extends NCBlockFluid> Block withName(String modId, T block) {
		return block.setTranslationKey(modId + "." + block.name).setRegistryName(new ResourceLocation(modId, block.name));
	}
	
	private static boolean registerCoFHAlt() {
		return register_cofh_fluids || !ModCheck.thermalFoundationLoaded();
	}
}
