package nc.multiblock.fission.tile.port;

import static nc.util.BlockPosHelper.DEFAULT_NON;

import java.util.Set;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.multiblock.fission.salt.tile.TileSaltFissionVessel;
import nc.multiblock.network.FissionVesselPortUpdatePacket;
import nc.recipe.NCRecipes;
import nc.tile.ITileGui;
import nc.util.FluidStackHelper;
import net.minecraft.entity.player.EntityPlayer;

public class TileFissionVesselPort extends TileFissionFluidPort<TileFissionVesselPort, TileSaltFissionVessel> implements ITileGui<FissionVesselPortUpdatePacket> {
	
	protected Set<EntityPlayer> playersToUpdate;
	
	public TileFissionVesselPort() {
		super(TileFissionVesselPort.class, FluidStackHelper.INGOT_BLOCK_VOLUME, NCRecipes.salt_fission_valid_fluids.get(0), NCRecipes.salt_fission);
		
		playersToUpdate = new ObjectOpenHashSet<>();
	}
	
	@Override
	public int getTankCapacityPerConnection() {
		return 36;
	}
	
	// Ticking
	
	@Override
	public void update() {
		super.update();
		if (!world.isRemote) {
			sendUpdateToListeningPlayers();
		}
	}
	
	// ITileGui
	
	@Override
	public int getGuiID() {
		return 302;
	}
	
	@Override
	public Set<EntityPlayer> getPlayersToUpdate() {
		return playersToUpdate;
	}
	
	@Override
	public FissionVesselPortUpdatePacket getGuiUpdatePacket() {
		return new FissionVesselPortUpdatePacket(pos, masterPortPos, getTanks(), getFilterTanks());
	}
	
	@Override
	public void onGuiPacket(FissionVesselPortUpdatePacket message) {
		masterPortPos = message.masterPortPos;
		if (DEFAULT_NON.equals(masterPortPos) ^ masterPort == null) {
			refreshMasterPort();
		}
		for (int i = 0; i < getTanks().size(); i++) {
			getTanks().get(i).readInfo(message.tanksInfo.get(i));
		}
		for (int i = 0; i < getFilterTanks().size(); i++) {
			getFilterTanks().get(i).readInfo(message.filterTanksInfo.get(i));
		}
	}
}
