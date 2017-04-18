package nc.handler;

import java.util.Random;

import nc.NuclearCraft;
import nc.item.NCItems;
import net.minecraft.entity.monster.EntityMob;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EntityDropHandler {
	public Random rand = new Random();
	
	@SubscribeEvent
	public void onEntityDrop(LivingDropsEvent event) {
		if (NuclearCraft.extraDrops && event.entity.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
			if(event.entityLiving instanceof EntityMob) {
				if (rand.nextInt(100) < 1) event.entityLiving.dropItem(NCItems.dominoes, 1);
				if (rand.nextInt(100) < 4) event.entityLiving.dropItem(NCItems.ricecake, 1);
				if (rand.nextInt(100) < 1) event.entityLiving.dropItem(NCItems.fishAndRicecake, 1);
				if (rand.nextInt(100) < 5) event.entityLiving.dropItem(NCItems.dUBullet, 1 + rand.nextInt(2));
				if (rand.nextInt(400) < 1) event.entityLiving.dropItem(NCItems.recordArea51, 1);
				if (rand.nextInt(400) < 1) event.entityLiving.dropItem(NCItems.recordNeighborhood, 1);
				if (rand.nextInt(400) < 1) event.entityLiving.dropItem(NCItems.recordPractice, 1);
			}
		}
	} 
}