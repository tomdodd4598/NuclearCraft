package nc.block.fission;

import nc.multiblock.fission.FissionReactor;
import nc.tile.fission.TilePebbleFissionChamber;
import nc.util.Lang;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

public class BlockPebbleFissionChamber extends BlockFissionPart {
	
	public BlockPebbleFissionChamber() {
		super();
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TilePebbleFissionChamber();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) {
			return false;
		}
		
		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TilePebbleFissionChamber chamber) {
				FissionReactor reactor = chamber.getMultiblock();
				if (reactor != null) {
					ItemStack heldStack = player.getHeldItem(hand);
					if (chamber.canModifyFilter(0) && chamber.getInventoryStacks().get(0).isEmpty() && !heldStack.isItemEqual(chamber.getFilterStacks().get(0)) && chamber.isItemValidForSlotInternal(0, heldStack)) {
						player.sendMessage(new TextComponentString(Lang.localize("message.nuclearcraft.filter") + " " + TextFormatting.BOLD + heldStack.getDisplayName()));
						ItemStack filter = heldStack.copy();
						filter.setCount(1);
						chamber.getFilterStacks().set(0, filter);
						chamber.onFilterChanged(0);
					}
					else {
						chamber.openGui(world, pos, player);
					}
					return true;
				}
			}
		}
		return rightClickOnPart(world, pos, player, hand, facing, true);
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (!keepInventory) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TilePebbleFissionChamber chamber) {
				dropItems(world, pos, chamber.getInventoryStacksInternal());
			}
		}
		world.removeTileEntity(pos);
	}
	
}
