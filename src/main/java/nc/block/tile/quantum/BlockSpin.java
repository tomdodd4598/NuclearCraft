package nc.block.tile.quantum;

import java.util.Random;

import javax.annotation.Nullable;

import nc.block.NCBlock;
import nc.tab.NCTabs;
import nc.tile.quantum.TileSpin;
import nc.util.Complex;
import nc.util.Matrix;
import nc.util.NCUtil;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSpin extends NCBlock implements ITileEntityProvider {
	
	private Random rand = new Random();
	public double phi = 0;
	public double theta = 0;

	public BlockSpin(String name) {
		super(name, Material.IRON);
		setHardness(1F);
		setResistance(5F);
		setCreativeTab(NCTabs.MISC);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileSpin();
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null) {
			if (tile instanceof TileSpin) {
				phi = (placer.rotationYaw + 360D) % 360D;
				theta = placer.rotationPitch;
				((TileSpin) tile).setStateFromAngles(phi, theta);
			}
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity tile = world.getTileEntity(pos);
		if (player != null) if (tile instanceof TileSpin && !player.isSneaking()) {
			TileSpin qubit = (TileSpin) tile;
			double phi = (player.rotationYaw + 360D) % 360D;
			double theta = player.rotationPitch;
			NCUtil.getLogger().info(phi + "   " + theta);
			Complex[] newState = qubit.getStateFromAngles(phi, theta);
			NCUtil.getLogger().info(newState[0].re() + "   " + newState[0].im() + "   " + newState[1].re() + "   " + newState[1].im());
			NCUtil.getLogger().info(qubit.stateVector[0].re() + "   " + qubit.stateVector[0].im() + "   " + qubit.stateVector[1].re() + "   " + qubit.stateVector[1].im());
			double expectation = Complex.absSq(Matrix.dot(newState, qubit.stateVector));
			NCUtil.getLogger().info(expectation);
			double newSpin = expectation > rand.nextDouble() ? 0.5D : -0.5D;
			qubit.measuredSpin = newSpin;
			qubit.setStateFromAngles(phi, theta);
			qubit.phi = phi;
			qubit.theta = theta;
			qubit.isMeasured = 10;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null) {
			if (tile instanceof TileSpin) {
				if (((TileSpin) tile).measuredSpin > 0) return 15;
			}
		}
		return 0;
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
		return side != null;
	}
	
	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		TileEntity tile = blockAccess.getTileEntity(pos);
		if (tile != null) {
			if (tile instanceof TileSpin) {
				if (((TileSpin) tile).measuredSpin > 0) return 15;
			}
		}
		return 0;
	}
}
