package nc.multiblock.saltFission.block;

import nc.Global;
import nc.block.NCBlock;
import nc.multiblock.IMultiblockPart;
import nc.multiblock.MultiblockControllerBase;
import nc.multiblock.validation.ValidationError;
import nc.tab.NCTabs;
import nc.util.Lang;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockSaltFissionPartBase extends NCBlock implements ITileEntityProvider {
	
	protected static boolean keepInventory;
	
	public BlockSaltFissionPartBase(String name) {
		super(name, Material.IRON);
		this.hasTileEntity = true;
		setDefaultState(blockState.getBaseState());
		setCreativeTab(NCTabs.TAB_SALT_FISSION_BLOCKS);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState();
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		world.setBlockState(pos, state, 2);
	}
	
	protected boolean rightClickOnPart(World world, BlockPos pos, EntityPlayer player) {
		return rightClickOnPart(world, pos, player, false);
	}
	
	protected boolean rightClickOnPart(World world, BlockPos pos, EntityPlayer player, boolean prioritiseGui) {
		if (!world.isRemote) {
			if (player.getHeldItemMainhand().isEmpty()) {
				TileEntity tile = world.getTileEntity(pos);
				if (tile instanceof IMultiblockPart) {
					MultiblockControllerBase controller = ((IMultiblockPart) tile).getMultiblockController();
					if (controller != null) {
						ValidationError e = controller.getLastError();
						if (e != null) {
							player.sendMessage(e.getChatMessage());
							return true;
						}
					} else {
						player.sendMessage(new TextComponentString(Lang.localise(Global.MOD_ID + ".multiblock_validation.no_controller")));
						return true;
					}
				}
			}
		}
		return prioritiseGui;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (!keepInventory) {
			TileEntity tileentity = world.getTileEntity(pos);
			
			if (tileentity instanceof IInventory) {
				dropItems(world, pos, (IInventory) tileentity);
				world.updateComparatorOutputLevel(pos, this);
			}
		}
		super.breakBlock(world, pos, state);
		world.removeTileEntity(pos);
	}
	
	public void dropItems(World world, BlockPos pos, IInventory tileentity) {
		InventoryHelper.dropInventoryItems(world, pos, tileentity);
	}
	
	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
		super.eventReceived(state, worldIn, pos, id, param);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
	}
	
	public static abstract class Transparent extends BlockSaltFissionPartBase {
		
		protected final boolean smartRender;
		
		public Transparent(String name, boolean smartRender) {
			super(name);
			setHardness(1.5F);
			setResistance(10F);
			this.smartRender = smartRender;
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public BlockRenderLayer getBlockLayer() {
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
