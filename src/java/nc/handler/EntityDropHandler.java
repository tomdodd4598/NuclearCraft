package nc.handler;

import java.util.Random;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import nc.NuclearCraft;
import nc.item.NCItems;
import net.minecraft.entity.monster.EntityMob;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

public class EntityDropHandler {
	public Random rand = new Random();
	
	@SubscribeEvent
	public void onEntityDrop(LivingDropsEvent event) {
		if (NuclearCraft.extraDrops) {
			if(event.entityLiving instanceof EntityMob) {
				if (rand.nextInt(100) < 1) event.entityLiving.dropItem(NCItems.dominoes, 1);
				if (rand.nextInt(100) < 5) event.entityLiving.dropItem(NCItems.ricecake, 1);
				if (rand.nextInt(100) < 1) event.entityLiving.dropItem(NCItems.fishAndRicecake, 1);
				if (rand.nextInt(100) < 5) event.entityLiving.dropItem(NCItems.dUBullet, rand.nextInt(1));
				if (rand.nextInt(100) < 1) event.entityLiving.dropItem(NCItems.dUSword, rand.nextInt(1));
				if (rand.nextInt(100) < 1) event.entityLiving.dropItem(NCItems.dUHoe, rand.nextInt(1));
			}
		}
	} 
}