package nc.multiblock.fission.block;

import static nc.block.property.BlockProperties.AXIS_ALL;

import nc.NuclearCraft;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.tile.TileFissionPort;
import nc.util.Lang;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class BlockFissionPort extends BlockFissionPart {

	public BlockFissionPort() {
		super();
		setDefaultState(blockState.getBaseState().withProperty(AXIS_ALL, EnumFacing.Axis.Z));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, AXIS_ALL);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(AXIS_ALL, EnumFacing.Axis.values()[meta]);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(AXIS_ALL).ordinal();
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileFissionPort();
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(AXIS_ALL, EnumFacing.getDirectionFromEntityLiving(pos, placer).getAxis());
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) return false;
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) return false;
		
		if (!world.isRemote) {
			if (world.getTileEntity(pos) instanceof TileFissionPort) {
				TileFissionPort port = (TileFissionPort) world.getTileEntity(pos);
				if (port.getMultiblock() != null /*&& port.getMultiblock().isAssembled()*/) {
					ItemStack heldStack = player.getHeldItem(hand);
					if (port.getInventoryStacks().get(0).isEmpty() && !heldStack.isItemEqual(port.getFilterStacks().get(0)) && port.isItemValidForSlotRaw(0, heldStack)) {
						player.sendMessage(new TextComponentString(Lang.localise("message.nuclearcraft.filter") + " " + TextFormatting.BOLD + Lang.localise(heldStack.getTranslationKey() + ".name")));
						ItemStack filter = heldStack.copy();
						filter.setCount(1);
						port.getFilterStacks().set(0, filter);
						port.onFilterChanged(0);
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
			if (tile instanceof TileFissionPort) {
				TileFissionPort port = (TileFissionPort) tile;
				dropItems(world, pos, port.getInventoryStacksInternal());
				//world.updateComparatorOutputLevel(pos, this);
				FissionReactor reactor = port.getMultiblock();
				world.removeTileEntity(pos);
				if (reactor != null) {
					reactor.getLogic().refreshPorts();
				}
			}
		}
		//super.breakBlock(world, pos, state);
		world.removeTileEntity(pos);
	}
}
