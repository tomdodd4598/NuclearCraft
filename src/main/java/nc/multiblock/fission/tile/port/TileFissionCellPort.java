package nc.multiblock.fission.tile.port;

import static nc.util.BlockPosHelper.DEFAULT_NON;

import java.util.Set;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.multiblock.fission.solid.tile.TileSolidFissionCell;
import nc.multiblock.network.FissionCellPortUpdatePacket;
import nc.recipe.NCRecipes;
import nc.tile.ITileGui;
import net.minecraft.entity.player.EntityPlayer;

public class TileFissionCellPort extends TileFissionItemPort<TileFissionCellPort, TileSolidFissionCell> implements ITileGui<FissionCellPortUpdatePacket> {
	
	protected Set<EntityPlayer> playersToUpdate;
	
	public TileFissionCellPort() {
		super(TileFissionCellPort.class, "cell", NCRecipes.solid_fission);
		
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
		return 301;
	}
	
	@Override
	public Set<EntityPlayer> getPlayersToUpdate() {
		return playersToUpdate;
	}
	
	@Override
	public FissionCellPortUpdatePacket getGuiUpdatePacket() {
		return new FissionCellPortUpdatePacket(pos, masterPortPos, getFilterStacks());
	}
	
	@Override
	public void onGuiPacket(FissionCellPortUpdatePacket message) {
		masterPortPos = message.masterPortPos;
		if (DEFAULT_NON.equals(masterPortPos) ^ masterPort == null) {
			refreshMasterPort();
		}
		getFilterStacks().set(0, message.filterStack);
	}
}
