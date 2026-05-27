package nc.worldgen.dimension;

import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.*;
import nc.radiation.RadiationHelper;
import nc.recipe.*;
import nc.util.*;
import nc.worldgen.structure.vault.VaultGenerator;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Arrays;

import static nc.config.NCConfig.*;

public class WastelandEventHandler {
	
	private static final Lazy<BasicRecipeHandler> WASTELAND_BLOCK_MAPPING = new Lazy<>(() -> NCRecipes.wasteland_block_mapping);
	
	protected static final IBlockState COARSE_DIRT = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
	
	private static final Lazy<ObjectSet<IBlockState>> WASTELAND_DEFAULT_MAPPING_STATES = new Lazy<>(() -> new ObjectOpenHashSet<>(Arrays.asList(COARSE_DIRT)));
	private static final Lazy<ObjectSet<Block>> WASTELAND_DEFAULT_MAPPING_BLOCKS = new Lazy<>(() -> new ObjectOpenHashSet<>(Arrays.asList(Blocks.AIR, Blocks.DEADBUSH, Blocks.LADDER)));
	
	private final Int2ObjectMap<LongSet> pendingRecheckMap = new Int2ObjectOpenHashMap<>();
	private final Int2ObjectMap<LongSet> queuedChunkSetMap = new Int2ObjectOpenHashMap<>();
	private final Int2ObjectMap<LongArrayFIFOQueue> chunkQueueMap = new Int2ObjectOpenHashMap<>();
	
	private final Int2ObjectMap<VaultGenerator> vaultGeneratorMap = new Int2ObjectOpenHashMap<>();
	
	@SubscribeEvent
	public void onPopulateChunk(PopulateChunkEvent.Post event) {
		World world = event.getWorld();
		int dimension;
		if (world.isRemote || (dimension = world.provider.getDimension()) != wasteland_dimension || !(world instanceof WorldServer worldServer)) {
			return;
		}
		
		int chunkX = event.getChunkX(), chunkZ = event.getChunkZ();
		
		if (wasteland_mapping_enabled) {
			processChunk(worldServer, chunkX, chunkZ);
			
			LongSet pendingSet = pendingRecheckMap.computeIfAbsent(dimension, x -> new LongOpenHashSet());
			pendingSet.remove(ChunkPos.asLong(chunkX, chunkZ));
			
			ChunkProviderServer chunkProvider = worldServer.getChunkProvider();
			
			LongSet queuedSet = queuedChunkSetMap.computeIfAbsent(dimension, x -> new LongOpenHashSet());
			LongArrayFIFOQueue chunkQueue = chunkQueueMap.computeIfAbsent(dimension, x -> new LongArrayFIFOQueue());
			
			for (int dx = -1; dx <= 1; ++dx) {
				for (int dz = -1; dz <= 1; ++dz) {
					if (dx == 0 && dz == 0) {
						continue;
					}
					
					int x = chunkX + dx, z = chunkZ + dz;
					long chunkPosLong = ChunkPos.asLong(x, z);
					if (pendingSet.add(chunkPosLong) && chunkProvider.getLoadedChunk(x, z) != null && queuedSet.add(chunkPosLong)) {
						chunkQueue.enqueue(chunkPosLong);
					}
				}
			}
		}
		
		VaultGenerator vaultGenerator = vaultGeneratorMap.computeIfAbsent(dimension, x -> new VaultGenerator());
		vaultGenerator.generate(world, chunkX, chunkZ, null);
		vaultGenerator.generateStructure(world, event.getRand(), new ChunkPos(chunkX, chunkZ));
	}
	
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		if (wasteland_mapping_chunks_per_tick > 0) {
			if (event.phase != TickEvent.Phase.END || event.side == Side.CLIENT || !(event.world instanceof WorldServer world)) {
				return;
			}
			
			int dimension = world.provider.getDimension();
			if (dimension != wasteland_dimension) {
				return;
			}
			
			LongArrayFIFOQueue chunkQueue = chunkQueueMap.get(dimension);
			LongSet queuedSet = queuedChunkSetMap.get(dimension);
			LongSet pendingSet = pendingRecheckMap.get(dimension);
			if (chunkQueue == null || queuedSet == null || pendingSet == null || pendingSet.isEmpty()) {
				return;
			}
			
			ChunkProviderServer chunkProvider = world.getChunkProvider();
			int chunksToProcess = wasteland_mapping_chunks_per_tick;
			while (chunksToProcess > 0 && !chunkQueue.isEmpty()) {
				--chunksToProcess;
				
				long chunkPosLong = chunkQueue.dequeueLong();
				queuedSet.remove(chunkPosLong);
				
				if (!pendingSet.contains(chunkPosLong)) {
					continue;
				}
				
				int chunkX = (int) chunkPosLong, chunkZ = (int) (chunkPosLong >>> 32);
				Chunk chunk = chunkProvider.getLoadedChunk(chunkX, chunkZ);
				if (chunk == null || !chunk.isLoaded()) {
					continue;
				}
				
				processChunk(world, chunkX, chunkZ);
				pendingSet.remove(chunkPosLong);
			}
		}
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event) {
		if (event.getWorld().isRemote) {
			return;
		}
		
		int dimension = event.getWorld().provider.getDimension();
		
		pendingRecheckMap.remove(dimension);
		queuedChunkSetMap.remove(dimension);
		chunkQueueMap.remove(dimension);
		
		vaultGeneratorMap.remove(dimension);
	}
	
	private void processChunk(WorldServer world, int chunkX, int chunkZ) {
		ChunkProviderServer chunkProvider = world.getChunkProvider();
		Chunk chunk = chunkProvider.getLoadedChunk(chunkX, chunkZ);
		if (chunk == null || !chunk.isLoaded()) {
			return;
		}
		
		int startX = chunkX << 4, startZ = chunkZ << 4;
		int height = world.getHeight();
		
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < 16; ++x) {
				for (int z = 0; z < 16; ++z) {
					pos.setPos(startX + x, y, startZ + z);
					IBlockState state = chunk.getBlockState(pos), result = null;
					Block block;
					if (state != null && !(block = state.getBlock()).hasTileEntity(state)) {
						BasicRecipe recipe = RecipeHelper.blockRecipe(WASTELAND_BLOCK_MAPPING.get(), state);
						if (recipe != null) {
							result = RecipeHelper.getBlockStateFromProductList(recipe.getItemProducts(), 0);
						}
						
						if (result == null) {
							Material material = state.getMaterial();
							MapColor mapColor = material.getMaterialMapColor();
							if (material.equals(Material.ROCK) || WASTELAND_DEFAULT_MAPPING_STATES.get().contains(state) || WASTELAND_DEFAULT_MAPPING_BLOCKS.get().contains(block)) {
								result = state;
							}
							else if (material.equals(Material.GLASS)) {
								result = world.rand.nextBoolean() ? Blocks.AIR.getDefaultState() : state;
							}
							else if (material.isReplaceable() || mapColor.equals(MapColor.AIR) || MaterialHelper.isSnow(mapColor) || material.equals(Material.WATER)) {
								result = Blocks.AIR.getDefaultState();
							}
							else if (MaterialHelper.isDirt(mapColor)) {
								result = COARSE_DIRT;
							}
							else if (material.equals(Material.WOOD)) {
								if (block instanceof BlockDoor) {
									result = Blocks.AIR.getDefaultState();
								}
								else if (block instanceof BlockLog) {
									result = state;
								}
								else {
									result = world.rand.nextInt(8) == 0 ? Blocks.AIR.getDefaultState() : state;
								}
							}
							else if (material.equals(Material.PLANTS)) {
								result = world.rand.nextBoolean() ? Blocks.AIR.getDefaultState() : Blocks.DEADBUSH.getDefaultState();
							}
							else if (MaterialHelper.isFoliage(mapColor)) {
								result = Blocks.AIR.getDefaultState();
							}
							else if (MaterialHelper.isCloth(mapColor)) {
								result = world.rand.nextBoolean() ? Blocks.AIR.getDefaultState() : state;
							}
							else {
								result = state;
							}
						}
						
						if (result != state) {
							chunk.setBlockState(pos, result);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		World world = event.getWorld();
		if (world.isRemote || world.provider.getDimension() != wasteland_dimension || !(event.getEntity() instanceof EntityLiving entityLiving)) {
			return;
		}
		
		if (register_entity[0] && entityLiving instanceof INpc) {
			RadiationHelper.spawnFeralGhoul(world, entityLiving);
			entityLiving.setDead();
			event.setCanceled(true);
		}
	}
}
