package nc.capability.radiation;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class EntityRadsProvider implements ICapabilitySerializable {
	
	private final IEntityRads entityRads;
	
	public EntityRadsProvider(double maxRads) {
		entityRads = new EntityRads(maxRads);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == IEntityRads.CAPABILITY_ENTITY_RADS;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == IEntityRads.CAPABILITY_ENTITY_RADS) return IEntityRads.CAPABILITY_ENTITY_RADS.cast(entityRads);
		return null;
	}

	@Override
	public NBTBase serializeNBT() {
		return IEntityRads.CAPABILITY_ENTITY_RADS.writeNBT(entityRads, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		IEntityRads.CAPABILITY_ENTITY_RADS.readNBT(entityRads, null, nbt);
	}
}
