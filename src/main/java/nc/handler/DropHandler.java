package nc.handler;

import java.util.Random;

import nc.init.NCItems;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DropHandler {
	
	private Random rand = new Random();
	
	@SubscribeEvent
	public void addEntityDrop(LivingDropsEvent event) {
		if (event.getEntity() instanceof EntityMob && rand.nextInt(100) < 1) {
			ItemStack stack = new ItemStack(NCItems.dominos, 1);
			event.getDrops().add(new EntityItem(event.getEntity().world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, stack));
		}
	}
	
	@SubscribeEvent
	public void addBlockDrop(HarvestDropsEvent event) {
		if((event.getState().getBlock() == Blocks.REDSTONE_ORE || event.getState().getBlock() == Blocks.LIT_REDSTONE_ORE) && rand.nextInt(8) < 1) {
			event.getDrops().add(new ItemStack(NCItems.gem, 1, 0));
		}
	}
}
