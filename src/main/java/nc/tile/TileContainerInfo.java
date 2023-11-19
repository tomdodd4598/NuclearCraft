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
	
	public final Class<TILE> tileClass;
	
	public final Class<? extends Container> containerClass;
	protected final ContainerFunction<TILE> containerFunction;
	
	public final Class<? extends GuiContainer> guiClass;
	protected final GuiFunction<TILE> guiFunction;
	
	protected final LazyInt guiId;
	
	public TileContainerInfo(String modId, String name, Class<TILE> tileClass, Class<? extends Container> containerClass, ContainerFunction<TILE> containerFunction, Class<? extends GuiContainer> guiClass, GuiFunction<TILE> guiFunction) {
		this.modId = modId;
		this.name = name;
		
		this.tileClass = tileClass;
		
		this.containerClass = containerClass;
		this.containerFunction = containerFunction;
		
		this.guiClass = guiClass;
		this.guiFunction = guiFunction;
		
		guiId = new LazyInt(() -> GuiHandler.getGuiId(name));
	}
	
	public Object getNewContainer(int ID, EntityPlayer player, TILE tile) {
		return containerFunction.apply(player, tile);
	}
	
	public Object getNewGui(int ID, EntityPlayer player, TILE tile) {
		return guiFunction.apply(player, tile);
	}
	
	public int getGuiId() {
		return guiId.get();
	}
}
