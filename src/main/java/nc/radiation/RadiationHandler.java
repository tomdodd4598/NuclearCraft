package nc.radiation;

import nc.ModCheck;
import nc.capability.radiation.entity.IEntityRads;
import nc.capability.radiation.source.IRadiationSource;
import nc.init.NCSounds;
import nc.network.radiation.PlayerRadsUpdatePacket;
import nc.util.*;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.*;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import vazkii.patchouli.common.item.ItemModBook;

import java.util.*;

import static nc.config.NCConfig.*;

public class RadiationHandler {
	
	public static final Random RAND = new Random();
	
	public static final String RAD_X_WORE_OFF = Lang.localize("message.nuclearcraft.rad_x_wore_off");
	public static final String RAD_WARNING = Lang.localize("message.nuclearcraft.rad_warning");
	
	public static boolean default_rad_immunity = false;
	public static String[] rad_immunity_stages = new String[] {};
	
	private final WorldRadiationHandler worldRadiationHandler = new WorldRadiationHandler();
	
	@SubscribeEvent
	public void updatePlayerRadiation(TickEvent.PlayerTickEvent event) {
		if (radiation_enabled_public && !radiation_require_counter && event.phase == TickEvent.Phase.START && event.side == Side.CLIENT) {
			playGeigerSound(event.player);
		}
		
		UUID playerUUID = event.player.getUniqueID();
		if (event.phase != TickEvent.Phase.START || (event.player.world.getTotalWorldTime() + playerUUID.hashCode()) % radiation_player_tick_rate != 0) {
			return;
		}
		
		if (event.side == Side.SERVER && event.player instanceof EntityPlayerMP player) {
			IEntityRads playerRads = RadiationHelper.getEntityRadiation(player);
			if (playerRads == null) {
				return;
			}
			
			if (give_guidebook && ModCheck.patchouliLoaded() && playerRads.getGiveGuidebook()) {
				boolean success = player.inventory.addItemStackToInventory(ItemModBook.forBook("nuclearcraft:guide"));
				if (success) {
					playerRads.setGiveGuidebook(false);
				}
			}
			
			if (!radiation_enabled_public) {
				return;
			}
			
			if (ModCheck.gameStagesLoaded()) {
				playerRads.setRadiationImmunityStage(default_rad_immunity ^ GameStageHelper.hasAnyOf(player, rad_immunity_stages));
			}
			
			String uuidString = playerUUID.toString();
			for (String uuid : radiation_immune_players) {
				if (uuidString.equals(uuid)) {
					playerRads.setRadiationImmunityStage(true);
					break;
				}
			}
			
			if (radiation_player_rads_fatal && !player.isCreative() && !player.isSpectator() && !playerRads.isImmune() && playerRads.isFatal()) {
				player.attackEntityFrom(DamageSources.FATAL_RADS, Float.MAX_VALUE);
			}
			
			double previousImmunityTime = playerRads.getRadiationImmunityTime();
			if (previousImmunityTime > 0D) {
				playerRads.setRadiationImmunityTime(previousImmunityTime - radiation_player_tick_rate);
			}
			double previousRadPercentage = playerRads.getRadsPercentage();
			
			playerRads.setExternalRadiationResistance(RadiationHelper.getArmorInventoryRadResistance(player));
			
			if (radiation_player_decay_rate > 0D) {
				playerRads.setTotalRads(playerRads.getTotalRads() * Math.pow(1D - radiation_player_decay_rate, radiation_player_tick_rate), false);
			}
			
			double radiationLevel = RadiationHelper.transferRadsFromInventoryToPlayer(playerRads, player, radiation_player_tick_rate);
			Chunk chunk = player.world.getChunk((int) Math.floor(player.posX) >> 4, (int) Math.floor(player.posZ) >> 4);
			if (chunk.isLoaded()) {
				IRadiationSource chunkSource = RadiationHelper.getRadiationSource(chunk);
				radiationLevel += RadiationHelper.transferRadsToPlayer(chunkSource, playerRads, player, radiation_player_tick_rate);
			}
			
			if (playerRads.getPoisonBuffer() > 0D) {
				double poisonRads = Math.min(playerRads.getPoisonBuffer() / radiation_player_tick_rate, playerRads.getRecentPoisonAddition() / radiation_poison_time);
				radiationLevel += RadiationHelper.addRadsToEntity(playerRads, player, poisonRads, true, true, radiation_player_tick_rate);
				playerRads.setPoisonBuffer(playerRads.getPoisonBuffer() - poisonRads * radiation_player_tick_rate);
				if (playerRads.getPoisonBuffer() == 0D) {
					playerRads.resetRecentPoisonAddition();
				}
			}
			else {
				playerRads.resetRecentPoisonAddition();
			}
			
			playerRads.setMessageCooldownTime(playerRads.getMessageCooldownTime() - radiation_player_tick_rate);
			
			playerRads.setRadiationLevel(radiationLevel);
			
			if (!player.isCreative() && !player.isSpectator() && !playerRads.isImmune()) {
				if (radiation_player_rads_fatal && playerRads.isFatal()) {
					player.attackEntityFrom(DamageSources.FATAL_RADS, Float.MAX_VALUE);
				}
				else if (!RadPotionEffects.PLAYER_RAD_LEVEL_LIST.isEmpty() && previousRadPercentage < RadPotionEffects.PLAYER_RAD_LEVEL_LIST.get(0) && playerRads.getRadsPercentage() >= RadPotionEffects.PLAYER_RAD_LEVEL_LIST.get(0) && !RadiationHelper.shouldShowHUD(player)) {
					playerRads.setShouldWarn(true);
				}
				else {
					playerRads.setShouldWarn(false);
				}
			}
			
			double previousInternalResistance = playerRads.getInternalRadiationResistance();
			double recentRadXAdditionModified = radiation_rad_x_amount * (1D + playerRads.getRecentRadXAddition()) / (1D + radiation_rad_x_amount);
			if (previousInternalResistance > 0D) {
				double radXDecayRate = Math.max(previousInternalResistance, recentRadXAdditionModified) / radiation_rad_x_lifetime;
				playerRads.setInternalRadiationResistance(Math.max(0D, previousInternalResistance - radXDecayRate * radiation_player_tick_rate));
				if (playerRads.getInternalRadiationResistance() == 0D) {
					playerRads.resetRecentRadXAddition();
					playerRads.setRadXWoreOff(true);
				}
			}
			else {
				if (previousInternalResistance < 0D) {
					double radXDecayRate = Math.max(-previousInternalResistance, recentRadXAdditionModified) / radiation_rad_x_lifetime;
					playerRads.setInternalRadiationResistance(Math.min(0D, previousInternalResistance + radXDecayRate * radiation_player_tick_rate));
					if (playerRads.getInternalRadiationResistance() == 0D) {
						playerRads.resetRecentRadXAddition();
					}
				}
				else {
					playerRads.resetRecentRadXAddition();
				}
				playerRads.setRadXWoreOff(false);
			}
			
			if (playerRads.getRadXWoreOff() && playerRads.getRadXUsed()) {
				playerRads.setRadXUsed(false);
			}
			
			if (playerRads.getRadawayBuffer(false) > 0D) {
				double change = Math.min(playerRads.getRadawayBuffer(false), playerRads.getRecentRadawayAddition() * radiation_radaway_rate * radiation_player_tick_rate / radiation_radaway_amount);
				playerRads.setTotalRads(playerRads.getTotalRads() - change, false);
				playerRads.setRadawayBuffer(false, playerRads.getRadawayBuffer(false) - change);
				if (playerRads.getRadawayBuffer(false) == 0D) {
					playerRads.resetRecentRadawayAddition();
				}
			}
			else {
				playerRads.resetRecentRadawayAddition();
			}
			
			if (playerRads.getRadawayBuffer(true) > 0D) {
				double change = Math.min(playerRads.getRadawayBuffer(true), radiation_radaway_slow_rate * radiation_player_tick_rate);
				playerRads.setTotalRads(playerRads.getTotalRads() - change, false);
				playerRads.setRadawayBuffer(true, playerRads.getRadawayBuffer(true) - change);
			}
			
			if (playerRads.getRadawayCooldown() > 0D) {
				playerRads.setRadawayCooldown(playerRads.getRadawayCooldown() - radiation_player_tick_rate);
			}
			if (playerRads.getRadXCooldown() > 0D) {
				playerRads.setRadXCooldown(playerRads.getRadXCooldown() - radiation_player_tick_rate);
			}
			
			new PlayerRadsUpdatePacket(playerRads).sendTo(player);
			
			if (!player.isCreative() && !player.isSpectator() && !playerRads.isImmune()) {
				RadiationHelper.applyEntityEffects(player, playerRads, 1D, RadPotionEffects.PLAYER_RAD_LEVEL_LIST, RadPotionEffects.PLAYER_RAD_EFFECT_LISTS, RadPotionEffects.PLAYER_RAD_ATTRIBUTE_MAP);
			}
		}
		else {
			if (!radiation_enabled_public) {
				return;
			}
			
			EntityPlayer player = event.player;
			IEntityRads playerRads = RadiationHelper.getEntityRadiation(player);
			if (playerRads == null) {
				return;
			}
			if (playerRads.getRadXWoreOff() && playerRads.getRadXUsed()) {
				player.playSound(NCSounds.chems_wear_off, (float) (0.65D * radiation_sound_volumes[4]), 1F);
				player.sendMessage(new TextComponentString(TextFormatting.ITALIC + RAD_X_WORE_OFF));
			}
			if (playerRads.getShouldWarn()) {
				player.playSound(NCSounds.chems_wear_off, (float) (0.8D * radiation_sound_volumes[6]), 0.7F);
				player.sendMessage(new TextComponentString(TextFormatting.GOLD + RAD_WARNING));
			}
		}
	}
	
	@SubscribeEvent
	public void updateWorldRadiation(TickEvent.WorldTickEvent event) {
		if (!radiation_enabled_public) {
			return;
		}
		
		if (event.phase != TickEvent.Phase.START || event.side == Side.CLIENT || !(event.world instanceof WorldServer world)) {
			return;
		}
		
		worldRadiationHandler.update(world);
	}
	
	public static void playGeigerSound(EntityPlayer player) {
		IEntityRads entityRads = RadiationHelper.getEntityRadiation(player);
		if (entityRads == null || entityRads.isRawRadiationNegligible()) {
			return;
		}
		
		double radiation = entityRads.getRawRadiationLevel();
		int loops = radiation == 0D ? 0 : Math.min(4, NCMath.toInt(Math.log(Math.E + entityRads.getRawRadiationLevel())));
		if (loops == 0) {
			return;
		}
		
		double soundChance = Math.cbrt(entityRads.getRawRadiationLevel() / 200D);
		double soundVolume = MathHelper.clamp(8D * soundChance, 0.55D, 1.1D);
		for (int i = 0; i < loops; ++i) {
			if (RAND.nextDouble() < soundChance) {
				player.playSound(NCSounds.geiger_tick, (float) ((soundVolume + RAND.nextDouble() * 0.12D) * radiation_sound_volumes[0]), 0.92F + RAND.nextFloat() * 0.16F);
			}
		}
	}
}
