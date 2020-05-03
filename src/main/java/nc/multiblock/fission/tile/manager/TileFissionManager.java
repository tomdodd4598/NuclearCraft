package nc.multiblock.fission.tile.manager;

import static nc.block.property.BlockProperties.FACING_ALL;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.tile.TileFissionPart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

public abstract class TileFissionManager<MANAGER extends TileFissionManager<MANAGER, LISTENER>, LISTENER extends IFissionManagerListener<MANAGER, LISTENER>> extends TileFissionPart implements IFissionManager<MANAGER, LISTENER> {
	
	protected final Class<MANAGER> managerClass;
	//protected BlockPos masterManagerPos = DEFAULT_NON;
	//protected MANAGER masterManager = null;
	protected LongSet listenerPosCache = new LongOpenHashSet();
	protected ObjectSet<LISTENER> listeners = new ObjectOpenHashSet<>();
	public boolean refreshPartsFlag = false;
	
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
	public ObjectSet<LISTENER> getListeners() {
		return listeners;
	}
	
	public abstract void moveListenersFromCache();
	
	/*@Override
	public BlockPos getMasterManagerPos() {
		return masterManagerPos;
	}*/
	
	/*@Override
	public void setMasterManagerPos(BlockPos pos) {
		masterManagerPos = pos;
	}*/
	
	/*@Override
	public void clearMasterManager() {
		masterManager = null;
		masterManagerPos = DEFAULT_NON;
	}*/
	
	/*@Override
	public void refreshMasterManager() {
		masterManager = getMultiblock() == null ? null : getMultiblock().getPartMap(managerClass).get(masterManagerPos.toLong());
		if (masterManager == null) masterManagerPos = DEFAULT_NON;
	}*/
	
	// Ticking
	
	@Override
	public void update() {
		super.update();
		if (!world.isRemote) {
			if (refreshPartsFlag) {
				refreshListeners();
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
		//nbt.setLong("masterManagerPos", masterManagerPos.toLong());
		
		BlockPos listenerPos;
		IntList posCacheArrayX = new IntArrayList(), posCacheArrayY = new IntArrayList(), posCacheArrayZ = new IntArrayList();
		for (LISTENER listener : listeners) {
			listenerPos = listener.getTilePos();
			posCacheArrayX.add(listenerPos.getX());
			posCacheArrayY.add(listenerPos.getY());
			posCacheArrayZ.add(listenerPos.getZ());
		}
		for (long posLong : listenerPosCache) {
			BlockPos pos = BlockPos.fromLong(posLong);
			posCacheArrayX.add(pos.getX());
			posCacheArrayY.add(pos.getY());
			posCacheArrayZ.add(pos.getZ());
		}
		nbt.setIntArray("listenerPosCacheX", posCacheArrayX.toIntArray());
		nbt.setIntArray("listenerPosCacheY", posCacheArrayY.toIntArray());
		nbt.setIntArray("listenerPosCacheZ", posCacheArrayZ.toIntArray());
		
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		//masterManagerPos = BlockPos.fromLong(nbt.getLong("masterManagerPos"));
		
		listenerPosCache.clear();
		int[] posCacheArrayX = nbt.getIntArray("listenerPosCacheX"), posCacheArrayY = nbt.getIntArray("listenerPosCacheY"), posCacheArrayZ = nbt.getIntArray("listenerPosCacheZ");
		for (int i = 0; i < posCacheArrayX.length; i++) {
			listenerPosCache.add(new BlockPos(posCacheArrayX[i], posCacheArrayY[i], posCacheArrayZ[i]).toLong());
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
