package nc.capability.radiation.entity;

import javax.annotation.*;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.*;

public class EntityRadsProvider implements ICapabilitySerializable<NBTBase> {
	
	private final IEntityRads entityRads;
	
	public EntityRadsProvider() {
		entityRads = new PlayerRads();
	}
	
	public EntityRadsProvider(EntityLivingBase entity) {
		entityRads = new EntityRads(entity);
	}
	
	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == IEntityRads.CAPABILITY_ENTITY_RADS;
	}
	
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == IEntityRads.CAPABILITY_ENTITY_RADS) {
			return IEntityRads.CAPABILITY_ENTITY_RADS.cast(entityRads);
		}
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
