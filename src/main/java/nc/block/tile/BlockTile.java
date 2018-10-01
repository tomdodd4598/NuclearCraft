package nc.block.tile;

import javax.annotation.Nullable;

import nc.NuclearCraft;
import nc.block.NCBlock;
import nc.init.NCItems;
import nc.tile.IGui;
import nc.tile.fluid.ITileFluid;
import nc.tile.processor.IUpgradable;
import nc.util.FluidHelper;
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
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class BlockTile extends NCBlock implements ITileEntityProvider {
	
	protected static boolean keepInventory;

	public BlockTile(String name, Material material) {
		super(name, material);
		hasTileEntity = true;
		setDefaultState(blockState.getBaseState());
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) return false;
		
		if (hand != EnumHand.MAIN_HAND) return false;
		
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof IUpgradable && player.getHeldItemMainhand().isItemEqual(new ItemStack(NCItems.upgrade, 1, 0))) {
			int speedSlot = ((IUpgradable) tile).getSpeedUpgradeSlot();
			IItemHandler inv = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
			
			if (inv != null && inv.isItemValid(speedSlot, player.getHeldItemMainhand())) {
				if (player.isSneaking()) {
					player.setHeldItem(EnumHand.MAIN_HAND, inv.insertItem(speedSlot, player.getHeldItemMainhand(), false));
					return true;
				} else {
					if (inv.insertItem(speedSlot, new ItemStack(NCItems.upgrade, 1, 0), false).isEmpty()) {
						player.getHeldItemMainhand().shrink(1);
						return true;
					}
				}
			}
		}
		
		if (player.isSneaking()) return false;
		
		if (!(tile instanceof ITileFluid) && !(tile instanceof IGui)) return false;
		if (tile instanceof ITileFluid && !(tile instanceof IGui) && FluidUtil.getFluidHandler(player.getHeldItem(hand)) == null) return false;
		
		if (!world.isRemote && tile instanceof ITileFluid) {
			ITileFluid tileFluid = (ITileFluid) tile;
			if (tileFluid.getTanks() != null) {
				boolean accessedTanks = FluidHelper.accessTanks(player, hand, tileFluid.getTanks());
				if (accessedTanks) return true;
			}
		}
		if (tile instanceof IGui) {
			if (world.isRemote) {
				onGuiOpened(world, pos);
				return true;
			} else {
				onGuiOpened(world, pos);
				IGui tileGui = (IGui) tile;
				FMLNetworkHandler.openGui(player, NuclearCraft.instance, tileGui.getGuiID(), world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		else return false;
		
		return true;
	}
	
	public void onGuiOpened(World world, BlockPos pos) {
		
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
}
