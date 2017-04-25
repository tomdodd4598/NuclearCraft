package nc.init;

import nc.Global;
import nc.block.fluid.BlockFluidBase;
import nc.block.fluid.BlockFluidGas;
import nc.block.fluid.BlockFluidPlasma;
import nc.block.fluid.BlockFluidSteam;
import nc.block.fluid.BlockSuperFluid;
import nc.block.fluid.FluidGas;
import nc.block.fluid.FluidPlasma;
import nc.block.fluid.SuperFluid;
import nc.util.NCUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class NCFluids {
	
	public static Fluid oxygen;
	public static BlockFluidBase block_oxygen;
	
	public static Fluid hydrogen;
	public static BlockFluidBase block_hydrogen;
	
	public static Fluid deuterium;
	public static BlockFluidBase block_deuterium;
	
	public static Fluid tritium;
	public static BlockFluidBase block_tritium;
	
	public static Fluid helium3;
	public static BlockFluidBase block_helium3;
	
	public static Fluid helium;
	public static BlockFluidBase block_helium;
	
	public static Fluid steam;
	public static BlockFluidBase block_steam;
	
	public static Fluid liquid_helium;
	public static BlockFluidBase block_liquid_helium;
	
	public static Fluid plasma;
	public static BlockFluidBase block_plasma;
	
	public static void init() {
		oxygen = new FluidGas("oxygen");
		block_oxygen = new BlockFluidGas(oxygen, "fluid_" + "oxygen");
		
		hydrogen = new FluidGas("hydrogen");
		block_hydrogen = new BlockFluidGas(hydrogen, "fluid_" + "hydrogen");
		
		deuterium = new FluidGas("deuterium");
		block_deuterium = new BlockFluidGas(deuterium, "fluid_" + "deuterium");
		
		tritium = new FluidGas("tritium");
		block_tritium = new BlockFluidGas(tritium, "fluid_" + "tritium");
		
		helium3 = new FluidGas("helium3");
		block_helium3 = new BlockFluidGas(helium3, "fluid_" + "helium3");
		
		helium = new FluidGas("helium");
		block_helium = new BlockFluidGas(helium, "fluid_" + "helium");
		
		steam = new FluidGas("steam");
		block_steam = new BlockFluidSteam(steam, "fluid_" + "steam");
		
		liquid_helium = new SuperFluid("liquid_helium");
		block_liquid_helium = new BlockSuperFluid(liquid_helium, "fluid_" + "liquid_helium");
		
		plasma = new FluidPlasma("plasma");
		block_plasma = new BlockFluidPlasma(plasma, "fluid_" + "plasma");
	}
	
	public static void register() {
		FluidRegistry.registerFluid(oxygen);
		registerBlock(block_oxygen);
		
		FluidRegistry.registerFluid(hydrogen);
		registerBlock(block_hydrogen);
		
		FluidRegistry.registerFluid(deuterium);
		registerBlock(block_deuterium);
		
		FluidRegistry.registerFluid(tritium);
		registerBlock(block_tritium);
		
		FluidRegistry.registerFluid(helium3);
		registerBlock(block_helium3);
		
		FluidRegistry.registerFluid(helium);
		registerBlock(block_helium);
		
		FluidRegistry.registerFluid(steam);
		registerBlock(block_steam);
		
		FluidRegistry.registerFluid(liquid_helium);
		registerBlock(block_liquid_helium);
		
		FluidRegistry.registerFluid(plasma);
		registerBlock(block_plasma);
	}
	
	public static void registerBlock(Block block) {
		//block.setRegistryName(name);
		GameRegistry.register(block);
		GameRegistry.register(new ItemBlock(block), block.getRegistryName());
		NCUtils.getLogger().info("Registered fluid " + block.getUnlocalizedName().substring(5));
	}
}
