package nc.handler;

import static nc.config.NCConfig.*;

import java.util.Random;

import nc.entity.EntityFeralGhoul;
import nc.init.NCItems;
import nc.util.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DropHandler {
	
	private final Random rand = new Random();
	
	@SubscribeEvent
	public void addEntityDrop(LivingDropsEvent event) {
		Entity entity = event.getEntity();
		if (entity.getEntityWorld().getGameRules().getBoolean("doMobLoot")) {
			if (entity instanceof EntityFeralGhoul) {
				if (rand.nextInt(50) < 1) {
					event.getDrops().add(entityItem(entity, new ItemStack(NCItems.ingot, 1, 3), "ingotThorium"));
				}
				if (rand.nextInt(50) < 1) {
					event.getDrops().add(entityItem(entity, new ItemStack(NCItems.ingot, 1, 4), "ingotUranium"));
				}
			}
			
			if (rare_drops) {
				if (entity instanceof EntityMob) {
					if (rand.nextInt(100) < 1) {
						event.getDrops().add(entityItem(entity, new ItemStack(NCItems.dominos, 1), null));
					}
				}
			}
		}
	}
	
	private static EntityItem entityItem(Entity entity, ItemStack stack, String oreName) {
		return new EntityItem(entity.getEntityWorld(), entity.posX, entity.posY, entity.posZ, OreDictHelper.getPrioritisedCraftingStack(stack, oreName));
	}
	
	@SubscribeEvent
	public void addBlockDrop(HarvestDropsEvent event) {
		if (event.getWorld().getGameRules().getBoolean("doTileDrops") && !event.isSilkTouching()) {
			if (ore_drops[0]) {
				blockDrop(event, new ItemStack(NCItems.gem, 1, 0), Blocks.REDSTONE_ORE, 25);
			}
			if (ore_drops[0]) {
				blockDrop(event, new ItemStack(NCItems.gem, 1, 0), Blocks.LIT_REDSTONE_ORE, 25);
			}
			if (ore_drops[1]) {
				blockDrop(event, new ItemStack(NCItems.dust, 1, 9), Blocks.COAL_ORE, 18);
			}
			if (ore_drops[2]) {
				blockDrop(event, new ItemStack(NCItems.dust, 1, 10), Blocks.QUARTZ_ORE, 18);
			}
			if (ore_drops[3]) {
				blockDrop(event, new ItemStack(NCItems.gem, 2, 2), Blocks.LAPIS_ORE, 95);
			}
			if (ore_drops[4]) {
				blockDrop(event, new ItemStack(NCItems.gem_dust, 1, 6), Blocks.COAL_ORE, 18);
			}
			if (ore_drops[5]) {
				blockDrop(event, NCItems.gem, 1, 3, Blocks.DIAMOND_ORE, 100);
			}
			if (ore_drops[6]) {
				blockDrop(event, NCItems.gem, 4, 4, Blocks.DIAMOND_ORE, 100);
			}
		}
	}
	
	private void blockDrop(HarvestDropsEvent event, ItemStack drop, Block block, int chance) {
		if (event.getState().getBlock() == block && rand.nextInt(100) < chance) {
			event.getDrops().add(drop);
		}
	}
	
	private void blockDrop(HarvestDropsEvent event, Item drop, int maxAmount, int meta, Block block, int chance) {
		blockDrop(event, new ItemStack(drop, rand.nextInt(maxAmount + 1), meta), block, chance);
	}
}
