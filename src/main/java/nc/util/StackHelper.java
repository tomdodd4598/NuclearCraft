package nc.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class StackHelper {
	
	public static Object fixStack(Object object) {
		if (object instanceof FluidStack) {
			FluidStack fluidstack = ((FluidStack) object).copy();
			if (fluidstack.amount == 0) {
				fluidstack.amount = 1000;
			}
			return fluidstack;
		} else if (object instanceof Fluid) {
			return new FluidStack((Fluid) object, 1000);
		} else if (object instanceof ItemStack) {
			ItemStack stack = ((ItemStack) object).copy();
			if (stack.getCount() == 0) {
				stack.setCount(1);
			}
			return stack;
		} else if (object instanceof Item) {
			return new ItemStack((Item) object, 1);
		} else {
			if (!(object instanceof Block)) {
				throw new RuntimeException(String.format("Invalid ItemStack: %s", object));
			}
			return new ItemStack((Block) object, 1);
		}
	}
	
	public static ItemStack fixItemStack(Object object) {
		if (object instanceof ItemStack) {
			ItemStack stack = ((ItemStack) object).copy();
			if (stack.getCount() == 0) {
				stack.setCount(1);
			}
			return stack;
		} else if (object instanceof Item) {
			return new ItemStack((Item) object, 1);
		} else {
			if (!(object instanceof Block)) {
				throw new RuntimeException(String.format("Invalid ItemStack: %s", object));
			}
			return new ItemStack((Block) object, 1);
		}
	}
	
	public static Object fixFluidStack(Object object) {
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
	
	public static ItemStack blockToStack(IBlockState state) {
		if (state == null) return ItemStack.EMPTY;
		Block block = state.getBlock();
		if (block == null) return ItemStack.EMPTY;
		int meta = block.getMetaFromState(state);
		return new ItemStack(block, 1, meta);
	}
	
	public static ItemStack changeStackSize(ItemStack stack, int size) {
		ItemStack newStack = stack.copy();
		newStack.setCount(size);
		return newStack.copy();
	}
	
	public static ItemStack getBucket(FluidStack fluidStack) {
		return FluidUtil.getFilledBucket(fluidStack);
	}
	
	public static ItemStack getBucket(Fluid fluid) {
		return getBucket(new FluidStack(fluid, 1000));
	}
	
	public static ItemStack getBucket(String fluidName) {
		return getBucket(FluidRegistry.getFluid(fluidName));
	}
	
	public static ItemStack blockStackFromRegistry(String domain, String name, int stackSize, int meta) {
		return new ItemStack(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(domain, name)), stackSize, meta);
	}
	
	public static ItemStack blockStackFromRegistry(String domain, String name, int stackSize) {
		return blockStackFromRegistry(domain, name, stackSize, 0);
	}
	
	public static ItemStack blockStackFromRegistry(String domain, String name) {
		return blockStackFromRegistry(domain, name, 1);
	}
	
	public static ItemStack itemStackFromRegistry(String domain, String name, int stackSize, int meta) {
		return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(domain, name)), stackSize, meta);
	}
	
	public static ItemStack itemStackFromRegistry(String domain, String name, int stackSize) {
		return itemStackFromRegistry(domain, name, stackSize, 0);
	}
	
	public static ItemStack itemStackFromRegistry(String domain, String name) {
		return itemStackFromRegistry(domain, name, 1);
	}
}
