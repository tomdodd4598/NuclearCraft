package nc.util;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class OreStackHelper {
	
	public static boolean exists(String ore) {
		ArrayList<ItemStack> itemList = new ArrayList<ItemStack>(OreDictionary.getOres(ore));
		if (itemList.size() > 0) return true;
		ArrayList<Fluid> fluidList = new ArrayList<Fluid>(FluidRegistry.getRegisteredFluids().values());
		for (Fluid fluid : fluidList) {
			if (fluid.getName() == ore.toLowerCase()) return true;
		}
		return false;
	}
	
	public static ItemStack blockToStack(IBlockState state) {
		if (state == null) return ItemStack.EMPTY;
		Block block = state.getBlock();
		if (block == null) return ItemStack.EMPTY;
		int meta = block.getMetaFromState(state);
		return new ItemStack(block, 1, meta);
	}
}
