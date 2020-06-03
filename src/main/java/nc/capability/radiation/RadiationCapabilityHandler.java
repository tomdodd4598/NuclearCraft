package nc.capability.radiation;

import nc.capability.radiation.entity.*;
import nc.capability.radiation.resistance.*;
import nc.capability.radiation.sink.*;
import nc.capability.radiation.source.*;
import nc.init.NCItems;
import nc.radiation.*;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RadiationCapabilityHandler {
	
	@SubscribeEvent
	public void attachEntityRadiationCapability(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EntityPlayer) {
			event.addCapability(IEntityRads.CAPABILITY_ENTITY_RADS_NAME, new EntityRadsProvider());
		}
		else if (event.getObject() instanceof EntityLivingBase) {
			event.addCapability(IEntityRads.CAPABILITY_ENTITY_RADS_NAME, new EntityRadsProvider((EntityLivingBase) event.getObject()));
		}
	}
	
	@SubscribeEvent
	public void attachChunkRadiationCapability(AttachCapabilitiesEvent<Chunk> event) {
		event.addCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE_NAME, new RadiationSourceProvider(0D));
	}
	
	@SubscribeEvent
	public void attachTileRadiationCapability(AttachCapabilitiesEvent<TileEntity> event) {
		event.addCapability(IRadiationResistance.CAPABILITY_RADIATION_RESISTANCE_NAME, new RadiationResistanceProvider(0D));
	}
	
	@SubscribeEvent
	public void attachStackRadiationCapability(AttachCapabilitiesEvent<ItemStack> event) {
		ItemStack stack = event.getObject();
		
		if (stack.getItem() == NCItems.radiation_badge) {
			event.addCapability(IRadiationSink.CAPABILITY_RADIATION_SINK_NAME, new RadiationSinkProvider(0D));
		}
		
		int packed = RecipeItemHelper.pack(stack);
		if (RadSources.STACK_MAP.containsKey(packed)) {
			event.addCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE_NAME, new RadiationSourceStackProvider(stack));
		}
		if (RadArmor.ARMOR_RAD_RESISTANCE_MAP.containsKey(packed)) {
			event.addCapability(IRadiationResistance.CAPABILITY_RADIATION_RESISTANCE_NAME, new RadiationResistanceStackProvider(stack));
		}
	}
}
