package nc.radiation;

import com.google.common.collect.Lists;
import nc.ModCheck;
import nc.capability.radiation.entity.IEntityRads;
import nc.capability.radiation.source.IRadiationSource;
import nc.entity.IRadiationMob;
import nc.recipe.*;
import nc.tile.radiation.ITileRadiationEnvironment;
import nc.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;

import java.util.*;

import static nc.config.NCConfig.*;

public class WorldRadiationHandler {
	
	private static final Random RAND = new Random();
	
	private static final Lazy<BasicRecipeHandler> RADIATION_BLOCK_MUTATION = new Lazy<>(() -> NCRecipes.radiation_block_mutation);
	private static final Lazy<BasicRecipeHandler> RADIATION_BLOCK_PURIFICATION = new Lazy<>(() -> NCRecipes.radiation_block_purification);
	
	private static EnumFacing tile_side = EnumFacing.DOWN;
	
	public void update(WorldServer world) {
		ChunkProviderServer chunkProvider = world.getChunkProvider();
		List<Chunk> chunks = new ArrayList<>(chunkProvider.getLoadedChunks());
		
		if (chunks.isEmpty()) {
			return;
		}
		
		int chunkCount = chunks.size();
		int chunksPerTick = Math.min(Math.max(0, radiation_world_chunks_per_tick), chunkCount);
		if (chunksPerTick == 0) {
			return;
		}
		
		int chunkStart = RAND.nextInt(chunkCount);
		
		double tickMult = Math.max(1D, (double) chunkCount / (double) chunksPerTick);
		double entityDecayMult = radiation_entity_decay_rate > 0D ? Math.pow(1D - radiation_entity_decay_rate, tickMult) : 1D;
		double radiationDecayMult = Math.pow(1D - radiation_decay_rate, tickMult);
		
		int dimension = world.provider.getDimension();
		boolean hasDimensionRadiation = RadWorlds.RAD_MAP.containsKey(dimension);
		double dimensionRadiation = hasDimensionRadiation ? RadWorlds.RAD_MAP.get(dimension) : 0D;
		boolean hasDimensionLimit = RadWorlds.LIMIT_MAP.containsKey(dimension);
		double dimensionLimit = hasDimensionLimit ? RadWorlds.LIMIT_MAP.get(dimension) : 0D;
		boolean biomeBlacklisted = RadBiomes.DIM_BLACKLIST.contains(dimension);
		
		BlockPos randomOffsetPos = newRandomOffsetPos(world);
		BiomeProvider biomeProvider = world.getBiomeProvider();
		
		String randomStructure = ModCheck.cubicChunksLoaded() || RadStructures.STRUCTURE_LIST.isEmpty() ? null : RadStructures.STRUCTURE_LIST.get(RAND.nextInt(RadStructures.STRUCTURE_LIST.size()));
		boolean hasStructureRadiation = randomStructure != null && RadStructures.RAD_MAP.containsKey(randomStructure);
		double structureRadiation = hasStructureRadiation ? RadStructures.RAD_MAP.getDouble(randomStructure) : 0D;
		
		List<Chunk> updatedChunks = new ArrayList<>(chunksPerTick);
		
		for (int i = 0; i < chunksPerTick; ++i) {
			Chunk chunk = chunks.get((chunkStart + i) % chunkCount);
			
			if (!chunk.isLoaded()) {
				continue;
			}
			
			IRadiationSource chunkSource = RadiationHelper.getRadiationSource(chunk);
			if (chunkSource == null) {
				continue;
			}
			updatedChunks.add(chunk);
			
			ClassInheritanceMultiMap<Entity>[] entityListArray = chunk.getEntityLists();
			for (ClassInheritanceMultiMap<Entity> entities : entityListArray) {
				Entity[] entityArray = entities.toArray(new Entity[0]);
				for (Entity entity : entityArray) {
					if (entity instanceof EntityPlayer player) {
						RadiationHelper.transferRadsFromInventoryToChunkBuffer(player.inventory, chunkSource);
					}
					else if (radiation_dropped_items && entity instanceof EntityItem entityItem) {
						RadiationHelper.transferRadiationFromStackToChunkBuffer(entityItem.getItem(), chunkSource, 1D);
					}
					else if (entity instanceof EntityLiving entityLiving) {
						IEntityRads entityRads = RadiationHelper.getEntityRadiation(entityLiving);
						if (entityRads == null) {
							continue;
						}
						
						entityRads.setExternalRadiationResistance(RadiationHelper.getEntityArmorRadResistance(entityLiving));
						
						if (radiation_entity_decay_rate > 0D) {
							entityRads.setTotalRads(entityRads.getTotalRads() * entityDecayMult, false);
						}
						
						RadiationHelper.transferRadsFromSourceToEntity(chunkSource, entityRads, entityLiving, tickMult);
						
						if (entityRads.getPoisonBuffer() > 0D) {
							double poisonRads = Math.min(entityRads.getPoisonBuffer(), entityRads.getRecentPoisonAddition() * tickMult / radiation_poison_time);
							entityRads.setTotalRads(entityRads.getTotalRads() + poisonRads, false);
							entityRads.setPoisonBuffer(entityRads.getPoisonBuffer() - poisonRads);
							if (entityRads.getPoisonBuffer() == 0D) {
								entityRads.resetRecentPoisonAddition();
							}
						}
						else {
							entityRads.resetRecentPoisonAddition();
						}
						
						if (entityLiving instanceof IMob) {
							if (radiation_mob_rads_fatal && !(entityLiving instanceof IRadiationMob) && entityRads.isFatal()) {
								entityLiving.attackEntityFrom(DamageSources.FATAL_RADS, Float.MAX_VALUE);
							}
							else {
								RadiationHelper.applyEntityEffects(entityLiving, entityRads, tickMult, RadPotionEffects.MOB_RAD_LEVEL_LIST, RadPotionEffects.MOB_RAD_EFFECT_LISTS, RadPotionEffects.MOB_RAD_ATTRIBUTE_MAP);
							}
						}
						else {
							if (entityRads.isFatal()) {
								if (register_entity[0] && entityLiving instanceof INpc) {
									RadiationHelper.spawnFeralGhoul(world, entityLiving);
									entityLiving.setDead();
								}
								else if (radiation_passive_rads_fatal) {
									entityLiving.attackEntityFrom(DamageSources.FATAL_RADS, Float.MAX_VALUE);
								}
							}
							else {
								RadiationHelper.applyEntityEffects(entityLiving, entityRads, tickMult, RadPotionEffects.ENTITY_RAD_LEVEL_LIST, RadPotionEffects.ENTITY_RAD_EFFECT_LISTS, RadPotionEffects.ENTITY_RAD_ATTRIBUTE_MAP);
							}
						}
						entityRads.setRadiationLevel(entityRads.getRadiationLevel() * radiationDecayMult);
					}
				}
			}
			
			chunkSource.setScrubbingFraction(0D);
			chunkSource.setEffectiveScrubberCount(0D);
			
			Collection<TileEntity> tiles = new ArrayList<>(chunk.getTileEntityMap().values());
			List<ITileRadiationEnvironment> radiationEnvironmentTiles = new ArrayList<>();
			for (TileEntity tile : tiles) {
				if (radiation_tile_entities) {
					RadiationHelper.transferRadiationFromProviderToChunkBuffer(tile, tile_side, chunkSource);
				}
				if (tile instanceof ITileRadiationEnvironment tileRadiationEnvironment) {
					radiationEnvironmentTiles.add(tileRadiationEnvironment);
				}
			}
			
			if (hasDimensionRadiation) {
				RadiationHelper.addToSourceBuffer(chunkSource, dimensionRadiation);
			}
			
			Biome biome = getBiome(chunk, randomOffsetPos, biomeProvider);
			if (biome != null && !biomeBlacklisted && RadBiomes.RAD_MAP.containsKey(biome)) {
				RadiationHelper.addToSourceBuffer(chunkSource, RadBiomes.RAD_MAP.getDouble(biome));
			}
			
			BlockPos randomChunkPos = null;
			if (hasStructureRadiation) {
				randomChunkPos = newRandomPosInChunk(world, chunk);
				if (StructureHelper.CACHE.isInStructure(world, randomStructure, randomChunkPos)) {
					RadiationHelper.addToSourceBuffer(chunkSource, structureRadiation);
				}
			}
			
			if (radiation_check_blocks && i == 0) {
				if (randomChunkPos == null) {
					randomChunkPos = newRandomPosInChunk(world, chunk);
				}
				int packed = RecipeItemHelper.pack(StackHelper.blockStateToStack(world.getBlockState(randomChunkPos)));
				if (RadSources.STACK_MAP.containsKey(packed)) {
					RadiationHelper.addToSourceBuffer(chunkSource, RadSources.STACK_MAP.get(packed));
				}
			}
			
			double currentLevel = chunkSource.getRadiationLevel(), currentBuffer = chunkSource.getRadiationBuffer();
			for (ITileRadiationEnvironment tileRadiationEnvironment : radiationEnvironmentTiles) {
				tileRadiationEnvironment.setCurrentChunkRadiationLevel(currentLevel);
				tileRadiationEnvironment.setCurrentChunkRadiationBuffer(currentBuffer);
				RadiationHelper.addScrubbingFractionToChunk(chunkSource, tileRadiationEnvironment);
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
				newLevel = Math.min(newLevel, RadBiomes.LIMIT_MAP.getDouble(biome));
			}
			if (hasDimensionLimit) {
				newLevel = Math.min(newLevel, dimensionLimit);
			}
			
			chunkSource.setRadiationLevel(newLevel);
			
			mutateTerrain(world, chunk, newLevel);
		}
		
		for (Chunk chunk : updatedChunks) {
			RadiationHelper.spreadRadiationFromChunk(chunk, getRandomAdjacentChunk(chunkProvider, chunk));
		}
		
		tile_side = EnumFacing.byIndex(tile_side.getIndex() + 1);
	}
	
	public static final List<int[]> ADJACENT_COORDS = Lists.newArrayList(new int[] {1, 0}, new int[] {0, 1}, new int[] {-1, 0}, new int[] {0, -1});
	
	public static Chunk getRandomAdjacentChunk(ChunkProviderServer chunkProvider, Chunk chunk) {
		if (chunkProvider == null || chunk == null || !chunk.isLoaded()) {
			return null;
		}
		ChunkPos chunkPos = chunk.getPos();
		int x = chunkPos.x, z = chunkPos.z;
		Collections.shuffle(ADJACENT_COORDS);
		for (int[] pos : ADJACENT_COORDS) {
			if (chunkProvider.chunkExists(x + pos[0], z + pos[1])) {
				Chunk adjChunk = chunkProvider.getLoadedChunk(x + pos[0], z + pos[1]);
				if (adjChunk != null) {
					return adjChunk;
				}
			}
		}
		return null;
	}
	
	public static BlockPos newRandomOffsetPos(World world) {
		return new BlockPos(RAND.nextInt(16), RAND.nextInt(world.getHeight()), RAND.nextInt(16));
	}
	
	public static BlockPos newRandomPosInChunk(World world, Chunk chunk) {
		return chunk.getPos().getBlock(RAND.nextInt(16), RAND.nextInt(world.getHeight()), RAND.nextInt(16));
	}
	
	public static Biome getBiome(Chunk chunk, BlockPos randomOffsetPos, BiomeProvider biomeProvider) {
		try {
			return chunk.getBiome(randomOffsetPos, biomeProvider);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public static void mutateTerrain(World world, Chunk chunk, double radiation) {
		long j = Math.min(radiation_block_effect_max_rate, (long) Math.log(Math.E - 1D + radiation / RecipeStats.getBlockMutationThreshold()));
		while (j > 0) {
			--j;
			BlockPos randomChunkPos = newRandomPosInChunk(world, chunk);
			BasicRecipe mutationRecipe = RecipeHelper.blockRecipe(RADIATION_BLOCK_MUTATION.get(), world, randomChunkPos);
			if (mutationRecipe != null && radiation >= mutationRecipe.getBlockMutationThreshold()) {
				IBlockState result = RecipeHelper.getBlockStateFromProductList(mutationRecipe.getItemProducts(), 0);
				if (result != null) {
					world.setBlockState(randomChunkPos, result);
				}
			}
		}
		
		j = radiation == 0D ? radiation_block_effect_max_rate : Math.min(radiation_block_effect_max_rate, (long) Math.log(Math.E - 1D + RecipeStats.getBlockPurificationThreshold() / radiation));
		while (j > 0) {
			--j;
			BlockPos randomChunkPos = newRandomPosInChunk(world, chunk);
			BasicRecipe purificationRecipe = RecipeHelper.blockRecipe(RADIATION_BLOCK_PURIFICATION.get(), world, randomChunkPos);
			if (purificationRecipe != null && radiation < purificationRecipe.getBlockMutationThreshold()) {
				IBlockState result = RecipeHelper.getBlockStateFromProductList(purificationRecipe.getItemProducts(), 0);
				if (result != null) {
					world.setBlockState(randomChunkPos, result);
				}
			}
		}
	}
}
