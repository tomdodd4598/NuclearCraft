package nc.block;

import java.util.List;

import com.google.common.collect.Lists;

import nc.block.tile.INBTDrop;
import nc.tile.ITile;
import nc.util.NCInventoryHelper;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.*;

public class NCBlock extends Block {
	
	protected boolean canSustainPlant = true;
	protected boolean canCreatureSpawn = true;
	
	protected static boolean keepInventory;
	
	public NCBlock(Material material) {
		super(material);
		setHarvestLevel("pickaxe", 0);
		setHardness(2F);
		setResistance(15F);
	}
	
	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
		return canSustainPlant && super.canSustainPlant(state, world, pos, direction, plantable);
	}
	
	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type) {
		return canCreatureSpawn && super.canCreatureSpawn(state, world, pos, type);
	}
	
	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
		return false;
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (this instanceof ITileEntityProvider) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof ITile) {
				((ITile) tile).onBlockNeighborChanged(state, world, pos, fromPos);
			}
		}
	}
	
	// Inventory
	
	public void dropItems(World world, BlockPos pos, IInventory inventory) {
		InventoryHelper.dropInventoryItems(world, pos, inventory);
	}
	
	public void dropItems(World world, BlockPos pos, List<ItemStack> stacks) {
		NCInventoryHelper.dropInventoryItems(world, pos, stacks);
	}
	
	// NBT Stuff
	
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		if (this instanceof INBTDrop && willHarvest) {
			onBlockHarvested(world, pos, state, player);
			return true;
		}
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		if (this instanceof INBTDrop) {
			return Lists.newArrayList(((INBTDrop) this).getNBTDrop(world, pos, state));
		}
		return super.getDrops(world, pos, state, fortune);
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, placer, stack);
		if (this instanceof INBTDrop && stack.hasTagCompound()) {
			((INBTDrop) this).readStackData(world, pos, placer, stack);
			world.notifyBlockUpdate(pos, state, state, 3);
		}
	}
	
	// Transparent Block
	
	public static class Transparent extends NCBlock {
		
		protected final boolean smartRender;
		
		public Transparent(Material material, boolean smartRender) {
			super(material);
			setHardness(1.5F);
			setResistance(10F);
			this.smartRender = smartRender;
			canSustainPlant = false;
			canCreatureSpawn = false;
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
		public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
			if (!smartRender) {
				return true;
			}
			
			IBlockState otherState = world.getBlockState(pos.offset(side));
			Block block = otherState.getBlock();
			
			if (state != otherState) {
				return true;
			}
			
			return block == this ? false : super.shouldSideBeRendered(state, world, pos, side);
		}
	}
}
