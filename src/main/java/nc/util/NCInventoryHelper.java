package nc.util;

import java.util.*;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.*;

public class NCInventoryHelper {
	
	private static final Random RAND = new Random();
	
	public static void dropInventoryItems(World world, BlockPos pos, IInventory inventory, int... slots) {
		dropInventoryItems(world, pos.getX(), pos.getY(), pos.getZ(), inventory, slots);
	}
	
	public static void dropInventoryItems(World world, Entity entityAt, IInventory inventory, int... slots) {
		dropInventoryItems(world, entityAt.posX, entityAt.posY, entityAt.posZ, inventory, slots);
	}
	
	private static void dropInventoryItems(World world, double x, double y, double z, IInventory inventory, int... slots) {
		if (slots.length == 0) {
			for (int i = 0; i < inventory.getSizeInventory(); ++i) {
				ItemStack itemstack = inventory.getStackInSlot(i);
				if (!itemstack.isEmpty()) {
					spawnItemStack(world, x, y, z, itemstack);
				}
			}
		}
		else {
			for (int i : slots) {
				ItemStack itemstack = inventory.getStackInSlot(i);
				if (!itemstack.isEmpty()) {
					spawnItemStack(world, x, y, z, itemstack);
				}
			}
		}
	}
	
	public static void dropInventoryItems(World worldIn, BlockPos pos, List<ItemStack> stacks) {
		dropInventoryItems(worldIn, pos.getX(), pos.getY(), pos.getZ(), stacks);
	}
	
	public static void dropInventoryItems(World worldIn, Entity entityAt, List<ItemStack> stacks) {
		dropInventoryItems(worldIn, entityAt.posX, entityAt.posY, entityAt.posZ, stacks);
	}
	
	private static void dropInventoryItems(World worldIn, double x, double y, double z, List<ItemStack> stacks) {
		for (int i = 0; i < stacks.size(); ++i) {
			ItemStack itemstack = stacks.get(i);
			if (!itemstack.isEmpty()) {
				spawnItemStack(worldIn, x, y, z, itemstack);
			}
		}
	}
	
	private static void spawnItemStack(World world, double x, double y, double z, ItemStack stack) {
		float fx = RAND.nextFloat() * 0.8F + 0.1F;
		float fy = RAND.nextFloat() * 0.8F + 0.1F;
		float fz = RAND.nextFloat() * 0.8F + 0.1F;
		
		while (!stack.isEmpty()) {
			int split = RAND.nextInt(21) + 10;
			EntityItem entityitem = new EntityItem(world, x + fx, y + fy, z + fz, stack.splitStack(split));
			entityitem.motionX = RAND.nextGaussian() * 0.05D;
			entityitem.motionY = RAND.nextGaussian() * 0.05D + 0.2D;
			entityitem.motionZ = RAND.nextGaussian() * 0.05D;
			world.spawnEntity(entityitem);
		}
	}
	
	/** Returns the stack remaining after insertion */
	public static ItemStack addStackToInventory(IItemHandler inv, ItemStack stack) {
		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		}
		stack = ItemHandlerHelper.insertItemStacked(inv, stack, false);
		return stack;
	}
}
