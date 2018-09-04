package nc.util;

import nc.capability.radiation.IEntityRads;
import nc.capability.radiation.IRadiation;
import nc.capability.radiation.IRadiationSource;
import nc.config.NCConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class RadiationHelper {
	
	public static void addToChunkBuffer(IRadiationSource chunkRadiation, double addedRadiation) {
		chunkRadiation.setRadiationBuffer(chunkRadiation.getRadiationBuffer() + addedRadiation);
	}
	
	// Block -> Chunk
	
	public static void transferRadiationFromBlockToChunk(Chunk chunk, double addedRadiation) {
		if (chunk == null || !chunk.hasCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null)) return;
		IRadiationSource chunkRadiation = chunk.getCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null);
		chunkRadiation.setRadiationLevel(chunkRadiation.getRadiationLevel() + addedRadiation);
	}
	
	// ItemStack -> ChunkBuffer
	
	public static void transferRadiationFromStackToChunkBuffer(ItemStack stack, Chunk chunk) {
		if (chunk == null || !stack.hasCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null) || !chunk.hasCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null)) return;
		IRadiationSource stackRadiation = stack.getCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null);
		IRadiationSource chunkRadiation = chunk.getCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null);
		addToChunkBuffer(chunkRadiation, stackRadiation.getRadiationLevel()*stack.getCount());
	}
	
	// Source -> ChunkBuffer
	
	public static void transferRadiationFromSourceToChunkBuffer(ICapabilityProvider provider, Chunk chunk) {
		if (chunk == null || !provider.hasCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null) || !chunk.hasCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null)) return;
		IRadiationSource sourceRadiation = provider.getCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null);
		IRadiationSource chunkRadiation = chunk.getCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null);
		addToChunkBuffer(chunkRadiation, sourceRadiation.getRadiationLevel());
	}
	
	// Inventory -> ChunkBuffer
	
	public static void transferRadsFromInventoryToChunkBuffer(InventoryPlayer inventory, Chunk chunk) {
		if (!NCConfig.radiation_hardcore_stacks) return;
		for (ItemStack stack : inventory.mainInventory) {
			if (!stack.isEmpty()) transferRadiationFromSourceToChunkBuffer(stack, chunk);
		}
		for (ItemStack stack : inventory.armorInventory) {
			if (!stack.isEmpty()) transferRadiationFromSourceToChunkBuffer(stack, chunk);
		}
		for (ItemStack stack : inventory.offHandInventory) {
			if (!stack.isEmpty()) transferRadiationFromSourceToChunkBuffer(stack, chunk);
		}
	}
	
	// Chunk Set Previous Radiation and Spread
	
	public static void spreadRadiationFromChunk(Chunk sourceChunk, Chunk targetChunk) {
		if (sourceChunk == null || !sourceChunk.hasCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null)) return;
		IRadiationSource sourceChunkRadiation = sourceChunk.getCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null);
		
		if (targetChunk != null && targetChunk.hasCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null)) {
			IRadiationSource targetChunkRadiation = targetChunk.getCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null);
			
			if (!sourceChunkRadiation.isRadiationNegligible()) {
				if (targetChunkRadiation.getRadiationLevel() == 0D || sourceChunkRadiation.getRadiationLevel()/targetChunkRadiation.getRadiationLevel() > 1.5D) {
					double radiationSpread = (sourceChunkRadiation.getRadiationLevel() - targetChunkRadiation.getRadiationLevel())*NCConfig.radiation_spread_rate;
					sourceChunkRadiation.setRadiationLevel(sourceChunkRadiation.getRadiationLevel() - radiationSpread);
					targetChunkRadiation.setRadiationLevel(targetChunkRadiation.getRadiationLevel() + radiationSpread);
				}
			}
		}
		
		if (sourceChunkRadiation.isRadiationNegligible()) sourceChunkRadiation.setRadiationLevel(0D);
	}
	
	// Player Radiation Resistance
	
	public static double addRadsToPlayer(EntityPlayer player, IEntityRads playerRads, double rawRadiation, int updateRate) {
		double resistance = playerRads.getRadiationResistance();
		for (ItemStack armor : player.inventory.armorInventory) {
			if (armor.hasTagCompound()) if (armor.getTagCompound().hasKey("ncRadiationResistance")) {
				resistance += armor.getTagCompound().getDouble("ncRadiationResistance");
			}
		}
		double addedRadiation = rawRadiation <= 0D ? 0D : NCMath.square(rawRadiation)/(rawRadiation + resistance);
		playerRads.setTotalRads(playerRads.getTotalRads() + addedRadiation*updateRate);
		return addedRadiation;
	}
	
	public static ItemStack armorWithRadResistance(Item armor, double resistance) {
		ItemStack stack = new ItemStack(armor);
		if (!(armor instanceof ItemArmor)) return stack;
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setDouble("ncRadiationResistance", resistance);
		return stack;
	}
	
	// Entity Radiation Resistance
	
	public static double addRadsToEntity(IEntityRads entityRads, double rawRadiation, int updateRate) {
		double addedRadiation = rawRadiation <= 0D ? 0D : NCMath.square(rawRadiation)/(rawRadiation + entityRads.getRadiationResistance());
		entityRads.setTotalRads(entityRads.getTotalRads() + addedRadiation*updateRate);
		return addedRadiation;
	}
	
	// Inventory -> Player
	
	public static double transferRadsFromInventoryToPlayer(EntityPlayer player, IEntityRads playerRads, Chunk chunk, int updateRate) {
		double radiationLevel = 0D;
		InventoryPlayer inventory = player.inventory;
		for (ItemStack stack : inventory.mainInventory) {
			radiationLevel += transferRadsFromStackToPlayer(stack, player, playerRads, updateRate);
		}
		for (ItemStack stack : inventory.armorInventory) {
			radiationLevel += transferRadsFromStackToPlayer(stack, player, playerRads, updateRate);
		}
		for (ItemStack stack : inventory.offHandInventory) {
			radiationLevel += transferRadsFromStackToPlayer(stack, player, playerRads, updateRate);
		}
		return radiationLevel;
	}
	
	private static double transferRadsFromStackToPlayer(ItemStack stack, EntityPlayer player, IEntityRads playerRads, int updateRate) {
		if (stack.isEmpty() || !stack.hasCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null)) return 0D;
		double stackRadiation = stack.getCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null).getRadiationLevel()*stack.getCount();
		return addRadsToPlayer(player, playerRads, stackRadiation, updateRate);
	}
	
	// Source -> Player
	
	public static double transferRadsToPlayer(ICapabilityProvider provider, EntityPlayer player, IEntityRads playerRads, int updateRate) {
		if (provider == null || !provider.hasCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null)) return 0D;
		IRadiationSource sourceRadiation = provider.getCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null);
		return addRadsToPlayer(player, playerRads, sourceRadiation.getRadiationLevel(), updateRate);
	}
	
	// Source -> Entity
	
	public static void transferRadsFromSourceToEntity(ICapabilityProvider provider, Entity entity, int updateRate) {
		if (entity == null || provider == null || !entity.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null) || !provider.hasCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null)) return;
		IEntityRads entityRads = entity.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
		IRadiationSource sourceRadiation = provider.getCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null);
		entityRads.setRadiationLevel(addRadsToEntity(entityRads, sourceRadiation.getRadiationLevel(), updateRate));
	}
	
	// Entity Symptoms
	
	public static void applySymptoms(EntityLivingBase entity, IEntityRads entityRads, int updateRate) {
		int radPercentage = entityRads.getRadsPercentage();
		if (radPercentage < 40) return;
		else if (radPercentage < 55) {
			entity.addPotionEffect(PotionHelper.newEffect(18, 1, updateRate + 1));
		}
		else if (radPercentage < 70) {
			entity.addPotionEffect(PotionHelper.newEffect(18, 1, updateRate + 1));
			entity.addPotionEffect(PotionHelper.newEffect(4, 1, updateRate + 1));
		}
		else if (radPercentage < 80) {
			entity.addPotionEffect(PotionHelper.newEffect(18, 2, updateRate + 1));
			entity.addPotionEffect(PotionHelper.newEffect(4, 1, updateRate + 1));
			entity.addPotionEffect(PotionHelper.newEffect(17, 1, updateRate + 1));
		}
		else if (radPercentage < 90) {
			entity.addPotionEffect(PotionHelper.newEffect(18, 2, updateRate + 1));
			entity.addPotionEffect(PotionHelper.newEffect(4, 2, updateRate + 1));
			entity.addPotionEffect(PotionHelper.newEffect(17, 1, updateRate + 1));
			entity.addPotionEffect(PotionHelper.newEffect(19, 1, updateRate + 1));
		}
		else {
			entity.addPotionEffect(PotionHelper.newEffect(18, 3, updateRate + 1));
			entity.addPotionEffect(PotionHelper.newEffect(4, 3, updateRate + 1));
			entity.addPotionEffect(PotionHelper.newEffect(17, 2, updateRate + 1));
			entity.addPotionEffect(PotionHelper.newEffect(19, 1, updateRate + 1));
			entity.addPotionEffect(PotionHelper.newEffect(20, 1, updateRate + 1));
		}
	}
	
	public static void applyMobBuffs(EntityLiving entity, IEntityRads entityRads, int updateRate) {
		int radPercentage = entityRads.getRadsPercentage();
		if (radPercentage < 40) return;
		else if (radPercentage < 55) {
			entity.addPotionEffect(PotionHelper.newEffect(1, 1, updateRate + 1));
		}
		else if (radPercentage < 70) {
			entity.addPotionEffect(PotionHelper.newEffect(1, 1, updateRate + 1));
			entity.addPotionEffect(PotionHelper.newEffect(11, 1, updateRate + 1));
		}
		else if (radPercentage < 80) {
			entity.addPotionEffect(PotionHelper.newEffect(1, 2, updateRate + 1));
			entity.addPotionEffect(PotionHelper.newEffect(11, 1, updateRate + 1));
			entity.addPotionEffect(PotionHelper.newEffect(8, 1, updateRate + 1));
		}
		else if (radPercentage < 90) {
			entity.addPotionEffect(PotionHelper.newEffect(1, 2, updateRate + 1));
			entity.addPotionEffect(PotionHelper.newEffect(11, 2, updateRate + 1));
			entity.addPotionEffect(PotionHelper.newEffect(8, 1, updateRate + 1));
			entity.addPotionEffect(PotionHelper.newEffect(22, 1, updateRate + 1));
		}
		else {
			entity.addPotionEffect(PotionHelper.newEffect(1, 3, updateRate + 1));
			entity.addPotionEffect(PotionHelper.newEffect(11, 3, updateRate + 1));
			entity.addPotionEffect(PotionHelper.newEffect(8, 2, updateRate + 1));
			entity.addPotionEffect(PotionHelper.newEffect(22, 1, updateRate + 1));
			entity.addPotionEffect(PotionHelper.newEffect(10, 1, updateRate + 1));
		}
	}
	
	// Text Colors
	
	public static TextFormatting getRadsTextColor(IEntityRads playerRads) {
		int radsPercent = playerRads.getRadsPercentage();
		return radsPercent < 10 ? TextFormatting.WHITE : (radsPercent < 40 ? TextFormatting.YELLOW : (radsPercent < 70 ? TextFormatting.RED : TextFormatting.DARK_RED));
	}
	
	public static TextFormatting getRadiationTextColor(IRadiation irradiated) {
		if (irradiated.isRadiationNegligible()) return TextFormatting.WHITE;
		double radiation = irradiated.getRadiationLevel();
		return radiation < 0.001D ? TextFormatting.YELLOW : (radiation < 1D ? TextFormatting.RED : TextFormatting.DARK_RED);
	}
}
