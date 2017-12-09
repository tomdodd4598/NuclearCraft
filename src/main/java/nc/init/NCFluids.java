package nc.init;

import nc.NuclearCraft;
import nc.block.fluid.BlockFluid;
import nc.block.fluid.BlockFluidAcid;
import nc.block.fluid.BlockFluidFlammable;
import nc.block.fluid.BlockFluidGas;
import nc.block.fluid.BlockFluidLiquid;
import nc.block.fluid.BlockFluidMolten;
import nc.block.fluid.BlockFluidParticle;
import nc.block.fluid.BlockFluidPlasma;
import nc.block.fluid.BlockFluidSteam;
import nc.block.fluid.BlockSuperFluid;
import nc.block.fluid.FluidAcid;
import nc.block.fluid.FluidFlammable;
import nc.block.fluid.FluidGas;
import nc.block.fluid.FluidLiquid;
import nc.block.fluid.FluidMolten;
import nc.block.fluid.FluidParticle;
import nc.block.fluid.FluidPlasma;
import nc.block.fluid.SuperFluid;
import nc.util.NCUtil;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

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
	
	public static Fluid boron10;
	public static BlockFluid block_boron10;
	
	public static Fluid boron11;
	public static BlockFluid block_boron11;
	
	public static Fluid neutron;
	public static BlockFluid block_neutron;
	
	public static Fluid fluorine;
	public static BlockFluid block_fluorine;
	
	public static Fluid nitrogen;
	public static BlockFluid block_nitrogen;
	
	public static Fluid ammonia;
	public static BlockFluid block_ammonia;
	
	public static Fluid ethanol;
	public static BlockFluid block_ethanol;
	
	public static Fluid boric_acid;
	public static BlockFluid block_boric_acid;
	
	public static Fluid diborane;
	public static BlockFluid block_diborane;
	
	public static Fluid radaway;
	public static BlockFluid block_radaway;
	
	public static Fluid beryllium;
	public static BlockFluid block_beryllium;
	
	public static Fluid lif;
	public static BlockFluid block_lif;
	
	public static Fluid bef2;
	public static BlockFluid block_bef2;
	
	public static Fluid flibe;
	public static BlockFluid block_flibe;
	
	public static Fluid boron_nitride_solution;
	public static BlockFluid block_boron_nitride_solution;
	
	public static Fluid coolant;
	public static BlockFluid block_coolant;
	
	public static Fluid sulfur;
	public static BlockFluid block_sulfur;
	
	public static Fluid sulfur_dioxide;
	public static BlockFluid block_sulfur_dioxide;
	
	public static Fluid sulfur_trioxide;
	public static BlockFluid block_sulfur_trioxide;
	
	public static Fluid sulfuric_acid;
	public static BlockFluid block_sulfuric_acid;
	
	public static Fluid fluorite_water;
	public static BlockFluid block_fluorite_water;
	
	public static Fluid calcium_sulfate_solution;
	public static BlockFluid block_calcium_sulfate_solution;
	
	public static Fluid hydrofluoric_acid;
	public static BlockFluid block_hydrofluoric_acid;
	
	public static Fluid oxygen_difluoride;
	public static BlockFluid block_oxygen_difluoride;
	
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
		
		boron10 = new FluidMolten("boron10");
		FluidRegistry.addBucketForFluid(boron10);
		block_boron10 = new BlockFluidMolten(boron10, "fluid_boron10");
		
		boron11 = new FluidMolten("boron11");
		FluidRegistry.addBucketForFluid(boron11);
		block_boron11 = new BlockFluidMolten(boron11, "fluid_boron11");
		
		neutron = new FluidParticle("neutron");
		FluidRegistry.addBucketForFluid(neutron);
		block_neutron = new BlockFluidParticle(neutron, "fluid_neutron");
		
		fluorine = new FluidGas("fluorine");
		FluidRegistry.addBucketForFluid(fluorine);
		block_fluorine = new BlockFluidGas(fluorine, "fluid_fluorine");
		
		nitrogen = new FluidGas("nitrogen");
		FluidRegistry.addBucketForFluid(nitrogen);
		block_nitrogen = new BlockFluidGas(nitrogen, "fluid_nitrogen");
		
		ammonia = new FluidGas("ammonia");
		FluidRegistry.addBucketForFluid(ammonia);
		block_ammonia = new BlockFluidGas(ammonia, "fluid_ammonia");
		
		ethanol = new FluidFlammable("ethanol");
		FluidRegistry.addBucketForFluid(ethanol);
		block_ethanol = new BlockFluidFlammable(ethanol, "fluid_ethanol");
		
		boric_acid = new FluidAcid("boric_acid");
		FluidRegistry.addBucketForFluid(boric_acid);
		block_boric_acid = new BlockFluidAcid(boric_acid, "fluid_boric_acid");
		
		diborane = new FluidGas("diborane");
		FluidRegistry.addBucketForFluid(diborane);
		block_diborane = new BlockFluidGas(diborane, "fluid_diborane");
		
		radaway = new FluidLiquid("radaway");
		FluidRegistry.addBucketForFluid(radaway);
		block_radaway = new BlockFluidLiquid(radaway, "fluid_radaway");
		
		beryllium = new FluidMolten("beryllium");
		FluidRegistry.addBucketForFluid(beryllium);
		block_beryllium = new BlockFluidMolten(beryllium, "fluid_beryllium");
		
		lif = new FluidMolten("lif");
		FluidRegistry.addBucketForFluid(lif);
		block_lif = new BlockFluidMolten(lif, "fluid_lif");
		
		bef2 = new FluidMolten("bef2");
		FluidRegistry.addBucketForFluid(bef2);
		block_bef2 = new BlockFluidMolten(bef2, "fluid_bef2");
		
		flibe = new FluidMolten("flibe");
		FluidRegistry.addBucketForFluid(flibe);
		block_flibe = new BlockFluidMolten(flibe, "fluid_flibe");
		
		boron_nitride_solution = new FluidLiquid("boron_nitride_solution");
		FluidRegistry.addBucketForFluid(boron_nitride_solution);
		block_boron_nitride_solution = new BlockFluidLiquid(boron_nitride_solution, "fluid_boron_nitride_solution");
		
		coolant = new FluidLiquid("coolant");
		FluidRegistry.addBucketForFluid(coolant);
		block_coolant = new BlockFluidLiquid(coolant, "fluid_coolant");
		
		sulfur = new FluidMolten("sulfur");
		FluidRegistry.addBucketForFluid(sulfur);
		block_sulfur = new BlockFluidMolten(sulfur, "fluid_sulfur");
		
		sulfur_dioxide = new FluidGas("sulfur_dioxide");
		FluidRegistry.addBucketForFluid(sulfur_dioxide);
		block_sulfur_dioxide = new BlockFluidGas(sulfur_dioxide, "fluid_sulfur_dioxide");
		
		sulfur_trioxide = new FluidGas("sulfur_trioxide");
		FluidRegistry.addBucketForFluid(sulfur_trioxide);
		block_sulfur_trioxide = new BlockFluidGas(sulfur_trioxide, "fluid_sulfur_trioxide");
		
		sulfuric_acid = new FluidAcid("sulfuric_acid");
		FluidRegistry.addBucketForFluid(sulfuric_acid);
		block_sulfuric_acid = new BlockFluidAcid(sulfuric_acid, "fluid_sulfuric_acid");
		
		fluorite_water = new FluidLiquid("fluorite_water");
		FluidRegistry.addBucketForFluid(fluorite_water);
		block_fluorite_water = new BlockFluidLiquid(fluorite_water, "fluid_fluorite_water");
		
		calcium_sulfate_solution = new FluidLiquid("calcium_sulfate_solution");
		FluidRegistry.addBucketForFluid(calcium_sulfate_solution);
		block_calcium_sulfate_solution = new BlockFluidLiquid(calcium_sulfate_solution, "fluid_calcium_sulfate_solution");
		
		hydrofluoric_acid = new FluidAcid("hydrofluoric_acid");
		FluidRegistry.addBucketForFluid(hydrofluoric_acid);
		block_hydrofluoric_acid = new BlockFluidAcid(hydrofluoric_acid, "fluid_hydrofluoric_acid");
		
		oxygen_difluoride = new FluidGas("oxygen_difluoride");
		FluidRegistry.addBucketForFluid(oxygen_difluoride);
		block_oxygen_difluoride = new BlockFluidGas(oxygen_difluoride, "fluid_oxygen_difluoride");
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
		registerBlock(block_boron10);
		registerBlock(block_boron11);
		registerBlock(block_neutron);
		registerBlock(block_fluorine);
		registerBlock(block_nitrogen);
		registerBlock(block_ammonia);
		registerBlock(block_ethanol);
		registerBlock(block_boric_acid);
		registerBlock(block_diborane);
		registerBlock(block_radaway);
		registerBlock(block_beryllium);
		registerBlock(block_lif);
		registerBlock(block_bef2);
		registerBlock(block_flibe);
		registerBlock(block_boron_nitride_solution);
		registerBlock(block_coolant);
		registerBlock(block_sulfur);
		registerBlock(block_sulfur_dioxide);
		registerBlock(block_sulfur_trioxide);
		registerBlock(block_sulfuric_acid);
		registerBlock(block_fluorite_water);
		registerBlock(block_calcium_sulfate_solution);
		registerBlock(block_hydrofluoric_acid);
		registerBlock(block_oxygen_difluoride);
	}
	
	public static void registerBlock(BlockFluid block) {
		//block.setRegistryName(name);
		ForgeRegistries.BLOCKS.register(block);
		ForgeRegistries.ITEMS.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
		NuclearCraft.proxy.registerFluidBlockRendering(block, block.getName());
		NCUtil.getLogger().info("Registered fluid " + block.getUnlocalizedName().substring(5));
	}
}
