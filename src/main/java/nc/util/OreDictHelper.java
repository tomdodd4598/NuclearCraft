package nc.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.config.NCConfig;
import net.minecraft.block.Block;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictHelper {
	
	public static final List<String> INGOT_VOLUME_TYPES = Lists.newArrayList("ingot", "dust");
	public static final List<String> NUGGET_VOLUME_TYPES = Lists.newArrayList("nugget", "tinyDust");
	
	public static final List<String> GEM_VOLUME_TYPES = Lists.newArrayList("gem", "dust");
	public static final List<String> GEM_NUGGET_VOLUME_TYPES = Lists.newArrayList("nugget", "tinyDust");
	
	public static final List<String> COAL_TYPES = Lists.newArrayList("coal", "dustCoal");
	
	public static final List<String> DUST_VOLUME_TYPES = Lists.newArrayList("dust");
	public static final List<String> TINYDUST_VOLUME_TYPES = Lists.newArrayList("tinyDust");
	
	public static final List<String> FUEL_VOLUME_TYPES = Lists.newArrayList("fuel", "dust");
	
	public static final List<String> BLOCK_VOLUME_TYPES = Lists.newArrayList("block");
	public static final List<String> FUEL_BLOCK_VOLUME_TYPES = Lists.newArrayList("block", "blockFuel");
	
	public static boolean isOreMatching(ItemStack stack, ItemStack target) {
		for (String oreName : getOreNames(target)) {
			for (ItemStack ore : OreDictionary.getOres(oreName, false)) {
				if (ItemStack.areItemsEqual(ore, stack)) return true;
			}
		}
		return false;
	}
	
	public static boolean isOreMember(ItemStack stack, String oreName) {
		for (ItemStack ore : OreDictionary.getOres(oreName, false)) {
			if (ItemStack.areItemsEqual(ore, stack)) return true;
		}
		return false;
	}
	
	public static boolean oreExists(String ore) {
		return !OreDictionary.getOres(ore, false).isEmpty();
	}
	
	public static boolean oresExist(String... ores) {
		for (String ore : ores) {
			if (!oreExists(ore)) return false;
		}
		return true;
	}
	
	public static String getOreNameFromStacks(List<ItemStack> stackList) {
		List<String> oreNameList = new ArrayList<>();
		if (stackList == null || stackList.isEmpty()) return "Unknown";
		oreNameList.addAll(getOreNames(stackList.get(0)));
		
		for (ItemStack stack : stackList) {
			if (stack == null || stack.isEmpty()) return "Unknown";
			oreNameList = CollectionHelper.intersect(oreNameList, getOreNames(stack));
			if (oreNameList.isEmpty()) return "Unknown";
		}
		return oreNameList.get(0);
	}
	
	public static boolean getBlockMatchesOre(World world, BlockPos pos, String... names) {
		List<ItemStack> stackList = new ArrayList<>();
		for (int i = 0; i < names.length; i++) {
			List<ItemStack> stacks = OreDictionary.getOres(names[i], false);
			stackList.addAll(stacks);
		}
		ItemStack stack = ItemStackHelper.blockStateToStack(world.getBlockState(pos));
		for (ItemStack oreStack : stackList) if (oreStack.isItemEqual(stack)) return true;
		return false;
	}
	
	public static List<ItemStack> getPrioritisedStackList(String ore) {
		List<ItemStack> defaultStackList = new ArrayList<ItemStack>(OreDictionary.getOres(ore, false));
		if (!NCConfig.ore_dict_priority_bool || NCConfig.ore_dict_priority.length < 1) return defaultStackList;
		List<ItemStack> prioritisedStackList = new ArrayList<>();
		for (int i = 0; i < NCConfig.ore_dict_priority.length; i++) {
			for (ItemStack stack : defaultStackList) {
				if (RegistryHelper.getModID(stack).equals(NCConfig.ore_dict_priority[i]) && !prioritisedStackList.contains(stack)) {
					prioritisedStackList.add(stack);
				}
			}
		}
		if (prioritisedStackList.isEmpty()) return defaultStackList;
		for (ItemStack stack : defaultStackList) {
			if (!prioritisedStackList.contains(stack)) {
				prioritisedStackList.add(stack);
			}
		}
		return prioritisedStackList;
	}
	
	public static ItemStack getPrioritisedCraftingStack(ItemStack backup, String ore) {
		if (ore == null) {
			return backup;
		}
		List<ItemStack> stackList = getPrioritisedStackList(ore);
		if (stackList == null || stackList.isEmpty()) {
			if (backup == null || backup.isEmpty()) return null;
			return backup;
		}
		ItemStack stack = stackList.get(0).copy();
		stack.setCount(backup == null || backup.isEmpty() ? 1 : backup.getCount());
		return stack;
	}
	
	public static ItemStack getPrioritisedCraftingStack(Item backup, String ore) {
		return getPrioritisedCraftingStack(backup == null ? ItemStack.EMPTY : new ItemStack(backup), ore);
	}
	
	public static ItemStack getPrioritisedCraftingStack(Block backup, String ore) {
		return getPrioritisedCraftingStack(backup == null ? ItemStack.EMPTY : new ItemStack(backup), ore);
	}
	
	// Wildcard Helper
	
	public static void addWildcard(Set<String> set, String ore) {
		ore = StringHelper.regex(ore);
		for (String o : OreDictionary.getOreNames()) {
			if (o.matches(ore)) {
				set.add(o);
			}
		}
	}
	
	public static <T> void putWildcard(Map<String, T> map, String ore, T value) {
		ore = StringHelper.regex(ore);
		for (String o : OreDictionary.getOreNames()) {
			if (o.matches(ore)) {
				map.put(o, value);
			}
		}
	}
	
	// Ore Dictionary Entry Cache
	
	private static final Int2ObjectMap<Set<String>> ORE_DICT_CACHE = new Int2ObjectOpenHashMap<>();
	
	private static Set<String> getOreNames(ItemStack stack, boolean useCache) {
		if (stack == null || stack.isEmpty()) return Collections.emptySet();
		int packed = RecipeItemHelper.pack(stack);
		if (!useCache || !ORE_DICT_CACHE.containsKey(packed)) {
			Set<String> names = new ObjectOpenHashSet<>();
			for (int oreID : OreDictionary.getOreIDs(stack)) {
				names.add(OreDictionary.getOreName(oreID));
			}
			if (useCache) ORE_DICT_CACHE.put(packed, names);
			return names;
		}
		return ORE_DICT_CACHE.get(packed);
	}
	
	public static Set<String> getOreNames(ItemStack stack) {
		return getOreNames(stack, true);
	}
	
	public static boolean hasOrePrefix(ItemStack stack, String... prefixes) {
		for (String name : getOreNames(stack, false)) {
			for (String prefix : prefixes) {
				if (name.startsWith(prefix)) return true;
			}
		}
		return false;
	}
	
	public static void refreshOreDictCache() {
		ORE_DICT_CACHE.clear();
	}
}
