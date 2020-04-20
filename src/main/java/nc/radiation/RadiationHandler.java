package nc.radiation;

import static nc.config.NCConfig.radiation_block_effect_max_rate;
import static nc.config.NCConfig.radiation_player_tick_rate;
import static nc.config.NCConfig.radiation_world_chunks_per_tick;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import nc.ModCheck;
import nc.capability.radiation.entity.IEntityRads;
import nc.capability.radiation.source.IRadiationSource;
import nc.config.NCConfig;
import nc.entity.EntityFeralGhoul;
import nc.init.NCSounds;
import nc.network.PacketHandler;
import nc.network.radiation.PlayerRadsUpdatePacket;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import nc.recipe.RecipeHelper;
import nc.recipe.RecipeInfo;
import nc.tile.radiation.ITileRadiationEnvironment;
import nc.util.DamageSources;
import nc.util.ItemStackHelper;
import nc.util.Lang;
import nc.util.StructureHelper;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.INpc;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class RadiationHandler {
	
	private static final Random RAND = new Random();
	
	private static final String RAD_X_WORE_OFF = Lang.localise("message.nuclearcraft.rad_x_wore_off");
	private static final String RAD_WARNING = Lang.localise("message.nuclearcraft.rad_warning");
	
	private static EnumFacing tile_side = EnumFacing.DOWN;
	
	public static boolean default_rad_immunity = false;
	public static String[] rad_immunity_stages = new String[] {};
	
	@SubscribeEvent
	public void updatePlayerRadiation(TickEvent.PlayerTickEvent event) {
		if (!NCConfig.radiation_enabled_public) return;
		
		if (!NCConfig.radiation_require_counter && event.phase == TickEvent.Phase.START && event.side == Side.CLIENT) playGeigerSound(event.player);
		
		if (event.phase != TickEvent.Phase.START || ((event.player.world.getTotalWorldTime() + event.player.getUniqueID().hashCode()) % radiation_player_tick_rate) != 0) return;
		
		if (event.side == Side.SERVER && event.player instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP)event.player;
			IEntityRads playerRads = RadiationHelper.getEntityRadiation(player);
			if (playerRads == null) return;
			
			if (ModCheck.gameStagesLoaded()) {
				playerRads.setRadiationImmunityStage(default_rad_immunity ^ GameStageHelper.hasAnyOf(player, rad_immunity_stages));
			}
			
			if (!player.isCreative() && playerRads.isFatal()) {
				player.attackEntityFrom(DamageSources.FATAL_RADS, Float.MAX_VALUE);
			}
			
			double previousImmunityTime = playerRads.getRadiationImmunityTime();
			if (previousImmunityTime > 0D) {
				playerRads.setRadiationImmunityTime(previousImmunityTime - radiation_player_tick_rate);
			}
			double previousRadPercentage = playerRads.getRadsPercentage();
			
			playerRads.setExternalRadiationResistance(RadiationHelper.getArmorInventoryRadResistance(player));
			
			if (NCConfig.radiation_player_decay_rate > 0D) {
				playerRads.setTotalRads(playerRads.getTotalRads()*Math.pow(1D - NCConfig.radiation_player_decay_rate, radiation_player_tick_rate), false);
			}
			
			double radiationLevel = RadiationHelper.transferRadsFromInventoryToPlayer(playerRads, player, radiation_player_tick_rate);
			Chunk chunk = player.world.getChunk((int) Math.floor(player.posX) >> 4, (int) Math.floor(player.posZ) >> 4);
			if (chunk.isLoaded()) {
				IRadiationSource chunkSource = RadiationHelper.getRadiationSource(chunk);
				radiationLevel += RadiationHelper.transferRadsToPlayer(chunkSource, playerRads, player, radiation_player_tick_rate);
			}
			
			if (playerRads.getPoisonBuffer() > 0D) {
				double poisonRads = Math.min(playerRads.getPoisonBuffer()/radiation_player_tick_rate, playerRads.getRecentPoisonAddition()/NCConfig.radiation_poison_time);
				radiationLevel += RadiationHelper.addRadsToEntity(playerRads, player, poisonRads, true, true, radiation_player_tick_rate);
				playerRads.setPoisonBuffer(playerRads.getPoisonBuffer() - poisonRads*radiation_player_tick_rate);
				if (playerRads.getPoisonBuffer() == 0D) playerRads.resetRecentPoisonAddition();
			}
			else playerRads.resetRecentPoisonAddition();
			
			playerRads.setRadiationLevel(radiationLevel);
			
			if (!player.isCreative()) {
				if (playerRads.isFatal()) {
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
			double recentRadXAdditionModified = NCConfig.radiation_rad_x_amount*(1D + playerRads.getRecentRadXAddition())/(1D + NCConfig.radiation_rad_x_amount);
			if (previousInternalResistance > 0D) {
				double radXDecayRate = Math.max(previousInternalResistance, recentRadXAdditionModified)/NCConfig.radiation_rad_x_lifetime;
				playerRads.setInternalRadiationResistance(Math.max(0D, previousInternalResistance - radXDecayRate*radiation_player_tick_rate));
				if (playerRads.getInternalRadiationResistance() == 0D) {
					playerRads.resetRecentRadXAddition();
					playerRads.setRadXWoreOff(true);
				}
			}
			else {
				if (previousInternalResistance < 0D) {
					double radXDecayRate = Math.max(-previousInternalResistance, recentRadXAdditionModified)/NCConfig.radiation_rad_x_lifetime;
					playerRads.setInternalRadiationResistance(Math.min(0D, previousInternalResistance + radXDecayRate*radiation_player_tick_rate));
					if (playerRads.getInternalRadiationResistance() == 0D) playerRads.resetRecentRadXAddition();
				}
				else {
					playerRads.resetRecentRadXAddition();
				}
				playerRads.setRadXWoreOff(false);
			}
			
			if (playerRads.getRadXWoreOff() && playerRads.getRadXUsed()) playerRads.setRadXUsed(false);
			
			if (playerRads.getRadawayBuffer(false) > 0D) {
				double change = Math.min(playerRads.getRadawayBuffer(false), playerRads.getRecentRadawayAddition()*NCConfig.radiation_radaway_rate*radiation_player_tick_rate/NCConfig.radiation_radaway_amount);
				playerRads.setTotalRads(playerRads.getTotalRads() - change, false);
				playerRads.setRadawayBuffer(false, playerRads.getRadawayBuffer(false) - change);
				if (playerRads.getRadawayBuffer(false) == 0D) playerRads.resetRecentRadawayAddition();
			}
			else playerRads.resetRecentRadawayAddition();
			
			if (playerRads.getRadawayBuffer(true) > 0D) {
				double change = Math.min(playerRads.getRadawayBuffer(true), NCConfig.radiation_radaway_slow_rate*radiation_player_tick_rate);
				playerRads.setTotalRads(playerRads.getTotalRads() - change, false);
				playerRads.setRadawayBuffer(true, playerRads.getRadawayBuffer(true) - change);
			}
			
			if (playerRads.getRadawayCooldown() > 0D) {
				playerRads.setRadawayCooldown(playerRads.getRadawayCooldown() - radiation_player_tick_rate);
			}
			if (playerRads.getRadXCooldown() > 0D) {
				playerRads.setRadXCooldown(playerRads.getRadXCooldown() - radiation_player_tick_rate);
			}
			
			PacketHandler.instance.sendTo(new PlayerRadsUpdatePacket(playerRads), player);
			
			if (!player.isCreative() && !playerRads.isImmune()) {
				RadiationHelper.applyPotionEffects(player, playerRads, RadPotionEffects.PLAYER_RAD_LEVEL_LIST, RadPotionEffects.PLAYER_DEBUFF_LIST);
			}
		}
		else {
			EntityPlayer player = event.player;
			IEntityRads playerRads = RadiationHelper.getEntityRadiation(player);
			if (playerRads == null) return;
			if (playerRads.getRadXWoreOff() && playerRads.getRadXUsed()) {
				player.playSound(NCSounds.chems_wear_off, 0.65F, 1F);
				player.sendMessage(new TextComponentString(TextFormatting.ITALIC + RAD_X_WORE_OFF));
			}
			if (playerRads.getShouldWarn()) {
				player.playSound(NCSounds.chems_wear_off, 0.8F, 0.7F);
				player.sendMessage(new TextComponentString(TextFormatting.GOLD + RAD_WARNING));
			}
		}
	}
	
	@SubscribeEvent
	public void updateWorldRadiation(TickEvent.WorldTickEvent event) {
		if (!NCConfig.radiation_enabled_public) return;
		
		if (event.phase != TickEvent.Phase.START || event.side == Side.CLIENT || !(event.world instanceof WorldServer)) return;
		WorldServer world = (WorldServer)event.world;
		
		ChunkProviderServer chunkProvider = world.getChunkProvider();
		Collection<Chunk> loadedChunks = chunkProvider.getLoadedChunks();
		int chunkArrSize = loadedChunks.size();
		Chunk[] chunkArray = loadedChunks.toArray(new Chunk[chunkArrSize]);
		int chunkStart = RAND.nextInt(chunkArrSize + 1);
		int chunksPerTick = Math.min(radiation_world_chunks_per_tick, chunkArrSize);
		int tickMult = chunkArrSize > 0 ? Math.max(1, chunkArrSize/chunksPerTick) : 1;
		
		BiomeProvider biomeProvider = world.getBiomeProvider();
		int dimension = world.provider.getDimension();
		BlockPos randomOffsetPos = newRandomOffsetPos();
		String randomStructure = (ModCheck.cubicChunksLoaded() || RadStructures.STRUCTURE_LIST.isEmpty()) ? null : RadStructures.STRUCTURE_LIST.get(RAND.nextInt(RadStructures.STRUCTURE_LIST.size()));
		
		if (chunkArrSize > 0) for (int i = chunkStart; i < chunkStart + chunksPerTick; i++) {
			Chunk chunk = chunkArray[i % chunkArrSize];
			if (!chunk.isLoaded()) continue;
			
			IRadiationSource chunkSource = RadiationHelper.getRadiationSource(chunk);
			if (chunkSource == null) continue;
			
			for (int j = 0; j < chunk.getEntityLists().length; j++) {
				ClassInheritanceMultiMap<Entity> entitySubset = chunk.getEntityLists()[j];
				Entity[] entityArray = entitySubset.toArray(new Entity[entitySubset.size()]);
				for (Entity entity : entityArray) {
					if (entity instanceof EntityPlayer) {
						RadiationHelper.transferRadsFromInventoryToChunkBuffer(((EntityPlayer)entity).inventory, chunkSource);
					}
					else if (NCConfig.radiation_dropped_items && entity instanceof EntityItem) {
						RadiationHelper.transferRadiationFromStackToChunkBuffer(((EntityItem)entity).getItem(), chunkSource, 1D);
					}
					else if (entity instanceof EntityLiving) {
						EntityLiving entityLiving = (EntityLiving)entity;
						IEntityRads entityRads = RadiationHelper.getEntityRadiation(entityLiving);
						if (entityRads == null) continue;
						
						entityRads.setExternalRadiationResistance(RadiationHelper.getEntityArmorRadResistance(entityLiving));
						
						if (NCConfig.radiation_entity_decay_rate > 0D) {
							entityRads.setTotalRads(entityRads.getTotalRads()*Math.pow(1D - NCConfig.radiation_entity_decay_rate, tickMult), false);
						}
						
						RadiationHelper.transferRadsFromSourceToEntity(chunkSource, entityRads, entityLiving, tickMult);
						
						if (entityRads.getPoisonBuffer() > 0D) {
							double poisonRads = Math.min(entityRads.getPoisonBuffer(), entityRads.getRecentPoisonAddition()*tickMult/NCConfig.radiation_poison_time);
							entityRads.setTotalRads(entityRads.getTotalRads() + poisonRads, false);
							entityRads.setPoisonBuffer(entityRads.getPoisonBuffer() - poisonRads);
							if (entityRads.getPoisonBuffer() == 0D) entityRads.resetRecentPoisonAddition();
						}
						else entityRads.resetRecentPoisonAddition();
						
						if (entityLiving instanceof IMob) {
							RadiationHelper.applyPotionEffects(entityLiving, entityRads, RadPotionEffects.MOB_RAD_LEVEL_LIST, RadPotionEffects.MOB_EFFECTS_LIST);
						}
						else {
							if (entityRads.isFatal()) {
								if (NCConfig.register_entity[0] && entityLiving instanceof INpc) {
									spawnFeralGhoul(world, entityLiving);
								}
								else {
									entityLiving.attackEntityFrom(DamageSources.FATAL_RADS, Float.MAX_VALUE);
								}
							}
							else {
								RadiationHelper.applyPotionEffects(entityLiving, entityRads, RadPotionEffects.ENTITY_RAD_LEVEL_LIST, RadPotionEffects.ENTITY_DEBUFF_LIST);
							}
						}
						entityRads.setRadiationLevel(entityRads.getRadiationLevel()*Math.pow(1D - NCConfig.radiation_decay_rate, tickMult));
					}
				}
			}
			
			chunkSource.setScrubbingFraction(0D);
			chunkSource.setEffectiveScrubberCount(0D);
			
			Collection<TileEntity> tileCollection = chunk.getTileEntityMap().values();
			TileEntity[] tileArray = tileCollection.toArray(new TileEntity[tileCollection.size()]);
			
			for (TileEntity tile : tileArray) {
				RadiationHelper.transferRadiationFromProviderToChunkBuffer(tile, tile_side, chunkSource);
			}
			
			if (RadWorlds.RAD_MAP.containsKey(dimension)) {
				RadiationHelper.addToSourceBuffer(chunkSource, RadWorlds.RAD_MAP.get(dimension));
			}
			
			if (!RadBiomes.DIM_BLACKLIST.contains(dimension)) {
				Double biomeRadiation = RadBiomes.RAD_MAP.get(chunk.getBiome(randomOffsetPos, biomeProvider));
				if (biomeRadiation != null) RadiationHelper.addToSourceBuffer(chunkSource, biomeRadiation);
			}
			
			BlockPos randomChunkPos = newRandomPosInChunk(chunk);
			if (randomStructure != null && StructureHelper.CACHE.isInStructure(world, randomStructure, randomChunkPos)) {
				Double structureRadiation = RadStructures.RAD_MAP.get(randomStructure);
				if (structureRadiation != null) RadiationHelper.addToSourceBuffer(chunkSource, structureRadiation);
			}
			
			if (i == chunkStart) {
				int packed = RecipeItemHelper.pack(ItemStackHelper.blockStateToStack(world.getBlockState(randomChunkPos)));
				if (RadSources.STACK_MAP.containsKey(packed)) {
					RadiationHelper.addToSourceBuffer(chunkSource, RadSources.STACK_MAP.get(packed));
				}
			}
			
			double currentLevel = chunkSource.getRadiationLevel(), currentBuffer = chunkSource.getRadiationBuffer();
			for (TileEntity tile : tileArray) {
				if (tile instanceof ITileRadiationEnvironment) {
					((ITileRadiationEnvironment)tile).setCurrentChunkRadiationLevel(currentLevel);
					((ITileRadiationEnvironment)tile).setCurrentChunkRadiationBuffer(currentBuffer);
					RadiationHelper.addScrubbingFractionToChunk(RadiationHelper.getRadiationSource(chunk), (ITileRadiationEnvironment)tile);
				}
			}
			
			if (NCConfig.radiation_scrubber_non_linear) {
				double scrubbers = chunkSource.getEffectiveScrubberCount();
				double scrubbingFraction = RadiationHelper.getAltScrubbingFraction(scrubbers);
				
				RadiationHelper.addToSourceBuffer(chunkSource, -scrubbingFraction*chunkSource.getRadiationBuffer());
				chunkSource.setScrubbingFraction(scrubbingFraction);
			}
			
			double changeRate = (chunkSource.getRadiationLevel() < chunkSource.getRadiationBuffer()) ? NCConfig.radiation_spread_rate : NCConfig.radiation_decay_rate*(1D - chunkSource.getScrubbingFraction()) + NCConfig.radiation_spread_rate*chunkSource.getScrubbingFraction();
			
			double newLevel = Math.max(0D, chunkSource.getRadiationLevel() + (chunkSource.getRadiationBuffer() - chunkSource.getRadiationLevel())*changeRate);
			if (NCConfig.radiation_chunk_limit >= 0D) {
				newLevel = Math.min(newLevel, NCConfig.radiation_chunk_limit);
			}
			Biome biome = chunk.getBiome(randomOffsetPos, biomeProvider);
			if (RadBiomes.LIMIT_MAP.containsKey(biome)) {
				newLevel = Math.min(newLevel, RadBiomes.LIMIT_MAP.get(biome));
			}
			if (RadWorlds.LIMIT_MAP.containsKey(dimension)) {
				newLevel = Math.min(newLevel, RadWorlds.LIMIT_MAP.get(dimension));
			}
			
			chunkSource.setRadiationLevel(newLevel);
			
			mutateTerrain(world, chunk, newLevel);
		}
		
		if (chunkArrSize > 0) for (int i = chunkStart; i < chunkStart + chunksPerTick; i++) {
			Chunk chunk = chunkArray[i % chunkArrSize];
			// Emptying buffers here too!
			RadiationHelper.spreadRadiationFromChunk(chunk, getRandomAdjacentChunk(chunkProvider, chunk));
		}
		
		tile_side = EnumFacing.byIndex(tile_side.getIndex() + 1);
	}
	
	private static final List<byte[]> ADJACENT_COORDS = Lists.newArrayList(new byte[] {1, 0}, new byte[] {0, 1}, new byte[] {-1, 0}, new byte[] {0, -1});
	
	private static Chunk getRandomAdjacentChunk(ChunkProviderServer chunkProvider, Chunk chunk) {
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
	
	private static BlockPos newRandomOffsetPos() {
		return new BlockPos(RAND.nextInt(16), RAND.nextInt(256), RAND.nextInt(16));
	}
	
	private static BlockPos newRandomPosInChunk(Chunk chunk) {
		return chunk.getPos().getBlock(RAND.nextInt(16), RAND.nextInt(256), RAND.nextInt(16));
	}
	
	private static void mutateTerrain(World world, Chunk chunk, double radiation) {
		long j = Math.min(radiation_block_effect_max_rate, (long) Math.log(Math.E - 1D + radiation/getBlockMutationThreshold()));
		while (j > 0) {
			j--;
			BlockPos randomChunkPos = newRandomPosInChunk(chunk);
			IBlockState state = world.getBlockState(randomChunkPos);
			
			ItemStack stack = ItemStackHelper.blockStateToStack(state);
			if (stack != null && !stack.isEmpty()) {
				RecipeInfo<ProcessorRecipe> mutationInfo = NCRecipes.radiation_block_mutation.getRecipeInfoFromInputs(Lists.newArrayList(stack), new ArrayList<>());
				if (mutationInfo != null && radiation >= mutationInfo.getRecipe().getBlockMutationThreshold()) {
					ItemStack output = RecipeHelper.getItemStackFromIngredientList(mutationInfo.getRecipe().getItemProducts(), 0);
					if (output != null) {
						IBlockState result = ItemStackHelper.getBlockStateFromStack(output);
						if (result != null) {
							world.setBlockState(randomChunkPos, result);
						}
					}
				}
			}
		}
		
		j = radiation == 0D ? radiation_block_effect_max_rate : Math.min(radiation_block_effect_max_rate, (long) Math.log(Math.E - 1D + getBlockPurificationThreshold()/radiation));
		while (j > 0) {
			j--;
			BlockPos randomChunkPos = newRandomPosInChunk(chunk);
			IBlockState state = world.getBlockState(randomChunkPos);
			ItemStack stack = ItemStackHelper.blockStateToStack(state);
			if (stack != null && !stack.isEmpty()) {
				RecipeInfo<ProcessorRecipe> mutationInfo = NCRecipes.radiation_block_purification.getRecipeInfoFromInputs(Lists.newArrayList(stack), new ArrayList<>());
				if (mutationInfo != null && radiation < mutationInfo.getRecipe().getBlockMutationThreshold()) {
					ItemStack output = RecipeHelper.getItemStackFromIngredientList(mutationInfo.getRecipe().getItemProducts(), 0);
					if (output != null) {
						IBlockState result = ItemStackHelper.getBlockStateFromStack(output);
						if (result != null) {
							world.setBlockState(randomChunkPos, result);
						}
					}
				}
			}
		}
	}
	
	private static Double block_mutation_threshold = null;
	private static double getBlockMutationThreshold() {
		if (block_mutation_threshold == null) {
			double threshold = Double.MAX_VALUE;
			for (ProcessorRecipe recipe : NCRecipes.radiation_block_mutation.getRecipeList()) {
				if (recipe != null) threshold = Math.min(threshold, recipe.getBlockMutationThreshold());
			}
			block_mutation_threshold = new Double(threshold);
		}
		return block_mutation_threshold.doubleValue();
	}
	
	private static Double block_purification_threshold = null;
	private static double getBlockPurificationThreshold() {
		if (block_purification_threshold == null) {
			double threshold = 0D;
			for (ProcessorRecipe recipe : NCRecipes.radiation_block_purification.getRecipeList()) {
				if (recipe != null) threshold = Math.max(threshold, recipe.getBlockMutationThreshold());
			}
			block_purification_threshold = new Double(threshold);
		}
		return block_purification_threshold.doubleValue();
	}
	
	public static void playGeigerSound(EntityPlayer player) {
		IEntityRads entityRads = RadiationHelper.getEntityRadiation(player);
		if (entityRads == null || entityRads.isRadiationUndetectable()) return;
		
		double radiation = entityRads.getRawRadiationLevel();
		int loops = radiation == 0D ? 0 : Math.min(4, (int) Math.log(Math.E + entityRads.getRawRadiationLevel()));
		if (loops == 0) return;
		
		double soundChance = Math.cbrt(entityRads.getRawRadiationLevel()/200D);
		float soundVolume = MathHelper.clamp((float)(8F*soundChance), 0.55F, 1.1F);
		for (int i = 0; i < loops; i++) {
			if (RAND.nextDouble() < soundChance) {
				player.playSound(NCSounds.geiger_tick, soundVolume + RAND.nextFloat()*0.12F, 0.92F + RAND.nextFloat()*0.16F);
			}
		}
	}
	
	private static void spawnFeralGhoul(World world, EntityLiving entityLiving) {
		EntityFeralGhoul feralGhoul = new EntityFeralGhoul(world);
		feralGhoul.setLocationAndAngles(entityLiving.posX, entityLiving.posY, entityLiving.posZ, entityLiving.rotationYaw, entityLiving.rotationPitch);
		feralGhoul.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(feralGhoul)), (IEntityLivingData)null);
		feralGhoul.setNoAI(entityLiving.isAIDisabled());
		if (entityLiving.hasCustomName()) {
			feralGhoul.setCustomNameTag(entityLiving.getCustomNameTag());
			feralGhoul.setAlwaysRenderNameTag(entityLiving.getAlwaysRenderNameTag());
		}
		world.spawnEntity(feralGhoul);
		entityLiving.setDead();
	}
}
