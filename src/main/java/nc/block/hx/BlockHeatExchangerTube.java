package nc.block.hx;

import nc.block.property.*;
import nc.block.tile.IDynamicState;
import nc.multiblock.hx.*;
import nc.tile.hx.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import javax.annotation.Nullable;
import java.util.*;

import static nc.util.NCRenderHelper.PIXEL;

public class BlockHeatExchangerTube extends BlockHeatExchangerPart implements IDynamicState {
	
	private static boolean placementSneaking = false;
	private static EnumFacing placementSide = EnumFacing.DOWN;
	
	private final HeatExchangerTubeType tubeType;
	
	public BlockHeatExchangerTube(HeatExchangerTubeType tubeType) {
		super();
		this.tubeType = tubeType;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return switch (tubeType) {
			case COPPER -> new TileHeatExchangerTube.Copper();
			case HARD_CARBON -> new TileHeatExchangerTube.HardCarbon();
			case THERMOCONDUCTING -> new TileHeatExchangerTube.Thermoconducting();
		};
	}
	
	private static final PropertySidedEnum<HeatExchangerTubeSetting> DOWN = tubeSide("down", EnumFacing.DOWN);
	private static final PropertySidedEnum<HeatExchangerTubeSetting> UP = tubeSide("up", EnumFacing.UP);
	private static final PropertySidedEnum<HeatExchangerTubeSetting> NORTH = tubeSide("north", EnumFacing.NORTH);
	private static final PropertySidedEnum<HeatExchangerTubeSetting> SOUTH = tubeSide("south", EnumFacing.SOUTH);
	private static final PropertySidedEnum<HeatExchangerTubeSetting> WEST = tubeSide("west", EnumFacing.WEST);
	private static final PropertySidedEnum<HeatExchangerTubeSetting> EAST = tubeSide("east", EnumFacing.EAST);
	
	public static PropertySidedEnum<HeatExchangerTubeSetting> tubeSide(String name, EnumFacing facing) {
		return PropertySidedEnum.create(name, HeatExchangerTubeSetting.class, facing);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, DOWN, UP, NORTH, SOUTH, WEST, EAST);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileHeatExchangerTube tube) {
			HeatExchangerTubeSetting[] settings = tube.settings;
			return state.withProperty(DOWN, settings[0]).withProperty(UP, settings[1]).withProperty(NORTH, settings[2]).withProperty(SOUTH, settings[3]).withProperty(WEST, settings[4]).withProperty(EAST, settings[5]);
		}
		else {
			return state;
		}
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (hand != EnumHand.MAIN_HAND) {
			return false;
		}
		
		return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		placementSneaking = placer.isSneaking();
		placementSide = facing.getOpposite();
		return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileHeatExchangerTube tube) {
			boolean update = false;
			
			BlockPos otherPos = pos.offset(placementSide);
			TileEntity otherTile = world.getTileEntity(otherPos);
			if (otherTile instanceof TileHeatExchangerTube other) {
				if (placementSneaking) {
					tube.setTubeSettings(other.settings);
					for (int i = 0; i < 6; ++i) {
						EnumFacing side = EnumFacing.VALUES[i], opposite = side.getOpposite();
						if (side.equals(placementSide)) {
							tube.setTubeSetting(placementSide, other.getTubeSetting(opposite));
						}
						else if (other.settings[i].isOpen() && world.getTileEntity(pos.offset(side)) instanceof TileHeatExchangerTube offsetTube && world.getTileEntity(otherPos.offset(side)) instanceof TileHeatExchangerTube offsetOther && offsetOther.getTubeSetting(opposite).isOpen()) {
							offsetTube.setTubeSettingOpen(opposite, true);
							offsetTube.markDirtyAndNotify(true);
						}
					}
				}
				else {
					other.setTubeSettingOpen(placementSide.getOpposite(), true);
					tube.setTubeSetting(placementSide, other.getTubeSetting(placementSide.getOpposite()));
					other.markDirtyAndNotify(true);
				}
				update = true;
			}
			
			for (EnumFacing side : Arrays.asList(placementSide, placementSide.getOpposite())) {
				otherTile = world.getTileEntity(pos.offset(side));
				if (otherTile instanceof TileHeatExchangerInlet || otherTile instanceof TileHeatExchangerOutlet) {
					tube.setTubeSetting(side, HeatExchangerTubeSetting.OPEN);
					update = true;
					break;
				}
			}
			
			if (update) {
				tube.markDirtyAndNotify(true);
			}
		}
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileHeatExchangerTube tube) {
			for (EnumFacing side : EnumFacing.VALUES) {
				HeatExchangerTubeSetting setting = tube.getTubeSetting(side);
				if (setting.isOpen()) {
					TileEntity otherTile = world.getTileEntity(pos.offset(side));
					if (otherTile instanceof TileHeatExchangerTube other) {
						other.setTubeSettingOpen(side.getOpposite(), false);
						other.markDirtyAndNotify(true);
					}
				}
			}
		}
		
		super.breakBlock(world, pos, state);
	}
	
	// Rendering
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	// Bounding Box
	
	private static final AxisAlignedBB CENTER_AABB = new AxisAlignedBB(PIXEL * 2D, PIXEL * 2D, PIXEL * 2D, PIXEL * 14D, PIXEL * 14D, PIXEL * 14D);
	
	private static final AxisAlignedBB[] SIDE_AABB = {
			new AxisAlignedBB(PIXEL * 2D, 0D, PIXEL * 2D, PIXEL * 14D, PIXEL * 2D, PIXEL * 14D),
			new AxisAlignedBB(PIXEL * 2D, 1D, PIXEL * 2D, PIXEL * 14D, PIXEL * 14D, PIXEL * 14D),
			new AxisAlignedBB(PIXEL * 2D, PIXEL * 2D, 0D, PIXEL * 14D, PIXEL * 14D, PIXEL * 2D),
			new AxisAlignedBB(PIXEL * 2D, PIXEL * 2D, 1D, PIXEL * 14D, PIXEL * 14D, PIXEL * 14D),
			new AxisAlignedBB(0D, PIXEL * 2D, PIXEL * 2D, PIXEL * 2D, PIXEL * 14D, PIXEL * 14D),
			new AxisAlignedBB(1D, PIXEL * 2D, PIXEL * 2D, PIXEL * 14D, PIXEL * 14D, PIXEL * 14D)
	};
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
		if (state.getBlock() != this) {
			super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
			return;
		}
		
		Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, CENTER_AABB);
		
		if (!isActualState) {
			state = getActualState(state, worldIn, pos);
		}
		
		if (!state.getValue(DOWN).equals(HeatExchangerTubeSetting.CLOSED)) {
			Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, SIDE_AABB[0]);
		}
		
		if (!state.getValue(UP).equals(HeatExchangerTubeSetting.CLOSED)) {
			Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, SIDE_AABB[1]);
		}
		
		if (!state.getValue(NORTH).equals(HeatExchangerTubeSetting.CLOSED)) {
			Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, SIDE_AABB[2]);
		}
		
		if (!state.getValue(SOUTH).equals(HeatExchangerTubeSetting.CLOSED)) {
			Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, SIDE_AABB[3]);
		}
		
		if (!state.getValue(WEST).equals(HeatExchangerTubeSetting.CLOSED)) {
			Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, SIDE_AABB[4]);
		}
		
		if (!state.getValue(EAST).equals(HeatExchangerTubeSetting.CLOSED)) {
			Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, SIDE_AABB[5]);
		}
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (state.getBlock() != this) {
			return super.getBoundingBox(state, source, pos);
		}
		
		//noinspection UnnecessaryLocalVariable
		AxisAlignedBB boundingBox = CENTER_AABB;
		
		/*
		state = getActualState(state, source, pos);
		
		if (state.getValue(DOWN)) {
			boundingBox = boundingBox.union(SIDE_AABB[0]);
		}
		
		if (state.getValue(UP)) {
			boundingBox = boundingBox.union(SIDE_AABB[1]);
		}
		
		if (state.getValue(NORTH)) {
			boundingBox = boundingBox.union(SIDE_AABB[2]);
		}
		
		if (state.getValue(SOUTH)) {
			boundingBox = boundingBox.union(SIDE_AABB[3]);
		}
		
		if (state.getValue(WEST)) {
			boundingBox = boundingBox.union(SIDE_AABB[4]);
		}
		
		if (state.getValue(EAST)) {
			boundingBox = boundingBox.union(SIDE_AABB[5]);
		}
		*/
		
		return boundingBox;
	}
}
