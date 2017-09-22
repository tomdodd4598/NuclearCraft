package nc.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTransparent extends NCBlock {
	
	private final boolean smartRender;

	public BlockTransparent(String unlocalizedName, String registryName, Material material, boolean smartRender) {
		super(unlocalizedName, registryName, material);
		this.smartRender = smartRender;
		setHarvestLevel("pickaxe", 0);
		setHardness(1.5F);
		setResistance(10);
	}
	
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		if (!smartRender) return true;
		
		IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
		Block block = iblockstate.getBlock();
		
		if (blockState != iblockstate) return true;
		
		if (block == this) return false;
		
		return block == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }
	
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	public boolean isFullCube(IBlockState state) {
		return false;
	}
}
