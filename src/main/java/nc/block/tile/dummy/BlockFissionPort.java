package nc.block.tile.dummy;

import nc.block.tile.BlockSidedInventory;
import nc.proxy.CommonProxy;
import nc.tile.dummy.TileFissionPort;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFissionPort extends BlockSidedInventory {
	
	public BlockFissionPort(String unlocalizedName, String registryName) {
		super(unlocalizedName, registryName, Material.IRON);
		setHarvestLevel("pickaxe", 0);
		setHardness(2);
		setResistance(15);
		setCreativeTab(CommonProxy.TAB_FISSION_BLOCKS);
	}
	
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileFissionPort();
	}
	
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		world.removeTileEntity(pos);
	}
}
