package nc.multiblock.saltFission.tile;

import nc.config.NCConfig;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.saltFission.SaltFissionReactor;
import nc.multiblock.saltFission.block.BlockSaltFissionRedstonePort;
import net.minecraft.nbt.NBTTagCompound;

public class TileSaltFissionRedstonePort extends TileSaltFissionPartBase {
	
	public int comparatorSignal = 0;
	
	protected int updateCount;
	
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
	public void update() {
		super.update();
		tickTile();
		if (updateCount == 0) updateBlock();
	}
	
	public void tickTile() {
		updateCount++; updateCount %= NCConfig.machine_update_rate / 4;
	}
	
	public void updateBlock() {
		if (getBlockType() instanceof BlockSaltFissionRedstonePort) {
			if (((BlockSaltFissionRedstonePort)getBlockType()).setActiveState(getBlockState(pos), world, pos, isRedstonePowered())) {
				world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
			}
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
