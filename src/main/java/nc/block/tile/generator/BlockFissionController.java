package nc.block.tile.generator;

import java.util.Random;

import nc.block.tile.processor.BlockProcessor;
import nc.init.NCBlocks;
import nc.proxy.CommonProxy;
import nc.tile.generator.TileFissionController;
import nc.util.NCInventoryHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFissionController extends BlockProcessor {

	public BlockFissionController(String unlocalizedName, String registryName, boolean isActive, int guiId) {
		super(unlocalizedName, registryName, "", "", null, isActive, guiId);
		if (!isActive) setCreativeTab(CommonProxy.TAB_FISSION_BLOCKS);
	}
	
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileFissionController();
	}
	
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(NCBlocks.fission_controller_idle);
	}
	
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		return new ItemStack(NCBlocks.fission_controller_idle);
	}
	
	public void dropItems(World world, BlockPos pos, IInventory tileentity) {
		NCInventoryHelper.dropInventoryItems(world, pos, tileentity, 0, 1);
	}

	public static void setState(boolean active, World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		TileEntity tile = world.getTileEntity(pos);
		keepInventory = true;
		
		if (active) {
			world.setBlockState(pos, NCBlocks.fission_controller_active.getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);
		} else {
			world.setBlockState(pos, NCBlocks.fission_controller_idle.getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);
		}
		keepInventory = false;
		
		if (tile != null) {
			tile.validate();
			world.setTileEntity(pos, tile);
		}
	}
}
