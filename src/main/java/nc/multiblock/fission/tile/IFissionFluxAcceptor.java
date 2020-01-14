package nc.multiblock.fission.tile;

import net.minecraft.util.EnumFacing;

public interface IFissionFluxAcceptor extends IFissionComponent {
	
	/** True if neutron flux can be used by and should affect this part. */
	public boolean canAcceptFlux(EnumFacing side);
	
	public boolean contributeEfficiency();
	
	public void addFlux(int flux);
	
	public void refreshIsProcessing(boolean checkCluster);
}
