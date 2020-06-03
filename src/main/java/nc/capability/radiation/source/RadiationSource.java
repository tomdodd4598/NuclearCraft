package nc.capability.radiation.source;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

public class RadiationSource implements IRadiationSource {
	
	private double radiationLevel = 0D, radiationBuffer = 0D, scrubbingFraction = 0D, effectiveScrubberCount = 0D;
	
	public RadiationSource(double startRadiation) {
		radiationLevel = startRadiation;
	}
	
	@Override
	public NBTTagCompound writeNBT(IRadiationSource instance, EnumFacing side, NBTTagCompound nbt) {
		nbt.setDouble("radiationLevel", getRadiationLevel());
		nbt.setDouble("radiationBuffer", getRadiationBuffer());
		nbt.setDouble("scrubbingFraction", scrubbingFraction);
		nbt.setDouble("effectiveScrubberCount", effectiveScrubberCount);
		return nbt;
	}
	
	@Override
	public void readNBT(IRadiationSource instance, EnumFacing side, NBTTagCompound nbt) {
		setRadiationLevel(nbt.getDouble("radiationLevel"));
		setRadiationBuffer(nbt.getDouble("radiationBuffer"));
		scrubbingFraction = nbt.getDouble("scrubbingFraction");
		effectiveScrubberCount = nbt.getDouble("effectiveScrubberCount");
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
	
	@Override
	public double getScrubbingFraction() {
		return scrubbingFraction;
	}
	
	@Override
	public void setScrubbingFraction(double newFraction) {
		scrubbingFraction = MathHelper.clamp(newFraction, 0D, 1D);
	}
	
	@Override
	public double getEffectiveScrubberCount() {
		return effectiveScrubberCount;
	}
	
	@Override
	public void setEffectiveScrubberCount(double newScrubberCount) {
		effectiveScrubberCount = Math.max(0D, newScrubberCount);
	}
}
