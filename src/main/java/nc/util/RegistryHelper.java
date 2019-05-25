package nc.util;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class RegistryHelper {
	
	public static Block getBlock(String location) {
		ResourceLocation resLoc = new ResourceLocation(location);
		if (!Loader.isModLoaded(resLoc.getResourceDomain())) return null;
		return ForgeRegistries.BLOCKS.getValue(resLoc);
	}
	
	public static Item getItem(String location) {
		ResourceLocation resLoc = new ResourceLocation(location);
		if (!Loader.isModLoaded(resLoc.getResourceDomain())) return null;
		return ForgeRegistries.ITEMS.getValue(resLoc);
	}
	
	public static ItemStack blockStackFromRegistry(String location, int stackSize) {
		return new ItemStack(getBlock(removeMeta(location)), stackSize, getStackMeta(location));
	}
	
	public static ItemStack blockStackFromRegistry(String location) {
		return blockStackFromRegistry(location, 1);
	}
	
	public static ItemStack itemStackFromRegistry(String location, int stackSize) {
		return new ItemStack(getItem(removeMeta(location)), stackSize, getStackMeta(location));
	}
	
	public static ItemStack itemStackFromRegistry(String location) {
		return itemStackFromRegistry(location, 1);
	}
	
	public static Biome biomeFromRegistry(String location) {
		ResourceLocation resLoc = new ResourceLocation(location);
		if (!Loader.isModLoaded(resLoc.getResourceDomain())) return null;
		return ForgeRegistries.BIOMES.getValue(resLoc);
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
		if (item == null || item.delegate == null || item.delegate.name() == null || item.delegate.name().getResourceDomain() == null) return "";
		return item.delegate.name().getResourceDomain();
	}
}
