package nc.multiblock.block;

import javax.annotation.Nullable;

import nc.Global;
import nc.block.NCBlock;
import nc.multiblock.Multiblock;
import nc.multiblock.MultiblockValidationError;
import nc.multiblock.tile.ITileMultiblockPart;
import nc.render.BlockHighlightTracker;
import nc.tile.fluid.ITileFluid;
import nc.tile.inventory.ITileInventory;
import nc.util.FluidHelper;
import nc.util.Lang;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockMultiblockPart extends NCBlock implements ITileEntityProvider {
	
	public BlockMultiblockPart(Material material, CreativeTabs tab) {
		super(material);
		hasTileEntity = true;
		setDefaultState(blockState.getBaseState());
		setCreativeTab(tab);
		canCreatureSpawn = false;
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState();
	}
	
	protected boolean rightClickOnPart(World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing facing) {
		return rightClickOnPart(world, pos, player, hand, facing, false);
	}
	
	protected boolean rightClickOnPart(World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing facing, boolean prioritiseGui) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof ITileFluid && FluidUtil.getFluidHandler(player.getHeldItem(hand)) != null) {
			ITileFluid tileFluid = (ITileFluid) tile;
			if (FluidHelper.accessTanks(player, hand, facing, tileFluid)) return true;
		}
		if (!world.isRemote && player.getHeldItem(hand).isEmpty()) {
			if (tile instanceof ITileMultiblockPart) {
				Multiblock controller = ((ITileMultiblockPart) tile).getMultiblock();
				if (controller != null) {
					MultiblockValidationError e = controller.getLastError();
					if (e != null) {
						e = e.updatedError(world);
						player.sendMessage(e.getChatMessage());
						if (e.getErrorPos() != null) BlockHighlightTracker.sendPacket((EntityPlayerMP) player, e.getErrorPos(), 5000);
						return true;
					}
				} else {
					player.sendMessage(new TextComponentString(Lang.localise(Global.MOD_ID + ".multiblock_validation.no_controller")));
					return true;
				}
			}
		}
		return prioritiseGui;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (!keepInventory) {
			TileEntity tileentity = world.getTileEntity(pos);
			
			IInventory inv = null;
			if (tileentity instanceof IInventory) {
				inv = (IInventory) tileentity;
			}
			else if (tileentity instanceof ITileInventory) {
				inv = ((ITileInventory)tileentity).getInventory();
			}
			
			if (inv != null) {
				dropItems(world, pos, inv);
				world.updateComparatorOutputLevel(pos, this);
			}
		}
		super.breakBlock(world, pos, state);
		world.removeTileEntity(pos);
	}
	
	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity tile, ItemStack stack) {
		super.harvestBlock(world, player, pos, state, tile, stack);
		world.setBlockToAir(pos);
	}
	
	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
		super.eventReceived(state, worldIn, pos, id, param);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
	}
	
	public static abstract class Transparent extends BlockMultiblockPart {
		
		protected final boolean smartRender;
		
		public Transparent(Material material, CreativeTabs tab, boolean smartRender) {
			super(material, tab);
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
