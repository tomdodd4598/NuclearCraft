package nc.capability.radiation.resistance;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class RadiationResistance implements IRadiationResistance {
	
	private double radiationResistance = 0D;
	
	public RadiationResistance(double radiationResistance) {
		this.radiationResistance = radiationResistance;
	}
	
	@Override
	public NBTTagCompound writeNBT(IRadiationResistance instance, EnumFacing side, NBTTagCompound nbt) {
		nbt.setDouble("radiationResistance", getRadiationResistance());
		return nbt;
	}
	
	@Override
	public void readNBT(IRadiationResistance instance, EnumFacing side, NBTTagCompound nbt) {
		setRadiationResistance(nbt.getDouble("radiationResistance"));
	}
	
	@Override
	public double getRadiationResistance() {
		return radiationResistance;
	}
	
	@Override
	public void setRadiationResistance(double newResistance) {
		radiationResistance = Math.max(newResistance, 0D);
	}
}
