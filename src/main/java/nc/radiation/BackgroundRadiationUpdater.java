package nc.radiation;

import nc.ModCheck;
import nc.capability.radiation.entity.IEntityRads;
import nc.capability.radiation.source.IRadiationSource;
import nc.tile.radiation.ITileRadiationEnvironment;
import nc.util.DamageSources;
import nc.util.StackHelper;
import nc.util.StructureHelper;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.INpc;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static nc.config.NCConfig.*;
import static nc.radiation.RadiationHandler.*;

public class BackgroundRadiationUpdater {
	private final ExecutorService worldRadiationExecutor = Executors.newSingleThreadExecutor();

	public void updateWorldRadiation(WorldServer world) {
		worldRadiationExecutor.submit(() -> updateWorldRadiationInt(world));
	}

	private synchronized void updateWorldRadiationInt(WorldServer world) {
		ChunkProviderServer chunkProvider = world.getChunkProvider();
		Collection<Chunk> chunks = new ArrayList<>(chunkProvider.getLoadedChunks());

		if (chunks.isEmpty()) {
			return;
		}

		int chunkCount = chunks.size();
		int chunkStart = RAND.nextInt(chunkCount);
		int chunksPerTick = Math.min(radiation_world_chunks_per_tick, chunkCount);
		double tickMult = Math.max(1D, (double) chunkCount / (double) chunksPerTick);

		int dimension = world.provider.getDimension();
		BlockPos randomOffsetPos = newRandomOffsetPos(world);
		BiomeProvider biomeProvider = world.getBiomeProvider();
		String randomStructure = ModCheck.cubicChunksLoaded() || RadStructures.STRUCTURE_LIST.isEmpty() ? null : RadStructures.STRUCTURE_LIST.get(RAND.nextInt(RadStructures.STRUCTURE_LIST.size()));

		int chunkIndex = -1;
		for (Chunk chunk : chunks) {
			++chunkIndex;

			if ((chunkIndex - chunkStart) % chunkCount >= chunksPerTick) {
				continue;
			}

			if (!chunk.isLoaded()) {
				continue;
			}

			IRadiationSource chunkSource = RadiationHelper.getRadiationSource(chunk);
			if (chunkSource == null) {
				continue;
			}

			ClassInheritanceMultiMap<Entity>[] entityListArray = chunk.getEntityLists();
			for (ClassInheritanceMultiMap<Entity> entities : entityListArray) {
				Entity[] entityArray = entities.toArray(new Entity[0]);
				for (Entity entity : entityArray) {
					if (entity instanceof EntityPlayer player) {
						RadiationHelper.transferRadsFromInventoryToChunkBuffer(player.inventory, chunkSource);
					} else if (radiation_dropped_items && entity instanceof EntityItem entityItem) {
						RadiationHelper.transferRadiationFromStackToChunkBuffer(entityItem.getItem(), chunkSource, 1D);
					} else if (entity instanceof EntityLiving entityLiving) {
						IEntityRads entityRads = RadiationHelper.getEntityRadiation(entityLiving);
						if (entityRads == null) {
							continue;
						}

						entityRads.setExternalRadiationResistance(RadiationHelper.getEntityArmorRadResistance(entityLiving));

						if (radiation_entity_decay_rate > 0D) {
							entityRads.setTotalRads(entityRads.getTotalRads() * Math.pow(1D - radiation_entity_decay_rate, tickMult), false);
						}

						RadiationHelper.transferRadsFromSourceToEntity(chunkSource, entityRads, entityLiving, tickMult);

						if (entityRads.getPoisonBuffer() > 0D) {
							double poisonRads = Math.min(entityRads.getPoisonBuffer(), entityRads.getRecentPoisonAddition() * tickMult / radiation_poison_time);
							entityRads.setTotalRads(entityRads.getTotalRads() + poisonRads, false);
							entityRads.setPoisonBuffer(entityRads.getPoisonBuffer() - poisonRads);
							if (entityRads.getPoisonBuffer() == 0D) {
								entityRads.resetRecentPoisonAddition();
							}
						} else {
							entityRads.resetRecentPoisonAddition();
						}

						if (entityLiving instanceof IMob) {
							if (radiation_mob_rads_fatal && entityRads.isFatal()) {
								entityLiving.attackEntityFrom(DamageSources.FATAL_RADS, Float.MAX_VALUE);
							} else {
								RadiationHelper.applyEntityEffects(entityLiving, entityRads, tickMult, RadPotionEffects.MOB_RAD_LEVEL_LIST, RadPotionEffects.MOB_RAD_EFFECT_LISTS, RadPotionEffects.MOB_RAD_ATTRIBUTE_MAP);
							}
						} else {
							if (entityRads.isFatal()) {
								if (register_entity[0] && entityLiving instanceof INpc) {
									spawnFeralGhoul(world, entityLiving);
								} else if (radiation_passive_rads_fatal) {
									entityLiving.attackEntityFrom(DamageSources.FATAL_RADS, Float.MAX_VALUE);
								}
							} else {
								RadiationHelper.applyEntityEffects(entityLiving, entityRads, tickMult, RadPotionEffects.ENTITY_RAD_LEVEL_LIST, RadPotionEffects.ENTITY_RAD_EFFECT_LISTS, RadPotionEffects.ENTITY_RAD_ATTRIBUTE_MAP);
							}
						}
						entityRads.setRadiationLevel(entityRads.getRadiationLevel() * Math.pow(1D - radiation_decay_rate, tickMult));
					}
				}
			}

			chunkSource.setScrubbingFraction(0D);
			chunkSource.setEffectiveScrubberCount(0D);

			Collection<TileEntity> tiles = new ArrayList<>(chunk.getTileEntityMap().values());

			if (radiation_tile_entities) {
				for (TileEntity tile : tiles) {
					RadiationHelper.transferRadiationFromProviderToChunkBuffer(tile, tile_side, chunkSource);
				}
			}

			if (RadWorlds.RAD_MAP.containsKey(dimension)) {
				RadiationHelper.addToSourceBuffer(chunkSource, RadWorlds.RAD_MAP.get(dimension));
			}

			Biome biome = getBiome(chunk, randomOffsetPos, biomeProvider);
			if (biome != null && !RadBiomes.DIM_BLACKLIST.contains(dimension)) {
				Double biomeRadiation = RadBiomes.RAD_MAP.get(biome);
				if (biomeRadiation != null) {
					RadiationHelper.addToSourceBuffer(chunkSource, biomeRadiation);
				}
			}

			BlockPos randomChunkPos = newRandomPosInChunk(world, chunk);
			if (randomStructure != null && StructureHelper.CACHE.isInStructure(world, randomStructure, randomChunkPos)) {
				Double structureRadiation = RadStructures.RAD_MAP.get(randomStructure);
				if (structureRadiation != null) {
					RadiationHelper.addToSourceBuffer(chunkSource, structureRadiation);
				}
			}

			if (radiation_check_blocks && chunkIndex == chunkStart) {
				int packed = RecipeItemHelper.pack(StackHelper.blockStateToStack(world.getBlockState(randomChunkPos)));
				if (RadSources.STACK_MAP.containsKey(packed)) {
					RadiationHelper.addToSourceBuffer(chunkSource, RadSources.STACK_MAP.get(packed));
				}
			}

			double currentLevel = chunkSource.getRadiationLevel(), currentBuffer = chunkSource.getRadiationBuffer();
			for (TileEntity tile : tiles) {
				if (tile instanceof ITileRadiationEnvironment tileRadiationEnvironment) {
					tileRadiationEnvironment.setCurrentChunkRadiationLevel(currentLevel);
					tileRadiationEnvironment.setCurrentChunkRadiationBuffer(currentBuffer);
					RadiationHelper.addScrubbingFractionToChunk(RadiationHelper.getRadiationSource(chunk), tileRadiationEnvironment);
				}
			}

			if (radiation_scrubber_non_linear) {
				double scrubbers = chunkSource.getEffectiveScrubberCount();
				double scrubbingFraction = RadiationHelper.getAltScrubbingFraction(scrubbers);

				RadiationHelper.addToSourceBuffer(chunkSource, -scrubbingFraction * chunkSource.getRadiationBuffer());
				chunkSource.setScrubbingFraction(scrubbingFraction);
			}

			double changeRate = chunkSource.getRadiationLevel() < chunkSource.getRadiationBuffer() ? radiation_spread_rate : radiation_decay_rate * (1D - chunkSource.getScrubbingFraction()) + radiation_spread_rate * chunkSource.getScrubbingFraction();

			double newLevel = Math.max(0D, chunkSource.getRadiationLevel() + (chunkSource.getRadiationBuffer() - chunkSource.getRadiationLevel()) * changeRate);
			if (radiation_chunk_limit >= 0D) {
				newLevel = Math.min(newLevel, radiation_chunk_limit);
			}
			if (biome != null && RadBiomes.LIMIT_MAP.containsKey(biome)) {
				newLevel = Math.min(newLevel, RadBiomes.LIMIT_MAP.get(biome));
			}
			if (RadWorlds.LIMIT_MAP.containsKey(dimension)) {
				newLevel = Math.min(newLevel, RadWorlds.LIMIT_MAP.get(dimension));
			}

			chunkSource.setRadiationLevel(newLevel);

			mutateTerrain(world, chunk, newLevel);
		}

		chunkIndex = -1;
		for (Chunk chunk : chunks) {
			++chunkIndex;

			if ((chunkIndex - chunkStart) % chunkCount >= chunksPerTick) {
				continue;
			}

			RadiationHelper.spreadRadiationFromChunk(chunk, getRandomAdjacentChunk(chunkProvider, chunk));
		}

		tile_side = EnumFacing.byIndex(tile_side.getIndex() + 1);
	}
}
