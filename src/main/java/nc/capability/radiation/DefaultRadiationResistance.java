package nc.capability.radiation;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class DefaultRadiationResistance implements IDefaultRadiationResistance {
	
	private double radiationResistance = 0D;
	
	public DefaultRadiationResistance(double radiationResistance) {
		this.radiationResistance = radiationResistance;
	}

	@Override
	public NBTTagCompound writeNBT(IDefaultRadiationResistance instance, EnumFacing side, NBTTagCompound nbt) {
		return nbt;
	}

	@Override
	public void readNBT(IDefaultRadiationResistance instance, EnumFacing side, NBTTagCompound nbt) {}
	
	@Override
	public double getRadiationResistance() {
		return radiationResistance;
	}
}
