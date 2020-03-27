package nc.multiblock.fission.tile;

import net.minecraft.util.EnumFacing;

public interface IFissionFluxSink extends IFissionComponent {
	
	/** True if neutron flux can be used by and should affect this part. */
	public boolean isAcceptingFlux(EnumFacing side);
	
	public boolean isNullifyingSources(EnumFacing side);
	
	/** Additional multiplier for the actively searching fuel component's moderator line efficiency. */
	public double moderatorLineEfficiencyFactor();
	
	public void addFlux(int flux);
	
	public void refreshIsProcessing(boolean checkCluster);
}
