package nc.enumm;

public interface IFissionFuelEnum extends IMetaEnum {
	
	public int getBaseTime();
		
	public int getBaseHeat();
	
	public double getBaseEfficiency();
	
	public int getCriticality();
	
	public boolean getSelfPriming();
}
