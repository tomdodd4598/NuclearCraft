package nc.radiation;

import java.util.List;

import ic2.api.reactor.IReactor;
import nc.ModCheck;
import nc.capability.radiation.IRadiation;
import nc.capability.radiation.entity.IEntityRads;
import nc.capability.radiation.resistance.IRadiationResistance;
import nc.capability.radiation.source.IRadiationSource;
import nc.config.NCConfig;
import nc.tile.radiation.ITileRadiationEnvironment;
import nc.util.ArmorHelper;
import nc.util.NCMath;
import nc.util.UnitHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class RadiationHelper {
	
	// Capability Getters
	
	public static IEntityRads getEntityRadiation(EntityLivingBase entity) {
		if (entity == null || !entity.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) {
			return null;
		}
		return entity.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
	}
	
	public static IRadiationSource getRadiationSource(ICapabilityProvider provider) {
		if (provider == null || !provider.hasCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null)) {
			return null;
		}
		return provider.getCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null);
	}
	
	public static IRadiationResistance getRadiationResistance(ICapabilityProvider provider) {
		if (provider == null || !provider.hasCapability(IRadiationResistance.CAPABILITY_RADIATION_RESISTANCE, null)) {
			return null;
		}
		return provider.getCapability(IRadiationResistance.CAPABILITY_RADIATION_RESISTANCE, null);
	}
	
	public static IItemHandler getTileInventory(ICapabilityProvider provider, EnumFacing side) {
		if (!(provider instanceof TileEntity) || !provider.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side)) {
			return null;
		}
		return provider.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
	}
	
	// Radiation Level Modification
	
	public static void addToSourceBuffer(IRadiationSource source, double addedRadiation) {
		source.setRadiationBuffer(source.getRadiationBuffer() + addedRadiation);
	}
	
	/** Only use for radiation leaks, etc. */
	public static void addToSourceRadiation(IRadiationSource source, double addedRadiation) {
		source.setRadiationLevel(source.getRadiationLevel() + addedRadiation);
	}
	
	// ITileRadiationEnvironment -> ChunkBuffer
	
	public static void addFractionToChunkBuffer(IRadiationSource chunkSource, ITileRadiationEnvironment tile) {
		if (chunkSource == null) {
			return;
		}
		addToSourceBuffer(chunkSource, tile.getContributionFraction()*tile.getCurrentChunkBuffer());
		
		if (tile.getContributionFraction() < 0D) {
			chunkSource.addScrubbingFraction(-tile.getContributionFraction());
		}
	}
	
	// ItemStack -> ChunkBuffer
	
	public static void transferRadiationFromStackToChunkBuffer(ItemStack stack, IRadiationSource chunkSource, double multiplier) {
		if (chunkSource == null) {
			return;
		}
		addToSourceBuffer(chunkSource, getRadiationFromStack(stack, multiplier));
	}
	
	public static double getRadiationFromStack(ItemStack stack, double multiplier) {
		if (stack.isEmpty()) {
			return 0D;
		}
		IRadiationSource stackSource = getRadiationSource(stack);
		if (stackSource == null) {
			return 0D;
		}
		return stackSource.getRadiationLevel()*stack.getCount()*multiplier;
	}
	
	// Source -> ChunkBuffer
	
	public static void transferRadiationFromProviderToChunkBuffer(ICapabilityProvider provider, EnumFacing side, IRadiationSource chunkSource) {
		if (chunkSource == null) {
			return;
		}
		
		double rawRadiation = 0D;
		if (ModCheck.ic2Loaded()) {
			if (provider instanceof IReactor) {
				rawRadiation += ((IReactor)provider).getReactorEUEnergyOutput()*0.00001D;
			}
		}
		
		IItemHandler inventory = getTileInventory(provider, side);
		if (NCConfig.radiation_hardcore_containers > 0D && inventory != null) {
			for (int i = 0; i < inventory.getSlots(); i++) {
				ItemStack stack = inventory.getStackInSlot(i);
				rawRadiation += getRadiationFromStack(stack, NCConfig.radiation_hardcore_containers);
			}
		}
		
		IRadiationSource radiationSource = getRadiationSource(provider);
		if (radiationSource != null) {
			rawRadiation += radiationSource.getRadiationLevel();
		}
		
		double resistance = 0D;
		IRadiationResistance providerResistance = getRadiationResistance(provider);
		if (providerResistance != null) {
			resistance = providerResistance.getRadiationResistance();
		}
		
		double radiation = rawRadiation <= 0D ? 0D : NCMath.square(rawRadiation)/(rawRadiation + resistance);
		addToSourceBuffer(chunkSource, radiation);
	}
	
	// Inventory -> ChunkBuffer
	
	public static void transferRadsFromInventoryToChunkBuffer(InventoryPlayer inventory, IRadiationSource chunkSource) {
		if (!NCConfig.radiation_hardcore_stacks) {
			return;
		}
		for (ItemStack stack : inventory.mainInventory) {
			if (!stack.isEmpty()) {
				transferRadiationFromProviderToChunkBuffer(stack, null, chunkSource);
			}
		}
		for (ItemStack stack : inventory.armorInventory) {
			if (!stack.isEmpty()) {
				transferRadiationFromProviderToChunkBuffer(stack, null, chunkSource);
			}
		}
		for (ItemStack stack : inventory.offHandInventory) {
			if (!stack.isEmpty()) {
				transferRadiationFromProviderToChunkBuffer(stack, null, chunkSource);
			}
		}
	}
	
	// Chunk Set Previous Radiation and Spread
	
	public static void spreadRadiationFromChunk(Chunk chunk, Chunk targetChunk) {
		if (chunk != null && chunk.isLoaded()) {
			IRadiationSource chunkSource = getRadiationSource(chunk);
			if (chunkSource == null) return;
			
			if (targetChunk != null && targetChunk.isLoaded()) {
				IRadiationSource targetChunkSource = getRadiationSource(targetChunk);
				if (targetChunkSource != null && !chunkSource.isRadiationNegligible() && targetChunkSource.getScrubbingFraction() < 1D) {
					double spreadMult = 1D - targetChunkSource.getScrubbingFraction();
					if (spreadMult > 0D && (targetChunkSource.getRadiationLevel() == 0D || chunkSource.getRadiationLevel()/targetChunkSource.getRadiationLevel() > (1D + NCConfig.radiation_spread_gradient)/spreadMult)) {
						double radiationSpread = (chunkSource.getRadiationLevel() - targetChunkSource.getRadiationLevel())*NCConfig.radiation_spread_rate*spreadMult;
						chunkSource.setRadiationLevel(chunkSource.getRadiationLevel() - radiationSpread);
						targetChunkSource.setRadiationLevel(targetChunkSource.getRadiationLevel() + radiationSpread);
					}
				}
			}
			
			chunkSource.setRadiationBuffer(0D);
			if (chunkSource.isRadiationNegligible()) {
				chunkSource.setRadiationLevel(0D);
			}
		}
	}
	
	// Player Radiation Resistance
	
	public static double getArmorInventoryRadResistance(Entity entity) {
		if (entity == null) {
			return 0D;
		}
		double resistance = 0D;
		for (ItemStack armor : entity.getArmorInventoryList()) {
			resistance += getArmorRadResistance(armor);
		}
		return resistance;
	}
	
	private static double getArmorRadResistance(ItemStack armor) {
		if (armor.isEmpty()) {
			return 0D;
		}
		double resistance = 0D;
		IRadiationResistance armorResistance = getRadiationResistance(armor);
		if (armorResistance != null) {
			resistance += armorResistance.getRadiationResistance();
		}
		if (armor.hasTagCompound() && armor.getTagCompound().hasKey("ncRadiationResistance")) {
			resistance += armor.getTagCompound().getDouble("ncRadiationResistance");
		}
		return resistance;
	}
	
	// Entity Radiation Resistance
	
	public static double addRadsToEntity(IEntityRads entityRads, double rawRadiation, boolean ignoreResistance, int updateRate) {
		double resistance = ignoreResistance ? Math.min(0D, entityRads.getInternalRadiationResistance()) : entityRads.getFullRadiationResistance();
		
		double addedRadiation = rawRadiation <= 0D ? 0D : resistance > 0D ? NCMath.square(rawRadiation)/(rawRadiation + resistance) : rawRadiation*(1D - resistance);
		entityRads.setTotalRads(entityRads.getTotalRads() + addedRadiation*updateRate, true);
		return addedRadiation;
	}
	
	public static double getEntityArmorRadResistance(EntityLiving entityLiving) {
		double resistance = getArmorInventoryRadResistance(entityLiving);
		if (NCConfig.radiation_horse_armor_public && entityLiving instanceof EntityHorse) {
			resistance += getHorseArmorRadResistance((EntityHorse)entityLiving);
		}
		return resistance;
	}
	
	private static double getHorseArmorRadResistance(EntityHorse horse) {
		double resistance = 0D;
		NBTTagCompound compound = new NBTTagCompound();
		horse.writeEntityToNBT(compound);
		
		ItemStack armor = new ItemStack(compound.getCompoundTag("ArmorItem"));
		if (ArmorHelper.isHorseArmor(armor.getItem())) {
			resistance += getArmorRadResistance(armor);
		}
		return resistance;
	}
	
	// Inventory -> Player
	
	public static double transferRadsFromInventoryToPlayer(IEntityRads playerRads, EntityPlayer player, int updateRate) {
		double radiationLevel = 0D;
		InventoryPlayer inventory = player.inventory;
		for (ItemStack stack : inventory.mainInventory) {
			if (!stack.isEmpty()) {
				radiationLevel += transferRadsFromStackToPlayer(stack, playerRads, player, updateRate);
			}
		}
		for (ItemStack stack : inventory.armorInventory) {
			if (!stack.isEmpty()) {
				radiationLevel += transferRadsFromStackToPlayer(stack, playerRads, player, updateRate);
			}
		}
		for (ItemStack stack : inventory.offHandInventory) {
			if (!stack.isEmpty()) {
				radiationLevel += transferRadsFromStackToPlayer(stack, playerRads, player, updateRate);
			}
		}
		return radiationLevel;
	}
	
	private static double transferRadsFromStackToPlayer(ItemStack stack, IEntityRads playerRads, EntityPlayer player, int updateRate) {
		IRadiationSource stackSource = getRadiationSource(stack);
		if (stackSource == null) {
			return 0D;
		}
		return addRadsToEntity(playerRads, stackSource.getRadiationLevel()*stack.getCount(), false, updateRate);
	}
	
	// Source -> Player
	
	public static double transferRadsToPlayer(IRadiationSource source, IEntityRads playerRads, EntityPlayer player, int updateRate) {
		if (source == null) {
			return 0D;
		}
		return addRadsToEntity(playerRads, source.getRadiationLevel(), false, updateRate);
	}
	
	// Biome -> Player
	
	/*public static double transferBackgroundRadsToPlayer(Biome biome, IEntityRads playerRads, EntityPlayer player, int updateRate) {
		Double biomeRadiation = RadBiomes.RAD_MAP.get(biome);
		if (biomeRadiation == null) {
			return 0D;
		}
		return addRadsToPlayer(playerRads, player, biomeRadiation, updateRate);
	}*/
	
	// Source -> Entity
	
	public static void transferRadsFromSourceToEntity(IRadiationSource source, IEntityRads entityRads, EntityLiving entityLiving, int updateRate) {
		if (source == null) {
			return;
		}
		entityRads.setRadiationLevel(addRadsToEntity(entityRads, source.getRadiationLevel(), false, updateRate));
	}
	
	// Biome -> Entity
	
	/*public static void transferBackgroundRadsToEntity(Biome biome, IEntityRads entityRads, EntityLiving entityLiving, int updateRate) {
		Double biomeRadiation = RadBiomes.RAD_MAP.get(biome);
		if (biomeRadiation != null) {
			entityRads.setRadiationLevel(addRadsToEntity(entityRads, entityLiving, biomeRadiation, updateRate));
		}
	}*/
	
	// Entity Symptoms
	
	public static void applyPotionEffects(EntityLivingBase entity, IEntityRads entityRads, List<Double> radLevelList, List<List<PotionEffect>> potionList) {
		if (radLevelList.isEmpty() || radLevelList.size() != potionList.size()) {
			return;
		}
		double radPercentage = entityRads.getRadsPercentage();
		
		for (int i = 0; i < radLevelList.size(); i++) {
			final int j = radLevelList.size() - 1 - i;
			if (radPercentage >= radLevelList.get(j)) {
				for (PotionEffect potionEffect : potionList.get(j)) {
					entity.addPotionEffect(new PotionEffect(potionEffect));
				}
				break;
			}
		}
	}
	
	// Text Colours
	
	public static TextFormatting getRadsTextColor(IEntityRads entityRads) {
		double radsPercent = entityRads.getRadsPercentage();
		return radsPercent < 30 ? TextFormatting.WHITE : radsPercent < 50 ? TextFormatting.YELLOW : radsPercent < 70 ? TextFormatting.GOLD : radsPercent < 90 ? TextFormatting.RED : TextFormatting.DARK_RED;
	}
	
	public static TextFormatting getRadiationTextColor(double radiation) {
		return radiation < 0.000000001D ? TextFormatting.WHITE : radiation < 0.001D ? TextFormatting.YELLOW : radiation < 0.1D ? TextFormatting.GOLD : radiation < 1D ? TextFormatting.RED : TextFormatting.DARK_RED;
	}
	
	public static TextFormatting getRadiationTextColor(IRadiation irradiated) {
		return getRadiationTextColor(irradiated.getRadiationLevel());
	}
	
	public static TextFormatting getRawRadiationTextColor(IEntityRads entityRads) {
		return getRadiationTextColor(entityRads.getRawRadiationLevel());
	}
	
	public static TextFormatting getFoodRadiationTextColor(double radiation) {
		return radiation <= -100D ? TextFormatting.LIGHT_PURPLE : radiation <= -10D ? TextFormatting.BLUE : radiation < 0D ? TextFormatting.AQUA : radiation < 0.1D ? TextFormatting.WHITE : radiation < 1D ? TextFormatting.YELLOW : radiation < 10D ? TextFormatting.GOLD : radiation < 100D ? TextFormatting.RED : TextFormatting.DARK_RED;
	}
	
	public static TextFormatting getFoodResistanceTextColor(double resistance) {
		return resistance < 0D ? TextFormatting.GRAY : TextFormatting.WHITE;
	}
	
	// Unit Prefixing
	
	public static String radsPrefix(double rads, boolean rate) {
		String unit = rate ? "Rads/t" : "Rads";
		return NCConfig.radiation_unit_prefixes > 0 ? NCMath.sigFigs(rads, NCConfig.radiation_unit_prefixes) + " " + unit : UnitHelper.prefix(rads, 3, unit, 0, -8);
	}
	
	// Rad Resistance Sig Figs
	
	public static String resistanceSigFigs(double resistance) {
		return NCMath.sigFigs(resistance, Math.max(2, NCConfig.radiation_unit_prefixes));
	}
}
