package nc.handler;

import static nc.config.NCConfig.*;

import nc.ModCheck;
import nc.capability.radiation.entity.IEntityRads;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import vazkii.patchouli.common.item.ItemModBook;

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
				}
				newRads.setRadiationImmunityTime(radiation_death_immunity_time * 20D);
			}
			else {
				newRads.setConsumedMedicine(oldRads.getConsumedMedicine());
				newRads.setExternalRadiationResistance(oldRads.getExternalRadiationResistance());
				newRads.setInternalRadiationResistance(oldRads.getInternalRadiationResistance());
				newRads.setPoisonBuffer(oldRads.getPoisonBuffer());
				newRads.setRadawayBuffer(false, oldRads.getRadawayBuffer(false));
				newRads.setRadawayBuffer(true, oldRads.getRadawayBuffer(true));
				newRads.setRadawayCooldown(oldRads.getRadawayCooldown());
				newRads.setRadiationImmunityStage(oldRads.getRadiationImmunityStage());
				newRads.setRadiationImmunityTime(oldRads.getRadiationImmunityTime());
				newRads.setRadXCooldown(oldRads.getRadXCooldown());
				newRads.setRadXUsed(oldRads.getRadXUsed());
				newRads.setRadXWoreOff(oldRads.getRadXWoreOff());
				newRads.setRecentPoisonAddition(oldRads.getRecentPoisonAddition());
				newRads.setRecentRadawayAddition(oldRads.getRecentRadawayAddition());
				newRads.setRecentRadXAddition(oldRads.getRecentRadXAddition());
				newRads.setShouldWarn(oldRads.getShouldWarn());
				newRads.setTotalRads(oldRads.getTotalRads(), false);
			}
			
			newRads.setGiveGuidebook(oldRads.getGiveGuidebook());
		}
	}
	
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		if (!player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) {
			return;
		}
		
		IEntityRads playerRads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
		if (playerRads == null) {
			return;
		}
		
		if (give_guidebook && ModCheck.patchouliLoaded() && playerRads.getGiveGuidebook()) {
			boolean success = player.inventory.addItemStackToInventory(ItemModBook.forBook("nuclearcraft:guide"));
			if (success) {
				playerRads.setGiveGuidebook(false);
			}
		}
	}
}
