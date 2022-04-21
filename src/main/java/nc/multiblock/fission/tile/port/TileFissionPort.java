package nc.multiblock.fission.tile.port;

import static nc.block.property.BlockProperties.AXIS_ALL;
import static nc.util.PosHelper.DEFAULT_NON;

import javax.annotation.*;

import it.unimi.dsi.fastutil.objects.*;
import nc.multiblock.cuboidal.*;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.tile.TileFissionPart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

public abstract class TileFissionPort<PORT extends TileFissionPort<PORT, TARGET>, TARGET extends IFissionPortTarget<PORT, TARGET>> extends TileFissionPart implements ITickable, IFissionPort<PORT, TARGET> {
	
	protected final Class<PORT> portClass;
	protected BlockPos masterPortPos = DEFAULT_NON;
	protected PORT masterPort = null;
	protected ObjectSet<TARGET> targets = new ObjectOpenHashSet<>();
	public boolean refreshTargetsFlag = false;
	
	public Axis axis = Axis.Z;
	
	public TileFissionPort(Class<PORT> portClass) {
		super(CuboidalPartPositionType.WALL);
		this.portClass = portClass;
	}
	
	@Override
	public void onMachineAssembled(FissionReactor controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		if (!getWorld().isRemote && getPartPosition().getFacing() != null) {
			getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()).withProperty(AXIS_ALL, getPartPosition().getFacing().getAxis()), 2);
		}
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
	}
	
	@Override
	public @Nonnull PartPosition getPartPosition() {
		PartPosition partPos = super.getPartPosition();
		if (partPos.getFacing() != null) {
			axis = partPos.getFacing().getAxis();
		}
		return partPos;
	}
	
	@Override
	public ObjectSet<TARGET> getTargets() {
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getTargets() : targets;
	}
	
	@Override
	public BlockPos getMasterPortPos() {
		return masterPortPos;
	}
	
	@Override
	public void setMasterPortPos(BlockPos pos) {
		masterPortPos = pos;
	}
	
	@Override
	public void clearMasterPort() {
		masterPort = null;
		masterPortPos = DEFAULT_NON;
	}
	
	@Override
	public void refreshMasterPort() {
		masterPort = getMultiblock() == null ? null : getMultiblock().getPartMap(portClass).get(masterPortPos.toLong());
		if (masterPort == null) {
			masterPortPos = DEFAULT_NON;
		}
	}
	
	// TODO - temporary ports
	@Override
	public void refreshTargets() {
		refreshTargetsFlag = false;
		if (isMultiblockAssembled()) {
			boolean refresh = false;
			for (TARGET part : getTargets()) {
				if (part.onPortRefresh()) {
					refresh = true;
				}
			}
			if (refresh) {
				getMultiblock().refreshFlag = true;
			}
		}
	}
	
	@Override
	public void setRefreshTargetsFlag(boolean refreshTargetsFlag) {
		this.refreshTargetsFlag = refreshTargetsFlag;
	}
	
	// Ticking
	
	@Override
	public void update() {
		if (!world.isRemote) {
			if (refreshTargetsFlag) {
				refreshTargets();
			}
		}
	}
	
	@Override
	public void markDirty() {
		refreshTargetsFlag = true;
		super.markDirty();
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		// nbt.setLong("masterPortPos", masterPortPos.toLong());
		nbt.setString("axis", axis.getName());
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		// masterPortPos = BlockPos.fromLong(nbt.getLong("masterPortPos"));
		Axis ax = Axis.byName(nbt.getString("axis"));
		this.axis = ax == null ? Axis.Z : ax;
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
