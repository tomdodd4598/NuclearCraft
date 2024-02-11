package nc.tile.fission;

public interface IFissionSpecialComponent extends IFissionComponent {
	
	/** Called after all flux and cluster-searching is complete. */
	public void postClusterSearch();
}
