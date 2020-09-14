package nc.multiblock.fission.tile.manager;

import static nc.block.property.BlockProperties.FACING_ALL;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.longs.*;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.tile.TileFissionPart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

public abstract class TileFissionManager<MANAGER extends TileFissionManager<MANAGER, LISTENER>, LISTENER extends IFissionManagerListener<MANAGER, LISTENER>> extends TileFissionPart implements ITickable, IFissionManager<MANAGER, LISTENER> {
	
	protected final Class<MANAGER> managerClass;
	protected LongSet listenerPosSet = new LongOpenHashSet();
	public boolean refreshListenersFlag = false;
	
	public TileFissionManager(Class<MANAGER> managerClass) {
		super(CuboidalPartPositionType.WALL);
		this.managerClass = managerClass;
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
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
	
	@Override
	public LongSet getListenerPosSet() {
		return listenerPosSet;
	}
	
	// Ticking
	
	@Override
	public void update() {
		if (!world.isRemote) {
			if (refreshListenersFlag) {
				refreshListeners(false);
			}
		}
	}
	
	@Override
	public void markDirty() {
		refreshListenersFlag = true;
		super.markDirty();
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		
		int count = 0;
		for (long posLong : listenerPosSet) {
			nbt.setLong("listenerPos" + count, posLong);
			count++;
		}
		
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		
		for (String key : nbt.getKeySet()) {
			if (key.startsWith("listenerPos")) {
				listenerPosSet.add(nbt.getLong(key));
			}
		}
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		return super.getCapability(capability, side);
	}
}
