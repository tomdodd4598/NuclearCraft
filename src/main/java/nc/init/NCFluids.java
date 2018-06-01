package nc.init;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import nc.NuclearCraft;
import nc.block.fluid.BlockFluidAcid;
import nc.block.fluid.BlockFluidBase;
import nc.block.fluid.BlockFluidCoolant;
import nc.block.fluid.BlockFluidFission;
import nc.block.fluid.BlockFluidFlammable;
import nc.block.fluid.BlockFluidGas;
import nc.block.fluid.BlockFluidHotCoolant;
import nc.block.fluid.BlockFluidLiquid;
import nc.block.fluid.BlockFluidMolten;
import nc.block.fluid.BlockFluidParticle;
import nc.block.fluid.BlockFluidPlasma;
import nc.block.fluid.BlockFluidSaltSolution;
import nc.block.fluid.BlockSuperFluid;
import nc.fluid.FluidAcid;
import nc.fluid.FluidCoolant;
import nc.fluid.FluidFission;
import nc.fluid.FluidFlammable;
import nc.fluid.FluidGas;
import nc.fluid.FluidHotCoolant;
import nc.fluid.FluidLiquid;
import nc.fluid.FluidMolten;
import nc.fluid.FluidParticle;
import nc.fluid.FluidPlasma;
import nc.fluid.FluidSaltSolution;
import nc.fluid.SuperFluid;
import nc.util.ColorHelper;
import nc.util.NCUtil;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class NCFluids {
	
public static List<Pair<Fluid, BlockFluidBase>> fluidPairList = new ArrayList<Pair<Fluid, BlockFluidBase>>();
	
	static {
		try {
			fluidPairList.add(fluidPair("oxygen", 0x7E8CC8, FluidGas.class, BlockFluidGas.class));
			fluidPairList.add(fluidPair("hydrogen", 0xB37AC4, FluidGas.class, BlockFluidGas.class));
			fluidPairList.add(fluidPair("deuterium", 0x9E6FEF, FluidGas.class, BlockFluidGas.class));
			fluidPairList.add(fluidPair("tritium", 0x5DBBD6, FluidGas.class, BlockFluidGas.class));
			fluidPairList.add(fluidPair("helium3", 0xCBBB67, FluidGas.class, BlockFluidGas.class));
			fluidPairList.add(fluidPair("helium", 0xC57B81, FluidGas.class, BlockFluidGas.class));
			
			fluidPairList.add(fluidPair("boron", 0x7B7B7B, FluidMolten.class, BlockFluidMolten.class));
			fluidPairList.add(fluidPair("boron10", 0x7B7B7B, FluidMolten.class, BlockFluidMolten.class));
			fluidPairList.add(fluidPair("boron11", 0x7B7B7B, FluidMolten.class, BlockFluidMolten.class));
			fluidPairList.add(fluidPair("lithium", 0xEFEFEF, FluidMolten.class, BlockFluidMolten.class));
			fluidPairList.add(fluidPair("lithium6", 0xEFEFEF, FluidMolten.class, BlockFluidMolten.class));
			fluidPairList.add(fluidPair("lithium7", 0xEFEFEF, FluidMolten.class, BlockFluidMolten.class));
			
			fluidPairList.add(fluidPair("beryllium", 0xD4DBC2, FluidMolten.class, BlockFluidMolten.class));
			fluidPairList.add(fluidPair("sulfur", 0xDEDE7A, FluidMolten.class, BlockFluidMolten.class));
			
			SuperFluid liquidhelium = new SuperFluid("liquidhelium");
			fluidPairList.add(fluidPair(liquidhelium, new BlockSuperFluid(liquidhelium)));
			FluidPlasma plasma = new FluidPlasma("plasma");
			fluidPairList.add(fluidPair(plasma, new BlockFluidPlasma(plasma)));
			FluidParticle neutron = new FluidParticle("neutron");
			fluidPairList.add(fluidPair(neutron, new BlockFluidParticle(neutron)));
			fluidPairList.add(fluidPair("ethanol", 0x655140, FluidFlammable.class, BlockFluidFlammable.class));
			FluidLiquid radaway = new FluidLiquid("radaway");
			fluidPairList.add(fluidPair(radaway, new BlockFluidLiquid(radaway)));
			
			fluidPairList.add(fluidPair("nitrogen", 0x7CC37B, FluidGas.class, BlockFluidGas.class));
			fluidPairList.add(fluidPair("fluorine", 0xD3C75D, FluidGas.class, BlockFluidGas.class));
			
			fluidPairList.add(fluidPair("ammonia", 0x7AC3A0, FluidGas.class, BlockFluidGas.class));
			fluidPairList.add(fluidPair("oxygen_difluoride", 0xEA1B01, FluidGas.class, BlockFluidGas.class));
			fluidPairList.add(fluidPair("diborane", 0xCC6E8C, FluidGas.class, BlockFluidGas.class));
			fluidPairList.add(fluidPair("sulfur_dioxide", 0xC3BC7A, FluidGas.class, BlockFluidGas.class));
			fluidPairList.add(fluidPair("sulfur_trioxide", 0xD3AE5D, FluidGas.class, BlockFluidGas.class));
			
			fluidPairList.add(fluidPair("hydrofluoric_acid", 0x004C05, FluidAcid.class, BlockFluidAcid.class));
			fluidPairList.add(fluidPair("boric_acid", 0x696939, FluidAcid.class, BlockFluidAcid.class));
			fluidPairList.add(fluidPair("sulfuric_acid", 0x454500, FluidAcid.class, BlockFluidAcid.class));
			
			fluidPairList.add(fluidPair("boron_nitride_solution", waterBlend(0x6F8E5C), FluidSaltSolution.class, BlockFluidSaltSolution.class));
			fluidPairList.add(fluidPair("fluorite_water", waterBlend(0x8AB492), FluidSaltSolution.class, BlockFluidSaltSolution.class));
			fluidPairList.add(fluidPair("calcium_sulfate_solution", waterBlend(0xB8B0A6), FluidSaltSolution.class, BlockFluidSaltSolution.class));
			fluidPairList.add(fluidPair("sodium_fluoride_solution", waterBlend(0xC2B1A1), FluidSaltSolution.class, BlockFluidSaltSolution.class));
			fluidPairList.add(fluidPair("potassium_fluoride_solution", waterBlend(0xC1C99D), FluidSaltSolution.class, BlockFluidSaltSolution.class));
			fluidPairList.add(fluidPair("sodium_hydroxide_solution", waterBlend(0xC2B7BB), FluidSaltSolution.class, BlockFluidSaltSolution.class));
			fluidPairList.add(fluidPair("potassium_hydroxide_solution", waterBlend(0xB8C6B0), FluidSaltSolution.class, BlockFluidSaltSolution.class));
			
			fluidPairList.add(fluidPair("corium", 0x7F8178, FluidFission.class, BlockFluidFission.class));
			
			fluidPairList.add(fluidPair("lif", 0xCDCDCB, FluidMolten.class, BlockFluidMolten.class));
			fluidPairList.add(fluidPair("bef2", 0xBEC6AA, FluidMolten.class, BlockFluidMolten.class));
			fluidPairList.add(fluidPair("flibe", 0xC1C8B0, FluidMolten.class, BlockFluidMolten.class));
			fluidPairList.add(fluidPair("naoh", 0xC2B7BB, FluidMolten.class, BlockFluidMolten.class));
			fluidPairList.add(fluidPair("koh", 0xB8C6B0, FluidMolten.class, BlockFluidMolten.class));
			
			fluidPairList.add(fluidPair("steam", 0x8E8E8E, FluidGas.class, BlockFluidGas.class));
			fluidPairList.add(fluidPair("coolant", 0x88D7CF, FluidCoolant.class, BlockFluidCoolant.class));
			
			fluidPairList.add(fluidPair("sodium", 0xC1898C, FluidMolten.class, BlockFluidMolten.class));
			fluidPairList.add(fluidPair("potassium", 0xB8C503, FluidMolten.class, BlockFluidMolten.class));
			fluidPairList.add(fluidPair("nak", 0xFFE5BC, FluidCoolant.class, BlockFluidCoolant.class));
			fluidPairList.add(fluidPair("nak_hot", 0xFFD5AC, FluidHotCoolant.class, BlockFluidHotCoolant.class));
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public static void register() {
		for (Pair<Fluid, BlockFluidBase> fluidPair : fluidPairList) {
			Fluid fluid = fluidPair.getLeft();
			
			boolean defaultFluid = FluidRegistry.registerFluid(fluid);
			if (!defaultFluid) fluid = FluidRegistry.getFluid(fluid.getName());
			FluidRegistry.addBucketForFluid(fluid);
			
			registerBlock(fluidPair.getRight());
		}
	}
	
	public static void registerBlock(BlockFluidBase block) {
		ForgeRegistries.BLOCKS.register(block);
		ForgeRegistries.ITEMS.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
		NuclearCraft.proxy.registerFluidBlockRendering(block, block.getName());
	}
	
	public static Pair<Fluid, BlockFluidBase> fluidPair(Fluid fluid, BlockFluidBase block) {
		return Pair.of(fluid, block);
	}
	
	public static <T extends Fluid, V extends BlockFluidBase> Pair<Fluid, BlockFluidBase> fluidPair(String name, int color, Class<T> fluidClass, Class<V> blockClass) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		T fluid = NCUtil.newInstance(fluidClass, name, color);
		V block = NCUtil.newInstance(blockClass, fluid);
		return Pair.of(fluid, block);
	}
	
	static int waterBlend(int soluteColor) {
		return ColorHelper.blend(0x2F43F4, soluteColor, 0.5F);
	}
}
