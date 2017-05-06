package nc.block.tile.passive;

import nc.init.NCBlocks;
import nc.proxy.CommonProxy;
import nc.tile.passive.Passives.TileCobblestoneGenerator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCobblestoneGenerator extends BlockPassive {
	
	public BlockCobblestoneGenerator(String unlocalizedName, String registryName) {
		super(unlocalizedName, registryName, false, CommonProxy.TAB_MACHINES);
	}
	
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileCobblestoneGenerator();
	}
	
	public static void setState(World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		keepInventory = true;
		world.setBlockState(pos, NCBlocks.cobblestone_generator.getDefaultState(), 3);
		keepInventory = false;
		
		if (tile != null) {
			tile.validate();
			world.setTileEntity(pos, tile);
		}
	}
}
