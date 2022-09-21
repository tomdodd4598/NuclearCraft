package nc.block.tile;

import javax.annotation.*;

import nc.block.NCBlock;
import nc.init.NCItems;
import nc.tile.ITileGui;
import nc.tile.fluid.ITileFluid;
import nc.tile.processor.*;
import nc.util.BlockHelper;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.*;

public abstract class BlockTile extends NCBlock implements ITileEntityProvider {
	
	public BlockTile(Material material) {
		super(material);
		hasTileEntity = true;
		setDefaultState(blockState.getBaseState());
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState();
	}
	
	// TODO move this logic into tile entities with ITile method
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null || hand != EnumHand.MAIN_HAND) {
			return false;
		}
		
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof IBasicUpgradeProcessor) {
			if (installUpgrade(tile, ((IBasicUpgradeProcessor) tile).getSpeedUpgradeSlot(), player, hand, facing, new ItemStack(NCItems.upgrade, 1, 0))) {
				return true;
			}
			if (installUpgrade(tile, ((IBasicUpgradeProcessor) tile).getEnergyUpgradeSlot(), player, hand, facing, new ItemStack(NCItems.upgrade, 1, 1))) {
				return true;
			}
		}
		
		if (player.isSneaking()) {
			return false;
		}
		
		boolean isTileFluid = tile instanceof ITileFluid, isTileGui = tile instanceof ITileGui;
		if (!isTileFluid && !isTileGui) {
			return false;
		}
		if (isTileFluid && !isTileGui && FluidUtil.getFluidHandler(player.getHeldItem(hand)) == null) {
			return false;
		}
		
		if (tile instanceof ITileFluid) {
			if (world.isRemote) {
				return true;
			}
			ITileFluid tileFluid = (ITileFluid) tile;
			boolean accessedTanks = BlockHelper.accessTanks(player, hand, facing, tileFluid);
			if (accessedTanks) {
				if (tile instanceof IProcessor) {
					((IProcessor<?>) tile).refreshRecipe();
					((IProcessor<?>) tile).refreshActivity();
				}
				return true;
			}
		}
		if (isTileGui) {
			if (world.isRemote) {
				onGuiOpened(world, pos);
				return true;
			}
			else {
				onGuiOpened(world, pos);
				if (tile instanceof IProcessor) {
					((IProcessor<?>) tile).refreshRecipe();
					((IProcessor<?>) tile).refreshActivity();
				}
				((ITileGui<?, ?>) tile).openGui(world, pos, player);
			}
		}
		else {
			return false;
		}
		
		return true;
	}
	
	protected boolean installUpgrade(TileEntity tile, int slot, EntityPlayer player, EnumHand hand, EnumFacing facing, @Nonnull ItemStack stack) {
		ItemStack held = player.getHeldItem(hand);
		if (held.isItemEqual(stack)) {
			IItemHandler inv = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
			
			if (inv != null && inv.isItemValid(slot, held)) {
				if (player.isSneaking()) {
					player.setHeldItem(EnumHand.MAIN_HAND, inv.insertItem(slot, held, false));
					return true;
				}
				else {
					if (inv.insertItem(slot, stack, false).isEmpty()) {
						player.getHeldItem(hand).shrink(1);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void onGuiOpened(World world, BlockPos pos) {
		
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (!keepInventory) {
			TileEntity tileentity = world.getTileEntity(pos);
			
			IInventory inv = null;
			if (tileentity instanceof IInventory) {
				inv = (IInventory) tileentity;
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
}
