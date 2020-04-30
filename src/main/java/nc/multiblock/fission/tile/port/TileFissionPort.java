package nc.multiblock.fission.tile.port;

import static nc.block.property.BlockProperties.AXIS_ALL;
import static nc.util.BlockPosHelper.DEFAULT_NON;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.cuboidal.PartPosition;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.tile.TileFissionPart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

public abstract class TileFissionPort<PORT extends TileFissionPort<PORT, TARGET>, TARGET extends IFissionPortTarget<PORT, TARGET>> extends TileFissionPart implements IFissionPort<PORT, TARGET> {
	
	protected final Class<PORT> portClass;
	protected BlockPos masterPortPos = DEFAULT_NON;
	protected PORT masterPort = null;
	protected ObjectSet<TARGET> targets = new ObjectOpenHashSet<>();
	public boolean refreshPartsFlag = false;
	
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
		
		//TODO - temporary ports
		/*if (!getWorld().isRemote && !DEFAULT_NON.equals(masterPortPos)) {
			PORT master = masterPort;
			clearMasterPort();
			master.shiftStacks(this);
		}*/
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
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
	
	@Override
	public ObjectSet<TARGET> getTargets() {
		return targets;
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
		if (masterPort == null) masterPortPos = DEFAULT_NON;
	}
	
	//TODO - temporary ports
	@Override
	public void refreshTargets() {
		refreshPartsFlag = false;
		if (isMultiblockAssembled()) {
			boolean refresh = false;
			for (TARGET part : targets) {
				if (part.onPortRefresh()) refresh = true;
			}
			if (refresh) getMultiblock().refreshFlag = true;
		}
	}
	
	// Ticking
	
	@Override
	public void update() {
		super.update();
		if (!world.isRemote) {
			if (refreshPartsFlag) {
				refreshTargets();
			}
		}
	}
	
	@Override
	public void markDirty() {
		refreshPartsFlag = true;
		super.markDirty();
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		//nbt.setLong("masterPortPos", masterPortPos.toLong());
		nbt.setString("axis", axis.getName());
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		//masterPortPos = BlockPos.fromLong(nbt.getLong("masterPortPos"));
		Axis axis = Axis.byName(nbt.getString("axis"));
		this.axis = axis == null ? Axis.Z : axis;
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
