package nc.util;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NCInventoryHelper {
	
	private static final Random RANDOM = new Random();

	public static void dropInventoryItems(World worldIn, BlockPos pos, IInventory inventory, int... slots) {
		dropInventoryItems(worldIn, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), inventory, slots);
	}
	
	public static void dropInventoryItems(World worldIn, Entity entityAt, IInventory inventory, int... slots) {
		dropInventoryItems(worldIn, entityAt.posX, entityAt.posY, entityAt.posZ, inventory, slots);
	}
	
	private static void dropInventoryItems(World worldIn, double x, double y, double z, IInventory inventory, int... slots) {
		if (slots.length == 0) {
			for (int i = 0; i < inventory.getSizeInventory(); ++i) {
				ItemStack itemstack = inventory.getStackInSlot(i);
				if (!itemstack.isEmpty()) {
					spawnItemStack(worldIn, x, y, z, itemstack);
				}
			}
		} else {
			for (int i : slots) {
				ItemStack itemstack = inventory.getStackInSlot(i);
				if (!itemstack.isEmpty()) {
					spawnItemStack(worldIn, x, y, z, itemstack);
				}
			}
		}
	}
	
	public static void spawnItemStack(World worldIn, double x, double y, double z, ItemStack stack) {
		float f = RANDOM.nextFloat() * 0.8F + 0.1F;
		float f1 = RANDOM.nextFloat() * 0.8F + 0.1F;
		float f2 = RANDOM.nextFloat() * 0.8F + 0.1F;

		while (!stack.isEmpty()) {
			int i = RANDOM.nextInt(21) + 10;
			EntityItem entityitem = new EntityItem(worldIn, x + (double)f, y + (double)f1, z + (double)f2, stack.splitStack(i));
			entityitem.motionX = RANDOM.nextGaussian() * 0.05000000074505806D;
			entityitem.motionY = RANDOM.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D;
			entityitem.motionZ = RANDOM.nextGaussian() * 0.05000000074505806D;
			worldIn.spawnEntity(entityitem);
		}
	}
}
