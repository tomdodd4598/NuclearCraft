package nc.multiblock.fission.tile;

import net.minecraft.util.EnumFacing;

public interface IFissionFluxSink extends IFissionComponent {
	
	/** True if neutron flux can be used by and should affect this part. */
	public boolean isAcceptingFlux(EnumFacing side);
	
	public default boolean canSupportActiveModerator(boolean activeModeratorPos) {
		return false;
	}
	
	/** Additional multiplier for the actively searching fuel component's moderator line efficiency. */
	public double moderatorLineEfficiencyFactor();
	
	public long getFlux();
	
	public void addFlux(long addedFlux);
	
	public void refreshIsProcessing(boolean checkCluster);
}
