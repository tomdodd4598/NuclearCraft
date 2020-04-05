package nc.capability.radiation.entity;

import static nc.radiation.RadEntities.MAX_RADS_MAP;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class EntityRads extends PlayerRads {
	
	private final EntityLivingBase entity;
	private boolean setMaxRads = false;
	
	public EntityRads(EntityLivingBase entity) {
		super();
		this.entity = entity;
	}
	
	@Override
	public NBTTagCompound writeNBT(IEntityRads instance, EnumFacing side, NBTTagCompound nbt) {
		nbt.setDouble("maxRads", getMaxRads());
		nbt.setBoolean("setMaxRads", setMaxRads);
		return super.writeNBT(instance, side, nbt);
	}
	
	@Override
	public void readNBT(IEntityRads instance, EnumFacing side, NBTTagCompound nbt) {
		if (nbt.getDouble("maxRads") > 0) maxRads = nbt.getDouble("maxRads");
		setMaxRads = nbt.getBoolean("setMaxRads");
		super.readNBT(instance, side, nbt);
	}
	
	@Override
	public double getMaxRads() {
		if (!setMaxRads) {
			maxRads = MAX_RADS_MAP.containsKey(entity.getClass()) ? MAX_RADS_MAP.get(entity.getClass()) : 50D*entity.getMaxHealth();
			setMaxRads = true;
		}
		return maxRads;
	}
}
