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
	
	private static List<Advancement> achievements = new ArrayList<Advancement>();

	public static void registerAdvancements() {
		Advancement[] achievementArray = new Advancement[achievements.size()];
		for(Advancement achievement : achievements) {
			achievement.registerStat();
			achievementArray[achievements.indexOf(achievement)] = achievement;
		}
		AdvancementPage.registerAdvancementPage(new AdvancementPage(Global.MOD_NAME, achievementArray));
	}
	
	private static Advancement createAdvancement(String name, int column, int row, Item item) {
		Advancement acheivement = new Advancement("achievement." + name, name, column, row, item, (Advancement)null);
		achievements.add(acheivement);
		return acheivement;
	}
	
	private static Advancement createAdvancement(String name, int column, int row, Block block) {
		Advancement acheivement = new Advancement("achievement." + name, name, column, row, block, (Advancement)null);
		achievements.add(acheivement);
		return acheivement;
	}
	
	private static Advancement createAdvancement(String name, int column, int row, ItemStack stack) {
		Advancement acheivement = new Advancement("achievement." + name, name, column, row, stack, (Advancement)null);
		achievements.add(acheivement);
		return acheivement;
	}

}*/
