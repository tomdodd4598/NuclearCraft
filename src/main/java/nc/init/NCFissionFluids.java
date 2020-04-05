package nc.init;

import static nc.config.NCConfig.register_fluid_blocks;

import java.util.ArrayList;
import java.util.List;

import nc.Global;
import nc.NuclearCraft;
import nc.block.fluid.BlockFluidFission;
import nc.block.fluid.NCBlockFluid;
import nc.block.item.NCItemBlock;
import nc.fluid.FluidFission;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class NCFissionFluids {
	
	public static List<FluidFission> fluidList = new ArrayList<>();
	
	public static void init() {
		fluidList.add(new FluidFission("thorium", 0x242424));
		fluidList.add(new FluidFission("uranium", 0x375437));
		
		addIsotopeFluids("uranium_233", 0x212E20);
		addIsotopeFluids("uranium_235", 0x102D10);
		addIsotopeFluids("uranium_238", 0x333E32);
		
		addIsotopeFluids("neptunium_236", 0x293E40);
		addIsotopeFluids("neptunium_237", 0x2E3A42);
		
		addIsotopeFluids("plutonium_238", 0x999999);
		addIsotopeFluids("plutonium_239", 0x9E9E9E);
		addIsotopeFluids("plutonium_241", 0x909090);
		addIsotopeFluids("plutonium_242", 0xABABAB);
		
		addIsotopeFluids("americium_241", 0x393725);
		addIsotopeFluids("americium_242", 0x332D1A);
		addIsotopeFluids("americium_243", 0x2C280C);
		
		addIsotopeFluids("curium_243", 0x311137);
		addIsotopeFluids("curium_245", 0x2D102D);
		addIsotopeFluids("curium_246", 0x3F2442);
		addIsotopeFluids("curium_247", 0x321635);
		
		addIsotopeFluids("berkelium_247", 0x5A2301);
		addIsotopeFluids("berkelium_248", 0x602502);
		
		addIsotopeFluids("californium_249", 0x460D12);
		addIsotopeFluids("californium_250", 0x3E0C14);
		addIsotopeFluids("californium_251", 0x3B0B16);
		addIsotopeFluids("californium_252", 0x430B0E);
		
		addFuelFluids("tbu", 0x272727);
		//fluidList.add(new FluidFission("tbu_za", FluidFission.getZAColor(0x272727)));
		
		addFuelFluids("leu_233", 0x1D321B);
		addFuelFluids("heu_233", 0x123B0D);
		addFuelFluids("leu_235", 0x1D321B);
		addFuelFluids("heu_235", 0x123B0D);
		
		addFuelFluids("len_236", 0x1B3132);
		addFuelFluids("hen_236", 0x0D393B);
		
		addFuelFluids("lep_239", 0x949494);
		addFuelFluids("hep_239", 0x8A8A8A);
		addFuelFluids("lep_241", 0x969696);
		addFuelFluids("hep_241", 0x8C8C8C);
		
		addFuelFluids("mix_239", 0x485547);
		addFuelFluids("mix_241", 0x404E3E);
		
		addFuelFluids("lea_242", 0x322C1B);
		addFuelFluids("hea_242", 0x3B2F0D);
		
		addFuelFluids("lecm_243", 0x301B32);
		addFuelFluids("hecm_243", 0x360D3B);
		addFuelFluids("lecm_245", 0x2D1A2D);
		addFuelFluids("hecm_245", 0x340B2E);
		addFuelFluids("lecm_247", 0x331F35);
		addFuelFluids("hecm_247", 0x39103E);
		
		addFuelFluids("leb_248", 0x4A1B04);
		addFuelFluids("heb_248", 0x571B00);
		
		addFuelFluids("lecf_249", 0x3D0310);
		addFuelFluids("hecf_249", 0x4A0212);
		addFuelFluids("lecf_251", 0x3A0313);
		addFuelFluids("hecf_251", 0x460215);
		
		addFuelFluids("depleted_tbu", 0x1D3826);
		//fluidList.add(new FluidFission("depleted_tbu_za", FluidFission.getZAColor(0x1D3826)));
		
		addFuelFluids("depleted_leu_233", 0x5C5C53);
		addFuelFluids("depleted_heu_233", 0x34484B);
		addFuelFluids("depleted_leu_235", 0x3A493D);
		addFuelFluids("depleted_heu_235", 0x4D5352);
		
		addFuelFluids("depleted_len_236", 0x4E4E46);
		addFuelFluids("depleted_hen_236", 0x646663);
		
		addFuelFluids("depleted_lep_239", 0x5C545D);
		addFuelFluids("depleted_hep_239", 0x392D2F);
		addFuelFluids("depleted_lep_241", 0x473441);
		addFuelFluids("depleted_hep_241", 0x40213A);
		
		addFuelFluids("depleted_mix_239", 0x2F342C);
		addFuelFluids("depleted_mix_241", 0x2F342F);
		
		addFuelFluids("depleted_lea_242", 0x311830);
		addFuelFluids("depleted_hea_242", 0x391C28);
		
		addFuelFluids("depleted_lecm_243", 0x441F1F);
		addFuelFluids("depleted_hecm_243", 0x401920);
		addFuelFluids("depleted_lecm_245", 0x49221A);
		addFuelFluids("depleted_hecm_245", 0x462119);
		addFuelFluids("depleted_lecm_247", 0x431512);
		addFuelFluids("depleted_hecm_247", 0x441415);
		
		addFuelFluids("depleted_leb_248", 0x471B22);
		addFuelFluids("depleted_heb_248", 0x481F26);
		
		addFuelFluids("depleted_lecf_249", 0x4B1B23);
		addFuelFluids("depleted_hecf_249", 0x491A22);
		addFuelFluids("depleted_lecf_251", 0x4A1B23);
		addFuelFluids("depleted_hecf_251", 0x4B1B23);
	}
	
	private static void addIsotopeFluids(String name, int color) {
		fluidList.add(new FluidFission(name, color));
		//fluidList.add(new FluidFission(name + "_za", FluidFission.getZAColor(color)));
	}
	
	private static void addFuelFluids(String name, int color) {
		fluidList.add(new FluidFission(name, color));
		fluidList.add(new FluidFission(name + "_fluoride", FluidFission.getFluorideColor(color)));
		fluidList.add(new FluidFission(name + "_fluoride_flibe", FluidFission.getFLIBEColor(color)));
		//fluidList.add(new FluidFission(name + "_za", FluidFission.getZAColor(color)));
	}
	
	public static void register() {
		for (Fluid fluid : fluidList) {
			if (!FluidRegistry.registerFluid(fluid)) {
				fluid = FluidRegistry.getFluid(fluid.getName());
			}
			FluidRegistry.addBucketForFluid(fluid);
			if (register_fluid_blocks) {
				registerBlock(new BlockFluidFission(fluid));
			}
		}
	}
	
	private static void registerBlock(NCBlockFluid block) {
		ForgeRegistries.BLOCKS.register(withName(block));
		ForgeRegistries.ITEMS.register(new NCItemBlock(block, TextFormatting.AQUA).setRegistryName(block.getRegistryName()));
		NuclearCraft.proxy.registerFluidBlockRendering(block, "fluid_molten_colored");
	}
	
	private static <T extends NCBlockFluid> Block withName(T block) {
		return block.setTranslationKey(Global.MOD_ID + "." + block.name).setRegistryName(new ResourceLocation(Global.MOD_ID, block.name));
	}
}
