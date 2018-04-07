package nc.multiblock.fission.moltensalt.block;

import nc.Global;
import nc.block.NCBlock;
import nc.multiblock.IMultiblockPart;
import nc.multiblock.MultiblockControllerBase;
import nc.multiblock.validation.ValidationError;
import nc.proxy.CommonProxy;
import nc.util.Lang;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public abstract class BlockSaltFissionPartBase extends NCBlock implements ITileEntityProvider {
	
	protected static boolean keepInventory;
	
	public BlockSaltFissionPartBase(String name) {
		this(name, false, false);
	}
	
	public BlockSaltFissionPartBase(String name, boolean smartRender) {
		this(name, true, smartRender);
	}
	
	public BlockSaltFissionPartBase(String name, boolean transparent, boolean smartRender) {
		super(name, Material.IRON, transparent, smartRender);
		this.hasTileEntity = true;
		setDefaultState(blockState.getBaseState());
		setCreativeTab(CommonProxy.TAB_SALT_FISSION_BLOCKS);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState();
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		world.setBlockState(pos, state, 2);
	}
	
	public boolean rightClickOnPart(World world, BlockPos pos, EntityPlayer player) {
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
		return true;
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
}
