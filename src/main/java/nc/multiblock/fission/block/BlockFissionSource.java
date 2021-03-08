package nc.multiblock.fission.block;

import static nc.block.property.BlockProperties.*;

import javax.annotation.Nullable;

import nc.block.tile.IActivatable;
import nc.multiblock.fission.tile.TileFissionSource;
import nc.multiblock.fission.tile.TileFissionSource.PrimingTargetInfo;
import nc.render.BlockHighlightTracker;
import nc.util.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.*;

public abstract class BlockFissionSource extends BlockFissionPart implements IActivatable {
	
	public BlockFissionSource() {
		super();
		setDefaultState(getDefaultState().withProperty(FACING_ALL, EnumFacing.NORTH).withProperty(ACTIVE, Boolean.valueOf(false)));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING_ALL, ACTIVE);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.byIndex(meta & 7);
		return getDefaultState().withProperty(FACING_ALL, enumfacing).withProperty(ACTIVE, Boolean.valueOf((meta & 8) > 0));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		int i = state.getValue(FACING_ALL).getIndex();
		if (state.getValue(ACTIVE).booleanValue()) {
			i |= 8;
		}
		return i;
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(FACING_ALL, EnumFacing.getDirectionFromEntityLiving(pos, placer));
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
		BlockHelper.setDefaultFacing(world, pos, state, FACING_ALL);
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileFissionSource) {
			TileFissionSource source = (TileFissionSource) tile;
			source.facing = state.getValue(FACING_ALL);
			world.setBlockState(pos, state.withProperty(FACING_ALL, source.facing).withProperty(ACTIVE, source.getIsRedstonePowered()), 2);
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null || hand != EnumHand.MAIN_HAND || player.isSneaking()) {
			return false;
		}
		
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileFissionSource) {
			TileFissionSource source = (TileFissionSource) tile;
			if (!world.isRemote) {
				PrimingTargetInfo targetInfo = source.getPrimingTarget(true);
				if (targetInfo == null) {
					player.sendMessage(new TextComponentString(Lang.localise("nuclearcraft.multiblock.fission_reactor_source.no_target")));
				}
				else {
					BlockPos p = targetInfo.fuelComponent.getTilePos();
					BlockHighlightTracker.sendPacket((EntityPlayerMP) player, p, 5000);
					player.sendMessage(new TextComponentString(Lang.localise("nuclearcraft.multiblock.fission_reactor_source.target", p.getX(), p.getY(), p.getZ(), world.getBlockState(p).getBlock().getLocalizedName())));
				}
			}
			return true;
		}
		
		return rightClickOnPart(world, pos, player, hand, facing);
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
		return side != null;
	}
}
