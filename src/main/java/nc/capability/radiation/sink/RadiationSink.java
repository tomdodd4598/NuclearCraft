package nc.capability.radiation.sink;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class RadiationSink implements IRadiationSink {
	
	private double radiationLevel = 0D;
	private double radiationBuffer = 0D;
	
	public RadiationSink(double startRadiation) {
		this.radiationLevel = startRadiation;
	}

	@Override
	public NBTTagCompound writeNBT(IRadiationSink instance, EnumFacing side, NBTTagCompound nbt) {
		nbt.setDouble("radiationLevel", getRadiationLevel());
		nbt.setDouble("radiationBuffer", getRadiationBuffer());
		return nbt;
	}

	@Override
	public void readNBT(IRadiationSink instance, EnumFacing side, NBTTagCompound nbt) {
		setRadiationLevel(nbt.getDouble("radiationLevel"));
		setRadiationBuffer(nbt.getDouble("radiationBuffer"));
	}

	@Override
	public double getRadiationLevel() {
		return radiationLevel;
	}

	@Override
	public void setRadiationLevel(double newRads) {
		radiationLevel = Math.max(newRads, 0D);
	}
	
	@Override
	public double getRadiationBuffer() {
		return radiationBuffer;
	}
	
	@Override
	public void setRadiationBuffer(double newBuffer) {
		radiationBuffer = newBuffer;
	}
}
