package nc.tile.fission;

public interface IFissionHeatingComponent extends IFissionComponent {
	
	public long getRawHeating();
	
	public long getRawHeatingIgnoreCoolingPenalty();
	
	public double getEffectiveHeating();
	
	public double getEffectiveHeatingIgnoreCoolingPenalty();
}
