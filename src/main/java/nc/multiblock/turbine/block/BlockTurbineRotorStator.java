package nc.multiblock.turbine.block;

import static nc.util.NCRenderHelper.PIXEL;

import nc.multiblock.turbine.TurbineRotorBladeUtil;
import nc.multiblock.turbine.TurbineRotorBladeUtil.*;
import nc.multiblock.turbine.tile.TileTurbineRotorStator;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public class BlockTurbineRotorStator extends BlockTurbinePart implements IBlockRotorBlade {
	
	private static final AxisAlignedBB[] STATOR_AABB = {new AxisAlignedBB(0D, PIXEL * 2D, PIXEL * 7D, PIXEL * 16D, PIXEL * 14D, PIXEL * 9D), new AxisAlignedBB(PIXEL * 7D, 0D, PIXEL * 2D, PIXEL * 9D, PIXEL * 16D, PIXEL * 14D), new AxisAlignedBB(PIXEL * 7D, PIXEL * 2D, 0D, PIXEL * 9D, PIXEL * 14D, PIXEL * 16D)};
	
	public BlockTurbineRotorStator() {
		super();
		setDefaultState(blockState.getBaseState().withProperty(TurbineRotorBladeUtil.DIR, TurbinePartDir.Y));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TurbineRotorBladeUtil.DIR);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(TurbineRotorBladeUtil.DIR, TurbinePartDir.fromFacingAxis(facing.getAxis()));
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileTurbineRotorStator.Standard();
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (state.getBlock() != this) {
			return super.getBoundingBox(state, source, pos);
		}
		
		switch (state.getValue(TurbineRotorBladeUtil.DIR)) {
			case X:
				return STATOR_AABB[0];
			case Y:
				return STATOR_AABB[1];
			case Z:
				return STATOR_AABB[2];
			default:
				return super.getBoundingBox(state, source, pos);
		}
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockStateIn, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		if (state.getBlock() != this) {
			return super.getSelectedBoundingBox(state, worldIn, pos);
		}
		
		switch (state.getValue(TurbineRotorBladeUtil.DIR)) {
			case X:
				return STATOR_AABB[0].offset(pos);
			case Y:
				return STATOR_AABB[1].offset(pos);
			case Z:
				return STATOR_AABB[2].offset(pos);
			default:
				return super.getSelectedBoundingBox(state, worldIn, pos);
		}
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
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TurbineRotorBladeUtil.DIR).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TurbineRotorBladeUtil.DIR, TurbinePartDir.values()[meta]);
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
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isTopSolid(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean causesSuffocation(IBlockState state) {
		return false;
	}
}
