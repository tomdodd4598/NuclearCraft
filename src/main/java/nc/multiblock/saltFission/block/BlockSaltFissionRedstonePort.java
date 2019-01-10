package nc.multiblock.saltFission.block;

import nc.multiblock.saltFission.tile.TileSaltFissionRedstonePort;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSaltFissionRedstonePort extends BlockSaltFissionPartBase {
	
	private static final PropertyBool ACTIVE = PropertyBool.create("active");

	public BlockSaltFissionRedstonePort() {
		super("salt_fission_redstone_port");
		setDefaultState(blockState.getBaseState().withProperty(ACTIVE, Boolean.valueOf(false)));
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileSaltFissionRedstonePort();
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, ACTIVE);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(ACTIVE, Boolean.valueOf(meta > 0));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return ((Boolean)state.getValue(ACTIVE)).booleanValue() ? 1 : 0;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) return false;
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) return false;
		return rightClickOnPart(world, pos, player, hand);
	}
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileSaltFissionRedstonePort) {
			return ((TileSaltFissionRedstonePort)tile).comparatorSignal;
		}
		return 0;
	}
	
	public boolean setActiveState(IBlockState state, World world, BlockPos pos, boolean active) {
		if (!world.isRemote) {
			if (active != state.getValue(ACTIVE)) {
				world.setBlockState(pos, state.withProperty(ACTIVE, active), 2);
				return true;
			}
		}
		return false;
	}
}
