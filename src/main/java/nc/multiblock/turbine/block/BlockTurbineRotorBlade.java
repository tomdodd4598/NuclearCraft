package nc.multiblock.turbine.block;

import nc.multiblock.turbine.TurbineRotorBladeType;
import nc.multiblock.turbine.tile.TileTurbineRotorBlade;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTurbineRotorBlade extends BlockTurbinePartBase {
	
	private static final PropertyEnum<RotorBladeState> DIR = PropertyEnum.create("dir", RotorBladeState.class);
	
	private final TurbineRotorBladeType bladeType;
	
	public BlockTurbineRotorBlade(TurbineRotorBladeType bladeType) {
		super("turbine_rotor_blade_" + bladeType.toString());
		setDefaultState(blockState.getBaseState().withProperty(DIR, RotorBladeState.Y));
		
		this.bladeType = bladeType;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {DIR});
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(DIR, RotorBladeState.fromFacingAxis(facing.getAxis()));
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
		return rightClickOnPart(world, pos, player, hand);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return ((RotorBladeState) state.getValue(DIR)).getID();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(DIR, RotorBladeState.values()[meta]);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
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
		return true;
	}
	
	private static enum RotorBladeState implements IStringSerializable {
		INVISIBLE("invisible", 0),
		X("x", 1),
		Y("y", 2),
		Z("z", 3);
		
		private String name;
		private int id;
		
		private RotorBladeState(String name, int id) {
			this.name = name;
			this.id = id;
		}
		
		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		public int getID() {
			return id;
		}
		
		public static RotorBladeState fromFacingAxis(EnumFacing.Axis axis) {
			switch (axis) {
				case X:
					return X;
				case Y:
					return Y;
				case Z:
					return Z;
				default:
					return INVISIBLE;
			}
		}
	}
}
