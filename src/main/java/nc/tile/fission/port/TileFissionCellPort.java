package nc.tile.fission.port;

import static nc.util.PosHelper.DEFAULT_NON;

import java.util.Set;
import java.util.stream.IntStream;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.container.ContainerFunction;
import nc.gui.*;
import nc.handler.TileInfoHandler;
import nc.network.tile.multiblock.port.ItemPortUpdatePacket;
import nc.recipe.NCRecipes;
import nc.tile.*;
import nc.tile.fission.TileSolidFissionCell;
import nc.tile.fission.port.TileFissionCellPort.FissionCellPortContainerInfo;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class TileFissionCellPort extends TileFissionItemPort<TileFissionCellPort, TileSolidFissionCell> implements ITileGui<TileFissionCellPort, ItemPortUpdatePacket, FissionCellPortContainerInfo> {
	
	public static class FissionCellPortContainerInfo extends TileContainerInfo<TileFissionCellPort> {
		
		public FissionCellPortContainerInfo(String modId, String name, Class<? extends Container> containerClass, ContainerFunction<TileFissionCellPort> containerFunction, Class<? extends GuiContainer> guiClass, GuiInfoTileFunction<TileFissionCellPort> guiFunction) {
			super(modId, name, containerClass, containerFunction, guiClass, GuiFunction.of(modId, name, containerFunction, guiFunction));
		}
	}
	
	protected final FissionCellPortContainerInfo info = TileInfoHandler.getTileContainerInfo("fission_cell_port");
	
	protected final Set<EntityPlayer> updatePacketListeners = new ObjectOpenHashSet<>();
	
	public TileFissionCellPort() {
		super(TileFissionCellPort.class, "cell", NCRecipes.solid_fission);
	}
	
	@Override
	public FissionCellPortContainerInfo getContainerInfo() {
		return info;
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
	public Set<EntityPlayer> getTileUpdatePacketListeners() {
		return updatePacketListeners;
	}
	
	@Override
	public ItemPortUpdatePacket getTileUpdatePacket() {
		return new ItemPortUpdatePacket(pos, masterPortPos, getFilterStacks());
	}
	
	@Override
	public void onTileUpdatePacket(ItemPortUpdatePacket message) {
		masterPortPos = message.masterPortPos;
		if (DEFAULT_NON.equals(masterPortPos) ^ masterPort == null) {
			refreshMasterPort();
		}
		IntStream.range(0, filterStacks.size()).forEach(x -> filterStacks.set(x, message.filterStacks.get(x)));
	}
}
