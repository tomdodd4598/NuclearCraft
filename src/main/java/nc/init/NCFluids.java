package nc.init;

import nc.NuclearCraft;
import nc.block.fluid.BlockFluid;
import nc.block.fluid.BlockFluidGas;
import nc.block.fluid.BlockFluidMolten;
import nc.block.fluid.BlockFluidParticle;
import nc.block.fluid.BlockFluidPlasma;
import nc.block.fluid.BlockFluidSteam;
import nc.block.fluid.BlockSuperFluid;
import nc.block.fluid.FluidGas;
import nc.block.fluid.FluidMolten;
import nc.block.fluid.FluidParticle;
import nc.block.fluid.FluidPlasma;
import nc.block.fluid.SuperFluid;
import nc.util.NCUtils;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class NCFluids {
	
	public static Fluid oxygen;
	public static BlockFluid block_oxygen;
	
	public static Fluid hydrogen;
	public static BlockFluid block_hydrogen;
	
	public static Fluid deuterium;
	public static BlockFluid block_deuterium;
	
	public static Fluid tritium;
	public static BlockFluid block_tritium;
	
	public static Fluid helium3;
	public static BlockFluid block_helium3;
	
	public static Fluid helium;
	public static BlockFluid block_helium;
	
	public static Fluid steam;
	public static BlockFluid block_steam;
	
	public static Fluid liquidhelium;
	public static BlockFluid block_liquidhelium;
	
	public static Fluid plasma;
	public static BlockFluid block_plasma;
	
	public static Fluid lithium6;
	public static BlockFluid block_lithium6;
	
	public static Fluid lithium7;
	public static BlockFluid block_lithium7;
	
	public static Fluid boron11;
	public static BlockFluid block_boron11;
	
	public static Fluid neutron;
	public static BlockFluid block_neutron;
	
	public static void init() {
		oxygen = new FluidGas("oxygen");
		FluidRegistry.addBucketForFluid(oxygen);
		block_oxygen = new BlockFluidGas(oxygen, "fluid_oxygen");
		
		hydrogen = new FluidGas("hydrogen");
		FluidRegistry.addBucketForFluid(hydrogen);
		block_hydrogen = new BlockFluidGas(hydrogen, "fluid_hydrogen");
		
		deuterium = new FluidGas("deuterium");
		FluidRegistry.addBucketForFluid(deuterium);
		block_deuterium = new BlockFluidGas(deuterium, "fluid_deuterium");
		
		tritium = new FluidGas("tritium");
		FluidRegistry.addBucketForFluid(tritium);
		block_tritium = new BlockFluidGas(tritium, "fluid_tritium");
		
		helium3 = new FluidGas("helium3");
		FluidRegistry.addBucketForFluid(helium3);
		block_helium3 = new BlockFluidGas(helium3, "fluid_helium3");
		
		helium = new FluidGas("helium");
		FluidRegistry.addBucketForFluid(helium);
		block_helium = new BlockFluidGas(helium, "fluid_helium");
		
		steam = new FluidGas("steam");
		FluidRegistry.addBucketForFluid(steam);
		block_steam = new BlockFluidSteam(steam, "fluid_steam");
		
		liquidhelium = new SuperFluid("liquidhelium");
		FluidRegistry.addBucketForFluid(liquidhelium);
		block_liquidhelium = new BlockSuperFluid(liquidhelium, "fluid_liquidhelium");
		
		plasma = new FluidPlasma("plasma");
		FluidRegistry.addBucketForFluid(plasma);
		block_plasma = new BlockFluidPlasma(plasma, "fluid_plasma");
		
		lithium6 = new FluidMolten("lithium6");
		FluidRegistry.addBucketForFluid(lithium6);
		block_lithium6 = new BlockFluidMolten(lithium6, "fluid_lithium6");
		
		lithium7 = new FluidMolten("lithium7");
		FluidRegistry.addBucketForFluid(lithium7);
		block_lithium7 = new BlockFluidMolten(lithium7, "fluid_lithium7");
		
		boron11 = new FluidMolten("boron11");
		FluidRegistry.addBucketForFluid(boron11);
		block_boron11 = new BlockFluidMolten(boron11, "fluid_boron11");
		
		neutron = new FluidParticle("neutron");
		FluidRegistry.addBucketForFluid(neutron);
		block_neutron = new BlockFluidParticle(neutron, "fluid_neutron");
	}
	
	public static void register() {
		registerBlock(block_oxygen);
		registerBlock(block_hydrogen);
		registerBlock(block_deuterium);
		registerBlock(block_tritium);
		registerBlock(block_helium3);
		registerBlock(block_helium);
		registerBlock(block_steam);
		registerBlock(block_liquidhelium);
		registerBlock(block_plasma);
		registerBlock(block_lithium6);
		registerBlock(block_lithium7);
		registerBlock(block_boron11);
		registerBlock(block_neutron);
	}
	
	public static void registerBlock(BlockFluid block) {
		//block.setRegistryName(name);
		GameRegistry.register(block);
		GameRegistry.register(new ItemBlock(block), block.getRegistryName());
		NuclearCraft.proxy.registerFluidBlockRendering(block, block.getName());
		NCUtils.getLogger().info("Registered fluid " + block.getUnlocalizedName().substring(5));
	}
}
