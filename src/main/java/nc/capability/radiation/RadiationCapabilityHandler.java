package nc.capability.radiation;

import nc.capability.radiation.entity.EntityRadsProvider;
import nc.capability.radiation.entity.IEntityRads;
import nc.capability.radiation.resistance.IRadiationResistance;
import nc.capability.radiation.resistance.RadiationResistanceProvider;
import nc.capability.radiation.resistance.RadiationResistanceStackProvider;
import nc.capability.radiation.sink.IRadiationSink;
import nc.capability.radiation.sink.RadiationSinkProvider;
import nc.capability.radiation.source.IRadiationSource;
import nc.capability.radiation.source.RadiationSourceProvider;
import nc.capability.radiation.source.RadiationSourceStackProvider;
import nc.config.NCConfig;
import nc.init.NCItems;
import nc.radiation.RadWorlds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RadiationCapabilityHandler {
	
	@SubscribeEvent
	public void attachEntityRadiationCapability(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EntityPlayer) {
			event.addCapability(IEntityRads.CAPABILITY_ENTITY_RADS_NAME, new EntityRadsProvider(NCConfig.max_player_rads));
		}
		else if (event.getObject() instanceof EntityLivingBase) {
			event.addCapability(IEntityRads.CAPABILITY_ENTITY_RADS_NAME, new EntityRadsProvider(50D*((EntityLivingBase)event.getObject()).getHealth()));
		}
	}
	
	@SubscribeEvent
	public void attachChunkRadiationCapability(AttachCapabilitiesEvent<Chunk> event) {
		event.addCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE_NAME, new RadiationSourceProvider(0D));
	}
	
	@SubscribeEvent
	public void attachWorldRadiationCapability(AttachCapabilitiesEvent<World> event) {
		int dim = event.getObject().provider.getDimension();
		if (RadWorlds.RAD_MAP.containsKey(dim)) {
			event.addCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE_NAME, new RadiationSourceProvider(RadWorlds.RAD_MAP.get(dim)));
			return;
		}
	}
	
	@SubscribeEvent
	public void attachTileRadiationCapability(AttachCapabilitiesEvent<TileEntity> event) {
		event.addCapability(IRadiationResistance.CAPABILITY_RADIATION_RESISTANCE_NAME, new RadiationResistanceProvider(0D));
	}
	
	@SubscribeEvent
	public void attachStackRadiationCapability(AttachCapabilitiesEvent<ItemStack> event) {
		ItemStack stack = event.getObject();
		if (stack.isEmpty()) return;
		/*ItemInfo itemInfo = new ItemInfo(stack);
		if (RadSources.STACK_MAP.containsKey(itemInfo)) {
			event.addCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE_NAME, new RadiationSourceProvider(RadSources.STACK_MAP.get(itemInfo)));
			return;
		}
		for (String oreName : OreDictHelper.getOreNames(stack)) {
			if (RadSources.ORE_MAP.containsKey(oreName)) {
				event.addCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE_NAME, new RadiationSourceProvider(RadSources.ORE_MAP.get(oreName)));
				return;
			}
		}*/
		event.addCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE_NAME, new RadiationSourceStackProvider(stack));
		
		if (stack.getItem() == NCItems.radiation_badge) event.addCapability(IRadiationSink.CAPABILITY_RADIATION_SINK_NAME, new RadiationSinkProvider(0D));
		
		/*ItemInfo itemInfo = new ItemInfo(stack);
		if (RadiationArmor.ARMOR_RAD_RESISTANCE_MAP.containsKey(itemInfo)) {
			event.addCapability(IRadiationResistance.CAPABILITY_RADIATION_RESISTANCE_NAME, new RadiationResistanceProvider(RadiationArmor.ARMOR_RAD_RESISTANCE_MAP.get(itemInfo)));
			return;
		}*/
		event.addCapability(IRadiationResistance.CAPABILITY_RADIATION_RESISTANCE_NAME, new RadiationResistanceStackProvider(stack));
	}
}
