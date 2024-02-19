package nc.tile;

import nc.container.ContainerFunction;
import nc.gui.GuiFunction;
import nc.handler.GuiHandler;
import nc.util.Lazy.LazyInt;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

public class TileContainerInfo<TILE extends TileEntity> {
	
	public final String modId;
	public final String name;
	
	public final Class<? extends Container> containerClass;
	protected final ContainerFunction<TILE> containerFunction;
	
	public final Class<? extends GuiContainer> guiClass;
	protected final GuiFunction<TILE> guiFunction;
	
	protected final LazyInt guiId;
	
	public TileContainerInfo(String modId, String name, Class<? extends Container> containerClass, ContainerFunction<TILE> containerFunction, Class<? extends GuiContainer> guiClass, GuiFunction<TILE> guiFunction) {
		this.modId = modId;
		this.name = name;
		
		this.containerClass = containerClass;
		this.containerFunction = containerFunction;
		
		this.guiClass = guiClass;
		this.guiFunction = guiFunction;
		
		guiId = new LazyInt(() -> GuiHandler.getGuiId(name));
	}
	
	public Object getNewContainer(int id, EntityPlayer player, TILE tile) {
		return containerFunction.apply(player, tile);
	}
	
	public Object getNewGui(int id, EntityPlayer player, TILE tile) {
		return guiFunction.apply(player, tile);
	}
	
	public int getGuiId() {
		return guiId.get();
	}
}
