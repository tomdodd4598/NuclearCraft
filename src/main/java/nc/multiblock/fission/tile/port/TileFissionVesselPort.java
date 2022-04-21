package nc.multiblock.fission.tile.port;

import static nc.util.FluidStackHelper.INGOT_BLOCK_VOLUME;
import static nc.util.PosHelper.DEFAULT_NON;

import java.util.Set;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.multiblock.fission.salt.tile.TileSaltFissionVessel;
import nc.network.multiblock.FissionVesselPortUpdatePacket;
import nc.recipe.NCRecipes;
import nc.tile.ITileGui;
import net.minecraft.entity.player.EntityPlayer;

public class TileFissionVesselPort extends TileFissionFluidPort<TileFissionVesselPort, TileSaltFissionVessel> implements ITileGui<FissionVesselPortUpdatePacket> {
	
	protected Set<EntityPlayer> updatePacketListeners;
	
	public TileFissionVesselPort() {
		super(TileFissionVesselPort.class, INGOT_BLOCK_VOLUME, NCRecipes.salt_fission_valid_fluids.get(0), NCRecipes.salt_fission);
		
		updatePacketListeners = new ObjectOpenHashSet<>();
	}
	
	@Override
	public int getTankCapacityPerConnection() {
		return 36;
	}
	
	@Override
	public Object getFilterKey() {
		return getFilterTanks().get(0).getFluidName();
	}
	
	// Ticking
	
	@Override
	public void update() {
		super.update();
		if (!world.isRemote) {
			sendTileUpdatePacketToListeners();
		}
	}
	
	// ITileGui
	
	@Override
	public int getGuiID() {
		return 302;
	}
	
	@Override
	public Set<EntityPlayer> getTileUpdatePacketListeners() {
		return updatePacketListeners;
	}
	
	@Override
	public FissionVesselPortUpdatePacket getTileUpdatePacket() {
		return new FissionVesselPortUpdatePacket(pos, masterPortPos, getTanks(), getFilterTanks());
	}
	
	@Override
	public void onTileUpdatePacket(FissionVesselPortUpdatePacket message) {
		masterPortPos = message.masterPortPos;
		if (DEFAULT_NON.equals(masterPortPos) ^ masterPort == null) {
			refreshMasterPort();
		}
		for (int i = 0; i < getTanks().size(); ++i) {
			getTanks().get(i).readInfo(message.tanksInfo.get(i));
		}
		for (int i = 0; i < getFilterTanks().size(); ++i) {
			getFilterTanks().get(i).readInfo(message.filterTanksInfo.get(i));
		}
	}
}
