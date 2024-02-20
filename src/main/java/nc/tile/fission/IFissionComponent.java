package nc.tile.fission;

import java.util.Iterator;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import nc.multiblock.fission.FissionCluster;
import nc.tile.fission.IFissionFuelComponent.*;
import nc.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public interface IFissionComponent extends IFissionPart {
	
	@Nullable FissionCluster getCluster();
	
	default FissionCluster newCluster(int id) {
		return new FissionCluster(getMultiblock(), id);
	}
	
	default void setCluster(@Nullable FissionCluster cluster) {
		if (cluster == null && getCluster() != null) {
			// getCluster().getComponentMap().remove(pos.toLong());
		}
		else if (cluster != null) {
			cluster.getComponentMap().put(getTilePos().toLong(), this);
		}
		setClusterInternal(cluster);
	}
	
	void setClusterInternal(@Nullable FissionCluster cluster);
	
	default boolean isClusterSearched() {
		return getCluster() != null;
	}
	
	/** Unlike {@link IFissionComponent#isFunctional}, includes checking logic during clusterSearch if necessary! */
    boolean isValidHeatConductor(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache);
	
	boolean isFunctional();
	
	void resetStats();
	
	boolean isClusterRoot();
	
	default void clusterSearch(Integer id, final Object2IntMap<IFissionComponent> clusterSearchCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		if (!isValidHeatConductor(componentFailCache, assumedValidCache)) {
			return;
		}
		
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
			cluster = newCluster(id);
			getMultiblock().getClusterMap().put(id.intValue(), cluster);
		}
		setCluster(cluster);
		
		for (EnumFacing dir : EnumFacing.VALUES) {
			BlockPos offPos = getTilePos().offset(dir);
			if (!getCluster().connectedToWall) {
				TileEntity part = getTileWorld().getTileEntity(offPos);
				if (part instanceof TileFissionPart && ((TileFissionPart) part).getPartPositionType().isGoodForWall()) {
					getCluster().connectedToWall = true;
					continue;
				}
			}
			IFissionComponent component = getMultiblock().getPartMap(IFissionComponent.class).get(offPos.toLong());
			if (component != null) {
				clusterSearchCache.put(component, id);
			}
		}
	}
	
	long getHeatStored();
	
	void setHeatStored(long heat);
	
	void onClusterMeltdown(Iterator<IFissionComponent> componentIterator);
	
	boolean isNullifyingSources(EnumFacing side);
	
	// Moderator Line
	
	default ModeratorBlockInfo getModeratorBlockInfo(EnumFacing dir, boolean validActiveModeratorPos) {
		return null;
	}
	
	/** The moderator line does not necessarily have to be complete! */
	default void onAddedToModeratorCache(ModeratorBlockInfo thisInfo) {}
	
	/** Called if and only if the moderator line from the fuel component searching in the dir direction is complete! */
	default void onModeratorLineComplete(ModeratorLine line, ModeratorBlockInfo thisInfo, EnumFacing dir) {}
	
	/** Called during cluster searches! */
	default boolean isActiveModerator() {
		return false;
	}
	
	// IMultitoolLogic
	
	@Override
    default boolean onUseMultitool(ItemStack multitool, EntityPlayer player, World world, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player.isSneaking()) {
			NBTTagCompound nbt = NBTHelper.getStackNBT(multitool);
			if (nbt != null) {
				nbt.setLong("componentPos", getTilePos().toLong());
				player.sendMessage(new TextComponentString(Lang.localize("info.nuclearcraft.multitool.copy_component_info")));
				return true;
			}
		}
		else {
			
		}
		return IFissionPart.super.onUseMultitool(multitool, player, world, facing, hitX, hitY, hitZ);
	}
}
