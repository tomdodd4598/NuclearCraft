package nc.block.tile.quantum;

import java.util.Random;

import javax.annotation.Nullable;

import nc.block.NCBlock;
import nc.proxy.CommonProxy;
import nc.tile.quantum.TileSpin;
import nc.util.Complex;
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

	public BlockSpin(String unlocalizedName, String registryName) {
		super(unlocalizedName, registryName, Material.IRON);
		setHarvestLevel("pickaxe", 0);
		setHardness(1);
		setResistance(5);
		setCreativeTab(CommonProxy.TAB_MISC);
	}
	
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileSpin();
	}
	
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null) {
			if (tile instanceof TileSpin) {
				phi = (double) (placer.rotationYaw + 360D) % 360D;
				theta = (double) placer.rotationPitch;
				((TileSpin) tile).setStateFromAngles(phi, theta);
			}
		}
	}
	
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    	TileEntity tile = world.getTileEntity(pos);
		if (tile != null) {
			if (tile instanceof TileSpin) {
				if (!player.isSneaking()) {
					double phi = (double) (player.rotationYaw + 360D) % 360D;
					double theta = (double) player.rotationPitch;
					NCUtil.getLogger().info(phi + "   " + theta);
					Complex[] newState = ((TileSpin) tile).getStateFromAngles(phi, theta);
					NCUtil.getLogger().info(newState[0].re() + "   " + newState[0].im() + "   " + newState[1].re() + "   " + newState[1].im());
					NCUtil.getLogger().info(((TileSpin) tile).stateVector[0].re() + "   " + ((TileSpin) tile).stateVector[0].im() + "   " + ((TileSpin) tile).stateVector[1].re() + "   " + ((TileSpin) tile).stateVector[1].im());
					double expectation = 0.5; /*Complex.absSq(Matrix.dot(newState, ((TileSpin) tile).stateVector));*/
					NCUtil.getLogger().info(expectation);
					double newSpin = expectation > rand.nextDouble() ? 0.5D : -0.5D;
					((TileSpin) tile).measuredSpin = newSpin;
					((TileSpin) tile).setStateFromAngles(phi, theta);
					((TileSpin) tile).phi = phi;
					((TileSpin) tile).theta = theta;
					((TileSpin) tile).isMeasured = 10;
					return true;
				}
			}
		}
		return false;
    }
	
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	public boolean isNormalCube(IBlockState state) {
		return false;
	}

	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}
	
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null) {
			if (tile instanceof TileSpin) {
				if (((TileSpin) tile).measuredSpin > 0) return 15;
			}
		}
		return 0;
	}
	
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
		return true;
	}
	
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
