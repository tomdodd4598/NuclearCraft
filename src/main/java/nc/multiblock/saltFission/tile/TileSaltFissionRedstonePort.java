package nc.multiblock.saltFission.tile;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.saltFission.SaltFissionReactor;
import nc.multiblock.saltFission.block.BlockSaltFissionRedstonePort;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileSaltFissionRedstonePort extends TileSaltFissionPartBase {
	
	public int comparatorSignal = 0;
	
	public TileSaltFissionRedstonePort() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public void onMachineAssembled(SaltFissionReactor controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		if (getWorld().isRemote) return;
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		if (getWorld().isRemote) return;
		//getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()), 2);
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}
	
	@Override
	public void onBlockNeighborChanged(IBlockState state, World world, BlockPos pos, BlockPos fromPos) {
		super.onBlockNeighborChanged(state, world, pos, fromPos);
		updateBlockState();
		if (getMultiblock() != null) getMultiblock().setIsReactorOn();
	}
	
	public void updateBlockState() {
		if (getBlockType() instanceof BlockSaltFissionRedstonePort) {
			((BlockSaltFissionRedstonePort)getBlockType()).setState(getIsRedstonePowered(), this);
			world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
		}
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setInteger("comparatorSignal", comparatorSignal);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		comparatorSignal = nbt.getInteger("comparatorSignal");
	}
}
