package nc.radiation;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import nc.capability.radiation.entity.IEntityRads;
import nc.capability.radiation.source.IRadiationSource;
import nc.config.NCConfig;
import nc.handler.SoundHandler;
import nc.network.PacketHandler;
import nc.network.radiation.PlayerRadsUpdatePacket;
import nc.tile.radiation.ITileRadiationEnvironment;
import nc.util.Lang;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class RadiationHandler {
	
	private Random rand = new Random();
	
	public static final DamageSource FATAL_RADS = new DamageSource("fatal_rads").setDamageBypassesArmor().setDamageIsAbsolute();
	
	private static final String RAD_X_WORE_OFF = Lang.localise("message.nuclearcraft.rad_x_wore_off");
	private static final String RAD_WARNING = Lang.localise("message.nuclearcraft.rad_warning");
	
	private static final int WORLD_TICK_RATE = NCConfig.radiation_world_tick_rate, PLAYER_TICK_RATE = NCConfig.radiation_player_tick_rate;
			
	@SubscribeEvent
	public void updatePlayerRadiation(PlayerTickEvent event) {
		if (!NCConfig.radiation_enabled_public) return;
		
		if (!NCConfig.radiation_require_counter && event.phase == Phase.START && event.side == Side.CLIENT) {
			playGeigerSound(event.player);
		}
		
		if (event.phase != Phase.START || (event.player.world.getTotalWorldTime() % PLAYER_TICK_RATE) != 0) return;
		if (event.side == Side.SERVER && event.player instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP)event.player;
			IEntityRads playerRads = RadiationHelper.getEntityRadiation(player);
			if (playerRads == null) return;
			
			IRadiationSource chunkSource = RadiationHelper.getRadiationSource(player.world.getChunkFromChunkCoords((int) Math.floor(player.posX) >> 4, (int) Math.floor(player.posZ) >> 4));
			
			double previousImmunityTime = playerRads.getRadiationImmunityTime();
			if (previousImmunityTime > 0D) {
				playerRads.setRadiationImmunityTime(previousImmunityTime - PLAYER_TICK_RATE);
			}
			double previousRadPercentage = playerRads.getRadsPercentage();
			
			double radiationLevel = RadiationHelper.transferRadsToPlayer(chunkSource, playerRads, player, PLAYER_TICK_RATE) + RadiationHelper.transferRadsFromInventoryToPlayer(playerRads, player, PLAYER_TICK_RATE);
			playerRads.setRadiationLevel(radiationLevel);
			
			if (!player.isCreative()) {
				if (playerRads.isFatal()) {
					player.attackEntityFrom(FATAL_RADS, Float.MAX_VALUE);
				}
				else if (!RadEffects.PLAYER_RAD_LEVEL_LIST.isEmpty() && previousRadPercentage < RadEffects.PLAYER_RAD_LEVEL_LIST.get(0) && playerRads.getRadsPercentage() >= RadEffects.PLAYER_RAD_LEVEL_LIST.get(0) && !RadiationRenders.shouldShowHUD(player)) {
					playerRads.setShouldWarn(true);
				}
				else {
					playerRads.setShouldWarn(false);
				}
			}
			
			double previousResistance = playerRads.getRadiationResistance();
			if (previousResistance > 0D) {
				double radXDecayRate = Math.max(previousResistance, NCConfig.radiation_rad_x_amount)/NCConfig.radiation_rad_x_lifetime;
				playerRads.setRadiationResistance(previousResistance - radXDecayRate*PLAYER_TICK_RATE);
				if (playerRads.getRadiationResistance() == 0D) playerRads.setRadXWoreOff(true);
			}
			else playerRads.setRadXWoreOff(false);
			
			if (NCConfig.radiation_player_decay_rate > 0D) {
				playerRads.setTotalRads(playerRads.getTotalRads()*Math.pow(1D - NCConfig.radiation_player_decay_rate, PLAYER_TICK_RATE), false);
			}
			
			if (playerRads.getRadawayBuffer(false) > 0D) {
				playerRads.setTotalRads(playerRads.getTotalRads() - NCConfig.radiation_radaway_rate*PLAYER_TICK_RATE, false);
				playerRads.setRadawayBuffer(false, playerRads.getRadawayBuffer(false) - NCConfig.radiation_radaway_rate*PLAYER_TICK_RATE);
			}
			
			if (playerRads.getRadawayBuffer(true) > 0D) {
				playerRads.setTotalRads(playerRads.getTotalRads() - NCConfig.radiation_radaway_slow_rate*PLAYER_TICK_RATE, false);
				playerRads.setRadawayBuffer(true, playerRads.getRadawayBuffer(true) - NCConfig.radiation_radaway_slow_rate*PLAYER_TICK_RATE);
			}
			
			if (playerRads.getRadawayCooldown() > 0D) {
				playerRads.setRadawayCooldown(playerRads.getRadawayCooldown() - PLAYER_TICK_RATE);
			}
			if (playerRads.getRadXCooldown() > 0D) {
				playerRads.setRadXCooldown(playerRads.getRadXCooldown() - PLAYER_TICK_RATE);
			}
			
			PacketHandler.instance.sendTo(new PlayerRadsUpdatePacket(playerRads), player);
			
			if (!player.isCreative()) {
				RadiationHelper.applyPotionEffects(player, playerRads, RadEffects.PLAYER_RAD_LEVEL_LIST, RadEffects.PLAYER_DEBUFF_LIST);
			}
		}
		else {
			EntityPlayer player = event.player;
			IEntityRads playerRads = RadiationHelper.getEntityRadiation(player);
			if (playerRads == null) return;
			if (playerRads.getRadXWoreOff()) {
				player.playSound(SoundHandler.chems_wear_off, 0.65F, 1F);
				player.sendMessage(new TextComponentString(TextFormatting.ITALIC + RAD_X_WORE_OFF));
			}
			if (playerRads.getShouldWarn()) {
				player.playSound(SoundHandler.chems_wear_off, 0.8F, 0.7F);
				player.sendMessage(new TextComponentString(TextFormatting.GOLD + RAD_WARNING));
			}
		}
	}
	
	@SubscribeEvent
	public void updateChunkRadiation(WorldTickEvent event) {
		if (!NCConfig.radiation_enabled_public) return;
		
		if (event.phase != Phase.START || event.side == Side.CLIENT || (event.world.getTotalWorldTime() % WORLD_TICK_RATE) != 0 || !(event.world instanceof WorldServer)) return;
		
		WorldServer world = (WorldServer)event.world;
		ChunkProviderServer chunkProvider = world.getChunkProvider();
		Chunk[] chunkArray = chunkProvider.getLoadedChunks().toArray(new Chunk[chunkProvider.getLoadedChunks().size()]);
		
		Entity[] entityArray = world.loadedEntityList.toArray(new Entity[world.loadedEntityList.size()]);
		for (Entity entity : entityArray) {
			IRadiationSource chunkSource = RadiationHelper.getRadiationSource(world.getChunkFromChunkCoords((int) Math.floor(entity.posX) >> 4, (int) Math.floor(entity.posZ) >> 4));
			if (entity instanceof EntityPlayer) {
				RadiationHelper.transferRadsFromInventoryToChunkBuffer(((EntityPlayer)entity).inventory, chunkSource);
			}
			else if (NCConfig.radiation_dropped_items && entity instanceof EntityItem) {
				RadiationHelper.transferRadiationFromStackToChunkBuffer(((EntityItem)entity).getItem(), chunkSource, 1D);
			}
			else if (entity instanceof EntityLiving) {
				EntityLiving entityLiving = (EntityLiving)entity;
				IEntityRads entityRads = RadiationHelper.getEntityRadiation(entityLiving);
				if (entityRads == null) return;
				
				RadiationHelper.transferRadsFromSourceToEntity(chunkSource, entityRads, entityLiving, WORLD_TICK_RATE);
				
				if (NCConfig.radiation_entity_decay_rate > 0D) {
					entityRads.setTotalRads(entityRads.getTotalRads()*Math.pow(1D - NCConfig.radiation_entity_decay_rate, WORLD_TICK_RATE), false);
				}
				
				if (entityLiving instanceof IMob) {
					RadiationHelper.applyPotionEffects(entityLiving, entityRads, RadEffects.MOB_RAD_LEVEL_LIST, RadEffects.MOB_EFFECTS_LIST);
				}
				else {
					if (entityRads.isFatal()) {
						entityLiving.attackEntityFrom(FATAL_RADS, Float.MAX_VALUE);
					}
					else {
						RadiationHelper.applyPotionEffects(entityLiving, entityRads, RadEffects.ENTITY_RAD_LEVEL_LIST, RadEffects.ENTITY_DEBUFF_LIST);
					}
				}
				entityRads.setRadiationLevel(entityRads.getRadiationLevel()*(1D - NCConfig.radiation_decay_rate));
			}
		}
		
		TileEntity[] tileArray = world.loadedTileEntityList.toArray(new TileEntity[world.loadedTileEntityList.size()]);
		for (TileEntity tile : tileArray) {
			Chunk chunk = world.getChunkFromBlockCoords(tile.getPos());
			RadiationHelper.transferRadiationFromProviderToChunkBuffer(tile, RadiationHelper.getRadiationSource(chunk));
		}
		
		BiomeProvider biomeProvider = world.getBiomeProvider();
		int dimension = world.provider.getDimension();
		for (Chunk chunk : chunkArray) {
			if (!chunk.isLoaded()) return;
			IRadiationSource chunkSource = RadiationHelper.getRadiationSource(chunk);
			if (chunkSource == null) return;
			
			chunkSource.resetScrubbingFraction();
			
			if (RadWorlds.RAD_MAP.containsKey(dimension)) {
				RadiationHelper.addToSourceBuffer(chunkSource, RadWorlds.RAD_MAP.get(dimension));
			}
			
			if (!RadBiomes.DIM_BLACKLIST.contains(dimension)) {
				Double biomeRadiation = RadBiomes.RAD_MAP.get(chunk.getBiome(new BlockPos(8, 8, 8), biomeProvider));
				if (biomeRadiation != null) RadiationHelper.addToSourceBuffer(chunkSource, biomeRadiation);
			}
		}
		
		for (TileEntity tile : tileArray) {
			if (tile instanceof ITileRadiationEnvironment) {
				Chunk chunk = world.getChunkFromBlockCoords(tile.getPos());
				IRadiationSource chunkSource = RadiationHelper.getRadiationSource(chunk);
				if (chunkSource == null) return;
				
				((ITileRadiationEnvironment)tile).setCurrentChunkBuffer(chunkSource.getRadiationBuffer());
			}
		}
		
		for (TileEntity tile : tileArray) {
			if (tile instanceof ITileRadiationEnvironment) {
				Chunk chunk = world.getChunkFromBlockCoords(tile.getPos());
				RadiationHelper.addFractionToChunkBuffer(RadiationHelper.getRadiationSource(chunk), (ITileRadiationEnvironment)tile);
			}
		}
		
		for (Chunk chunk : chunkArray) {
			if (!chunk.isLoaded()) return;
			IRadiationSource chunkSource = RadiationHelper.getRadiationSource(chunk);
			if (chunkSource == null) return;
			
			double changeRate = (chunkSource.getRadiationLevel() < chunkSource.getRadiationBuffer()) ? NCConfig.radiation_spread_rate : NCConfig.radiation_decay_rate*(1D - chunkSource.getScrubbingFraction()) + NCConfig.radiation_spread_rate*chunkSource.getScrubbingFraction();
			
			double newLevel = Math.max(0D, chunkSource.getRadiationLevel() + (chunkSource.getRadiationBuffer() - chunkSource.getRadiationLevel())*changeRate);
			if (NCConfig.radiation_chunk_limit >= 0D) {
				newLevel = Math.min(newLevel, NCConfig.radiation_chunk_limit);
			}
			Biome biome = chunk.getBiome(new BlockPos(8, 8, 8), biomeProvider);
			if (!RadBiomes.LIMIT_MAP.isEmpty() && RadBiomes.LIMIT_MAP.containsKey(biome)) {
				newLevel = Math.min(newLevel, RadBiomes.LIMIT_MAP.get(biome));
			}
			if (!RadWorlds.LIMIT_MAP.isEmpty() && RadWorlds.LIMIT_MAP.containsKey(dimension)) {
				newLevel = Math.min(newLevel, RadWorlds.LIMIT_MAP.get(dimension));
			}
			
			chunkSource.setRadiationLevel(newLevel);
		}
		
		for (Chunk chunk : chunkArray) {
			// Emptying buffers here too!
			RadiationHelper.spreadRadiationFromChunk(chunk, getRandomAdjacentChunk(chunkProvider, chunk));
		}
	}
	
	private static final List<byte[]> ADJACENT_COORDS = Lists.newArrayList(new byte[] {1, 0}, new byte[] {0, 1}, new byte[] {-1, 0}, new byte[] {0, -1});
	
	private Chunk getRandomAdjacentChunk(ChunkProviderServer chunkProvider, Chunk chunk) {
		if (chunkProvider == null || chunk == null || !chunk.isLoaded()) return null;
		int x = chunk.getPos().x;
		int z = chunk.getPos().z;
		Collections.shuffle(ADJACENT_COORDS);
		for (byte[] pos : ADJACENT_COORDS) {
			if (chunkProvider.chunkExists(x + pos[0], z + pos[1])) {
				Chunk adjChunk = chunkProvider.getLoadedChunk(x + pos[0], z + pos[1]);
				if (adjChunk != null) return adjChunk;
			}
		}
		return null;
	}
	
	private void playGeigerSound(EntityPlayer player) {
		IEntityRads entityRads = RadiationHelper.getEntityRadiation(player);
		if (entityRads == null) return;
		if (!entityRads.isRadiationUndetectable()) {
			double soundChance = Math.cbrt(entityRads.getRadiationLevel()/200D);
			for (int i = 0; i < 2; i++) if (rand.nextDouble() < soundChance) player.playSound(SoundHandler.geiger_tick, 0.6F + rand.nextFloat()*0.2F, 0.92F + rand.nextFloat()*0.16F);
		}
	}
}
