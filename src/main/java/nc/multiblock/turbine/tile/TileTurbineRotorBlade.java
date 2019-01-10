package nc.multiblock.turbine.tile;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.turbine.Turbine;
import nc.multiblock.turbine.TurbineRotorBladeType;

public class TileTurbineRotorBlade extends TileTurbinePartBase {
	
	public final double efficiency;
	
	public static class Steel extends TileTurbineRotorBlade {
		
		public Steel() {
			super(TurbineRotorBladeType.STEEL);
		}
	}
	
	public static class Extreme extends TileTurbineRotorBlade {
		
		public Extreme() {
			super(TurbineRotorBladeType.EXTREME);
		}
	}
	
	public static class SicSicCMC extends TileTurbineRotorBlade {
		
		public SicSicCMC() {
			super(TurbineRotorBladeType.SIC_SIC_CMC);
		}
	}
	
	private TileTurbineRotorBlade(TurbineRotorBladeType bladeType) {
		super(CuboidalPartPositionType.INTERIOR);
		
		efficiency = bladeType.getEfficiency();
	}
	
	@Override
	public void onMachineAssembled(Turbine controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		if (getWorld().isRemote) return;
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		if (getWorld().isRemote) return;
		//getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()), 2);
	}

}
