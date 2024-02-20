package nc.tile.fission;

public interface IFissionHeatingComponent extends IFissionComponent {
	
	long getRawHeating();
	
	long getRawHeatingIgnoreCoolingPenalty();
	
	double getEffectiveHeating();
	
	double getEffectiveHeatingIgnoreCoolingPenalty();
}
