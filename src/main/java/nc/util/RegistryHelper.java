package nc.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class RegistryHelper {
	
	public static Block getBlock(String domain, String name) {
		return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(domain, name));
	}
	
	public static Item getItem(String domain, String name) {
		return ForgeRegistries.ITEMS.getValue(new ResourceLocation(domain, name));
	}
	
	public static ItemStack blockStackFromRegistry(String domain, String name, int stackSize, int meta) {
		return new ItemStack(getBlock(domain, name), stackSize, meta);
	}
	
	public static ItemStack blockStackFromRegistry(String domain, String name, int stackSize) {
		return blockStackFromRegistry(domain, name, stackSize, 0);
	}
	
	public static ItemStack blockStackFromRegistry(String domain, String name) {
		return blockStackFromRegistry(domain, name, 1);
	}
	
	public static ItemStack itemStackFromRegistry(String domain, String name, int stackSize, int meta) {
		return new ItemStack(getItem(domain, name), stackSize, meta);
	}
	
	public static ItemStack itemStackFromRegistry(String domain, String name, int stackSize) {
		return itemStackFromRegistry(domain, name, stackSize, 0);
	}
	
	public static ItemStack itemStackFromRegistry(String domain, String name) {
		return itemStackFromRegistry(domain, name, 1);
	}
	
	public static String getModID(ItemStack stack) {
		if (stack == null) return "";
		Item item = stack.getItem();
		if (item == null) return "";
		return item.delegate.name().getResourceDomain();
	}
}
