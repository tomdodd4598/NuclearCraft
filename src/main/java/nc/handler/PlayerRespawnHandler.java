package nc.handler;

import static nc.config.NCConfig.*;

import nc.capability.radiation.entity.IEntityRads;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerRespawnHandler {
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.Clone event) {
		EntityPlayer newPlayer = event.getEntityPlayer();
		EntityPlayer oldPlayer = event.getOriginal();
		if (newPlayer == null || oldPlayer == null) {
			return;
		}
		
		if (radiation_enabled_public) {
			if (!oldPlayer.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) {
				return;
			}
			IEntityRads oldRads = oldPlayer.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
			if (oldRads == null) {
				return;
			}
			
			if (!newPlayer.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) {
				return;
			}
			IEntityRads newRads = newPlayer.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
			if (newRads == null) {
				return;
			}
			
			if (event.isWasDeath()) {
				if (radiation_death_persist) {
					newRads.setTotalRads(oldRads.getTotalRads() * radiation_death_persist_fraction % oldRads.getMaxRads(), false);
					newRads.setRadiationImmunityTime(oldRads.getTotalRads() * radiation_death_immunity_time * 20D / oldRads.getMaxRads());
				}
			}
			else {
				newRads.setTotalRads(oldRads.getTotalRads(), false);
			}
		}
	}
}
