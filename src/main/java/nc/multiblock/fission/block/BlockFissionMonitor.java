package nc.multiblock.fission.block;

import static nc.block.property.BlockProperties.ACTIVE;
import static nc.block.property.BlockProperties.FACING_ALL;

import nc.item.ItemMultitool;
import nc.multiblock.fission.FissionCluster;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.tile.IFissionComponent;
import nc.multiblock.fission.tile.TileFissionMonitor;
import nc.render.BlockHighlightTracker;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFissionMonitor extends BlockFissionPart {
	
	public BlockFissionMonitor() {
		super();
		setDefaultState(blockState.getBaseState().withProperty(FACING_ALL, EnumFacing.NORTH).withProperty(ACTIVE, Boolean.valueOf(false)));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING_ALL, ACTIVE);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.byIndex(meta & 7);
		return getDefaultState().withProperty(FACING_ALL, enumfacing).withProperty(ACTIVE, Boolean.valueOf((meta & 8) > 0));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		int i = state.getValue(FACING_ALL).getIndex();
		if (state.getValue(ACTIVE).booleanValue()) i |= 8;
		return i;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileFissionMonitor();
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(FACING_ALL, EnumFacing.getDirectionFromEntityLiving(pos, placer)).withProperty(ACTIVE, Boolean.valueOf(false));
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
		setDefaultDirection(world, pos, state);
	}
	
	private static void setDefaultDirection(World world, BlockPos pos, IBlockState state) {
		if (!world.isRemote) {
			EnumFacing enumfacing = state.getValue(FACING_ALL);
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
			world.setBlockState(pos, state.withProperty(FACING_ALL, enumfacing).withProperty(ACTIVE, Boolean.valueOf(false)), 2);
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) return false;
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) return false;
		
		if (!world.isRemote && !ItemMultitool.isMultitool(player.getHeldItem(hand))) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileFissionMonitor) {
				TileFissionMonitor monitor = (TileFissionMonitor) tile;
				FissionReactor reactor = monitor.getMultiblock();
				if (reactor != null) {
					IFissionComponent component = reactor.getPartMap(IFissionComponent.class).get(monitor.getComponentPos().toLong());
					if (component != null) {
						FissionCluster cluster = component.getCluster();
						if (cluster != null) {
							for (long posLong : cluster.getComponentMap().keySet()) {
								BlockHighlightTracker.sendPacket((EntityPlayerMP)player, posLong, 5000);
							}
							return true;
						}
					}
				}
			}
		}
		return rightClickOnPart(world, pos, player, hand, facing, true);
	}
	
	public void setState(boolean isActive, TileEntity tile) {
		World world = tile.getWorld();
		BlockPos pos = tile.getPos();
		IBlockState state = world.getBlockState(pos);
		if (!world.isRemote && state.getBlock() instanceof BlockFissionMonitor) {
			if (isActive != state.getValue(ACTIVE)) {
				world.setBlockState(pos, state.withProperty(ACTIVE, isActive), 2);
			}
		}
	}
}
