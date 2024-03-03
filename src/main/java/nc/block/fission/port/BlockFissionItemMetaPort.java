package nc.block.fission.port;

import nc.enumm.IBlockMetaEnum;
import nc.multiblock.fission.FissionReactor;
import nc.tile.ITileGui;
import nc.tile.fission.port.*;
import nc.tile.inventory.ITileFilteredInventory;
import nc.util.Lang;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

public abstract class BlockFissionItemMetaPort<PORT extends TileFissionItemPort<PORT, TARGET>, TARGET extends IFissionPortTarget<PORT, TARGET> & ITileFilteredInventory, T extends Enum<T> & IStringSerializable & IBlockMetaEnum> extends BlockFissionMetaPort<PORT, TARGET, T> {
	
	public BlockFissionItemMetaPort(Class<PORT> portClass, Class<T> enumm, PropertyEnum<T> property) {
		super(portClass, enumm, property);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (hand != EnumHand.MAIN_HAND || player.isSneaking()) {
			return false;
		}
		
		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(pos);
			if (portClass.isInstance(tile)) {
				PORT port = portClass.cast(tile);
				FissionReactor reactor = port.getMultiblock();
				if (reactor != null) {
					ItemStack heldStack = player.getHeldItem(hand);
					if (port.canModifyFilter(0) && port.getInventoryStacks().get(0).isEmpty() && !heldStack.isItemEqual(port.getFilterStacks().get(0)) && port.isItemValidForSlotInternal(0, heldStack)) {
						player.sendMessage(new TextComponentString(Lang.localize("message.nuclearcraft.filter") + " " + TextFormatting.BOLD + heldStack.getDisplayName()));
						ItemStack filter = heldStack.copy();
						filter.setCount(1);
						port.getFilterStacks().set(0, filter);
						port.onFilterChanged(0);
					}
					else {
						if (tile instanceof ITileGui<?, ?, ?> tileGui) {
							tileGui.openGui(world, pos, player);
						}
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
			if (portClass.isInstance(tile)) {
				PORT port = portClass.cast(tile);
				dropItems(world, pos, port.getInventoryStacksInternal());
				// world.updateComparatorOutputLevel(pos, this);
				// FissionReactor reactor = port.getMultiblock();
				// world.removeTileEntity(pos);
				/*if (reactor != null) {
					reactor.getLogic().refreshPorts();
				}*/
			}
		}
		// super.breakBlock(world, pos, state);
		world.removeTileEntity(pos);
	}
}
