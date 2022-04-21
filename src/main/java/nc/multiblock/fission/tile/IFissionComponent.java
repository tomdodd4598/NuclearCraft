package nc.multiblock.fission.tile;

import java.util.Iterator;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import nc.multiblock.fission.FissionCluster;
import nc.multiblock.fission.tile.IFissionFuelComponent.*;
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
	
	public @Nullable FissionCluster getCluster();
	
	public default FissionCluster newCluster(int id) {
		return new FissionCluster(getMultiblock(), id);
	}
	
	public default void setCluster(@Nullable FissionCluster cluster) {
		if (cluster == null && getCluster() != null) {
			// getCluster().getComponentMap().remove(pos.toLong());
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
	public boolean isValidHeatConductor(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache);
	
	public boolean isFunctional();
	
	public void resetStats();
	
	public boolean isClusterRoot();
	
	public default void clusterSearch(Integer id, final Object2IntMap<IFissionComponent> clusterSearchCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
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
	
	public long getHeatStored();
	
	public void setHeatStored(long heat);
	
	public void onClusterMeltdown(Iterator<IFissionComponent> componentIterator);
	
	public boolean isNullifyingSources(EnumFacing side);
	
	// Moderator Line
	
	public default ModeratorBlockInfo getModeratorBlockInfo(EnumFacing dir, boolean validActiveModeratorPos) {
		return null;
	}
	
	/** The moderator line does not necessarily have to be complete! */
	public default void onAddedToModeratorCache(ModeratorBlockInfo thisInfo) {}
	
	/** Called if and only if the moderator line from the fuel component searching in the dir direction is complete! */
	public default void onModeratorLineComplete(ModeratorLine line, ModeratorBlockInfo thisInfo, EnumFacing dir) {}
	
	/** Called during cluster searches! */
	public default boolean isActiveModerator() {
		return false;
	}
	
	// IMultitoolLogic
	
	@Override
	public default boolean onUseMultitool(ItemStack multitool, EntityPlayer player, World world, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player.isSneaking()) {
			NBTTagCompound nbt = NBTHelper.getStackNBT(multitool);
			if (nbt != null) {
				nbt.setLong("componentPos", getTilePos().toLong());
				player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.copy_component_info")));
				return true;
			}
		}
		else {
			
		}
		return IFissionPart.super.onUseMultitool(multitool, player, world, facing, hitX, hitY, hitZ);
	}
}
