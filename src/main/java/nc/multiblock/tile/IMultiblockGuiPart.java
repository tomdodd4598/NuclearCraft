package nc.multiblock.tile;

import nc.multiblock.Multiblock;
import nc.multiblock.container.ContainerMultiblockController;
import net.minecraft.entity.player.EntityPlayer;

public interface IMultiblockGuiPart<MULTIBLOCK extends Multiblock> extends ITileMultiblockPart<MULTIBLOCK> {
	
	public <TILE extends IMultiblockGuiPart<MULTIBLOCK>> ContainerMultiblockController<MULTIBLOCK, TILE> getContainer(EntityPlayer player);
}
