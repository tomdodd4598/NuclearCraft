package nc.multiblock.fission.tile.port;

import static nc.util.BlockPosHelper.DEFAULT_NON;

import java.util.Set;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.multiblock.fission.tile.TileFissionIrradiator;
import nc.multiblock.network.FissionIrradiatorPortUpdatePacket;
import nc.recipe.NCRecipes;
import nc.tile.ITileGui;
import net.minecraft.entity.player.EntityPlayer;

public class TileFissionIrradiatorPort extends TileFissionItemPort<TileFissionIrradiatorPort, TileFissionIrradiator> implements ITileGui<FissionIrradiatorPortUpdatePacket> {
	
	protected Set<EntityPlayer> playersToUpdate;
	
	public TileFissionIrradiatorPort() {
		super(TileFissionIrradiatorPort.class, "irradiator", NCRecipes.fission_irradiator);
		
		playersToUpdate = new ObjectOpenHashSet<>();
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
		return 300;
	}
	
	@Override
	public Set<EntityPlayer> getPlayersToUpdate() {
		return playersToUpdate;
	}
	
	@Override
	public FissionIrradiatorPortUpdatePacket getGuiUpdatePacket() {
		return new FissionIrradiatorPortUpdatePacket(pos, masterPortPos, getFilterStacks());
	}
	
	@Override
	public void onGuiPacket(FissionIrradiatorPortUpdatePacket message) {
		masterPortPos = message.masterPortPos;
		if (DEFAULT_NON.equals(masterPortPos) ^ masterPort == null) {
			refreshMasterPort();
		}
		getFilterStacks().set(0, message.filterStack);
	}
}
