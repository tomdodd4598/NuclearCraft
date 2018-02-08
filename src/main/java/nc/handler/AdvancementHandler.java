/*package nc.handler;

import java.util.ArrayList;
import java.util.List;

import nc.Global;
import net.minecraft.advancements.Advancement;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.AdvancementPage;

public class AdvancementHandler {
	
	private static List<Advancement> advancements = new ArrayList<Advancement>();

	public static void registerAdvancements() {
		Advancement[] advancementArray = new Advancement[advancements.size()];
		for(Advancement advancement : advancements) {
			advancement.registerStat();
			advancementArray[advancements.indexOf(advancement)] = advancement;
		}
		AdvancementPage.registerAdvancementPage(new AdvancementPage(Global.MOD_NAME, advancementArray));
	}
	
	private static Advancement createAdvancement(String name, int column, int row, Item item) {
		Advancement advancement = new Advancement("advancement." + name, name, column, row, item, (Advancement)null);
		advancements.add(advancement);
		return advancement;
	}
	
	private static Advancement createAdvancement(String name, int column, int row, Block block) {
		Advancement advancement = new Advancement("advancement." + name, name, column, row, block, (Advancement)null);
		advancements.add(advancement);
		return advancement;
	}
	
	private static Advancement createAdvancement(String name, int column, int row, ItemStack stack) {
		Advancement advancement = new Advancement("advancement." + name, name, column, row, stack, (Advancement)null);
		advancements.add(advancement);
		return advancement;
	}

}*/
