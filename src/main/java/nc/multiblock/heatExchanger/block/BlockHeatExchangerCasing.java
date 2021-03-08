package nc.multiblock.heatExchanger.block;

import static nc.block.property.BlockProperties.FRAME;

import nc.multiblock.heatExchanger.tile.TileHeatExchangerCasing;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockHeatExchangerCasing extends BlockHeatExchangerPart {
	
	public BlockHeatExchangerCasing() {
		super();
		setDefaultState(blockState.getBaseState().withProperty(FRAME, false));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FRAME);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FRAME, meta == 1);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FRAME) ? 1 : 0;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileHeatExchangerCasing();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) {
			return false;
		}
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) {
			return false;
		}
		return rightClickOnPart(world, pos, player, hand, facing);
	}
}
