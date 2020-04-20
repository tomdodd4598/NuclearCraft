package nc.util;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class FluidStackHelper {
	
public static final int BUCKET_VOLUME = 1000;
	
	public static final int INGOT_VOLUME = 144;
	public static final int NUGGET_VOLUME = INGOT_VOLUME/9;
	public static final int INGOT_BLOCK_VOLUME = INGOT_VOLUME*9;
	public static final int INGOT_ORE_VOLUME = INGOT_VOLUME*2;
	
	public static final int FRAGMENT_VOLUME = INGOT_VOLUME/4;
	public static final int SHARD_VOLUME = INGOT_VOLUME/2;
	
	public static final int GEM_VOLUME = 666;
	public static final int GEM_NUGGET_VOLUME = GEM_VOLUME/9;
	public static final int GEM_BLOCK_VOLUME = GEM_VOLUME*9;
	public static final int GEM_ORE_VOLUME = GEM_VOLUME*2;
	
	public static final int GLASS_VOLUME = BUCKET_VOLUME;
	public static final int GLASS_PANE_VOLUME = (GLASS_VOLUME*6)/16;
	public static final int BRICK_VOLUME = INGOT_VOLUME;
	public static final int BRICK_BLOCK_VOLUME = BRICK_VOLUME*4;
	
	public static final int SEARED_BLOCK_VOLUME = INGOT_VOLUME*2;
	public static final int SEARED_MATERIAL_VOLUME = INGOT_VOLUME/2;
	
	public static final int SLIMEBALL_VOLUME = 250;
	
	public static final int REDSTONE_DUST_VOLUME = 100;
	public static final int REDSTONE_BLOCK_VOLUME = REDSTONE_DUST_VOLUME*9;
	public static final int GLOWSTONE_DUST_VOLUME = 250;
	public static final int GLOWSTONE_BLOCK_VOLUME = GLOWSTONE_DUST_VOLUME*4;
	public static final int COAL_DUST_VOLUME = 100;
	public static final int COAL_BLOCK_VOLUME = COAL_DUST_VOLUME*9;
	public static final int ENDER_PEARL_VOLUME = 250;
	
	public static final int EUM_DUST_VOLUME = 250;
	
	public static FluidStack fixFluidStack(Object object) {
		if (object instanceof FluidStack) {
			FluidStack fluidstack = ((FluidStack) object).copy();
			if (fluidstack.amount == 0) {
				fluidstack.amount = 1000;
			}
			return fluidstack;
		} else {
			if (!(object instanceof Fluid)) {
				throw new RuntimeException(String.format("Invalid FluidStack: %s", object));
			}
			return new FluidStack((Fluid) object, 1000);
		}
	}
	
	public static String getFluidName(FluidStack stack) {
		if (stack == null || stack.getFluid() == null) return "null";
		return stack.getFluid().getName();
	}
	
	public static String stackListNames(List<FluidStack> list) {
		String names = "";
		for (FluidStack stack : list) names += (", " + getFluidName(stack));
		return names.substring(2);
	}
	
	public static FluidStack changeStackSize(FluidStack stack, int size) {
		FluidStack newStack = stack.copy();
		newStack.amount = size;
		return newStack.copy();
	}
	
	public static boolean stacksEqual(FluidStack stackA, FluidStack stackB) {
		return stackA == null ? stackB == null : stackA.isFluidEqual(stackB);
	}
	
	public static boolean fluidsEqual(FluidStack fluid, ItemStack container) {
		FluidStack containedFluid = getFluid(container);
		return fluid == null ? containedFluid == null : fluid.isFluidEqual(containedFluid);
	}
	
	public static FluidStack getFluid(ItemStack container) {
		return FluidUtil.getFluidContained(container);
	}
}
