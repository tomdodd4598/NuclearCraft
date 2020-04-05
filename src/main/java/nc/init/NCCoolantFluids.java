package nc.init;

import static nc.config.NCConfig.register_cofh_fluids;
import static nc.config.NCConfig.register_fluid_blocks;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import nc.Global;
import nc.ModCheck;
import nc.NuclearCraft;
import nc.block.fluid.BlockFluidCoolant;
import nc.block.fluid.BlockFluidHotCoolant;
import nc.block.fluid.BlockFluidMolten;
import nc.block.fluid.NCBlockFluid;
import nc.block.item.NCItemBlock;
import nc.fluid.FluidCoolant;
import nc.fluid.FluidHotCoolant;
import nc.fluid.FluidMolten;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class NCCoolantFluids {
	
	private static List<Pair<Fluid, NCBlockFluid>> fluidPairList = new ArrayList<>();
	public static List<Fluid> fluidList = new ArrayList<>();
	
	public static final List<String> COOLANTS = Lists.newArrayList("");
	
	public static void init() {
		addMoltenPair("iron", 0x8D1515);
		addNAKPairs("iron", 0x8D1515);
		
		if (registerCoFHAlt()) {
			addMoltenPair("redstone", 0xAB1C09);
		}
		addNAKPairs("redstone", 0xAB1C09);
		
		addMoltenPair("quartz", 0xECE9E2);
		addNAKPairs("quartz", 0xECE9E2);
		
		addMoltenPair("obsidian", 0x1C1828);
		addNAKPairs("obsidian", 0x1C1828);
		
		addMoltenPair("nether_brick", 0x271317);
		addNAKPairs("nether_brick", 0x271317);
		
		if (registerCoFHAlt()) {
			addMoltenPair("glowstone", 0xA38037);
		}
		addNAKPairs("glowstone", 0xA38037);
		
		addMoltenPair("lapis", 0x27438A);
		addNAKPairs("lapis", 0x27438A);
		
		addMoltenPair("gold", 0xE6DA3C);
		addNAKPairs("gold", 0xE6DA3C);
		
		addMoltenPair("prismarine", 0x70A695);
		addNAKPairs("prismarine", 0x70A695);
		
		addMoltenPair("slime", 0x79C865);
		addNAKPairs("slime", 0x79C865);
		
		addMoltenPair("end_stone", 0xE7E9B3);
		addNAKPairs("end_stone", 0xE7E9B3);
		
		addMoltenPair("purpur", 0xA979A9);
		addNAKPairs("purpur", 0xA979A9);
		
		addMoltenPair("diamond", 0x6FDFDA);
		addNAKPairs("diamond", 0x6FDFDA);
		
		addMoltenPair("emerald", 0x51D975);
		addNAKPairs("emerald", 0x51D975);
		
		addMoltenPair("copper", 0xAD6544);
		addNAKPairs("copper", 0xAD6544);
		
		addMoltenPair("tin", 0xD9DDF0);
		addNAKPairs("tin", 0xD9DDF0);
		
		addMoltenPair("lead", 0x3F4C4C);
		addNAKPairs("lead", 0x3F4C4C);
		
		addMoltenPair("boron", 0x7D7D7D);
		addNAKPairs("boron", 0x7D7D7D);
		
		addMoltenPair("lithium", 0xEFEFEF);
		addNAKPairs("lithium", 0xEFEFEF);
		
		addMoltenPair("magnesium", 0xEED5E1);
		addNAKPairs("magnesium", 0xEED5E1);
		
		addMoltenPair("manganese", 0x99A1CA);
		addNAKPairs("manganese", 0x99A1CA);
		
		addMoltenPair("aluminum", 0xB5ECD5);
		addNAKPairs("aluminum", 0xB5ECD5);
		
		addMoltenPair("silver", 0xE2DAF6);
		addNAKPairs("silver", 0xE2DAF6);
		
		addMoltenPair("fluorite", 0x8AB492);
		addNAKPairs("fluorite", 0x8AB492);
		
		addMoltenPair("villiaumite", 0xB06C56);
		addNAKPairs("villiaumite", 0xB06C56);
		
		addMoltenPair("carobbiite", 0x95A251);
		addNAKPairs("carobbiite", 0x95A251);
		
		addNAKPairs("arsenic", 0x818475);
		
		addNAKPairs("liquid_nitrogen", 0x31C23A);
		
		addNAKPairs("liquid_helium", 0xF3433D);
		
		addNAKPairs("enderium", 0x0B5B5C);
		
		addNAKPairs("cryotheum", 0x0099C1);
	}
	
	private static void addMoltenPair(String name, int color) {
		FluidMolten fluid = new FluidMolten(name, color);
		fluidPairList.add(Pair.of(fluid, register_fluid_blocks ? new BlockFluidMolten(fluid) : null));
	}
	
	private static void addNAKPairs(String name, int color) {
		COOLANTS.add(name + "_");
		FluidCoolant coolant = new FluidCoolant(name + "_nak", FluidCoolant.getNAKColor(color));
		fluidPairList.add(Pair.of(coolant, register_fluid_blocks ? new BlockFluidCoolant(coolant) : null));
		FluidHotCoolant hotCoolant = new FluidHotCoolant(name + "_nak_hot", FluidHotCoolant.getNAKColor(color));
		fluidPairList.add(Pair.of(hotCoolant, register_fluid_blocks ? new BlockFluidHotCoolant(hotCoolant) : null));
	}
	
	public static void register() {
		for (Pair<Fluid, NCBlockFluid> fluidPair : fluidPairList) {
			
			Fluid fluid = fluidPair.getLeft();
			if (!FluidRegistry.registerFluid(fluid)) {
				fluid = FluidRegistry.getFluid(fluid.getName());
			}
			FluidRegistry.addBucketForFluid(fluid);
			fluidList.add(fluid);
			
			NCBlockFluid fluidBlock = fluidPair.getRight();
			if (fluidBlock != null) {
				registerBlock(fluidBlock);
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
	
	private static boolean registerCoFHAlt() {
		return register_cofh_fluids || !ModCheck.thermalFoundationLoaded();
	}
}
