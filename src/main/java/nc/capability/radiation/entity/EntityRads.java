package nc.capability.radiation.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class EntityRads extends PlayerRads {
	
	private final EntityLivingBase entity;
	private boolean setFromMaxHealth = false;
	
	public EntityRads(EntityLivingBase entity) {
		super();
		this.entity = entity;
	}
	
	@Override
	public NBTTagCompound writeNBT(IEntityRads instance, EnumFacing side, NBTTagCompound nbt) {
		nbt.setDouble("maxRads", getMaxRads());
		nbt.setBoolean("setFromMaxHealth", setFromMaxHealth);
		return super.writeNBT(instance, side, nbt);
	}
	
	@Override
	public void readNBT(IEntityRads instance, EnumFacing side, NBTTagCompound nbt) {
		if (nbt.getDouble("maxRads") > 0) maxRads = nbt.getDouble("maxRads");
		setFromMaxHealth = nbt.getBoolean("setFromMaxHealth");
		super.readNBT(instance, side, nbt);
	}
	
	@Override
	public double getMaxRads() {
		if (!setFromMaxHealth) {
			maxRads = 50D*entity.getMaxHealth();
			setFromMaxHealth = true;
		}
		return maxRads;
	}
}
