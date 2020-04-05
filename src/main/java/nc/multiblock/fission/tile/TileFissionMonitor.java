package nc.multiblock.fission.tile;

import static nc.block.property.BlockProperties.FACING_ALL;
import static nc.util.BlockPosHelper.DEFAULT_NON;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.block.BlockFissionMonitor;
import nc.util.Lang;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class TileFissionMonitor extends TileFissionPart {
	
	protected BlockPos componentPos = DEFAULT_NON;
	
	public TileFissionMonitor() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public void onMachineAssembled(FissionReactor controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		if (!getWorld().isRemote && getPartPosition().getFacing() != null) {
			getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()).withProperty(FACING_ALL, getPartPosition().getFacing()), 2);
		}
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		//if (getWorld().isRemote) return;
		//getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()), 2);
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
	
	public void updateBlockState(boolean isActive) {
		if (getBlockType() instanceof BlockFissionMonitor) {
			((BlockFissionMonitor)getBlockType()).setState(isActive, this);
			world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
		}
	}
	
	public BlockPos getComponentPos() {
		return componentPos;
	}
	
	// IMultitoolLogic
	
	@Override
	public boolean onUseMultitool(ItemStack multitoolStack, EntityPlayer player, World world, EnumFacing facing, float hitX, float hitY, float hitZ) {
		//TODO
		if (player.isSneaking()) {
			
		}
		else {
			NBTTagCompound nbt = multitoolStack.getTagCompound();
			if (nbt.hasKey("componentPos", 99)) {
				componentPos = BlockPos.fromLong(nbt.getLong("componentPos"));
				player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.connect_component_monitor")));
				return true;
			}
		}
		return super.onUseMultitool(multitoolStack, player, world, facing, hitX, hitY, hitZ);
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setLong("componentPos", componentPos.toLong());
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		componentPos = BlockPos.fromLong(nbt.getLong("componentPos"));
	}
}
