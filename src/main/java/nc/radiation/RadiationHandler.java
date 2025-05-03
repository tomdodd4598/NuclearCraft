package nc.radiation;

import com.google.common.collect.Lists;
import nc.ModCheck;
import nc.capability.radiation.entity.IEntityRads;
import nc.capability.radiation.source.IRadiationSource;
import nc.entity.EntityFeralGhoul;
import nc.init.NCSounds;
import nc.network.radiation.PlayerRadsUpdatePacket;
import nc.recipe.*;
import nc.tile.radiation.ITileRadiationEnvironment;
import nc.util.*;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
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
	
	public static EnumFacing tile_side = EnumFacing.DOWN;
	
	public static boolean default_rad_immunity = false;
	public static String[] rad_immunity_stages = new String[] {};
	
	public static final Lazy<BasicRecipeHandler> RADIATION_BLOCK_PURIFICATION = new Lazy<>(() -> NCRecipes.radiation_block_purification);
	
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
		
		ChunkProviderServer chunkProvider = world.getChunkProvider();
		Collection<Chunk> chunks = chunkProvider.getLoadedChunks();
		
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
					if (entity instanceof EntityPlayer) {
						RadiationHelper.transferRadsFromInventoryToChunkBuffer(((EntityPlayer) entity).inventory, chunkSource);
					}
					else if (radiation_dropped_items && entity instanceof EntityItem) {
						RadiationHelper.transferRadiationFromStackToChunkBuffer(((EntityItem) entity).getItem(), chunkSource, 1D);
					}
					else if (entity instanceof EntityLiving entityLiving) {
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
						}
						else {
							entityRads.resetRecentPoisonAddition();
						}
						
						if (entityLiving instanceof IMob) {
							if (radiation_mob_rads_fatal && entityRads.isFatal()) {
								entityLiving.attackEntityFrom(DamageSources.FATAL_RADS, Float.MAX_VALUE);
							}
							else {
								RadiationHelper.applyEntityEffects(entityLiving, entityRads, tickMult, RadPotionEffects.MOB_RAD_LEVEL_LIST, RadPotionEffects.MOB_RAD_EFFECT_LISTS, RadPotionEffects.MOB_RAD_ATTRIBUTE_MAP);
							}
						}
						else {
							if (entityRads.isFatal()) {
								if (register_entity[0] && entityLiving instanceof INpc) {
									spawnFeralGhoul(world, entityLiving);
								}
								else if (radiation_passive_rads_fatal) {
									entityLiving.attackEntityFrom(DamageSources.FATAL_RADS, Float.MAX_VALUE);
								}
							}
							else {
								RadiationHelper.applyEntityEffects(entityLiving, entityRads, tickMult, RadPotionEffects.ENTITY_RAD_LEVEL_LIST, RadPotionEffects.ENTITY_RAD_EFFECT_LISTS, RadPotionEffects.ENTITY_RAD_ATTRIBUTE_MAP);
							}
						}
						entityRads.setRadiationLevel(entityRads.getRadiationLevel() * Math.pow(1D - radiation_decay_rate, tickMult));
					}
				}
			}
			
			chunkSource.setScrubbingFraction(0D);
			chunkSource.setEffectiveScrubberCount(0D);
			
			Collection<TileEntity> tiles = chunk.getTileEntityMap().values();
			
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
	
	public static final List<int[]> ADJACENT_COORDS = Lists.newArrayList(new int[] {1, 0}, new int[] {0, 1}, new int[] {-1, 0}, new int[] {0, -1});
	
	public static Chunk getRandomAdjacentChunk(ChunkProviderServer chunkProvider, Chunk chunk) {
		if (chunkProvider == null || chunk == null || !chunk.isLoaded()) {
			return null;
		}
		ChunkPos chunkPos = chunk.getPos();
		int x = chunkPos.x;
		int z = chunkPos.z;
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
			IBlockState state = world.getBlockState(randomChunkPos);
			
			ItemStack stack = StackHelper.blockStateToStack(state);
			if (stack != null && !stack.isEmpty()) {
				RecipeInfo<BasicRecipe> mutationInfo = RADIATION_BLOCK_PURIFICATION.get().getRecipeInfoFromInputs(Lists.newArrayList(stack), Collections.emptyList());
				if (mutationInfo != null && radiation >= mutationInfo.recipe.getBlockMutationThreshold()) {
					ItemStack output = RecipeHelper.getItemStackFromIngredientList(mutationInfo.recipe.getItemProducts(), 0);
					if (output != null) {
						IBlockState result = StackHelper.getBlockStateFromStack(output);
						if (result != null) {
							world.setBlockState(randomChunkPos, result);
						}
					}
				}
			}
		}
		
		j = radiation == 0D ? radiation_block_effect_max_rate : Math.min(radiation_block_effect_max_rate, (long) Math.log(Math.E - 1D + RecipeStats.getBlockPurificationThreshold() / radiation));
		while (j > 0) {
			--j;
			BlockPos randomChunkPos = newRandomPosInChunk(world, chunk);
			IBlockState state = world.getBlockState(randomChunkPos);
			ItemStack stack = StackHelper.blockStateToStack(state);
			if (stack != null && !stack.isEmpty()) {
				RecipeInfo<BasicRecipe> mutationInfo = RADIATION_BLOCK_PURIFICATION.get().getRecipeInfoFromInputs(Lists.newArrayList(stack), Collections.emptyList());
				if (mutationInfo != null && radiation < mutationInfo.recipe.getBlockMutationThreshold()) {
					ItemStack output = RecipeHelper.getItemStackFromIngredientList(mutationInfo.recipe.getItemProducts(), 0);
					if (output != null) {
						IBlockState result = StackHelper.getBlockStateFromStack(output);
						if (result != null) {
							world.setBlockState(randomChunkPos, result);
						}
					}
				}
			}
		}
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
	
	public static void spawnFeralGhoul(World world, EntityLiving entityLiving) {
		EntityFeralGhoul feralGhoul = new EntityFeralGhoul(world);
		feralGhoul.setLocationAndAngles(entityLiving.posX, entityLiving.posY, entityLiving.posZ, entityLiving.rotationYaw, entityLiving.rotationPitch);
		feralGhoul.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(feralGhoul)), null);
		feralGhoul.setNoAI(entityLiving.isAIDisabled());
		if (entityLiving.hasCustomName()) {
			feralGhoul.setCustomNameTag(entityLiving.getCustomNameTag());
			feralGhoul.setAlwaysRenderNameTag(entityLiving.getAlwaysRenderNameTag());
		}
		world.spawnEntity(feralGhoul);
		entityLiving.setDead();
	}
}
