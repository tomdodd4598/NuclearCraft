package nc.handler;

import static nc.config.NCConfig.*;

import nc.capability.radiation.source.IRadiationSource;
import nc.entity.EntityFeralGhoul;
import nc.radiation.RadiationHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

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
				loop: for (int i = 0; i < entityListArray.length; ++i) {
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
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onItemExpired(ItemExpireEvent event) {
		if (event.isCanceled()) {
			return;
		}
		
		final EntityItem entity = event.getEntityItem();
		if (entity == null || entity.world == null) {
			return;
		}
		
		final ItemStack stack = entity.getItem();
		if (stack.isEmpty()) {
			return;
		}
		
		if (radiation_enabled_public && radiation_hardcore_stacks) {
			Chunk chunk = entity.world.getChunk(new BlockPos(entity));
			if (chunk.isLoaded()) {
				IRadiationSource chunkSource = RadiationHelper.getRadiationSource(chunk);
				if (chunkSource != null) {
					RadiationHelper.addToSourceRadiation(chunkSource, RadiationHelper.getRadiationFromStack(stack, 8D));
				}
			}
		}
	}
}
