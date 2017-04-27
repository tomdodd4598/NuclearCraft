package nc.block.tile.energy.generator;

import nc.block.tile.BlockInventoryGui;
import nc.proxy.CommonProxy;
import nc.tile.generator.TileFusionCore;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFusionCore extends BlockInventoryGui {
	
	public BlockFusionCore(String unlocalizedName, String registryName, int guiId) {
		super(unlocalizedName, registryName, Material.IRON, guiId);
		setHarvestLevel("pickaxe", 0);
		setHardness(2);
		setResistance(15);
		setCreativeTab(CommonProxy.TAB_FUSION);
	}
	
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileFusionCore();
	}
	
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return new AxisAlignedBB(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F);
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
}
