package cofh.api.modhelpers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ModAPIManager;

@SuppressWarnings("unchecked")
public class ThaumcraftHelper {

	// public static final boolean THAUMCRAFT_PRESENT;

	private static Map<String, ? extends Object> aspects = null;
	private static Method registerItem = null;
	private static Method registerEntity = null;
	private static Class<?> AspectList = null;
	private static Constructor<?> newAspectList = null;
	private static Method addAspect = null;
	private static boolean works = false;

	static {
		l: try {
			if (!ModAPIManager.INSTANCE.hasAPI("Thaumcraft|API")) {
				break l;
			}
			Class<?> Aspect = Class.forName("thaumcraft.api.aspects.Aspect");
			aspects = (Map<String, ? extends Object>) Aspect.getDeclaredField("aspects").get(null);
			Class<?> ThaumcraftApi = Class.forName("thaumcraft.api.ThaumcraftApi");
			AspectList = Class.forName("thaumcraft.api.aspects.AspectList");
			registerItem = ThaumcraftApi.getDeclaredMethod("registerObjectTag", ItemStack.class, AspectList);
			Class<?> EntityTagsNBT = Class.forName("[Lthaumcraft.api.ThaumcraftApi$EntityTagsNBT;");
			registerEntity = ThaumcraftApi.getDeclaredMethod("registerEntityTag", String.class, AspectList, EntityTagsNBT);
			addAspect = AspectList.getDeclaredMethod("add", Aspect, int.class);
			newAspectList = AspectList.getDeclaredConstructor(ItemStack.class);
			works = true;
		} catch (Throwable x) {
			x.printStackTrace();
		}
	}

	private ThaumcraftHelper() {

	}

	private static void parseAspects(Object aspectList, String toadd) throws Throwable {

		if (!works) {
			return;
		}
		toadd = toadd.trim();
		if (!toadd.isEmpty()) {
			String[] list = toadd.split(",");
			for (int i = 0, e = list.length; i < e; ++i) {
				String[] temp = list[i].trim().split(" ", 2);
				if (temp.length != 2) {
					FMLLog.bigWarning("[CoFH Thaumcraft Helper] Invalid aspect entry '%s'", list[i]);
					continue;
				}
				String aspect = temp[1].trim();
				if (aspects.containsKey(aspect)) {
					addAspect.invoke(aspectList, aspects.get(aspect), Integer.valueOf(temp[0], 10));
				} else {
					FMLLog.fine("[CoFH Thaumcraft Helper] %s aspect missing.", temp[1]);
				}
			}
		}
	}

	/**
	 * @param entity
	 *            The string ID of the entity
	 * @param toadd
	 *            A list of comma-separated aspects for scanning the entity
	 *            <ol>
	 *            <b>entry format</b>: '<code>\d+ .+</code>'
	 *            </ol>
	 * @throws Throwable
	 *             Any errors caused by reflection
	 */
	public static void parseAspects(String entity, String toadd) throws Throwable {

		if (!works) {
			return;
		}
		Object aspectList = AspectList.newInstance();

		parseAspects(aspectList, toadd);

		registerEntity.invoke(null, entity, aspectList, null);
	}

	/**
	 * @param item
	 *            The {@link ItemStack} to add aspects to.
	 * @param toadd
	 *            A list of comma-separated aspects for scanning the ItemStack
	 *            <ol>
	 *            <b>entry format</b>: '<code>\d+ .+</code>'
	 *            </ol>
	 * @param craftedAspects
	 *            True if the item should inherit aspects from its crafting ingredients
	 * @throws Throwable
	 *             Any errors caused by reflection
	 */
	public static void parseAspects(ItemStack item, String toadd, boolean craftedAspects) throws Throwable {

		if (!works) {
			return;
		}
		Object aspectList;
		if (craftedAspects) {
			aspectList = newAspectList.newInstance(item);
		} else {
			aspectList = AspectList.newInstance();
		}

		parseAspects(aspectList, toadd);

		registerItem.invoke(null, item, aspectList);
	}

	/**
	 * @param item
	 *            The {@link ItemStack} to add aspects to.
	 *            <ol>
	 *            Will <b>inherit aspects</b>.
	 *            </ol>
	 * @param toadd
	 *            A list of comma-separated aspects for scanning the ItemStack
	 *            <ol>
	 *            <b>entry format</b>: '<code>\d+ .+</code>'
	 *            </ol>
	 * @throws Throwable
	 *             Any errors caused by reflection
	 */
	public static void parseAspects(ItemStack item, String toadd) throws Throwable {

		parseAspects(item, toadd, true);
	}

	/* ITEMS */
	/**
	 * @param item
	 *            The {@link Item} to add aspects to.
	 * @param meta
	 *            The metadata of the Item
	 * @param toadd
	 *            A list of comma-separated aspects for scanning the Item
	 *            <ol>
	 *            <b>entry format</b>: '<code>\d+ .+</code>'
	 *            </ol>
	 * @param craftedAspects
	 *            True if the item should inherit aspects from its crafting ingredients
	 * @throws Throwable
	 *             Any errors caused by reflection
	 */
	public static void parseAspects(Item item, int meta, String toadd, boolean craftedAspects) throws Throwable {

		parseAspects(new ItemStack(item, 1, meta), toadd, craftedAspects);
	}

	/**
	 * @param item
	 *            The {@link Item} to add aspects to.
	 *            <ol>
	 *            Will apply to <b>all metadatas</b>.
	 *            </ol>
	 * @param toadd
	 *            A list of comma-separated aspects for scanning the Item
	 *            <ol>
	 *            <b>entry format</b>: '<code>\d+ .+</code>'
	 *            </ol>
	 * @param craftedAspects
	 *            True if the item should inherit aspects from its crafting ingredients
	 * @throws Throwable
	 *             Any errors caused by reflection
	 */
	public static void parseAspects(Item item, String toadd, boolean craftedAspects) throws Throwable {

		parseAspects(item, OreDictionary.WILDCARD_VALUE, toadd, craftedAspects);
	}

	/**
	 * @param item
	 *            The {@link Item} to add aspects to.
	 *            <ol>
	 *            Will <b>inherit aspects</b>.
	 *            </ol>
	 * @param meta
	 *            The metadata of the Item
	 * @param toadd
	 *            A list of comma-separated aspects for scanning the Item
	 *            <ol>
	 *            <b>entry format</b>: '<code>\d+ .+</code>'
	 *            </ol>
	 * @throws Throwable
	 *             Any errors caused by reflection
	 */
	public static void parseAspects(Item item, int meta, String toadd) throws Throwable {

		parseAspects(item, meta, toadd, true);
	}

	/**
	 * @param item
	 *            The {@link Item} to add aspects to.
	 *            <ol>
	 *            Will apply to <b>all metadatas</b>. <br>
	 *            Will <b>inherit aspects</b>.
	 *            </ol>
	 * @param toadd
	 *            A list of comma-separated aspects for scanning the Item
	 *            <ol>
	 *            <b>entry format</b>: '<code>\d+ .+</code>'
	 *            </ol>
	 * @throws Throwable
	 *             Any errors caused by reflection
	 */
	public static void parseAspects(Item item, String toadd) throws Throwable {

		parseAspects(item, OreDictionary.WILDCARD_VALUE, toadd, true);
	}

	/* BLOCKS */
	/**
	 * @param item
	 *            The {@link Block} to add aspects to.
	 * @param meta
	 *            The metadata of the Block
	 * @param toadd
	 *            A list of comma-separated aspects for scanning the Block
	 *            <ol>
	 *            <b>entry format</b>: '<code>\d+ .+</code>'
	 *            </ol>
	 * @param craftedAspects
	 *            True if the item should inherit aspects from its crafting ingredients
	 * @throws Throwable
	 *             Any errors caused by reflection
	 */
	public static void parseAspects(Block item, int meta, String toadd, boolean craftedAspects) throws Throwable {

		parseAspects(new ItemStack(item, 1, meta), toadd, craftedAspects);
	}

	/**
	 * @param item
	 *            The {@link Block} to add aspects to.
	 *            <ol>
	 *            Will apply to <b>all metadatas</b>.
	 *            </ol>
	 * @param toadd
	 *            A list of comma-separated aspects for scanning the Block
	 *            <ol>
	 *            <b>entry format</b>: '<code>\d+ .+</code>'
	 *            </ol>
	 * @param craftedAspects
	 *            True if the item should inherit aspects from its crafting ingredients
	 * @throws Throwable
	 *             Any errors caused by reflection
	 */
	public static void parseAspects(Block item, String toadd, boolean craftedAspects) throws Throwable {

		parseAspects(item, OreDictionary.WILDCARD_VALUE, toadd, craftedAspects);
	}

	/**
	 * @param item
	 *            The {@link Block} to add aspects to.
	 *            <ol>
	 *            Will <b>inherit aspects</b>.
	 *            </ol>
	 * @param meta
	 *            The metadata of the Block
	 * @param toadd
	 *            A list of comma-separated aspects for scanning the Block
	 *            <ol>
	 *            <b>entry format</b>: '<code>\d+ .+</code>'
	 *            </ol>
	 * @throws Throwable
	 *             Any errors caused by reflection
	 */
	public static void parseAspects(Block item, int meta, String toadd) throws Throwable {

		parseAspects(item, meta, toadd, true);
	}

	/**
	 * @param item
	 *            The {@link Block} to add aspects to.
	 *            <ol>
	 *            Will apply to <b>all metadatas</b>. <br>
	 *            Will <b>inherit aspects</b>.
	 *            </ol>
	 * @param toadd
	 *            A list of comma-separated aspects for scanning the Block
	 *            <ol>
	 *            <b>entry format</b>: '<code>\d+ .+</code>'
	 *            </ol>
	 * @throws Throwable
	 *             Any errors caused by reflection
	 */
	public static void parseAspects(Block item, String toadd) throws Throwable {

		parseAspects(item, OreDictionary.WILDCARD_VALUE, toadd, true);
	}

}
