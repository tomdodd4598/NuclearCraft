package nc.multiblock.fission.tile.manager;

import it.unimi.dsi.fastutil.longs.*;
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
	public int[] weakSidesToCheck(World worldIn, BlockPos posIn) {
		return new int[] {2, 3, 4, 5};
	}
	
	public boolean isShieldingActive() {
		return getIsRedstonePowered();
	}
	
	@Override
	public void refreshManager() {
		refreshListeners(true);
	}
	
	// TODO - temporary managers
	@Override
	public void refreshListeners(boolean refreshPosSet) {
		refreshListenersFlag = false;
		if (getMultiblock() == null) {
			return;
		}
		
		boolean refresh = false;
		LongSet invalidPosSet = new LongOpenHashSet();
		for (Long listenerPos : listenerPosSet) {
			TileFissionShield shield = getMultiblock().getPartMap(TileFissionShield.class).get(listenerPos);
			if (shield != null) {
				if (shield.onManagerRefresh(this)) {
					refresh = true;
				}
			}
			else if (refreshPosSet) {
				invalidPosSet.add(listenerPos);
			}
		}
		listenerPosSet.removeAll(invalidPosSet);
		
		if (refresh) {
			getMultiblock().refreshFlag = true;
		}
	}
	
	@Override
	public void onBlockNeighborChanged(IBlockState state, World worldIn, BlockPos posIn, BlockPos fromPos) {
		boolean wasShieldingActive = isShieldingActive();
		super.onBlockNeighborChanged(state, worldIn, posIn, fromPos);
		setActivity(isShieldingActive());
		if (!worldIn.isRemote && wasShieldingActive != isShieldingActive()) {
			refreshListeners(false);
		}
	}
	
	// IMultitoolLogic
	
	@Override
	public boolean onUseMultitool(ItemStack multitool, EntityPlayer player, World worldIn, EnumFacing facing, float hitX, float hitY, float hitZ) {
		// TODO
		if (player.isSneaking()) {
			
		}
		else {
			if (getMultiblock() != null) {
				listenerPosSet.clear();
				for (TileFissionShield shield : getMultiblock().getParts(TileFissionShield.class)) {
					listenerPosSet.add(shield.getPos().toLong());
					shield.setManagerPos(pos);
					shield.refreshManager();
				}
				markDirty();
				getMultiblock().refreshFlag = true;
				player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.fission.connect_shield_manager", listenerPosSet.size())));
				return true;
			}
		}
		return super.onUseMultitool(multitool, player, worldIn, facing, hitX, hitY, hitZ);
	}
}
