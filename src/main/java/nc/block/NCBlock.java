package nc.block;

import java.util.List;

import com.google.common.collect.Lists;

import nc.Global;
import nc.NuclearCraft;
import nc.block.tile.INBTDrop;
import nc.tile.ITile;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NCBlock extends Block {
	
	protected final boolean canCreatureSpawn;
	protected static boolean keepInventory;
	
	public NCBlock(String name, Material material) {
		this(name, material, false);
	}
	
	public NCBlock(String name, Material material, boolean canCreatureSpawn) {
		super(material);
		setTranslationKey(Global.MOD_ID + "." + name);
		if (NuclearCraft.regName) setRegistryName(new ResourceLocation(Global.MOD_ID, name));
		setHarvestLevel("pickaxe", 0);
		setHardness(2F);
		setResistance(15F);
		this.canCreatureSpawn = canCreatureSpawn;
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
			if (world.getTileEntity(pos) instanceof ITile) ((ITile)world.getTileEntity(pos)).onBlockNeighborChanged(state, world, pos, fromPos);
		}
	}
	
	// NBT Stuff
	
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		if (this instanceof INBTDrop && willHarvest) return true;
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		if (this instanceof INBTDrop) return Lists.newArrayList(((INBTDrop)this).getNBTDrop(world, pos, state));
		return super.getDrops(world, pos, state, fortune);
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack) {
		world.setBlockState(pos, state, 2);
		if (this instanceof INBTDrop && stack.hasTagCompound()) ((INBTDrop)this).readStackData(world, pos, player, stack);
		world.notifyBlockUpdate(pos, state, state, 3);
	}
	
	// Transparent Block
	
	public static class Transparent extends NCBlock {
		
		protected final boolean smartRender;
		
		public Transparent(String name, Material material, boolean smartRender) {
			super(name, material);
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
