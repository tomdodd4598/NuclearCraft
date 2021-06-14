package nc.handler;

import static nc.config.NCConfig.dungeon_loot;

import nc.Global;
import nc.init.NCItems;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.*;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DungeonLootHandler {
	
	@SubscribeEvent
	public void onLootTableLoad(LootTableLoadEvent event) {
		if (event.getName() != null && event.getTable() != null) {
			LootCondition[] noCondition = new LootCondition[0];
			
			LootPool pool = event.getTable().getPool("main");
			if (pool == null) {
				pool = new LootPool(new LootEntry[0], noCondition, new RandomValueRange(5, 10), new RandomValueRange(0), "main");
				event.getTable().addPool(pool);
			}
			
			// boolean addPlating = false;
			// boolean addSolenoids = false;
			// boolean addMachinery = false;
			boolean addOther = false;
			
			if (dungeon_loot) {
				if (LootTableList.CHESTS_SIMPLE_DUNGEON.equals(event.getName())) {
					// addPlating = true;
					// addMachinery = true;
					addOther = true;
				}
				else if (LootTableList.CHESTS_ABANDONED_MINESHAFT.equals(event.getName())) {
					// addPlating = true;
					// addMachinery = true;
				}
				else if (LootTableList.CHESTS_VILLAGE_BLACKSMITH.equals(event.getName())) {
					// addPlating = true;
					// addSolenoids = true;
					addOther = true;
				}
				else if (LootTableList.CHESTS_STRONGHOLD_LIBRARY.equals(event.getName())) {
					addOther = true;
				}
				else if (LootTableList.CHESTS_STRONGHOLD_CROSSING.equals(event.getName())) {
					// addPlating = true;
					// addSolenoids = true;
					// addMachinery = true;
				}
				else if (LootTableList.CHESTS_STRONGHOLD_CORRIDOR.equals(event.getName())) {
					// addPlating = true;
					// addSolenoids = true;
					// addMachinery = true;
				}
				else if (LootTableList.CHESTS_IGLOO_CHEST.equals(event.getName())) {
					// addSolenoids = true;
					// addMachinery = true;
				}
				else if (LootTableList.CHESTS_DESERT_PYRAMID.equals(event.getName())) {
					// addSolenoids = true;
					addOther = true;
				}
				else if (LootTableList.CHESTS_NETHER_BRIDGE.equals(event.getName())) {
					// addPlating = true;
					// addSolenoids = true;
					// addMachinery = true;
				}
				else if (LootTableList.CHESTS_END_CITY_TREASURE.equals(event.getName())) {
					// addPlating = true;
					// addSolenoids = true;
					// addMachinery = true;
				}
				else if (LootTableList.CHESTS_WOODLAND_MANSION.equals(event.getName())) {
					addOther = true;
				}
				else if (LootTableList.CHESTS_JUNGLE_TEMPLE.equals(event.getName())) {
					// addPlating = true;
					// addSolenoids = true;
					// addMachinery = true;
				}
			}
			
			/*if (addPlating) {
				pool.addEntry(new LootEntryItem(NCItems.part, 15, 0, lootFunctions(0, 3, 3, 6), noCondition, Global.MOD_ID + ":plating"));
			}
			
			if (addSolenoids) {
				pool.addEntry(new LootEntryItem(NCItems.part, 15, 0, lootFunctions(4, 5, 4, 8), noCondition, Global.MOD_ID + ":solenoids"));
			}
			
			if (addMachinery) {
				pool.addEntry(new LootEntryItem(NCItems.part, 20, 0, lootFunctions(7, 9, 2, 4), noCondition, Global.MOD_ID + ":machinery"));
			}*/
			
			if (addOther) {
				pool.addEntry(new LootEntryItem(NCItems.dominos, 3, 0, lootFunctions(0, 0, 2, 4), noCondition, Global.MOD_ID + ":dominos"));
				pool.addEntry(new LootEntryItem(NCItems.milk_chocolate, 4, 0, lootFunctions(0, 0, 2, 4), noCondition, Global.MOD_ID + ":milk_chocolate"));
				pool.addEntry(new LootEntryItem(NCItems.marshmallow, 4, 0, lootFunctions(0, 0, 2, 4), noCondition, Global.MOD_ID + ":marshmallow"));
				pool.addEntry(new LootEntryItem(NCItems.smore, 3, 0, lootFunctions(0, 0, 2, 4), noCondition, Global.MOD_ID + ":smore"));
				pool.addEntry(new LootEntryItem(NCItems.record_end_of_the_world, 3, 0, lootFunctions(0, 0, 1, 1), noCondition, Global.MOD_ID + ":record_end_of_the_world"));
				pool.addEntry(new LootEntryItem(NCItems.record_money_for_nothing, 3, 0, lootFunctions(0, 0, 1, 1), noCondition, Global.MOD_ID + ":record_money_for_nothing"));
				pool.addEntry(new LootEntryItem(NCItems.record_wanderer, 3, 0, lootFunctions(0, 0, 1, 1), noCondition, Global.MOD_ID + ":record_wanderer"));
				pool.addEntry(new LootEntryItem(NCItems.record_hyperspace, 3, 0, lootFunctions(0, 0, 1, 1), noCondition, Global.MOD_ID + ":record_hyperspace"));
			}
		}
	}
	
	private static LootFunction[] lootFunctions(float metaMin, float metaMax, float countMin, float countMax) {
		LootCondition[] noCondition = new LootCondition[0];
		LootFunction damage = new SetMetadata(noCondition, new RandomValueRange(metaMin, metaMax));
		LootFunction amount = new SetCount(noCondition, new RandomValueRange(countMin, countMax));
		return new LootFunction[] {damage, amount};
	}
}
