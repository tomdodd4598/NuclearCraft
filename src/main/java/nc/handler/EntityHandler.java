package nc.handler;

import nc.entity.EntityFeralGhoul;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityHandler {
	
	@SubscribeEvent
	public void onLootTableLoad(LivingSpawnEvent.CheckSpawn event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (entity instanceof EntityFeralGhoul) {
			World world = entity.world;
			if (!event.isSpawner() && (!world.canSeeSky(new BlockPos(entity.posX, entity.getEntityBoundingBox().minY, entity.posZ)) || world.countEntities(EntityFeralGhoul.class) > 20)) {
				event.setResult(Result.DENY);
			}
		}
	}
}
