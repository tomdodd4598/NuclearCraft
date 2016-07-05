package nc.handler;

import java.util.Random;

import nc.NuclearCraft;
import nc.block.NCBlocks;
import nc.item.NCItems;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BlockDropHandler {
	public Random rand = new Random();
	
	public void extra(BlockEvent.HarvestDropsEvent event, Block block, ItemStack drop) {
		if (event.harvester != null) if (event.block == block && event.harvester.getHeldItem() != null && !event.isSilkTouching) event.drops.add(drop);
	}
	
	public void chanceExtra(BlockEvent.HarvestDropsEvent event, Block block, ItemStack drop, int chance) {
		if (rand.nextInt(100) < chance) extra(event, block, drop);
	}
	
	public void extraMeta(BlockEvent.HarvestDropsEvent event, Block block, int meta, ItemStack drop) {
		if (event.harvester != null) if (event.block == block && event.blockMetadata == meta && event.harvester.getHeldItem() != null && !event.isSilkTouching) event.drops.add(drop);
	}
	
	public void chanceMetaExtra(BlockEvent.HarvestDropsEvent event, Block block, int meta, ItemStack drop, int chance) {
		if (rand.nextInt(100) < chance) extraMeta(event, block, meta, drop);
	}
	
	@SubscribeEvent
	public void onBlockDrops(BlockEvent.HarvestDropsEvent event) {
		if (NuclearCraft.extraDrops) {
			chanceExtra(event, Blocks.quartz_ore, new ItemStack(NCItems.material, 1, 70), 50);
			
			chanceMetaExtra(event, NCBlocks.blockOre, 6, new ItemStack(NCItems.material, 1, 33), 25);
			chanceMetaExtra(event, NCBlocks.blockOre, 6, new ItemStack(NCItems.material, 1, 35), 25);
			chanceMetaExtra(event, NCBlocks.blockOre, 6, new ItemStack(NCItems.material, 1, 37), 25);
			chanceMetaExtra(event, NCBlocks.blockOre, 6, new ItemStack(NCItems.material, 1, 62), 5);
		}
		chanceExtra(event, Blocks.redstone_ore, new ItemStack(NCItems.material, 1, 73), 10);
		chanceExtra(event, Blocks.lit_redstone_ore, new ItemStack(NCItems.material, 1, 73), 10);
	}
}