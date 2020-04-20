package nc.multiblock.qComputer.block;

import nc.multiblock.qComputer.tile.TileQuantumComputerConnector;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockQuantumComputerConnector extends BlockQuantumComputerPart {
	
	public BlockQuantumComputerConnector() {
		super();
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileQuantumComputerConnector();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) return false;
		if (hand != EnumHand.MAIN_HAND) return false;
		
		return rightClickOnPart(world, pos, player, hand, facing, false);
	}
}
