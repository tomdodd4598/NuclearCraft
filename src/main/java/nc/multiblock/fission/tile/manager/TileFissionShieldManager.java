package nc.multiblock.fission.tile.manager;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import nc.multiblock.fission.block.manager.BlockFissionShieldManager;
import nc.multiblock.fission.tile.TileFissionShield;
import nc.util.Lang;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class TileFissionShieldManager extends TileFissionManager<TileFissionShieldManager, TileFissionShield> {
	
	public TileFissionShieldManager() {
		super(TileFissionShieldManager.class);
	}
	
	@Override
	public int[] weakSidesToCheck(World world, BlockPos pos) {
		return new int[] {2, 3, 4, 5};
	}
	
	public boolean isShieldingActive() {
		return getIsRedstonePowered();
	}
	
	@Override
	public void moveListenersFromCache() {
		if (!listenerPosCache.isEmpty()) {
			for (long posLong : listenerPosCache) {
				TileFissionShield shield = getMultiblock().getPartMap(TileFissionShield.class).get(posLong);
				if (shield != null) {
					listeners.add(shield);
					shield.setMasterManagerPos(pos);
					shield.refreshMasterManager();
				}
			}
			listenerPosCache.clear();
			markTileDirty();
		}
	}
	
	//TODO - Temporary shield manager connections
	@Override
	public void refreshManager() {
		moveListenersFromCache();
		refreshListeners();
	}
	
	//TODO - temporary managers
	@Override
	public void refreshListeners() {
		refreshPartsFlag = false;
		//if (isMultiblockAssembled()) {
			boolean refresh = false;
			for (TileFissionShield shield : listeners) {
				if (shield.onManagerRefresh()) refresh = true;
			}
			if (refresh) {
				getMultiblock().refreshFlag = true;
			}
		//}
	}
	
	@Override
	public void onBlockNeighborChanged(IBlockState state, World world, BlockPos pos, BlockPos fromPos) {
		boolean wasShieldingActive = isShieldingActive();
		super.onBlockNeighborChanged(state, world, pos, fromPos);
		updateBlockState(isShieldingActive());
		if (!world.isRemote && wasShieldingActive != isShieldingActive()) {
			refreshListeners();
		}
	}
	
	public void updateBlockState(boolean isActive) {
		if (getBlockType() instanceof BlockFissionShieldManager) {
			((BlockFissionShieldManager)getBlockType()).setState(isActive, this);
			//world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
		}
	}
	
	//IMultitoolLogic
	
	@Override
	public boolean onUseMultitool(ItemStack multitoolStack, EntityPlayer player, World world, EnumFacing facing, float hitX, float hitY, float hitZ) {
		//TODO
		if (player.isSneaking()) {
			
		}
		else {
			if (getMultiblock() != null) {
				Long2ObjectMap<TileFissionShield> shieldMap = getMultiblock().getPartMap(TileFissionShield.class);
				for (TileFissionShield shield : shieldMap.values()) {
					getListeners().add(shield);
					shield.setMasterManagerPos(pos);
					shield.refreshMasterManager();
				}
				markDirtyAndNotify();
				getMultiblock().refreshFlag = true;
				player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.fission.connect_shield_manager", shieldMap.size())));
				return true;
			}
		}
		return super.onUseMultitool(multitoolStack, player, world, facing, hitX, hitY, hitZ);
	}
}
