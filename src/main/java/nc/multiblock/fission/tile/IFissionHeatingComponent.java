package nc.multiblock.fission.tile;

public interface IFissionHeatingComponent extends IFissionComponent {
	
	public long getRawHeating();
	
	public long getRawHeatingIgnoreCoolingPenalty();
	
	public double getEffectiveHeating();
	
	public double getEffectiveHeatingIgnoreCoolingPenalty();
}
