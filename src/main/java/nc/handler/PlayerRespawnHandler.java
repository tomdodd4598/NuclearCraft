package nc.handler;

import nc.capability.radiation.IEntityRads;
import nc.config.NCConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerRespawnHandler {
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.Clone event) {
		EntityPlayer newPlayer = event.getEntityPlayer();
		EntityPlayer oldPlayer = event.getOriginal();
		if (newPlayer == null || oldPlayer == null) return;
		
		if (event.isWasDeath()) {
			if (NCConfig.radiation_enabled && NCConfig.radiation_death_persist) {
				if (!oldPlayer.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) return;
				IEntityRads oldRads = oldPlayer.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
				if (oldRads == null) return;
				
				if (oldRads.isFatal()) return;
				
				if (!newPlayer.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) return;
				IEntityRads newRads = newPlayer.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
				if (newRads == null) return;
				
				newRads.setTotalRads(oldRads.getTotalRads()*NCConfig.radiation_death_persist_fraction);
			}
		}
	}
}
