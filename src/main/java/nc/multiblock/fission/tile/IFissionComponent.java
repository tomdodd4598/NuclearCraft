package nc.multiblock.fission.tile;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import nc.multiblock.fission.FissionCluster;
import nc.multiblock.fission.salt.tile.TileSaltFissionHeater;
import nc.multiblock.fission.salt.tile.TileSaltFissionVessel;
import nc.multiblock.fission.solid.tile.TileSolidFissionCell;
import nc.multiblock.fission.solid.tile.TileSolidFissionSink;
import nc.multiblock.fission.tile.IFissionFuelComponent.ModeratorBlockInfo;
import nc.recipe.NCRecipes;
import nc.util.Lang;
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
	
	/** Called during cluster searches! */
	public default boolean isActiveModerator() {
		return false;
	}
	
	public void resetStats();
	
	public boolean isClusterRoot();
	
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
			cluster = newCluster(id);
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
	
	// Moderator Lines
	
	public default ModeratorBlockInfo getModeratorBlockInfo(EnumFacing dir, boolean activeModeratorPos) {
		return null;
	}
	
	/** The moderator line does not necessarily have to be active! */
	public default void onAddedToModeratorCache(ModeratorBlockInfo info) {}
	
	/** Called if and only if the moderator line from the fuel component searching in the dir direction is active! */
	public default void onModeratorLineComplete(ModeratorBlockInfo info, EnumFacing dir, int flux) {}
	
	// IMultitoolLogic
	
	@Override
	public default boolean onUseMultitool(ItemStack multitoolStack, EntityPlayer player, World world, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player.isSneaking()) {
			NBTTagCompound nbt = multitoolStack.getTagCompound();
			nbt.setLong("componentPos", getTilePos().toLong());
			player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.copy_component_info")));
			return true;
		}
		else {
			
		}
		return IFissionPart.super.onUseMultitool(multitoolStack, player, world, facing, hitX, hitY, hitZ);
	}
	
	// Helper methods
	
	public default boolean isActiveModerator(BlockPos pos) {
		IFissionComponent component = getMultiblock().getPartMap(IFissionComponent.class).get(pos.toLong());
		return (component != null && component.isActiveModerator()) || (getMultiblock().activeModeratorCache.contains(pos.toLong()) && blockRecipe(NCRecipes.fission_moderator, pos) != null);
	}
	
	public default boolean isActiveReflector(BlockPos pos) {
		return getMultiblock().activeReflectorCache.contains(pos.toLong()) && blockRecipe(NCRecipes.fission_reflector, pos) != null;
	}
	
	public default boolean isActiveCell(BlockPos pos) {
		TileSolidFissionCell cell = getMultiblock().getPartMap(TileSolidFissionCell.class).get(pos.toLong());
		return cell == null ? false : cell.isFunctional();
	}
	
	public default boolean isActiveSink(BlockPos pos, String sinkName) {
		TileSolidFissionSink sink = getMultiblock().getPartMap(TileSolidFissionSink.class).get(pos.toLong());
		return sink == null ? false : sink.isFunctional() && sink.sinkName.equals(sinkName);
	}
	
	public default boolean isActiveVessel(BlockPos pos) {
		TileSaltFissionVessel vessel = getMultiblock().getPartMap(TileSaltFissionVessel.class).get(pos.toLong());
		return vessel == null ? false : vessel.isFunctional();
	}
	
	public default boolean isActiveHeater(BlockPos pos, String sinkName) {
		TileSaltFissionHeater heater = getMultiblock().getPartMap(TileSaltFissionHeater.class).get(pos.toLong());
		return heater == null ? false : heater.isFunctional() && heater.heaterName.equals(sinkName);
	}
	
	public default boolean isWall(BlockPos pos) {
		TileEntity part = getTileWorld().getTileEntity(pos);
		return part instanceof TileFissionPart && ((TileFissionPart)part).getPartPositionType().isGoodForWall();
	}
}
