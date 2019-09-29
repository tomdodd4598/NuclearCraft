package nc.init;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import nc.Global;
import nc.ModCheck;
import nc.NuclearCraft;
import nc.block.fluid.NCBlockFluid;
import nc.block.item.NCItemBlock;
import nc.config.NCConfig;
import nc.enumm.FluidType;
import nc.util.ColorHelper;
import nc.util.NCUtil;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class NCFluids {
	
	public static List<Pair<Fluid, NCBlockFluid>> fluidPairList = new ArrayList<Pair<Fluid, NCBlockFluid>>();
	
	public static void init() {
		try {
			fluidPairList.add(fluidPair(FluidType.GAS, "oxygen", 0x7E8CC8));
			fluidPairList.add(fluidPair(FluidType.GAS, "hydrogen", 0xB37AC4));
			fluidPairList.add(fluidPair(FluidType.GAS, "deuterium", 0x9E6FEF));
			fluidPairList.add(fluidPair(FluidType.GAS, "tritium", 0x5DBBD6));
			fluidPairList.add(fluidPair(FluidType.GAS, "helium3", 0xCBBB67));
			fluidPairList.add(fluidPair(FluidType.GAS, "helium", 0xC57B81));
			
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "boron", 0x7D7D7D));
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "boron10", 0x7D7D7D));
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "boron11", 0x7D7D7D));
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "lithium", 0xEFEFEF));
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "lithium6", 0xEFEFEF));
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "lithium7", 0xEFEFEF));
			
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "ferroboron", 0x4A4A4A));
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "tough", 0x150F21));
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "hard_carbon", 0x195970));
			
			if (registerCoFHAlt()) fluidPairList.add(fluidPair(FluidType.MOLTEN, "coal", 0x202020));
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "beryllium", 0xD4DBC2));
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "manganese", 0x7284CC));
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "manganese_dioxide", 0x28211E));
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "sulfur", 0xDEDE7A));
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "arsenic", 0x818475));
			
			fluidPairList.add(fluidPair(FluidType.SUPERFLUID, "liquid_helium"));
			fluidPairList.add(fluidPair(FluidType.LIQUID, "liquid_nitrogen", false, 0x31C23A, 810, 70, 170));
			fluidPairList.add(fluidPair(FluidType.LIQUID, "heavy_water"));
			
			fluidPairList.add(fluidPair(FluidType.PLASMA, "plasma"));
			
			fluidPairList.add(fluidPair(FluidType.FLAMMABLE, "ethanol", 0x655140));
			fluidPairList.add(fluidPair(FluidType.FLAMMABLE, "methanol", 0x71524C));
			
			fluidPairList.add(fluidPair(FluidType.LIQUID, "radaway"));
			fluidPairList.add(fluidPair(FluidType.LIQUID, "radaway_slow"));
			
			fluidPairList.add(fluidPair(FluidType.GAS, "nitrogen", 0x7CC37B));
			fluidPairList.add(fluidPair(FluidType.GAS, "fluorine", 0xD3C75D));
			
			fluidPairList.add(fluidPair(FluidType.GAS, "carbon_dioxide", 0x5C635A));
			fluidPairList.add(fluidPair(FluidType.GAS, "carbon_monoxide", 0x4C5649));
			fluidPairList.add(fluidPair(FluidType.GAS, "ethene", 0xFFE4A3));
			
			fluidPairList.add(fluidPair(FluidType.GAS, "fluoromethane", 0x424C05));
			fluidPairList.add(fluidPair(FluidType.GAS, "ammonia", 0x7AC3A0));
			fluidPairList.add(fluidPair(FluidType.GAS, "oxygen_difluoride", 0xEA1B01));
			fluidPairList.add(fluidPair(FluidType.GAS, "diborane", 0xCC6E8C));
			fluidPairList.add(fluidPair(FluidType.GAS, "sulfur_dioxide", 0xC3BC7A));
			fluidPairList.add(fluidPair(FluidType.GAS, "sulfur_trioxide", 0xD3AE5D));
			
			fluidPairList.add(fluidPair(FluidType.ACID, "hydrofluoric_acid", 0x004C05));
			fluidPairList.add(fluidPair(FluidType.ACID, "boric_acid", 0x696939));
			fluidPairList.add(fluidPair(FluidType.ACID, "sulfuric_acid", 0x454500));
			
			fluidPairList.add(fluidPair(FluidType.SALT_SOLUTION, "boron_nitride_solution", waterBlend(0x6F8E5C)));
			fluidPairList.add(fluidPair(FluidType.SALT_SOLUTION, "fluorite_water", waterBlend(0x8AB492)));
			fluidPairList.add(fluidPair(FluidType.SALT_SOLUTION, "calcium_sulfate_solution", waterBlend(0xB8B0A6)));
			fluidPairList.add(fluidPair(FluidType.SALT_SOLUTION, "sodium_fluoride_solution", waterBlend(0xC2B1A1)));
			fluidPairList.add(fluidPair(FluidType.SALT_SOLUTION, "potassium_fluoride_solution", waterBlend(0xC1C99D)));
			fluidPairList.add(fluidPair(FluidType.SALT_SOLUTION, "sodium_hydroxide_solution", waterBlend(0xC2B7BB)));
			fluidPairList.add(fluidPair(FluidType.SALT_SOLUTION, "potassium_hydroxide_solution", waterBlend(0xB8C6B0)));
			fluidPairList.add(fluidPair(FluidType.SALT_SOLUTION, "borax_solution", waterBlend(0xEEEEEE)));
			
			fluidPairList.add(fluidPair(FluidType.CORIUM, "corium", 0x7F8178));
			
			fluidPairList.add(fluidPair(FluidType.CHOCOLATE, "chocolate_liquor", 0x41241C));
			fluidPairList.add(fluidPair(FluidType.CHOCOLATE, "cocoa_butter", 0xF6EEBF));
			fluidPairList.add(fluidPair(FluidType.CHOCOLATE, "unsweetened_chocolate", 0x2C0A08));
			fluidPairList.add(fluidPair(FluidType.CHOCOLATE, "dark_chocolate", 0x2C0B06));
			fluidPairList.add(fluidPair(FluidType.CHOCOLATE, "milk_chocolate", 0x884121));
			fluidPairList.add(fluidPair(FluidType.SUGAR, "sugar", 0xFFD59A));
			fluidPairList.add(fluidPair(FluidType.SUGAR, "gelatin", 0xDDD09C));
			fluidPairList.add(fluidPair(FluidType.SUGAR, "hydrated_gelatin", waterBlend(0xDDD09C, 0.8F)));
			fluidPairList.add(fluidPair(FluidType.CHOCOLATE, "marshmallow", 0xE1E1E3));
			fluidPairList.add(fluidPair(FluidType.LIQUID, "milk", true, 0xDEDBCF, 1100, 300, 1000));
			
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "lif", 0xCDCDCB));
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "bef2", 0xBEC6AA));
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "flibe", 0xC1C8B0));
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "naoh", 0xC2B7BB));
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "koh", 0xB8C6B0));
			
			if (registerCoFHAlt()) fluidPairList.add(fluidPair(FluidType.STEAM, "steam", 0x929292, 800));
			fluidPairList.add(fluidPair(FluidType.STEAM, "high_pressure_steam", 0xBDBDBD, 1200));
			fluidPairList.add(fluidPair(FluidType.STEAM, "exhaust_steam", 0x7E7E7E, 500));
			fluidPairList.add(fluidPair(FluidType.STEAM, "low_pressure_steam", 0xA8A8A8, 800));
			fluidPairList.add(fluidPair(FluidType.STEAM, "low_quality_steam", 0x828282, 350));
			
			fluidPairList.add(fluidPair(FluidType.LIQUID, "preheated_water", false, 0x2F43F4, 1000, 400, 250));
			fluidPairList.add(fluidPair(FluidType.LIQUID, "condensate_water", false, 0x2F43F4, 1000, 300, 850));
			
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "sodium", 0xC1898C));
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "potassium", 0xB8C503));
			
			fluidPairList.add(fluidPair(FluidType.COOLANT, "nak", 0xFFE5BC));
			fluidPairList.add(fluidPair(FluidType.HOT_COOLANT, "nak_hot", 0xFFD5AC));
			
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "bas", 0x9B9B89));
			
			fluidPairList.add(fluidPair(FluidType.HOT_GAS, "sic_vapor", 0x78746A));
			
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "alugentum", 0xB5C9CB));
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "alumina", 0x919880));
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "aluminum", 0xB5ECD5));
			fluidPairList.add(fluidPair(FluidType.MOLTEN, "silver", 0xE2DAF6));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static <T extends NCBlockFluid> Block withName(T block) {
		return block.setTranslationKey(Global.MOD_ID + "." + block.getBlockName()).setRegistryName(new ResourceLocation(Global.MOD_ID, block.getBlockName()));
	}
	
	public static void register() {
		for (Pair<Fluid, NCBlockFluid> fluidPair : fluidPairList) {
			Fluid fluid = fluidPair.getLeft();
			
			boolean defaultFluid = FluidRegistry.registerFluid(fluid);
			if (!defaultFluid) fluid = FluidRegistry.getFluid(fluid.getName());
			FluidRegistry.addBucketForFluid(fluid);
			
			registerBlock(fluidPair.getRight());
		}
	}
	
	public static void registerBlock(NCBlockFluid block) {
		ForgeRegistries.BLOCKS.register(withName(block));
		ForgeRegistries.ITEMS.register(new NCItemBlock(block).setRegistryName(block.getRegistryName()));
		NuclearCraft.proxy.registerFluidBlockRendering(block, block.getBlockName());
	}
	
	public static <T extends Fluid, V extends NCBlockFluid> Pair<Fluid, NCBlockFluid> fluidPair(FluidType fluidType, Object... fluidArgs) throws Exception {
		T fluid = NCUtil.newInstance(fluidType.getFluidClass(), fluidArgs);
		V block = NCUtil.newInstance(fluidType.getBlockClass(), fluid);
		return Pair.of(fluid, block);
	}
	
	private static int waterBlend(int soluteColor, float blendRatio) {
		return ColorHelper.blend(0x2F43F4, soluteColor, blendRatio);
	}
	
	private static int waterBlend(int soluteColor) {
		return waterBlend(soluteColor, 0.5F);
	}
	
	private static boolean registerCoFHAlt() {
		return NCConfig.register_cofh_fluids || !ModCheck.thermalFoundationLoaded();
	}
}
