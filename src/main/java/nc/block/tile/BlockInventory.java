package nc.block.tile;

import nc.NuclearCraft;
import nc.block.NCBlock;
import nc.tile.IGui;
import nc.tile.fluid.ITileFluid;
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

public abstract class BlockInventory extends NCBlock implements ITileEntityProvider {
	
	protected static boolean keepInventory;

	public BlockInventory(String name, Material material) {
		super(name, material);
		this.hasTileEntity = true;
		setDefaultState(blockState.getBaseState());
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState();
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		world.setBlockState(pos, state, 2);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) return false;
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) return false;
		
		TileEntity tileentity = world.getTileEntity(pos);
		if (!(tileentity instanceof ITileFluid) && !(tileentity instanceof IGui)) return false;
		if (tileentity instanceof ITileFluid && !(tileentity instanceof IGui) && FluidUtil.getFluidHandler(player.getHeldItem(hand)) == null) return false;
		
		if (world.isRemote) return true;
		
		if (tileentity instanceof ITileFluid) {
			ITileFluid tileFluid = (ITileFluid) tileentity;
			if (tileFluid.getTanks() != null) {
				boolean accessedTanks = FluidHelper.accessTankArray(player, hand, tileFluid.getTanks());
				if (accessedTanks) return true;
			}
		}
		if (tileentity instanceof IGui) {
			IGui tileGui = (IGui) tileentity;
			FMLNetworkHandler.openGui(player, NuclearCraft.instance, tileGui.getGuiID(), world, pos.getX(), pos.getY(), pos.getZ());
		}
		else return false;
		
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
