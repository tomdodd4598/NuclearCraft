package nc.tile.passive;

import nc.tile.energy.ITileEnergy;
import nc.tile.fluid.ITileFluid;
import nc.tile.inventory.ITileInventory;

public interface ITilePassive extends ITileEnergy, ITileInventory, ITileFluid {
	
	public int getEnergyChange();
	
	public int getItemChange();
	
	public int getFluidChange();
	
	public boolean canPushEnergyTo();
	
	public boolean canPushItemsTo();
	
	public boolean canPushFluidsTo();
}
