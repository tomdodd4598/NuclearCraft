package nc.multiblock.fission.moltensalt.block;

import nc.NuclearCraft;
import nc.multiblock.fission.moltensalt.tile.TileSaltFissionController;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSaltFissionController extends BlockSaltFissionPartBase {
	
	public static final PropertyInteger ON_OFF = PropertyInteger.create("on_off", 0, 1);

	public BlockSaltFissionController() {
		super("salt_fission_controller");
		setDefaultState(blockState.getBaseState().withProperty(ON_OFF, 0));
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileSaltFissionController();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(ON_OFF, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(ON_OFF);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, ON_OFF);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;
		if (hand != EnumHand.MAIN_HAND) return false;
		if (player.isSneaking()) return false;

		if (world.getTileEntity(pos) != null && ((TileSaltFissionController) world.getTileEntity(pos)).getMultiblockController() != null && ((TileSaltFissionController) world.getTileEntity(pos)).getMultiblockController().isAssembled()) {
			player.openGui(NuclearCraft.instance, 102, world, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return rightClickOnPart(world, pos, player);
	}
}
