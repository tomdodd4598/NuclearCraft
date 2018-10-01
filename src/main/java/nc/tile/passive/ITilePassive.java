package nc.tile.passive;

public interface ITilePassive {
	
	public int getEnergyChange();
	public int getItemChange();
	public int getFluidChange();
	
	public boolean canPushEnergyTo();
	public boolean canPushItemsTo();
	public boolean canPushFluidsTo();
}
