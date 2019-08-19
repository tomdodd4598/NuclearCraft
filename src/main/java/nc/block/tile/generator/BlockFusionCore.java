package nc.block.tile.generator;

import nc.block.tile.BlockTile;
import nc.block.tile.IActivatable;
import nc.init.NCBlocks;
import nc.tab.NCTabs;
import nc.tile.generator.TileFusionCore;
import nc.util.BlockPosHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFusionCore extends BlockTile implements IActivatable {
	
	public BlockFusionCore() {
		super(Material.ANVIL);
		setCreativeTab(NCTabs.FUSION);
		canCreatureSpawn = false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileFusionCore();
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
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		BlockPosHelper helper = new BlockPosHelper(pos);
		for (BlockPos blockPos : helper.cuboid(-1, 0, -1, 1, 2, 1)) if (!(isAir(world, blockPos))) return false;
		return true;
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
		BlockPosHelper helper = new BlockPosHelper(pos);
		for (BlockPos blockPos : helper.squareRing(1, 0)) setSide(world, blockPos);
		for (BlockPos blockPos : helper.cuboid(-1, 1, -1, 1, 1, 1)) setSide(world, blockPos);
		for (BlockPos blockPos : helper.cuboid(-1, 2, -1, 1, 2, 1)) setTop(world, blockPos);
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		BlockPosHelper helper = new BlockPosHelper(pos);
		for (BlockPos blockPos : helper.squareRing(1, 0)) setAir(world, blockPos);
		for (BlockPos blockPos : helper.cuboid(-1, 1, -1, 1, 2, 1)) setAir(world, blockPos);
		world.removeTileEntity(pos);
	}
	
	private static boolean isAir(World world, BlockPos pos) {
		Material mat = world.getBlockState(pos).getMaterial();
		return mat == Material.AIR || mat == Material.FIRE || mat == Material.WATER || mat == Material.VINE || mat == Material.SNOW;
	}
	
	private static void setAir(World world, BlockPos pos) {
		world.destroyBlock(pos, false);
	}
	
	private static void setSide(World world, BlockPos pos) {
		IBlockState dummy = NCBlocks.fusion_dummy_side.getDefaultState();
		world.setBlockState(pos, dummy);
	}
	
	private static void setTop(World world, BlockPos pos) {
		IBlockState dummy = NCBlocks.fusion_dummy_top.getDefaultState();
		world.setBlockState(pos, dummy);
	}
	
	@Override
	public Block getBlockType(boolean active) {
		return NCBlocks.fusion_core;
	}
	
	@Override
	public void setState(boolean isActive, TileEntity tile) {
		//tile.getWorld().setBlockState(tile.getPos(), NCBlocks.fusion_core.getDefaultState(), 2);
	}
	
	@Override
	public void onGuiOpened(World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileFusionCore) {
			((TileFusionCore) tile).refreshMultiblock();
		}
	}
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileFusionCore) {
			return ((TileFusionCore)tile).getComparatorStrength();
		}
		return Container.calcRedstone(tile);
	}
}
