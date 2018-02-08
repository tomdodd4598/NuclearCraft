package nc.init;

import java.util.ArrayList;
import java.util.List;

import nc.NuclearCraft;
import nc.block.fluid.BlockFluid;
import nc.block.fluid.BlockFluidFission;
import nc.config.NCConfig;
import nc.fluid.FluidFission;
import nc.util.ArrayHelper;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class NCFissionFluids {
	
	public static List<FluidFission> fluidList = new ArrayList<FluidFission>();
	
	static {
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("thorium", 0x242424));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("uranium", 0x375437));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("plutonium", 0xDBDBDB));
		
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("thorium_230", 0x383838));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("thorium_232", 0x303030));
		
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("uranium_233", 0x212E20));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("uranium_235", 0x102D10));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("uranium_238", 0x333E32));
		
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("neptunium_236", 0x293E40));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("neptunium_237", 0x2E3A42));
		
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("plutonium_238", 0x999999));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("plutonium_239", 0x9E9E9E));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("plutonium_241", 0x909090));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("plutonium_242", 0xABABAB));
		
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("americium_241", 0x393725));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("americium_242", 0x332D1A));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("americium_243", 0x2C280C));
		
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("curium_243", 0x311137));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("curium_245", 0x2D102D));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("curium_246", 0x3F2442));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("curium_247", 0x321635));
		
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("berkelium_247", 0x5A2301));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("berkelium_248", 0x602502));
		
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("californium_249", 0x460D12));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("californium_250", 0x3E0C14));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("californium_251", 0x3B0B16));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("californium_252", 0x430B0E));
		
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("fuel_tbu", 0x272727));
		
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("fuel_leu_233", 0x1D321B));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("fuel_heu_233", 0x123B0D));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("fuel_leu_235", 0x1D321B));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("fuel_heu_235", 0x123B0D));
		
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("fuel_len_236", 0x1B3132));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("fuel_hen_236", 0x0D393B));
		
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("fuel_lep_239", 0x949494));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("fuel_hep_239", 0x8A8A8A));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("fuel_lep_241", 0x969696));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("fuel_hep_241", 0x8C8C8C));
		
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("fuel_lea_242", 0x322C1B));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("fuel_hea_242", 0x3B2F0D));
		
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("fuel_lecm_243", 0x301B32));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("fuel_hecm_243", 0x360D3B));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("fuel_lecm_245", 0x2D1A2D));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("fuel_hecm_245", 0x340B2E));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("fuel_lecm_247", 0x331F35));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("fuel_hecm_247", 0x39103E));
		
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("fuel_leb_248", 0x4A1B04));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("fuel_heb_248", 0x571B00));
		
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("fuel_lecf_249", 0x3D0310));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("fuel_hecf_249", 0x4A0212));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("fuel_lecf_251", 0x3A0313));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("fuel_hecf_251", 0x460215));
		
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("depleted_fuel_tbu", 0x1D3826));
		
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("depleted_fuel_leu_233", 0x5C5C53));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("depleted_fuel_heu_233", 0x34484B));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("depleted_fuel_leu_235", 0x3A493D));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("depleted_fuel_heu_235", 0x4D5352));
		
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("depleted_fuel_len_236", 0x4E4E46));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("depleted_fuel_hen_236", 0x646663));
		
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("depleted_fuel_lep_239", 0x5C545D));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("depleted_fuel_hep_239", 0x392D2F));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("depleted_fuel_lep_241", 0x473441));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("depleted_fuel_hep_241", 0x40213A));
		
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("depleted_fuel_lea_242", 0x311830));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("depleted_fuel_hea_242", 0x391C28));
		
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("depleted_fuel_lecm_243", 0x441F1F));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("depleted_fuel_hecm_243", 0x401920));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("depleted_fuel_lecm_245", 0x49221A));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("depleted_fuel_hecm_245", 0x462119));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("depleted_fuel_lecm_247", 0x431512));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("depleted_fuel_hecm_247", 0x441415));
		
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("depleted_fuel_leb_248", 0x471B22));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("depleted_fuel_heb_248", 0x481F26));
		
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("depleted_fuel_lecf_249", 0x4B1B23));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("depleted_fuel_hecf_249", 0x491A22));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("depleted_fuel_lecf_251", 0x4A1B23));
		fluidList = ArrayHelper.concatenate(fluidList, fissionFluids("depleted_fuel_hecf_251", 0x4B1B23));
	}
	
	public static void register() {
		for (FluidFission fluid : fluidList) if (NCConfig.register_fission_fluid_blocks) registerBlock(new BlockFluidFission(fluid));
	}
	
	public static void registerBlock(BlockFluid block) {
		//block.setRegistryName(name);
		ForgeRegistries.BLOCKS.register(block);
		ForgeRegistries.ITEMS.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
		NuclearCraft.proxy.registerFluidBlockRendering(block, "fluid_fission");
	}
	
	public static List<FluidFission> fissionFluids(String name, int color) {
		List<FluidFission> fluidList = new ArrayList<FluidFission>();
		fluidList.add(new FluidFission(name, color));
		fluidList.add(new FluidFission(name + "_fluoride", FluidFission.getFluorideColor(color)));
		fluidList.add(new FluidFission(name + "_fluoride_flibe", FluidFission.getFLIBEColor(color)));
		return fluidList;
	}
}
