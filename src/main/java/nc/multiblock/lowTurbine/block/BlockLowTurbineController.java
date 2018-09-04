package nc.multiblock.lowTurbine.block;

import nc.NuclearCraft;
import nc.multiblock.lowTurbine.tile.TileLowTurbineController;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockLowTurbineController extends BlockLowTurbinePartBase {
	
	private static final PropertyDirection FACING = BlockDirectional.FACING;
	private static final PropertyBool ACTIVE = PropertyBool.create("active");
	
	public BlockLowTurbineController() {
		super("low_turbine_controller");
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, Boolean.valueOf(false)));
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileLowTurbineController();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getFront(meta & 7);
		if (enumfacing.getAxis() == EnumFacing.Axis.Y) enumfacing = EnumFacing.NORTH;
		
		return getDefaultState().withProperty(FACING, enumfacing).withProperty(ACTIVE, Boolean.valueOf((meta & 8) > 0));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		int i = ((EnumFacing)state.getValue(FACING)).getIndex();
		
		if (((Boolean)state.getValue(ACTIVE)).booleanValue()) i |= 8;
		
		return i;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, ACTIVE);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)).withProperty(ACTIVE, Boolean.valueOf(false));
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
		setDefaultDirection(world, pos, state);
	}
	
	private void setDefaultDirection(World world, BlockPos pos, IBlockState state) {
		if (!world.isRemote) {
			EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
			boolean flag = world.getBlockState(pos.north()).isFullBlock();
			boolean flag1 = world.getBlockState(pos.south()).isFullBlock();

			if (enumfacing == EnumFacing.NORTH && flag && !flag1) enumfacing = EnumFacing.SOUTH;
			else if (enumfacing == EnumFacing.SOUTH && flag1 && !flag) enumfacing = EnumFacing.NORTH;
			
			else {
				boolean flag2 = world.getBlockState(pos.west()).isFullBlock();
				boolean flag3 = world.getBlockState(pos.east()).isFullBlock();

				if (enumfacing == EnumFacing.WEST && flag2 && !flag3) enumfacing = EnumFacing.EAST;
				else if (enumfacing == EnumFacing.EAST && flag3 && !flag2) enumfacing = EnumFacing.WEST;
			}
			world.setBlockState(pos, state.withProperty(FACING, enumfacing).withProperty(ACTIVE, Boolean.valueOf(false)), 2);
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) return false;
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) return false;
		
		if (!world.isRemote) {
			if (world.getTileEntity(pos) instanceof TileLowTurbineController) {
				TileLowTurbineController controller = (TileLowTurbineController) world.getTileEntity(pos);
				if (controller.getMultiblock() != null && controller.getMultiblock().isAssembled()) {
					player.openGui(NuclearCraft.instance, 102, world, pos.getX(), pos.getY(), pos.getZ());
					return true;
				}
			}
		}
		return rightClickOnPart(world, pos, player, true);
	}
	
	public void setActiveState(IBlockState state, World world, BlockPos pos, boolean active) {
		if (!world.isRemote) {
			if (active != state.getValue(ACTIVE)) {
				world.setBlockState(pos, state.withProperty(ACTIVE, active), 2);
			}
		}
	}
}
