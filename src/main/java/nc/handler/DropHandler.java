package nc.handler;

import nc.config.NCConfig;
import nc.init.NCItems;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class DropHandler {
	
	private Random rand = new Random();
	
	@SubscribeEvent
	public void addEntityDrop(LivingDropsEvent event) {
		if (NCConfig.rare_drops && event.getEntity().getEntityWorld().getGameRules().getBoolean("doMobLoot")) {
			if (event.getEntity() instanceof EntityMob && rand.nextInt(100) < 1) {
				ItemStack stack = new ItemStack(rand.nextInt(2) < 1 ? NCItems.dominos : NCItems.marshmallow, 1);
				event.getDrops().add(new EntityItem(event.getEntity().getEntityWorld(), event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, stack));
			}
		}
	}
	
	@SubscribeEvent
	public void addBlockDrop(HarvestDropsEvent event) {
		if (event.getWorld().getGameRules().getBoolean("doTileDrops")) {
			if (NCConfig.ore_drops[0]) blockDrop(event, new ItemStack(NCItems.gem, 1, 0), Blocks.REDSTONE_ORE, 25);
			if (NCConfig.ore_drops[0]) blockDrop(event, new ItemStack(NCItems.gem, 1, 0), Blocks.LIT_REDSTONE_ORE, 25);
			if (NCConfig.ore_drops[1]) blockDrop(event, new ItemStack(NCItems.dust, 1, 9), Blocks.COAL_ORE, 18);
			if (NCConfig.ore_drops[2]) blockDrop(event, new ItemStack(NCItems.dust, 1, 10), Blocks.QUARTZ_ORE, 18);
			if (NCConfig.ore_drops[3]) blockDrop(event, new ItemStack(NCItems.gem, 2, 2), Blocks.LAPIS_ORE, 95);
			if (NCConfig.ore_drops[4]) blockDrop(event, new ItemStack(NCItems.gem_dust, 1, 6), Blocks.COAL_ORE, 18);
			if (NCConfig.ore_drops[5]) blockDrop(event, NCItems.gem, 1, 3, Blocks.DIAMOND_ORE, 100);
			if (NCConfig.ore_drops[6]) blockDrop(event, NCItems.gem, 4, 4, Blocks.DIAMOND_ORE, 100);
		}
	}
	
	public void blockDrop(HarvestDropsEvent event, ItemStack drop, Block block, int chance) {
		if((event.getState().getBlock() == block) && rand.nextInt(100) < chance) {
			if (!event.isSilkTouching()) event.getDrops().add(drop);
		}
	}
	
	public void blockDrop(HarvestDropsEvent event, Item drop, int maxAmount, int meta, Block block, int chance) {
		if((event.getState().getBlock() == block) && rand.nextInt(100) < chance) {
			if (!event.isSilkTouching()) event.getDrops().add(new ItemStack(drop, rand.nextInt(maxAmount + 1), meta));
		}
	}
}
