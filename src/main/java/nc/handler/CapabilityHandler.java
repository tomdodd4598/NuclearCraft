package nc.handler;

import java.util.Map.Entry;

import nc.capability.ICapability;
import nc.capability.radiation.EntityRads;
import nc.capability.radiation.EntityRadsProvider;
import nc.capability.radiation.IEntityRads;
import nc.capability.radiation.IRadiationSource;
import nc.capability.radiation.RadiationSource;
import nc.capability.radiation.RadiationSourceProvider;
import nc.config.NCConfig;
import nc.radiation.RadSources;
import nc.radiation.RadWorlds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

public class CapabilityHandler {
	
	public static void init() {
		registerCapability(IEntityRads.class, new EntityRads(NCConfig.max_player_rads));
		registerCapability(IRadiationSource.class, new RadiationSource());
	}
	
	public static <T extends ICapability> void registerCapability(Class<T> clazz, T defaultImpl) {
		CapabilityManager.INSTANCE.register(clazz, new IStorage<T>() {
			@Override
			public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side) {
				NBTTagCompound nbt = new NBTTagCompound();
				return instance.writeNBT(instance, side, nbt);
			}
			
			@Override
			public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt) {
				if (nbt instanceof NBTTagCompound) instance.readNBT(instance, side, (NBTTagCompound)nbt);
			}
		},
		() -> defaultImpl);
	}
	
	/* ================================== Capability Attaching ===================================== */
	
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
	
	/*@SubscribeEvent
	public void attachTileRadiationCapability(AttachCapabilitiesEvent<TileEntity> event) {
		if (event.getObject() instanceof IRadiationSource) {
			event.addCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE_NAME, new RadiationSourceProvider());
		}
	}*/
	
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
}
