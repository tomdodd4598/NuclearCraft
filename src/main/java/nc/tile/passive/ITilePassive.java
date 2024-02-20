package nc.tile.passive;

import nc.tile.energy.ITileEnergy;
import nc.tile.fluid.ITileFluid;
import nc.tile.inventory.ITileInventory;
import net.minecraft.util.ITickable;

public interface ITilePassive extends ITickable, ITileEnergy, ITileInventory, ITileFluid {
	
	double getEnergyRate();
	
	double getItemRate();
	
	double getFluidRate();
	
	boolean canPushEnergyTo();
	
	boolean canPushItemsTo();
	
	boolean canPushFluidsTo();
}
