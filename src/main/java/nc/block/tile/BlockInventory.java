package nc.block.tile;

import nc.Global;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockInventory extends BlockContainer implements ITileEntityProvider {
	
	public static boolean keepInventory;

	public BlockInventory(String unlocalizedName, String registryName, Material material) {
		super(material);
		setUnlocalizedName(unlocalizedName);
		setRegistryName(new ResourceLocation(Global.MOD_ID, registryName));
		setDefaultState(blockState.getBaseState());
	}

	public TileEntity createNewTileEntity(World world, int meta) {
		return null;
	}
	
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
		return getDefaultState();
	}
	
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		world.setBlockState(pos, state, 2);
	}
	
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (!keepInventory) {
			TileEntity tileentity = world.getTileEntity(pos);
			
			if (tileentity instanceof IInventory) {
				dropItems(world, pos, (IInventory) tileentity);
				world.updateComparatorOutputLevel(pos, this);
			}
		}
		super.breakBlock(world, pos, state);
	}
	
	public void dropItems(World world, BlockPos pos, IInventory tileentity) {
		InventoryHelper.dropInventoryItems(world, pos, tileentity);
	}
	
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}
	
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		return Container.calcRedstone(world.getTileEntity(pos));
	}
	
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
		return false;
	}
}
