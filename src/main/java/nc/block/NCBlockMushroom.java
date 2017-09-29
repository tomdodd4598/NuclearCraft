package nc.block;

import java.util.Random;

import nc.Global;
import nc.proxy.CommonProxy;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NCBlockMushroom extends BlockMushroom {
	
	public NCBlockMushroom(String unlocalizedName, String registryName) {
		super();
		setUnlocalizedName(unlocalizedName);
		setRegistryName(new ResourceLocation(Global.MOD_ID, registryName));
		setCreativeTab(CommonProxy.TAB_BASE_BLOCK_MATERIALS);
		setLightLevel(1F);
	}
	
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return false;
	}

	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return false;
	}

	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {}
	
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
		if (pos.getY() >= 0 && pos.getY() < 256) {
			IBlockState iblockstate = worldIn.getBlockState(pos.down());
			return iblockstate.getBlock() == Blocks.MYCELIUM ? true : (iblockstate.getBlock() == Blocks.DIRT && iblockstate.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.PODZOL ? true : iblockstate.getBlock().canSustainPlant(iblockstate, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this));
		} else return false;
	}
}
