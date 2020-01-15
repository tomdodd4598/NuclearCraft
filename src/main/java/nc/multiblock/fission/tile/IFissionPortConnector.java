package nc.multiblock.fission.tile;

import net.minecraft.util.math.BlockPos;

public interface IFissionPortConnector extends IFissionComponent {
	
	public BlockPos getMasterPortPos();
	
	public void setMasterPortPos(BlockPos pos);
	
	public void clearMasterPort();
	
	public void refreshMasterPort();
	
	public boolean onPortRefresh();
}
