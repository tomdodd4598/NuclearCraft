package nc.tile.dummy;

import nc.block.tile.generator.BlockFusionCore;
import nc.config.NCConfig;
import nc.tile.generator.TileFusionCore;
import net.minecraft.util.math.BlockPos;

public class TileFusionDummyTop extends TileDummy {
	
	private static final String[] FUSION_FUELS = new String[] {"hydrogen", "deuterium", "tritium", "helium3", "lithium6", "lithium7", "boron11"};
	
	private static final String[][] ALLOWED_FUELS = new String[][] {FUSION_FUELS, FUSION_FUELS, {}, {}, {}, {}, {}, {}};
	
	public TileFusionDummyTop() {
		super("fusion_dummy_top", NCConfig.fusion_update_rate, ALLOWED_FUELS);
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
		if (findCore(0, -2, 0)) masterPosition = getPos(0, -2, 0);
		else if (findCore(1, -2, 0)) masterPosition = getPos(1, -2, 0);
		else if (findCore(1, -2, 1)) masterPosition = getPos(1, -2, 1);
		else if (findCore(0, -2, 1)) masterPosition = getPos(0, -2, 1);
		else if (findCore(-1, -2, 1)) masterPosition = getPos(-1, -2, 1);
		else if (findCore(-1, -2, 0)) masterPosition = getPos(-1, -2, 0);
		else if (findCore(-1, -2, -1)) masterPosition = getPos(-1, -2, -1);
		else if (findCore(0, -2, -1)) masterPosition = getPos(0, -2, -1);
		else if (findCore(1, -2, -1)) masterPosition = getPos(1, -2, -1);
		else masterPosition = null;
	}
	
	public boolean isMaster(BlockPos pos) {
		return world.getTileEntity(pos) instanceof TileFusionCore;
	}
}
