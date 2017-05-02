package nc.tile.dummy;

import nc.block.tile.generator.BlockFusionCore;
import nc.config.NCConfig;
import nc.tile.generator.TileFusionCore;
import net.minecraft.util.math.BlockPos;

public class TileFusionDummySide extends TileDummy {
	
	public TileFusionDummySide() {
		super("fusion_dummy_side", NCConfig.fusion_update_rate);
	}
	
	public void update() {
		super.update();
		if(!world.isRemote) {
			pushEnergy();
			pushFluid();
		}
	}
	
	// Finding Blocks
	
	private BlockPos getPos(int x, int y, int z) {
		return new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
	}
	
	private boolean findCore(int x, int y, int z) {
		return world.getBlockState(getPos(x, y, z)).getBlock() instanceof BlockFusionCore;
	}
	
	// Find Master
	
	protected void findMaster() {
		if (findCore(0, -1, 0)) masterPosition = getPos(0, -1, 0);
		else if (findCore(1, 0, 0)) masterPosition = getPos(1, 0, 0);
		else if (findCore(1, 0, 1)) masterPosition = getPos(1, 0, 1);
		else if (findCore(0, 0, 1)) masterPosition = getPos(0, 0, 1);
		else if (findCore(-1, 0, 1)) masterPosition = getPos(-1, 0, 1);
		else if (findCore(-1, 0, 0)) masterPosition = getPos(-1, 0, 0);
		else if (findCore(-1, 0, -1)) masterPosition = getPos(-1, 0, -1);
		else if (findCore(0, 0, -1)) masterPosition = getPos(0, 0, -1);
		else if (findCore(1, 0, -1)) masterPosition = getPos(1, 0, -1);
		
		else if (findCore(1, -1, 0)) masterPosition = getPos(1, -1, 0);
		else if (findCore(1, -1, 1)) masterPosition = getPos(1, -1, 1);
		else if (findCore(0, -1, 1)) masterPosition = getPos(0, -1, 1);
		else if (findCore(-1, -1, 1)) masterPosition = getPos(-1, -1, 1);
		else if (findCore(-1, -1, 0)) masterPosition = getPos(-1, -1, 0);
		else if (findCore(-1, -1, -1)) masterPosition = getPos(-1, -1, -1);
		else if (findCore(0, -1, -1)) masterPosition = getPos(0, -1, -1);
		else if (findCore(1, -1, -1)) masterPosition = getPos(1, -1, -1);
		else masterPosition = null;
	}
	
	public boolean isMaster(BlockPos pos) {
		return world.getTileEntity(pos) instanceof TileFusionCore;
	}
}
