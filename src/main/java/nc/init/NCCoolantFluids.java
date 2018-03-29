package nc.init;

import java.util.ArrayList;
import java.util.List;

import nc.NuclearCraft;
import nc.block.fluid.BlockFluidBase;
import nc.block.fluid.BlockFluidCoolant;
import nc.block.fluid.BlockFluidCryotheum;
import nc.block.fluid.BlockFluidGlowstone;
import nc.block.fluid.BlockFluidMolten;
import nc.config.NCConfig;
import nc.fluid.FluidBase;
import nc.fluid.FluidCoolant;
import nc.fluid.FluidCryotheum;
import nc.fluid.FluidGlowstone;
import nc.fluid.FluidMolten;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class NCCoolantFluids {
	
	public static List<FluidBase> fluidList = new ArrayList<FluidBase>();
	public static List<BlockFluidBase> blockList = new ArrayList<BlockFluidBase>();
	
	static {
		if (NCConfig.register_cofh_fluids) {
			FluidMolten redstone = new FluidMolten("redstone", 0xAB1C09);
			fluidList.add(redstone);
			blockList.add(new BlockFluidMolten(redstone));
		}
		
		FluidCoolant redstone_nak = new FluidCoolant("redstone_nak", FluidCoolant.getNAKColor(0xAB1C09));
		fluidList.add(redstone_nak);
		blockList.add(new BlockFluidCoolant(redstone_nak));
		
		FluidMolten quartz = new FluidMolten("quartz", 0xECE9E2);
		fluidList.add(quartz);
		blockList.add(new BlockFluidMolten(quartz));
		
		FluidCoolant quartz_nak = new FluidCoolant("quartz_nak", FluidCoolant.getNAKColor(0xECE9E2));
		fluidList.add(quartz_nak);
		blockList.add(new BlockFluidCoolant(quartz_nak));
		
		FluidMolten gold = new FluidMolten("gold", 0xE6DA3C);
		fluidList.add(gold);
		blockList.add(new BlockFluidMolten(gold));
		
		FluidCoolant gold_nak = new FluidCoolant("gold_nak", FluidCoolant.getNAKColor(0xE6DA3C));
		fluidList.add(gold_nak);
		blockList.add(new BlockFluidCoolant(gold_nak));
		
		if (NCConfig.register_cofh_fluids) {
			FluidGlowstone glowstone = new FluidGlowstone("glowstone", 0xA38037);
			fluidList.add(glowstone);
			blockList.add(new BlockFluidGlowstone(glowstone));
		}
		
		FluidCoolant glowstone_nak = new FluidCoolant("glowstone_nak", FluidCoolant.getNAKColor(0xA38037));
		fluidList.add(glowstone_nak);
		blockList.add(new BlockFluidCoolant(glowstone_nak));
		
		FluidMolten lapis = new FluidMolten("lapis", 0x27438A);
		fluidList.add(new FluidMolten("lapis", 0x27438A));
		blockList.add(new BlockFluidMolten(lapis));
		
		FluidCoolant lapis_nak = new FluidCoolant("lapis_nak", FluidCoolant.getNAKColor(0x27438A));
		fluidList.add(lapis_nak);
		blockList.add(new BlockFluidCoolant(lapis_nak));
		
		FluidMolten diamond = new FluidMolten("diamond", 0x6FDFDA);
		fluidList.add(new FluidMolten("diamond", 0x6FDFDA));
		blockList.add(new BlockFluidMolten(diamond));
		
		FluidCoolant diamond_nak = new FluidCoolant("diamond_nak", FluidCoolant.getNAKColor(0x6FDFDA));
		fluidList.add(diamond_nak);
		blockList.add(new BlockFluidCoolant(diamond_nak));
		
		FluidCoolant liquidhelium_nak = new FluidCoolant("liquidhelium_nak", FluidCoolant.getNAKColor(0xF3433D));
		fluidList.add(liquidhelium_nak);
		blockList.add(new BlockFluidCoolant(liquidhelium_nak));
		
		if (NCConfig.register_cofh_fluids) {
			FluidMolten ender = new FluidMolten("ender", 0x14584D);
			fluidList.add(ender);
			blockList.add(new BlockFluidMolten(ender));
		}
		
		FluidCoolant ender_nak = new FluidCoolant("ender_nak", FluidCoolant.getNAKColor(0x14584D));
		fluidList.add(ender_nak);
		blockList.add(new BlockFluidCoolant(ender_nak));
		
		if (NCConfig.register_cofh_fluids) {
			FluidCryotheum cryotheum = new FluidCryotheum("cryotheum", 0x0099C1);
			fluidList.add(cryotheum);
			blockList.add(new BlockFluidCryotheum(cryotheum));
		}
		
		FluidCoolant cryotheum_nak = new FluidCoolant("cryotheum_nak", FluidCoolant.getNAKColor(0x0099C1));
		fluidList.add(cryotheum_nak);
		blockList.add(new BlockFluidCoolant(cryotheum_nak));
		
		FluidMolten iron = new FluidMolten("iron", 0x8D1515);
		fluidList.add(new FluidMolten("iron", 0x8D1515));
		blockList.add(new BlockFluidMolten(iron));
		
		FluidCoolant iron_nak = new FluidCoolant("iron_nak", FluidCoolant.getNAKColor(0x8D1515));
		fluidList.add(iron_nak);
		blockList.add(new BlockFluidCoolant(iron_nak));
		
		FluidMolten emerald = new FluidMolten("emerald", 0x51D975);
		fluidList.add(emerald);
		blockList.add(new BlockFluidMolten(emerald));
		
		FluidCoolant emerald_nak = new FluidCoolant("emerald_nak", FluidCoolant.getNAKColor(0x51D975));
		fluidList.add(emerald_nak);
		blockList.add(new BlockFluidCoolant(emerald_nak));
		
		FluidMolten copper = new FluidMolten("copper", 0x5C2F1A);
		fluidList.add(copper);
		blockList.add(new BlockFluidMolten(copper));
		
		FluidCoolant copper_nak = new FluidCoolant("copper_nak", FluidCoolant.getNAKColor(0x5C2F1A));
		fluidList.add(copper_nak);
		blockList.add(new BlockFluidCoolant(copper_nak));
		
		FluidMolten tin = new FluidMolten("tin", 0xD9DDF0);
		fluidList.add(tin);
		blockList.add(new BlockFluidMolten(tin));
		
		FluidCoolant tin_nak = new FluidCoolant("tin_nak", FluidCoolant.getNAKColor(0xD9DDF0));
		fluidList.add(tin_nak);
		blockList.add(new BlockFluidCoolant(tin_nak));
		
		FluidMolten magnesium = new FluidMolten("magnesium", 0xEED5E1);
		fluidList.add(magnesium);
		blockList.add(new BlockFluidMolten(magnesium));
		
		FluidCoolant magnesium_nak = new FluidCoolant("magnesium_nak", FluidCoolant.getNAKColor(0xEED5E1));
		fluidList.add(magnesium_nak);
		blockList.add(new BlockFluidCoolant(magnesium_nak));
	}
	
	public static void register() {
		for (BlockFluidBase block : blockList) if (NCConfig.register_fission_fluid_blocks) registerBlock(block);
	}
	
	public static void registerBlock(BlockFluidBase block) {
		ForgeRegistries.BLOCKS.register(block);
		ForgeRegistries.ITEMS.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
		NuclearCraft.proxy.registerFluidBlockRendering(block, "fluid_molten_colored");
	}
}
