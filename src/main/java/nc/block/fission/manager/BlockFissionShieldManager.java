package nc.block.fission.manager;

import javax.annotation.Nullable;

import nc.tile.fission.TileFissionShield;
import nc.tile.fission.manager.TileFissionShieldManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;

public class BlockFissionShieldManager extends BlockFissionManager<TileFissionShieldManager, TileFissionShield> {
	
	public BlockFissionShieldManager() {
		super(TileFissionShieldManager.class);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileFissionShieldManager();
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
		return side != null;
	}
}
