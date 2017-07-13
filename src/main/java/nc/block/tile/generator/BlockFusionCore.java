package nc.block.tile.generator;

import nc.block.tile.BlockInventoryGui;
import nc.init.NCBlocks;
import nc.proxy.CommonProxy;
import nc.tile.generator.TileFusionCore;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
	
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return isAir(world, pos, 1, 0, 0) &&
				isAir(world, pos, 1, 0, 1) &&
				isAir(world, pos, 0, 0, 1) &&
				isAir(world, pos, -1, 0, 1) &&
				isAir(world, pos, -1, 0, 0) &&
				isAir(world, pos, -1, 0, -1) &&
				isAir(world, pos, 0, 0, -1) &&
				isAir(world, pos, 1, 0, -1) &&
				isAir(world, pos, 0, 1, 0) &&
				isAir(world, pos, 1, 1, 0) &&
				isAir(world, pos, 1, 1, 1) &&
				isAir(world, pos, 0, 1, 1) &&
				isAir(world, pos, -1, 1, 1) &&
				isAir(world, pos, -1, 1, 0) &&
				isAir(world, pos, -1, 1, -1) &&
				isAir(world, pos, 0, 1, -1) &&
				isAir(world, pos, 1, 1, -1) &&
				isAir(world, pos, 0, 2, 0) &&
				isAir(world, pos, 1, 2, 0) &&
				isAir(world, pos, 1, 2, 1) &&
				isAir(world, pos, 0, 2, 1) &&
				isAir(world, pos, -1, 2, 1) &&
				isAir(world, pos, -1, 2, 0) &&
				isAir(world, pos, -1, 2, -1) &&
				isAir(world, pos, 0, 2, -1) &&
				isAir(world, pos, 1, 2, -1);
	}
	
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
		
		setSide(world, pos, 1, 0, 0);
		setSide(world, pos, 1, 0, 1);
		setSide(world, pos, 0, 0, 1);
		setSide(world, pos, -1, 0, 1);
		setSide(world, pos, -1, 0, 0);
		setSide(world, pos, -1, 0, -1);
		setSide(world, pos, 0, 0, -1);
		setSide(world, pos, 1, 0, -1);
		
		setSide(world, pos, 0, 1, 0);
		setSide(world, pos, 1, 1, 0);
		setSide(world, pos, 1, 1, 1);
		setSide(world, pos, 0, 1, 1);
		setSide(world, pos, -1, 1, 1);
		setSide(world, pos, -1, 1, 0);
		setSide(world, pos, -1, 1, -1);
		setSide(world, pos, 0, 1, -1);
		setSide(world, pos, 1, 1, -1);
		
		setTop(world, pos, 0, 2, 0);
		setTop(world, pos, 1, 2, 0);
		setTop(world, pos, 1, 2, 1);
		setTop(world, pos, 0, 2, 1);
		setTop(world, pos, -1, 2, 1);
		setTop(world, pos, -1, 2, 0);
		setTop(world, pos, -1, 2, -1);
		setTop(world, pos, 0, 2, -1);
		setTop(world, pos, 1, 2, -1);
	}
	
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		setAir(world, pos, 1, 0, 0);
		setAir(world, pos, 1, 0, 1);
		setAir(world, pos, 0, 0, 1);
		setAir(world, pos, -1, 0, 1);
		setAir(world, pos, -1, 0, 0);
		setAir(world, pos, -1, 0, -1);
		setAir(world, pos, 0, 0, -1);
		setAir(world, pos, 1, 0, -1);
		setAir(world, pos, 0, 1, 0);
		setAir(world, pos, 1, 1, 0);
		setAir(world, pos, 1, 1, 1);
		setAir(world, pos, 0, 1, 1);
		setAir(world, pos, -1, 1, 1);
		setAir(world, pos, -1, 1, 0);
		setAir(world, pos, -1, 1, -1);
		setAir(world, pos, 0, 1, -1);
		setAir(world, pos, 1, 1, -1);
		setAir(world, pos, 0, 2, 0);
		setAir(world, pos, 1, 2, 0);
		setAir(world, pos, 1, 2, 1);
		setAir(world, pos, 0, 2, 1);
		setAir(world, pos, -1, 2, 1);
		setAir(world, pos, -1, 2, 0);
		setAir(world, pos, -1, 2, -1);
		setAir(world, pos, 0, 2, -1);
		setAir(world, pos, 1, 2, -1);
		
		world.removeTileEntity(pos);
	}
	
	private BlockPos getPos(BlockPos pos, int x, int y, int z) {
		return new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
	}
	
	private boolean isAir(World world, BlockPos pos, int x, int y, int z) {
		Material mat = world.getBlockState(getPos(pos, x, y, z)).getMaterial();
		return mat == Material.AIR || mat == Material.FIRE || mat == Material.WATER || mat == Material.VINE || mat == Material.SNOW;
	}
	
	private void setAir(World world, BlockPos pos, int x, int y, int z) {
		world.destroyBlock(getPos(pos, x, y, z), false);
	}
	
	private void setSide(World world, BlockPos pos, int x, int y, int z) {
		IBlockState dummy = NCBlocks.fusion_dummy_side.getDefaultState();
		world.setBlockState(getPos(pos, x, y, z), dummy);
	}
	
	private void setTop(World world, BlockPos pos, int x, int y, int z) {
		IBlockState dummy = NCBlocks.fusion_dummy_top.getDefaultState();
		world.setBlockState(getPos(pos, x, y, z), dummy);
	}
	
	public static void setState(World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		keepInventory = true;
		world.setBlockState(pos, NCBlocks.fusion_core.getDefaultState(), 3);
		keepInventory = false;
		
		if (tile != null) {
			tile.validate();
			world.setTileEntity(pos, tile);
		}
	}
	
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null) {
			if (tile instanceof TileFusionCore) return (int) MathHelper.clamp(0.01*16D*((TileFusionCore)tile).efficiency, 0, 15);
		}
		return Container.calcRedstone(world.getTileEntity(pos));
	}
}
