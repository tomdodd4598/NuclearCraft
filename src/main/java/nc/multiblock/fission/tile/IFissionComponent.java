package nc.multiblock.fission.tile;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import nc.multiblock.fission.FissionCluster;
import nc.multiblock.fission.solid.tile.TileSolidFissionCell;
import nc.multiblock.fission.solid.tile.TileSolidFissionSink;
import nc.recipe.NCRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface IFissionComponent extends IFissionPart {
	
	public @Nullable FissionCluster getCluster();
	
	public default void setCluster(@Nullable FissionCluster cluster) {
		if (cluster == null && getCluster() != null) {
			//getCluster().getComponentMap().remove(pos.toLong());
		}
		else if (cluster != null) {
			cluster.getComponentMap().put(getTilePos().toLong(), this);
		}
		setClusterInternal(cluster);
	}
	
	public void setClusterInternal(@Nullable FissionCluster cluster);
	
	public default boolean isClusterSearched() {
		return getCluster() != null;
	}
	
	/** Unlike {@link IFissionComponent#isFunctional}, includes checking logic during clusterSearch if necessary! */
	public boolean isValidHeatConductor();
	
	public boolean isFunctional();
	
	public void resetStats();
	
	public default void clusterSearch(Integer id, final Object2IntMap<IFissionComponent> clusterSearchCache) {
		if (!isValidHeatConductor()) return;
		
		if (isClusterSearched()) {
			if (id != null) {
				getMultiblock().mergeClusters(id, getCluster());
			}
			return;
		}
		
		if (id == null) {
			id = getMultiblock().clusterCount++;
		}
		FissionCluster cluster = getMultiblock().getClusterMap().get(id.intValue());
		if (cluster == null) {
			cluster = new FissionCluster(getMultiblock(), id);
			getMultiblock().getClusterMap().put(id.intValue(), cluster);
		}
		setCluster(cluster);
		
		for (EnumFacing dir : EnumFacing.VALUES) {
			BlockPos offPos = getTilePos().offset(dir);
			if (!getCluster().connectedToWall && isWall(offPos)) {
				getCluster().connectedToWall = true;
				continue;
			}
			IFissionComponent component = getMultiblock().getPartMap(IFissionComponent.class).get(offPos.toLong());
			if (component != null) clusterSearchCache.put(component, id);
		}
	}
	
	public long getHeatStored();
	
	public void setHeatStored(long heat);
	
	public void onClusterMeltdown();
	
	// Helper methods
	
	public default boolean isActiveCell(BlockPos pos) {
		TileSolidFissionCell cell = getMultiblock().getPartMap(TileSolidFissionCell.class).get(pos.toLong());
		return cell == null ? false : cell.isFunctional();
	}
	
	public default boolean isActiveSink(BlockPos pos, String sinkName) {
		TileSolidFissionSink sink = getMultiblock().getPartMap(TileSolidFissionSink.class).get(pos.toLong());
		return sink == null ? false : sink.isFunctional() && sink.sinkName.equals(sinkName);
	}
	
	public default boolean isActiveModerator(BlockPos pos) {
		return getMultiblock().activeModeratorCache.contains(pos.toLong()) && blockRecipe(NCRecipes.fission_moderator, pos) != null;
	}
	
	public default boolean isActiveReflector(BlockPos pos) {
		return getMultiblock().activeReflectorCache.contains(pos.toLong()) && blockRecipe(NCRecipes.fission_reflector, pos) != null;
	}
	
	public default boolean isWall(BlockPos pos) {
		TileEntity part = getTileWorld().getTileEntity(pos);
		return part instanceof TileFissionPart && ((TileFissionPart)part).getPartPositionType().isGoodForWall();
	}
}
