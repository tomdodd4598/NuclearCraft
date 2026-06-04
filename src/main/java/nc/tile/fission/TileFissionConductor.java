package nc.tile.fission;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;
import java.util.Iterator;

public class TileFissionConductor extends TileFissionPart implements IFissionComponent {
	
	private FissionCluster cluster = null;
	private long heat = 0L;
	
	public TileFissionConductor() {
		super(CuboidalPartPositionType.INTERIOR);
	}
	
	@Override
	public void onMachineAssembled(FissionReactor multiblock) {
		doStandardNullControllerResponse(multiblock);
		super.onMachineAssembled(multiblock);
	}
	
	// IFissionComponent
	
	@Override
	public @Nullable FissionCluster getCluster() {
		return cluster;
	}
	
	@Override
	public void setClusterInternal(@Nullable FissionCluster cluster) {
		this.cluster = cluster;
	}
	
	@Override
	public boolean isValidHeatConductor(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
		return true;
	}
	
	@Override
	public boolean isFunctional(boolean simulate) {
		return false;
	}
	
	@Override
	public void resetStats() {}
	
	@Override
	public boolean isClusterRoot() {
		return false;
	}
	
	@Override
	public long getHeatStored() {
		return heat;
	}
	
	@Override
	public void setHeatStored(long heat) {
		this.heat = heat;
	}
	
	@Override
	public void onClusterMeltdown(Iterator<IFissionComponent> componentIterator) {
	
	}
	
	@Override
	public boolean isNullifyingSources(EnumFacing side, boolean simulate) {
		return false;
	}
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setLong("clusterHeat", heat);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		heat = nbt.getLong("clusterHeat");
	}
	
	// OpenComputers
	
	@Override
	public String getOCKey() {
		return "conductor";
	}
	
	@Override
	public Object getOCInfo() {
		return new Object2ObjectLinkedOpenHashMap<String, Object>();
	}
}
