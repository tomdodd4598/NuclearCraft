package nc.multiblock.turbine.block;

import nc.multiblock.turbine.TurbineRotorBladeUtil;
import nc.multiblock.turbine.TurbineRotorBladeUtil.IBlockRotorBlade;
import nc.multiblock.turbine.TurbineRotorBladeUtil.TurbinePartDir;
import nc.multiblock.turbine.TurbineRotorBladeUtil.TurbineRotorBladeType;
import nc.multiblock.turbine.tile.TileTurbineRotorBlade;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTurbineRotorBlade extends BlockTurbinePartBase implements IBlockRotorBlade {
	
	private final TurbineRotorBladeType bladeType;
	
	public BlockTurbineRotorBlade(TurbineRotorBladeType bladeType) {
		super();
		setDefaultState(blockState.getBaseState().withProperty(TurbineRotorBladeUtil.DIR, TurbinePartDir.Y));
		this.bladeType = bladeType;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {TurbineRotorBladeUtil.DIR});
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(TurbineRotorBladeUtil.DIR, TurbinePartDir.fromFacingAxis(facing.getAxis()));
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		switch (bladeType) {
		case STEEL:
			return new TileTurbineRotorBlade.Steel();
		case EXTREME:
			return new TileTurbineRotorBlade.Extreme();
		case SIC_SIC_CMC:
			return new TileTurbineRotorBlade.SicSicCMC();
		default:
			return null;
		}
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) return false;
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) return false;
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
