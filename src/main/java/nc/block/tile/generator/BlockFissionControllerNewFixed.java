package nc.block.tile.generator;

import static nc.block.property.BlockProperties.ACTIVE;
import static nc.block.property.BlockProperties.FACING_HORIZONTAL;

import java.util.Random;

import nc.block.tile.processor.BlockProcessor;
import nc.config.NCConfig;
import nc.enumm.BlockEnums.ProcessorType;
import nc.init.NCSounds;
import nc.tile.generator.TileFissionController;
import nc.util.BlockFinder;
import nc.util.NCInventoryHelper;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockFissionControllerNewFixed extends BlockProcessor {

	public BlockFissionControllerNewFixed() {
		super(ProcessorType.FISSION_CONTROLLER_NEW_FIXED);
		canCreatureSpawn = false;
	}
	
	@Override
	protected IBlockState getNewDefaultState() {
		return blockState.getBaseState().withProperty(FACING_HORIZONTAL, EnumFacing.NORTH).withProperty(ACTIVE, Boolean.valueOf(false));
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.byIndex(meta & 7);
		if (enumfacing.getAxis() == EnumFacing.Axis.Y) enumfacing = EnumFacing.NORTH;
		
		return getDefaultState().withProperty(FACING_HORIZONTAL, enumfacing).withProperty(ACTIVE, Boolean.valueOf((meta & 8) > 0));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		int i = state.getValue(FACING_HORIZONTAL).getIndex();
		
		if (state.getValue(ACTIVE).booleanValue()) i |= 8;
		
		return i;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING_HORIZONTAL, ACTIVE);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(FACING_HORIZONTAL, placer.getHorizontalFacing().getOpposite()).withProperty(ACTIVE, Boolean.valueOf(false));
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
		setDefaultDirection(world, pos, state);
	}
	
	private static void setDefaultDirection(World world, BlockPos pos, IBlockState state) {
		if (!world.isRemote) {
			EnumFacing enumfacing = state.getValue(FACING_HORIZONTAL);
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
			world.setBlockState(pos, state.withProperty(FACING_HORIZONTAL, enumfacing).withProperty(ACTIVE, Boolean.valueOf(false)), 2);
		}
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		
	}
	
	public void setStateNewFixed(boolean isActive, TileEntity tile) {
		World world = tile.getWorld();
		BlockPos pos = tile.getPos();
		IBlockState state = world.getBlockState(pos);
		if (isActive != state.getValue(ACTIVE)) {
			world.setBlockState(pos, state.withProperty(ACTIVE, isActive), 2);
		}
	}
	
	@Override
	public void setState(boolean isActive, TileEntity tile) {}
	
	@Override
	public void onGuiOpened(World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileFissionController) {
			TileFissionController controller = (TileFissionController) tile;
			controller.refreshMultiblock(true);
		}
	}
	
	@Override
	public void dropItems(World world, BlockPos pos, IInventory tileentity) {
		NCInventoryHelper.dropInventoryItems(world, pos, tileentity, 0, 1);
	}
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileFissionController) {
			return ((TileFissionController)tile).getComparatorStrength();
		}
		return Container.calcRedstone(tile);
	}
	
	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		super.randomDisplayTick(state, world, pos, rand);
		
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileFissionController) {
			TileFissionController controller = (TileFissionController) tile;
			BlockFinder finder = new BlockFinder(pos, world, controller.getBlockMetadata() & 7);
			BlockPos position = finder.randomWithin(controller.minX, controller.maxX, controller.minY, controller.maxY, controller.minZ, controller.maxZ);
			
			if (controller.cells <= 0) return;
			double soundRate = MathHelper.clamp(0.08D, 2*Math.sqrt(controller.cells)/NCConfig.fission_max_size, 1D);
			if (controller.isProcessing) if (rand.nextDouble() < soundRate) {
				world.playSound(position.getX(), position.getY(), position.getZ(), NCSounds.geiger_tick, SoundCategory.BLOCKS, 1.6F, 1F + 0.12F*(rand.nextFloat() - 0.5F), false);
			}
		}
	}
}
