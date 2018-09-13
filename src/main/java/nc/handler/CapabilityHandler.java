package nc.handler;

import nc.capability.ICapability;
import nc.capability.radiation.DefaultRadiationResistance;
import nc.capability.radiation.EntityRads;
import nc.capability.radiation.IDefaultRadiationResistance;
import nc.capability.radiation.IEntityRads;
import nc.capability.radiation.IRadiationSource;
import nc.capability.radiation.RadiationSource;
import nc.config.NCConfig;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityHandler {
	
	public static void init() {
		registerCapability(IEntityRads.class, new EntityRads(NCConfig.max_player_rads));
		registerCapability(IRadiationSource.class, new RadiationSource());
		registerCapability(IDefaultRadiationResistance.class, new DefaultRadiationResistance(0D));
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
}
