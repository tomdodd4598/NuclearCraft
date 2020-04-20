package nc.block.tile;

import javax.annotation.Nullable;

import nc.NuclearCraft;
import nc.block.NCBlock;
import nc.init.NCItems;
import nc.tile.ITileGui;
import nc.tile.fluid.ITileFluid;
import nc.tile.inventory.ITileInventory;
import nc.tile.processor.IProcessor;
import nc.tile.processor.IUpgradable;
import nc.util.FluidHelper;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
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
	
	public BlockTile(Material material) {
		super(material);
		hasTileEntity = true;
		setDefaultState(blockState.getBaseState());
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null || hand != EnumHand.MAIN_HAND) return false;
		
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof IUpgradable) {
			if (installUpgrade(tile, ((IUpgradable) tile).getSpeedUpgradeSlot(), player, hand, facing, new ItemStack(NCItems.upgrade, 1, 0))) return true;
			if (installUpgrade(tile, ((IUpgradable) tile).getEnergyUpgradeSlot(), player, hand, facing, new ItemStack(NCItems.upgrade, 1, 1))) return true;
		}
		
		if (player.isSneaking()) return false;
		
		if (!(tile instanceof ITileFluid) && !(tile instanceof ITileGui)) return false;
		if (tile instanceof ITileFluid && !(tile instanceof ITileGui) && FluidUtil.getFluidHandler(player.getHeldItem(hand)) == null) return false;
		
		if (tile instanceof ITileFluid) {
			if (world.isRemote) return true;
			ITileFluid tileFluid = (ITileFluid) tile;
			boolean accessedTanks = FluidHelper.accessTanks(player, hand, facing, tileFluid);
			if (accessedTanks) {
				if (tile instanceof IProcessor) {
					((IProcessor) tile).refreshRecipe();
					((IProcessor) tile).refreshActivity();
				}
				return true;
			}
		}
		if (tile instanceof ITileGui) {
			if (world.isRemote) {
				onGuiOpened(world, pos);
				return true;
			} else {
				onGuiOpened(world, pos);
				if (tile instanceof IProcessor) {
					((IProcessor) tile).refreshRecipe();
					((IProcessor) tile).refreshActivity();
				}
				FMLNetworkHandler.openGui(player, NuclearCraft.instance, ((ITileGui) tile).getGuiID(), world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		else return false;
		
		return true;
	}
	
	protected boolean installUpgrade(TileEntity tile, int slot, EntityPlayer player, EnumHand hand, EnumFacing facing, ItemStack stack) {
		if (player.getHeldItem(hand).isItemEqual(stack)) {
			IItemHandler inv = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
			
			if (inv != null && inv.isItemValid(slot, player.getHeldItem(hand))) {
				if (player.isSneaking()) {
					player.setHeldItem(EnumHand.MAIN_HAND, inv.insertItem(slot, player.getHeldItem(hand), false));
					return true;
				} else {
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
}
