package nc.multiblock.highTurbine.block;

import nc.multiblock.highTurbine.tile.TileHighTurbineWall;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockHighTurbineWall extends BlockHighTurbinePartBase {

	public BlockHighTurbineWall() {
		super("high_turbine_wall");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileHighTurbineWall();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) return false;
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) return false;
		return rightClickOnPart(world, pos, player);
	}
}
