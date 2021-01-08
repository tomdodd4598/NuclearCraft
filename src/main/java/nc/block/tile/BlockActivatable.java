package nc.block.tile;

import static nc.block.property.BlockProperties.ACTIVE;

import nc.enumm.BlockEnums.ActivatableTileType;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public class BlockActivatable extends BlockTile implements IActivatable, ITileType {
	
	protected final ActivatableTileType type;
	
	public BlockActivatable(ActivatableTileType type) {
		super(Material.IRON);
		setDefaultState(blockState.getBaseState().withProperty(ACTIVE, Boolean.valueOf(false)));
		if (type.getCreativeTab() != null) {
			setCreativeTab(type.getCreativeTab());
		}
		this.type = type;
	}
	
	@Override
	public String getTileName() {
		return type.getName();
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return type.getTile();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(ACTIVE, Boolean.valueOf(meta == 1));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(ACTIVE).booleanValue() ? 1 : 0;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {ACTIVE});
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(ACTIVE, Boolean.valueOf(false));
	}
	
	public static class Transparent extends BlockActivatable {
		
		protected final boolean smartRender;
		
		public Transparent(ActivatableTileType type, boolean smartRender) {
			super(type);
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
			if (!smartRender) {
				return true;
			}
			
			IBlockState otherState = world.getBlockState(pos.offset(side));
			Block block = otherState.getBlock();
			
			if (blockState != otherState) {
				return true;
			}
			
			return block == this ? false : super.shouldSideBeRendered(blockState, world, pos, side);
		}
	}
}
