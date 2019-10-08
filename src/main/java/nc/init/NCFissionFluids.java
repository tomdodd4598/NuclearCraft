package nc.init;

import java.util.ArrayList;
import java.util.List;

import nc.Global;
import nc.NuclearCraft;
import nc.block.fluid.BlockFluidFission;
import nc.block.fluid.NCBlockFluid;
import nc.block.item.NCItemBlock;
import nc.config.NCConfig;
import nc.fluid.FluidFission;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class NCFissionFluids {
	
	public static List<FluidFission> fluidList = new ArrayList<FluidFission>();
	
	public static void init() {
		fluidList.addAll(fissionFluids("thorium", 0x242424));
		fluidList.addAll(fissionFluids("uranium", 0x375437));
		fluidList.addAll(fissionFluids("plutonium", 0xDBDBDB));
		
		//fluidList.addAll(fissionFluids("thorium_232", 0x303030));
		
		fluidList.addAll(fissionFluids("uranium_233", 0x212E20));
		fluidList.addAll(fissionFluids("uranium_235", 0x102D10));
		fluidList.addAll(fissionFluids("uranium_238", 0x333E32));
		
		fluidList.addAll(fissionFluids("neptunium_236", 0x293E40));
		fluidList.addAll(fissionFluids("neptunium_237", 0x2E3A42));
		
		fluidList.addAll(fissionFluids("plutonium_238", 0x999999));
		fluidList.addAll(fissionFluids("plutonium_239", 0x9E9E9E));
		fluidList.addAll(fissionFluids("plutonium_241", 0x909090));
		fluidList.addAll(fissionFluids("plutonium_242", 0xABABAB));
		
		fluidList.addAll(fissionFluids("americium_241", 0x393725));
		fluidList.addAll(fissionFluids("americium_242", 0x332D1A));
		fluidList.addAll(fissionFluids("americium_243", 0x2C280C));
		
		fluidList.addAll(fissionFluids("curium_243", 0x311137));
		fluidList.addAll(fissionFluids("curium_245", 0x2D102D));
		fluidList.addAll(fissionFluids("curium_246", 0x3F2442));
		fluidList.addAll(fissionFluids("curium_247", 0x321635));
		
		fluidList.addAll(fissionFluids("berkelium_247", 0x5A2301));
		fluidList.addAll(fissionFluids("berkelium_248", 0x602502));
		
		fluidList.addAll(fissionFluids("californium_249", 0x460D12));
		fluidList.addAll(fissionFluids("californium_250", 0x3E0C14));
		fluidList.addAll(fissionFluids("californium_251", 0x3B0B16));
		fluidList.addAll(fissionFluids("californium_252", 0x430B0E));
		
		fluidList.addAll(fissionFluids("fuel_tbu", 0x272727));
		
		fluidList.addAll(fissionFluids("fuel_leu_233", 0x1D321B));
		fluidList.addAll(fissionFluids("fuel_heu_233", 0x123B0D));
		fluidList.addAll(fissionFluids("fuel_leu_235", 0x1D321B));
		fluidList.addAll(fissionFluids("fuel_heu_235", 0x123B0D));
		
		fluidList.addAll(fissionFluids("fuel_len_236", 0x1B3132));
		fluidList.addAll(fissionFluids("fuel_hen_236", 0x0D393B));
		
		fluidList.addAll(fissionFluids("fuel_lep_239", 0x949494));
		fluidList.addAll(fissionFluids("fuel_hep_239", 0x8A8A8A));
		fluidList.addAll(fissionFluids("fuel_lep_241", 0x969696));
		fluidList.addAll(fissionFluids("fuel_hep_241", 0x8C8C8C));
		
		fluidList.addAll(fissionFluids("fuel_mix_239", 0x485547));
		fluidList.addAll(fissionFluids("fuel_mix_241", 0x404E3E));
		
		fluidList.addAll(fissionFluids("fuel_lea_242", 0x322C1B));
		fluidList.addAll(fissionFluids("fuel_hea_242", 0x3B2F0D));
		
		fluidList.addAll(fissionFluids("fuel_lecm_243", 0x301B32));
		fluidList.addAll(fissionFluids("fuel_hecm_243", 0x360D3B));
		fluidList.addAll(fissionFluids("fuel_lecm_245", 0x2D1A2D));
		fluidList.addAll(fissionFluids("fuel_hecm_245", 0x340B2E));
		fluidList.addAll(fissionFluids("fuel_lecm_247", 0x331F35));
		fluidList.addAll(fissionFluids("fuel_hecm_247", 0x39103E));
		
		fluidList.addAll(fissionFluids("fuel_leb_248", 0x4A1B04));
		fluidList.addAll(fissionFluids("fuel_heb_248", 0x571B00));
		
		fluidList.addAll(fissionFluids("fuel_lecf_249", 0x3D0310));
		fluidList.addAll(fissionFluids("fuel_hecf_249", 0x4A0212));
		fluidList.addAll(fissionFluids("fuel_lecf_251", 0x3A0313));
		fluidList.addAll(fissionFluids("fuel_hecf_251", 0x460215));
		
		fluidList.addAll(fissionFluids("depleted_fuel_tbu", 0x1D3826));
		
		fluidList.addAll(fissionFluids("depleted_fuel_leu_233", 0x5C5C53));
		fluidList.addAll(fissionFluids("depleted_fuel_heu_233", 0x34484B));
		fluidList.addAll(fissionFluids("depleted_fuel_leu_235", 0x3A493D));
		fluidList.addAll(fissionFluids("depleted_fuel_heu_235", 0x4D5352));
		
		fluidList.addAll(fissionFluids("depleted_fuel_len_236", 0x4E4E46));
		fluidList.addAll(fissionFluids("depleted_fuel_hen_236", 0x646663));
		
		fluidList.addAll(fissionFluids("depleted_fuel_lep_239", 0x5C545D));
		fluidList.addAll(fissionFluids("depleted_fuel_hep_239", 0x392D2F));
		fluidList.addAll(fissionFluids("depleted_fuel_lep_241", 0x473441));
		fluidList.addAll(fissionFluids("depleted_fuel_hep_241", 0x40213A));
		
		fluidList.addAll(fissionFluids("depleted_fuel_mix_239", 0x2F342C));
		fluidList.addAll(fissionFluids("depleted_fuel_mix_241", 0x2F342F));
		
		fluidList.addAll(fissionFluids("depleted_fuel_lea_242", 0x311830));
		fluidList.addAll(fissionFluids("depleted_fuel_hea_242", 0x391C28));
		
		fluidList.addAll(fissionFluids("depleted_fuel_lecm_243", 0x441F1F));
		fluidList.addAll(fissionFluids("depleted_fuel_hecm_243", 0x401920));
		fluidList.addAll(fissionFluids("depleted_fuel_lecm_245", 0x49221A));
		fluidList.addAll(fissionFluids("depleted_fuel_hecm_245", 0x462119));
		fluidList.addAll(fissionFluids("depleted_fuel_lecm_247", 0x431512));
		fluidList.addAll(fissionFluids("depleted_fuel_hecm_247", 0x441415));
		
		fluidList.addAll(fissionFluids("depleted_fuel_leb_248", 0x471B22));
		fluidList.addAll(fissionFluids("depleted_fuel_heb_248", 0x481F26));
		
		fluidList.addAll(fissionFluids("depleted_fuel_lecf_249", 0x4B1B23));
		fluidList.addAll(fissionFluids("depleted_fuel_hecf_249", 0x491A22));
		fluidList.addAll(fissionFluids("depleted_fuel_lecf_251", 0x4A1B23));
		fluidList.addAll(fissionFluids("depleted_fuel_hecf_251", 0x4B1B23));
	}
	
	public static <T extends NCBlockFluid> Block withName(T block) {
		return block.setTranslationKey(Global.MOD_ID + "." + block.getBlockName()).setRegistryName(new ResourceLocation(Global.MOD_ID, block.getBlockName()));
	}
	
	public static void register() {
		for (FluidFission fluid : fluidList) {
			
			boolean defaultFluid = FluidRegistry.registerFluid(fluid);
			if (!defaultFluid) {
				Fluid fluidDefault = FluidRegistry.getFluid(fluid.getName());
				FluidRegistry.addBucketForFluid(fluidDefault);
				if (NCConfig.register_fission_fluid_blocks) registerBlock(new BlockFluidFission(fluidDefault));
			}
			else {
				FluidRegistry.addBucketForFluid(fluid);
				if (NCConfig.register_fission_fluid_blocks) registerBlock(new BlockFluidFission(fluid));
			}
		}
	}
	
	public static void registerBlock(NCBlockFluid block) {
		ForgeRegistries.BLOCKS.register(withName(block));
		ForgeRegistries.ITEMS.register(new NCItemBlock(block, TextFormatting.AQUA).setRegistryName(block.getRegistryName()));
		NuclearCraft.proxy.registerFluidBlockRendering(block, "fluid_molten_colored");
	}
	
	public static List<FluidFission> fissionFluids(String name, int color) {
		List<FluidFission> fluidList = new ArrayList<FluidFission>();
		fluidList.add(new FluidFission(name, color));
		fluidList.add(new FluidFission(name + "_fluoride", FluidFission.getFluorideColor(color)));
		fluidList.add(new FluidFission(name + "_fluoride_flibe", FluidFission.getFLIBEColor(color)));
		return fluidList;
	}
}
