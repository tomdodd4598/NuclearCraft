package nc.tile;

import nc.container.ContainerFunction;
import nc.gui.GuiFunction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class TileContainerInfo<TILE extends TileEntity> {
	
	public final String modId;
	public final String name;
	
	protected final ContainerFunction<TILE> containerFunction;
	protected final GuiFunction<TILE> guiFunction;
	
	public TileContainerInfo(String modId, String name, ContainerFunction<TILE> containerFunction, GuiFunction<TILE> guiFunction) {
		this.modId = modId;
		this.name = name;
		this.containerFunction = containerFunction;
		this.guiFunction = guiFunction;
	}
	
	public Object getNewContainer(int ID, EntityPlayer player, TILE tile) {
		return containerFunction.apply(player, tile);
	}
	
	public Object getNewGui(int ID, EntityPlayer player, TILE tile) {
		return guiFunction.apply(player, tile);
	}
}
