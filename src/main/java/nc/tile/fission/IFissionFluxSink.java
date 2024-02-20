package nc.tile.fission;

import net.minecraft.util.EnumFacing;

public interface IFissionFluxSink extends IFissionComponent {
	
	/** True if neutron flux can be used by and should affect this part. */
    boolean isAcceptingFlux(EnumFacing side);
	
	default boolean canSupportActiveModerator(boolean activeModeratorPos) {
		return false;
	}
	
	/** Additional multiplier for the actively searching fuel component's moderator line efficiency. */
    double moderatorLineEfficiencyFactor();
	
	long getFlux();
	
	void addFlux(long addedFlux);
	
	void refreshIsProcessing(boolean checkCluster);
}
