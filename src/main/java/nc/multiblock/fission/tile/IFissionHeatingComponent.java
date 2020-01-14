package nc.multiblock.fission.tile;

public interface IFissionHeatingComponent extends IFissionComponent {
	
	public long getRawHeating();
	
	public double getEffectiveHeating();
}
