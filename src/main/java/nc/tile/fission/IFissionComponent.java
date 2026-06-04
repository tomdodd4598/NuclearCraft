package nc.tile.fission;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import nc.multiblock.fission.FissionCluster;
import nc.tile.fission.IFissionFuelComponent.*;
import nc.util.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Iterator;

public interface IFissionComponent extends IFissionPart {
	
	@Nullable
	FissionCluster getCluster();
	
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
	
	/**
	 * Unlike {@link IFissionComponent#isFunctional}, includes checking logic during clusterSearch if necessary!
	 */
	boolean isValidHeatConductor(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate);
	
	boolean isFunctional(boolean simulate);
	
	void resetStats();
	
	boolean isClusterRoot();
	
	default void clusterSearch(Integer id, final Object2IntMap<IFissionComponent> clusterSearchCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
		if (!isValidHeatConductor(componentFailCache, assumedValidCache, simulate)) {
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
				TileEntity tile = getTileWorld().getTileEntity(offPos);
				if (tile instanceof TileFissionPart part && part.getPartPositionType().isGoodForWall()) {
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
	
	default void addToComponentFailCache(final Long2ObjectMap<IFissionComponent> componentFailCache) {
		componentFailCache.put(getTilePos().toLong(), this);
	}
	
	long getHeatStored();
	
	void setHeatStored(long heat);
	
	void onClusterMeltdown(Iterator<IFissionComponent> componentIterator);
	
	boolean isNullifyingSources(EnumFacing side, boolean simulate);
	
	// Moderator Line
	
	default ModeratorBlockInfo getModeratorBlockInfo(EnumFacing dir, boolean validActiveModeratorPos) {
		return null;
	}
	
	/**
	 * The moderator line does not necessarily have to be complete!
	 */
	default void onAddedToModeratorCache(ModeratorBlockInfo thisInfo) {}
	
	/**
	 * Called if and only if the moderator line from the fuel component searching in the dir direction is complete!
	 */
	default void onModeratorLineComplete(ModeratorLine line, ModeratorBlockInfo thisInfo, EnumFacing dir) {}
	
	/**
	 * Called during cluster searches!
	 */
	default boolean isActiveModerator() {
		return false;
	}
	
	// IMultitoolLogic
	
	@Override
	default boolean onUseMultitool(ItemStack multitool, EntityPlayerMP player, World world, EnumFacing facing, float hitX, float hitY, float hitZ) {
		NBTTagCompound nbt = NBTHelper.getStackNBT(multitool, "ncMultitool");
		if (nbt != null) {
			if (player.isSneaking()) {
				NBTTagCompound info = new NBTTagCompound();
				String displayName = getTileBlockDisplayName();
				info.setString("displayName", displayName);
				info.setLong("componentPos", getTilePos().toLong());
				player.sendMessage(new TextComponentString(Lang.localize("info.nuclearcraft.multitool.save_component_info", displayName)));
				nbt.setTag("fissionComponentInfo", info);
				return true;
			}
		}
		return IFissionPart.super.onUseMultitool(multitool, player, world, facing, hitX, hitY, hitZ);
	}
	
	// OpenComputers
	
	String getOCKey();
	
	Object getOCInfo();
}
