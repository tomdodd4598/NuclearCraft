package nc.util;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class RegistryHelper {
	
	public static Block getBlock(String location) {
		return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(location));
	}
	
	public static Item getItem(String location) {
		return ForgeRegistries.ITEMS.getValue(new ResourceLocation(location));
	}
	
	public static ItemStack blockStackFromRegistry(String location, int stackSize) {
		return getBlock(removeMeta(location)) == null ? null : new ItemStack(getBlock(removeMeta(location)), stackSize, getStackMeta(location));
	}
	
	public static ItemStack blockStackFromRegistry(String location) {
		return blockStackFromRegistry(location, 1);
	}
	
	public static ItemStack itemStackFromRegistry(String location, int stackSize) {
		return getItem(removeMeta(location)) == null ? null : new ItemStack(getItem(removeMeta(location)), stackSize, getStackMeta(location));
	}
	
	public static ItemStack itemStackFromRegistry(String location) {
		return itemStackFromRegistry(location, 1);
	}
	
	public static Biome biomeFromRegistry(String location) {
		return ForgeRegistries.BIOMES.getValue(new ResourceLocation(location));
	}
	
	public static int getStackMeta(String location) {
		if (StringUtils.countMatches(location, ':') < 2) return 0;
		return Integer.parseInt(location.substring(location.lastIndexOf(':') + 1));
	}
	
	public static String removeMeta(String location) {
		if (StringUtils.countMatches(location, ':') < 2) return location;
		return StringHelper.starting(location, location.lastIndexOf(':'));
	}
	
	public static String getModID(ItemStack stack) {
		if (stack == null) return "";
		Item item = stack.getItem();
		if (item == null) return "";
		return item.delegate.name().getResourceDomain();
	}
}
