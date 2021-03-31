package nc.multiblock.fission.tile.manager;

import static nc.block.property.BlockProperties.FACING_ALL;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.longs.*;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.tile.TileFissionPart;
import nc.util.NBTHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
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
		NBTHelper.writeLongCollection(nbt, listenerPosSet, "listenerPosSet");
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		NBTHelper.readLongCollection(nbt, listenerPosSet, "listenerPosSet");
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
