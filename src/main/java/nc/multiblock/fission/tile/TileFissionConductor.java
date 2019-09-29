package nc.multiblock.fission.tile;

import javax.annotation.Nullable;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.FissionCluster;
import nc.multiblock.fission.FissionReactor;

public class TileFissionConductor extends TileFissionPartBase implements IFissionComponent {
	
	private FissionCluster cluster = null;
	
	public TileFissionConductor() {
		super(CuboidalPartPositionType.INTERIOR);
	}
	
	@Override
	public void onMachineAssembled(FissionReactor controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		//if (getWorld().isRemote) return;
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		//if (getWorld().isRemote) return;
		//getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()), 2);
	}
	
	// IFissionComponent
	
	@Override
	public @Nullable FissionCluster getCluster() {
		return cluster;
	}
	
	@Override
	public void setCluster(@Nullable FissionCluster cluster) {
		if (cluster == null && this.cluster !=  null) {
			this.cluster.getComponentMap().remove(pos.toLong());
		}
		else if (cluster != null) {
			cluster.getComponentMap().put(pos.toLong(), this);
		}
		this.cluster = cluster;
	}
	
	@Override
	public boolean isValidHeatConductor() {
		return true;
	}
	
	@Override
	public boolean isFunctional() {
		return false;
	}
	
	@Override
	public void resetStats() {}
}
