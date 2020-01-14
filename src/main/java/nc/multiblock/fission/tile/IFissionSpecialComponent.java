package nc.multiblock.fission.tile;

public interface IFissionSpecialComponent extends IFissionComponent {
	
	/** Called after all flux and cluster-searching is complete. */
	public void postClusterSearch();
}
