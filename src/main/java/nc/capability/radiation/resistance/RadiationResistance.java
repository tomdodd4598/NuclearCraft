package nc.capability.radiation.resistance;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class RadiationResistance implements IRadiationResistance {
	
	private double baseRadResistance = 0D, shieldingRadResistance = 0D;
	
	public RadiationResistance(double baseRadResistance) {
		this.baseRadResistance = baseRadResistance;
	}
	
	@Override
	public NBTTagCompound writeNBT(IRadiationResistance instance, EnumFacing side, NBTTagCompound nbt) {
		nbt.setDouble("radiationResistance", getBaseRadResistance());
		nbt.setDouble("shieldingRadResistance", getShieldingRadResistance());
		return nbt;
	}
	
	@Override
	public void readNBT(IRadiationResistance instance, EnumFacing side, NBTTagCompound nbt) {
		setBaseRadResistance(nbt.getDouble("radiationResistance"));
		setShieldingRadResistance(nbt.getDouble("shieldingRadResistance"));
	}
	
	@Override
	public double getBaseRadResistance() {
		return baseRadResistance;
	}
	
	@Override
	public void setBaseRadResistance(double newResistance) {
		baseRadResistance = Math.max(newResistance, 0D);
	}
	
	@Override
	public double getShieldingRadResistance() {
		return shieldingRadResistance;
	}
	
	@Override
	public void setShieldingRadResistance(double newResistance) {
		shieldingRadResistance = Math.max(newResistance, 0D);
	}
}
