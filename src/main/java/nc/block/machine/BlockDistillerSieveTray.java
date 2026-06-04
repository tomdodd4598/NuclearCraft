package nc.block.machine;

import nc.tile.machine.TileDistillerSieveTray;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

import static nc.block.property.BlockProperties.INVISIBLE;

public class BlockDistillerSieveTray extends BlockMachinePart {
	
	public BlockDistillerSieveTray() {
		super();
		setDefaultState(blockState.getBaseState().withProperty(INVISIBLE, false));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, INVISIBLE);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(INVISIBLE, meta == 1);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(INVISIBLE) ? 1 : 0;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileDistillerSieveTray();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) {
			return false;
		}
		return rightClickOnPart(world, pos, player, hand, facing);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
}
