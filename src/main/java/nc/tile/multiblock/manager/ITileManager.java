package nc.tile.multiblock.manager;

import it.unimi.dsi.fastutil.longs.*;
import nc.multiblock.*;
import nc.render.BlockHighlightTracker;
import nc.tile.multiblock.ITileLogicMultiblockPart;
import nc.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public interface ITileManager<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & ILogicMultiblock<MULTIBLOCK, LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>, MANAGER extends ITileManager<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER>, LISTENER extends ITileManagerListener<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER>> extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T> {
	
	LongSet getListenerPosSet();
	
	boolean getRefreshListenersFlag();
	
	void setRefreshListenersFlag(boolean flag);
	
	default void refreshManager() {
		refreshListeners();
	}
	
	@SuppressWarnings("unchecked")
	default void refreshListeners() {
		setRefreshListenersFlag(false);
		
		MULTIBLOCK multiblock = getMultiblock();
		if (multiblock == null) {
			return;
		}
		
		boolean refresh = false;
		LongSet listenerPosSet = getListenerPosSet(), invalidPosSet = new LongOpenHashSet();
		for (Long listenerPos : listenerPosSet) {
			LISTENER listener = (LISTENER) multiblock.getPartMap(getListenerClass()).get(listenerPos);
			if (listener != null) {
				if (listener.onManagerRefresh((MANAGER) this)) {
					refresh = true;
				}
			}
			else {
				invalidPosSet.add(listenerPos);
			}
		}
		
		listenerPosSet.removeAll(invalidPosSet);
		
		if (refresh) {
			refreshMultiblock();
		}
	}
	
	void refreshMultiblock();
	
	String getManagerType();
	
	Class<? extends T> getListenerClass();
	
	boolean isManagerActive();
	
	@Override
	default void onBlockNeighborChanged(IBlockState state, World worldIn, BlockPos posIn, BlockPos fromPos) {
		boolean wasActive = isManagerActive();
		ITileLogicMultiblockPart.super.onBlockNeighborChanged(state, worldIn, posIn, fromPos);
		setActivity(isManagerActive());
		if (!worldIn.isRemote && wasActive != isManagerActive()) {
			refreshListeners();
		}
	}
	
	// IMultitoolLogic
	
	@SuppressWarnings("unchecked")
	@Override
	default boolean onUseMultitool(ItemStack multitool, EntityPlayerMP player, World world, EnumFacing facing, float hitX, float hitY, float hitZ) {
		NBTTagCompound nbt = NBTHelper.getStackNBT(multitool, "ncMultitool");
		if (nbt != null) {
			MULTIBLOCK multiblock;
			if (player.isSneaking()) {
				NBTTagCompound info = new NBTTagCompound();
				String displayName = getTileBlockDisplayName();
				info.setString("managerType", getManagerType());
				info.setString("displayName", displayName);
				info.setLong("managerPos", getTilePos().toLong());
				info.setInteger("listenerCount", 0);
				player.sendMessage(new TextComponentString(Lang.localize("info.nuclearcraft.multitool.start_manager_listener_set", displayName)));
				nbt.setTag("componentManagerInfo", info);
				return true;
			}
			else if ((multiblock = getMultiblock()) != null) {
				if (nbt.hasKey("componentManagerInfo", 10)) {
					NBTTagCompound info = nbt.getCompoundTag("componentManagerInfo");
					if (info.getLong("managerPos") == getTilePos().toLong()) {
						String displayName = getTileBlockDisplayName();
						if (info.getString("managerType").equals(getManagerType())) {
							LongSet listenerPosSet = getListenerPosSet();
							listenerPosSet.clear();
							Long2ObjectMap<? extends T> partMap = multiblock.getPartMap(getListenerClass());
							int listenerCount = info.getInteger("listenerCount");
							if (listenerCount <= 0) {
								for (Long2ObjectMap.Entry<? extends T> entry : partMap.long2ObjectEntrySet()) {
									listenerPosSet.add(entry.getLongKey());
									onAddListener((LISTENER) entry.getValue());
								}
							}
							else {
								for (int i = 0; i < listenerCount; ++i) {
									long listenerPosLong = info.getLong("listenerPos" + i);
									if (partMap.containsKey(listenerPosLong)) {
										listenerPosSet.add(listenerPosLong);
										onAddListener((LISTENER) partMap.get(listenerPosLong));
									}
								}
							}
							onSetListeners();
							BlockHighlightTracker.sendPacket(player, listenerPosSet, 5000);
							player.sendMessage(new TextComponentString(Lang.localize("info.nuclearcraft.multitool.finish_manager_listener_set", displayName, listenerPosSet.size())));
							nbt.removeTag("componentManagerInfo");
							return true;
						}
						else {
							player.sendMessage(new TextComponentString(Lang.localize("info.nuclearcraft.multitool.invalid_component_info", info.getString("displayName"), displayName)));
						}
					}
				}
				else {
					LongSet listenerPosSet = getListenerPosSet();
					BlockHighlightTracker.sendPacket(player, listenerPosSet, 5000);
					player.sendMessage(new TextComponentString(Lang.localize("info.nuclearcraft.multitool.manager_listener_info", getTileBlockDisplayName(), listenerPosSet.size())));
					return true;
				}
			}
		}
		return ITileLogicMultiblockPart.super.onUseMultitool(multitool, player, world, facing, hitX, hitY, hitZ);
	}
	
	default void onAddListener(LISTENER listener) {
		listener.setManagerPos(getTilePos());
		listener.refreshManager();
	}
	
	default void onSetListeners() {
		markTileDirty();
		refreshMultiblock();
	}
}
