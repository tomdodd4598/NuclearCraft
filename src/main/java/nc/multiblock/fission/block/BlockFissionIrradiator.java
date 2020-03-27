package nc.multiblock.fission.block;

import nc.NuclearCraft;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.tile.TileFissionIrradiator;
import nc.util.Lang;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class BlockFissionIrradiator extends BlockFissionPart {
	
	public BlockFissionIrradiator() {
		super();
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileFissionIrradiator();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) return false;
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) return false;
		
		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileFissionIrradiator) {
				TileFissionIrradiator irradiator = (TileFissionIrradiator) tile;
				FissionReactor reactor = irradiator.getMultiblock();
				if (reactor != null) {
					ItemStack heldStack = player.getHeldItem(hand);
					if (irradiator.canModifyFilter(0) && irradiator.getInventoryStacks().get(0).isEmpty() && !heldStack.isItemEqual(irradiator.getFilterStacks().get(0)) && irradiator.isItemValidForSlotInternal(0, heldStack)) {
						player.sendMessage(new TextComponentString(Lang.localise("message.nuclearcraft.filter") + " " + TextFormatting.BOLD + Lang.localise(heldStack.getTranslationKey() + ".name")));
						ItemStack filter = heldStack.copy();
						filter.setCount(1);
						irradiator.getFilterStacks().set(0, filter);
						irradiator.onFilterChanged(0);
					}
					else {
						player.openGui(NuclearCraft.instance, 200, world, pos.getX(), pos.getY(), pos.getZ());
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
			if (tile instanceof TileFissionIrradiator) {
				TileFissionIrradiator irradiator = (TileFissionIrradiator) tile;
				dropItems(world, pos, irradiator.getInventoryStacksInternal());
				//world.updateComparatorOutputLevel(pos, this);
				//FissionReactor reactor = irradiator.getMultiblock();
				//world.removeTileEntity(pos);
				/*if (reactor != null) {
					reactor.getLogic().refreshPorts();
				}*/
			}
		}
		//super.breakBlock(world, pos, state);
		world.removeTileEntity(pos);
	}
}
