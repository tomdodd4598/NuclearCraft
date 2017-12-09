package nc.handler;

import java.util.Random;

import nc.config.NCConfig;
import nc.init.NCItems;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DropHandler {
	
	private Random rand = new Random();
	
	@SubscribeEvent
	public void addEntityDrop(LivingDropsEvent event) {
		if (NCConfig.rare_drops && event.getEntity().getEntityWorld().getGameRules().getBoolean("doMobLoot")) {
			if (event.getEntity() instanceof EntityMob && rand.nextInt(100) < 1) {
				ItemStack stack = new ItemStack(NCItems.dominos, 1);
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
			if (NCConfig.ore_drops[2]) blockDrop(event, new ItemStack(NCItems.dust, 1, 10), Blocks.QUARTZ_ORE, 25);
			if (NCConfig.ore_drops[3]) blockDrop(event, new ItemStack(NCItems.gem, 2, 2), Blocks.LAPIS_ORE, 95);
			if (NCConfig.ore_drops[4]) blockDrop(event, new ItemStack(NCItems.gem_dust, 1, 6), Blocks.COAL_ORE, 18);
		}
	}
	
	public void blockDrop(HarvestDropsEvent event, ItemStack drop, Block block, int chance) {
		if((event.getState().getBlock() == block) && rand.nextInt(100) < chance) {
			ItemStack heldItem = ItemStack.EMPTY;
			if (event.getHarvester() != null) {
				if (event.getHarvester().getHeldItemMainhand() != null) heldItem = event.getHarvester().getHeldItemMainhand();
			}
			boolean silkTouch = false;
			if (heldItem != ItemStack.EMPTY && heldItem != null) {
				if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, heldItem) > 0) silkTouch = true;
			}
			if (!silkTouch) event.getDrops().add(drop);
		}
	}
}
