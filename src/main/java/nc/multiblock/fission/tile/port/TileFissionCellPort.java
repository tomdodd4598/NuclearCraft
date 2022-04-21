package nc.multiblock.fission.tile.port;

import static nc.util.PosHelper.DEFAULT_NON;

import java.util.Set;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.multiblock.fission.solid.tile.TileSolidFissionCell;
import nc.network.multiblock.FissionCellPortUpdatePacket;
import nc.recipe.NCRecipes;
import nc.tile.ITileGui;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.player.EntityPlayer;

public class TileFissionCellPort extends TileFissionItemPort<TileFissionCellPort, TileSolidFissionCell> implements ITileGui<FissionCellPortUpdatePacket> {
	
	protected Set<EntityPlayer> updatePacketListeners;
	
	public TileFissionCellPort() {
		super(TileFissionCellPort.class, "cell", NCRecipes.solid_fission);
		
		updatePacketListeners = new ObjectOpenHashSet<>();
	}
	
	@Override
	public Object getFilterKey() {
		return getFilterStacks().get(0).isEmpty() ? 0 : RecipeItemHelper.pack(getFilterStacks().get(0));
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
		return 301;
	}
	
	@Override
	public Set<EntityPlayer> getTileUpdatePacketListeners() {
		return updatePacketListeners;
	}
	
	@Override
	public FissionCellPortUpdatePacket getTileUpdatePacket() {
		return new FissionCellPortUpdatePacket(pos, masterPortPos, getFilterStacks());
	}
	
	@Override
	public void onTileUpdatePacket(FissionCellPortUpdatePacket message) {
		masterPortPos = message.masterPortPos;
		if (DEFAULT_NON.equals(masterPortPos) ^ masterPort == null) {
			refreshMasterPort();
		}
		getFilterStacks().set(0, message.filterStack);
	}
}
