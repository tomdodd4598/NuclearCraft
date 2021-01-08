package nc.handler;

import nc.entity.EntityFeralGhoul;
import net.minecraft.entity.*;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityHandler {
	
	@SubscribeEvent
	public void onEntityLivingSpawn(LivingSpawnEvent.CheckSpawn event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (entity instanceof EntityFeralGhoul) {
			if (!event.isSpawner()) {
				World world = entity.world;
				BlockPos pos = new BlockPos(entity.posX, entity.posY, entity.posZ);
				
				boolean canSeeSky = world.canSeeSky(pos);
				
				if (!canSeeSky) {
					event.setResult(Result.DENY);
					return;
				}
				
				boolean tooManyGhouls = false;
				ClassInheritanceMultiMap<Entity>[] entityListArray = world.getChunk(pos).getEntityLists();
				loop: for (int i = 0; i < entityListArray.length; i++) {
					Iterable<EntityFeralGhoul> ghouls = entityListArray[i].getByClass(EntityFeralGhoul.class);
					while (ghouls.iterator().hasNext()) {
						tooManyGhouls = true;
						break loop;
					}
				}
				
				if (tooManyGhouls) {
					event.setResult(Result.DENY);
					return;
				}
			}
		}
	}
}
