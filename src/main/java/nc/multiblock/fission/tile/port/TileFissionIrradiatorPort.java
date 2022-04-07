package nc.multiblock.fission.tile.port;

import static nc.util.PosHelper.DEFAULT_NON;

import java.util.Set;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.multiblock.fission.tile.TileFissionIrradiator;
import nc.network.multiblock.FissionIrradiatorPortUpdatePacket;
import nc.recipe.NCRecipes;
import nc.tile.ITileGui;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.player.EntityPlayer;

public class TileFissionIrradiatorPort extends TileFissionItemPort<TileFissionIrradiatorPort, TileFissionIrradiator> implements ITileGui<FissionIrradiatorPortUpdatePacket> {
	
	protected Set<EntityPlayer> updatePacketListeners;
	
	public TileFissionIrradiatorPort() {
		super(TileFissionIrradiatorPort.class, "irradiator", NCRecipes.fission_irradiator);
		
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
		return 300;
	}
	
	@Override
	public Set<EntityPlayer> getTileUpdatePacketListeners() {
		return updatePacketListeners;
	}
	
	@Override
	public FissionIrradiatorPortUpdatePacket getTileUpdatePacket() {
		return new FissionIrradiatorPortUpdatePacket(pos, masterPortPos, getFilterStacks());
	}
	
	@Override
	public void onTileUpdatePacket(FissionIrradiatorPortUpdatePacket message) {
		masterPortPos = message.masterPortPos;
		if (DEFAULT_NON.equals(masterPortPos) ^ masterPort == null) {
			refreshMasterPort();
		}
		getFilterStacks().set(0, message.filterStack);
	}
}
