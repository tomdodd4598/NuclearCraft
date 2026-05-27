package nc.handler;

import it.unimi.dsi.fastutil.objects.*;
import nc.Global;
import nc.init.NCItems;
import nc.util.Lazy;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.*;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

import static nc.config.NCConfig.dungeon_loot;
import static net.minecraft.world.storage.loot.LootTableList.*;

public class DungeonLootHandler {
	
	private static final Lazy<ObjectSet<ResourceLocation>> LOOT_TARGETS = new Lazy<>(() -> new ObjectOpenHashSet<>(Arrays.asList(CHESTS_SIMPLE_DUNGEON, CHESTS_VILLAGE_BLACKSMITH, CHESTS_ABANDONED_MINESHAFT, CHESTS_STRONGHOLD_LIBRARY, CHESTS_STRONGHOLD_CROSSING, CHESTS_STRONGHOLD_CORRIDOR, CHESTS_WOODLAND_MANSION)));
	
	@SubscribeEvent
	public void onLootTableLoad(LootTableLoadEvent event) {
		if (!dungeon_loot) {
			return;
		}
		
		ResourceLocation name = event.getName();
		if (name == null || !LOOT_TARGETS.get().contains(name)) {
			return;
		}
		
		LootTable table = event.getTable();
		if (table == null) {
			return;
		}
		
		LootCondition[] emptyCondition = new LootCondition[0];
		LootPool pool = table.getPool("main");
		if (pool == null) {
			pool = new LootPool(new LootEntry[0], emptyCondition, new RandomValueRange(3, 8), new RandomValueRange(0), "main");
			table.addPool(pool);
		}
		
		pool.addEntry(new LootEntryItem(NCItems.dominos, 4, 0, lootFunctionArray(0, 0, 2, 4), emptyCondition, Global.MOD_ID + ":dominos"));
		pool.addEntry(new LootEntryItem(NCItems.smore, 4, 0, lootFunctionArray(0, 0, 2, 4), emptyCondition, Global.MOD_ID + ":smore"));
		pool.addEntry(new LootEntryItem(NCItems.moresmore, 2, 0, lootFunctionArray(0, 0, 1, 2), emptyCondition, Global.MOD_ID + ":smore"));
		pool.addEntry(new LootEntryItem(NCItems.record_end_of_the_world, 1, 0, lootFunctionArray(0, 0, 1, 1), emptyCondition, Global.MOD_ID + ":record_end_of_the_world"));
		pool.addEntry(new LootEntryItem(NCItems.record_money_for_nothing, 1, 0, lootFunctionArray(0, 0, 1, 1), emptyCondition, Global.MOD_ID + ":record_money_for_nothing"));
		pool.addEntry(new LootEntryItem(NCItems.record_wanderer, 1, 0, lootFunctionArray(0, 0, 1, 1), emptyCondition, Global.MOD_ID + ":record_wanderer"));
		pool.addEntry(new LootEntryItem(NCItems.record_hyperspace, 1, 0, lootFunctionArray(0, 0, 1, 1), emptyCondition, Global.MOD_ID + ":record_hyperspace"));
	}
	
	private static LootFunction[] lootFunctionArray(float metaMin, float metaMax, float countMin, float countMax) {
		LootCondition[] noCondition = new LootCondition[0];
		LootFunction damage = new SetMetadata(noCondition, new RandomValueRange(metaMin, metaMax));
		LootFunction amount = new SetCount(noCondition, new RandomValueRange(countMin, countMax));
		return new LootFunction[] {damage, amount};
	}
}
