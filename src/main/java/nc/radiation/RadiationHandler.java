package nc.radiation;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import nc.capability.radiation.IEntityRads;
import nc.capability.radiation.IRadiationSource;
import nc.config.NCConfig;
import nc.handler.SoundHandler;
import nc.network.PacketHandler;
import nc.network.PlayerRadsUpdatePacket;
import nc.util.Lang;
import nc.util.RadiationHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
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
	
	@SubscribeEvent
	public void updatePlayerRadiation(PlayerTickEvent event) {
		if (!NCConfig.radiation_require_counter && event.phase == Phase.START && event.side == Side.CLIENT) {
			EntityPlayer player = event.player;
			if (player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) {
				IEntityRads entityRads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
				if (entityRads == null) return;
				if (!entityRads.isRadiationUndetectable()) {
					double soundChance = Math.cbrt(entityRads.getRadiationLevel()/200D);
					for (int i = 0; i < 2; i++) if (rand.nextDouble() < soundChance) player.playSound(SoundHandler.geiger_tick, 0.6F + rand.nextFloat()*0.2F, 0.92F + rand.nextFloat()*0.16F);
				}
			}
		}
		
		if (event.phase != Phase.START || (event.player.world.getTotalWorldTime() % 5) != 0) return;
		if (event.side == Side.SERVER && event.player instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP)event.player;
			if (!player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) return;
			IEntityRads playerRads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
			if (playerRads == null) return;
			Chunk chunk = player.world.getChunkFromChunkCoords((int)player.posX >> 4, (int)player.posZ >> 4);
			
			double radiationLevel = RadiationHelper.transferRadsToPlayer(player.world, player, playerRads, 5) + RadiationHelper.transferRadsToPlayer(chunk, player, playerRads, 5) + RadiationHelper.transferRadsFromInventoryToPlayer(player, playerRads, chunk, 5);
			playerRads.setRadiationLevel(radiationLevel);
			
			if (playerRads.isFatal()) player.attackEntityFrom(FATAL_RADS, 1000F);
			
			double previousResistance = playerRads.getRadiationResistance();
			if (previousResistance > 0D) {
				double radXDecayRate = Math.max(previousResistance, NCConfig.radiation_rad_x_amount)/NCConfig.radiation_rad_x_lifetime;
				playerRads.setRadiationResistance(previousResistance - radXDecayRate*5);
				if (playerRads.getRadiationResistance() == 0D) playerRads.setRadXWoreOff(true);
			}
			else playerRads.setRadXWoreOff(false);
			
			if (playerRads.getRadawayBuffer() > 0D) {
				playerRads.setTotalRads(playerRads.getTotalRads() - NCConfig.radiation_radaway_rate*5);
				playerRads.setRadawayBuffer(playerRads.getRadawayBuffer() - NCConfig.radiation_radaway_rate*5);
			}
			
			PacketHandler.instance.sendTo(new PlayerRadsUpdatePacket(playerRads), player);
			
			RadiationHelper.applySymptoms(player, playerRads, 5);
		} else {
			EntityPlayer player = event.player;
			if (!player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) return;
			IEntityRads playerRads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
			if (playerRads == null) return;
			if (playerRads.getRadXWoreOff()) {
				player.playSound(SoundHandler.chems_wear_off, 0.5F, 1F);
				player.sendMessage(new TextComponentString(TextFormatting.ITALIC + RAD_X_WORE_OFF));
			}
		}
	}
	
	@SubscribeEvent
	public void updateChunkRadiation(WorldTickEvent event) {
		if (event.phase != Phase.START || event.side == Side.CLIENT || (event.world.getTotalWorldTime() % 20) != 0 || !(event.world instanceof WorldServer)) return;
		WorldServer world = (WorldServer)event.world;
		ChunkProviderServer chunkProvider = world.getChunkProvider();
		List<Chunk> loadedChunks = Lists.newArrayList(chunkProvider.getLoadedChunks());
		
		for (Entity entity : Lists.newArrayList(world.loadedEntityList)) {
			Chunk chunk = world.getChunkFromChunkCoords((int)entity.posX >> 4, (int)entity.posZ >> 4);
			if (entity instanceof EntityPlayer) {
				RadiationHelper.transferRadsFromInventoryToChunkBuffer(((EntityPlayer)entity).inventory, chunk);
			}
			else if (entity instanceof EntityItem) {
				ItemStack stack = ((EntityItem) entity).getItem();
				if (!stack.isEmpty()) RadiationHelper.transferRadiationFromStackToChunkBuffer(stack, chunk);
			}
			else if (entity instanceof EntityLiving) {
				EntityLiving entityLiving = (EntityLiving) entity;
				if (entityLiving.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) {
					IEntityRads entityRads = entityLiving.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
					if (entityRads != null) {
						RadiationHelper.transferRadsFromSourceToEntity(world, entityRads, 20);
						RadiationHelper.transferRadsFromSourceToEntity(chunk, entityRads, 20);
						if (entity instanceof EntityMob) RadiationHelper.applyMobBuffs(entityLiving, entityRads, 20);
						else {
							//if (entityRads.isFatal()) entityLiving.attackEntityFrom(FATAL_RADS, 1000F);
							RadiationHelper.applySymptoms(entityLiving, entityRads, 20);
						}
						entityRads.setRadiationLevel(entityRads.getRadiationLevel()*(1D - NCConfig.radiation_decay_rate));
					}
				}
			}
		}
		for (TileEntity tile : Lists.newArrayList(world.loadedTileEntityList)) {
			Chunk chunk = world.getChunkFromChunkCoords(tile.getPos().getX() >> 4, tile.getPos().getZ() >> 4);
			RadiationHelper.transferRadiationFromSourceToChunkBuffer(tile, chunk);
		}
		
		BiomeProvider biomeProvider = world.getBiomeProvider();
		for (Chunk chunk : loadedChunks) {
			if (chunk == null || !chunk.hasCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null)) return;
			IRadiationSource chunkRadiation = chunk.getCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null);
			if (chunkRadiation == null) return;
			
			Double biomeRadiation = RadBiomes.BIOME_MAP.get(chunk.getBiome(new BlockPos(8, 8, 8), biomeProvider));
			if (biomeRadiation != null) RadiationHelper.addToChunkBuffer(chunkRadiation, biomeRadiation);
			
			double changeRate = chunkRadiation.getRadiationLevel() < chunkRadiation.getRadiationBuffer() ? NCConfig.radiation_spread_rate : NCConfig.radiation_decay_rate;
			
			chunkRadiation.setRadiationLevel(chunkRadiation.getRadiationLevel() + (chunkRadiation.getRadiationBuffer() - chunkRadiation.getRadiationLevel())*changeRate);
			chunkRadiation.setRadiationBuffer(0D);
		}
		for (Chunk chunk : loadedChunks) RadiationHelper.spreadRadiationFromChunk(chunk, getRandomAdjacentChunk(chunkProvider, chunk));
	}
	
	private static final List<int[]> ADJACENT_COORDS = Lists.newArrayList(new int[] {1, 0}, new int[] {0, 1}, new int[] {-1, 0}, new int[] {0, -1});
	
	private Chunk getRandomAdjacentChunk(ChunkProviderServer chunkProvider, Chunk chunk) {
		if (chunkProvider == null || chunk == null) return null;
		int x = chunk.getPos().x;
		int z = chunk.getPos().z;
		Collections.shuffle(ADJACENT_COORDS);
		for (int[] pos : ADJACENT_COORDS) {
			if (chunkProvider.chunkExists(x + pos[0], z + pos[1])) {
				Chunk adjChunk = chunkProvider.getLoadedChunk(x + pos[0], z + pos[1]);
				if (adjChunk != null) return adjChunk;
			}
		}
		return null;
	}
}
