package nc.handler;

import nc.capability.ICapability;
import nc.capability.radiation.entity.IEntityRads;
import nc.capability.radiation.entity.PlayerRads;
import nc.capability.radiation.resistance.IRadiationResistance;
import nc.capability.radiation.resistance.RadiationResistance;
import nc.capability.radiation.sink.IRadiationSink;
import nc.capability.radiation.sink.RadiationSink;
import nc.capability.radiation.source.IRadiationSource;
import nc.capability.radiation.source.RadiationSource;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityHandler {
	
	public static void init() {
		registerCapability(IEntityRads.class, new PlayerRads());
		registerCapability(IRadiationSource.class, new RadiationSource(0D));
		registerCapability(IRadiationResistance.class, new RadiationResistance(0D));
		registerCapability(IRadiationSink.class, new RadiationSink(0D));
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
