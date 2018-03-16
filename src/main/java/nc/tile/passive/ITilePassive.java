package nc.tile.passive;

import nc.tile.fluid.tank.Tank;
import nc.tile.fluid.tank.EnumTank.FluidConnection;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;

public interface ITilePassive {
	public int getEnergyChange();
	public int getItemChange();
	public int getFluidChange();
	
	public boolean canPushEnergyTo();
	public boolean canPushItemsTo();
	public boolean canPushFluidsTo();
}
