package nc.capability.radiation;

import java.util.Map.Entry;

import nc.config.NCConfig;
import nc.radiation.RadSources;
import nc.radiation.RadWorlds;
import nc.radiation.RadiationArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

public class RadiationCapabilityHandler {
	
	@SubscribeEvent
	public void attachEntityRadiationCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EntityPlayer) {
			event.addCapability(IEntityRads.CAPABILITY_ENTITY_RADS_NAME, new EntityRadsProvider(NCConfig.max_player_rads));
		}
		else if (event.getObject() instanceof EntityLivingBase) {
			event.addCapability(IEntityRads.CAPABILITY_ENTITY_RADS_NAME, new EntityRadsProvider(50D*((EntityLivingBase)event.getObject()).getHealth()));
		}
	}
	
	@SubscribeEvent
	public void attachChunkRadiationCapability(AttachCapabilitiesEvent<Chunk> event) {
		event.addCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE_NAME, new RadiationSourceProvider());
	}
	
	@SubscribeEvent
	public void attachWorldRadiationCapability(AttachCapabilitiesEvent<World> event) {
		int dim = event.getObject().provider.getDimension();
		if (RadWorlds.BACKGROUND_MAP.containsKey(dim)) {
			event.addCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE_NAME, new RadiationSourceProvider(RadWorlds.BACKGROUND_MAP.get(dim)));
			return;
		}
	}
	
	@SubscribeEvent
	public void attachStackRadiationCapability(AttachCapabilitiesEvent<ItemStack> event) {
		ItemStack stack = event.getObject();
		if (stack.isEmpty()) return;
		for (Entry<ItemStack, Double> entry : RadSources.STACK_MAP.entrySet()) {
			if (stack.isItemEqual(entry.getKey())) {
				event.addCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE_NAME, new RadiationSourceProvider(entry.getValue()));
				return;
			}
		}
		for (int oreID : OreDictionary.getOreIDs(stack)) {
			String oreName = OreDictionary.getOreName(oreID);
			if (RadSources.ORE_MAP.containsKey(oreName)) {
				event.addCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE_NAME, new RadiationSourceProvider(RadSources.ORE_MAP.get(oreName)));
				return;
			}
		}
	}
	
	@SubscribeEvent
	public void attachArmorDefaultRadiationResistanceCapability(AttachCapabilitiesEvent<ItemStack> event) {
		ItemStack stack = event.getObject();
		if (stack.isEmpty() || !(stack.getItem() instanceof ItemArmor)) return;
		for (Entry<ItemStack, Double> entry : RadiationArmor.ARMOR_STACK_RESISTANCE_MAP.entrySet()) {
			if (stack.isItemEqual(entry.getKey())) {
				event.addCapability(IDefaultRadiationResistance.CAPABILITY_DEFAULT_RADIATION_RESISTANCE_NAME, new DefaultRadiationResistanceProvider(entry.getValue()));
				return;
			}
		}
	}
}
