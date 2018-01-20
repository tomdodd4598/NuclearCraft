package nc.util;

import java.util.ArrayList;

import nc.recipe.StackType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.oredict.OreDictionary;

public class OreStackHelper {
	
	public static boolean exists(String ore, StackType type) {
		if (!type.isFluid()) if (OreDictionary.getOres(ore).size() > 0) return true;
		if (!type.isItem()) if (FluidRegistry.getRegisteredFluids().keySet().contains(ore.toLowerCase())) return true;
		return false;
	}
	
	public static ItemStack blockToStack(IBlockState state) {
		if (state == null) return ItemStack.EMPTY;
		Block block = state.getBlock();
		if (block == null) return ItemStack.EMPTY;
		int meta = block.getMetaFromState(state);
		return new ItemStack(block, 1, meta);
	}
	
	public static final String[] INGOT_VOLUME_TYPES = new String[] {"ingot", "dust"};
	public static final String[] NUGGET_VOLUME_TYPES = new String[] {"nugget", "tinyDust"};
	
	public static final String[] DUST_VOLUME_TYPES = new String[] {"dust"};
	public static final String[] TINYDUST_VOLUME_TYPES = new String[] {"tinyDust"};
	
	public static final String[] FUEL_VOLUME_TYPES = new String[] {"fuel", "dust"};
	
	public static final String[] BLOCK_VOLUME_TYPES = new String[] {"block"};
}
