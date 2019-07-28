package nc.block.tile;

import java.util.Random;

import nc.enumm.BlockEnums.ActivatableTileType;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockActivatable extends BlockTile implements IActivatable, ITileType {
	
	protected final boolean isActive;
	protected final ActivatableTileType type;
	
	public BlockActivatable(ActivatableTileType type, boolean isActive) {
		super(Material.IRON);
		this.isActive = isActive;
		if (!isActive) setCreativeTab(type.getTab());
		this.type = type;
	}
	
	@Override
	public String getTileName() {
		return type.getName() + (isActive ? "_active" : "_idle");
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return type.getTile();
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(type.getIdleBlock());
	}
	
	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		return new ItemStack(type.getIdleBlock());
	}
	
	@Override
	public void setState(boolean active, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		keepInventory = true;
		
		if (active) {
			world.setBlockState(pos, type.getActiveBlock().getDefaultState(), 3);
		} else {
			world.setBlockState(pos, type.getIdleBlock().getDefaultState(), 3);
		}
		keepInventory = false;
		
		if (tile != null) {
			tile.validate();
			world.setTileEntity(pos, tile);
		}
	}
	
	public static class Transparent extends BlockActivatable {
		
		protected final boolean smartRender;
		
		public Transparent(ActivatableTileType type, boolean isActive, boolean smartRender) {
			super(type, isActive);
			setHardness(1.5F);
			setResistance(10F);
			this.smartRender = smartRender;
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public BlockRenderLayer getRenderLayer() {
			return BlockRenderLayer.CUTOUT;
		}

		@Override
		public boolean isFullCube(IBlockState state) {
			return false;
		}
		
		@Override
		public boolean isOpaqueCube(IBlockState state) {
			return false;
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess world, BlockPos pos, EnumFacing side) {
			if (!smartRender) return true;
			
			IBlockState otherState = world.getBlockState(pos.offset(side));
			Block block = otherState.getBlock();
			
			if (blockState != otherState) return true;
			
			return block == this ? false : super.shouldSideBeRendered(blockState, world, pos, side);
		}
	}
}
